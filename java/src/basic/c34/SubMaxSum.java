package basic.c34;

// 子数组最大累加和
public class SubMaxSum {
    public static int max(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int cur = 0;
        for (int i = 0; i < arr.length; i++) {
            cur += arr[i];
            max = Math.max(max, cur);
            cur = cur < 0 ? 0 : cur;
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr = {-2, -3, -5, 40, -10, -10, 100, 1};
        System.out.println(max(arr));
    }
}
