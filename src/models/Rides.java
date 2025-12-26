package models;

public class Rides implements Comparable<Rides> {
    private String rideId;
    private String customerId;
    private String driverId;
    private double distance;
    private double fare;


    private static final double PRICE_PER_KM = 12000.0; // Price per km

    public Rides(String rideId, String customerId, String driverId, double distance) {
        this.rideId = rideId;
        this.customerId = customerId;
        this.driverId = driverId;
        this.distance = distance;
        this.fare = calculateFare(distance);
    }

    public Rides(String rideId, String customerId, String driverId, double distance, double fare) {
        this.rideId = rideId;
        this.customerId = customerId;
        this.driverId = driverId;
        this.distance = distance;
        this.fare = fare;
    }

    private double calculateFare(double distance) {
        return distance * PRICE_PER_KM;
    }

    // Getters
    public String getRideId() { return rideId; }
    public String getCustomerId() { return customerId; }
    public String getDriverId() { return driverId; }
    public double getDistance() { return distance; }
    public double getFare() { return fare; }

    @Override
    public String toString() {
        return String.format("Ride[ID=%s | Cus=%s | Drv=%s | %.1fkm | %.0f VND]",
                rideId, customerId, driverId, distance, fare);
    }


    @Override
    public int compareTo(Rides other) {
        // Trả về số âm nếu this < other
        // Trả về 0 nếu bằng nhau
        // Trả về dương nếu this > other
        return this.rideId.compareTo(other.rideId);
    }
}