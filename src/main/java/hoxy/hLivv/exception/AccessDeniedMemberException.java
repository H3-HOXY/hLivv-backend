package hoxy.hLivv.exception;

public class AccessDeniedMemberException extends RuntimeException {
    public AccessDeniedMemberException() {
        super();
    }
    public AccessDeniedMemberException(String message) {
         super(message);
    }
}
