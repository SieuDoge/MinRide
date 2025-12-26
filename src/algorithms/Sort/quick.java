package algorithms.Sort;

import java.util.Comparator;

public class quick {
    
    // Generic QuickSort for Arrays
    public static <T> void quickSort(T[] arr, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pi = partition(arr, low, high, comparator);

            quickSort(arr, low, pi - 1, comparator);
            quickSort(arr, pi + 1, high, comparator);
        }
    }

    private static <T> int partition(T[] arr, int low, int high, Comparator<T> comparator) {
        T pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            // If current element is <= pivot (based on comparator)
            if (comparator.compare(arr[j], pivot) <= 0) {
                i++;
                // swap arr[i] and arr[j]
                T temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        T temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}