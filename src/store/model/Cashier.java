package store.model;

import java.io.Serializable;

public class Cashier implements Serializable {
    private String id;
    private String name;
    private double salary;

    public Cashier(String id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return name + " (ID: " + id + ", заплата: " + salary + " лв.)";
    }
}
