package store.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Product implements Serializable{
    private String id;
    private String name;
    private double deliveryPrice;
    private ProductCategory category;
    private LocalDate expirationDate;
    private int quantity;

    public Product(String id, String name, double deliveryPrice, ProductCategory category, LocalDate expirationDate, int quantity) {
        this.id = id;
        this.name = name;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
        this.expirationDate = expirationDate;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getDeliveryPrice() { return deliveryPrice; }
    public ProductCategory getCategory() { return category; }
    public LocalDate getExpirationDate() { return expirationDate; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return name + " (" + quantity + " бр., " + deliveryPrice + " лв.)";
    }
}
