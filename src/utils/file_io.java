package utils;

import data_structures.LinkedList.Doubly;
import models.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class file_io {
    private static final String DATA_Dir = "src/data/";

    // Read Drivers.csv
    public static Doubly<Drivers> loadDrivers(String filename) {
        Doubly<Drivers> list = new Doubly<>();
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
                    // Cấu trúc CSV: ID, Name, Rating, x, y, Revenue(Optional)
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;

                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    double rating = Double.parseDouble(parts[2].trim());
                    int x = Integer.parseInt(parts[3].trim());
                    int y = Integer.parseInt(parts[4].trim());
                    
                    double revenue = 0.0;
                    if (parts.length >= 6) {
                        revenue = Double.parseDouble(parts[5].trim());
                    }

                    // Tạo đối tượng Driver và thêm vào list
                    Drivers driver = new Drivers(id, name, rating, x, y, revenue);
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

    public static Doubly<Customers> loadCustomers(String filename) {
        Doubly<Customers> list = new Doubly<>();
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
                    // Cấu trúc CSV: ID, Name, District, x, y
                    String[] parts = line.split(",");
                    if (parts.length < 5) continue;

                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String district = parts[2].trim();
                    int x = Integer.parseInt(parts[3].trim());
                    int y = Integer.parseInt(parts[4].trim());

                    // Tạo đối tượng customer và thêm vào list
                    Customers customer = new Customers(id, name, district, x, y);
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

    public static Doubly<Rides> loadRides(String filename) {
        Doubly<Rides> list = new Doubly<>();
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
        }
        return list;
    }


    // ================== GHI FILE (APPEND) ==================

    public static void saveDriver(Drivers driver, String filename) {
        String filePath = DATA_Dir + filename;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            // Format: ID, Name, Rating, x, y, Revenue
            String line = driver.getId() + "," + driver.getName() + "," + driver.getRating() + "," +
                          driver.getX() + "," + driver.getY() + "," + driver.getRevenue();
            bw.write(line);
            bw.newLine();
            System.out.println("Đã lưu driver: " + driver.getName());
        } catch (IOException e) {
        System.err.println("Lỗi ghi file Driver: " + e.getMessage());
        }
    }

    public static void saveCustomer(Customers customer, String filename) {
        String filePath = DATA_Dir + filename;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            // Format: ID, Name, District, x, y
            String line = customer.getId() + "," + customer.getName() + "," +
                          customer.getDistrict() + "," +
                          customer.getX() + "," + customer.getY();
            bw.write(line);
            bw.newLine();
            System.out.println("Đã lưu customer: " + customer.getName());
        } catch (IOException e) {
            System.err.println("Lỗi ghi file Customer: " + e.getMessage());
        }
    }

    public static void saveRide(Rides ride, String filename) {
        String filePath = DATA_Dir + filename;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            String line = ride.getRideId() + "," + ride.getCustomerId() + "," + ride.getDriverId() + "," +
                          ride.getDistance() + "," + ride.getFare();
            bw.write(line);
            bw.newLine();
            System.out.println("Đã lưu ride: " + ride.getRideId());
        } catch (IOException e) {
            System.err.println("Lỗi ghi file Ride: " + e.getMessage());
        }
    }

    // ================== GHI LẠI TOÀN BỘ FILE (OVERWRITE) ==================

    public static void rewriteDrivers(Doubly<Drivers> list, String filename) {
        String filePath = DATA_Dir + filename;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write Header
            bw.write("ID,Name,Rating,x,y,Revenue");
            bw.newLine();

            for (int i = 0; i < list.getSize(); i++) {
                Drivers d = list.get(i);
                String line = d.getId() + "," + d.getName() + "," + d.getRating() + "," +
                        d.getX() + "," + d.getY() + "," + d.getRevenue();
                bw.write(line);
                bw.newLine();
            }
            // System.out.println("Đã cập nhật file " + filename);
        } catch (IOException e) {
            System.err.println("Lỗi ghi lại file Drivers: " + e.getMessage());
        }
    }

    public static void rewriteCustomers(Doubly<Customers> list, String filename) {
        String filePath = DATA_Dir + filename;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write Header
            bw.write("ID,Name,District,x,y");
            bw.newLine();

            for (int i = 0; i < list.getSize(); i++) {
                Customers c = list.get(i);
                String line = c.getId() + "," + c.getName() + "," + c.getDistrict() + "," +
                        c.getX() + "," + c.getY();
                bw.write(line);
                bw.newLine();
            }
            // System.out.println("Đã cập nhật file " + filename);
        } catch (IOException e) {
            System.err.println("Lỗi ghi lại file Customers: " + e.getMessage());
        }
    }

    public static void rewriteQueue(data_structures.StackNQueue.Queue<Rides> queue, String filename) {
        String filePath = DATA_Dir + filename;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            // Write Header
            bw.write("RideID,CusID,DrvID,Distance,Fare");
            bw.newLine();

            Object[] objs = queue.toArray();
            for (Object obj : objs) {
                Rides r = (Rides) obj;
                String line = r.getRideId() + "," + r.getCustomerId() + "," + r.getDriverId() + "," +
                        r.getDistance() + "," + r.getFare();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Lỗi ghi lại file Queue: " + e.getMessage());
        }
    }

    public static data_structures.StackNQueue.Queue<Rides> loadQueue(String filename) {
        data_structures.StackNQueue.Queue<Rides> queue = new data_structures.StackNQueue.Queue<>(100);
        String filePath = DATA_Dir + filename;
        
        // If file doesn't exist, return empty queue
        java.io.File f = new java.io.File(filePath);
        if(!f.exists()) return queue;

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
                    queue.enqueue(ride);

                } catch (NumberFormatException e) {
                    System.err.println("Lỗi định dạng số ở dòng (Queue): " + line);
                }
            }
            System.out.println("Đã tải xong hàng chờ từ " + filename);
        } catch (IOException e) {
            System.err.println("Lỗi đọc file Queue: " + e.getMessage());
        }
        return queue;
    }
}
