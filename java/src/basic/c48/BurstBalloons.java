package basic.c48;

// 打爆气球加分L*X*R, L和R是左右最近没爆的气球分数，返回最大得分
public class BurstBalloons {
    // [l-1]和[r+1]一定没有打爆, 处理最后打的情况
    private static int process(int[] arr, int l, int r) {
        if (l == r) {
            return arr[l - 1] * arr[l] * arr[r + 1];
        }
        // 最后打爆l或r
        int max = Math.max(arr[l - 1] * arr[l] * arr[r + 1] + process(arr, l + 1, r),
                arr[l - 1] * arr[r] * arr[r + 1] + process(arr, l, r - 1));
        // 中间位置
        for (int i = l + 1; i < r; i++) {
            max = Math.max(max, arr[l - 1] * arr[i] * arr[r + 1]
                    + process(arr, l, i - 1) + process(arr, i + 1, r));
        }
        return max;
    }

    public static int max1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int n = arr.length;
        int[] help = new int[n + 2];
        help[0] = 1;
        help[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        return process(help, 1, n);
    }

    //

    public static int max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int n = arr.length;
        int[] help = new int[n + 2];
        help[0] = 1;
        help[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        int[][] dp = new int[n + 2][n + 2];
        for (int i = 1; i <= n; i++) {
            dp[i][i] = help[i - 1] * help[i] * help[i + 1];
        }
        for (int l = n; l >= 1; l--) {
            for (int r = l + 1; r <= n; r++) {
                int finalL = help[l - 1] * help[l] * help[r + 1] + dp[l + 1][r];
                int finalR = help[l - 1] * help[r] * help[r + 1] + dp[l][r - 1];
                dp[l][r] = Math.max(finalL, finalR);
                for (int i = l + 1; i < r; i++) {
                    dp[l][r] = Math.max(dp[l][r], help[l - 1] * help[i] * help[r + 1] + dp[l][i - 1] + dp[i + 1][r]);
                }
            }
        }
        return dp[1][n];
    }

    public static void main(String[] args) {
        int[] arr = {4, 2, 3, 5, 1, 6};
        System.out.println(max1(arr));
        System.out.println(max2(arr));
    }
}
