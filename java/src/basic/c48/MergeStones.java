package basic.c48;

/**
 * 合并石子问题 - K个相邻石子合并版本
 * 
 * 问题描述：
 * 有N堆石子排成一行，每次可以将相邻的K堆石子合并成一堆，
 * 合并的代价是这K堆石子的重量和。
 * 求把所有石子合并成一堆的最小代价。
 * 
 * 算法思路：
 * 1. 区间动态规划：dp[l][r][part] 表示将l到r区间合并成part堆的最小代价
 * 2. 状态转移：
 *    - 当part=1时：先合并成K堆，再合并成1堆
 *    - 当part>1时：枚举中间分割点，递归求解
 * 3. 合并条件：只有当(n-1)%(k-1)==0时才能合并成一堆
 * 
 * 时间复杂度：O(n^3 * k)
 * 空间复杂度：O(n^2 * k)
 * 
 * @author 算法学习
 */
public class MergeStones {
    
    /**
     * 区间动态规划求解合并石子最小代价
     * 
     * @param l 左边界
     * @param r 右边界  
     * @param part 要合并成的堆数
     * @param arr 石子数组
     * @param k 每次合并的堆数
     * @param presum 前缀和数组
     * @param dp 记忆化搜索数组
     * @return 最小合并代价
     */
    private static int process1(int l, int r, int part, int[] arr, int k, int[] presum, int[][][] dp) {
        // 如果已经计算过，直接返回结果
        if (dp[l][r][part] != 0) {
            return dp[l][r][part];
        }
        
        // 基础情况：只有一个元素时，代价为0
        if (l == r) {
            return 0;
        }
        
        int ans = Integer.MAX_VALUE;
        
        // 如果要合并成1堆
        if (part == 1) {
            // 先合并成k堆，然后合并成1堆，加上合并代价
            ans = process1(l, r, k, arr, k, presum, dp) + presum[r + 1] - presum[l];
        } else {
            // 合并成多堆：枚举分割点
            // 每次步长为k-1，确保左边能合并成1堆
            for (int mid = l; mid < r; mid += k - 1) {
                int next1 = process1(l, mid, 1, arr, k, presum, dp);           // 左边合并成1堆
                int next2 = process1(mid + 1, r, part - 1, arr, k, presum, dp); // 右边合并成part-1堆
                ans = Math.min(ans, next1 + next2);
            }
        }
        
        dp[l][r][part] = ans;
        return ans;
    }

    /**
     * 合并石子主方法
     * 
     * @param stones 石子重量数组
     * @param k 每次合并的石子堆数
     * @return 最小合并代价，无法合并返回-1
     */
    public static int min1(int[] stones, int k) {
        int n = stones.length;
        
        // 判断是否能合并成一堆：(n-1) % (k-1) 必须等于0
        // 原理：每次合并k堆变成1堆，实际减少k-1堆
        if ((n - 1) % (k - 1) > 0) {
            return -1;
        }
        
        // 构建前缀和数组，用于快速计算区间和
        int[] presum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            presum[i + 1] = presum[i] + stones[i];
        }
        
        // 初始化dp数组：dp[l][r][part] = 将l到r合并成part堆的最小代价
        int[][][] dp = new int[n][n][k + 1];
        
        return process1(0, n - 1, 1, stones, k, presum, dp);
    }

    /**
     * 生成随机数组用于测试
     * 
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) (maxLen * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    /**
     * 测试方法
     * 验证合并石子算法的正确性
     */
    public static void main(String[] args) {
        int maxLen = 12;
        int maxVal = 100;
        
        // 生成随机测试数据
        int[] arr = randomArr(maxLen, maxVal);
        int k = (int) (Math.random() * 7) + 2; // k在2-8之间
        
        System.out.println("石子数组：");
        print(arr);
        System.out.println("每次合并堆数 k = " + k);
        
        int result = min1(arr, k);
        if (result == -1) {
            System.out.println("无法合并成一堆");
        } else {
            System.out.println("最小合并代价：" + result);
        }
    }
}
