package nl.rostykerei.news.service.freebase.exception;


public class ServiceUnavailableException extends FreebaseServiceException {

    public ServiceUnavailableException() {
        super();
    }

    public ServiceUnavailableException(String error) {
        super(error);
    }
}
