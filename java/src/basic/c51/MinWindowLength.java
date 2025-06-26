package basic.c51;

/**
 * 最小窗口子串问题
 * 
 * 问题描述：
 * 给定一个字符串s1和一个字符串s2，找到s1中包含s2所有字符的最小窗口子串的长度。
 * 
 * 示例：
 * s1 = "ADOBECODEBANC", s2 = "ABC"
 * 输出: 4
 * 解释: 最小窗口子串 "BANC" 包含了s2中的所有字符 'A','B','C'
 * 
 * 算法思路：
 * 使用滑动窗口技术：
 * 1. 用数组记录s2中每个字符的需求数量
 * 2. 用双指针维护一个滑动窗口[left, right]
 * 3. 右指针扩展窗口，直到包含s2的所有字符
 * 4. 左指针收缩窗口，直到不再包含所有字符
 * 5. 重复3-4步骤，记录最小窗口长度
 * 
 * 时间复杂度：O(|s1| + |s2|)
 * 空间复杂度：O(1) - 字符集大小固定为256
 * 
 * LeetCode: https://leetcode.com/problems/minimum-window-substring/
 * 
 * @author 算法学习
 */
public class MinWindowLength {
    
    /**
     * 找到s1中包含s2所有字符的最小窗口长度
     * 
     * @param s1 源字符串
     * @param s2 目标字符串（包含所需字符）
     * @return 最小窗口长度，如果不存在返回0
     */
    public static int min(String s1, String s2) {
        // 边界条件检查
        if (s1 == null || s2 == null || s1.length() < s2.length()) {
            return Integer.MAX_VALUE;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 统计s2中每个字符的需求数量
        int[] map = new int[256];  // ASCII字符集
        for (int i = 0; i < str2.length; i++) {
            map[str2[i]]++;
        }
        
        int left = 0;                          // 窗口左边界
        int right = 0;                         // 窗口右边界
        int all = str2.length;                 // 还需要匹配的字符总数
        int min = Integer.MAX_VALUE;           // 最小窗口长度
        
        // 滑动窗口算法
        while (right < str1.length) {
            // 右指针扩展窗口
            map[str1[right]]--;
            
            // 如果当前字符是需要的字符，减少待匹配数量
            if (map[str1[right]] >= 0) {
                all--;
            }
            
            // 当窗口包含了s2的所有字符
            if (all == 0) {
                // 左指针收缩窗口，移除多余字符
                while (map[str1[left]] < 0) {
                    map[str1[left++]]++;
                }
                
                // 更新最小窗口长度
                min = Math.min(min, right - left + 1);
                
                // 移动左边界，准备寻找下一个可能的窗口
                all++;
                map[str1[left++]]++;
            }
            
            right++;
        }
        
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    /**
     * 测试方法
     * 验证最小窗口算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最小窗口子串测试 ===");
        
        // 基本测试用例
        String s1 = "adabbca";
        String s2 = "acb";
        int result = min(s1, s2);
        System.out.println("s1 = \"" + s1 + "\"");
        System.out.println("s2 = \"" + s2 + "\"");
        System.out.println("最小窗口长度: " + result);
        System.out.println("最小窗口应该是: \"abbc\" (长度4)");
        
        System.out.println("\n=== 更多测试用例 ===");
        
        // 测试用例1：经典例子
        testCase("ADOBECODEBANC", "ABC", 4);
        
        // 测试用例2：无解情况
        testCase("a", "aa", 0);
        
        // 测试用例3：完全匹配
        testCase("abc", "abc", 3);
        
        // 测试用例4：重复字符
        testCase("aaab", "aab", 4);
        
        // 测试用例5：目标字符串为空
        testCase("abc", "", 0);
        
        // 测试用例6：源字符串更短
        testCase("a", "abc", Integer.MAX_VALUE);
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(|s1| + |s2|)");
        System.out.println("空间复杂度: O(1) - 字符集大小固定");
        System.out.println("核心思想: 滑动窗口 + 双指针技术");
    }
    
    /**
     * 测试用例辅助方法
     * 
     * @param s1 源字符串
     * @param s2 目标字符串
     * @param expected 期望结果
     */
    private static void testCase(String s1, String s2, int expected) {
        int result = min(s1, s2);
        String status = (result == expected) ? "✓" : "✗";
        System.out.printf("s1=\"%s\", s2=\"%s\" => 结果:%d, 期望:%d %s%n", 
                s1, s2, result, expected, status);
    }
}
