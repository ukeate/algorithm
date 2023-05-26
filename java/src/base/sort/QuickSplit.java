package base.sort;

public class QuickSplit {

    private static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    public static void split(int[] arr) {
        int l = -1;
        int i = 0;
        int n = arr.length;
        while (i < n) {
            if (arr[i] <= arr[n - 1]) {
                swap(arr, ++l, i++);
            } else {
                i++;
            }
        }
    }

    public static void split2(int[] arr) {
        int n = arr.length;
        int l = -1;
        int r = n - 1;
        int i = 0;
        while (i < r) {
            if (arr[i] < arr[n - 1]) {
                swap(arr, ++l, i++);
            } else if (arr[i] > arr[n - 1]) {
                swap(arr, --r, i);
            } else {
                i++;
            }
        }
        swap(arr, r, n - 1);
    }

}
