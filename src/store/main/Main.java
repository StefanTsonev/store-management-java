package store.main;

import store.exceptions.CashierNotAssignedException;
import store.exceptions.ExpiredProductException;
import store.exceptions.InsufficientQuantityException;
import store.model.*;
import store.service.ReceiptService;
import store.service.StoreService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        StoreService storeService = new StoreService();
        ReceiptService receiptService = new ReceiptService();

        if (storeService.calculateCosts() == 0) {
            storeService.addInitialStockCostsOnce();
        }

        if (!storeService.getProducts().containsKey("P001")) {
            Product milk = new Product("P001", "Яйца", 2.20, ProductCategory.FOOD, LocalDate.now().plusDays(7), 10);
            storeService.loadProduct(milk);
        }

        if (!storeService.getProducts().containsKey("P002")) {
            Product soap = new Product("P002", "Кухненска хартия", 1.80, ProductCategory.NON_FOOD, LocalDate.now().plusDays(365), 20);
            storeService.loadProduct(soap);
        }

        // Назначаване на касиер
        if (!storeService.getCashiers().containsKey(1)) {
            Cashier cashier = new Cashier("C001", "Иван Иванов", 1200.00);
            storeService.assignCashierToRegister(1, cashier);
        }

        // Продажба
        Map<String, Integer> cart = new HashMap<>();
        cart.put("P001", 1);
        cart.put("P002", 2);

        try {
            Receipt receipt = storeService.sellProducts(1, cart);
            System.out.println(receipt);

            // Запазване на бележката
            receiptService.saveReceiptAsText(receipt);
            receiptService.serializeReceipt(receipt);

        } catch (CashierNotAssignedException | ExpiredProductException | InsufficientQuantityException e) {
            System.err.println("Грешка при продажбата: " + e.getMessage());
        }

        System.out.println("\nОборот: " + storeService.calculateRevenue() + " лв.");
        System.out.println("Разходи: " + storeService.calculateCosts() + " лв.");
        System.out.println("Печалба: " + storeService.calculateProfit() + " лв.");
        System.out.println("Общо бележки: " + storeService.getReceiptCount());

        System.out.println("\nНалични продукти:");
        storeService.getProducts().values().forEach(p ->
                System.out.println(p.getName() + ": " + p.getQuantity() + " бр.")
        );

        System.out.println("\nКасиери:");
        storeService.getCashiers().forEach((reg, c) ->
                System.out.println("Каса " + reg + ": " + c.getName() + ", заплата: " + c.getSalary() + " лв.")
        );
    }
}
