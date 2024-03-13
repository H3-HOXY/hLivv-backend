package hoxy.hLivv.exception;

/**
 * @author 반정현
 */
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
