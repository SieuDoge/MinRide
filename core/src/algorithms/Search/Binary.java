package algorithms.Search;

public class Binary {
    public static int BinarySearch(int[] a, int x) {
        int left = 0;
        int right = a.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (a[mid] == x) {
                return mid;
            } else if (a[mid] < x) {
                left = mid + 1;
            } else if (a[mid] > x) {
                right = mid - 1;
            }
        }
        return -1;
    }
}
