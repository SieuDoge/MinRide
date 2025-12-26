package services;

import algorithms.Sort.merge;
import data_structures.LinkedList.Doubly;
import data_structures.Tree.AVLTree;
import data_structures.Tree.Heap;
import models.Drivers;
import utils.distance;
import utils.file_io;

public class driver_service {
    private Doubly<Drivers> driverList; // Maintain List for UI Traversal & Name Search
    private AVLTree<Drivers> driverTree; // Maintain AVL Tree for fast ID Search

    public driver_service() {
        this.driverList = new Doubly<>();
        // AVL Tree compares Drivers by ID
        this.driverTree = new AVLTree<>((d1, d2) -> d1.getId().compareToIgnoreCase(d2.getId()));
    }
    
    public Doubly<Drivers> getDriverList() {
        return driverList;
    }
    
    public void setDriverList(Doubly<Drivers> list) {
        this.driverList = list;
        // Rebuild Tree from list
        driverTree.clear();
        for(int i=0; i<list.getSize(); i++) {
            Drivers d = list.get(i);
            driverTree.insert(d);
        }
        // Actually, let's just use `addDriver` to build initial state if needed.
    }
    
    // Note: Doubly.java needs a get() method or we iterate manually.
    // For now, assuming Doubly needs an update or I iterate manually.
    // I will iterate using a helper or modify Doubly. For now, let's assume I can iterate.

    // 1. Thêm tài xế (Sync both List and AVL)
    public void addDriver(Drivers driver) {
        if (getDriverById(driver.getId()) != null) {
            System.out.println("Driver ID already exists!");
            return;
        }
        driverList.addLast(driver);
        driverTree.insert(driver);
    }

    // 2. Tìm kiếm tài xế theo ID (Use AVL - O(logN))
    public Drivers getDriverById(String id) {
        Drivers key = new Drivers(id, "", 0, 0, 0);
        return driverTree.search(key);
    }

    // Tìm kiếm theo Tên (Use Doubly List - Linear)
    public Doubly<Drivers> searchDriverByName(String name) {
        Doubly<Drivers> result = new Doubly<>();
        String lower = name.toLowerCase();
        
        for (int i = 0; i < driverList.getSize(); i++) {
            Drivers d = driverList.get(i);
            if (d.getName().toLowerCase().contains(lower)) {
                result.addLast(d);
            }
        }
        return result; 
    }
    
    // Tìm kiếm theo Tên hoặc ID (Combined)
    public Doubly<Drivers> searchDriver(String query) {
        Doubly<Drivers> result = new Doubly<>();
        String lowerQuery = query.toLowerCase();
        
        for (int i = 0; i < driverList.getSize(); i++) {
            Drivers d = driverList.get(i);
            if (d.getName().toLowerCase().contains(lowerQuery) || d.getId().toLowerCase().contains(lowerQuery)) {
                result.addLast(d);
            }
        }
        return result;
    }

    public String getNextId() {
        int maxId = 0;
        for (int i = 0; i < driverList.getSize(); i++) {
            try {
                int id = Integer.parseInt(driverList.get(i).getId());
                if (id > maxId) maxId = id;
            } catch (NumberFormatException e) {}
        }
        return String.valueOf(maxId + 1);
    }

    // 3. Xóa tài xế (Sync both)
    public boolean deleteDriver(String id) {
        Drivers target = getDriverById(id);
        if (target == null) return false;

        // Remove from Tree
        driverTree.delete(target);

        // Remove from List (Linear removal)
        driverList.remove(target);
        
        file_io.rewriteDrivers(driverList, "drivers.csv");
        return true;
    }

    // 4. Cập nhật thông tin
    public boolean updateDriver(String id, String newName, double newRating, int newX, int newY) {
        Drivers oldD = getDriverById(id);
        if(oldD == null) return false;
        
        // Preserve revenue
        double currentRevenue = oldD.getRevenue();
        
        // Remove old (memory only)
        driverTree.delete(oldD);
        driverList.remove(oldD);
        
        // Add new updated node
        Drivers newD = new Drivers(id, newName, newRating, newX, newY, currentRevenue);
        addDriver(newD);
        
        file_io.rewriteDrivers(driverList, "drivers.csv");
        return true;
    }
    
