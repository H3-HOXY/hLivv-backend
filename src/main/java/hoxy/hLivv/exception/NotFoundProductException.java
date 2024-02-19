package hoxy.hLivv.exception;

public class NotFoundProductException extends RuntimeException {
    public NotFoundProductException() {
        super();
    }
    public NotFoundProductException(String message) {
         super(message);
    }
}
