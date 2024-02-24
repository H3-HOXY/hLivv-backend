package hoxy.hLivv.exception;

public class AlreadyUsedCouponException extends RuntimeException {
    public AlreadyUsedCouponException() {
        super();
    }

    public AlreadyUsedCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyUsedCouponException(String message) {
        super(message);
    }

    public AlreadyUsedCouponException(Throwable cause) {
        super(cause);
    }
}
