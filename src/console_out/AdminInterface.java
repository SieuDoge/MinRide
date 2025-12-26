package console_out;

import services.*;
import static console_out.ConsoleUtils.*;

public class AdminInterface {
    private driver_service driverService;
    private customer_service customerService;
    private ride_service rideService;
    private booking_service bookingService;

    // Sub-menus
    private DriverMenu driverMenu;
    private CustomerMenu customerMenu;
    private BookingMenu bookingMenu;
    private DashboardMenu dashboardMenu;

    public AdminInterface(driver_service ds, customer_service cs, ride_service rs, booking_service bs) {
        this.driverService = ds;
        this.customerService = cs;
        this.rideService = rs;
        this.bookingService = bs;

        // Initialize Sub-menus
        this.driverMenu = new DriverMenu(ds);
        this.customerMenu = new CustomerMenu(cs, ds);
        this.bookingMenu = new BookingMenu(bs, rs, cs, ds);
        this.dashboardMenu = new DashboardMenu(ds, cs, rs);
    }

    public void show() {
        boolean running = true;
        while (running) {
            clearConsole();
            printHeader("QUẢN TRỊ VIÊN (ADMINISTRATOR)");
            
            printOption(1, "Quản lý Tài xế (Drivers)");
            printOption(2, "Quản lý Khách hàng (Customers)");
            printOption(3, "Xử lý Hàng chờ (Process Queue)");
            printOption(4, "Xem Lịch sử Chuyến đi (All Rides)");
            printOption(5, "Thống kê (Dashboard)");
            printOption(6, "Đặt xe hộ Khách (Manual Booking)");
            
            printBorderLine("╠", "═", "╣");
            printOption(0, RED + "Đăng xuất" + RESET);
            printLine();

            int choice = getIntInput(">> Mời chọn: ");

            switch (choice) {
                case 1: driverMenu.show(); break;
                case 2: customerMenu.show(); break;
                case 3: 
                    bookingMenu.processQueueUI(); // Public method
                    pause(); 
                    break;
                case 4:
                    System.out.println(PURPLE + "[DANH SÁCH TOÀN BỘ CHUYẾN ĐI]" + RESET);
                    bookingMenu.displayRidesTable(rideService.getRideList()); // Public method
                    pause();
                    break;
                case 5: dashboardMenu.show(); break;
                case 6: bookingMenu.createBookingUI(); break; // Admin booking manual
                case 0: running = false; break;
                default: printError("Lựa chọn không hợp lệ!"); pause();
            }
        }
    }
}
