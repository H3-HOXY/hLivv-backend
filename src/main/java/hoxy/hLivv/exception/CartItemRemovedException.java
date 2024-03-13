package hoxy.hLivv.exception;

/**
 * @author 반정현
 */
public class CartItemRemovedException extends RuntimeException {
    public CartItemRemovedException() {
        super();
    }

    public CartItemRemovedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartItemRemovedException(String message) {
        super(message);
    }

    public CartItemRemovedException(Throwable cause) {
        super(cause);
    }
}
