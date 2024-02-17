package hoxy.hLivv.exception;

public class NotFoundCartItemException extends RuntimeException {
    public NotFoundCartItemException() {
        super();
    }

    public NotFoundCartItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCartItemException(String message) {
        super(message);
    }

    public NotFoundCartItemException(Throwable cause) {
        super(cause);
    }
}