    // Add Revenue (Task 3)
    public void addRevenue(String driverId, double amount) {
        Drivers d = getDriverById(driverId);
        if (d != null) {
            d.addRevenue(amount);
            // Persist changes immediately to avoid data loss
            file_io.rewriteDrivers(driverList, "drivers.csv");
        }
    }
    
    // Top K Income Drivers (Task 3 & 4) - Using Merge Sort
    public Doubly<Drivers> getTopIncomeDrivers(int k) {
        Object[] arrObjs = driverList.toArray();
        Drivers[] arr = new Drivers[arrObjs.length];
        for(int i=0; i<arrObjs.length; i++) arr[i] = (Drivers)arrObjs[i];

        // Sort Descending by Revenue
        merge.mergeSort(arr, 0, arr.length - 1, (d1, d2) -> Double.compare(d2.getRevenue(), d1.getRevenue()));

        Doubly<Drivers> result = new Doubly<>();
        int count = 0;
        for (Drivers d : arr) {
            if (count >= k) break;
            result.addLast(d);
            count++;
        }
        return result;
    }

    // Sort by Rating (Asc/Desc)
    public void sortDriversByRating(boolean ascending) {
        Object[] arrObjs = driverList.toArray();
        Drivers[] arr = new Drivers[arrObjs.length];
        for(int i=0; i<arrObjs.length; i++) arr[i] = (Drivers)arrObjs[i];

        // Merge Sort
        merge.mergeSort(arr, 0, arr.length - 1, (d1, d2) -> {
            int cmp = Double.compare(d1.getRating(), d2.getRating());
            return ascending ? cmp : -cmp; // If asc: min->max, else max->min
        });

        rebuildStructures(arr);
    }

    // Sort by Name (Asc/Desc)
    public void sortDriversByName(boolean ascending) {
        Object[] arrObjs = driverList.toArray();
        Drivers[] arr = new Drivers[arrObjs.length];
        for(int i=0; i<arrObjs.length; i++) arr[i] = (Drivers)arrObjs[i];

        merge.mergeSort(arr, 0, arr.length - 1, (d1, d2) -> {
            int cmp = d1.getName().compareToIgnoreCase(d2.getName());
            return ascending ? cmp : -cmp;
        });

        rebuildStructures(arr);
    }
    
    private void rebuildStructures(Drivers[] arr) {
        driverList.clear();
        driverTree.clear();
        for (Drivers d : arr) {
            driverList.addLast(d);
            driverTree.insert(d);
        }
    }
    
    public void rateDriver(String driverId, int stars) {
        Drivers d = getDriverById(driverId);
        if (d == null) return;
        
        // Simple Average update: New = (Old + New) / 2 (Naive)
        // Better: Since we don't store count, we can only approximate or use a running average with assumed weight.
        // Let's assume weight of 1 for simplicity (just averaging with new value) -> highly volatile.
        // OR, we just update it. The requirement "Khách hàng đánh giá" implies the rating should change.
        // Let's do: NewRating = (OldRating * 4 + Stars) / 5 (Weighted: Old is heavier).
        // Or better yet, we can't do it perfectly without Count.
        // I will assume the current rating is based on 10 ratings.
        // rating = ((rating * 10) + stars) / 11.
        
        double newRating = ((d.getRating() * 10) + stars) / 11.0;
        // Clamp to 5.0
        if(newRating > 5.0) newRating = 5.0;
        
        updateDriver(driverId, d.getName(), newRating, d.getX(), d.getY());
    }
    
