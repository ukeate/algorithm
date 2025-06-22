package base.dp6_biz;

// https://leetcode.com/problems/burst-balloons/
// 打爆气球，附近3个乘积得分
/**
 * 爆破气球问题
 * 有n个气球，每个气球有一个数值。爆破气球i可以获得nums[i-1] * nums[i] * nums[i+1]分数
 * 边界外的气球数值视为1。求爆破所有气球能获得的最大分数
 * 这是一个经典的区间DP问题，关键是逆向思考：最后爆破哪个气球
 */
public class BurstBalloons {
    /**
     * 递归方法 - 区间DP的递归实现
     * @param arr 扩展后的数组（两端添加1）
     * @param l 左边界
     * @param r 右边界
     * @return 爆破区间[l,r]内所有气球的最大得分
     */
    private static int process1(int[] arr, int l, int r) {
        // 只有一个气球时，直接计算得分
        if (l == r) {
            return arr[l - 1] * arr[l] * arr[r + 1];
        }
        
        // 考虑先爆破左边界或右边界的情况
        int max = Math.max(arr[l - 1] * arr[l] * arr[r + 1] + process1(arr, l + 1, r),
                arr[l - 1] * arr[r] * arr[r + 1] + process1(arr, l, r - 1));
        
        // 考虑最后爆破中间某个气球i的情况
        for (int i = l + 1; i < r; i++) {
            // i最后打爆，此时左右两部分已经爆破完毕
            max = Math.max(max, arr[l - 1] * arr[i] * arr[r + 1]
                    + process1(arr, l, i - 1) + process1(arr, i + 1, r));
        }
        return max;
    }

    /**
     * 方法1：递归解法
     */
    public static int max1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int n = arr.length;
        // 构造辅助数组，两端添加1
        int[] help = new int[n + 2];
        help[0] = 1;
        help[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        return process1(help, 1, n);
    }

    //

    /**
     * 方法2：动态规划解法
     * dp[l][r]表示爆破区间[l,r]内所有气球的最大得分
     */
    public static int max2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        if (arr.length == 1) {
            return arr[0];
        }
        int n = arr.length;
        // 构造辅助数组
        int[] help = new int[n + 2];
        help[0] = 1;
        help[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            help[i + 1] = arr[i];
        }
        
        int[][] dp = new int[n + 2][n + 2];
        
        // 基础情况：单个气球的得分
        for (int i = 1; i <= n; i++) {
            dp[i][i] = help[i - 1] * help[i] * help[i + 1];
        }
        
        // 填表：从小区间到大区间
        for (int l = n; l >= 1; l--) {
            for (int r = l + 1; r <= n; r++) {
                // 先爆破边界气球的情况
                int ans = Math.max(help[l - 1] * help[l] * help[r + 1] + dp[l + 1][r],
                        help[l - 1] * help[r] * help[r + 1] + dp[l][r - 1]);
                
                // 最后爆破中间气球i的情况
                for (int i = l + 1; i < r; i++) {
                    ans = Math.max(ans, help[l - 1] * help[i] * help[r + 1] + dp[l][i - 1] + dp[i + 1][r]);
                }
                dp[l][r] = ans;
            }
        }
        return dp[1][n];
    }

}
