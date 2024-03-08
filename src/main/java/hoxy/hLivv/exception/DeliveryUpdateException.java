package hoxy.hLivv.exception;

public class DeliveryUpdateException extends RuntimeException {
    public DeliveryUpdateException() {
        super();
    }

    public DeliveryUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeliveryUpdateException(String message) {
        super(message);
    }

    public DeliveryUpdateException(Throwable cause) {
        super(cause);
    }
}
