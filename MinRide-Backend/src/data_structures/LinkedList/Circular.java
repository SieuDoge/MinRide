package data_structures.LinkedList;

public class Circular {

    /* ================= NODE ================= */
    private class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    /* ============== LINKED LIST ============== */
    private Node head = null;
    private Node tail = null;

    /* ========== THÊM PHẦN TỬ CUỐI ========== */
    public void addLast(int data) {
        Node newNode = new Node(data);

        // Danh sách rỗng
        if (head == null) {
            head = tail = newNode;
            newNode.next = head;   // trỏ về chính nó
        }
        // Danh sách đã có phần tử
        else {
            tail.next = newNode;
            tail = newNode;
            tail.next = head;      // giữ vòng tròn
        }
    }

    /* ========== HIỂN THỊ DANH SÁCH ========== */
    public void display() {
        if (head == null) {
            System.out.println("Danh sach rong");
            return;
        }

        Node temp = head;
        do {
            System.out.print(temp.data + " ");
            temp = temp.next;
        } while (temp != head);

        System.out.println();
    }

    /* ========== TÌM KIẾM ========== */
    public boolean search(int key) {
        if (head == null) return false;

        Node temp = head;
        do {
            if (temp.data == key)
                return true;
            temp = temp.next;
        } while (temp != head);

        return false;
    }

    /* ========== XÓA PHẦN TỬ ĐẦU ========== */
    public void deleteFirst() {
        if (head == null) return;

        // Chỉ có 1 node
        if (head == tail) {
            head = tail = null;
        }
        // Nhiều node
        else {
            head = head.next;
            tail.next = head;
        }
    }

    /* ========== XÓA PHẦN TỬ CUỐI ========== */
    public void deleteLast() {
        if (head == null) return;

        // Chỉ có 1 node
        if (head == tail) {
            head = tail = null;
            return;
        }

        Node temp = head;
        while (temp.next != tail) {
            temp = temp.next;
        }

        temp.next = head;
        tail = temp;
    }
}
