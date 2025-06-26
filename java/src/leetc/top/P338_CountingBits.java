package leetc.top;

/**
 * LeetCode 338. 比特位计数 (Counting Bits)
 * 
 * 问题描述：
 * 给你一个整数 n ，对于 0 <= i <= n 中的每个 i ，计算其二进制表示中 1 的个数，
 * 返回一个长度为 n + 1 的数组 ans 作为答案。
 * 
 * 示例：
 * 输入：n = 2
 * 输出：[0,1,1]
 * 解释：
 * 0 --> 0
 * 1 --> 1
 * 2 --> 10
 * 
 * 输入：n = 5
 * 输出：[0,1,1,2,1,2]
 * 解释：
 * 0 --> 0
 * 1 --> 1
 * 2 --> 10
 * 3 --> 11
 * 4 --> 100
 * 5 --> 101
 * 
 * 解法思路：
 * 动态规划 + 位运算规律：
 * 
 * 1. 核心观察：
 *    - 对于任意数字 i，其二进制中1的个数与 i/2 有关
 *    - i >> 1 表示将 i 右移一位（相当于除以2）
 *    - i & 1 表示 i 的最低位是否为1
 * 
 * 2. 状态转移关系：
 *    dp[i] = dp[i >> 1] + (i & 1)
 *    解释：
 *    - dp[i >> 1]：去掉最低位后的1的个数
 *    - (i & 1)：最低位是否为1
 * 
 * 3. 算法步骤：
 *    - 初始化 dp[0] = 0
 *    - 对于每个 i（1 到 n），使用状态转移方程计算
 *    - 返回整个dp数组
 * 
 * 4. 位运算技巧：
 *    - i >> 1：快速计算 i/2
 *    - i & 1：快速判断i的奇偶性（最低位）
 *    - 这种方法比逐一计算每个数的位数更高效
 * 
 * 核心思想：
 * - 递推关系：利用已计算的结果推导新结果
 * - 位运算优化：使用移位和按位与快速计算
 * - 一次遍历：O(n)时间复杂度，比朴素方法更优
 * 
 * 其他解法：
 * 1. Brian Kernighan算法：dp[i] = dp[i & (i-1)] + 1
 * 2. 最高有效位方法：利用2的幂次的规律
 * 3. 最低有效位方法：当前使用的方法
 * 
 * 时间复杂度：O(n) - 每个数字计算一次
 * 空间复杂度：O(1) - 除了返回数组，只使用常数空间
 * 
 * LeetCode链接：https://leetcode.com/problems/counting-bits/
 */
public class P338_CountingBits {
    
    /**
     * 计算0到n中每个数字二进制表示中1的个数
     * 
     * 使用动态规划 + 最低有效位方法：
     * 1. 对于数字i，其1的个数 = (i>>1)的1的个数 + i的最低位
     * 2. i>>1 相当于将i右移一位，去掉最低位
     * 3. i&1 表示i的最低位是0还是1
     * 
     * @param n 上界
     * @return 每个数字的1的个数数组
     */
    public int[] countBits(int n) {
        int[] dp = new int[n + 1];  // dp[i]表示数字i的二进制中1的个数
        
        // dp[0] = 0，数字0的二进制中没有1
        // 从1开始计算
        for (int i = 1; i <= n; i++) {
            // 状态转移：dp[i] = dp[i >> 1] + (i & 1)
            // i >> 1: 将i右移一位，相当于去掉最低位
            // i & 1: 检查i的最低位是否为1
            dp[i] = dp[i >> 1] + (i & 1);
        }
        
        return dp;
    }
    
    /**
     * 替代方法1：使用Brian Kernighan算法
     * 
     * 核心思想：i & (i-1) 会清除i的最低位的1
     * 状态转移：dp[i] = dp[i & (i-1)] + 1
     * 
     * @param n 上界
     * @return 每个数字的1的个数数组
     */
    public int[] countBitsBrianKernighan(int n) {
        int[] dp = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            // i & (i-1) 清除i的最低位1
            // 所以dp[i] = dp[i & (i-1)] + 1
            dp[i] = dp[i & (i - 1)] + 1;
        }
        
        return dp;
    }
    
    /**
     * 替代方法2：使用最高有效位方法
     * 
     * 利用2的幂次的规律：
     * - 对于2^k到2^(k+1)-1的数字，其模式具有规律性
     * 
     * @param n 上界
     * @return 每个数字的1的个数数组
     */
    public int[] countBitsHighestBit(int n) {
        int[] dp = new int[n + 1];
        int highestBit = 0;  // 当前最高位
        
        for (int i = 1; i <= n; i++) {
            // 当i是2的幂时，更新最高位
            if ((i & (i - 1)) == 0) {
                highestBit = i;
            }
            
            // dp[i] = dp[i - highestBit] + 1
            // i - highestBit 表示去掉最高位1后的数字
            dp[i] = dp[i - highestBit] + 1;
        }
        
        return dp;
    }
}
