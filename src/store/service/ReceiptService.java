package store.service;

import store.model.Receipt;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReceiptService {

    private static final String RECEIPT_FOLDER = "receipts/";

    public ReceiptService() {
        File folder = new File(RECEIPT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public void saveReceiptAsText(Receipt receipt) {
        String fileName = RECEIPT_FOLDER + "receipt_" + receipt.getNumber() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(receipt.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serializeReceipt(Receipt receipt) {
        String fileName = RECEIPT_FOLDER + "receipt_" + receipt.getNumber() + ".ser";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Receipt deserializeReceipt(int number) {
        String fileName = RECEIPT_FOLDER + "receipt_" + number + ".ser";
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Receipt) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printReceiptText(int number) {
        String fileName = RECEIPT_FOLDER + "receipt_" + number + ".txt";
        try {
            Files.lines(Paths.get(fileName)).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Не може да се прочете бележката: " + number);
        }
    }
}
