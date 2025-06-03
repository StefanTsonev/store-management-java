package store.model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class Receipt implements Serializable {
    private static final String LAST_NUMBER_FILE = "receipts/last_number.txt";
    private static int nextNumber = readLastNumberFromFile();

    private int number;
    private Cashier cashier;
    private LocalDateTime issuedAt;
    private List<ReceiptItem> items;
    private double total;

    public Receipt(Cashier cashier, List<ReceiptItem> items) {
        this.number = nextNumber++;
        this.cashier = cashier;
        this.issuedAt = LocalDateTime.now();
        this.items = items;
        this.total = calculateTotal();
        saveLastNumberToFile(this.number);
    }

    private double calculateTotal() {
        return items.stream().mapToDouble(ReceiptItem::getTotalPrice).sum();
    }

    public int getNumber() { return number; }
    public Cashier getCashier() { return cashier; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public List<ReceiptItem> getItems() { return items; }
    public double getTotal() { return total; }

    private static int readLastNumberFromFile() {
        try {
            File file = new File(LAST_NUMBER_FILE);
            if (!file.exists()) return 1;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            return Integer.parseInt(reader.readLine()) + 1;
        } catch (Exception e) {
            return 1;
        }
    }

    private static void saveLastNumberToFile(int number) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LAST_NUMBER_FILE))) {
            writer.write(String.valueOf(number));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("КАСОВА БЕЛЕЖКА №").append(number).append("\n");
        sb.append("Касиер: ").append(cashier.getName()).append("\n");
        sb.append("Дата: ").append(issuedAt).append("\n");
        sb.append("Продукти:\n");
        for (ReceiptItem item : items) {
            sb.append("  - ").append(item).append("\n");
        }
        sb.append("Обща сума: ").append(total).append(" лв.");
        return sb.toString();
    }
}
