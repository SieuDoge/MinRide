package console_out;

import services.*;
import models.Customers;
import data_structures.LinkedList.Doubly;
import static console_out.ConsoleUtils.*;

public class CustomerInterface {
    private driver_service driverService;
    private customer_service customerService;
    private ride_service rideService;
    private booking_service bookingService;
    private BookingMenu bookingMenu; // Helper for tables/booking flow

    public CustomerInterface(driver_service ds, customer_service cs, ride_service rs, booking_service bs) {
        this.driverService = ds;
        this.customerService = cs;
        this.rideService = rs;
        this.bookingService = bs;
        this.bookingMenu = new BookingMenu(bs, rs, cs, ds);
    }

    public void show() {
        clearConsole();
        printHeader("KHÁCH HÀNG (CUSTOMER LOGIN)");
        
        System.out.println(YELLOW + "Vui lòng đăng nhập để tiếp tục." + RESET);
        String id = getStringInput(">> Nhập ID của bạn (VD: C001, 1...): ");
        
        Customers me = customerService.getCustomerById(id);
        
        if (me == null) {
            printError("Không tìm thấy ID khách hàng này!");
            System.out.println("Bạn có muốn đăng ký mới không? (y/n)");
            if (getStringInput(">> ").equalsIgnoreCase("y")) {
                // Register logic (Quick inline)
                String name = getStringInput("Tên của bạn: ");
                String dist = getStringInput("Quận: ");
                int x = getIntInput("Tọa độ X: ");
                int y = getIntInput("Tọa độ Y: ");
                me = new Customers(id, name, dist, x, y);
                customerService.addCustomer(me);
                utils.file_io.saveCustomer(me, "customers.csv");
                printSuccess("Đăng ký thành công! Đang đăng nhập...");
                try { Thread.sleep(1000); } catch(Exception e){}
            } else {
                return;
            }
        }
        
        // Logged In
        showCustomerMenu(me);
    }

    private void showCustomerMenu(Customers me) {
        boolean running = true;
        while (running) {
            clearConsole();
            
            // Personal Header
            System.out.println(CYAN + "Xin chào, " + BOLD + me.getName() + RESET);
            System.out.println("Vị trí: (" + me.getX() + "," + me.getY() + ") | Quận: " + me.getDistrict());
            System.out.println("----------------------------------------");
            
            printHeader("MENU KHÁCH HÀNG");
            printOption(1, "Đặt xe (Book Ride)");
            printOption(2, "Lịch sử chuyến đi của tôi");
            printOption(3, "Thông tin cá nhân");
            printBorderLine("╠", "═", "╣");
            printOption(0, RED + "Đăng xuất" + RESET);
            printLine();

            int choice = getIntInput(">> Mời chọn: ");
            
            switch (choice) {
                case 1:
                    bookingMenu.createBookingUI(me); // Personalized booking
                    pause();
                    break;
                case 2:
                    System.out.println(PURPLE + "[LỊCH SỬ CHUYẾN ĐI CỦA BẠN]" + RESET);
                    // Filter rides manually or via service if available. 
                    // Assuming ride_service has no getRidesByCustomer, we filter here or add one.
                    // For now let's filter manually from full list.
                    displayMyHistory(me.getId());
                    pause();
                    break;
                case 3:
                    showMyProfile(me);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    printError("Lựa chọn không hợp lệ!");
                    pause();
            }
        }
    }
    
    private void displayMyHistory(String cusId) {
        Doubly<models.Rides> all = rideService.getRideList();
        Doubly<models.Rides> myRides = new Doubly<>();
        
        for(int i=0; i<all.getSize(); i++) {
            models.Rides r = all.get(i);
            if (r.getCustomerId().equalsIgnoreCase(cusId)) {
                myRides.addLast(r);
            }
        }
        
        bookingMenu.displayRidesTable(myRides);
    }
    
    private void showMyProfile(Customers me) {
        clearConsole();
        printInfoCard("HỒ SƠ CÁ NHÂN",
            new String[]{"ID", "Họ Tên", "Quận", "Tọa độ"},
            new String[]{me.getId(), me.getName(), me.getDistrict(), "(" + me.getX() + "," + me.getY() + ")"}
        );
        
        System.out.println("\nBạn có muốn cập nhật thông tin không? (y/n)");
        if (getStringInput(">> ").equalsIgnoreCase("y")) {
            // Edit flow
            String newName = getStringInput("Tên mới (" + me.getName() + "): ");
            if(newName.isEmpty()) newName = me.getName();
            
            String newDist = getStringInput("Quận mới (" + me.getDistrict() + "): ");
            if(newDist.isEmpty()) newDist = me.getDistrict();
            
            // For simple coords, user might skip. Simplified logic here.
            System.out.println("Cập nhật tọa độ (Nhập -1 để giữ nguyên):");
            int nx = getIntInput("X: ");
            int ny = getIntInput("Y: ");
            
            if (nx == -1) nx = me.getX();
            if (ny == -1) ny = me.getY();
            
            customerService.updateCustomer(me.getId(), newName, newDist, nx, ny);
            printSuccess("Cập nhật thành công! Thông tin mới sẽ hiển thị lần sau.");
            pause();
        }
    }
}
