package store.service;

import store.exceptions.CashierNotAssignedException;
import store.exceptions.ExpiredProductException;
import store.exceptions.InsufficientQuantityException;
import store.model.*;
import store.util.StoreDataManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StoreService {
    private Map<String, Product> products;
    private Map<Integer, Cashier> cashiersByRegister;
    private double foodMarkupPercent = 0.2;
    private double nonFoodMarkupPercent = 0.3;
    private int expirationThresholdDays = 5;
    private double discountPercent = 0.25;

    private double totalRevenue;
    private double totalCosts;
    private LocalDate lastSalaryDate = LocalDate.now();

    public StoreService() {
        this.products = StoreDataManager.loadProducts();
        this.cashiersByRegister = StoreDataManager.loadCashiers();
        double[] finance = StoreDataManager.loadFinance();
        this.totalRevenue = finance[0];
        this.totalCosts = finance[1];
    }

    public void loadProduct(Product product) {
        products.merge(product.getId(), product, (existing, incoming) -> {
            existing.setQuantity(existing.getQuantity() + incoming.getQuantity());
            return existing;
        });
        StoreDataManager.saveProducts(products);
    }

    public void assignCashierToRegister(int registerNumber, Cashier cashier) {
        cashiersByRegister.put(registerNumber, cashier);
        StoreDataManager.saveCashiers(cashiersByRegister);
    }

    public Receipt sellProducts(int registerNumber, Map<String, Integer> cart)
            throws CashierNotAssignedException, ExpiredProductException, InsufficientQuantityException {

        if (!cashiersByRegister.containsKey(registerNumber)) {
            throw new CashierNotAssignedException(registerNumber);
        }

        Cashier cashier = cashiersByRegister.get(registerNumber);
        List<ReceiptItem> items = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();

            Product product = products.get(productId);
            if (product == null) continue;

            if (product.getExpirationDate().isBefore(LocalDate.now())) {
                throw new ExpiredProductException(product.getName());
            }

            if (product.getQuantity() < quantity) {
                throw new InsufficientQuantityException(product.getName(), quantity, product.getQuantity());
            }

            double price = calculateSellingPrice(product);
            items.add(new ReceiptItem(product, quantity, price));
            product.setQuantity(product.getQuantity() - quantity);
        }

        Receipt receipt = new Receipt(cashier, items);

        totalRevenue += receipt.getTotal();

        long daysSinceLastSalary = ChronoUnit.DAYS.between(lastSalaryDate, LocalDate.now());
        if (daysSinceLastSalary >= 30) {
            double salaryCost = cashiersByRegister.values().stream()
                    .mapToDouble(Cashier::getSalary)
                    .sum();
            totalCosts += salaryCost;
            lastSalaryDate = LocalDate.now();
        }

        StoreDataManager.saveProducts(products);
        StoreDataManager.saveFinance(round(totalRevenue), round(totalCosts));

        return receipt;
    }

    private double calculateSellingPrice(Product product) {
        double markup = product.getCategory() == ProductCategory.FOOD ? foodMarkupPercent : nonFoodMarkupPercent;
        double basePrice = product.getDeliveryPrice() * (1 + markup);

        long daysToExpire = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpirationDate());
        if (daysToExpire <= expirationThresholdDays) {
            basePrice *= (1 - discountPercent);
        }

        return round(basePrice);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public Map<Integer, Cashier> getCashiers() {
        return cashiersByRegister;
    }

    public double calculateRevenue() {
        return round(totalRevenue);
    }

    public double calculateCosts() {
        return round(totalCosts);
    }

    public double calculateProfit() {
        return round(totalRevenue - totalCosts);
    }

    public int getReceiptCount() {
        try (BufferedReader reader = new BufferedReader(new FileReader("receipts/last_number.txt"))) {
            return Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            return 0;
        }
    }

    public void addInitialStockCostsOnce() {
        double stockCost = products.values().stream()
                .mapToDouble(p -> p.getDeliveryPrice() * p.getQuantity())
                .sum();
        totalCosts += stockCost;
        StoreDataManager.saveFinance(round(totalRevenue), round(totalCosts));
    }
}
