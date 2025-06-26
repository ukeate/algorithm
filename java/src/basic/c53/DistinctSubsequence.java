package basic.c53;

import java.util.Arrays;

/**
 * 不同子序列数量问题
 * 
 * 问题描述：
 * 给定一个字符串，计算这个字符串中字面值不同的子序列数量。
 * 注意：这里的子序列是指从原字符串中删除某些字符（可以不删除）得到的序列，
 * 字面值不同是指子序列的字符串内容不同。
 * 
 * 示例：
 * "abc" → 子序列有："", "a", "b", "c", "ab", "ac", "bc", "abc" 共8个
 * "aba" → 由于有重复字符，需要去重计算
 * 
 * 算法思路：
 * 1. 动态规划法：dp[i]表示以第i个字符结尾的不同子序列数量
 * 2. 优化算法：利用字符最后出现位置，避免重复计算
 * 
 * 时间复杂度：O(n^2) / O(n)
 * 空间复杂度：O(n) / O(1)
 * 
 * @author 算法学习
 */
public class DistinctSubsequence {
    
    /**
     * 方法1：动态规划解法
     * 对于每个位置，计算以该位置字符结尾的不同子序列数量
     * 
     * @param s 输入字符串
     * @return 不同子序列的数量
     */
    public static int distinct1(String s) {
        char[] str = s.toCharArray();
        int rst = 0;
        
        // dp[i]表示以第i个字符结尾的不同子序列数量
        int[] dp = new int[str.length];
        Arrays.fill(dp, 1);  // 每个字符本身是一个子序列
        
        for (int i = 0; i < str.length; i++) {
            // 对于位置i，检查前面所有位置
            for (int j = 0; j < i; j++) {
                if (str[j] != str[i]) {
                    // 如果字符不同，可以将str[i]添加到以str[j]结尾的所有子序列后面
                    dp[i] += dp[j];
                }
                // 如果字符相同，不能直接添加，因为会产生重复的子序列
            }
            rst += dp[i];  // 累加到总结果中
        }
        
        return rst;
    }

    /**
     * 方法2：优化算法
     * 使用字符计数数组，避免重复遍历
     * 
     * @param s 输入字符串
     * @return 不同子序列的数量
     */
    public static int distinct2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        // count[i]表示以字符('a'+i)结尾的子序列数量
        int[] count = new int[26];
        
        int rst = 0;  // 不包括空集
        
        for (char x : str) {
            // 当前字符可以：
            // 1. 自己单独成为一个子序列 (+1)
            // 2. 添加到之前所有子序列的后面 (+rst)
            // 3. 减去之前以相同字符结尾的子序列数量，避免重复
            int add = rst + 1 - count[x - 'a'];
            rst += add;
            
            // 更新以当前字符结尾的子序列数量
            count[x - 'a'] += add;
        }
        
        return rst;
    }

    /**
     * 生成随机字符串用于测试
     * 
     * @param maxLen 最大长度
     * @param variable 字符种类数
     * @return 随机字符串
     */
    private static String randomStr(int maxLen, int variable) {
        int size = (int) (maxLen * Math.random()) + 1;
        char[] str = new char[size];
        for (int i = 0; i < size; i++) {
            str[i] = (char) ((int) (variable * Math.random()) + 'a');
        }
        return String.valueOf(str);
    }

    /**
     * 测试方法
     * 验证两种算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 不同子序列数量测试 ===");
        
        // 基本测试用例
        testCase("abc");
        testCase("aba");
        testCase("aaa");
        testCase("abcd");
        testCase("");
        testCase("a");
        
        // 大规模随机测试
        System.out.println("\n=== 随机测试开始 ===");
        int maxLen = 10;
        int variable = 5;
        
        boolean allPassed = true;
        for (int i = 0; i < 1000000; i++) {
            String s = randomStr(maxLen, variable);
            int result1 = distinct1(s);
            int result2 = distinct2(s);
            
            if (result1 != result2) {
                System.out.println("测试失败！字符串: \"" + s + "\"");
                System.out.println("方法1结果: " + result1);
                System.out.println("方法2结果: " + result2);
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("随机测试通过，共测试1000000个用例");
        }
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("方法1 - DP: 时间O(n²), 空间O(n), 直观易懂");
        System.out.println("方法2 - 优化: 时间O(n), 空间O(1), 高效实用");
        System.out.println("核心思想: 动态规划 + 去重策略");
    }
    
    /**
     * 测试用例辅助方法
     * 
     * @param s 测试字符串
     */
    private static void testCase(String s) {
        int result1 = distinct1(s);
        int result2 = distinct2(s);
        String status = (result1 == result2) ? "✓" : "✗";
        System.out.printf("字符串: \"%s\" => 方法1:%d, 方法2:%d %s%n", 
                s, result1, result2, status);
    }
}
