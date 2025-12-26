package services;

import data_structures.LinkedList.Doubly;
import data_structures.Tree.AVLTree;
import data_structures.Tree.BinarySearch;
import data_structures.Tree.Heap;
import models.Customers;
import algorithms.Sort.merge;
import utils.file_io;

public class customer_service {
    private Doubly<Customers> customerList; // List for Traversal
    private AVLTree<Customers> customerTree; // AVL for ID Search
    private BinarySearch<Customers> districtTree; // BST for District+ID Search

    public customer_service() {
        this.customerList = new Doubly<>();
        this.customerTree = new AVLTree<>((c1, c2) -> c1.getId().compareToIgnoreCase(c2.getId()));
        
        // BST Key: District first, then ID (ascending)
        this.districtTree = new BinarySearch<>((c1, c2) -> {
            int cmp = c1.getDistrict().compareToIgnoreCase(c2.getDistrict());
            if (cmp != 0) return cmp;
            return c1.getId().compareToIgnoreCase(c2.getId());
        });
    }

    public void setCustomerList(Doubly<Customers> list) {
        this.customerList = list;
        // Rebuild Trees
        customerTree.clear();
        districtTree.clear();
        for(int i=0; i<list.getSize(); i++){
            Customers c = list.get(i);
            customerTree.insert(c);
            districtTree.insert(c);
        }
    }
    
    public Doubly<Customers> getCustomerList() {
        return customerList;
    }

    public void addCustomer(Customers c) {
        if (getCustomerById(c.getId()) != null) {
            System.out.println("Customer ID exists.");
            return;
        }
        customerList.addLast(c);
        customerTree.insert(c);
        districtTree.insert(c);
    }

    public Customers getCustomerById(String id) {
        // Dummy for search
        Customers key = new Customers(id, "", "", 0, 0); 
        return customerTree.search(key);
    }

    public String getNextId() {
        int maxId = 0;
        for (int i = 0; i < customerList.getSize(); i++) {
            try {
                int id = Integer.parseInt(customerList.get(i).getId());
                if (id > maxId) maxId = id;
            } catch (NumberFormatException e) {}
        }
        return String.valueOf(maxId + 1);
    }

    public Doubly<Customers> searchCustomer(String query) {
        Doubly<Customers> result = new Doubly<>();
        String lower = query.toLowerCase();
        for (int i = 0; i < customerList.getSize(); i++) {
            Customers c = customerList.get(i);
            if (c.getName().toLowerCase().contains(lower) || c.getId().toLowerCase().contains(lower)) {
                result.addLast(c);
            }
        }
        return result;
    }
    
    public boolean deleteCustomer(String id) {
        Customers target = getCustomerById(id);
        if (target == null) return false;
        
        customerTree.delete(target);
        districtTree.delete(target); 
        
        customerList.remove(target);
        
        // Persist change
        file_io.rewriteCustomers(customerList, "customers.csv");
        return true;
    }

    public boolean updateCustomer(String id, String newName, String newDistrict, int newX, int newY) {
        Customers oldC = getCustomerById(id);
        if(oldC == null) return false;
        
        // Remove old from structures manually to avoid double rewrite
        customerTree.delete(oldC);
        districtTree.delete(oldC);
        customerList.remove(oldC);
        
        // Add new
        Customers newC = new Customers(id, newName, newDistrict, newX, newY);
        addCustomer(newC); // This adds to memory
        
        // Persist all
        file_io.rewriteCustomers(customerList, "customers.csv");
        return true;
    }
    
    // Feature: List customers by District
    public Doubly<Customers> getCustomersByDistrict(String district) {
        Doubly<Customers> result = new Doubly<>();
        // In-order traversal of BST gives sorted list.
        // We can filter from the sorted list.
        // Or if BST structure allows range search, we could do that, but BinarySearch.java is basic.
        // So we traverse all and filter. (O(N) unfortunately, but O(LogN) insert).
        // Requirement said "BST (key=District+ID) - In-order Traversal".
        // In-order traversal of this BST will give customers grouped by District and sorted by ID.
        // So we just iterate the in-order list and pick the chunk matching the district.
        
        java.util.List<Customers> all = districtTree.inorder();
        boolean foundStart = false;
        for(Customers c : all) {
            if(c.getDistrict().equalsIgnoreCase(district)) {
                result.addLast(c);
                foundStart = true;
            } else {
                if(foundStart) break; // Optimization: Since sorted, if we passed the district, we are done.
                // Note: Case insensitive sorting? My comparator was IgnoreCase. So yes.
            }
        }
        return result;
    }
    
    // Top K (similar to Drivers, logic not specified for Customers but maybe Ride count? Or just ID?)
    // "Top k khách hàng ở đầu hoặc cuối danh sách" -> Just list head/tail k?
    public Doubly<Customers> getTopKCustomers(int k) {
        Doubly<Customers> result = new Doubly<>();
        for(int i=0; i<Math.min(k, customerList.getSize()); i++) {
            result.addLast(customerList.get(i));
        }
        return result;
    }
}