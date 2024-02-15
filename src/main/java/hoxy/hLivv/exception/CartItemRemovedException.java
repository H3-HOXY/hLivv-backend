package hoxy.hLivv.exception;

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
