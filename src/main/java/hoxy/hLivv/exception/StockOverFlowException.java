package hoxy.hLivv.exception;

public class StockOverFlowException extends RuntimeException {
    public StockOverFlowException() {
        super();
    }

    public StockOverFlowException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockOverFlowException(String message) {
        super(message);
    }

    public StockOverFlowException(Throwable cause) {
        super(cause);
    }
}
