package hoxy.hLivv.exception;
/**
 * @author 반정현
 */
public class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException() {
        super();
    }

    public InvalidPaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPaymentException(String message) {
        super(message);
    }

    public InvalidPaymentException(Throwable cause) {
        super(cause);
    }
}
