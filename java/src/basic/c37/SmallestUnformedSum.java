package basic.c37;

import java.util.Arrays;
import java.util.HashSet;

// 求最小不可组成和，集合元素相加不能得到的最小数
public class SmallestUnformedSum {
    private static void process(int[] arr, int i, int sum, HashSet<Integer> set) {
        if (i == arr.length) {
            set.add(sum);
            return;
        }
        process(arr, i + 1, sum, set);
        process(arr, i + 1, sum + arr[i], set);
    }

    public static int min1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;
        }
        HashSet<Integer> set = new HashSet<>();
        process(arr, 0, 0, set);
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            min = Math.min(min, arr[i]);
        }
        for (int i = min + 1; i < Integer.MAX_VALUE; i++) {
            if (!set.contains(i)) {
                return i;
            }
        }
        return 0;
    }

    //

    public static int min2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 1;
        }
        int sum = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            min = Math.min(min, arr[i]);
        }
        int n = arr.length;
        boolean[][] dp = new boolean[n][sum + 1];
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        dp[0][arr[0]] = true;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= sum; j++) {
                dp[i][j] = dp[i - 1][j] || ((j - arr[i] >= 0) ? dp[i - 1][j - arr[i]] : false);
            }
        }
        for (int j = min; j <= sum; j++) {
            if (!dp[n - 1][j]) {
                return j;
            }
        }
        return sum + 1;
    }

    //

    // 已知arr中有1
    public static int min3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        Arrays.sort(arr);
        int range = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > range + 1) {
                return range + 1;
            } else {
                range += arr[i];
            }
        }
        return range + 1;
    }

    //

    private static int[] randomArr(int len, int maxVal) {
        int[] res = new int[len];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) (maxVal * Math.random()) + 1;
        }
        return res;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int len = 27;
        int maxVal = 30;
        int[] arr = randomArr(len, maxVal);
        print(arr);
        long start = System.currentTimeMillis();
        System.out.println(min1(arr));
        long end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + " ms");
        System.out.println("=====");
        start = System.currentTimeMillis();
        System.out.println(min2(arr));
        end = System.currentTimeMillis();
        System.out.println("cost time: " + (end - start) + " ms");
        System.out.println("=====");
        arr[0] = 1;
        start = System.currentTimeMillis();
        System.out.println(min3(arr));
        end = System.currentTimeMillis();
        System.out.println("cost time " + (end - start) + " ms");
    }
}
