package store.model;

import java.io.Serializable;

public class ReceiptItem implements Serializable {
    private Product product;
    private int quantity;
    private double unitSellingPrice;

    public ReceiptItem(Product product, int quantity, double unitSellingPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitSellingPrice = unitSellingPrice;
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getUnitSellingPrice() { return unitSellingPrice; }

    public double getTotalPrice() {
        return quantity * unitSellingPrice;
    }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " @ " + unitSellingPrice + " = " + getTotalPrice() + " лв.";
    }
}
