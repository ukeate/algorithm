package base.dp4_quad;

/**
 * 石头合并问题 (Stone Merge Problem)
 * 有n堆石头排成一行，每次可以合并相邻的两堆，合并代价为两堆石头数量之和
 * 求合并成一堆石头的最小代价
 * 
 * 这是一个经典的区间DP问题，也是四边形不等式优化的典型应用
 * 提供三种解法：
 * 1. 递归解法 O(n³)
 * 2. 标准区间DP O(n³)  
 * 3. 四边形不等式优化 O(n²)
 */
public class StoneMerge {
    /**
     * 构建前缀和数组
     * @param arr 原数组
     * @return 前缀和数组
     */
    private static int[] sum(int[] arr) {
        int n = arr.length;
        int[] s = new int[n + 1];
        s[0] = 0;
        for (int i = 0; i < n; i++) {
            s[i + 1] = s[i] + arr[i];
        }
        return s;
    }

    /**
     * 计算区间[l,r]的石头总重量
     * @param s 前缀和数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间和
     */
    private static int w(int[] s, int l, int r) {
        return s[r + 1] - s[l];
    }

    /**
     * 递归方法：计算合并区间[l,r]的最小代价
     * @param l 左边界
     * @param r 右边界
     * @param s 前缀和数组
     * @return 最小合并代价
     */
    private static int process1(int l, int r, int[] s) {
        // 只有一堆石头，无需合并
        if (l == r) {
            return 0;
        }
        
        int next = Integer.MAX_VALUE;
        // 枚举所有可能的分割点
        for (int leftEnd = l; leftEnd < r; leftEnd++) {
            // 分别计算左右两部分的合并代价，再加上合并这两部分的代价
            next = Math.min(next, process1(l, leftEnd, s) + process1(leftEnd + 1, r, s));
        }
        // 加上合并当前区间的代价（等于区间内所有石头的重量和）
        return next + w(s, l, r);
    }

    /**
     * 方法1：递归解法
     * 时间复杂度O(n³)
     */
    public static int min1(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int[] s = sum(arr);
        return process1(0, n - 1, s);
    }

    //

    /**
     * 方法2：标准的区间动态规划
     * dp[l][r]表示合并区间[l,r]的最小代价
     * 时间复杂度O(n³)
     */
    public static int min2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int[] s = sum(arr);
        int[][] dp = new int[n][n];
        
        // 从小区间到大区间填表
        for (int l = n - 2; l >= 0; l--) {
            for (int r = l + 1; r < n; r++) {
                int next = Integer.MAX_VALUE;
                // 枚举分割点，找到最优的分割方案
                for (int leftEnd = l; leftEnd < r; leftEnd++) {
                    next = Math.min(next, dp[l][leftEnd] + dp[leftEnd + 1][r]);
                }
                // 记录合并区间[l,r]的最小代价
                dp[l][r] = next + w(s, l, r);
            }
        }
        return dp[0][n - 1];
    }

    //

    /**
     * 方法3：四边形不等式优化的区间DP
     * 利用最优分割点的单调性，将时间复杂度优化到O(n²)
     * 
     * 四边形不等式性质：对于区间[i,j]，最优分割点k满足单调性
     * best[i][j-1] ≤ best[i][j] ≤ best[i+1][j]
     */
    public static int min3(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int[] s = sum(arr);
        int[][] dp = new int[n][n];
        int[][] best = new int[n][n]; // 记录最优分割点
        
        // 基础条件：长度为2的区间
        for (int i = 0; i < n - 1; i++) {
            best[i][i + 1] = i;                // 只有一种分割方式
            dp[i][i + 1] = w(s, i, i + 1);     // 直接合并的代价
        }
        
        // 从长度为3的区间开始填表
        for (int l = n - 3; l >= 0; l--) {
            for (int r = l + 2; r < n; r++) {
                int next = Integer.MAX_VALUE;
                int choose = -1;
                
                // 利用四边形不等式，缩小搜索范围
                // 最优分割点在[best[l][r-1], best[l+1][r]]之间
                for (int leftEnd = best[l][r - 1]; leftEnd <= best[l + 1][r]; leftEnd++) {
                    int cur = dp[l][leftEnd] + dp[leftEnd + 1][r];
                    if (cur <= next) {
                        next = cur;
                        choose = leftEnd;
                    }
                }
                
                best[l][r] = choose;                // 记录最优分割点
                dp[l][r] = next + w(s, l, r);       // 记录最小代价
            }
        }
        return dp[0][n - 1];
    }

    //

    /**
     * 生成随机数组用于测试
     */
    private static int[] randomArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 测试方法，验证各个算法的正确性
     */
    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 15;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random());
            int[] arr = randomArr(len, maxVal);
            int ans1 = min1(arr);
            int ans2 = min2(arr);
            int ans3 = min3(arr);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
