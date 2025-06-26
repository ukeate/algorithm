package basic.c45;

/**
 * 最长回文子序列问题
 * 
 * 问题描述：
 * 给定一个字符串，求其最长回文子序列的长度
 * 子序列是指在不改变字符相对顺序的情况下，删除某些字符后得到的序列
 * 
 * 例如：
 * "A1BC2D33FG2H1I" 的最长回文子序列可能是 "1BC2D3D2CB1" 或其他
 * 
 * 解法思路：
 * 方法1（LCS转换）：
 * - 将原字符串反转
 * - 求原字符串与反转字符串的最长公共子序列（LCS）
 * - LCS的长度就是最长回文子序列的长度
 * 
 * 方法2（直接DP）：
 * - 使用区间动态规划
 * - dp[i][j] 表示字符串从位置i到位置j的最长回文子序列长度
 * - 状态转移：如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2
 *            否则dp[i][j] = max(dp[i+1][j], dp[i][j-1])
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 * 
 * @author 算法学习
 */
public class PalindromeSubsequence {
    
    /**
     * 反转字符数组
     * 
     * @param str 原字符数组
     * @return 反转后的字符数组
     */
    private static char[] reverse(char[] str) {
        char[] ans = new char[str.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = str[str.length - 1 - i];
        }
        return ans;
    }

    /**
     * 计算两个字符数组的最长公共子序列（LCS）长度
     * 
     * @param str1 第一个字符数组
     * @param str2 第二个字符数组
     * @return 最长公共子序列的长度
     * 
     * 算法思路：
     * 使用动态规划，dp[i][j]表示str1[0...i]和str2[0...j]的LCS长度
     * 状态转移：
     * - 如果str1[i] == str2[j]，则dp[i][j] = dp[i-1][j-1] + 1
     * - 否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     */
    private static int lcs(char[] str1, char[] str2) {
        int[][] dp = new int[str1.length][str2.length];
        
        // 初始化第一行第一列
        dp[0][0] = str1[0] == str2[0] ? 1 : 0;
        
        // 初始化第一列
        for (int i = 1; i < str1.length; i++) {
            dp[i][0] = Math.max(dp[i - 1][0], str1[i] == str2[0] ? 1 : 0);
        }
        
        // 初始化第一行  
        for (int j = 1; j < str2.length; j++) {
            dp[0][j] = Math.max(dp[0][j - 1], str1[0] == str2[j] ? 1 : 0);
        }
        
        // 填充DP表
        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                // 不匹配时，取左边或上边的最大值
                dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                // 如果字符匹配，可以选择对角线+1
                if (str1[i] == str2[j]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - 1] + 1);
                }
            }
        }
        return dp[str1.length - 1][str2.length - 1];
    }

    /**
     * 方法1：通过LCS求最长回文子序列
     * 
     * @param str 输入字符串
     * @return 最长回文子序列的长度
     * 
     * 算法思路：
     * 1. 将字符串反转
     * 2. 求原字符串与反转字符串的最长公共子序列
     * 3. 这个公共子序列就是最长回文子序列
     * 
     * 原理：回文序列从左读和从右读是一样的，所以原字符串与其反转的公共子序列
     * 必然是回文的，且这个公共子序列是最长的回文子序列
     */
    public static int max1(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        char[] str1 = str.toCharArray();
        char[] str2 = reverse(str1);  // 反转字符串
        return lcs(str1, str2);       // 求LCS
    }

    /**
     * 方法2：直接动态规划求最长回文子序列
     * 
     * @param s 输入字符串
     * @return 最长回文子序列的长度
     * 
     * 算法思路：
     * 使用区间DP，dp[i][j]表示字符串从位置i到位置j的最长回文子序列长度
     * 
     * 状态转移方程：
     * - 如果str[i] == str[j]，则dp[i][j] = dp[i+1][j-1] + 2
     * - 否则dp[i][j] = max(dp[i+1][j], dp[i][j-1])
     * 
     * 边界条件：
     * - dp[i][i] = 1（单个字符的回文长度是1）
     * - dp[i][i+1] = str[i] == str[i+1] ? 2 : 1（两个字符的情况）
     */
    public static int max2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int[][] dp = new int[str.length][str.length];
        
        // 单个字符的回文长度是1
        for (int i = 0; i < str.length; i++) {
            dp[i][i] = 1;
        }
        
        // 两个字符的情况
        for (int i = 0; i < str.length - 1; i++) {
            dp[i][i + 1] = str[i] == str[i + 1] ? 2 : 1;
        }
        
        // 区间DP：从长度3开始，逐步扩大区间
        for (int i = str.length - 2; i >= 0; i--) {
            for (int j = i + 2; j < str.length; j++) {
                // 不包含边界字符的情况
                dp[i][j] = Math.max(dp[i][j - 1], dp[i + 1][j]);
                
                // 如果边界字符相等，可以组成更长的回文
                if (str[i] == str[j]) {
                    dp[i][j] = Math.max(dp[i + 1][j - 1] + 2, dp[i][j]);
                }
            }
        }
        
        return dp[0][str.length - 1];
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String s = "A1BC2D33FG2H1I";
        System.out.println("测试字符串: " + s);
        System.out.println("方法1（LCS转换）结果: " + max1(s));
        System.out.println("方法2（直接DP）结果: " + max2(s));
        
        // 更多测试用例
        System.out.println("\n=== 更多测试用例 ===");
        String[] testCases = {
            "a",           // 1
            "aa",          // 2
            "ab",          // 1
            "aba",         // 3
            "abcba",       // 5
            "abcde",       // 1
            "racecar",     // 7
            "abccba",      // 6
            "bbbab"        // 4
        };
        
        for (String test : testCases) {
            System.out.printf("字符串 \"%s\" -> 方法1: %d, 方法2: %d%n", 
                            test, max1(test), max2(test));
        }
        
        // 性能测试
        System.out.println("\n=== 性能对比 ===");
        String longStr = "abcdefghijklmnopqrstuvwxyz";
        
        long start = System.currentTimeMillis();
        int result1 = max1(longStr);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = max2(longStr);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.printf("长字符串测试（长度%d）：%n", longStr.length());
        System.out.printf("方法1: 结果=%d, 耗时=%dms%n", result1, time1);
        System.out.printf("方法2: 结果=%d, 耗时=%dms%n", result2, time2);
    }
}
