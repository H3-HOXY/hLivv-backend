package hoxy.hLivv.exception;
/**
 * @author 반정현
 */
public class NotFoundCouponException extends RuntimeException {
    public NotFoundCouponException() {
        super();
    }

    public NotFoundCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCouponException(String message) {
        super(message);
    }

    public NotFoundCouponException(Throwable cause) {
        super(cause);
    }
}
