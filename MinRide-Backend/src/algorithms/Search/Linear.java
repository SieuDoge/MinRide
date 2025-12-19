package algorithms.Search;

public class Linear {
    public static int LinearSearch(int[] a, int x){
        for(int i=0;i<a.length;i++){
            if(a[i]==x){
                return i;
            }
        }
        return -1;
    }
}
