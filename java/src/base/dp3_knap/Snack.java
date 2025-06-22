package base.dp3_knap;

// 背包装零食
/**
 * 背包装零食问题
 * 给定零食数组arr和背包容量w，求有多少种装法（每种零食可装可不装）
 * 这是一个经典的0-1背包变种问题，求方案数而不是最大价值
 */
public class Snack {
    /**
     * 递归方法 - 暴力枚举所有可能
     * @param arr 零食重量数组
     * @param idx 当前处理的零食索引
     * @param rest 背包剩余容量
     * @return 方案数，-1表示当前方案不可行
     */
    private static int process1(int[] arr, int idx, int rest) {
        // 容量不够，当前方案不可行
        if (rest < 0) {
            return -1;
        }
        // 所有零食都处理完了，得到一种有效方案
        if (idx == arr.length) {
            return 1;
        }
        // 不选择当前零食
        int next1 = process1(arr, idx + 1, rest);
        // 选择当前零食
        int next2 = process1(arr, idx + 1, rest - arr[idx]);
        // 累加有效方案数
        return next1 + (next2 == -1 ? 0 : next2);
    }

    /**
     * 方法1的对外接口
     */
    public static int ways1(int[] arr, int w) {
        return process1(arr, 0, w);
    }

    //

    /**
     * 动态规划方法1 - 从右往左填表
     * dp[i][j]表示从第i个零食开始，背包剩余容量为j时的方案数
     */
    public static int dp1(int[] arr, int w) {
        int n = arr.length;
        int[][] dp = new int[n + 1][w + 1];
        // 基础条件：没有零食时，只有一种方案（什么都不装）
        for (int j = 0; j <= w; j++) {
            dp[n][j] = 1;
        }
        // 从后往前填表
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j <= w; j++) {
                // 不选择当前零食 + 选择当前零食（如果容量够）
                dp[i][j] = dp[i + 1][j] + ((j - arr[i] >= 0) ? dp[i + 1][j - arr[i]] : 0);
            }
        }
        return dp[0][w];
    }

    //

    /**
     * 动态规划方法2 - 从左往右填表
     * dp[i][j]表示前i个零食，背包容量为j时的方案数
     */
    public static int dp2(int[] arr, int w) {
        int n = arr.length;
        int[][] dp = new int[n][w + 1];
        // 基础条件：容量为0时，只有一种方案（什么都不装）
        for (int i = 0; i < n; i++) {
            dp[i][0] = 1;
        }
        // 第一个零食的初始化
        if (arr[0] <= w) {
            dp[0][arr[0]] = 1;
        }
        // 从第二个零食开始填表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= w; j++) {
                // 不选择当前零食 + 选择当前零食（如果容量够）
                dp[i][j] = dp[i - 1][j] + ((j - arr[i]) >= 0 ? dp[i - 1][j - arr[i]] : 0);
            }
        }
        // 统计所有可能容量的方案数
        int ans = 0;
        for (int j = 0; j <= w; j++) {
            ans += dp[n - 1][j];
        }
        return ans;
    }

    //

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int[] arr = {4, 3, 2, 9};
        int w = 8;
        System.out.println(ways1(arr, w));
        System.out.println(dp1(arr, w));
        System.out.println(dp2(arr, w));
    }
}
