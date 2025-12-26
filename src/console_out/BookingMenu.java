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
            printOption(1, "Đặt Xe Mới (Booking Now)");
            printOption(2, "Xử lý Hàng Chờ (Process Queue)");
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
    
    public void processQueueUI() {
        System.out.println(YELLOW + "--- XỬ LÝ HÀNG CHỜ (RETRY QUEUE) ---" + RESET);
        
        Rides[] pending = bookingService.getPendingRides();
        if (pending.length == 0) {
            printError("Hàng chờ trống! Không có yêu cầu nào cần xử lý.");
            return;
        }

        System.out.println("Tìm thấy " + BOLD + pending.length + RESET + " yêu cầu đang chờ.\n");
        
        int success = 0;
        int fail = 0;

        for (Rides ride : pending) {
            System.out.println(CYAN + ">> Đang thử lại đơn: " + ride.getRideId() + "..." + RESET);
            
            // Retry Auto Assign
            Drivers matchedDriver = bookingService.autoAssignDriver(ride, driverService, customerService);
            
            if (matchedDriver != null) {
                // SUCCESS
                success++;
                Customers c = customerService.getCustomerById(ride.getCustomerId());
                String cusName = (c != null) ? c.getName() : "Unknown";

                printInfoCard("GHÉP THÀNH CÔNG (QUEUE MATCHED)",
                    new String[]{"Mã Chuyến", "Khách Hàng", "Tài Xế", "Vị Trí Xe", "Khoảng Cách", "Cước Phí"},
                    new String[]{
                        ride.getRideId(),
                        cusName,
                        matchedDriver.getName() + " (ID: " + matchedDriver.getId() + ")",
                        "(" + matchedDriver.getX() + "," + matchedDriver.getY() + ")",
                        String.format("%.2f km", ride.getDistance()),
                        String.format("%,.0f VND", ride.getFare())
                    }
                );
                
                // Process Data
                driverService.addRevenue(matchedDriver.getId(), ride.getFare());
                
                Rides finalizedRide = new Rides(ride.getRideId(), ride.getCustomerId(), matchedDriver.getId(), ride.getDistance());
                rideService.addRide(finalizedRide);
                bookingService.removeRideFromQueue(ride);
                file_io.saveRide(finalizedRide, "rides.csv");
                
                System.out.println(GREEN + "   ✔ Đã xóa khỏi hàng chờ và tạo chuyến đi." + RESET);
                
                // --- RATING FLOW ---
                String rateChoice = getStringInput("   >> Đánh giá tài xế? (y/n): ");
                if (rateChoice.equalsIgnoreCase("y")) {
                    int stars = getIntInput("      -> Số sao (1-5): ");
                    if (stars >= 1 && stars <= 5) {
                        driverService.rateDriver(matchedDriver.getId(), stars);
                        System.out.println(GREEN + "      ✔ Đã đánh giá!" + RESET);
                    }
                }

            } else {
                // FAIL AGAIN
                fail++;
                System.out.println(RED + "   ✘ Vẫn chưa tìm thấy tài xế phù hợp. (Tiếp tục chờ)" + RESET);
            }
            System.out.println();
            try { Thread.sleep(500); } catch (Exception e) {}
        }
        
        System.out.println(YELLOW + "----------------------------------------" + RESET);
        System.out.println("KẾT QUẢ XỬ LÝ:");
        System.out.println(GREEN + "✔ Ghép được: " + success);
        System.out.println(RED + "✘ Còn lại:   " + fail);
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

    public void displayRidesTable(Doubly<Rides> list) {
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

    public void createBookingUI() {
        System.out.println(YELLOW + "--- TẠO BOOKING MỚI (ADMIN MODE) ---" + RESET);
        String cusId = getStringInput("Nhập ID Khách hàng: ");
        Customers c = customerService.getCustomerById(cusId);

        if (c == null) {
            printError("Không tìm thấy khách hàng này.");
            return;
        }
        createBookingUI(c);
    }

    public void createBookingUI(Customers c) {
        // Show Customer Card
        printInfoCard("THÔNG TIN KHÁCH HÀNG", 
            new String[]{"Tên", "Quận", "Vị trí", "ID"}, 
            new String[]{c.getName(), c.getDistrict(), "(" + c.getX() + "," + c.getY() + ")", c.getId()}
        );
        System.out.println();

        System.out.println("Nhập địa điểm đến:");
        int destX = getIntInput("   -> Tọa độ X: ");
        int destY = getIntInput("   -> Tọa độ Y: ");

        String rideId = "R_" + System.currentTimeMillis() % 10000;
        Rides newRide = bookingService.createBooking(rideId, c.getId(), c.getX(), c.getY(), destX, destY);
        bookingService.addToQueue(newRide);

        System.out.println("\nĐang tìm tài xế gần nhất...");
        try { Thread.sleep(1000); } catch (Exception e) {} // Simulate searching

        // IMMEDIATE AUTO-DISPATCH
        Drivers matchedDriver = bookingService.autoAssignDriver(newRide, driverService, customerService);

        if (matchedDriver != null) {
            // SUCCESS: DRIVER FOUND
            printInfoCard("GHÉP XE THÀNH CÔNG (MATCH FOUND)",
                new String[]{"Mã Chuyến", "Khách Hàng", "Tài Xế", "Vị Trí Xe", "Khoảng Cách", "Cước Phí"},
                new String[]{
                    newRide.getRideId(),
                    c.getName(),
                    matchedDriver.getName() + " (ID: " + matchedDriver.getId() + ")",
                    "(" + matchedDriver.getX() + "," + matchedDriver.getY() + ") - " + String.format("%.1f★", matchedDriver.getRating()),
                    String.format("%.2f km", newRide.getDistance()),
                    String.format("%,.0f VND", newRide.getFare())
                }
            );

            // Add Revenue
            driverService.addRevenue(matchedDriver.getId(), newRide.getFare());
            
            // Finalize Ride
            Rides finalizedRide = new Rides(newRide.getRideId(), newRide.getCustomerId(), matchedDriver.getId(), newRide.getDistance());
            rideService.addRide(finalizedRide);
            
            // Remove from Queue
            bookingService.removeRideFromQueue(newRide); 
            file_io.saveRide(finalizedRide, "rides.csv");

            printSuccess("Tài xế đang đến đón!");
            
            // --- RATING FLOW ---
            System.out.println();
            String rateChoice = getStringInput(">> Chuyến đi hoàn tất! Bạn có muốn đánh giá tài xế ngay? (y/n): ");
            if (rateChoice.equalsIgnoreCase("y")) {
                int stars = getIntInput("   -> Nhập số sao (1-5): ");
                if (stars >= 1 && stars <= 5) {
                    driverService.rateDriver(matchedDriver.getId(), stars);
                    printSuccess("Cảm ơn bạn đã đánh giá " + stars + " sao!");
                } else {
                    printError("Số sao không hợp lệ (Bỏ qua).");
                }
            }

        } else {
            // FAILURE: NO DRIVER
             printInfoCard("CHƯA TÌM THẤY TÀI XẾ",
                new String[]{"Mã Chuyến", "Trạng Thái", "Lý Do"},
                new String[]{
                    newRide.getRideId(),
                    RED + "Đang chờ (Pending)" + YELLOW,
                    "Không có tài xế phù hợp gần đây."
                }
            );
            System.out.println(ITALIC + "Yêu cầu của bạn đã được lưu vào hàng chờ. Hệ thống sẽ thử lại sau." + RESET);
        }
    }
}
