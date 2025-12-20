package algorithms.Sort;

public class selection {
    public static void selectionSort(int[]a){
        int n = a.length;
        for ( int i=0; i<n-1;i++){
            int minInD=i;
            // tìm phần tử nhỏ nhất
            for (int j=i+1;j<n;j++){
                if(a[j]>a[minInD]){
                    minInD=j;
                }
            }
            if (minInD !=i){
                int temp = a[i];
                a[i] = a[minInD];
                a[minInD] = temp;
            }
        }
    }
}
