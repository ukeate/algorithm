package base.dp;

import java.util.HashMap;
import java.util.HashSet;

// 自由选择arr的值，能不能累加得到sum
public class HasSum {
    private static boolean process1(int[] arr, int i, int sum) {
        if (sum == 0) {
            return true;
        }
        if (i == -1) {
            return false;
        }
        return process1(arr, i - 1, sum) || process1(arr, i - 1, sum - arr[i]);
    }

    public static boolean hasSum1(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        return process1(arr, arr.length - 1, sum);
    }

    //

    private static boolean process2(int[] arr, int i, int sum, HashMap<Integer, HashMap<Integer, Boolean>> dp) {
        if (dp.containsKey(i) && dp.get(i).containsKey(sum)) {
            return dp.get(i).get(sum);
        }
        boolean ans = false;
        if (sum == 0) {
            ans = true;
        } else if (i != -1) {
            ans = process2(arr, i - 1, sum, dp) || process2(arr, i - 1, sum - arr[i], dp);
        }

        if (!dp.containsKey(i)) {
            dp.put(i, new HashMap<>());
        }
        dp.get(i).put(sum, ans);
        return ans;
    }

    public static boolean hasSum2(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        return process2(arr, arr.length - 1, sum, new HashMap<>());
    }

    //

    public static boolean hasSum3(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        int min = 0;
        int max = 0;
        for (int num : arr) {
            min += num < 0 ? num : 0;
            max += num > 0 ? num : 0;
        }
        if (sum < min || sum > max) {
            return false;
        }
        int n = arr.length;
        boolean[][] dp = new boolean[n][max - min + 1];
        dp[0][-min] = true;
        dp[0][arr[0] - min] = true;
        for (int i = 1; i < n; i++) {
            for (int j = min; j <= max; j++) {
                dp[i][j - min] = dp[i - 1][j - min];
                int next = j - min - arr[i];
                dp[i][j - min] |= (next >= 0 && next <= max - min && dp[i - 1][next]);
            }
        }
        return dp[n - 1][sum - min];
    }

    //

    private static void process4(int[] arr, int i, int end, int pre, HashSet<Integer> ans) {
        if (i == end) {
            ans.add(pre);
        } else {
            process4(arr, i + 1, end, pre, ans);
            process4(arr, i + 1, end, pre + arr[i], ans);
        }
    }

    public static boolean hasSum4(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        if (arr.length == 1) {
            return arr[0] == sum;
        }
        int n = arr.length;
        int mid = n >> 1;
        HashSet<Integer> leftSum = new HashSet<>();
        HashSet<Integer> rightSum = new HashSet<>();
        process4(arr, 0, mid, 0, leftSum);
        process4(arr, mid, n, 0, rightSum);
        for (int l : leftSum) {
            if (rightSum.contains(sum - l)) {
                return true;
            }
        }
        return false;
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (((maxVal << 1) + 1) * Math.random()) - maxVal;
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 20;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int sum = (int) (((maxVal << 1) + 1) * Math.random()) - maxVal;
            boolean ans1 = hasSum1(arr, sum);
            boolean ans2 = hasSum2(arr, sum);
            boolean ans3 = hasSum3(arr, sum);
            boolean ans4 = hasSum4(arr, sum);
            if (ans1 ^ ans2 || ans3 ^ ans4 || ans1 ^ ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
