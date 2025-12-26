package console_out;

import data_structures.LinkedList.Doubly;
import models.Customers;
import models.Drivers;
import models.Rides;
import services.booking_service;
import services.customer_service;
import services.driver_service;
import services.ride_service;
import utils.file_io;

import static console_out.ConsoleUtils.*;

public class BookingMenu {
    private booking_service bookingService;
    private ride_service rideService;
    private customer_service customerService;
    private driver_service driverService;

    public BookingMenu(booking_service bookingService, ride_service rideService, customer_service customerService, driver_service driverService) {
        this.bookingService = bookingService;
        this.rideService = rideService;
        this.customerService = customerService;
        this.driverService = driverService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            clearConsole();
            printHeader("ĐẶT XE & ĐIỀU PHỐI");
            printOption(1, "Tạo Yêu Cầu Đặt Xe (Booking)");
            printOption(2, "Điều phối tự động (Auto Dispatch)");
            printOption(3, "Lịch sử chuyến đi (Theo DriverID)");
            printOption(4, "Danh sách toàn bộ chuyến đi");
            printOption(5, "Tìm tài xế phù hợp (Advanced)");
            printOption(0, "Quay lại");
            printLine();

            int choice = getIntInput(">> Mời chọn: ");
            printLine();

            switch (choice) {
                case 1:
                    createBookingUI();
                    pause();
                    break;
                case 2:
                    processQueueUI();
                    pause();
                    break;
                case 3:
                    String drvId = getStringInput("Nhập ID Tài xế: ");
                    System.out.println(PURPLE + "Lịch sử của " + drvId + ":" + RESET);
                    displayRidesTable(rideService.getRidesByDriver(drvId));
                    pause();
                    break;
                case 4:
                    System.out.println(PURPLE + "[NHẬT KÝ CHUYẾN ĐI]" + RESET);
                    displayRidesTable(rideService.getRideList());
                    pause();
                    break;
                case 5:
                    findSuitableDriversUI();
                    pause();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    printError("Chức năng không tồn tại.");
                    pause();
            }
        }
    }
    
    private void findSuitableDriversUI() {
        System.out.println(YELLOW + "--- TÌM KIẾM TÀI XẾ NÂNG CAO ---" + RESET);
        String cusId = getStringInput("Nhập ID Khách hàng (để lấy vị trí): ");
        Customers c = customerService.getCustomerById(cusId);
        if (c == null) {
            printError("Không tìm thấy khách hàng.");
            return;
        }

        System.out.println("Vị trí khách hàng: (" + BOLD + c.getX() + ", " + c.getY() + RESET + ")");
        double radius = getDoubleInput("Nhập bán kính tìm kiếm (km): ");
        
        System.out.println("Chọn tiêu chí ưu tiên:");
        System.out.println("  1. Khoảng cách gần nhất (Distance)");
        System.out.println("  2. Đánh giá cao nhất (Rating)");
        System.out.println("  3. Gần nhất + Rating cao (Smart Sort)");
        int sortType = getIntInput(">> Chọn (1-3): ");
        
        Doubly<Drivers> result = driverService.findDriversWithCriteria(c.getX(), c.getY(), radius, sortType);
        
        if (result.getSize() == 0) {
            printError("Không tìm thấy tài xế nào phù hợp trong bán kính " + radius + "km.");
            return;
        }
        
        System.out.println(GREEN + ">> Tìm thấy " + result.getSize() + " tài xế phù hợp:" + RESET);
        
        // Display Table
        String[] headers = {"ID", "Name", "Rating", "Location", "Distance", "Status"};
        String[][] data = new String[result.getSize()][6];
        
        for(int i=0; i<result.getSize(); i++) {
            Drivers d = result.get(i);
            double dist = utils.distance.calculate(c.getX(), c.getY(), d.getX(), d.getY());
            
            data[i][0] = d.getId();
            data[i][1] = d.getName();
            data[i][2] = String.format("%.1f", d.getRating());
            data[i][3] = "(" + d.getX() + "," + d.getY() + ")";
            data[i][4] = String.format("%.2f km", dist);
            data[i][5] = d.isAvailable() ? "Available" : "Busy";
        }
        
        ConsoleUtils.printTable(headers, data);
    }
    
    private void displayRidesTable(Doubly<Rides> list) {
        if (list.getSize() == 0) {
            System.out.println("  (Danh sách trống)");
            return;
        }
        String[] headers = {"Ride ID", "Cus ID", "Drv ID", "Distance", "Fare (VND)"};
        String[][] data = new String[list.getSize()][5];
        
        for (int i = 0; i < list.getSize(); i++) {
            Rides r = list.get(i);
            data[i][0] = r.getRideId();
            data[i][1] = r.getCustomerId();
            data[i][2] = r.getDriverId();
            data[i][3] = String.format("%.2f km", r.getDistance());
            data[i][4] = String.format("%.0f", r.getFare());
        }
        ConsoleUtils.printTable(headers, data);
    }

    private void createBookingUI() {
        System.out.println(YELLOW + "--- TẠO BOOKING MỚI ---" + RESET);
        String cusId = getStringInput("Nhập ID Khách hàng: ");
        Customers c = customerService.getCustomerById(cusId);

        if (c == null) {
            printError("Không tìm thấy khách hàng này.");
            return;
        }

        System.out.println("Vị trí đón (Tự động lấy): (" + BOLD + c.getX() + ", " + c.getY() + RESET + ")");
        System.out.println("Nhập địa điểm đến:");
        int destX = getIntInput("   -> Tọa độ X: ");
        int destY = getIntInput("   -> Tọa độ Y: ");

        String rideId = "R_" + System.currentTimeMillis() % 10000;
        Rides newRide = bookingService.createBooking(rideId, cusId, c.getX(), c.getY(), destX, destY);
        bookingService.addToQueue(newRide);

        printSuccess("Yêu cầu đã được đẩy vào hàng chờ (Queue)!");
        System.out.println("   - Mã chuyến: " + rideId);
        System.out.printf("   - Khoảng cách: %.2f km\n", newRide.getDistance());
        System.out.printf("   - Giá cước: %.0f VND\n", newRide.getFare());
    }

    private void processQueueUI() {
        System.out.println(YELLOW + "--- ĐIỀU PHỐI TỰ ĐỘNG (AUTO DISPATCH) ---" + RESET);
        
        Rides[] pending = bookingService.getPendingRides();
        if (pending.length == 0) {
            printError("Hàng chờ rỗng! Không có yêu cầu nào.");
            return;
        }

        System.out.println("Đang xử lý " + pending.length + " yêu cầu trong hàng chờ...\n");
        
        int successCount = 0;
        int failCount = 0;

        for (Rides ride : pending) {
            System.out.print(">> Xử lý Ride " + BOLD + ride.getRideId() + RESET + "... ");
            
            // Auto Assign
            Drivers matchedDriver = bookingService.autoAssignDriver(ride, driverService, customerService);
            
            if (matchedDriver != null) {
                // Success
                System.out.println(GREEN + "THÀNH CÔNG!" + RESET);
                System.out.println("   -> Tài xế: " + matchedDriver.getName() + " (Rating: " + matchedDriver.getRating() + ")");
                
                // Finalize Ride Object
                Rides finalizedRide = new Rides(ride.getRideId(), ride.getCustomerId(), matchedDriver.getId(), ride.getDistance());
                
                // Persist Data
                rideService.addRide(finalizedRide);
                bookingService.removeRideFromQueue(ride);
                file_io.saveRide(finalizedRide, "rides.csv");
                
                successCount++;
            } else {
                // Fail
                System.out.println(RED + "THẤT BẠI" + RESET + " (Không tìm thấy tài xế phù hợp)");
                failCount++;
            }
            
            // Small delay for effect
            try { Thread.sleep(500); } catch (Exception e) {}
        }

        System.out.println("\n" + CYAN + "----------------------------------------" + RESET);
        System.out.println("KẾT QUẢ ĐIỀU PHỐI:");
        System.out.println(GREEN + "✔ Ghép thành công: " + successCount);
        System.out.println(RED + "✘ Chưa ghép được:  " + failCount);
        System.out.println(CYAN + "----------------------------------------" + RESET);
        
        if (failCount > 0) {
            System.out.println(ITALIC + "Các chuyến chưa ghép được sẽ được giữ lại trong hàng chờ." + RESET);
        }
    }
}
