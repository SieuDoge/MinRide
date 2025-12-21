package models;

public class Customers extends Person {
    private String district;

    public Customers(String id, String name, int x, int y, String district) {
        super(id, name, x, y);
        this.district = district;
    }

    @Override
    public String getRole() {
        return "Customer";
    }
}
