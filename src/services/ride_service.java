package services;

import data_structures.LinkedList.Doubly;
import models.Rides;

public class ride_service {
    private Doubly<Rides> rideList;

    public ride_service() {
        this.rideList = new Doubly<>();
    }

    public void setRideList(Doubly<Rides> list) {
        this.rideList = list;
    }

    public Doubly<Rides> getRideList() {
        return rideList;
    }

    public void addRide(Rides ride) {
        rideList.addLast(ride);
    }

    // List rides by Driver ID (sorted by time/ID)
    public Doubly<Rides> getRidesByDriver(String driverId) {
        // 1. Filter
        Doubly<Rides> filtered = new Doubly<>();
        for(int i=0; i<rideList.getSize(); i++){
            Rides r = rideList.get(i);
            if(r.getDriverId().equalsIgnoreCase(driverId)){
                filtered.addLast(r);
            }
        }
        
        // 2. Sort (Insertion Sort per requirement)
        // Convert to Array, sort, put back
        Object[] arrObjs = filtered.toArray();
        if (arrObjs.length == 0) return filtered;

        // Insertion Sort implementation usually works on int[].
        // I need to adapt insertion sort to be Generic.
        // Or implement it inline here for simplicity since I can't easily refactor all algo classes in one go.
        // I'll implement inline insertion sort for Rides array.
        
        Rides[] arr = new Rides[arrObjs.length];
        for(int i=0; i<arrObjs.length; i++) arr[i] = (Rides)arrObjs[i];
        
        // Insertion Sort
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            Rides key = arr[i];
            int j = i - 1;

            // Compare by RideID (assuming newer ID = newer time)
            // Or use timestamp if available (not currently). RideID string compare might be enough if format is consistent.
            while (j >= 0 && arr[j].compareTo(key) > 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        
        Doubly<Rides> sorted = new Doubly<>();
        for(Rides r : arr) sorted.addLast(r);
        
        return sorted;
    }
}