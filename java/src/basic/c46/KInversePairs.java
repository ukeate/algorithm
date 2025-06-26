package basic.c46;
// https://leetcode.cn/problems/k-inverse-pairs-array/
// [1,n]个数字，求有多少种排列有k个逆序对

/**
 * K个逆序对的数组排列问题
 * 
 * 问题描述：
 * 给定n和k，求由数字[1,2,3,...,n]组成的排列中，恰好包含k个逆序对的排列数量
 * 逆序对：在数组中，如果i < j但arr[i] > arr[j]，则(i,j)是一个逆序对
 * 
 * 例如：
 * n=3, k=1 -> 可能的排列：[1,3,2], [2,1,3] -> 答案是2
 * n=4, k=2 -> 多种满足条件的排列 -> 需要计算总数
 * 
 * 解法思路：
 * 使用动态规划，dp[i][j]表示用数字[1,2,...,i]组成的排列中恰好有j个逆序对的数量
 * 
 * 状态转移分析：
 * 考虑数字i如何插入到已有的[1,2,...,i-1]的排列中：
 * - 插在最后：不产生新的逆序对
 * - 插在倒数第2位：产生1个逆序对
 * - ...
 * - 插在最前面：产生i-1个逆序对
 * 
 * 因此：dp[i][j] = sum(dp[i-1][j-0], dp[i-1][j-1], ..., dp[i-1][j-(i-1)])
 * 
 * 优化：使用前缀和避免重复计算
 * 
 * 时间复杂度：O(n*k)
 * 空间复杂度：O(n*k)
 * 
 * @author 算法学习
 * @see <a href="https://leetcode.cn/problems/k-inverse-pairs-array/">LeetCode 629</a>
 */
public class KInversePairs {
    
    /**
     * 方法1：基础动态规划解法
     * 
     * @param n 数组长度，使用[1,2,...,n]
     * @param k 目标逆序对数量
     * @return 恰好有k个逆序对的排列数量
     * 
     * 算法思路：
     * dp[i][j] = 用数字[1,2,...,i]组成排列中恰好有j个逆序对的数量
     * 
     * 状态转移：
     * 数字i可以插入到[1,2,...,i-1]排列的任意位置：
     * - 插在位置0：产生i-1个逆序对
     * - 插在位置1：产生i-2个逆序对
     * - ...
     * - 插在位置i-1：产生0个逆序对
     * 
     * 所以：dp[i][j] = dp[i-1][j] + dp[i-1][j-1] + ... + dp[i-1][j-(i-1)]
     */
    public static int ways1(int n, int k) {
        if (n < 1 || k < 0) {
            return 0;
        }
        
        // dp[i][j]: 用[1,2,...,i]组成排列，恰好有j个逆序对的数量
        int[][] dp = new int[n + 1][k + 1];
        
        // 边界条件：空排列有0个逆序对，数量为1
        dp[0][0] = 1;
        
        int mod = 1000000007; // 防止溢出
        
        // 枚举排列长度
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 1; // 有序排列[1,2,...,i]只有0个逆序对
            
            // 枚举逆序对数量
            for (int j = 1; j <= k; j++) {
                // 枚举数字i的插入位置（从后往前数第r个位置）
                // 插入第r个位置会产生r个逆序对
                for (int r = Math.max(0, j - i + 1); r <= j; r++) {
                    dp[i][j] += dp[i - 1][r];
                    dp[i][j] %= mod;
                }
            }
        }
        
