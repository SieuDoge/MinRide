package models;

public class Customers extends Person {
    private String district;

    public Customers(String id, String name, String district, int x, int y) {
        super(id, name, x, y);
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String getID() {
        return "ID: " + id;
    }
    
    @Override
    public String toString() {
        return super.toString() + " - District: " + district;
    }
}
