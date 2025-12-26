import services.booking_service;
import services.customer_service;
import services.driver_service;
import services.ride_service;
import utils.file_io;
import console_out.DriverMenu;
import console_out.CustomerMenu;
import console_out.BookingMenu;
import console_out.DashboardMenu;
import console_out.AdminInterface;
import console_out.CustomerInterface;
import static console_out.ConsoleUtils.*;

public class Main {
    private static driver_service driverService = new driver_service();
    private static customer_service customerService = new customer_service();
    private static ride_service rideService = new ride_service();
    private static booking_service bookingService = new booking_service();

    public static void main(String[] args) {
        showIntro();
        loadDataWithAnimation();

        // Portal Loop
        boolean running = true;
        while (running) {
            clearConsole();
            printHeader("HỆ THỐNG MINRIDE - CỔNG ĐĂNG NHẬP");
            
            System.out.println(CYAN + "   Chào mừng đến với MinRide! Vui lòng chọn vai trò:" + RESET);
            System.out.println();
            
            printOption(1, "Quản Trị Viên (Administrator)");
            printCenteredRow("║", "Quản lý hệ thống, tài xế, điều phối...", ITALIC + YELLOW);
            
            printOption(2, "Khách Hàng (Customer)");
            printCenteredRow("║", "Đặt xe, xem lịch sử, cá nhân...", ITALIC + BLUE);
            
            printBorderLine("╠", "═", "╣");
            printOption(0, RED + "Thoát chương trình (Exit)" + RESET);
            printLine();

            int choice = getIntInput(">> Mời chọn (1, 2, 0): ");

            switch (choice) {
                case 1:
                    // Admin Flow
                    AdminInterface admin = new AdminInterface(driverService, customerService, rideService, bookingService);
                    admin.show();
                    break;
                case 2:
                    // Customer Flow
                    CustomerInterface customer = new CustomerInterface(driverService, customerService, rideService, bookingService);
                    customer.show();
                    break;
                case 0:
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