        return dp[n][k];
    }

    /**
     * 方法2：前缀和优化的动态规划
     * 
     * @param n 数组长度
     * @param k 目标逆序对数量
     * @return 恰好有k个逆序对的排列数量
     * 
     * 算法优化：
     * 观察到状态转移中需要计算连续区间的和，可以用前缀和优化
     * 
     * 设前缀和 S[i][j] = dp[i][0] + dp[i][1] + ... + dp[i][j]
     * 则 dp[i][j] = S[i-1][j] - S[i-1][j-i] （如果j >= i）
     * 
     * 进一步优化：直接用dp数组维护前缀和
     */
    public static int ways2(int n, int k) {
        if (n < 1 || k < 0) {
            return 0;
        }
        
        int[][] dp = new int[n + 1][k + 1];
        dp[0][0] = 1;
        int mod = 1000000007;
        
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 1;
            
            for (int j = 1; j <= k; j++) {
                // 利用前缀和的递推关系
                dp[i][j] = (dp[i][j - 1] + dp[i - 1][j]) % mod;
                
                // 减去超出范围的部分
                if (j >= i) {
                    dp[i][j] = (dp[i][j] - dp[i - 1][j - i] + mod) % mod;
                }
            }
        }
        
        return dp[n][k];
    }

    /**
     * 方法3：简化版本的前缀和优化
     * 
     * @param n 数组长度
     * @param k 目标逆序对数量
     * @return 恰好有k个逆序对的排列数量
     * 
     * 算法思路：
     * 去掉取模运算的复杂处理，直接计算差值
     */
    public static int ways3(int n, int k) {
        if (n < 1 || k < 0) {
            return 0;
        }
        if (k == 0) {
            return 1; // 只有升序排列
        }
        
        int[][] dp = new int[n + 1][k + 1];
        
        // 边界条件：长度为1的排列只有0个逆序对
        dp[1][0] = 1;
        
        // 初始化：长度为i的升序排列都有0个逆序对
        for (int i = 2; i <= n; i++) {
            dp[i][0] = 1;
        }
        
        // 状态转移
        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= k; j++) {
                // 前缀和优化的状态转移
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j] - (i <= j ? dp[i - 1][j - i] : 0);
            }
        }
        
        return dp[n][k];
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== K个逆序对排列数测试 ===");
        
        // 基础测试用例
        int n = 9;
        int k = 15;
        
        System.out.printf("测试参数: n=%d, k=%d\n", n, k);
        
        long start = System.currentTimeMillis();
        int result1 = ways1(n, k);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = ways2(n, k);
        long time2 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result3 = ways3(n, k);
        long time3 = System.currentTimeMillis() - start;
        
        System.out.printf("方法1（基础DP）: 结果=%d, 耗时=%dms\n", result1, time1);
        System.out.printf("方法2（前缀和优化）: 结果=%d, 耗时=%dms\n", result2, time2);
        System.out.printf("方法3（简化版本）: 结果=%d, 耗时=%dms\n", result3, time3);
        
        // 验证结果一致性
        if (result1 == result2 && result2 == result3) {
            System.out.println("✓ 所有方法结果一致");
        } else {
            System.out.println("✗ 结果不一致，需要检查算法");
        }
        
        // 小规模测试用例验证
        System.out.println("\n=== 小规模测试用例 ===");
        int[][] testCases = {
            {3, 0}, // [1,2,3] -> 1种
            {3, 1}, // [1,3,2], [2,1,3] -> 2种
            {4, 2}, // 多种情况
            {4, 0}, // [1,2,3,4] -> 1种
            {1, 0}, // [1] -> 1种
            {2, 1}  // [2,1] -> 1种
        };
        
        for (int[] testCase : testCases) {
            int tn = testCase[0];
            int tk = testCase[1];
            int result = ways2(tn, tk); // 使用优化版本
            System.out.printf("n=%d, k=%d -> %d种排列\n", tn, tk, result);
        }
        
        // 手工验证小例子
        System.out.println("\n=== 手工验证 n=3, k=1 ===");
        System.out.println("可能的排列及其逆序对：");
        System.out.println("[1,2,3] -> 逆序对数: 0");
        System.out.println("[1,3,2] -> 逆序对数: 1 (3>2)");
        System.out.println("[2,1,3] -> 逆序对数: 1 (2>1)");
        System.out.println("[2,3,1] -> 逆序对数: 2 (2>1, 3>1)");
        System.out.println("[3,1,2] -> 逆序对数: 2 (3>1, 3>2)");
        System.out.println("[3,2,1] -> 逆序对数: 3 (3>2, 3>1, 2>1)");
        System.out.println("恰好1个逆序对的排列: [1,3,2], [2,1,3] -> 2种");
        System.out.printf("算法计算结果: %d种\n", ways2(3, 1));
    }
}
