package base.dp3_knap;

// 背包装零食
public class Snack {
    private static int process1(int[] arr, int idx, int rest) {
        if (rest < 0) {
            return -1;
        }
        if (idx == arr.length) {
            return 1;
        }
        int next1 = process1(arr, idx + 1, rest);
        int next2 = process1(arr, idx + 1, rest - arr[idx]);
        return next1 + (next2 == -1 ? 0 : next2);
    }

    public static int ways1(int[] arr, int w) {
        return process1(arr, 0, w);
    }

    //

    public static int dp1(int[] arr, int w) {
        int n = arr.length;
        int[][] dp = new int[n + 1][w + 1];
        for (int j = 0; j <= w; j++) {
            dp[n][j] = 1;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= w; j++) {
                dp[i][j] = dp[i + 1][j] + ((j - arr[i] >= 0) ? dp[i + 1][j - arr[i]] : 0);
            }
        }
        return dp[0][w];
    }

    //

    public static int dp2(int[] arr, int w) {
        int n = arr.length;
        int[][] dp = new int[n][w + 1];
        for (int i = 0; i < n; i++) {
            dp[i][0] = 1;
        }
        if (arr[0] <= w) {
            dp[0][arr[0]] = 1;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= w; j++) {
                dp[i][j] = dp[i - 1][j] + ((j - arr[i]) >= 0 ? dp[i - 1][j - arr[i]] : 0);
            }
        }
        int ans = 0;
        for (int j = 0; j <= w; j++) {
            ans += dp[n - 1][j];
        }
        return ans;
    }

    //

    public static void main(String[] args) {
        int[] arr = {4, 3, 2, 9};
        int w = 8;
        System.out.println(ways1(arr, w));
        System.out.println(dp1(arr, w));
        System.out.println(dp2(arr, w));
    }
}
