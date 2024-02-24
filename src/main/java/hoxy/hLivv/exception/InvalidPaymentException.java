package hoxy.hLivv.exception;

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
