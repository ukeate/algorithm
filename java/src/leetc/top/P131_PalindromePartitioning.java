package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * LeetCode 131. 分割回文串 (Palindrome Partitioning)
 * 
 * 问题描述：
 * 给你一个字符串 s，请你将 s 分割成一些子串，使得每个子串都是回文串。
 * 返回 s 所有可能的分割方案。
 * 回文串是正着读和反着读都一样的字符串。
 * 
 * 示例：
 * 输入：s = "aab"
 * 输出：[["a","a","b"],["aa","b"]]
 * 
 * 解法思路：
 * 动态规划预处理 + 回溯搜索：
 * 1. 预处理阶段：
 *    - 使用动态规划计算所有子串的回文性
 *    - dp[i][j] = true 表示s[i...j]是回文串
 * 2. 回溯搜索：
 *    - 从位置0开始，尝试所有可能的分割点
 *    - 如果当前子串是回文，加入路径并继续搜索
 *    - 到达字符串末尾时，记录一个有效分割方案
 * 
 * 动态规划状态转移：
 * - dp[i][i] = true（单字符总是回文）
 * - dp[i][i+1] = (s[i] == s[i+1])（两字符回文判断）
 * - dp[i][j] = (s[i] == s[j]) && dp[i+1][j-1]（多字符回文判断）
 * 
 * 回溯搜索策略：
 * - 枚举每个可能的结束位置
 * - 检查当前子串是否为回文
 * - 递归搜索剩余部分
 * - 回溯时移除当前选择
 * 
 * 时间复杂度：O(n² + 2^n) - 预处理O(n²)，搜索最坏情况O(2^n)
 * 空间复杂度：O(n²) - DP表空间 + 递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/palindrome-partitioning/
 */
public class P131_PalindromePartitioning {
    
    /**
     * 预处理：使用动态规划计算所有子串的回文性
     * 
     * @param str 字符数组
     * @return 回文判断的DP表
     */
    private static boolean[][] dp(char[] str) {
        int n = str.length;
        boolean[][] dp = new boolean[n][n];
        
        // 处理长度为1和2的子串
        for (int i = 0; i < n - 1; i++) {
            dp[i][i] = true;                        // 单字符必为回文
            dp[i][i + 1] = str[i] == str[i + 1];   // 两字符回文判断
        }
        dp[n - 1][n - 1] = true;  // 最后一个字符
        
        // 处理长度>=3的子串（按对角线填充）
        for (int j = 2; j < n; j++) {  // j表示子串长度-1
            int row = 0;
            int col = j;
            while (row < n && col < n) {
                // 回文判断：首尾字符相同 且 去掉首尾后的子串也是回文
                dp[row][col] = str[row] == str[col] && dp[row + 1][col - 1];
                row++;
                col++;
            }
        }
        
        return dp;
    }

    /**
     * 复制路径列表（深拷贝）
     * 
     * @param path 当前路径
     * @return 路径的深拷贝
     */
    private static List<String> copy(List<String> path) {
        List<String> ans = new ArrayList<>();
        for (String p : path) {
            ans.add(p);
        }
        return ans;
    }

    /**
     * 回溯搜索所有可能的回文分割方案
     * 
     * @param s 原字符串
     * @param idx 当前搜索位置
     * @param path 当前分割路径
     * @param dp 回文判断DP表
     * @param ans 结果集合
     */
    private static void process(String s, int idx, LinkedList<String> path, boolean[][] dp, List<List<String>> ans) {
        // 搜索到字符串末尾，找到一个有效分割方案
        if (idx == s.length()) {
            ans.add(copy(path));
            return;
        }
        
        // 枚举所有可能的结束位置
        for (int end = idx; end < s.length(); end++) {
            if (dp[idx][end]) {  // 当前子串是回文
                // 加入当前子串到路径
                path.addLast(s.substring(idx, end + 1));
                
                // 递归搜索剩余部分
                process(s, end + 1, path, dp, ans);
                
                // 回溯：移除当前选择
                path.pollLast();
            }
        }
    }

    /**
     * 分割回文串主方法
     * 
     * @param s 输入字符串
     * @return 所有可能的回文分割方案
     */
    public static List<List<String>> partition(String s) {
        // 预处理：计算所有子串的回文性
        boolean[][] dp = dp(s.toCharArray());
        
        // 回溯搜索所有分割方案
        LinkedList<String> path = new LinkedList<>();
        List<List<String>> ans = new ArrayList<>();
        process(s, 0, path, dp, ans);
        
        return ans;
    }
}
