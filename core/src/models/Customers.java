package models;

public class Customers extends Person {

    public Customers(String id, String name, int x, int y) {
        super(id, name, x, y);
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}
