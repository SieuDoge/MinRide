package algorithms.Sort;

import java.util.Comparator;
import java.lang.reflect.Array;

public class merge {
    // Hàm trộn 2 mảng con đã sắp xếp
    public static <T> void merge(T[] a, int left, int mid, int right, Comparator<T> c) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Create temp arrays using reflection or just Object array cast
        // Since we passed T[], we can try to use reflection if we had class, 
        // but simple way is to create Object[] and cast, OR just use valid T[] passed in helper.
        // To be safe and simple without passing Class<T>:
        Object[] L = new Object[n1];
        Object[] R = new Object[n2];

        for (int i = 0; i < n1; i++)
            L[i] = a[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = a[mid + 1 + j];

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            if (c.compare((T)L[i], (T)R[j]) <= 0) {
                a[k] = (T)L[i];
                i++;
            } else {
                a[k] = (T)R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            a[k] = (T)L[i];
            i++;
            k++;
        }

        while (j < n2) {
            a[k] = (T)R[j];
            j++;
            k++;
        }
    }

    // Hàm merge sort
    public static <T> void mergeSort(T[] a, int left, int right, Comparator<T> c) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(a, left, mid, c);
            mergeSort(a, mid + 1, right, c);

            merge(a, left, mid, right, c);
        }
    }
}
