package utils;

import data_structures.LinkedList.Singly;
import models.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class file_io {
    private static final String DATA_Dir = "core/src/data/";

    // Read Drivers.csv
    public static Singly<Drivers> loadDrivers(String filename) {
        Singly<Drivers> list = new Singly<>();
        String filePath = DATA_Dir + filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true; // Skip Header Flag

            while ((line = br.readLine()) != null) {
                // Bỏ qua dòng tiêu đề hoặc dòng trống
                if (isHeader || line.trim().isEmpty()) {
                    isHeader = false;
                    continue;
                }

                try {
                    // Cấu trúc CSV: ID, Name, Rating, x, y [cite: 39]
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;

                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    double rating = Double.parseDouble(parts[2].trim());
                    int x = Integer.parseInt(parts[3].trim());
                    int y = Integer.parseInt(parts[4].trim());

                    // Tạo đối tượng Driver và thêm vào list
                    Drivers driver = new Drivers(id, name, rating, x, y);
                    list.addLast(driver);

                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng số ở dòng: " + line);
                }
            }
            System.out.println("Đã tải xong danh sách Tài xế từ " + filename);

        } catch (IOException e) {
            System.err.println("Lỗi đọc file Driver: " + e.getMessage());
        }

        return list;
    }

    // Read Customer.csv

    public static Singly<Customers> loadCustomers(String filename) {
        Singly<Customers> list = new Singly<>();
        String filePath = DATA_Dir + filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true; // Skip Header Flag

            while ((line = br.readLine()) != null) {
                // Bỏ qua dòng tiêu đề hoặc dòng trống
                if (isHeader || line.trim().isEmpty()) {
                    isHeader = false;
                    continue;
                }

                try {
                    // Cấu trúc CSV: ID, Name, x, y
                    String[] parts = line.split(",");
                    if (parts.length < 4) continue;

                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    int x = Integer.parseInt(parts[2].trim());
                    int y = Integer.parseInt(parts[3].trim());

                    // Tạo đối tượng customer và thêm vào list
                    Customers customer = new Customers(id, name, x, y);
                    list.addLast(customer);

                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng số ở dòng: " + line);
                }
            }
            System.out.println("Đã tải xong danh sách Customer từ " + filename);

        } catch (IOException e) {
            System.err.println("Lỗi đọc file Customer: " + e.getMessage());
        }

        return list;
    }

    // Read rides.csv

    public static Singly<Rides> loadRides(String filename) {
        Singly<Rides> list = new Singly<>();
        String filePath = DATA_Dir + filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader || line.trim().isEmpty()) {
                    isHeader = false;
                    continue;
                }

                try {
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;
                    String rideID = parts[0].trim();
                    String cusID = parts[1].trim();
                    String drvId = parts[2].trim();
                    double distance = Double.parseDouble(parts[3].trim());
                    double fare = Double.parseDouble(parts[4].trim());
                    Rides ride = new Rides(rideID, cusID, drvId, distance, fare);

                    list.addLast(ride);

                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng số ở dòng: " + line);
                }
            }
            System.out.println("Đã tải xong danh sách Rides từ " + filename);
        } catch (IOException e) {
            System.err.println("Lỗi đọc file Rides: " + e.getMessage());
        } // Đóng catch
        return list;
    } // Đóng hàm
}
