package nl.rostykerei.news.service.freebase.google;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.http.HttpRequest;
import nl.rostykerei.news.service.http.HttpResponse;
import nl.rostykerei.news.service.http.HttpService;
import nl.rostykerei.news.service.http.impl.HttpRequestImpl;
import org.springframework.util.StringUtils;

public class FreebaseServiceGoogle implements FreebaseService {

    private HttpService httpService;

    private String apiKey;

    private final String BASE_URL = "https://www.googleapis.com/freebase/v1/search";

    public FreebaseServiceGoogle(String apiKey, HttpService httpService) {
        this.apiKey = apiKey;
        this.httpService = httpService;
    }

    @Override
    public FreebaseSearchResult searchForPerson(String person) throws FreebaseServiceException {
        try {
            return searchFreebase(
                    new StringBuilder(BASE_URL).
                    append("?query=").
                    append(URLEncoder.encode(person, "UTF-8")).
                    append("&filter=").
                    append("(any+type:/people/person)").
                    append("&lang=en").
                    append("&limit=1").
                    append("&key=").
                    append(apiKey).
                    toString()
            );
        }
        catch (UnsupportedEncodingException e) {
            throw new FreebaseServiceException();
        }
    }

    @Override
    public FreebaseSearchResult searchForLocation(String location) throws FreebaseServiceException {
        try {
            return searchFreebase(
                    new StringBuilder(BASE_URL).
                            append("?query=").
                            append(URLEncoder.encode(location, "UTF-8")).
                            append("&filter=").
                            append("(any+type:/location/)").
                            append("&lang=en").
                            append("&limit=1").
                            append("&key=").
                            append(apiKey).
                            toString()
            );
        }
        catch (UnsupportedEncodingException e) {
            throw new FreebaseServiceException();
        }
    }

    @Override
    public FreebaseSearchResult searchForOrganization(String organization) throws FreebaseServiceException {
        try {
            return searchFreebase(
                    new StringBuilder(BASE_URL).
                            append("?query=").
                            append(URLEncoder.encode(organization, "UTF-8")).
                            append("&filter=").
                            append("(should+type:%22organization%22)").
                            append("&lang=en").
                            append("&limit=1").
                            append("&key=").
                            append(apiKey).
                            toString()
            );
        }
        catch (UnsupportedEncodingException e) {
            throw new FreebaseServiceException();
        }
    }

    @Override
    public FreebaseSearchResult searchForMiscellaneous(String misc) throws FreebaseServiceException {
        try {
            return searchFreebase(
                    new StringBuilder(BASE_URL).
                            append("?query=").
                            append(URLEncoder.encode(misc, "UTF-8")).
                            append("&lang=en").
                            append("&limit=1").
                            append("&key=").
                            append(apiKey).
                            toString()
            );
        }
        catch (UnsupportedEncodingException e) {
            throw new FreebaseServiceException();
        }
    }

    private FreebaseSearchResult searchFreebase(String url) throws FreebaseServiceException {

        HttpResponse httpResponse = null;

        try {
            HttpRequest httpRequest = new HttpRequestImpl(url);

            httpResponse = httpService.execute(httpRequest);

            if (httpResponse.getHttpStatus() == 200) {
                FreebaseData data = new Gson().fromJson( new InputStreamReader(httpResponse.getStream()), FreebaseData.class);

                if (data != null && data.getResult() != null && data.getResult().size() > 0) {
                    FreebaseData.Result result = data.getResult().get(0);

                    if (StringUtils.isEmpty(result.getName()) || StringUtils.isEmpty(result.getMid())) {
                        return null;
                    }

                    FreebaseSearchResult ret = new FreebaseSearchResult();
                    ret.setName(result.getName());
                    ret.setMid(result.getMid());
                    ret.setScore(result.getScore());

                    return ret;
                }
            }
            else {
                // TODO non 200
            }

        }
        catch (IOException e) {
            // TODO http exception
            throw new FreebaseServiceException();
        }
        catch (JsonParseException e) {
            // TODO parse exception
            throw new FreebaseServiceException();
        }
        finally {
            synchronized (this) {
                if (httpResponse != null) {
                    httpResponse.releaseConnection();
                }
            }
        }

        return null;
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
