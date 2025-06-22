package base.dp4_quad;

import java.util.Arrays;

// 小镇设num个邮局, 使总路程最小
/**
 * 邮局设置问题
 * 在一条直线上有n个村庄，要设置num个邮局，使得所有村庄到最近邮局的距离总和最小
 * 这是一个经典的四边形不等式优化DP问题
 */
public class PostOffice {
    /**
     * 方法1：标准动态规划解法
     * dp[i][j]表示前i+1个村庄设置j个邮局的最小总距离
     * 时间复杂度O(n³)
     */
    public static int min1(int[] arr, int num) {
        if (arr == null || num < 1 || arr.length < num) {
            return 0;
        }
        int n = arr.length;
        
        // w[l][r]表示在区间[l,r]内设置一个邮局的最小总距离
        // 最优位置是中位数位置
        int[][] w = new int[n + 1][n + 1];
        for (int l = 0; l < n; l++) {
            for (int r = l + 1; r < n; r++) {
                // 递推计算：新增一个村庄到中位数的距离
                w[l][r] = w[l][r - 1] + arr[r] - arr[(l + r) >> 1];
            }
        }
        
        // dp[i][j]表示前i+1个村庄设置j个邮局的最小总距离
        int[][] dp = new int[n][num + 1];
        
        // 基础条件：只设置一个邮局
        for (int i = 0; i < n; i++) {
            dp[i][1] = w[0][i];
        }
        
        // 填表
        for (int i = 1; i < n; i++) {
            for (int j = 2; j <= Math.min(i, num); j++) {
                int ans = Integer.MAX_VALUE;
                // 枚举最后一个邮局的服务范围
                for (int k = 0; k <= i; k++) {
                    ans = Math.min(ans, dp[k][j - 1] + w[k + 1][i]);
                }
                dp[i][j] = ans;
            }
        }
        return dp[n - 1][num];
    }

    //

    /**
     * 方法2：四边形不等式优化
     * 利用最优分割点的单调性，将时间复杂度优化到O(n²)
     */
    public static int min2(int[] arr, int num) {
        if (arr == null || num < 1 || arr.length < num) {
            return 0;
        }
        int n = arr.length;
        
        // 预处理区间代价
        int[][] w = new int[n + 1][n + 1];
        for (int l = 0; l < n; l++) {
            for (int r = l + 1; r < n; r++) {
                w[l][r] = w[l][r - 1] + arr[r] - arr[(l + r) >> 1];
            }
        }
        
        int[][] dp = new int[n][num + 1];
        int[][] best = new int[n][num + 1]; // 记录最优分割点
        
        // 基础条件
        for (int i = 0; i < n; i++) {
            dp[i][1] = w[0][i];
            best[i][1] = -1; // 只有一个邮局时，没有分割点
        }
        
        // 利用四边形不等式的单调性优化
        for (int j = 2; j <= num; j++) {
            for (int i = n - 1; i >= j; i--) {
                // 最优分割点的搜索范围
                int down = best[i][j - 1];
                int up = i == n - 1 ? n - 1 : best[i + 1][j];
                
                int ans = Integer.MAX_VALUE;
                int bestChoose = -1;
                
                // 在缩小的范围内搜索最优分割点
                for (int leftEnd = down; leftEnd <= up; leftEnd++) {
                    int leftCost = leftEnd == -1 ? 0 : dp[leftEnd][j - 1];
                    int rightCost = leftEnd == i ? 0 : w[leftEnd + 1][i];
                    int cur = leftCost + rightCost;
                    if (cur <= ans) {
                        ans = cur;
                        bestChoose = leftEnd;
                    }
                }
                dp[i][j] = ans;
                best[i][j] = bestChoose;
            }
        }
        return dp[n - 1][num];
    }

    //

    /**
     * 生成随机排序数组用于测试
     */
    private static int[] randomSortedArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i != len; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        Arrays.sort(arr); // 村庄位置需要排序
        return arr;
    }

    /**
     * 测试方法，验证两种算法的正确性
     */
    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 30;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random()) + 1;
            int[] arr = randomSortedArr(len, maxVal);
            int num = (int) ((maxLen + 1) * Math.random()) + 1;
            int ans1 = min1(arr, num);
            int ans2 = min2(arr, num);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
