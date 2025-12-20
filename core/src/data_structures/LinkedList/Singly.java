package data_structures.LinkedList;

public class Singly {

    /* ========== NODE ========== */
    private class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    /* ========== LIST ========== */
    private Node head = null;

    /* ========== THÊM CUỐI ========== */
    public void addLast(int data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = newNode;
            return;
        }

        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    /* ========== HIỂN THỊ ========== */
    public void display() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    /* ========== TÌM KIẾM ========== */
    public boolean search(int key) {
        Node temp = head;
        while (temp != null) {
            if (temp.data == key)
                return true;
            temp = temp.next;
        }
        return false;
    }

    /* ========== XÓA ĐẦU ========== */
    public void deleteFirst() {
        if (head != null)
            head = head.next;
    }

    /* ========== XÓA CUỐI ========== */
    public void deleteLast() {
        if (head == null) return;

        if (head.next == null) {
            head = null;
            return;
        }

        Node temp = head;
        while (temp.next.next != null) {
            temp = temp.next;
        }
        temp.next = null;
    }
}

