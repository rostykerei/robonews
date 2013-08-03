package nl.rostykerei.news.manager.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class FeedUrlDto {

    @NotNull
    @Size(min = 1, max = 255, message = "URL cannot be empty")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
