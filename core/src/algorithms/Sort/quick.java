package algorithms.Sort;

public class quick {
    public static int partition(int[] a, int low, int high) {
        int pivot = a[high];   // chọn pivot là phần tử cuối
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }

        // đưa pivot về đúng vị trí
        int temp = a[i + 1];
        a[i + 1] = a[high];
        a[high] = temp;

        return i + 1; // vị trí pivot
    }

    // Hàm quick sort
    public static void quickSort(int[] a, int low, int high) {
        if (low < high) {
            int pi = partition(a, low, high);

            quickSort(a, low, pi - 1);
            quickSort(a, pi + 1, high);
        }
    }
}
