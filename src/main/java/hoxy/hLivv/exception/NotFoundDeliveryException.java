package hoxy.hLivv.exception;

public class NotFoundDeliveryException extends RuntimeException {
    public NotFoundDeliveryException() {
        super();
    }

    public NotFoundDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundDeliveryException(String message) {
        super(message);
    }

    public NotFoundDeliveryException(Throwable cause) {
        super(cause);
    }
}
