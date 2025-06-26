package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 132. 分割回文串 II (Palindrome Partitioning II)
 * 
 * 问题描述：
 * 给你一个字符串 s，请你将 s 分割成一些子串，使得每个子串都是回文串。
 * 返回符合要求的最少分割次数。
 * 
 * 示例：
 * 输入：s = "aab"
 * 输出：1
 * 解释：进行一次分割就可将 s 分割成 ["aa","b"] 这样两个回文子串。
 * 
 * 解法思路：
 * 动态规划 + 回文预处理：
 * 1. 预处理阶段：
 *    - 使用DP计算所有子串的回文性：checkMap[i][j]
 * 2. 主DP阶段：
 *    - dp[i] = 将s[i...]分割成回文串的最少分割次数
 *    - 状态转移：dp[i] = min(dp[j+1] + 1) for all j where s[i...j]是回文
 * 
 * 三种功能实现：
 * 1. minCut(): 返回最少分割次数
 * 2. oneWay(): 返回一种最优分割方案  
 * 3. allWays(): 返回所有最优分割方案
 * 
 * DP状态转移详解：
 * - dp[n] = 0（空串不需要分割）
 * - 如果s[i...n-1]整体是回文，dp[i] = 1
 * - 否则枚举所有可能的第一段回文s[i...j]，dp[i] = min(dp[j+1] + 1)
 * 
 * 优化技巧：
 * - 预计算回文性避免重复判断
 * - 从后向前填充DP表
 * - 特殊处理整段是回文的情况
 * 
 * 时间复杂度：O(n²) - 预处理O(n²) + 主DP O(n²)
 * 空间复杂度：O(n²) - 回文预处理表
 * 
 * LeetCode链接：https://leetcode.com/problems/palindrome-partitioning-ii/
 */
public class P132_PalindromePartitioningII {

    /**
     * 预处理：计算所有子串的回文性
     * 
     * @param str 字符数组
     * @param n 字符串长度
     * @return 回文判断的二维数组
     */
    private static boolean[][] checkMap(char[] str, int n) {
        boolean[][] ans = new boolean[n][n];
        
        // 处理长度为1和2的子串
        for (int i = 0; i < n - 1; i++) {
            ans[i][i] = true;                        // 单字符必为回文
            ans[i][i + 1] = str[i] == str[i + 1];   // 两字符回文判断
        }
        ans[n - 1][n - 1] = true;  // 最后一个字符
        
        // 处理长度>=3的子串
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                // 回文判断：首尾相同 且 去掉首尾后仍是回文
                ans[i][j] = str[i] == str[j] && ans[i + 1][j - 1];
            }
        }
        
        return ans;
    }

    /**
     * 返回最少分割次数
     * 
     * @param s 输入字符串
     * @return 最少分割次数
     */
    public static int minCut(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        boolean[][] checkMap = checkMap(str, n);
        
        // dp[i] = 将s[i...]分割成回文串的最少分割次数
        int[] dp = new int[n + 1];
        dp[n] = 0;  // 空串分割次数为0
        
        // 从后往前填充DP表
        for (int i = n - 1; i >= 0; i--) {
            if (checkMap[i][n - 1]) {
                // 整个后缀s[i...n-1]都是回文，只需1段
                dp[i] = 1;
            } else {
                int next = Integer.MAX_VALUE;
                
                // 枚举第一段回文串的结束位置
                for (int j = i; j < n; j++) {
                    if (checkMap[i][j]) {  // s[i...j]是回文
                        next = Math.min(next, dp[j + 1]);
                    }
                }
                
                dp[i] = 1 + next;  // 第一段回文 + 剩余部分的最优分割
            }
        }
        
        return dp[0] - 1;  // 分割次数 = 段数 - 1
    }

    /**
     * 返回一种最优分割方案
     * 
     * @param s 输入字符串
     * @return 一种最优分割方案
     */
    public static List<String> oneWay(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() < 2) {
            ans.add(s);
        } else {
            char[] str = s.toCharArray();
            int n = str.length;
            boolean[][] checkMap = checkMap(str, n);
            
            // 计算DP数组
            int[] dp = new int[n + 1];
            dp[n] = 0;
            for (int i = n - 1; i >= 0; i--) {
                if (checkMap[i][n - 1]) {
                    dp[i] = 1;
                } else {
                    int next = Integer.MAX_VALUE;
                    for (int j = i; j < n; j++) {
                        if (checkMap[i][j]) {
                            next = Math.min(next, dp[j + 1]);
                        }
                    }
                    dp[i] = 1 + next;
                }
            }
            
            // 根据DP数组重构分割方案
            for (int i = 0, j = 1; j <= n; j++) {
                if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
                    ans.add(s.substring(i, j));
                    i = j;
                }
            }
        }
        return ans;
    }

    /**
     * 复制字符串列表
     * 
     * @param list 原列表
     * @return 深拷贝的列表
     */
    private static List<String> copy(List<String> list) {
        List<String> ans = new ArrayList<>();
        for (String str : list) {
            ans.add(str);
        }
        return ans;
    }

    /**
     * 递归生成所有最优分割方案
     * 
     * @param s 原字符串
     * @param i 当前起始位置
     * @param j 当前结束位置
     * @param checkMap 回文判断表
     * @param dp DP数组
     * @param path 当前路径
     * @param ans 结果集合
     */
    private static void process(String s, int i, int j, boolean[][] checkMap, int[] dp, 
                              List<String> path, List<List<String>> ans) {
        if (j == s.length()) {
            // 到达字符串末尾
            if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
                path.add(s.substring(i, j));
                ans.add(copy(path));
                path.remove(path.size() - 1);
            }
        } else {
            // 尝试在位置j分割
            if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
                path.add(s.substring(i, j));
                process(s, j, j + 1, checkMap, dp, path, ans);
                path.remove(path.size() - 1);
            }
            
            // 尝试不在位置j分割，继续扩展
            process(s, i, j + 1, checkMap, dp, path, ans);
        }
    }

    /**
     * 返回所有最优分割方案
     * 
     * @param s 输入字符串
     * @return 所有最优分割方案
     */
    public static List<List<String>> allWays(String s) {
        List<List<String>> ans = new ArrayList<>();
        if (s == null || s.length() < 2) {
            List<String> cur = new ArrayList<>();
            cur.add(s);
            ans.add(cur);
        } else {
            char[] str = s.toCharArray();
            int n = str.length;
            boolean[][] checkMap = checkMap(str, n);
            
            // 计算DP数组
            int[] dp = new int[n + 1];
            dp[n] = 0;
            for (int i = n - 1; i >= 0; i--) {
                if (checkMap[i][n - 1]) {
                    dp[i] = 1;
                } else {
                    int next = Integer.MAX_VALUE;
                    for (int j = i; j < n; j++) {
                        if (checkMap[i][j]) {
                            next = Math.min(next, dp[j + 1]);
                        }
                    }
                    dp[i] = 1 + next;
                }
            }
            
            // 生成所有最优方案
            process(s, 0, 1, checkMap, dp, new ArrayList<>(), ans);
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String s = "cbbbcbc";
        
        // 测试一种方案
        List<String> ans2 = oneWay(s);
        for (String str : ans2) {
            System.out.print(str + " ");
        }
        System.out.println();

        // 测试所有方案
        List<List<String>> ans3 = allWays(s);
        for (List<String> way : ans3) {
            for (String str : way) {
                System.out.print(str + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
