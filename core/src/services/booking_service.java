package services;

import data_structures.StackNQueue.Queue;
import data_structures.LinkedList.Doubly;
import models.Drivers;
import models.Rides;
import utils.distance;

public class booking_service {
    
    private Queue<Rides> bookingQueue;

    public booking_service() {
        this.bookingQueue = new Queue<>(100);
    }

    public double calculateEstDistance(int x1, int y1, int x2, int y2) {
        return distance.calculate(x1, y1, x2, y2);
    }

    public double calculateFare(double dist) {
        return dist * 12000.0;
    }

    public Rides createBooking(String rideId, String cusId, int cx, int cy, int dx, int dy) {
        double dist = distance.calculate(cx, cy, dx, dy);
        // Driver ID unknown yet
        return new Rides(rideId, cusId, null, dist);
    }
    
    public void addToQueue(Rides ride) {
        bookingQueue.enqueue(ride);
        System.out.println("Booking added to queue: " + ride.getRideId());
    }
    
    public Rides processNextBooking(driver_service ds, int cx, int cy) {
        if (bookingQueue.isEmpty()) return null;
        
        Rides ride = bookingQueue.dequeue();
        
        // Auto-match: Find nearest driver
        // Radius hardcoded or dynamic? Let's say 10km.
        Doubly<Drivers> nearby = ds.findNearestDrivers(cx, cy, 10.0);
        
        if (nearby.getSize() > 0) {
            Drivers best = nearby.get(0); // Nearest
            // Assign
            // Note: Rides is immutable-ish in my model (no setters), I might need to recreate it or add setter.
            // Let's assume I can modify or create new.
            System.out.println("Matched Driver " + best.getName() + " for Ride " + ride.getRideId());
            return new Rides(ride.getRideId(), ride.getCustomerId(), best.getId(), ride.getDistance());
        } else {
            System.out.println("No driver found for Ride " + ride.getRideId());
            return null;
        }
    }
}