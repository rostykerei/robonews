package nl.rostykerei.news.service.syndication;

import java.io.IOException;

public class SyndicationServiceException extends IOException {

    public SyndicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
