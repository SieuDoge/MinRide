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
        clearConsole();
        
        // --- 1. TÍNH TOÁN DỮ LIỆU ---
        int totalDrivers = driverService.getDriverList().getSize();
        int totalCustomers = customerService.getCustomerList().getSize();
        int totalRides = rideService.getRideList().getSize();
        
        double totalRevenue = 0;
        Map<String, Double> driverRevenue = new HashMap<>();
        Map<String, Integer> driverRideCount = new HashMap<>();
        
        Doubly<Rides> rides = rideService.getRideList();
        for(int i=0; i<rides.getSize(); i++) {
            Rides r = rides.get(i);
            totalRevenue += r.getFare();
            driverRevenue.put(r.getDriverId(), driverRevenue.getOrDefault(r.getDriverId(), 0.0) + r.getFare());
            driverRideCount.put(r.getDriverId(), driverRideCount.getOrDefault(r.getDriverId(), 0) + 1);
        }

        String topDriverName = "N/A";
        double topRevenue = 0;
        for (Map.Entry<String, Double> entry : driverRevenue.entrySet()) {
            if (entry.getValue() > topRevenue) {
                topRevenue = entry.getValue();
                Drivers d = driverService.getDriverById(entry.getKey());
                if (d != null) topDriverName = d.getName();
            }
        }

        // --- 2. HIỂN THỊ UI (Dùng Theme Engine) ---

        printHeader("DASHBOARD - THỐNG KÊ HỆ THỐNG");
        
        // Custom Draw for Stats (KPIs)
        // Manual layout to fit inside standard width, using getVisibleLength logic if needed, 
        // OR simply list them as options. But KPI grid is nicer.
        // Let's print a simple summary block inside the borders.

        printCenteredRow("║", "THỐNG KÊ TỔNG QUAN", YELLOW);
        printCenteredRow("║", "------------------", CYAN);
        
        // Row 1: Drivers & Customers
        String row1 = String.format("Drivers: %s%d%s   |   Customers: %s%d%s", 
            GREEN, totalDrivers, RESET, 
            BLUE, totalCustomers, RESET);
        printCenteredRow("║", row1, RESET);
        
        // Row 2: Rides & Revenue
        String row2 = String.format("Rides: %s%d%s   |   Revenue: %s%,.0f VND%s", 
            YELLOW, totalRides, RESET, 
            RED, totalRevenue, RESET);
        printCenteredRow("║", row2, RESET);
        
        printBorderLine("╠", "═", "╣");
        
        // Top Performer Section
        if (topRevenue > 0) {
            printCenteredRow("║", "🏆 TOP PERFORMER 🏆", PURPLE);
            printCenteredRow("║", topDriverName, BOLD + GREEN);
            printCenteredRow("║", String.format("(%,.0f VND)", topRevenue), ITALIC);
        } else {
            printCenteredRow("║", "Chưa có dữ liệu doanh thu", ITALIC);
        }
        
        printLine(); // Bottom Border

        System.out.println();
        System.out.println(CYAN + " CHI TIẾT DOANH THU" + RESET);
        
        // Use generic table
        String[] headers = {"ID", "Name", "Rides", "Revenue", "%"};
        // Convert map to String[][]
        Doubly<Drivers> driverList = driverService.getDriverList();
        String[][] data = new String[driverList.getSize()][5];
        
        // Sort for display
        Drivers[] arr = new Drivers[driverList.getSize()];
        for(int i=0; i<driverList.getSize(); i++) arr[i] = driverList.get(i);
        // Sort desc revenue
        for(int i=0; i<arr.length-1; i++){
            for(int j=0; j<arr.length-i-1; j++){
                double r1 = driverRevenue.getOrDefault(arr[j].getId(), 0.0);
                double r2 = driverRevenue.getOrDefault(arr[j+1].getId(), 0.0);
                if(r1 < r2) {
                    Drivers temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        
        for(int i=0; i<arr.length; i++) {
            Drivers d = arr[i];
            double rev = driverRevenue.getOrDefault(d.getId(), 0.0);
            int count = driverRideCount.getOrDefault(d.getId(), 0);
            double pct = (totalRevenue > 0) ? (rev/totalRevenue)*100 : 0;
            
            data[i][0] = d.getId();
            data[i][1] = d.getName();
            data[i][2] = String.valueOf(count);
            data[i][3] = String.format("%,.0f", rev);
            data[i][4] = String.format("%.1f%%", pct);
        }
        
        printTable(headers, data);

        System.out.println("\n" + ITALIC + " (Nhấn Enter để quay lại...)" + RESET);
        pause();
    }
}
