package basic.c51;

/**
 * 扰乱字符串问题
 * 
 * 问题描述：
 * 使用下面描述的算法可以扰乱字符串s得到字符串t：
 * 1. 如果字符串的长度为1，算法停止
 * 2. 如果字符串的长度>1，执行下述步骤：
 *    - 在任意索引处将字符串分割成两个非空的子字符串
 *    - 选择是否交换两个子字符串然后将它们连接起来
 *    - 在两个子字符串上递归执行步骤1
 * 
 * 给定两个长度相等的字符串s1和s2，判断s2是否是s1的扰乱字符串。
 * 
 * 算法思路：
 * 1. 递归解法：枚举所有可能的分割点和交换方式
 * 2. 动态规划：记忆化搜索，避免重复计算
 * 3. 剪枝优化：先检查字符频次是否相同
 * 
 * 时间复杂度：O(n^4)
 * 空间复杂度：O(n^3)
 * 
 * LeetCode: https://leetcode.com/problems/scramble-string/
 * 
 * @author 算法学习
 */
public class ScrambleString {
    
    /**
     * 检查两个字符串的字符频次是否相同
     * 
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 字符频次是否相同
     */
    private static boolean sameCount(char[] str1, char[] str2) {
        if (str1.length != str2.length) {
            return false;
        }
        
        // 统计字符频次
        int[] map = new int[256];
        for (int i = 0; i < str1.length; i++) {
            map[str1[i]]++;
        }
        
        // 检查str2的字符是否都在str1中
        for (int i = 0; i < str2.length; i++) {
            if (--map[str2[i]] < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 递归判断str1能否通过扰乱变成str2
     * 
     * @param str1 源字符串
     * @param str2 目标字符串
     * @param l1 str1的起始位置
     * @param l2 str2的起始位置
     * @param size 要比较的长度
     * @return 是否可以通过扰乱得到
     */
    private static boolean process(char[] str1, char[] str2, int l1, int l2, int size) {
        // 基础情况：只有一个字符时直接比较
        if (size == 1) {
            return str1[l1] == str2[l2];
        }
        
        // 枚举所有可能的分割点
        for (int leftPart = 1; leftPart < size; leftPart++) {
            // 情况1：不交换左右部分
            // str1的左半部分对应str2的左半部分，右半部分对应右半部分
            boolean case1 = process(str1, str2, l1, l2, leftPart) && 
                           process(str1, str2, l1 + leftPart, l2 + leftPart, size - leftPart);
            
            // 情况2：交换左右部分
            // str1的左半部分对应str2的右半部分，右半部分对应左半部分
            boolean case2 = process(str1, str2, l1, l2 + size - leftPart, leftPart) && 
                           process(str1, str2, l1 + leftPart, l2, size - leftPart);
            
            // 只要有一种情况成立即可
            if (case1 || case2) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归解法：判断s1是否可以扰乱得到s2
     * 
     * @param s1 源字符串
     * @param s2 目标字符串
     * @return 是否可以扰乱得到
     */
    public static boolean can1(String s1, String s2) {
        // 处理null情况
        if (s1 == null ^ s2 == null) {
            return false;
        }
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1.equals(s2)) {
            return true;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 剪枝：字符频次不同直接返回false
        if (!sameCount(str1, str2)) {
            return false;
        }
        
        int n = s1.length();
        return process(str1, str2, 0, 0, n);
    }

    /**
     * 动态规划解法：自底向上填表
     * 
     * @param s1 源字符串
     * @param s2 目标字符串
     * @return 是否可以扰乱得到
     */
    public static boolean can2(String s1, String s2) {
        // 处理null情况
        if (s1 == null ^ s2 == null) {
            return false;
        }
        if (s1 == null && s2 == null) {
            return true;
        }
        if (s1.equals(s2)) {
            return true;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 剪枝：字符频次不同直接返回false
        if (!sameCount(str1, str2)) {
            return false;
        }
        
        int n = s1.length();
        // dp[l1][l2][size] = str1从l1开始长度为size的子串能否扰乱得到str2从l2开始长度为size的子串
        boolean[][][] dp = new boolean[n][n][n + 1];
        
        // 初始化：长度为1的情况
        for (int l1 = 0; l1 < n; l1++) {
            for (int l2 = 0; l2 < n; l2++) {
                dp[l1][l2][1] = str1[l1] == str2[l2];
            }
        }
        
        // 填表：从长度2开始
        for (int size = 2; size <= n; size++) {
            for (int l1 = 0; l1 <= n - size; l1++) {
                for (int l2 = 0; l2 <= n - size; l2++) {
                    // 枚举分割点
                    for (int leftPart = 1; leftPart < size; leftPart++) {
                        // 情况1：不交换
                        boolean case1 = dp[l1][l2][leftPart] && 
                                       dp[l1 + leftPart][l2 + leftPart][size - leftPart];
                        
                        // 情况2：交换
                        boolean case2 = dp[l1][l2 + size - leftPart][leftPart] && 
                                       dp[l1 + leftPart][l2][size - leftPart];
                        
                        if (case1 || case2) {
                            dp[l1][l2][size] = true;
                            break;
                        }
                    }
                }
            }
        }
        
        return dp[0][0][n];
    }

    /**
     * 测试方法
     * 验证扰乱字符串算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 扰乱字符串测试 ===");
        
        // 基本测试用例
        String s1 = "abcd";
        String s2 = "cdab";
        System.out.println("s1 = \"" + s1 + "\", s2 = \"" + s2 + "\"");
        System.out.println("递归解法: " + can1(s1, s2));
        System.out.println("动态规划: " + can2(s1, s2));
        
        s2 = "cadb";
        System.out.println("\ns1 = \"" + s1 + "\", s2 = \"" + s2 + "\"");
        System.out.println("递归解法: " + can1(s1, s2));
        System.out.println("动态规划: " + can2(s1, s2));
        
        // 复杂测试用例
        System.out.println("\n=== 复杂测试用例 ===");
        s1 = "bcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcdebcde";
        s2 = "ebcdeebcdebebcdebcdebcdecdebcbcdcdebcddebcbdebbbcdcdebcdeebcdebcdeebcddeebccdebcdbcdebcd";
        
        System.out.println("长字符串测试:");
        System.out.println("s1长度: " + s1.length());
        System.out.println("s2长度: " + s2.length());
        
        long start = System.currentTimeMillis();
        boolean result = can2(s1, s2);
        long end = System.currentTimeMillis();
        
        System.out.println("动态规划结果: " + result);
        System.out.println("耗时: " + (end - start) + "ms");
        
        // 更多测试用例
        System.out.println("\n=== 更多测试用例 ===");
        testCase("great", "rgeat", true);
        testCase("abcdef", "fecabd", true);
        testCase("hwareg", "grhwae", false);
        testCase("a", "a", true);
        testCase("ab", "ba", true);
        testCase("abc", "bca", true);
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n^4)");
        System.out.println("空间复杂度: O(n^3)");
        System.out.println("核心思想: 递归分治 + 动态规划优化");
    }
    
    /**
     * 测试用例辅助方法
     * 
     * @param s1 源字符串
     * @param s2 目标字符串
     * @param expected 期望结果
     */
    private static void testCase(String s1, String s2, boolean expected) {
        boolean result1 = can1(s1, s2);
        boolean result2 = can2(s1, s2);
        String status = (result1 == expected && result2 == expected) ? "✓" : "✗";
        System.out.printf("s1=\"%s\", s2=\"%s\" => 递归:%b, DP:%b, 期望:%b %s%n", 
                s1, s2, result1, result2, expected, status);
    }
}
