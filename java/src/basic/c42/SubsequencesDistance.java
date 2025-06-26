package basic.c42;

/**
 * 子序列匹配数量问题（Distinct Subsequences）
 * 
 * 问题描述：
 * 给定两个字符串s和t，计算s的子序列中等于t的数量
 * 子序列是通过删除一些（也可以不删除）字符而不改变剩余字符顺序得到的
 * 不同的删除方式得到相同子序列算作不同的匹配方法
 * 
 * 例如：s = "rabbbit", t = "rabbit"
 * s中包含3个"rabbit"子序列：rabb[b]it, rab[b]bit, ra[b]bbit
 * 
 * 算法思路：
 * 方法1：递归暴力搜索 - O(2^n)
 * 方法2：二维DP - O(m*n)时间，O(m*n)空间
 * 方法3：一维DP优化 - O(m*n)时间，O(n)空间
 * 
 * 状态定义：dp[i][j] = s的前i个字符中有多少个子序列等于t的前j个字符
 * 
 * @author 算法学习
 */
public class SubsequencesDistance {
    
    /**
     * 方法1：递归暴力解法
     * 
     * @param s 源字符串字符数组
     * @param t 目标字符串字符数组
     * @param i s中考虑的字符数量
     * @param j t中考虑的字符数量
     * @return s的前i个字符中有多少个子序列等于t的前j个字符
     * 
     * 递归关系：
     * 1. 如果j=0，说明t为空串，任何字符串都有1个空子序列
     * 2. 如果i=0但j>0，说明s为空但t非空，无法匹配，返回0
     * 3. 如果s[i-1] = t[j-1]，可以选择匹配或不匹配
     * 4. 如果s[i-1] ≠ t[j-1]，只能跳过s[i-1]
     */
    private static int process(char[] s, char[] t, int i, int j) {
        // 递归边界：t为空串，有1种匹配方式（空子序列）
        if (j == 0) {
            return 1;
        }
        // 递归边界：s为空但t非空，无法匹配
        if (i == 0) {
            return 0;
        }
        
        // 选择1：不使用s[i-1]字符，在s的前i-1个字符中匹配
        int res = process(s, t, i - 1, j);
        
        // 选择2：如果s[i-1] = t[j-1]，可以使用s[i-1]来匹配t[j-1]
        if (s[i - 1] == t[j - 1]) {
            res += process(s, t, i - 1, j - 1);
        }
        
        return res;
    }

    /**
     * 方法1的入口函数
     * 
     * @param s 源字符串
     * @param t 目标字符串
     * @return 匹配的子序列数量
     */
    public static int ways1(String s, String t) {
        char[] ss = s.toCharArray();
        char[] tt = t.toCharArray();
        return process(ss, tt, s.length(), t.length());
    }

    /**
     * 方法2：二维DP优化
     * 
     * @param ss 源字符串
     * @param tt 目标字符串
     * @return 匹配的子序列数量
     * 
     * 状态转移方程：
     * dp[i][j] = dp[i-1][j] + (s[i-1] == t[j-1] ? dp[i-1][j-1] : 0)
     * 
     * 边界条件：
     * dp[i][0] = 1 (空串t可以被任何字符串匹配1次)
     * dp[0][j] = 0 (j > 0时，空串s无法匹配非空t)
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public static int ways2(String ss, String tt) {
        char[] s = ss.toCharArray();
        char[] t = tt.toCharArray();
        
        // dp[i][j] = s的前i个字符中匹配t的前j个字符的方案数
        int[][] dp = new int[s.length + 1][t.length + 1];
        
        // 边界初始化：空串t可以被任何字符串匹配0次（应该是1次）
        for (int j = 0; j <= t.length; j++) {
            dp[0][j] = j == 0 ? 1 : 0;  // 修正：只有j=0时才是1
        }
        
        // 边界初始化：任何字符串都包含1个空子序列
        for (int i = 0; i <= s.length; i++) {
            dp[i][0] = 1;
        }
        
        // 状态转移
        for (int i = 1; i <= s.length; i++) {
            for (int j = 1; j <= t.length; j++) {
                // 不使用s[i-1]的方案数
                dp[i][j] = dp[i - 1][j];
                
                // 如果字符匹配，可以使用s[i-1]匹配t[j-1]
                if (s[i - 1] == t[j - 1]) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        
        return dp[s.length][t.length];
    }

    /**
     * 方法3：一维DP空间优化
     * 将二维DP压缩为一维DP，节省空间
     * 
     * @param ss 源字符串
     * @param tt 目标字符串
     * @return 匹配的子序列数量
     * 
     * 优化思路：
     * 观察到dp[i][j]只依赖于dp[i-1][j]和dp[i-1][j-1]
     * 可以用一维数组滚动更新，从右往左更新避免覆盖
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(n)
     */
    public static int ways3(String ss, String tt) {
        char[] s = ss.toCharArray();
        char[] t = tt.toCharArray();
        
        // 一维DP数组，dp[j]表示匹配t的前j个字符的方案数
        int[] dp = new int[t.length + 1];
        dp[0] = 1; // 空串可以匹配1次
        
        // 逐个考虑s中的字符
        for (int i = 1; i <= s.length; i++) {
            // 从右往左更新，避免覆盖还需要使用的值
            for (int j = t.length; j >= 1; j--) {
                // 如果字符匹配，累加使用当前字符的方案数
                if (s[i - 1] == t[j - 1]) {
                    dp[j] += dp[j - 1];
                }
                // 注意：dp[j] += 0的情况不需要显式处理，保持原值即可
            }
        }
        
        return dp[t.length];
    }
    
    /**
     * 测试方法：验证三种算法的正确性
     */
    public static void main(String[] args) {
        String s = "rabbbit";
        String t = "rabbit";
        
        System.out.println("测试字符串: s=\"" + s + "\", t=\"" + t + "\"");
        System.out.println("递归方法结果: " + ways1(s, t));
        System.out.println("二维DP结果: " + ways2(s, t));
        System.out.println("一维DP结果: " + ways3(s, t));
        
        // 测试边界情况
        System.out.println("\n边界测试:");
        System.out.println("空串匹配: " + ways2("", "")); // 期望: 1
        System.out.println("非空匹配空: " + ways2("abc", "")); // 期望: 1
        System.out.println("空匹配非空: " + ways2("", "a")); // 期望: 0
    }
}
