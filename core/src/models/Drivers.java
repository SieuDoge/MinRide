package models;

public class Drivers extends Person {
    private double rating;
    private boolean isAvailable;

    public Drivers(String id, String name, double rating, int x, int y) {
        super(id, name, x, y);
        this.rating = rating;
        this.isAvailable = true;
    }

    @Override
    public String getID() {
        return "ID: " + id;
    }

    public double getRating() {
        return rating;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return super.toString() + " - Rating: " + rating;
    }
}