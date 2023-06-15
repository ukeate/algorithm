package base.arr2;

// 最长和为sum的子串，值为正数
public class LongestSumSubPositive {

    public static int max(int[] arr, int k) {
        if (arr == null || arr.length == 0 || k <= 0) {
            return 0;
        }
        int l = 0, r = 0;
        int sum = arr[0];
        int len = 0;
        while (r < arr.length) {
            if (sum == k) {
                len = Math.max(len, r - l + 1);
                sum -= arr[l++];
            } else if (sum < k) {
                if (++r == arr.length) {
                    break;
                }
                sum += arr[r];
            } else {
                sum -= arr[l++];
            }

        }
        return len;
    }

    //

    private static boolean valid(int[] arr, int l, int r, int k) {
        int sum = 0;
        for (int i = l; i <= r; i++) {
            sum += arr[i];
        }
        return sum == k;
    }

    public static int sure(int[] arr, int k) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (valid(arr, i, j, k)) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    //

    private static int[] randomPositiveArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 500000;
        int len = 50;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomPositiveArr(len, maxVal);
            int k = (int) ((maxVal + 1) * Math.random()) + 1;
            int ans1 = max(arr, k);
            int ans2 = sure(arr, k);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
