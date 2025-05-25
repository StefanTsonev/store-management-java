package store.exceptions;

public class ExpiredProductException extends Exception {
    public ExpiredProductException(String productName) {
        super("Продуктът \"" + productName + "\" е с изтекъл срок на годност и не може да бъде продаден.");
    }
}
