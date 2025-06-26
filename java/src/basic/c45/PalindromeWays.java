package basic.c45;

/**
 * 子序列形成回文的方法数
 * 
 * 问题描述：
 * 给定一个字符串，计算有多少种不同的方式可以选择子序列来形成回文串
 * 注意：空串不算在内，单个字符算作回文
 * 
 * 例如：
 * "ABA" -> 可以选择 "A"(2种), "B"(1种), "ABA"(1种), "AA"(1种) = 5种
 * "XXY" -> 可以选择 "X"(2种), "Y"(1种), "XX"(1种) = 4种
 * 
 * 算法思路：
 * 使用区间动态规划
 * dp[i][j] 表示字符串从位置i到位置j可以形成的回文子序列数量
 * 
 * 状态转移：
 * 1. 如果 s[i] == s[j]：
 *    - 可以选择不要i和j：dp[i+1][j-1]种方式
 *    - 可以只要i或只要j：dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1]种方式
 *    - 可以要i和j组成新回文：dp[i+1][j-1] + 1种方式
 *    - 总计：dp[i+1][j] + dp[i][j-1] + 1
 * 
 * 2. 如果 s[i] != s[j]：
 *    - dp[i][j] = dp[i+1][j] + dp[i][j-1] - dp[i+1][j-1]
 *    - 需要减去重复计算的dp[i+1][j-1]
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 * 
 * @author 算法学习
 */
public class PalindromeWays {
    
    /**
     * 方法1：使用1-indexed的动态规划
     * 
     * @param str 输入字符串
     * @return 可以形成的回文子序列数量
     * 
     * 算法细节：
     * - 使用1-indexed的dp数组，dp[i][j]表示原字符串[i-1, j-1]范围的回文数量
     * - 边界条件：dp[i][i] = 1（空串对应1种方式，但题目要求空串不算，这里是技巧处理）
     * - 从长度2开始逐步扩展区间
     */
    public static int ways1(String str) {
        char[] s = str.toCharArray();
        int len = s.length;
        
        // dp[i][j]表示原字符串[i-1, j-1]范围内的回文子序列数量（1-indexed）
        int[][] dp = new int[len + 1][len + 1];
        
        // 边界条件：空区间有1种方式（用于递推计算）
        for (int i = 0; i <= len; i++) {
            dp[i][i] = 1;
        }
        
        // 按子串长度从小到大计算
        for (int subLen = 2; subLen <= len; subLen++) {
            for (int l = 1; l <= len - subLen + 1; l++) {
                int r = l + subLen - 1;
                
                // 不选择左端点的方案数
                dp[l][r] += dp[l + 1][r];
                // 不选择右端点的方案数
                dp[l][r] += dp[l][r - 1];
                
                if (s[l - 1] == s[r - 1]) { // 注意：数组是0-indexed，dp是1-indexed
                    // 左右端点相同，可以组成新的回文
                    // 加上左右端点单独组成回文的1种方式
                    dp[l][r] += 1;
                } else {
                    // 左右端点不同，需要减去重复计算的部分
                    dp[l][r] -= dp[l + 1][r - 1];
                }
            }
        }
        
        // 返回整个字符串的回文子序列数量，减去空串的1种方式
        return dp[1][len];
    }

    /**
     * 方法2：使用0-indexed的动态规划（更直观）
     * 
     * @param str 输入字符串
     * @return 可以形成的回文子序列数量
     * 
     * 算法细节：
     * - 使用0-indexed的dp数组，dp[i][j]表示字符串[i, j]范围的回文数量
     * - 边界条件：
     *   - dp[i][i] = 1（单个字符）
     *   - dp[i][i+1] = s[i] == s[i+1] ? 3 : 2（两个字符的情况）
     * - 状态转移更加清晰
     */
    public static int ways2(String str) {
        char[] s = str.toCharArray();
        int n = s.length;
        
        // dp[i][j]表示字符串[i, j]范围内的回文子序列数量
        int[][] dp = new int[n][n];
        
        // 边界条件1：单个字符都是回文
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        
        // 边界条件2：两个字符的情况
        for (int i = 0; i < n - 1; i++) {
            if (s[i] == s[i + 1]) {
                // 两个字符相同：可以选择s[i], s[i+1], s[i]s[i+1] = 3种方式
                dp[i][i + 1] = 3;
            } else {
                // 两个字符不同：可以选择s[i], s[i+1] = 2种方式
                dp[i][i + 1] = 2;
            }
        }
        
        // 从长度3开始，从右下角向左上角填表
        for (int l = n - 3; l >= 0; l--) {
            for (int r = l + 2; r < n; r++) {
                // 基础状态转移：包含左边 + 包含右边 - 重复的中间部分
                dp[l][r] = dp[l + 1][r] + dp[l][r - 1] - dp[l + 1][r - 1];
                
                if (s[l] == s[r]) {
                    // 左右端点相同时，可以：
                    // 1. 与中间的每个回文组合：dp[l+1][r-1]种
                    // 2. 单独组成一个新回文：1种
                    dp[l][r] += dp[l + 1][r - 1] + 1;
                }
            }
        }
        
        return dp[0][n - 1];
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 回文子序列方法数测试 ===");
        
        // 测试用例1
        String test1 = "ABA";
        System.out.println("字符串: " + test1);
        System.out.println("方法1结果: " + ways1(test1));
        System.out.println("方法2结果: " + ways2(test1));
        System.out.println("分析: A(2种) + B(1种) + ABA(1种) + AA(1种) = 5种");
        System.out.println();
        
        // 测试用例2
        String test2 = "XXY";
        System.out.println("字符串: " + test2);
        System.out.println("方法1结果: " + ways1(test2));
        System.out.println("方法2结果: " + ways2(test2));
        System.out.println("分析: X(2种) + Y(1种) + XX(1种) = 4种");
        System.out.println();
        
        // 更多测试用例
        System.out.println("=== 更多测试用例 ===");
        String[] testCases = {
            "A",        // 1种: A
            "AA",       // 3种: A, A, AA  
            "AB",       // 2种: A, B
            "AAA",      // 7种: A(3), AA(3), AAA(1)
            "ABBA",     // 多种组合
            "ABCD"      // 4种: A, B, C, D
        };
        
        for (String test : testCases) {
            System.out.printf("字符串 \"%s\" -> 方法1: %d, 方法2: %d%n", 
                            test, ways1(test), ways2(test));
        }
        
        // 性能测试
        System.out.println("\n=== 性能对比 ===");
        String longStr = "ABCABCABC";
        
        long start = System.currentTimeMillis();
        int result1 = ways1(longStr);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = ways2(longStr);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.printf("测试字符串: %s%n", longStr);
        System.out.printf("方法1: 结果=%d, 耗时=%dms%n", result1, time1);
        System.out.printf("方法2: 结果=%d, 耗时=%dms%n", result2, time2);
    }
}
