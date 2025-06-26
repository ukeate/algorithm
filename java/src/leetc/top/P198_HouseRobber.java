package leetc.top;

/**
 * LeetCode 198. 打家劫舍 (House Robber)
 * 
 * 问题描述：
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
 * 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * 
 * 给定一个代表每个房屋存放金额的非负整数数组，
 * 计算你不触动警报装置的情况下，一夜之间能够偷窃到的最高金额。
 * 
 * 示例：
 * 输入：[1,2,3,1]   输出：4
 * 解释：偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 * 
 * 输入：[2,7,9,3,1] 输出：12
 * 解释：偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
 *      偷窃到的最高金额 = 2 + 9 + 1 = 12 。
 * 
 * 解法思路：
 * 动态规划：
 * 1. 状态定义：dp[i] 表示偷窃前i+1间房屋能获得的最大金额
 * 2. 状态转移：对于第i间房屋，有两种选择：
 *    - 偷第i间：dp[i] = nums[i] + dp[i-2]（不能偷相邻的第i-1间）
 *    - 不偷第i间：dp[i] = dp[i-1]（保持前面的最优解）
 *    - 取两者最大值：dp[i] = max(dp[i-1], nums[i] + dp[i-2])
 * 
 * 边界条件：
 * - dp[0] = nums[0]：只有一间房屋时，偷它
 * - dp[1] = max(nums[0], nums[1])：两间房屋时，偷金额大的那间
 * 
 * 本类提供两种实现：
 * 1. 标准DP数组解法：使用O(n)空间
 * 2. 空间优化解法：使用O(1)空间
 * 
 * 核心思想：
 * - 每个位置的最优解只依赖于前两个位置的结果
 * - 通过状态转移方程保证不偷相邻房屋
 * - 贪心思想：在满足约束的前提下追求局部最优
 * 
 * 时间复杂度：O(n) - 遍历数组一次
 * 空间复杂度：rob()为O(n)，rob2()为O(1)
 * 
 * LeetCode链接：https://leetcode.com/problems/house-robber/
 */
public class P198_HouseRobber {
    
    /**
     * 方法1：标准动态规划解法
     * 使用dp数组存储每个位置的最优解
     * 
     * @param nums 每个房屋的金额数组
     * @return 不触动警报的最大偷窃金额
     */
    public static int rob(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        
        int n = nums.length;
        int[] dp = new int[n];
        
        // 初始化边界条件
        dp[0] = nums[0];                        // 只有第0间房屋
        dp[1] = Math.max(nums[0], nums[1]);     // 有第0、1间房屋
        
        // 状态转移
        for (int i = 2; i < n; i++) {
            // 选择偷第i间房屋 vs 不偷第i间房屋
            dp[i] = Math.max(dp[i - 1], nums[i] + dp[i - 2]);
        }
        
        return dp[n - 1];
    }

    /**
     * 方法2：空间优化的动态规划解法
     * 由于dp[i]只依赖于dp[i-1]和dp[i-2]，可以用两个变量代替数组
     * 
     * @param nums 每个房屋的金额数组
     * @return 不触动警报的最大偷窃金额
     */
    public static int rob2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        
        // 使用两个变量代替dp数组
        int pre2 = nums[0];                        // dp[i-2]：前前个位置的最优解
        int pre1 = Math.max(nums[0], nums[1]);     // dp[i-1]：前个位置的最优解
        
        // 滚动更新
        for (int i = 2; i < nums.length; i++) {
            int cur = Math.max(pre1, nums[i] + pre2);  // 当前位置的最优解
            pre2 = pre1;  // 更新pre2为原来的pre1
            pre1 = cur;   // 更新pre1为当前的cur
        }
        
        return pre1;  // 最终答案
    }
}
