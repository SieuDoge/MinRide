package console_out;

import data_structures.LinkedList.Doubly;
import models.Customers;
import services.customer_service;
import services.driver_service;
import utils.file_io;

import static console_out.ConsoleUtils.*;

public class CustomerMenu {
    private customer_service customerService;
    private driver_service driverService;

    public CustomerMenu(customer_service customerService, driver_service driverService) {
        this.customerService = customerService;
        this.driverService = driverService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            clearConsole();
            printHeader("QUẢN LÝ KHÁCH HÀNG");
            printOption(1, "Hiển thị danh sách");
            printOption(2, "Thêm khách hàng");
            printOption(3, "Cập nhật thông tin");
            printOption(4, "Xóa khách hàng");
            printOption(5, "Tìm kiếm (Tên/ID)");
            printOption(6, "Lọc theo Quận (District)");
            printOption(7, "Đánh giá tài xế");
            printOption(0, "Quay lại");
            printLine();

            int choice = getIntInput(">> Mời chọn: ");
            printLine();

            switch (choice) {
                case 1:
                    System.out.println(PURPLE + "[DANH SÁCH KHÁCH HÀNG]" + RESET);
                    displayCustomersTable(customerService.getCustomerList());
                    pause();
                    break;
                case 2:
                    addCustomerUI();
                    pause();
                    break;
                case 3:
                    updateCustomerUI();
                    pause();
                    break;
                case 4:
                    String delId = getStringInput("Nhập ID cần xóa: ");
                    if (customerService.deleteCustomer(delId)) printSuccess("Xóa khách hàng thành công!");
                    else printError("Không tìm thấy ID.");
                    pause();
                    break;
                case 5:
                    String q = getStringInput("Nhập Tên hoặc ID: ");
                    System.out.println(PURPLE + "Kết quả:" + RESET);
                    displayCustomersTable(customerService.searchCustomer(q));
                    pause();
                    break;
                case 6:
                    String dist = getStringInput("Nhập tên Quận (VD: Q1, BT): ");
                    Doubly<Customers> result = customerService.getCustomersByDistrict(dist);
                    int total = result.getSize();

                    System.out.println(PURPLE + ">> Tìm thấy tổng cộng " + BOLD + total + RESET + PURPLE + " khách hàng tại " + dist + RESET);

                    if (total == 0) {
                        printError("Không tìm thấy khách hàng nào trong quận này.");
                        pause();
                        break;
                    }

                    int currentCount = 0;
                    int pageSize = 10;
                    boolean keepViewing = true;

                    while (keepViewing && currentCount < total) {
                        // Lấy danh sách cho trang hiện tại
                        Doubly<Customers> pageList = new Doubly<>();
                        int limit = Math.min(currentCount + pageSize, total);

                        for (int i = currentCount; i < limit; i++) {
                            pageList.addLast(result.get(i));
                        }

                        System.out.println(CYAN + "\n--- Đang hiển thị từ " + (currentCount + 1) + " đến " + limit + " (Trong tổng số " + total + ") ---" + RESET);
                        displayCustomersTable(pageList);

                        currentCount = limit;

                        // Kiểm tra nếu còn dữ liệu để xem
                        if (currentCount < total) {
                            String choiceNext = getStringInput(">> Bạn có muốn xem thêm 10 khách hàng tiếp theo? (y/n): ");
                            if (!choiceNext.equalsIgnoreCase("y")) {
                                keepViewing = false;
                            }
                        } else {
                            System.out.println(GREEN + ">> Đã hiển thị hết danh sách." + RESET);
                            keepViewing = false;
                        }
                    }
                    pause();
                    break;
                case 7:
                    rateDriverUI();
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
    
    private void rateDriverUI() {
        System.out.println(YELLOW + "--- ĐÁNH GIÁ TÀI XẾ ---" + RESET);
        
        String drvId = getStringInput("Nhập ID Tài xế muốn đánh giá: ");
        if (driverService.getDriverById(drvId) == null) {
            printError("Không tìm thấy tài xế có ID: " + drvId);
            return;
        }
        
        int stars = getIntInput("Nhập số sao (1-5): ");
        if (stars < 1 || stars > 5) {
            printError("Số sao phải từ 1 đến 5.");
            return;
        }
        
        driverService.rateDriver(drvId, stars);
        printSuccess("Đã đánh giá tài xế " + stars + " sao!");
    }

    private void displayCustomersTable(Doubly<Customers> list) {
        if (list.getSize() == 0) {
            System.out.println("  (Danh sách trống)");
            return;
        }
        String[] headers = {"ID", "Name", "District", "Location (x,y)"};
        String[][] data = new String[list.getSize()][4];
        
        for (int i = 0; i < list.getSize(); i++) {
            Customers c = list.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getDistrict();
            data[i][3] = "(" + c.getX() + ", " + c.getY() + ")";
        }
        ConsoleUtils.printTable(headers, data);
    }

    private void addCustomerUI() {
        System.out.println(YELLOW + "--- THÊM KHÁCH HÀNG ---" + RESET);
        String id = customerService.getNextId();
        System.out.println("ID Tự động: " + BOLD + id + RESET);
        String name = getStringInput("Tên khách hàng: ");
        String district = getStringInput("Quận (VD: Q1): ");
        int x = getIntInput("Tọa độ X: ");
        int y = getIntInput("Tọa độ Y: ");

        Customers c = new Customers(id, name, district, x, y);
        customerService.addCustomer(c);
        file_io.saveCustomer(c, "customers.csv");
        printSuccess("Thêm khách hàng [" + name + "] thành công!");
    }

    private void updateCustomerUI() {
        String id = getStringInput("Nhập ID khách hàng: ");
        if (customerService.getCustomerById(id) == null) {
            printError("Không tìm thấy ID.");
            return;
        }
        String name = getStringInput("Tên mới: ");
        String district = getStringInput("Quận mới: ");
        int x = getIntInput("X mới: ");
        int y = getIntInput("Y mới: ");

        customerService.updateCustomer(id, name, district, x, y);
        printSuccess("Cập nhật thành công!");
    }
}
