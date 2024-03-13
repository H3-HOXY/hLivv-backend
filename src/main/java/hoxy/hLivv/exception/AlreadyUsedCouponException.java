package hoxy.hLivv.exception;

/**
 * @author 반정현
 */
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
