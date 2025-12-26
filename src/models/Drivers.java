package models;

public class Drivers extends Person {
    private double rating;
    private boolean isAvailable;
    private double revenue; // Tổng doanh thu

    public Drivers(String id, String name, double rating, int x, int y) {
        super(id, name, x, y); // Gọi constructor của cha (Person)
        this.rating = rating;
        this.isAvailable = true;
        this.revenue = 0.0;
    }

    public Drivers(String id, String name, double rating, int x, int y, double revenue) {
        super(id, name, x, y); // Gọi constructor của cha (Person)
        this.rating = rating;
        this.isAvailable = true;
        this.revenue = revenue;
    }

    // Implement abstract method từ Person
    @Override
    public String getID() {
        return id;
    }

    // Getters & Setters
    // id, name, x, y đã có getter từ Person, không cần viết lại trừ khi muốn override
    
    public double getRating() { return rating; }
    public boolean isAvailable() { return isAvailable; }
    public double getRevenue() { return revenue; }

    public void setAvailable(boolean available) { isAvailable = available; }
    public void setRevenue(double revenue) { this.revenue = revenue; }
    public void addRevenue(double amount) { this.revenue += amount; }

    @Override
    public String toString() {
        // Sử dụng getX(), getY() từ Person (thông qua Location)
        return String.format("Driver[ID=%s | Name=%-10s | %.1f★ | Loc(%d,%d) | $%.0f]", 
            id, name, rating, getX(), getY(), revenue);
    }
}
