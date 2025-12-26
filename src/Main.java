import services.booking_service;
import services.customer_service;
import services.driver_service;
import services.ride_service;
import utils.file_io;
import console_out.DriverMenu;
import console_out.CustomerMenu;
import console_out.BookingMenu;
import console_out.DashboardMenu;
import static console_out.ConsoleUtils.*;

public class Main {
    private static driver_service driverService = new driver_service();
    private static customer_service customerService = new customer_service();
    private static ride_service rideService = new ride_service();
    private static booking_service bookingService = new booking_service();

    public static void main(String[] args) {
        showIntro();
        loadDataWithAnimation();

        DriverMenu driverMenu = new DriverMenu(driverService);
        CustomerMenu customerMenu = new CustomerMenu(customerService, driverService);
        BookingMenu bookingMenu = new BookingMenu(bookingService, rideService, customerService, driverService);
        DashboardMenu dashboardMenu = new DashboardMenu(driverService, customerService, rideService);

        boolean running = true;
        while (running) {
            clearConsole();
            
            // NEW STANDARD MENU STYLE
            printHeader("HỆ THỐNG QUẢN LÝ ĐẶT XE MINRIDE");
            
            printOption(1, "Quản lý Tài xế (Drivers)");
            printCenteredRow("║", ITALIC + "   -> Thêm, Xóa, Sửa, Tìm kiếm...", YELLOW);
            
            printOption(2, "Quản lý Khách hàng (Customers)");
            printCenteredRow("║", ITALIC + "   -> Danh sách, Lọc Quận, Đánh giá...", YELLOW);
            
            printOption(3, "Đặt xe & Điều phối (Dispatch)");
            printCenteredRow("║", ITALIC + "   -> Booking, Ghép tài xế, Queue...", YELLOW);
            
            printOption(4, "Thống kê (Dashboard)");
            printCenteredRow("║", ITALIC + "   -> Tổng quan, Doanh thu, Báo cáo...", YELLOW);
            
            printBorderLine("╠", "═", "╣");
            printOption(5, RED + "Thoát chương trình (Exit)" + RESET);
            printLine(); // Bottom border

            System.out.println();
            int choice = getIntInput(">> Mời chọn chức năng (1-5): ");

            switch (choice) {
                case 1: driverMenu.show(); break;
                case 2: customerMenu.show(); break;
                case 3: bookingMenu.show(); break;
                case 4: dashboardMenu.show(); break;
                case 5:
                    System.out.println(YELLOW + "\n Đang lưu dữ liệu và thoát..." + RESET);
                    running = false;
                    break;
                default:
                    printError("Lựa chọn không hợp lệ!");
                    pause();
            }
        }
    }

    private static void loadDataWithAnimation() {
        System.out.print("Đang tải dữ liệu: [");
        for (int i = 0; i < 20; i++) {
            System.out.print("=");
            try { Thread.sleep(20); } catch (InterruptedException e) {}
        }
        System.out.println("] OK!\n");

        try {
            driverService.setDriverList(file_io.loadDrivers("drivers.csv"));
            customerService.setCustomerList(file_io.loadCustomers("customers.csv"));
            rideService.setRideList(file_io.loadRides("rides.csv"));

            System.out.println(GREEN + "✔ Đã tải: " + driverService.getDriverList().getSize() + " Drivers" + RESET);
            System.out.println(GREEN + "✔ Đã tải: " + customerService.getCustomerList().getSize() + " Customers" + RESET);
            System.out.println(GREEN + "✔ Đã tải: " + rideService.getRideList().getSize() + " Rides" + RESET);
        } catch (Exception e) {
            printError("Lỗi tải dữ liệu: " + e.getMessage());
        }
        pause();
    }

    private static void showIntro() {
        System.out.println(CYAN + BOLD);
        System.out.println("******************************************");
        System.out.println("* ỨNG DỤNG ĐẶT XE MINRIDE       *");
        System.out.println("******************************************");
        System.out.println(RESET);
    }
}
