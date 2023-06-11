package base.dp3;

import java.util.HashSet;
import java.util.TreeSet;

// 非负数组arr, 正数m，返回子序列累加和%m的最大值
public class SubsequenceMaxMod {
    private static void process1(int[] arr, int idx, int sum, HashSet<Integer> set) {
        if (idx == arr.length) {
            set.add(sum);
        } else {
            process1(arr, idx + 1, sum, set);
            process1(arr, idx + 1, sum + arr[idx], set);
        }
    }
    public static int max1(int[] arr, int m) {
        HashSet<Integer> set = new HashSet<>();
        process1(arr, 0, 0, set);
        int max = 0;
        for (Integer sum : set) {
            max = Math.max(max, sum % m);
        }
        return max;
    }

    //

    public static int dp1(int[] arr, int m) {
        int sum = 0;
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            sum += arr[i];
        }
        boolean[][] dp = new boolean[n][sum + 1];
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        dp[0][arr[0]] = true;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= sum; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - arr[i] >= 0) {
                    dp[i][j] |= dp[i - 1][j - arr[i]];
                }
            }
        }
        int ans = 0;
        for (int j = 0; j <= sum; j++) {
            if (dp[n - 1][j]) {
                ans = Math.max(ans, j % m);
            }
        }
        return ans;
    }

    //

    public static int dp2(int[] arr, int m) {
        int n = arr.length;
        boolean[][] dp = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }
        dp[0][arr[0] % m] = true;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                dp[i][j] = dp[i - 1][j];
                int cur = arr[i] % m;
                if (cur <= j) {
                    dp[i][j] |= dp[i - 1][j - cur];
                } else {
                    dp[i][j] |= dp[i - 1][m + j - cur];
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < m; i++) {
            if (dp[n - 1][i]) {
                ans = i;
            }
        }
        return ans;
    }

    //

    private static void process2(int[] arr, int idx, int sum, int end, int m, TreeSet<Integer> set) {
        if (idx == end + 1) {
            set.add(sum % m);
        } else {
            process2(arr, idx + 1, sum, end, m, set);
            process2(arr, idx + 1, sum + arr[idx], end, m, set);
        }
    }

    // 累加和大, m大, arr长度小时
    public static int max2(int[] arr, int m) {
        if (arr.length == 1) {
            return arr[0] % m;
        }
        int mid = (arr.length - 1) / 2;
        TreeSet<Integer> set1 = new TreeSet<>();
        process2(arr, 0, 0, mid, m, set1);
        TreeSet<Integer> set2 = new TreeSet<>();
        process2(arr, mid + 1, 0, arr.length - 1, m, set2);
        int ans = 0;
        for (Integer leftMod : set1) {
            ans = Math.max(ans, leftMod + set2.floor(m - 1 - leftMod));
        }
        return ans;
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] ans = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 500000;
        int maxLen = 10;
        int maxVal = 100;
        int m = 76;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = max1(arr, m);
            int ans2 = dp1(arr, m);
            int ans3 = dp2(arr, m);
            int ans4 = max2(arr, m);
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
