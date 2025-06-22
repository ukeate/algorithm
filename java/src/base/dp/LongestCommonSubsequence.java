package base.dp;

/**
 * 最长公共子序列问题 (Longest Common Subsequence, LCS)
 * 
 * 问题描述：
 * 给定两个字符串str1和str2，返回这两个字符串的最长公共子序列的长度。
 * 子序列是指在不改变字符相对顺序的情况下，删除某些字符后得到的序列。
 * 
 * 例如：str1 = "abcde", str2 = "ace"
 *      最长公共子序列是"ace"，长度为3
 * 
 * 解法分析：
 * 1. 状态定义：dp[i][j]表示str1[0...i]和str2[0...j]的最长公共子序列长度
 * 2. 状态转移：
 *    - 如果str1[i] == str2[j]：dp[i][j] = dp[i-1][j-1] + 1
 *    - 否则：dp[i][j] = max(dp[i-1][j], dp[i][j-1])
 * 3. 边界条件：dp[0][j]和dp[i][0]需要特殊处理
 * 
 * 这是动态规划的经典问题，广泛应用于：
 * - 文本相似度比较
 * - 版本控制中的差异分析
 * - 生物信息学中的序列比对
 * 
 * 时间复杂度：O(m*n)，其中m和n分别是两个字符串的长度
 * 空间复杂度：O(m*n)
 */
// https://leetcode.com/problems/longest-common-subsequence/
// 最长公共子序列
public class LongestCommonSubsequence {
    
    /**
     * 暴力递归解法
     * 
     * @param str1 第一个字符串的字符数组
     * @param str2 第二个字符串的字符数组
     * @param i str1的当前位置
     * @param j str2的当前位置
     * @return str1[0...i]和str2[0...j]的最长公共子序列长度
     */
    private static int process1(char[] str1, char[] str2, int i, int j) {
        // base case 1：两个字符串都只有一个字符
        if (i == 0 && j == 0) {
            return str1[i] == str2[j] ? 1 : 0;
        } else if (i == 0) {
            // base case 2：str1只有一个字符
            if (str1[i] == str2[j]) {
                return 1;
            } else {
                return process1(str1, str2, i, j - 1);
            }
        } else if (j == 0) {
            // base case 3：str2只有一个字符
            if (str1[i] == str2[j]) {
                return 1;
            } else {
                return process1(str1, str2, i - 1, j);
            }
        } else {
            // 一般情况：考虑三种可能性
            // 1. LCS不包含str1[i]
            int p1 = process1(str1, str2, i - 1, j);
            // 2. LCS不包含str2[j]
            int p2 = process1(str1, str2, i, j - 1);
            // 3. 如果str1[i] == str2[j]，LCS可能同时包含str1[i]和str2[j]
            int p3 = str1[i] == str2[j] ? (1 + process1(str1, str2, i - 1, j - 1)) : 0;
            
            return Math.max(p1, Math.max(p2, p3));
        }
    }

    /**
     * 暴力递归解法入口
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共子序列的长度
     */
    public static int lcs1(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        return process1(str1, str2, str1.length - 1, str2.length - 1);
    }

    /**
     * 动态规划解法
     * 
     * dp[i][j]表示str1[0...i]和str2[0...j]的最长公共子序列长度
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共子序列的长度
     */
    public static int lcs2(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return 0;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int n = str1.length;
        int m = str2.length;
        
        // dp[i][j]表示str1[0...i]和str2[0...j]的LCS长度
        int[][] dp = new int[n][m];
        
        // 初始化边界条件
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        
        // 初始化第一行：str1只有第一个字符时的情况
        for (int j = 1; j < m; j++) {
            dp[0][j] = str1[0] == str2[j] ? 1 : dp[0][j - 1];
        }
        
        // 初始化第一列：str2只有第一个字符时的情况
        for (int i = 1; i < n; i++) {
            dp[i][0] = str1[i] == str2[0] ? 1 : dp[i - 1][0];
        }
        
        // 填充dp表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // 贪心策略：先考虑不包含当前字符的情况
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                
                // 如果当前字符相等，考虑同时包含两个字符的情况
                if (str1[i] == str2[j]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
            }
        }
        
        return dp[n - 1][m - 1];
    }

    /**
     * 构建DP表的辅助函数
     * 
     * @param str1 第一个字符串的字符数组
     * @param str2 第二个字符串的字符数组
     * @return DP表
     */
    private static int[][] getDp(char[] str1, char[] str2) {
        int[][] dp = new int[str1.length][str2.length];
        
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        
        for (int i = 1; i < str1.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], str1[i] == str2[0] ? 1 : 0);
        }
        
        for (int j = 1; j < str2.length; j++) {
            dp[0][j] = Math.max(dp[0][j - 1], str1[0] == str2[j] ? 1 : 0);
        }
        
        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                if (str1[i] == str2[j]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
            }
        }
        
        return dp;
    }

    /**
     * 根据DP表还原LCS路径（具体的公共子序列）
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共子序列的具体内容
     */
    public static String lcsPath(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return "";
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int[][] dp = getDp(str1, str2);
        
        int m = str1.length - 1;
        int n = str2.length - 1;
        char[] rst = new char[dp[m][n]];
        int idx = rst.length - 1;
        
        // 从右下角开始回溯，重构LCS
        while (idx >= 0) {
            if (n > 0 && dp[m][n] == dp[m][n - 1]) {
                // LCS不包含str2[n]，向左移动
                n--;
            } else if (m > 0 && dp[m][n] == dp[m - 1][n]) {
                // LCS不包含str1[m]，向上移动
                m--;
            } else {
                // str1[m] == str2[n]，这个字符是LCS的一部分
                rst[idx--] = str1[m];
                m--;
                n--;
            }
        }
        
        return String.valueOf(rst);
    }

    public static void main(String[] args) {
        String str1 = "A1BC23Z4";
        String str2 = "1203YU4P";
        System.out.println(lcs1(str1, str2));
        System.out.println(lcs2(str1, str2));
        System.out.println(lcsPath(str1, str2));
    }
}


