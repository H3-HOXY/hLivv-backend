package hoxy.hLivv.exception;

public class MismatchedMemberException extends RuntimeException {
    public MismatchedMemberException() {
        super();
    }

    public MismatchedMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MismatchedMemberException(String message) {
        super(message);
    }

    public MismatchedMemberException(Throwable cause) {
        super(cause);
    }
}