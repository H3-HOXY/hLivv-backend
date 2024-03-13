package hoxy.hLivv.exception;
/**
 * @author 이상원
 */
public class AccessDeniedMemberException extends RuntimeException {
    public AccessDeniedMemberException() {
        super();
    }
    public AccessDeniedMemberException(String message) {
         super(message);
    }
}
