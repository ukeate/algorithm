package leetc.top;

/**
 * LeetCode 44. 通配符匹配 (Wildcard Matching)
 * 
 * 问题描述：
 * 给定一个字符串(s)和一个字符模式(p)，实现一个支持'?'和'*'的通配符匹配。
 * 
 * 字符匹配规则：
 * - '?'：可以匹配任何单个字符
 * - '*'：可以匹配任意字符串（包括空字符串）
 * 
 * 示例：
 * - s = "aa", p = "a" → false（a无法匹配aa）
 * - s = "aa", p = "*" → true（*可以匹配任意字符串）
 * - s = "cb", p = "?a" → false（第二个字符不匹配）
 * - s = "adceb", p = "a*c?b" → true
 * 
 * 解法思路：
 * 1. 递归解法：暴力尝试所有可能的匹配方式
 * 2. 动态规划：优化递归，避免重复计算
 * 
 * 核心难点：
 * - '*'的处理：可以匹配0到多个字符，需要尝试所有可能
 * - 边界条件：字符串结束时的判断逻辑
 * 
 * 时间复杂度：
 * - 递归：O(2^n) - 指数级，会超时
 * - 动态规划：O(m×n) - m和n分别是s和p的长度
 * 
 * 空间复杂度：O(m×n) - 需要二维dp数组
 */
public class P44_WildcardMatching {
    
    /**
     * 递归解法（会超时，仅用于理解思路）
     * 
     * 算法思路：
     * 1. 如果s已遍历完：检查p剩余部分是否全为'*'
     * 2. 如果p已遍历完：检查s是否也遍历完
     * 3. 普通字符：必须严格匹配
     * 4. '?'：匹配任意单个字符
     * 5. '*'：尝试匹配0到n个字符（递归所有可能）
     * 
     * @param s 输入字符串的字符数组
     * @param p 模式字符串的字符数组
     * @param si 当前在s中的位置
     * @param pi 当前在p中的位置
     * @return s[si...]是否匹配p[pi...]
     */
    private static boolean process1(char[] s, char[] p, int si, int pi) {
        // s已遍历完的情况
        if (si == s.length) {
            if (pi == p.length) {
                return true;  // s和p都遍历完，匹配成功
            } else {
                // p还有剩余，只有当前字符是'*'且剩余部分也匹配时才成功
                return p[pi] == '*' && process1(s, p, si, pi + 1);
            }
        }
        
        // p已遍历完但s还有剩余，匹配失败
        if (pi == p.length) {
            return si == s.length;
        }
        
        // 处理普通字符
        if (p[pi] != '?' && p[pi] != '*') {
            return s[si] == p[pi] && process1(s, p, si + 1, pi + 1);
        }
        
        // 处理'?'：匹配当前字符
        if (p[pi] == '?') {
            return process1(s, p, si + 1, pi + 1);
        }
        
        // 处理'*'：尝试匹配0到(s.length-si)个字符
        for (int len = 0; len <= s.length - si; len++) {
            if (process1(s, p, si + len, pi + 1)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 递归解法入口（会超时）
     * 
     * @param s 输入字符串
     * @param p 模式字符串
     * @return 是否匹配
     */
    public boolean isMatch1(String s, String p) {
        char[] str = s.toCharArray();
        char[] pp = p.toCharArray();
        return process1(str, pp, 0, 0);
    }

    /**
     * 动态规划解法（推荐）
     * 
     * 算法思路：
     * dp[i][j]表示s[i...]是否匹配p[j...]
     * 
     * 状态转移方程：
     * 1. 如果p[j]是普通字符或'?'：
     *    dp[i][j] = (s[i]==p[j] || p[j]=='?') && dp[i+1][j+1]
     * 2. 如果p[j]是'*'：
     *    dp[i][j] = dp[i][j+1] || dp[i+1][j]
     *    - dp[i][j+1]：'*'匹配空串
     *    - dp[i+1][j]：'*'匹配s[i]，继续尝试匹配后续字符
     * 
     * 边界条件：
     * - dp[n][m] = true（都到达末尾）
     * - dp[n][j] = p[j]=='*' && dp[n][j+1]（s已完但p还有）
     * 
     * @param str 输入字符串
     * @param pattern 模式字符串
     * @return 是否匹配
     */
    public static boolean isMatch2(String str, String pattern) {
        char[] s = str.toCharArray();
        char[] p = pattern.toCharArray();
        int n = s.length;
        int m = p.length;
        
        // dp[i][j]表示s[i...]是否匹配p[j...]
        boolean[][] dp = new boolean[n + 1][m + 1];
        
        // 边界条件：s和p都到达末尾
        dp[n][m] = true;
        
        // 边界条件：s已完，p还有剩余（只有连续的'*'才可能匹配）
        for (int pi = m - 1; pi >= 0; pi--) {
            dp[n][pi] = p[pi] == '*' && dp[n][pi + 1];
        }
        
        // 填充dp表：从右下角向左上角
        for (int si = n - 1; si >= 0; si--) {
            for (int pi = m - 1; pi >= 0; pi--) {
                if (p[pi] != '*') {
                    // 普通字符或'?'的情况
                    dp[si][pi] = (p[pi] == '?' || s[si] == p[pi]) && dp[si + 1][pi + 1];
                } else {
                    // '*'的情况：匹配空串 或 匹配当前字符继续尝试
                    dp[si][pi] = dp[si][pi + 1] || dp[si + 1][pi];
                }
            }
        }
        
        return dp[0][0];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        String[][] testCases = {
            {"aa", "a"},          // false
            {"aa", "*"},          // true  
            {"cb", "?a"},         // false
            {"adceb", "a*c?b"},   // true
            {"acdcb", "a*c?b"},   // false
            {"", "*"},            // true
            {"", ""},             // true
        };
        
        for (String[] test : testCases) {
            String s = test[0], p = test[1];
            boolean result = isMatch2(s, p);
            System.out.println("s=\"" + s + "\", p=\"" + p + "\" → " + result);
        }
    }
}
