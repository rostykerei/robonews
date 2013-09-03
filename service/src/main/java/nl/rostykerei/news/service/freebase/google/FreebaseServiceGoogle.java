package nl.rostykerei.news.service.freebase.google;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import nl.rostykerei.news.service.core.NamedEntityType;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.exception.AmbiguousResultException;
import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.exception.NotFoundException;
import nl.rostykerei.news.service.freebase.exception.ParserException;
import nl.rostykerei.news.service.freebase.exception.ServiceUnavailableException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class FreebaseServiceGoogle implements FreebaseService {

    private HttpService httpService;

    private String apiKey;

    private Logger logger = LoggerFactory.getLogger(FreebaseServiceGoogle.class);

    public FreebaseServiceGoogle(HttpService httpService) {
        this.httpService = httpService;
    }

    @Override
    public FreebaseSearchResult search(NamedEntity namedEntity) throws FreebaseServiceException {
        switch (namedEntity.getType()) {
            case PERSON:
                return searchFreebase(namedEntity, "(should+type:/people/person)");
            case LOCATION:
                return searchFreebase(namedEntity, "(should+type:/location/)");
            case ORGANIZATION:
                return searchFreebase(namedEntity, "(should+type:%22organization%22)");
            default:
                return searchFreebase(namedEntity, null);
        }
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private FreebaseSearchResult searchFreebase(NamedEntity namedEntity, String filter) throws FreebaseServiceException {

        StringBuilder url = new StringBuilder("https://www.googleapis.com/freebase/v1/search").
                                    append("?query=").
                                    append(urlEncode(namedEntity.getName()));

        if (!StringUtils.isEmpty(filter)) {
            url.append("&filter=").append(filter);
        }

        if (!StringUtils.isEmpty(getApiKey())) {
            url.append("&key=").append(getApiKey());
        }

        url.append("&lang=en").append("&limit=1").append("&output=(type)");

        HttpResponse httpResponse = null;

        try {
            HttpRequest httpRequest = new HttpRequestImpl(url.toString());
            httpRequest.setAccept("application/json");

            httpResponse = httpService.execute(httpRequest);

            if (httpResponse.getHttpStatus() == 200) {
                FreebaseData data = new Gson().fromJson( new InputStreamReader(httpResponse.getStream()), FreebaseData.class);

                if (data != null && data.getResult() != null && data.getResult().size() > 0) {
                    FreebaseData.Result result = data.getResult().get(0);

                    if (StringUtils.isEmpty(result.getName()) || StringUtils.isEmpty(result.getMid())) {
                        throw new AmbiguousResultException();
                    }

                    FreebaseSearchResult ret = new FreebaseSearchResult();
                    ret.setName(result.getName());
                    ret.setMid(result.getMid());
                    ret.setScore(result.getScore());
                    ret.setType(determinateType(result, namedEntity));

                    return ret;
                }
                else {
                    throw new NotFoundException();
                }
            }
            else {
                throw new ServiceUnavailableException("http error: " + httpResponse.getHttpStatus());
            }
        }
        catch (IOException e) {
            throw new ServiceUnavailableException();
        }
        catch (JsonParseException e) {
            logger.error("Could not parse Freebase response", e);
            throw new ParserException();
        }
        finally {
            synchronized (this) {
                if (httpResponse != null) {
                    httpResponse.releaseConnection();
                }
            }
        }
    }

    private String urlEncode(String input) throws FreebaseServiceException {
        try {
            return URLEncoder.encode(input, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            logger.error("Could not encode URL param " + input + ":", e);
            throw new FreebaseServiceException();
        }
    }

    private NamedEntityType determinateType(FreebaseData.Result result, NamedEntity namedEntity) {
        if (result.getOutput() != null &&
                result.getOutput().getType() != null &&
                    result.getOutput().getType().getObjectTypes() != null) {

            for (FreebaseData.Result.Output.Type.ObjectType ot : result.getOutput().getType().getObjectTypes()) {
                if ("/people/person".equals(ot.getId())) {
                    return NamedEntityType.PERSON;
                }
                else if ("/location/location".equals(ot.getId())) {
                    return NamedEntityType.LOCATION;
                }
                else if ("/organization/organization".equals(ot.getId())) {
                    return NamedEntityType.ORGANIZATION;
                }
            }

        }

        return namedEntity.getType();
    }


    class FreebaseData {

        private String status;

        private List<Result> result;

        private String getStatus() {
            return status;
        }

        private void setStatus(String status) {
            this.status = status;
        }

        List<Result> getResult() {
            return result;
        }

        void setResult(List<Result> result) {
            this.result = result;
        }

        class Result {

            private String mid;

            private String name;

            private float score;

            private Output output;

            String getMid() {
                return mid;
            }

            void setMid(String mid) {
                this.mid = mid;
            }

            String getName() {
                return name;
            }

            void setName(String name) {
                this.name = name;
            }

            float getScore() {
                return score;
            }

            void setScore(float score) {
                this.score = score;
            }

            Output getOutput() {
                return output;
            }

            void setOutput(Output output) {
                this.output = output;
            }

            class Output {

                private Type type;

                Type getType() {
                    return type;
                }

                void setType(Type type) {
                    this.type = type;
                }

                class Type {

                    @SerializedName("/type/object/type")
                    private List<ObjectType> objectTypes;

                    List<ObjectType> getObjectTypes() {
                        return objectTypes;
                    }

                    void setObjectTypes(List<ObjectType> objectTypes) {
                        this.objectTypes = objectTypes;
                    }

                    class ObjectType {
                        private String id;

                        String getId() {
                            return id;
                        }

                        void setId(String id) {
                            this.id = id;
                        }
                    }
                }
            }
        }
   }
}
