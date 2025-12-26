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
                    break; // No pause here, updateUI has its own flow
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
                    displayCustomersTable(result);
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
        String id = getStringInput("Nhập ID khách hàng cần sửa: ");
        Customers c = customerService.getCustomerById(id);
        
        if (c == null) {
            printError("Không tìm thấy ID.");
            pause();
            return;
        }

        // Temp variables
        String newName = c.getName();
        String newDistrict = c.getDistrict();
        int newX = c.getX();
        int newY = c.getY();

        boolean editing = true;
        while (editing) {
            clearConsole();
            System.out.println(YELLOW + "--- CHỈNH SỬA THÔNG TIN KHÁCH HÀNG ---" + RESET);
            
            // Show Live Preview Card
            printInfoCard("THÔNG TIN ĐANG SỬA (PREVIEW)",
                new String[]{"ID", "Tên", "Quận", "Vị Trí"},
                new String[]{c.getId(), newName, newDistrict, "(" + newX + "," + newY + ")"}
            );

            System.out.println("\nChọn thông tin cần sửa:");
            System.out.println("  1. Sửa Tên");
            System.out.println("  2. Sửa Quận");
            System.out.println("  3. Sửa Tọa độ (Vị trí)");
            System.out.println(GREEN + "  0. Lưu thay đổi & Thoát" + RESET);
            System.out.println(RED + "  9. Hủy bỏ (Không lưu)" + RESET);
            
            int choice = getIntInput(">> Chọn: ");
            
            switch (choice) {
                case 1:
                    newName = getStringInput(">> Nhập tên mới: ");
                    break;
                case 2:
                    newDistrict = getStringInput(">> Nhập quận mới: ");
                    break;
                case 3:
                    newX = getIntInput(">> Nhập X mới: ");
                    newY = getIntInput(">> Nhập Y mới: ");
                    break;
                case 0:
                    customerService.updateCustomer(id, newName, newDistrict, newX, newY);
                    printSuccess("Đã lưu thông tin khách hàng!");
                    editing = false;
                    pause();
                    break;
                case 9:
                    System.out.println("Đã hủy bỏ thao tác.");
                    editing = false;
                    pause();
                    break;
                default:
                    printError("Chọn sai!");
            }
        }
    }
}
