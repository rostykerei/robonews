package nl.rostykerei.news.service.freebase.google;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
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
    public FreebaseSearchResult searchForPerson(String person) throws FreebaseServiceException {
        return searchFreebase(person, "(any+type:/people/person)");
    }

    @Override
    public FreebaseSearchResult searchForLocation(String location) throws FreebaseServiceException {
        return searchFreebase(location, "(any+type:/location/)");
    }

    @Override
    public FreebaseSearchResult searchForOrganization(String organization) throws FreebaseServiceException {
        return searchFreebase(organization, "(should+type:%22organization%22)");
    }

    @Override
    public FreebaseSearchResult searchForMiscellaneous(String misc) throws FreebaseServiceException {
        return searchFreebase(misc, null);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private FreebaseSearchResult searchFreebase(String query, String filter) throws FreebaseServiceException {

        StringBuilder url = new StringBuilder("https://www.googleapis.com/freebase/v1/search").
                                    append("?query=").
                                    append(urlEncode(query));

        if (!StringUtils.isEmpty(filter)) {
            url.append("&filter=").append(filter);
        }

        if (!StringUtils.isEmpty(getApiKey())) {
            url.append("&key=").append(getApiKey());
        }

        url.append("&lang=en").append("&limit=1");

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

                    return ret;
                }
                else {
                    throw new NotFoundException();
                }
            }
            else {
                throw new ServiceUnavailableException();
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
        }
   }
}
