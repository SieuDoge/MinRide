package console_out;

import services.*;
import models.*;
import data_structures.LinkedList.Doubly;
import static console_out.ConsoleUtils.*;
import java.util.HashMap;
import java.util.Map;

public class DashboardMenu {
    private driver_service driverService;
    private customer_service customerService;
    private ride_service rideService;

    public DashboardMenu(driver_service ds, customer_service cs, ride_service rs) {
        this.driverService = ds;
        this.customerService = cs;
        this.rideService = rs;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            clearConsole();
            
            // --- 1. TÍNH TOÁN DỮ LIỆU TỔNG QUAN ---
            int totalDrivers = driverService.getDriverList().getSize();
            int totalCustomers = customerService.getCustomerList().getSize();
            int totalRides = rideService.getRideList().getSize();
            
            // Tính tổng doanh thu toàn hệ thống (Duyệt qua danh sách tài xế)
            double totalRevenue = 0;
            Doubly<Drivers> allDrivers = driverService.getDriverList();
            for(int i=0; i<allDrivers.getSize(); i++) {
                totalRevenue += allDrivers.get(i).getRevenue();
            }

            // --- 2. HIỂN THỊ UI ---
            printHeader("DASHBOARD - THỐNG KÊ HỆ THỐNG");
            
            printCenteredRow("║", "THỐNG KÊ TỔNG QUAN", YELLOW);
            printCenteredRow("║", "------------------", CYAN);
            
            String row1 = String.format("Drivers: %s%d%s   |   Customers: %s%d%s", 
                GREEN, totalDrivers, RESET, 
                BLUE, totalCustomers, RESET);
            printCenteredRow("║", row1, RESET);
            
            String row2 = String.format("Rides: %s%d%s   |   Revenue: %s%,.0f VND%s", 
                YELLOW, totalRides, RESET, 
                RED, totalRevenue, RESET);
            printCenteredRow("║", row2, RESET);
            printBorderLine("╠", "═", "╣");

            // Menu lựa chọn
            System.out.println("\nChọn chức năng xem báo cáo:");
            System.out.println("  1. Xem Top Tài xế theo Doanh thu (Top Income)");
            System.out.println("  2. Xem Top Tài xế theo Đánh giá (Top Rating)");
            System.out.println("  0. Quay lại");
            
            int choice = getIntInput(">> Mời chọn: ");
            
            if (choice == 0) {
                back = true;
            } else if (choice == 1) {
                showTopIncomeTable(totalRevenue);
            } else if (choice == 2) {
                showTopRatingTable();
            } else {
                printError("Lựa chọn không hợp lệ!");
                pause();
            }
        }
    }

    private void showTopIncomeTable(double totalSystemRevenue) {
        System.out.println(YELLOW + "\n--- BẢNG XẾP HẠNG DOANH THU (TOP INCOME) ---" + RESET);
        int k = getIntInput("Nhập số lượng tài xế muốn xem (Top K): ");
        
        Doubly<Drivers> topList = driverService.getTopIncomeDrivers(k);
        
        String[] headers = {"Rank", "ID", "Name", "Revenue (VND)", "% Total"};
        String[][] data = new String[topList.getSize()][5];
        
        for(int i=0; i<topList.getSize(); i++) {
            Drivers d = topList.get(i);
            double pct = (totalSystemRevenue > 0) ? (d.getRevenue() / totalSystemRevenue) * 100 : 0;
            
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = d.getId();
            data[i][2] = d.getName();
            data[i][3] = String.format("%,.0f", d.getRevenue());
            data[i][4] = String.format("%.1f%%", pct);
        }
        
        printTable(headers, data);
        pause();
    }

    private void showTopRatingTable() {
        System.out.println(YELLOW + "\n--- BẢNG XẾP HẠNG ĐÁNH GIÁ (TOP RATING) ---" + RESET);
        int k = getIntInput("Nhập số lượng tài xế muốn xem (Top K): ");
        
        // Sử dụng Heap logic có sẵn hoặc Sort
        Doubly<Drivers> topList = driverService.getTopKDrivers(k); // Hàm này trả về Ascending từ Heap
        
        // Cần đảo ngược danh sách để hiển thị cao nhất trước (Vì Heap trả về min trước nếu pop hết)
        // Tuy nhiên hàm getTopKDrivers hiện tại của bạn trả về theo thứ tự pop từ MinHeap
        // Nghĩa là: Rating Thấp nhất trong Top K -> Rating Cao nhất.
        // Để hiển thị đẹp, ta fill vào bảng ngược từ dưới lên.
        
        String[] headers = {"Rank", "ID", "Name", "Rating"};
        String[][] data = new String[topList.getSize()][4];
        
        int size = topList.getSize();
        for(int i=0; i<size; i++) {
            // Lấy từ danh sách (đang là Ascending của Top K)
            // Muốn Rank 1 là người cuối cùng trong list này
            Drivers d = topList.get(size - 1 - i); 
            
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = d.getId();
            data[i][2] = d.getName();
            data[i][3] = String.format("%.1f ★", d.getRating());
        }
        
        printTable(headers, data);
        pause();
    }
}
