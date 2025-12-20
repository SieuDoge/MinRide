package data_structures.StackNQueue;

public class Queue {
    private int[] queue;
    private int front, rear, size;

    public Queue(int capacity) {
        queue = new int[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    // Thêm phần tử (enqueue)
    public void enqueue(int x) {
        if (size == queue.length) {
            System.out.println("Queue full");
            return;
        }
        rear++;
        queue[rear] = x;
        size++;
    }

    // Xóa phần tử (dequeue)
    public void dequeue() {
        if (size == 0) {
            System.out.println("Queue empty");
            return;
        }
        front++;
        size--;
    }

    // Lấy phần tử đầu
    public int peek() {
        if (size == 0) return -1;
        return queue[front];
    }

    // Hiển thị
    public void display() {
        for (int i = front; i <= rear; i++) {
            System.out.print(queue[i] + " ");
        }
        System.out.println();
    }
}
