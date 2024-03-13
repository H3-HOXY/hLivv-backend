package hoxy.hLivv.exception;
/**
 * @author 반정현
 */
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
