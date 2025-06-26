package leetc.top;

/**
 * LeetCode 10. 正则表达式匹配 (Regular Expression Matching)
 * 
 * 问题描述：
 * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
 * '.' 匹配任意单个字符
 * '*' 匹配零个或多个前面的那一个元素
 * 所谓匹配，是要涵盖整个字符串 s 的，而不是部分字符串。
 * 
 * 解法思路：
 * 使用动态规划算法：
 * 1. dp[i][j] 表示字符串s的前i个字符是否能被模式p的前j个字符匹配
 * 2. 状态转移：
 *    - 如果p[j-1] != '*': dp[i][j] = (s[i-1] == p[j-1] || p[j-1] == '.') && dp[i-1][j-1]
 *    - 如果p[j-1] == '*': 可以匹配0次或多次前面的字符
 *      a) 匹配0次：dp[i][j] = dp[i][j-2]
 *      b) 匹配多次：dp[i][j] = (s[i-1] == p[j-2] || p[j-2] == '.') && dp[i-1][j]
 * 3. 边界条件：dp[0][0] = true（空字符串匹配空模式）
 * 
 * 核心优化：
 * - 从右下角开始填充DP表，避免重复计算
 * - 预处理验证输入合法性
 * - 优化边界初始化
 * 
 * 时间复杂度：O(m*n) - m为字符串长度，n为模式长度
 * 空间复杂度：O(m*n) - DP表空间
 * 
 * LeetCode链接：https://leetcode.com/problems/regular-expression-matching/
 */
public class P10_RegularExpressionMatching {
    
    /**
     * 验证输入字符串和模式的合法性
     * 
     * @param s 输入字符串数组
     * @param e 模式数组
     * @return 是否合法
     */
    private static boolean isValid(char[] s, char[] e) {
        // 字符串中不能包含通配符
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '*' || s[i] == '.') {
                return false;
            }
        }
        
        // 模式中'*'不能出现在开头，且不能连续出现
        for (int i = 0; i < e.length; i++) {
            if (e[i] == '*' && (i == 0 || e[i - 1] == '*')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 初始化动态规划表，处理边界条件
     * 
     * @param s 字符串数组
     * @param e 模式数组
     * @return 初始化的DP表
     */
    private static boolean[][] initDp(char[] s, char[] e) {
        int sl = s.length;
        int el = e.length;
        boolean[][] dp = new boolean[sl + 1][el + 1];
        
        // 空字符串匹配空模式
        dp[sl][el] = true;
        
        // 处理字符串为空，模式为"a*b*c*"这种情况
        for (int j = el - 2; j >= 0; j = j - 2) {
            if (e[j] != '*' && e[j + 1] == '*') {
                dp[sl][j] = true;
            } else {
                break;
            }
        }
        
        // 处理最后一个字符的直接匹配情况
        if (sl > 0 && el > 0) {
            if ((e[el - 1] == '.' || s[sl - 1] == e[el - 1])) {
                dp[sl - 1][el - 1] = true;
            }
        }
        return dp;
    }

    /**
     * 正则表达式匹配主方法
     * 
     * @param str 输入字符串
     * @param exp 正则表达式模式
     * @return 是否匹配
     */
    public static boolean isMatch(String str, String exp) {
        if (str == null || exp == null) {
            return false;
        }
        
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        
        // 验证输入合法性
        if (!isValid(s, e)) {
            return false;
        }
        
        // 初始化DP表
        boolean[][] dp = initDp(s, e);
        
        // 从右下角开始填充DP表
        for (int i = s.length - 1; i >= 0; i--) {
            for (int j = e.length - 2; j >= 0; j--) {
                if (e[j + 1] != '*') {
                    // 普通字符匹配：当前字符相等且后续能匹配
                    dp[i][j] = (s[i] == e[j] || e[j] == '.') && dp[i + 1][j + 1];
                } else {
                    // '*'匹配：可以匹配0次或多次
                    int si = i;
                    
                    // 尝试匹配多次：只要当前字符匹配，就继续尝试
                    for (; si < s.length && (s[si] == e[j] || e[j] == '.'); si++) {
                        if (dp[si][j + 2]) { // 如果后续子问题已解决
                            dp[i][j] = true;
                            break;
                        }
                    }
                    
                    // 如果多次匹配没有成功，尝试匹配0次
                    if (dp[i][j] != true) {
                        dp[i][j] = dp[si][j + 2];
                    }
                }
            }
        }
        
        return dp[0][0];
    }
}
