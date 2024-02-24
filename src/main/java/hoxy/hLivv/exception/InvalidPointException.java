package hoxy.hLivv.exception;

public class InvalidPointException extends RuntimeException {
    public InvalidPointException() {
        super();
    }

    public InvalidPointException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPointException(String message) {
        super(message);
    }

    public InvalidPointException(Throwable cause) {
        super(cause);
    }
}
