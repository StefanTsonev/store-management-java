package store.exceptions;

public class InsufficientQuantityException extends Exception {
    public InsufficientQuantityException(String productName, int requested, int available) {
        super("Недостатъчно количество от продукта: " + productName +
                ". Заявено: " + requested + ", Налично: " + available);
    }
}
