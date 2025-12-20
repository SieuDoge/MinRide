package data_structures.StackNQueue;

public class Stack {
    private int[] stack;
    private int top;

    public Stack(int capacity) {
        stack = new int[capacity];
        top = -1;
    }

    // Thêm phần tử (push)
    public void push(int x) {
        if (top == stack.length - 1) {
            System.out.println("Stack overflow");
            return;
        }
        stack[++top] = x;
    }

    // Xóa phần tử (pop)
    public int pop() {
        if (top == -1) {
            System.out.println("Stack underflow");
            return -1;
        }
        return stack[top--];
    }

    // Xem phần tử trên cùng
    public int peek() {
        if (top == -1) return -1;
        return stack[top];
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
