package algorithms.Sort;

public class interchange {
    public static void interchangeSort(int[] a){
        for (int i=0; i<a.length-1;i++){
            for (int j=i+1;j<a.length;j++){
                if(a[i]>a[j]){
                    int temp = a[i];
                    a[i]=a[j];
                    a[j]=temp;
                }
            }
        }
    }
}
