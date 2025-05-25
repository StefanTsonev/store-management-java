package store.util;

import store.model.Cashier;
import store.model.Product;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StoreDataManager {

    private static final String CASHIERS_FILE = "store/cashiers.ser";
    private static final String PRODUCTS_FILE = "store/products.ser";
    private static final String FINANCE_FILE = "store/finance.txt";

    public static void saveCashiers(Map<Integer, Cashier> cashiers) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(CASHIERS_FILE))) {
            out.writeObject(cashiers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, Cashier> loadCashiers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(CASHIERS_FILE))) {
            return (Map<Integer, Cashier>) in.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static void saveProducts(Map<String, Product> products) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PRODUCTS_FILE))) {
            out.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Product> loadProducts() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PRODUCTS_FILE))) {
            return (Map<String, Product>) in.readObject();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static void saveFinance(double revenue, double costs) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FINANCE_FILE))) {
            writer.write(revenue + "\n" + costs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] loadFinance() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FINANCE_FILE))) {
            double revenue = Double.parseDouble(reader.readLine());
            double costs = Double.parseDouble(reader.readLine());
            return new double[]{revenue, costs};
        } catch (Exception e) {
            return new double[]{0, 0};
        }
    }
}
