package data_structures.Tree;

import java.util.Comparator;
import java.util.ArrayList;

public class Heap<T> {
    private ArrayList<T> heap;
    private Comparator<T> comparator;

    public Heap(Comparator<T> comparator) {
        this.heap = new ArrayList<>();
        this.comparator = comparator;
    }

    public void insert(T data) {
        heap.add(data);
        heapifyUp(heap.size() - 1);
    }

    public T extractMin() {
        if (heap.isEmpty()) return null;
        T min = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return min;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    public int size() {
        return heap.size();
    }
    
    public void clear() {
        heap.clear();
    }

    private void heapifyUp(int index) {
        int parent = (index - 1) / 2;
        if (index > 0 && comparator.compare(heap.get(index), heap.get(parent)) < 0) {
            swap(index, parent);
            heapifyUp(parent);
        }
    }

    private void heapifyDown(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int smallest = index;

        if (left < heap.size() && comparator.compare(heap.get(left), heap.get(smallest)) < 0) {
            smallest = left;
        }

        if (right < heap.size() && comparator.compare(heap.get(right), heap.get(smallest)) < 0) {
            smallest = right;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}