package giant.c16;

import java.util.HashMap;
import java.util.HashSet;

// arr中的值可能为正，可能为负，可能为0
// 自由选择arr中的数字，能不能累加得到sum
public class IsSum {
    private static boolean process1(int[] arr, int i, int sum, HashMap<Integer, HashMap<Integer, Boolean>> dp) {
        if (dp.containsKey(i) && dp.get(i).containsKey(sum)) {
            return dp.get(i).get(sum);
        }
        boolean ans = false;
        if (sum == 0) {
            ans = true;
        } else if (i != -1) {
            ans = process1(arr, i - 1, sum, dp) || process1(arr, i - 1, sum - arr[i], dp);
        }
        if (!dp.containsKey(i)) {
            dp.put(i, new HashMap<>());
        }
        dp.get(i).put(sum, ans);
        return ans;
    }

    public static boolean isSum1(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        return process1(arr, arr.length - 1, sum, new HashMap<>());
    }

    //

    public static boolean dp(int[] arr, int sum) {
        if (sum == 0) {
            return true;
        }
        if (arr == null || arr.length == 0) {
            return false;
        }
        int min = 0, max = 0;
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

    private static void process2(int[] arr, int i, int end, int pre, HashSet<Integer> ans) {
       if (i == end) {
           ans.add(pre);
       } else {
           process2(arr, i + 1, end, pre, ans);
           process2(arr, i + 1, end, pre + arr[i], ans);
       }
    }

    // arr分治
    public static boolean isSum2(int[] arr, int sum) {
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
        HashSet<Integer> left = new HashSet<>();
        HashSet<Integer> right = new HashSet<>();
        process2(arr, 0, mid, 0, left);
        process2(arr, mid, n, 0, right);
        for (int l : left) {
            if (right.contains(sum - l)) {
                return true;
            }
        }
        return false;
    }

    //

    private static int[] randomArr(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * ((max << 1) + 1)) - max;
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 20;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int size = (int) (Math.random() * (maxLen + 1));
            int[] arr = randomArr(size, maxVal);
            int sum = (int) (Math.random() * ((maxVal << 1) + 1)) - maxVal;
            boolean ans1 = isSum1(arr, sum);
            boolean ans2 = dp(arr, sum);
            boolean ans3 = isSum2(arr, sum);
            if (ans1 ^ ans2 || ans2 ^ ans3) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
