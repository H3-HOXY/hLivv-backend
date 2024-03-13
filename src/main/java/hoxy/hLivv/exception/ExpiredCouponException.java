package hoxy.hLivv.exception;
/**
 * @author 반정현
 */
public class ExpiredCouponException extends RuntimeException {
    public ExpiredCouponException() {
        super();
    }

    public ExpiredCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredCouponException(String message) {
        super(message);
    }

    public ExpiredCouponException(Throwable cause) {
        super(cause);
    }
}
