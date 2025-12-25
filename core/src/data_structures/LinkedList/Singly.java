package data_structures.LinkedList;

public class Singly<T> {

    /* ========== NODE ========== */
    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
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
            tail = newNode;
        }
        size++;
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

        head = head.next;
        size--;

        if (head == null) {
            tail = null;
        }
    }

    /* ========== XÓA CUỐI ========== */
    public void deleteLast() {
        if (head == null) return;

        if (head == tail) {
            head = tail = null;
        } else {
            Node temp = head;
            while (temp.next != tail) {
                temp = temp.next;
            }
            tail = temp;
            tail.next = null;
        }
        size--;
    }

    /* ========== CÁC HÀM HỖ TRỢ (HELPER) ========== */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Node temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.data;
    }

    public void set(int index, T data) {
        if (index < 0 || index >= size) return;
        Node temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        temp.data = data;
    }
    
    // Convert to Array for easier sorting (if needed)
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node temp = head;
        int i = 0;
        while (temp != null) {
            arr[i++] = temp.data;
            temp = temp.next;
        }
        return arr;
    }

    public int getSize() {
        return size;
    }
}