    // Top K Drivers (Min-Heap)
    public Doubly<Drivers> getTopKDrivers(int k) {
        // We want TOP K Highest rating.
        // Min-Heap of size k. If new element > min, replace min.
        // Heap logic: Store 'Drivers'. Comparator should sort by Rating.
        // Java PriorityQueue defaults to Min-Heap.
        // My Heap class is Min-Heap.
        
        Heap<Drivers> minHeap = new Heap<>((d1, d2) -> Double.compare(d1.getRating(), d2.getRating()));
        
        java.util.List<Drivers> all = driverTree.inorder();
        
        for (Drivers d : all) {
            if (minHeap.size() < k) {
                minHeap.insert(d);
            } else {
                // Peek min. If d > min, replace.
                // My Heap doesn't have peekMin exposed efficiently except extractMin.
                // Let's extractMin then insert if d is better.
                // Wait, if I extractMin, I lose it.
                // I need to peek. `heap.get(0)`? My Heap wraps ArrayList.
                // Heap.java uses private ArrayList. I added `size()`, `extractMin`. I should add `peek()`.
                // Assuming I can't modify Heap easily right now, I'll use extractMin/insert strategy or just sort all and take top K.
                // Sorting all is O(N log N). Heap is O(N log K).
                // Given k is usually small, N log K is better.
                // But since I don't have peek(), I can do:
                // minHeap.insert(d); if (minHeap.size() > k) minHeap.extractMin();
                // This keeps the largest K elements in the heap (because smallest are evicted).
                
                minHeap.insert(d);
                if (minHeap.size() > k) {
                    minHeap.extractMin();
                }
            }
        }
        
        // Now heap contains top K. But they are not sorted.
        // Extract them (they come out smallest first).
        // Since we want Top K (Highest first?), we'll extract and put in list, then reverse, or addFirst.
        // Doubly list has addLast.
        
        Doubly<Drivers> result = new Doubly<>();
        // Extracting gives ascending order (Min first).
        // So for [4.9, 4.8, 4.7] (Top 3), heap extracts 4.7, 4.8, 4.9.
        // If we want detailed display 4.9 -> 4.7, we can use stack or addFirst (if available).
        // Doubly doesn't have addFirst. I'll add to a temp stack or just array and reverse.
        // Or just return ascending and let UI handle it? "Top K ở đầu hoặc cuối danh sách" -> Flexible.
        
        // Let's just return them.
        while(!minHeap.isEmpty()) {
            result.addLast(minHeap.extractMin());
        }
        // Result is currently Ascending (Worst of top K -> Best of top K).
        return result;
    }

    // 7. Find Nearest Drivers using HEAP
    public Doubly<Drivers> findNearestDrivers(int customerX, int customerY, double radius) {
        return findDriversWithCriteria(customerX, customerY, radius, 1); // Default to Distance sort
    }

    /**
     * Tìm kiếm tài xế trong bán kính R và sắp xếp theo tiêu chí.
     * @param radius Bán kính tìm kiếm (km)
     * @param sortType 1: Khoảng cách tăng dần, 2: Rating giảm dần, 3: Kết hợp (Gần nhất -> Rating cao)
     */
    public Doubly<Drivers> findDriversWithCriteria(int cx, int cy, double radius, int sortType) {
        // 1. Lọc danh sách trong bán kính R
        java.util.List<Drivers> all = driverTree.inorder();
        java.util.List<Drivers> filtered = new java.util.ArrayList<>();
        
        for (Drivers d : all) {
            double dist = distance.calculate(cx, cy, d.getX(), d.getY());
            if (dist <= radius && d.isAvailable()) {
                filtered.add(d);
            }
        }

        Drivers[] arr = filtered.toArray(new Drivers[0]);

        // 2. Sắp xếp dựa trên tiêu chí
        merge.mergeSort(arr, 0, arr.length - 1, (d1, d2) -> {
            double dist1 = distance.calculate(cx, cy, d1.getX(), d1.getY());
            double dist2 = distance.calculate(cx, cy, d2.getX(), d2.getY());

            switch (sortType) {
                case 2: // Rating giảm dần (High -> Low)
                    return Double.compare(d2.getRating(), d1.getRating());
                
                case 3: // Gần nhất -> Nếu bằng nhau thì Rating cao hơn
                    int cmp = Double.compare(dist1, dist2);
                    if (cmp != 0) return cmp;
                    return Double.compare(d2.getRating(), d1.getRating());

                case 1: // Mặc định: Khoảng cách tăng dần
                default:
                    return Double.compare(dist1, dist2);
            }
        });

        // 3. Convert to Doubly List
        Doubly<Drivers> result = new Doubly<>();
        for (Drivers d : arr) result.addLast(d);
        return result;
    }
}
