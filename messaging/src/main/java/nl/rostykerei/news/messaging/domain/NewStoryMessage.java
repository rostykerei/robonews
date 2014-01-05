package nl.rostykerei.news.messaging.domain;

import java.util.UUID;

public class NewStoryMessage {

    private long id;

    private String[] foundKeywords;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String[] getFoundKeywords() {
        return foundKeywords;
    }

    public void setFoundKeywords(String[] foundKeywords) {
        this.foundKeywords = foundKeywords;
    }
}
