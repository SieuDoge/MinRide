package data_structures.LinkedList;

public class Doubly<T> {

    /* ========== NODE ========== */
    private class Node {
        T data;
        Node prev;
        Node next;

        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }

    /* ========== LIST ========== */
    private Node head = null;
    private Node tail = null;
    private int size = 0;

    /* ========== THÊM CUỐI ========== */
    public void addLast(T data) {
        Node newNode = new Node(data);

        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /* ========== HIỂN THỊ TỪ ĐẦU ========== */
    public void displayForward() {
        Node temp = head;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    /* ========== HIỂN THỊ TỪ CUỐI ========== */
    public void displayBackward() {
        Node temp = tail;
        while (temp != null) {
            System.out.print(temp.data + " ");
            temp = temp.prev;
        }
        System.out.println();
    }

    /* ========== TÌM KIẾM ========== */
    public boolean search(T key) {
        Node temp = head;
        while (temp != null) {
            if (temp.data.equals(key))
                return true;
            temp = temp.next;
        }
        return false;
    }

    /* ========== XÓA ĐẦU ========== */
    public void deleteFirst() {
        if (head == null) return;

        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
    }

    /* ========== XÓA CUỐI ========== */
    public void deleteLast() {
        if (tail == null) return;

        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
    }

    public int getSize() {
        return size;
    }
}
