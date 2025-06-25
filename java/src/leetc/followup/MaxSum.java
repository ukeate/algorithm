package leetc.followup;

/**
 * 不相邻元素最大和问题 (Maximum Sum of Non-Adjacent Elements)
 * 
 * 问题描述：
 * 给定一个数组，从中选取若干元素使其累加和最大
 * 约束条件：选取的元素不能相邻
 * 
 * 这是一个经典的动态规划问题，类似于"打家劫舍"问题
 * 
 * 解法思路：
 * 动态规划：
 * 1. 定义状态：dp[i] 表示从前i+1个元素中选取不相邻元素的最大和
 * 2. 状态转移方程：
 *    dp[i] = max(dp[i-1], arr[i] + dp[i-2])
 *    - dp[i-1]：不选择当前元素，继承前面的最优解
 *    - arr[i] + dp[i-2]：选择当前元素，加上前前一个位置的最优解
 * 3. 边界条件：
 *    - dp[0] = arr[0]：只有一个元素时
 *    - dp[1] = max(arr[0], arr[1])：有两个元素时选较大的
 * 
 * 核心思想：
 * 对于每个位置，我们有两个选择：
 * - 选择当前元素：那么不能选择前一个元素，最大和为 arr[i] + dp[i-2]
 * - 不选择当前元素：最大和为 dp[i-1]
 * 取两者中的较大值
 * 
 * 时间复杂度：O(n) - 只需要遍历一次数组
 * 空间复杂度：O(n) - 需要dp数组存储中间结果（可优化为O(1)）
 */
public class MaxSum {
    /**
     * 计算不相邻元素的最大累加和
     * 
     * @param arr 输入数组
     * @return 不相邻元素的最大累加和
     */
    public static int maxSum(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0; // 空数组情况
        }
        
        int n = arr.length;
        if (n == 1) {
            return arr[0]; // 只有一个元素
        }
        if (n == 2) {
            return Math.max(arr[0], arr[1]); // 两个元素选较大的
        }
        
        // 动态规划数组
        int[] dp = new int[n];
        dp[0] = arr[0];                           // 边界条件：第一个元素
        dp[1] = Math.max(arr[0], arr[1]);         // 边界条件：前两个元素中的较大值
        
        // 状态转移
        for (int i = 2; i < n; i++) {
            // dp[i] = max(不选当前元素, 选当前元素)
            //       = max(dp[i-1], arr[i] + dp[i-2])
            dp[i] = Math.max(dp[i - 1], arr[i] + dp[i - 2]);
        }
        
        return dp[n - 1]; // 返回整个数组的最优解
    }
}
