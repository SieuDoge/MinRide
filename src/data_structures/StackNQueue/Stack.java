package data_structures.StackNQueue;

import java.util.Arrays;

public class Stack<T> {
    private Object[] stack;
    private int top;

    public Stack(int capacity) {
        stack = new Object[capacity];
        top = -1;
    }

    // Thêm phần tử (push)
    public void push(T x) {
        if (top == stack.length - 1) {
            // Resize if needed, or just print error
            // For this challenge, let's resize
             stack = Arrays.copyOf(stack, stack.length * 2);
        }
        stack[++top] = x;
    }

    // Xóa phần tử (pop)
    public T pop() {
        if (top == -1) {
            System.out.println("Stack underflow");
            return null;
        }
        return (T) stack[top--];
    }

    // Xem phần tử trên cùng
    public T peek() {
        if (top == -1) return null;
        return (T) stack[top];
    }

    // Kiểm tra rỗng
    public boolean isEmpty() {
        return top == -1;
    }

    // Hiển thị
    public void display() {
        for (int i = top; i >= 0; i--) {
            System.out.print(stack[i] + " ");
        }
        System.out.println();
    }
}
