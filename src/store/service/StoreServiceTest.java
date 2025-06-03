package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.exceptions.CashierNotAssignedException;
import store.exceptions.ExpiredProductException;
import store.exceptions.InsufficientQuantityException;
import store.model.Cashier;
import store.model.Product;
import store.model.ProductCategory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StoreServiceTest {

    private StoreService storeService;

    @BeforeEach
    public void setUp() {
        storeService = new StoreService();

        // Зареждане на продукти и касиер (ако ги няма)
        Product milk = new Product("P001", "Мляко", 1.20, ProductCategory.FOOD, LocalDate.now().plusDays(3), 10);
        storeService.loadProduct(milk);

        Cashier cashier = new Cashier("C001", "Иван", 1200.00);
        storeService.assignCashierToRegister(1, cashier);
    }

    @Test
    public void testSellingProductsUpdatesRevenue() throws CashierNotAssignedException, ExpiredProductException, InsufficientQuantityException {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("P001", 2);

        double revenueBefore = storeService.calculateRevenue();
        storeService.sellProducts(1, cart);
        double revenueAfter = storeService.calculateRevenue();

        assertTrue(revenueAfter > revenueBefore);
    }

    @Test
    public void testCalculateProfit() throws CashierNotAssignedException, ExpiredProductException, InsufficientQuantityException {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("P001", 1);

        storeService.sellProducts(1, cart);
        double profit = storeService.calculateProfit();

        // Поне да не е нула
        assertNotEquals(0, profit);
    }

    @Test
    public void testInsufficientQuantityException() {
        Map<String, Integer> cart = new HashMap<>();
        cart.put("P001", 1000); // много голямо количество

        assertThrows(InsufficientQuantityException.class, () -> storeService.sellProducts(1, cart));
    }

    @Test
    public void testExpiredProductException() {
        Product expired = new Product("P002", "Стари бисквити", 0.5, ProductCategory.FOOD, LocalDate.now().minusDays(1), 5);
        storeService.loadProduct(expired);

        Map<String, Integer> cart = new HashMap<>();
        cart.put("P002", 1);

        assertThrows(ExpiredProductException.class, () -> storeService.sellProducts(1, cart));
    }
}
