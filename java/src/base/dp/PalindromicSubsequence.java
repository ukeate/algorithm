package base.dp;

/**
 * 最长回文子序列问题
 * 
 * 问题描述：
 * 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
 * 子序列是指在不改变字符相对顺序的情况下，删除某些字符后得到的序列。
 * 
 * 例如：s = "bbbab"，最长回文子序列是 "bbbb"，长度为 4
 * 
 * 解法分析：
 * 1. 区间DP：dp[l][r]表示字符串s[l...r]范围内的最长回文子序列长度
 * 2. 转化为LCS：字符串与其反转串的最长公共子序列就是最长回文子序列
 * 3. 状态转移：
 *    - 如果s[l] == s[r]：dp[l][r] = dp[l+1][r-1] + 2
 *    - 否则：dp[l][r] = max(dp[l+1][r], dp[l][r-1])
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 */
// https://leetcode.com/problems/longest-palindromic-subsequence/
public class PalindromicSubsequence {
    
    /**
     * 暴力递归解法
     * 
     * @param str 字符数组
     * @param l 左边界
     * @param r 右边界
     * @return s[l...r]范围内的最长回文子序列长度
     */
    private static int f1(char[] str, int l, int r) {
        // base case 1：只有一个字符，回文长度为1
        if (l == r) {
            return 1;
        }
        
        // base case 2：两个字符
        if (l == r - 1) {
            return str[l] == str[r] ? 2 : 1;
        }
        
        // 四种可能的选择：
        // 1. 既不包含l位置也不包含r位置的字符
        int p1 = f1(str, l + 1, r - 1);
        
        // 2. 不包含r位置字符
        int p2 = f1(str, l, r - 1);
        
        // 3. 不包含l位置字符
        int p3 = f1(str, l + 1, r);
        
        // 4. 如果l和r位置字符相等，可以同时包含两个字符
        int p4 = str[l] != str[r] ? 0 : (2 + f1(str, l + 1, r - 1));
        
        return Math.max(Math.max(p1, p2), Math.max(p3, p4));
    }

    /**
     * 暴力递归解法入口
     * 
     * @param s 输入字符串
     * @return 最长回文子序列长度
     */
    public static int lps1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        return f1(str, 0, str.length - 1);
    }

    /**
     * 动态规划解法（区间DP）
     * 
     * dp[l][r]表示字符串s[l...r]范围内的最长回文子序列长度
     * 
     * @param s 输入字符串
     * @return 最长回文子序列长度
     */
    public static int lps2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        
        // 初始化：单个字符的回文长度为1
        dp[n - 1][n - 1] = 1;
        for (int i = 0; i < n - 1; i++) {
            dp[i][i] = 1;
            // 初始化长度为2的子串
            dp[i][i + 1] = str[i] == str[i + 1] ? 2 : 1;
        }
        
        // 按照区间长度从3开始填充DP表
        for (int l = n - 3; l >= 0; l--) {
            for (int r = l + 2; r < n; r++) {
                // 考虑不包含边界字符的情况
                dp[l][r] = Math.max(dp[l][r - 1], dp[l + 1][r]);
                
                // 如果边界字符相等，考虑同时包含两个边界字符
                if (str[l] == str[r]) {
                    dp[l][r] = Math.max(dp[l][r], 2 + dp[l + 1][r - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }

    /**
     * 最长公共子序列算法
     * 用于计算字符串与其反转串的LCS
     * 
     * @param str1 第一个字符数组
     * @param str2 第二个字符数组
     * @return 最长公共子序列长度
     */
    private static int lcs(char[] str1, char[] str2) {
        int n = str1.length;
        int m = str2.length;
        int[][] dp = new int[n][m];
        
        // 初始化第一个字符的匹配情况
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        
        // 初始化第一行
        for (int i = 1; i < n; i++) {
            dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
        }
        
        // 初始化第一列
        for (int j = 1; j < m; j++) {
            dp[0][j] = str1[0] == str2[j] ? 1 : dp[0][j - 1];
        }
        
        // 填充DP表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // 不匹配当前字符的情况
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                
                // 如果当前字符匹配，可以选择匹配
                if (str1[i] == str2[j]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
            }
        }
        
        return dp[n - 1][m - 1];
    }

    /**
     * 反转字符数组
     * 
     * @param str 原字符数组
     * @return 反转后的字符数组
     */
    private static char[] reverse(char[] str) {
        int n = str.length;
        char[] reverse = new char[str.length];
        for (int i = 0; i < str.length; i++) {
            reverse[--n] = str[i];
        }
        return reverse;
    }

    /**
     * 转化为LCS问题的解法
     * 
     * 关键思路：
     * 字符串的最长回文子序列等于该字符串与其反转串的最长公共子序列。
     * 
     * @param s 输入字符串
     * @return 最长回文子序列长度
     */
    public static int lps3(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }
        
        char[] str = s.toCharArray();
        char[] reverse = reverse(str);
        return lcs(str, reverse);
    }
}
