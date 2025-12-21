package data_structures.LinkedList;

public class Circular<T> {

    /* ================= NODE ================= */
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /* ============== LINKED LIST ============== */
    private Node head = null;
    private Node tail = null;
    private int size = 0;

    /* ========== THÊM PHẦN TỬ CUỐI ========== */
    public void addLast(T data) {
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
        size++;
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
    public boolean search(T key) {
        if (head == null) return false;

        Node temp = head;
        do {
            if (temp.data.equals(key))
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
        size--;
    }

    /* ========== XÓA PHẦN TỬ CUỐI ========== */
    public void deleteLast() {
        if (head == null) return;

        // Chỉ có 1 node
        if (head == tail) {
            head = tail = null;
            size--;
            return;
        }

        Node temp = head;
        while (temp.next != tail) {
            temp = temp.next;
        }

        temp.next = head;
        tail = temp;
        size--;
    }

    public int getSize() {
        return size;
    }
}
