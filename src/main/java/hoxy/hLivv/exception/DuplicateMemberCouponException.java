package hoxy.hLivv.exception;

public class DuplicateMemberCouponException extends RuntimeException {
    public DuplicateMemberCouponException() {
        super();
    }

    public DuplicateMemberCouponException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateMemberCouponException(String message) {
        super(message);
    }

    public DuplicateMemberCouponException(Throwable cause) {
        super(cause);
    }
}
