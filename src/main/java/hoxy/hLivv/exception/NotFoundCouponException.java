package hoxy.hLivv.exception;

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
