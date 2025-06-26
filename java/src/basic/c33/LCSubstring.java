package basic.c33;

/**
 * 最长公共子串问题
 * 
 * 问题描述：
 * 给定两个字符串str1和str2，找到它们的最长公共子串的长度。
 * 子串是连续的字符序列。
 * 
 * 例如：
 * str1 = "1AB2345CD", str2 = "12345EF"
 * 最长公共子串是"2345"，长度为4
 * 
 * 算法思路：
 * 使用动态规划解决，定义dp[i][j]表示以str1[i-1]和str2[j-1]结尾的公共子串长度
 * 
 * 状态转移方程：
 * - 如果str1[i-1] == str2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
 * - 否则dp[i][j] = 0（因为是子串，必须连续）
 * 
 * 最终答案是dp数组中的最大值
 * 
 * 时间复杂度：O(M*N)，其中M和N分别是两个字符串的长度
 * 空间复杂度：O(M*N)，可以优化到O(min(M,N))
 */
public class LCSubstring {
    
    /**
     * 计算最长公共子串的长度
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 最长公共子串的长度
     */
    public static int longestCommonSubstring(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0) {
            return 0;
        }
        
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int m = s1.length;
        int n = s2.length;
        
        // dp[i][j]表示以s1[i-1]和s2[j-1]结尾的公共子串长度
        int[][] dp = new int[m + 1][n + 1];
        int maxLength = 0; // 记录最长公共子串的长度
        
        // 填充dp表
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    // 字符相等，当前公共子串长度 = 前一个位置的长度 + 1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    maxLength = Math.max(maxLength, dp[i][j]);
                } else {
                    // 字符不相等，当前位置的公共子串长度为0
                    dp[i][j] = 0;
                }
            }
        }
        
        return maxLength;
    }
    
    /**
     * 空间优化版本：使用滚动数组
     * 因为dp[i][j]只依赖于dp[i-1][j-1]，所以可以使用一维数组
     * 
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 最长公共子串的长度
     */
    public static int longestCommonSubstringOptimized(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0) {
            return 0;
        }
        
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int m = s1.length;
        int n = s2.length;
        
        // 确保s1是较短的字符串，节省空间
        if (m > n) {
            char[] temp = s1;
            s1 = s2;
            s2 = temp;
            int tempLen = m;
            m = n;
            n = tempLen;
        }
        
        int[] dp = new int[m + 1];
        int maxLength = 0;
        
        for (int i = 1; i <= n; i++) {
            int prev = 0; // 相当于dp[i-1][j-1]
            for (int j = 1; j <= m; j++) {
                int temp = dp[j]; // 保存当前值，用于下一次循环的prev
                if (s2[i - 1] == s1[j - 1]) {
                    dp[j] = prev + 1;
                    maxLength = Math.max(maxLength, dp[j]);
                } else {
                    dp[j] = 0;
                }
                prev = temp;
            }
        }
        
        return maxLength;
    }
    
    /**
     * 获取最长公共子串本身（不仅仅是长度）
     * @param str1 第一个字符串
     * @param str2 第二个字符串
     * @return 最长公共子串
     */
    public static String getLongestCommonSubstring(String str1, String str2) {
        if (str1 == null || str2 == null || str1.length() == 0 || str2.length() == 0) {
            return "";
        }
        
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        int m = s1.length;
        int n = s2.length;
        
        int[][] dp = new int[m + 1][n + 1];
        int maxLength = 0;
        int endIndex = 0; // 记录最长公共子串在str1中的结束位置
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        endIndex = i; // 更新结束位置
                    }
                } else {
                    dp[i][j] = 0;
                }
            }
        }
        
        // 从结束位置往前推出最长公共子串
        return str1.substring(endIndex - maxLength, endIndex);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String str1 = "1AB2345CD";
        String str2 = "12345EF";
        
        System.out.println("字符串1: " + str1);
        System.out.println("字符串2: " + str2);
        System.out.println("最长公共子串长度: " + longestCommonSubstring(str1, str2));
        System.out.println("最长公共子串长度（优化版）: " + longestCommonSubstringOptimized(str1, str2));
        System.out.println("最长公共子串: " + getLongestCommonSubstring(str1, str2));
        
        // 测试其他情况
        System.out.println("\n其他测试:");
        System.out.println("空字符串测试: " + longestCommonSubstring("", "abc"));
        System.out.println("无公共子串测试: " + longestCommonSubstring("abc", "def"));
        System.out.println("完全相同测试: " + longestCommonSubstring("hello", "hello"));
    }
}
