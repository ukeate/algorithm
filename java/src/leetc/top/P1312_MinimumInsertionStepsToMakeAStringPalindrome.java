package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 1312. 让字符串成为回文串的最少插入次数 (Minimum Insertion Steps to Make a String Palindrome)
 * 
 * 问题描述：
 * 给你一个字符串 s ，每一次操作你都可以在字符串的任意位置插入任意字符。
 * 请你返回让 s 成为回文串的最少操作次数。
 * 
 * 解法思路：
 * 区间动态规划：
 * 1. 状态定义：dp[i][j] = 让s[i...j]成为回文串的最少插入次数
 * 2. 状态转移：
 *    - 如果s[i] == s[j]：dp[i][j] = dp[i+1][j-1]（两端字符匹配）
 *    - 如果s[i] != s[j]：dp[i][j] = min(dp[i][j-1], dp[i+1][j]) + 1
 *      a) 在右端插入s[i]：dp[i+1][j] + 1
 *      b) 在左端插入s[j]：dp[i][j-1] + 1
 * 3. 边界条件：dp[i][i+1] = s[i] == s[i+1] ? 0 : 1
 * 
 * 三种实现功能：
 * 1. minInsertions(): 返回最少插入次数
 * 2. oneWay(): 返回一种具体的回文构造方案
 * 3. allWays(): 返回所有可能的回文构造方案
 * 
 * 路径重构原理：
 * - 根据DP转移来源，确定插入位置和插入字符
 * - 双指针从两端向中间构造回文串
 * 
 * 时间复杂度：O(n²) - 填充DP表
 * 空间复杂度：O(n²) - DP表空间
 * 
 * LeetCode链接：https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/
 */
public class P1312_MinimumInsertionStepsToMakeAStringPalindrome {
    
    /**
     * 计算让字符串成为回文串的最少插入次数
     * 
     * @param s 输入字符串
     * @return 最少插入次数
     */
    public static int minInsertions(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        
        // 初始化：长度为2的子串
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
        }
        
        // 填充DP表：从长度3开始，从下往上，从左往右
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                // 两种插入策略的最小值
                dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
                
                // 如果两端字符相同，可以不插入
                if (str[i] == str[j]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
        }
        
        return dp[0][n - 1];
    }

    /**
     * 返回一种具体的回文构造方案
     * 
     * @param s 输入字符串
     * @return 构造的回文串
     */
    public static String oneWay(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        
        // 填充DP表（同minInsertions）
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
        }
        
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
                if (str[i] == str[j]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
        }

        // 根据DP表重构回文串
        int l = 0, r = n - 1;
        char[] ans = new char[n + dp[l][r]];  // 原长度 + 插入次数
        int ansl = 0, ansr = ans.length - 1;
        
        while (l < r) {
            if (dp[l][r - 1] == dp[l][r] - 1) {
                // 在左端插入s[r]
                ans[ansl++] = str[r];
                ans[ansr--] = str[r--];
            } else if (dp[l + 1][r] == dp[l][r] - 1) {
                // 在右端插入s[l]
                ans[ansl++] = str[l];
                ans[ansr--] = str[l++];
            } else {
                // 两端字符匹配，直接使用
                ans[ansl++] = str[l++];
                ans[ansr--] = str[r--];
            }
        }
        
        // 处理中间字符（奇数长度情况）
        if (l == r) {
            ans[ansl] = str[l];
        }
        
        return String.valueOf(ans);
    }

    /**
     * 递归生成所有可能的回文构造方案
     * 
     * @param str 字符数组
     * @param dp DP表
     * @param l 左边界
     * @param r 右边界
     * @param path 当前构造的回文串
     * @param pl 回文串左指针
     * @param pr 回文串右指针
     * @param ans 结果集合
     */
    public static void process(char[] str, int[][] dp, int l, int r, char[] path, int pl, int pr, List<String> ans) {
        if (l >= r) {
            // 处理中间字符
            if (l == r) {
                path[pl] = str[l];
            }
            ans.add(String.valueOf(path));
        } else {
            // 尝试在左端插入s[r]
            if (dp[l][r - 1] == dp[l][r] - 1) {
                path[pl] = str[r];
                path[pr] = str[r];
                process(str, dp, l, r - 1, path, pl + 1, pr - 1, ans);
            }
            
            // 尝试在右端插入s[l]
            if (dp[l + 1][r] == dp[l][r] - 1) {
                path[pl] = str[l];
                path[pr] = str[l];
                process(str, dp, l + 1, r, path, pl + 1, pr - 1, ans);
            }
            
            // 如果两端字符匹配
            if (str[l] == str[r] && (l + 1 == r || dp[l + 1][r - 1] == dp[l][r])) {
                path[pl] = str[l];
                path[pr] = str[r];
                process(str, dp, l + 1, r - 1, path, pl + 1, pr - 1, ans);
            }
        }
    }

    /**
     * 返回所有可能的回文构造方案
     * 
     * @param s 输入字符串
     * @return 所有可能的回文串
     */
    public static List<String> allWays(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() < 2) {
            ans.add(s);
            return ans;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        
        // 填充DP表
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
        }
        
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
                if (str[i] == str[j]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
        }
        
        // 生成所有方案
        int m = n + dp[0][n - 1];
        char[] path = new char[m];
        process(str, dp, 0, n - 1, path, 0, m - 1, ans);
        
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String s = "mbadm";
        
        // 测试一种方案
        String ans2 = oneWay(s);
        System.out.println(ans2);
        
        // 测试所有方案
        List<String> ans3 = allWays(s);
        for (String way : ans3) {
            System.out.println(way);
        }
        System.out.println();
    }
}
