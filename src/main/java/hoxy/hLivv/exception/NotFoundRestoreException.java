package hoxy.hLivv.exception;
/**
 * @author 이상원
 */
public class NotFoundRestoreException extends RuntimeException {
    public NotFoundRestoreException() {
        super();
    }
    public NotFoundRestoreException(String message) {
         super(message);
    }
}
