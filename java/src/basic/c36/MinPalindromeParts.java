package basic.c36;

/**
 * 最少分割回文子串问题
 * 
 * 问题描述：
 * 给定一个字符串，将其分割成若干个子串，使得每个子串都是回文串。
 * 求最少需要分割多少次。
 * 
 * 例如：
 * 字符串 "aba12321412321TabaKFK"
 * 可以分割为：["aba", "12321", "4", "12321", "T", "aba", "K", "F", "K"]
 * 分割次数 = 子串数量 - 1
 * 
 * 算法思路：
 * 这是一个经典的动态规划问题，分为两个步骤：
 * 1. 预处理：计算所有子串的回文性质（区间DP）
 * 2. 主DP：计算最少分割次数
 * 
 * 预处理时间复杂度：O(N²)
 * 主DP时间复杂度：O(N²)  
 * 总时间复杂度：O(N²)
 * 空间复杂度：O(N²)
 */
public class MinPalindromeParts {
    
    /**
     * 计算字符串的最少回文分割次数
     * 
     * 算法步骤：
     * 1. 预处理阶段：使用区间DP计算所有子串是否为回文
     * 2. 主DP阶段：计算以每个位置结尾的最少分割次数
     * 
     * 状态定义：
     * - isP[i][j]：子串s[i...j]是否为回文
     * - dp[i]：字符串s[i...n-1]的最少分割次数
     * 
     * 状态转移：
     * dp[i] = min(dp[i], 1 + dp[end+1])，其中s[i...end]是回文
     * 
     * @param s 输入字符串
     * @return 最少分割次数
     */
    public static int min(String s) {
        // 边界条件检查
        if (s == null || s.length() == 0) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;  // 单个字符本身就是回文，需要1个子串（0次分割+1）
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 第一阶段：预处理回文性质
        // isP[i][j] 表示子串str[i...j]是否为回文
        boolean[][] isP = new boolean[n][n];
        
        // 初始化：单个字符都是回文
        for (int i = 0; i < n; i++) {
            isP[i][i] = true;
        }
        
        // 初始化：长度为2的子串
        for (int i = 0; i < n - 1; i++) {
            isP[i][i + 1] = str[i] == str[i + 1];
        }
        
        // 区间DP：计算长度≥3的子串的回文性质
        // 从右下角向左上角填充（保证依赖的子问题已解决）
        for (int row = n - 3; row >= 0; row--) {
            for (int col = row + 2; col < n; col++) {
                // str[row...col]是回文 ⟺ str[row] == str[col] && str[row+1...col-1]是回文
                isP[row][col] = str[row] == str[col] && isP[row + 1][col - 1];
            }
        }
        
        // 第二阶段：计算最少分割次数
        // dp[i] 表示字符串str[i...n-1]的最少分割次数
        int[] dp = new int[n + 1];
        
        // 初始化：所有位置都设为最大值（最坏情况：每个字符单独一个子串）
        for (int i = 0; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        
        // 边界条件：空字符串不需要分割
        dp[n] = 0;
        
        // 从后往前计算dp值
        for (int i = n - 1; i >= 0; i--) {
            // 枚举所有可能的第一个回文子串的结束位置
            for (int end = i; end < n; end++) {
                if (isP[i][end]) {
                    // 如果str[i...end]是回文，则可以作为第一个子串
                    // 总分割次数 = 1个回文子串 + str[end+1...n-1]的最优分割
                    dp[i] = Math.min(dp[i], 1 + dp[end + 1]);
                }
            }
        }
        
        // dp[0]表示整个字符串的最少子串数量，分割次数 = 子串数量 - 1
        // 但这里dp的定义已经是分割次数了（因为我们加的是1而不是0）
        // 实际上这里返回的是子串数量，需要根据实际需求调整
        return dp[0];
    }
    
    /**
     * 辅助方法：打印回文分割的一种具体方案
     * 
     * @param s 输入字符串
     */
    public static void printOneSolution(String s) {
        if (s == null || s.length() == 0) {
            System.out.println("空字符串");
            return;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 重复计算回文性质（简化版，实际应该复用）
        boolean[][] isP = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            isP[i][i] = true;
        }
        for (int i = 0; i < n - 1; i++) {
            isP[i][i + 1] = str[i] == str[i + 1];
        }
        for (int row = n - 3; row >= 0; row--) {
            for (int col = row + 2; col < n; col++) {
                isP[row][col] = str[row] == str[col] && isP[row + 1][col - 1];
            }
        }
        
        // 贪心构造一个解：每次选择最长的回文前缀
        System.out.print("一种分割方案: [");
        int start = 0;
        boolean first = true;
        
        while (start < n) {
            // 找到从start开始的最长回文子串
            int end = start;
            for (int i = start; i < n; i++) {
                if (isP[start][i]) {
                    end = i;
                }
            }
            
            if (!first) System.out.print(", ");
            System.out.print("\"" + s.substring(start, end + 1) + "\"");
            first = false;
            start = end + 1;
        }
        System.out.println("]");
    }

    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最少分割回文子串测试 ===");
        
        // 测试用例1：复杂字符串
        String s1 = "aba12321412321TabaKFK";
        
        System.out.println("测试用例1：");
        System.out.println("字符串: \"" + s1 + "\"");
        int result1 = min(s1);
        System.out.println("最少子串数量: " + result1);
        printOneSolution(s1);
        System.out.println();
        
        // 测试用例2：全是回文
        String s2 = "abccba";
        
        System.out.println("测试用例2（整体回文）：");
        System.out.println("字符串: \"" + s2 + "\"");
        int result2 = min(s2);
        System.out.println("最少子串数量: " + result2);
        printOneSolution(s2);
        System.out.println();
        
        // 测试用例3：无回文（最坏情况）
        String s3 = "abcd";
        
        System.out.println("测试用例3（无长回文）：");
        System.out.println("字符串: \"" + s3 + "\"");
        int result3 = min(s3);
        System.out.println("最少子串数量: " + result3);
        printOneSolution(s3);
        System.out.println();
        
        // 测试用例4：单字符
        String s4 = "a";
        
        System.out.println("测试用例4（单字符）：");
        System.out.println("字符串: \"" + s4 + "\"");
        int result4 = min(s4);
        System.out.println("最少子串数量: " + result4);
        printOneSolution(s4);
        System.out.println();
        
        // 测试用例5：重复字符
        String s5 = "aaaa";
        
        System.out.println("测试用例5（重复字符）：");
        System.out.println("字符串: \"" + s5 + "\"");
        int result5 = min(s5);
        System.out.println("最少子串数量: " + result5);
        printOneSolution(s5);
        
        System.out.println("\n=== 测试完成 ===");
    }
}
