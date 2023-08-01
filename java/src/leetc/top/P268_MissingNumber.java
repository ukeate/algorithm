package leetc.top;

public class P268_MissingNumber {
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static int missingNumber(int[] arr) {
        int l = 0, r = arr.length;
        while (l < r) {
            if (arr[l] == l) {
                l++;
            } else if (arr[l] < l || arr[l] >= r || arr[arr[l]] == arr[l]) {
                // 无效
                swap(arr, l, --r);
            } else {
                // arr[arr[l]] != arr[l]
                swap(arr, l, arr[l]);
            }
        }
        return l;
    }
}
