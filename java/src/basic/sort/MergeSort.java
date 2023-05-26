package basic.sort;

public class MergeSort {

    private static void merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = m + 1;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
    }

    private static void mProcess(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        int m = l + ((r + l) >> 1);
        mProcess(arr, l, m);
        mProcess(arr, m + 1, r);
        merge(arr, l, m, r);
    }

    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        mProcess(arr, 0, arr.length - 1);
    }

    public static void mergeSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        int size = 1;
        while (size < n) {
            int l = 0;
            while (l < n) {
                if (n - l <= size) {
                    break;
                }
                int m = l + size - 1;
                int r = m + Math.min(size, n - 1 - m);
                merge(arr, l, m, r);
                l = r + 1;
            }
            if (size > (n >> 1)) {
                break;
            }
            size <<= 1;
        }
    }

    public static void main(String[] args) {

    }

}
