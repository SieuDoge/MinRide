package services;

import data_structures.StackNQueue.Queue;
import data_structures.LinkedList.Doubly;
import models.Drivers;
import models.Rides;
import utils.distance;
import utils.file_io;

public class booking_service {
    
    private Queue<Rides> bookingQueue;

    public booking_service() {
        // Load from file if exists, otherwise new
        this.bookingQueue = file_io.loadQueue("Queue.csv");
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
        return new Rides(rideId, cusId, "WAITING", dist);
    }
    
    public void addToQueue(Rides ride) {
        bookingQueue.enqueue(ride);
        file_io.rewriteQueue(bookingQueue, "Queue.csv");
    }
    
    public Rides[] getPendingRides() {
        Object[] objs = bookingQueue.toArray();
        Rides[] rides = new Rides[objs.length];
        for(int i=0; i<objs.length; i++) rides[i] = (Rides)objs[i];
        return rides;
    }

    public Rides getRideInQueue(String rideId) {
        Rides[] pending = getPendingRides();
        for (Rides r : pending) {
            if (r.getRideId().equalsIgnoreCase(rideId)) return r;
        }
        return null;
    }

    public boolean removeRideFromQueue(Rides r) {
        boolean res = bookingQueue.remove(r);
        if(res) file_io.rewriteQueue(bookingQueue, "Queue.csv");
        return res;
    }

    // Manual Dispatch
    public Rides dispatchRide(Rides ride, Drivers driver) {
        return new Rides(ride.getRideId(), ride.getCustomerId(), driver.getId(), ride.getDistance());
    }
    
    // Auto-Assign Logic (Smart Dispatch)
    public Drivers autoAssignDriver(Rides ride, driver_service ds, customer_service cs) {
        // 1. Get Customer Location
        models.Customers c = cs.getCustomerById(ride.getCustomerId());
        if (c == null) return null;

        // 2. Find best drivers (Radius 10.0km, Sort Criteria 3: Distance -> Rating)
        Doubly<Drivers> candidates = ds.findDriversWithCriteria(c.getX(), c.getY(), 10.0, 3);
        
        if (candidates.getSize() == 0) return null;

        // 3. Pick the best one (The first one in the sorted list)
        Drivers bestDriver = candidates.get(0);
        
        // 4. Update Driver Status
        bestDriver.setAvailable(false);
        file_io.rewriteDrivers(ds.getDriverList(), "drivers.csv"); // Persist status

        return bestDriver;
    }
    
    public Rides processNextBooking(driver_service ds, int cx, int cy) {
        if (bookingQueue.isEmpty()) return null;
        
        Rides ride = bookingQueue.dequeue();
        file_io.rewriteQueue(bookingQueue, "Queue.csv"); // Save state
        
        // Auto-match: Find nearest driver
        Doubly<Drivers> nearby = ds.findNearestDrivers(cx, cy, 10.0);
        
        if (nearby.getSize() > 0) {
            Drivers best = nearby.get(0); // Nearest
            System.out.println("Matched Driver " + best.getName() + " for Ride " + ride.getRideId());
            return new Rides(ride.getRideId(), ride.getCustomerId(), best.getId(), ride.getDistance());
        } else {
            System.out.println("No driver found for Ride " + ride.getRideId());
            return null;
        }
    }
}