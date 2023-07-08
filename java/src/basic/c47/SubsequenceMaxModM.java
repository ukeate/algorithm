package basic.c47;

import java.util.HashSet;
import java.util.TreeSet;

// 子数组累加和 mod M 最大值
public class SubsequenceMaxModM {
    private static void process(int[] arr, int idx, int sum, HashSet<Integer> set) {
        if (idx == arr.length) {
            set.add(sum);
        } else {
            process(arr, idx + 1, sum, set);
            process(arr, idx + 1, sum + arr[idx], set);
        }
    }

    public static int max1(int[] arr, int m) {
        HashSet<Integer> set = new HashSet<>();
        process(arr, 0, 0, set);
        int max = 0;
        for (Integer sum : set) {
            max = Math.max(max, sum % m);
        }
        return max;
    }

    //

    public static int max2(int[] arr, int m) {
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
                    dp[i][j] = dp[i][j] | dp[i - 1][j - arr[i]];
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

    public static int max3(int[] arr, int m) {
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
                if (j - cur >= 0) {
                    dp[i][j] = dp[i][j] | dp[i - 1][j - cur];
                }
                if (j - cur < 0) {
                    dp[i][j] = dp[i][j] | dp[i - 1][m + j - cur];
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

    private static void process4(int[] arr, int idx, int sum, int end, int m, TreeSet<Integer> set) {
        if (idx == end + 1) {
            set.add(sum % m);
        } else {
            process4(arr, idx + 1, sum, end, m, set);
            process4(arr, idx + 1, sum + arr[idx], end, m, set);
        }
    }

    public static int max4(int[] arr, int m) {
        if (arr.length == 1) {
            return arr[0] % m;
        }
        int mid = (arr.length - 1) / 2;
        TreeSet<Integer> set1 = new TreeSet<>();
        process4(arr, 0, 0, mid, m, set1);
        TreeSet<Integer> set2 = new TreeSet<>();
        process4(arr, mid + 1, 0, arr.length - 1, m, set2);
        int ans = 0;
        for (Integer leftMod : set1) {
            ans = Math.max(ans, leftMod + set2.floor(m - 1 - leftMod));
        }
        return ans;
    }

    //

    private static int[] randomArr(int len, int val) {
        int[] ans = new int[(int) (len * Math.random()) + 1];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (int) (val * Math.random());
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 500000;
        int len = 10;
        int val = 100;
        int m = 76;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, val);
            int ans1 = max1(arr, m);
            int ans2 = max2(arr, m);
            int ans3 = max3(arr, m);
            int ans4 = max4(arr, m);
            if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
