package console_out;

import data_structures.LinkedList.Doubly;
import models.Drivers;
import services.driver_service;
import utils.file_io;

import static console_out.ConsoleUtils.*;

public class DriverMenu {
    private driver_service driverService;

    public DriverMenu(driver_service driverService) {
        this.driverService = driverService;
    }

    public void show() {
        boolean back = false;
        while (!back) {
            clearConsole();
            printHeader("QUẢN LÝ TÀI XẾ");
            printOption(1, "Hiển thị danh sách");
            printOption(2, "Thêm tài xế mới");
            printOption(3, "Cập nhật tài xế (ID)");
            printOption(4, "Xóa tài xế (ID)");
            printOption(5, "Tìm kiếm (Tên/ID)");
            printOption(6, "Sắp xếp theo Rating");
            printOption(7, "Top K Rating cao nhất");
            printOption(0, "Quay lại");
            printLine();

            int choice = getIntInput(">> Mời chọn: ");
            printLine();

            switch (choice) {
                case 1:
                    System.out.println(PURPLE + "[DANH SÁCH TÀI XẾ]" + RESET);
                    displayDriversTable(driverService.getDriverList());
                    pause();
                    break;
                case 2:
                    addDriverUI();
                    pause();
                    break;
                case 3:
                    updateDriverUI();
                    pause();
                    break;
                case 4:
                    String delId = getStringInput("Nhập ID cần xóa: ");
                    if (driverService.deleteDriver(delId)) printSuccess("Xóa tài xế thành công!");
                    else printError("Không tìm thấy ID tài xế.");
                    pause();
                    break;
                case 5:
                    String query = getStringInput("Nhập Tên hoặc ID: ");
                    System.out.println(PURPLE + "Kết quả tìm kiếm:" + RESET);
                    displayDriversTable(driverService.searchDriver(query));
                    pause();
                    break;
                case 6:
                    showSortMenu();
                    break;
                case 7:
                    int k = getIntInput("Nhập số lượng K: ");
                    System.out.println(PURPLE + "Top " + k + " tài xế có rating cao nhất:" + RESET);
                    displayDriversTable(driverService.getTopKDrivers(k));
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

    private void showSortMenu() {
        System.out.println(YELLOW + "--- SẮP XẾP TÀI XẾ ---" + RESET);
        System.out.println("1. Rating giảm dần (High -> Low)");
        System.out.println("2. Rating tăng dần (Low -> High)");
        System.out.println("3. Tên A -> Z");
        System.out.println("4. Tên Z -> A");
        int sortChoice = getIntInput(">> Chọn kiểu sắp xếp: ");
        
        switch (sortChoice) {
            case 1:
                driverService.sortDriversByRating(false); // Descending
                printSuccess("Đã sắp xếp Rating giảm dần.");
                break;
            case 2:
                driverService.sortDriversByRating(true); // Ascending
                printSuccess("Đã sắp xếp Rating tăng dần.");
                break;
            case 3:
                driverService.sortDriversByName(true); // A-Z
                printSuccess("Đã sắp xếp Tên A->Z.");
                break;
            case 4:
                driverService.sortDriversByName(false); // Z-A
                printSuccess("Đã sắp xếp Tên Z->A.");
                break;
            default:
                printError("Lựa chọn không hợp lệ.");
                return;
        }
        displayDriversTable(driverService.getDriverList());
        pause();
    }

    private void displayDriversTable(Doubly<Drivers> list) {
        if (list.getSize() == 0) {
            System.out.println("  (Danh sách trống)");
            return;
        }
        String[] headers = {"ID", "Name", "Rating", "Location (x,y)", "Status"};
        String[][] data = new String[list.getSize()][5];
        
        for (int i = 0; i < list.getSize(); i++) {
            Drivers d = list.get(i);
            data[i][0] = d.getId();
            data[i][1] = d.getName();
            data[i][2] = String.format("%.1f", d.getRating());
            data[i][3] = "(" + d.getX() + ", " + d.getY() + ")";
            data[i][4] = d.isAvailable() ? "Available" : "Busy";
        }
        
        ConsoleUtils.printTable(headers, data);
    }

    private void addDriverUI() {
        System.out.println(YELLOW + "--- THÊM TÀI XẾ MỚI ---" + RESET);
        String id = driverService.getNextId();
        System.out.println("ID Tự động: " + BOLD + id + RESET);
        String name = getStringInput("Tên tài xế: ");
        double rating = getDoubleInput("Rating (0.0 - 5.0): ");
        int x = getIntInput("Tọa độ X: ");
        int y = getIntInput("Tọa độ Y: ");

        Drivers d = new Drivers(id, name, rating, x, y);
        driverService.addDriver(d);
        file_io.saveDriver(d, "drivers.csv");
        printSuccess("Thêm tài xế [" + name + "] thành công!");
    }

    private void updateDriverUI() {
        String id = getStringInput("Nhập ID tài xế cần sửa: ");
        Drivers old = driverService.getDriverById(id);
        if (old == null) {
            printError("Không tìm thấy ID.");
            return;
        }
        System.out.println("Thông tin hiện tại: " + old);
        System.out.println(YELLOW + "Nhập thông tin mới (Để sửa):" + RESET);

        String name = getStringInput("Tên mới: ");
        double rating = getDoubleInput("Rating mới: ");
        int x = getIntInput("X mới: ");
        int y = getIntInput("Y mới: ");

        if (driverService.updateDriver(id, name, rating, x, y)) {
            printSuccess("Cập nhật thông tin thành công!");
        } else {
            printError("Lỗi khi cập nhật.");
        }
    }
}
