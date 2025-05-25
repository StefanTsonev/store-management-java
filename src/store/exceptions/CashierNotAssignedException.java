package store.exceptions;

public class CashierNotAssignedException extends Exception {
    public CashierNotAssignedException(int registerNumber) {
        super("На каса №" + registerNumber + " няма назначен касиер.");
    }
}
