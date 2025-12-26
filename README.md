# 🚗 MinRide - Hệ Thống Đặt Xe Console (Java Core)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![DSA](https://img.shields.io/badge/DSA-Data%20Structures-blue?style=for-the-badge)
![Console](https://img.shields.io/badge/UI-Console%20Application-424242?style=for-the-badge)

> **MinRide** là một ứng dụng giả lập hệ thống đặt xe (tương tự Grab/Uber) chạy trên nền tảng Console. Dự án được xây dựng hoàn toàn bằng **Java thuần**, áp dụng các **Cấu trúc dữ liệu và Giải thuật (DSA)** nâng cao để tối ưu hóa hiệu năng quản lý và tìm kiếm.

---

## 🌟 Tính Năng Nổi Bật (Key Features)

Dự án không sử dụng thư viện bên ngoài, tự triển khai các cấu trúc dữ liệu:

*   **⚡ Tìm kiếm siêu tốc:** Sử dụng **AVL Tree** để quản lý và tìm kiếm Tài xế/Khách hàng theo ID với độ phức tạp $O(\log N)$.
*   **🧩 Điều phối thông minh:** Hệ thống tự động tìm tài xế gần nhất trong bán kính cho phép và ghép nối chuyến đi ngay lập tức.
*   **📊 Thống kê Top K:** Sử dụng **Min-Heap** và **Merge Sort** để hiển thị Top tài xế có doanh thu hoặc đánh giá cao nhất.
*   **🎨 Giao diện đẹp mắt:** Hệ thống Console UI được thiết kế với màu sắc, bảng biểu (Tables), và thẻ thông tin (Info Cards) trực quan.
*   **💾 Lưu trữ dữ liệu:** Tự động đọc/ghi dữ liệu từ file CSV.
*   **↩️ Hoàn tác (Undo):** Hỗ trợ Undo các thao tác thêm/xóa/sửa tài xế bằng cấu trúc Stack.

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy (Getting Started)

### 1. Yêu cầu hệ thống
*   **Java Development Kit (JDK):** Phiên bản 17 trở lên.
*   Hệ điều hành: Windows, macOS, hoặc Linux.

### 2. Biên dịch (Compile)
Mở Terminal/Command Prompt tại thư mục gốc của dự án (`MinRide/`) và chạy lệnh sau:

**Windows (PowerShell):**
```powershell
mkdir bin
javac -d bin -sourcepath src (Get-ChildItem -Recurse src/*.java)
```

**Linux / MacOS:**
```bash
mkdir -p bin
find src -name "*.java" > sources.txt
javac -d bin @sources.txt
```

### 3. Chạy chương trình (Run)
Sau khi biên dịch thành công:

```bash
java -cp bin Main
```

---

## 🎮 Kịch Bản Test Mẫu (Cheat Sheet)

Dữ liệu mẫu đã được tạo sẵn (50 Tài xế, 50 Khách hàng). Bạn có thể dùng các thông số dưới đây để test nhanh các chức năng mà không cần mò mẫm ID.

### 1. Đặt xe & Tự động ghép (Booking & Auto Dispatch)
Chọn chức năng **1. Đặt Xe Mới** tại Menu chính.

*   **ID Khách hàng:** `1` (hoặc từ `1` đến `50`)
*   **Tọa độ đến (X):** `50`
*   **Tọa độ đến (Y):** `50`

> 💡 **Kết quả:** Hệ thống sẽ hiện thẻ "Thông tin khách hàng", sau đó tự động tìm tài xế gần nhất. Nếu thành công, thẻ "GHÉP XE THÀNH CÔNG" sẽ hiện ra kèm giá tiền và quãng đường.

### 2. Tra cứu lịch sử chuyến đi
Chọn chức năng **2. Lịch sử chuyến đi**.
*   **ID Tài xế:** Nhập ID của tài xế vừa nhận cuốc xe ở bước trên (Ví dụ: `15`, `32`...).

### 3. Xem Dashboard & Top Doanh Thu
Chọn chức năng **4. Thống kê (Dashboard)** -> Chọn **1. Xem Top Tài xế theo Doanh thu**.
*   **Nhập số lượng K:** `5`
    *   *Hệ thống sẽ dùng thuật toán Merge Sort/Heap để hiển thị 5 tài xế giàu nhất.*

### 4. Tìm kiếm nâng cao
Chọn chức năng **3. Đặt xe & Điều phối** -> **4. Tìm tài xế phù hợp (Advanced)**.
*   **ID Khách hàng:** `10`
*   **Bán kính:** `20` (km)
*   **Tiêu chí:** `2` (Rating cao nhất)

---

## 📚 5. Phân Tích Cấu Trúc Dữ Liệu & Giải Thuật (Technical Deep Dive)

Dưới đây là chi tiết các thuật toán và cấu trúc dữ liệu được áp dụng trong từng module của dự án:

### A. Bản đồ Cấu Trúc Dữ Liệu (Data Structures Map)

| Cấu Trúc (DS) | Vị trí áp dụng | Mục đích sử dụng |
| :--- | :--- | :--- |
| **Doubly Linked List** | `Drivers`, `Customers`, `Rides` | Lưu trữ danh sách chính. Cho phép duyệt xuôi/ngược, thêm/xóa phần tử linh hoạt $O(1)$ ở đầu/cuối. |
| **AVL Tree** | `driver_service` | Cây nhị phân tìm kiếm cân bằng. Dùng để **Index (đánh chỉ mục)** tài xế theo ID, giúp tìm kiếm siêu tốc $O(\log N)$. |
| **Stack (LIFO)** | `undo.java` | Lưu trữ lịch sử thao tác (Command Pattern) để thực hiện tính năng **Hoàn tác (Undo)**. |
| **Queue (FIFO)** | `booking_service` | Hàng đợi đặt xe. Khi không có tài xế, yêu cầu được đẩy vào Queue để xử lý sau (First-In-First-Out). |
| **Min-Heap** | `driver_service` | Hàng đợi ưu tiên. Dùng để lọc ra **Top K** tài xế có chỉ số (Rating/Doanh thu) cao nhất hiệu quả. |

### B. Mổ Xẻ Thuật Toán (Algorithms Breakdown)

#### 1. Quản Lý Tài Xế (Driver Management)
*   **Tìm kiếm theo ID:**
    *   *Thuật toán:* **Binary Search trên cây AVL**.
    *   *Mô tả:* Thay vì duyệt từng phần tử trong danh sách ($O(N)$), hệ thống đi theo các nhánh cây cân bằng để tìm ID.
    *   *Độ phức tạp:* $O(\log N)$.
*   **Thêm/Xóa/Sửa:**
    *   *Thao tác:* Đồng bộ dữ liệu trên cả `DoublyLinkedList` (để duyệt UI) và `AVLTree` (để tìm kiếm).
*   **Hoàn tác (Undo):**
    *   *Logic:* Sử dụng **Stack** để lưu đối tượng `Command`. Mỗi khi thực hiện (Add/Delete/Update), một lệnh "ngược lại" được `push` vào Stack. Khi Undo, lệnh được `pop` ra và thực thi.

#### 2. Điều Phối & Đặt Xe (Booking & Dispatch)
*   **Tìm tài xế gần nhất (Nearest Neighbors):**
    *   *Logic:* Duyệt danh sách tài xế -> Tính khoảng cách **Euclidean** $\sqrt{(x_2-x_1)^2 + (y_2-y_1)^2}$.
    *   *Tối ưu:* Chỉ xét các tài xế trong bán kính cho phép (Filter).
*   **Sắp xếp & Chọn tài xế (Auto-Assign):**
    *   *Thuật toán:* **Merge Sort**.
    *   *Mô tả:* Hệ thống sắp xếp danh sách các ứng viên theo tiêu chí đa tầng: `Khoảng cách tăng dần` -> nếu bằng nhau thì `Rating giảm dần`.
    *   *Lý do dùng Merge Sort:* Thuật toán sắp xếp ổn định (Stable Sort), hiệu năng tốt $O(N \log N)$ với danh sách liên kết.
*   **Xử lý Hàng chờ (Queue Processing):**
    *   *Logic:* Nếu không tìm thấy tài xế, đơn hàng `enqueue` vào Queue. Khi có lệnh "Xử lý", hệ thống `dequeue` lần lượt và chạy lại quy trình tìm kiếm.

#### 3. Thống Kê & Báo Cáo (Dashboard)
*   **Top K Tài xế (Ranking):**
    *   *Thuật toán:* **Merge Sort** (cho Doanh thu) và **Min-Heap** (cho Rating).
    *   *Mô tả Heap:* Duy trì một Min-Heap kích thước K. Duyệt qua N phần tử, nếu phần tử lớn hơn đỉnh Heap thì thay thế. -> $O(N \log K)$.

---

## 📊 6. Bảng Độ Phức Tạp (Time Complexity)

| Chức năng | Thuật toán / DS sử dụng | Độ phức tạp trung bình |
| :--- | :--- | :--- |
| **Load Data** | File I/O + Add Last | $O(N)$ |
| **Search (ID)** | AVL Tree Search | $O(\log N)$ |
| **Search (Name)** | Linear Scan | $O(N)$ |
| **Add Driver** | AVL Insert + List Add | $O(\log N)$ |
| **Delete Driver** | AVL Delete + List Remove | $O(\log N)$ |
| **Sort Drivers** | Merge Sort | $O(N \log N)$ |
| **Find Nearest** | Linear Scan + Sort | $O(N \log N)$ |
| **Top K** | Min-Heap / Sort | $O(N \log K)$ hoặc $O(N \log N)$ |
| **Undo** | Stack Push/Pop | $O(1)$ |

---

## 📂 Cấu Trúc Dự Án (Project Structure)

```
MinRide/
├── src/
│   ├── algorithms/       # MergeSort, QuickSort...
│   ├── console_out/      # UI Classes (Menus, Card Builder, Table Builder)
│   ├── data/             # CSV Files (drivers.csv, customers.csv...)
│   ├── data_structures/  # Tự build: AVLTree, LinkedList, Heap, Queue...
│   ├── models/           # Các đối tượng: Drivers, Customers, Rides...
│   ├── services/         # Logic xử lý chính (Business Logic)
│   ├── utils/            # FileIO, DistanceCalc...
│   └── Main.java         # Entry Point
└── README.md
```

---
*Đồ án môn học Cấu Trúc Dữ Liệu & Giải Thuật.*