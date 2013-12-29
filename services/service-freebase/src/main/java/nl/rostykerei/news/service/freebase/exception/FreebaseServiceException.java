package nl.rostykerei.news.service.freebase.exception;


public class FreebaseServiceException extends Exception {

    public FreebaseServiceException() {
        super();
    }

    public FreebaseServiceException(String error) {
        super(error);
    }
}
