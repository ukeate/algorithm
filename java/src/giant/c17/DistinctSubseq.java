package giant.c17;

/**
 * 不同子序列数量问题
 * 
 * 问题描述：
 * 给定一个字符串s和一个字符串t，计算在s的所有子序列中，有多少个等于t。
 * 
 * 例如：
 * s = "rabbbit", t = "rabbit"
 * s的子序列中有3个等于"rabbit"：
 * rabb-b-it (选择第4个b)
 * ra-b-bbit (选择第3个b)  
 * rab-b-bit (选择第5个b)
 * 返回3
 * 
 * s = "babgbag", t = "bag"
 * s的子序列中有5个等于"bag"：
 * ba-g-b-a-g, b-a-gbag, ba-gb-a-g, b-a-gb-ag, babg-bag
 * 返回5
 * 
 * LeetCode链接：https://leetcode-cn.com/problems/21dk04/
 * 
 * 解决方案：
 * 1. 方法1：递归法 - 时间复杂度O(2^min(m,n))
 * 2. 方法2：动态规划法 - 时间复杂度O(m*n)
 * 3. 方法3：空间优化DP - 时间复杂度O(m*n)，空间O(n)
 * 
 * 核心思想：
 * 使用动态规划，dp[i][j]表示s[0...i-1]的子序列中等于t[0...j-1]的个数
 * 
 * 算法复杂度：
 * 时间复杂度：O(m*n)
 * 空间复杂度：O(n) (优化后)
 */
public class DistinctSubseq {
    
    /**
     * 递归处理函数（方法1使用）
     * 
     * 算法思路：
     * 考虑s[i-1]和t[j-1]的关系：
     * 1. 如果j == 0，说明t已经匹配完成，返回1（找到一个匹配）
     * 2. 如果i == 0，说明s已经用完但t还没匹配完，返回0
     * 3. 如果s[i-1] == t[j-1]，有两种选择：
     *    - 用s[i-1]匹配t[j-1]：process(s, t, i-1, j-1)
     *    - 不用s[i-1]匹配：process(s, t, i-1, j)
     * 4. 如果s[i-1] != t[j-1]，只能不用s[i-1]：process(s, t, i-1, j)
     * 
     * @param s 源字符串字符数组
     * @param t 目标字符串字符数组
     * @param i s的当前长度
     * @param j t的当前长度
     * @return s[0...i-1]的子序列中等于t[0...j-1]的个数
     */
    private static int process(char[] s, char[] t, int i, int j) {
        if (j == 0) {
            // 目标串已经匹配完成，找到一个有效子序列
            return 1;
        }
        if (i == 0) {
            // 源串已经用完，但目标串还没匹配完，无法匹配
            return 0;
        }
        
        // 不使用当前字符s[i-1]的情况
        int res = process(s, t, i - 1, j);
        
        // 如果当前字符匹配，可以选择使用它
        if (s[i - 1] == t[j - 1]) {
            res += process(s, t, i - 1, j - 1);
        }
        
        return res;
    }

    /**
     * 方法1：递归法
     * 
     * 算法特点：
     * - 思路直观，易于理解
     * - 时间复杂度较高，存在大量重复计算
     * - 适用于小规模数据的理解验证
     * 
     * 时间复杂度：O(2^min(m,n))
     * 空间复杂度：O(min(m,n))，递归调用栈深度
     * 
     * @param str 源字符串
     * @param target 目标字符串
     * @return 子序列匹配的数量
     */
    public static int numDistinct1(String str, String target) {
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        return process(s, t, s.length, t.length);
    }

