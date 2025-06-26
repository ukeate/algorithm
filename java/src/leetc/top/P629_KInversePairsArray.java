package leetc.top;

/**
 * LeetCode 629. K个逆序对数组 (K Inverse Pairs Array)
 * 
 * 问题描述：
 * 给出两个整数 n 和 k，找出所有包含从 1 到 n 的数字，且恰好拥有 k 个逆序对的不同的数组的个数。
 * 
 * 逆序对的定义如下：对于数组的第i个和第j个元素，如果满i < j且a[i] > a[j]，则其为一个逆序对；
 * 否则不是。
 * 
 * 由于答案可能很大，只需要返回 答案 mod 10^9 + 7 的值。
 * 
 * 示例：
 * - 输入: n = 3, k = 0
 * - 输出: 1
 * - 解释: 只有数组 [1,2,3] 包含了从1到3的整数并且正好拥有 0 个逆序对。
 * 
 * - 输入: n = 3, k = 1  
 * - 输出: 2
 * - 解释: 数组 [1,3,2] 和 [2,1,3] 都有正好 1 个逆序对。
 * 
 * 解法思路：
 * 动态规划 + 滑动窗口优化：
 * 1. dp[i][j] 表示使用前i个数字（1到i），恰好有j个逆序对的数组个数
 * 2. 状态转移：考虑数字i的插入位置，可以在已排序的i-1个数字中的任意位置插入
 * 3. 插入在位置k会产生k个新的逆序对（与后面k个数字形成逆序对）
 * 4. dp[i][j] = dp[i-1][j] + dp[i-1][j-1] + ... + dp[i-1][j-(i-1)]
 * 5. 使用滑动窗口技巧优化求和过程
 * 
 * 核心思想：
 * - 考虑数字的插入顺序：按1,2,3...n的顺序依次插入
 * - 新数字的插入位置决定了新增的逆序对数量
 * - 使用前缀和技巧避免重复计算区间和
 * 
 * 时间复杂度：O(n × k) - 二维DP，每个状态O(1)计算
 * 空间复杂度：O(n × k) - 二维DP数组
 * 
 * LeetCode链接：https://leetcode.com/problems/k-inverse-pairs-array/
 */
public class P629_KInversePairsArray {
    
    /**
     * 计算恰好有k个逆序对的数组个数
     * 
     * 动态规划状态定义：
     * dp[i][j] = 使用数字1到i，恰好有j个逆序对的数组个数
     * 
     * 状态转移方程：
     * 考虑数字i的插入位置，如果插入在第p个位置（从右往左数），
     * 会与后面的p个数字形成逆序对，所以：
     * dp[i][j] = dp[i-1][j] + dp[i-1][j-1] + ... + dp[i-1][max(0, j-(i-1))]
     * 
     * 滑动窗口优化：
     * 由于每次都是计算连续区间的和，可以用滑动窗口技巧：
     * dp[i][j] = dp[i][j-1] + dp[i-1][j] - dp[i-1][j-i] (如果j >= i)
     * 
     * @param n 数字范围1到n
     * @param k 逆序对数量
     * @return 满足条件的数组个数，结果对10^9+7取模
     */
    public static int kInversePairs(int n, int k) {
        // 边界条件检查
        if (n < 1 || k < 0) {
            return 0;
        }
        
        // dp[i][j] = 使用1到i的数字，恰好有j个逆序对的数组个数
        int[][] dp = new int[n + 1][k + 1];
        
        // 初始化：使用0个数字，0个逆序对的方案数为1（空数组）
        dp[0][0] = 1;
        
        int mod = 1000000007;
        
        // 填充DP表
        for (int i = 1; i <= n; i++) {
            // 使用i个数字，0个逆序对只有一种方案：[1,2,3,...,i]
            dp[i][0] = 1;
            
            for (int j = 1; j <= k; j++) {
                // 滑动窗口优化的状态转移
                // dp[i][j] = dp[i][j-1] + dp[i-1][j] - dp[i-1][j-i]
                
                // 当前状态 = 前一个j状态 + 前一个i状态
                dp[i][j] = (dp[i][j - 1] + dp[i - 1][j]) % mod;
                
                // 减去滑出窗口的部分（如果j >= i）
                if (j >= i) {
                    dp[i][j] = (dp[i][j] - dp[i - 1][j - i] + mod) % mod;
                }
            }
        }
        
        return dp[n][k];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：基础示例
        System.out.println("测试用例1:");
        System.out.println("n = 3, k = 0");
        System.out.println("输出: " + kInversePairs(3, 0));
        System.out.println("期望: 1");
        System.out.println("数组: [1,2,3]");
        System.out.println();
        
        // 测试用例2：一个逆序对
        System.out.println("测试用例2:");
        System.out.println("n = 3, k = 1");
        System.out.println("输出: " + kInversePairs(3, 1));
        System.out.println("期望: 2");
        System.out.println("数组: [1,3,2], [2,1,3]");
        System.out.println();
        
        // 测试用例3：更多逆序对
        System.out.println("测试用例3:");
        System.out.println("n = 4, k = 2");
        System.out.println("输出: " + kInversePairs(4, 2));
        System.out.println("期望: 5");
        System.out.println();
        
        // 测试用例4：边界情况 - k=0
        System.out.println("测试用例4 (k=0):");
        System.out.println("n = 5, k = 0");
        System.out.println("输出: " + kInversePairs(5, 0));
        System.out.println("期望: 1");
        System.out.println("只有一种方案: [1,2,3,4,5]");
        System.out.println();
        
        // 测试用例5：最大逆序对
        System.out.println("测试用例5 (最大逆序对):");
        System.out.println("n = 4, k = 6");
        System.out.println("输出: " + kInversePairs(4, 6));
        System.out.println("期望: 1");
        System.out.println("只有一种方案: [4,3,2,1] (最大逆序对数为C(4,2)=6)");
        System.out.println();
        
        // 测试用例6：不可能的情况
        System.out.println("测试用例6 (不可能):");
        System.out.println("n = 3, k = 10");
        System.out.println("输出: " + kInversePairs(3, 10));
        System.out.println("期望: 0");
        System.out.println("3个数字最多只能有C(3,2)=3个逆序对");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 动态规划 + 滑动窗口优化");
        System.out.println("- 时间复杂度: O(n×k) - 避免了内层循环");
        System.out.println("- 空间复杂度: O(n×k) - 二维DP数组");
        System.out.println("- 关键技巧: 考虑新数字的插入位置对逆序对的贡献");
        System.out.println("- 优化策略: 滑动窗口避免重复计算区间和");
        System.out.println("- 数学背景: n个数字最多有C(n,2)=n*(n-1)/2个逆序对");
    }
}
