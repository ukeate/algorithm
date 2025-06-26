package basic.c48;

/**
 * 正则表达式匹配问题
 * 
 * 问题描述：
 * 给定一个字符串s和一个字符模式p，实现一个支持'.'和'*'的正则表达式匹配。
 * - '.' 匹配任意单个字符
 * - '*' 匹配零个或多个前面的那一个元素
 * 
 * 示例：
 * s = "aa", p = "a" → false
 * s = "aa", p = "a*" → true  
 * s = "ab", p = ".*" → true
 * s = "aab", p = "c*a*b" → true
 * 
 * 算法思路：
 * 1. 递归解法：根据模式串的当前字符分情况讨论
 * 2. 动态规划：自底向上填表
 * 3. 关键在于正确处理'*'的匹配逻辑
 * 
 * 时间复杂度：O(m*n) 其中m,n分别是字符串和模式的长度
 * 空间复杂度：O(m*n)
 * 
 * LeetCode: https://leetcode.com/problems/regular-expression-matching/
 * 
 * @author 算法学习
 */
public class RegularExpressionMatch {
    
    /**
     * 验证输入的字符串和模式是否合法
     * 
     * @param s 待匹配字符串
     * @param e 正则表达式模式
     * @return 是否合法
     */
    public static boolean isValid(char[] s, char[] e) {
        // 字符串中不能包含*和.
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '*' || s[i] == '.') {
                return false;
            }
        }
        
        // 模式中*不能出现在开头，也不能连续出现
        for (int i = 0; i < e.length; i++) {
            if (e[i] == '*' && (i == 0 || e[i - 1] == '*')) {
                return false;
            }
        }
        return true;
    }

    /**
     * 递归匹配字符串和模式
     * 
     * @param s 待匹配字符串
     * @param e 正则表达式模式
     * @param si 字符串当前位置
     * @param ei 模式当前位置
     * @return 是否匹配
     */
    private static boolean process(char[] s, char[] e, int si, int ei) {
        // 模式匹配完了，字符串也要匹配完才算成功
        if (ei == e.length) {
            return si == s.length;
        }
        
        // 下一个字符不是*的情况
        if (ei == e.length - 1 || e[ei + 1] != '*') {
            // 当前字符必须匹配，然后递归匹配剩余部分
            return si < s.length
                    && (e[ei] == s[si] || e[ei] == '.') // 字符匹配或通配符
                    && process(s, e, si + 1, ei + 1);
        }
        
        // 下一个字符是*的情况：当前字符可以匹配0次或多次
        // 先尝试匹配0次（跳过当前字符）
        if (process(s, e, si, ei + 2)) {
            return true;
        }
        
        // 尝试匹配多次：只要当前字符能匹配，就继续尝试
        while (si < s.length && (e[ei] == s[si] || e[ei] == '.')) {
            si++;
            if (process(s, e, si, ei + 2)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 递归解法入口
     * 
     * @param str 待匹配字符串
     * @param exp 正则表达式
     * @return 是否匹配
     */
    private static boolean match(String str, String exp) {
        if (str == null || exp == null) {
            return false;
        }
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        return isValid(s, e) && process(s, e, 0, 0);
    }

    /**
     * 初始化动态规划表
     * 填充边界条件：最后一列和倒数第二列
     * 
     * @param s 字符串数组
     * @param e 模式数组
     * @return 初始化后的dp表
     */
    private static boolean[][] initDp(char[] s, char[] e) {
        int sl = s.length;
        int el = e.length;
        boolean[][] dp = new boolean[sl + 1][el + 1];
        
        // 字符串和模式都匹配完的情况
        dp[sl][el] = true;
        
        // 字符串匹配完，模式还有剩余的情况
        // 只有x*y*z*这种形式才能匹配空字符串
        for (int j = el - 2; j >= 0; j = j - 2) {
            if (e[j] != '*' && e[j + 1] == '*') {
                dp[sl][j] = true;
            } else {
                break;
            }
        }
        
        // 最后一个字符的匹配情况
        if (sl > 0 && el > 0) {
            if ((e[el - 1] == '.' || s[sl - 1] == e[el - 1])) {
                dp[sl - 1][el - 1] = true;
            }
        }
        return dp;
    }

    /**
     * 动态规划解法
     * 
     * @param str 待匹配字符串
     * @param exp 正则表达式
     * @return 是否匹配
     */
    public static boolean dp(String str, String exp) {
        if (str == null || exp == null) {
            return false;
        }
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        if (!isValid(s, e)) {
            return false;
        }
        
        // 初始化dp表
        boolean[][] dp = initDp(s, e);
        
        // 从右下角往左上角填表
        for (int i = s.length - 1; i >= 0; i--) {
            for (int j = e.length - 2; j >= 0; j--) {
                
                // 下一个字符不是*的情况
                if (e[j + 1] != '*') {
                    dp[i][j] = (s[i] == e[j] || e[j] == '.') && dp[i + 1][j + 1];
                } else {
                    // 下一个字符是*的情况
                    int si = i;
                    
                    // 尝试匹配多次
                    while (si < s.length && (s[si] == e[j] || e[j] == '.')) {
                        if (dp[si][j + 2]) {
                            dp[i][j] = true;
                            break;
                        }
                        si++;
                    }
                    
                    // 如果匹配多次都失败，尝试匹配0次
                    if (dp[i][j] != true) {
                        dp[i][j] = dp[si][j + 2];
                    }
                }
            }
        }
        return dp[0][0];
    }

    /**
     * 测试方法
     * 验证正则表达式匹配算法的正确性
     */
    public static void main(String[] args) {
        // 测试用例
        String str = "abcccdefg";
        String exp = "ab.*d.*e.*";
        
        System.out.println("字符串: " + str);
        System.out.println("模式: " + exp);
        System.out.println("递归解法结果: " + match(str, exp));
        System.out.println("动态规划结果: " + dp(str, exp));
        
        // 更多测试用例
        System.out.println("\n=== 更多测试用例 ===");
        testCase("aa", "a", false);
        testCase("aa", "a*", true);
        testCase("ab", ".*", true);
        testCase("aab", "c*a*b", true);
        testCase("mississippi", "mis*is*p*.", false);
    }
    
    /**
     * 测试用例辅助方法
     * 
     * @param str 测试字符串
     * @param exp 测试模式
     * @param expected 期望结果
     */
    private static void testCase(String str, String exp, boolean expected) {
        boolean result1 = match(str, exp);
        boolean result2 = dp(str, exp);
        System.out.printf("str='%s', exp='%s' => 递归:%b, DP:%b, 期望:%b %s%n", 
                str, exp, result1, result2, expected, 
                (result1 == expected && result2 == expected) ? "✓" : "✗");
    }
}