    /**
     * 方法2：动态规划法
     * 
     * 算法思路：
     * 使用二维DP表，dp[i][j]表示s[0...i-1]的子序列中等于t[0...j-1]的个数
     * 
     * 状态转移方程：
     * - 如果j == 0：dp[i][0] = 1 (空串总是匹配)
     * - 如果i == 0且j > 0：dp[0][j] = 0 (空串无法匹配非空串)
     * - 如果s[i-1] == t[j-1]：dp[i][j] = dp[i-1][j] + dp[i-1][j-1]
     * - 如果s[i-1] != t[j-1]：dp[i][j] = dp[i-1][j]
     * 
     * 关键理解：
     * - dp[i-1][j]表示不使用s[i-1]的匹配数
     * - dp[i-1][j-1]表示使用s[i-1]匹配t[j-1]的匹配数
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     * 
     * @param str 源字符串
     * @param target 目标字符串
     * @return 子序列匹配的数量
     */
    public static int numDistinct2(String str, String target) {
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        
        // dp[i][j]表示s[0...i-1]的子序列中等于t[0...j-1]的个数
        int[][] dp = new int[s.length + 1][t.length + 1];
        
        // 边界条件：目标串为空串的情况
        for (int i = 0; i <= s.length; i++) {
            dp[i][0] = 1;  // 空串总是可以匹配（选择空子序列）
        }
        
        // 边界条件：源串为空串的情况
        for (int j = 1; j <= t.length; j++) {
            dp[0][j] = 0;  // 空串无法匹配非空串
        }
        
        // 填表过程
        for (int i = 1; i <= s.length; i++) {
            for (int j = 1; j <= t.length; j++) {
                // 不使用当前字符s[i-1]
                dp[i][j] = dp[i - 1][j];
                
                // 如果字符匹配，可以选择使用s[i-1]匹配t[j-1]
                if (s[i - 1] == t[j - 1]) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        
        return dp[s.length][t.length];
    }

    /**
     * 方法3：空间优化的动态规划法
     * 
     * 算法思路：
     * 观察状态转移方程，发现dp[i][j]只依赖于dp[i-1][j]和dp[i-1][j-1]
     * 因此可以使用一维数组进行优化，从右到左更新避免数据覆盖
     * 
     * 优化技巧：
     * - 使用一维数组dp[j]表示当前行的状态
     * - 从右向左更新，保证使用的是上一行的数据
     * - 这样可以将空间复杂度从O(m*n)降低到O(n)
     * 
     * 关键注意：
     * - 必须从右向左更新，因为dp[j-1]需要使用上一行的值
     * - 这是经典的空间优化DP技巧
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(n)
     * 
     * @param str 源字符串
     * @param target 目标字符串
     * @return 子序列匹配的数量
     */
    public static int numDistinct3(String str, String target) {
        char[] s = str.toCharArray();
        char[] t = target.toCharArray();
        
        // dp[j]表示当前s前缀的子序列中等于t[0...j-1]的个数
        int[] dp = new int[t.length + 1];
        dp[0] = 1;  // 空串总是可以匹配
        
        // 逐个处理s中的字符
        for (int i = 1; i <= s.length; i++) {
            // 必须从右向左更新，避免使用已更新的当前行数据
            for (int j = t.length; j >= 1; j--) {
                // 如果字符匹配，累加使用s[i-1]匹配t[j-1]的方案数
                if (s[i - 1] == t[j - 1]) {
                    dp[j] += dp[j - 1];
                }
                // 不匹配的情况下，dp[j]保持不变（即不使用s[i-1]的方案数）
            }
        }
        
        return dp[t.length];
    }

    /**
     * 测试方法：验证三种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 不同子序列数量问题测试 ===");
        
        // 测试用例1
        String s1 = "rabbbit", t1 = "rabbit";
        System.out.println("源串: \"" + s1 + "\", 目标串: \"" + t1 + "\"");
        System.out.println("方法1: " + numDistinct1(s1, t1));
        System.out.println("方法2: " + numDistinct2(s1, t1));
        System.out.println("方法3: " + numDistinct3(s1, t1));
        System.out.println("期望: 3");
        System.out.println();
        
        // 测试用例2
        String s2 = "babgbag", t2 = "bag";
        System.out.println("源串: \"" + s2 + "\", 目标串: \"" + t2 + "\"");
        System.out.println("方法1: " + numDistinct1(s2, t2));
        System.out.println("方法2: " + numDistinct2(s2, t2));
        System.out.println("方法3: " + numDistinct3(s2, t2));
        System.out.println("期望: 5");
        System.out.println();
        
        // 测试用例3：目标串为空
        String s3 = "abc", t3 = "";
        System.out.println("源串: \"" + s3 + "\", 目标串: \"" + t3 + "\"");
        System.out.println("方法2: " + numDistinct2(s3, t3));
        System.out.println("方法3: " + numDistinct3(s3, t3));
        System.out.println("期望: 1 (空子序列匹配空串)");
        System.out.println();
        
        // 测试用例4：无法匹配的情况
        String s4 = "abc", t4 = "def";
        System.out.println("源串: \"" + s4 + "\", 目标串: \"" + t4 + "\"");
        System.out.println("方法2: " + numDistinct2(s4, t4));
        System.out.println("方法3: " + numDistinct3(s4, t4));
        System.out.println("期望: 0 (无法匹配)");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
