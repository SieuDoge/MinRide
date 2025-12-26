package data_structures.StackNQueue;

import java.util.Arrays;

public class Queue<T> {
    private Object[] queue;
    private int front, rear, size;

    public Queue(int capacity) {
        queue = new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    // Thêm phần tử (enqueue)
    public void enqueue(T x) {
        if (size == queue.length) {
             queue = Arrays.copyOf(queue, queue.length * 2);
        }
        rear++;
        queue[rear] = x;
        size++;
    }

    // Xóa phần tử (dequeue)
    public T dequeue() {
        if (size == 0) {
            System.out.println("Queue empty");
            return null;
        }
        T item = (T) queue[front];
        front++;
        size--;
        return item;
    }

    // Lấy phần tử đầu
    public T peek() {
        if (size == 0) return null;
        return (T) queue[front];
    }
    
    public boolean isEmpty() {
        return size == 0;
    }

    // Hiển thị
    public void display() {
        for (int i = front; i <= rear; i++) {
            System.out.print(queue[i] + " ");
        }
        System.out.println();
    }

    // Get all elements as array (for UI Table)
    public Object[] toArray() {
        if (size == 0) return new Object[0];
        Object[] result = new Object[size];
        for (int i = 0; i < size; i++) {
            result[i] = queue[front + i];
        }
        return result;
    }

    // Remove specific item (For "Picking" from queue)
    // Note: This operation is O(N) and technically violates FIFO, but required for "Dispatch Board" feature.
    public boolean remove(T item) {
        if (size == 0) return false;
        
        int index = -1;
        // Find index
        for (int i = front; i <= rear; i++) {
            if (queue[i].equals(item)) {
                index = i;
                break;
            }
        }
        
        if (index == -1) return false; // Not found

        // Shift elements to fill the gap
        // Shift elements from index+1 down to rear
        for (int i = index; i < rear; i++) {
            queue[i] = queue[i + 1];
        }
        
        queue[rear] = null; // Clear last
        rear--;
        size--;
        
        // Reset if empty to keep clean
        if (size == 0) {
            front = 0;
            rear = -1;
        }
        
        return true;
    }
}
