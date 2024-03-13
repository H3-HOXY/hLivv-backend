package hoxy.hLivv.exception;
/**
 * @author 반정현
 */
public class NotFoundOrderException extends RuntimeException {
    public NotFoundOrderException() {
        super();
    }

    public NotFoundOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundOrderException(String message) {
        super(message);
    }

    public NotFoundOrderException(Throwable cause) {
        super(cause);
    }
}
