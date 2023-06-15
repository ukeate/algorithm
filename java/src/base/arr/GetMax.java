package base.arr;

public class GetMax {
    private static int process(int[] arr, int l, int r) {
        if (l == r) {
            return arr[l];
        }
        int mid = l + ((r - l) >> 1);
        int lMax = process(arr, l, mid);
        int rMax = process(arr, mid + 1, r);
        return Math.max(lMax, rMax);
    }

    public static int getMax(int[] arr) {
        return process(arr, 0, arr.length - 1);
    }
}
