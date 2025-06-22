package base.dp;

/**
 * 解码方法问题
 * 
 * 问题描述：
 * 一条包含字母 A-Z 的消息通过以下映射进行了编码：
 * 'A' -> "1", 'B' -> "2", ..., 'Z' -> "26"
 * 要解码已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母（可能有多种方法）。
 * 给你一个只含数字的非空字符串 s，请计算并返回解码方法的总数。
 * 
 * 例如：
 * "12" 可以解码为 "AB"(1 2) 或者 "L"(12)，所以返回2
 * "226" 可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6)，所以返回3
 * 
 * 解法分析：
 * 1. 递归思路：对于位置i，可以单独解码或与i+1位置组成两位数解码
 * 2. 动态规划：dp[i]表示从位置i开始的解码方法数
 * 3. 边界条件：'0'不能单独解码，两位数必须在1-26范围内
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n) -> O(1) 空间优化
 */
// https://leetcode.cn/problems/decode-ways/
public class ConvertChar {
    
    /**
     * 暴力递归解法
     * 
     * @param str 字符数组
     * @param i 当前位置
     * @return 从位置i开始的解码方法数
     */
    private static int process(char[] str, int i) {
        // base case：到达字符串末尾，找到一种有效解码方法
        if (i == str.length) {
            return 1;
        }
        
        // 如果当前字符是'0'，无法单独解码
        if (str[i] == '0') {
            return 0;
        }
        
        // 选择1：单独解码当前字符（1-9都可以）
        int ways = process(str, i + 1);
        
        // 选择2：当前字符与下一个字符组成两位数解码（10-26）
        if (i + 1 < str.length && (str[i] - '0') * 10 + str[i + 1] - '0' < 27) {
            ways += process(str, i + 2);
        }
        
        return ways;
    }

    /**
     * 暴力递归解法入口
     * 
     * @param str 数字字符串
     * @return 解码方法总数
     */
    public static int convert(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        return process(str.toCharArray(), 0);
    }

    /**
     * 动态规划解法1（从右往左）
     * 
     * dp[i]表示从位置i开始到字符串末尾的解码方法数
     * 
     * @param s 数字字符串
     * @return 解码方法总数
     */
    public static int dp1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // dp[i]表示从位置i开始的解码方法数
        int[] dp = new int[n + 1];
        
        // base case：字符串末尾位置有1种方法（空字符串）
        dp[n] = 1;
        
        // 从右往左填充DP表
        for (int i = n - 1; i >= 0; i--) {
            if (str[i] != '0') {
                // 单独解码当前字符
                int ways = dp[i + 1];
                
                // 与下一个字符组成两位数解码
                if (i + 1 < n && (str[i] - '0') * 10 + str[i + 1] - '0' < 27) {
                    ways += dp[i + 2];
                }
                
                dp[i] = ways;
            }
            // 如果str[i] == '0'，dp[i]保持默认值0
        }
        
        return dp[0];
    }

    /**
     * 动态规划解法2（从左往右）
     * 
     * dp[i]表示从字符串开始到位置i的解码方法数
     * 
     * @param s 数字字符串
     * @return 解码方法总数
     */
    public static int dp2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        
        // 如果第一个字符是'0'，无法解码
        if (str[0] == '0') {
            return 0;
        }
        
        // dp[i]表示到位置i为止的解码方法数
        int[] dp = new int[n];
        
        // base case：第一个字符有1种解码方法
        dp[0] = 1;
        
        for (int i = 1; i < n; i++) {
            if (str[i] == '0') {
                // 当前字符是'0'，只能与前一个字符组成两位数
                if (str[i - 1] == '0' || str[i - 1] > '2') {
                    return 0;  // 无法组成有效的两位数，整个字符串无法解码
                } else {
                    // 只能通过两位数解码，方法数等于dp[i-2]
                    dp[i] = i - 2 >= 0 ? dp[i - 2] : 1;
                }
            } else {
                // 当前字符不是'0'，可以单独解码
                dp[i] = dp[i - 1];
                
                // 检查是否可以与前一个字符组成两位数解码
                if (str[i - 1] != '0' && (str[i - 1] - '0') * 10 + str[i] - '0' < 27) {
                    dp[i] += i - 2 >= 0 ? dp[i - 2] : 1;
                }
            }
        }
        
        return dp[n - 1];
    }

    /**
     * 生成随机数字字符串用于测试
     * 
     * @param len 字符串长度
     * @return 随机数字字符串
     */
    public static String randomStr(int len) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (Math.random() * 10) + '0');
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int n = 30;
        int times = 1000000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * n);
            String s = randomStr(len);
            int ans0 = convert(s);
            int ans1 = dp1(s);
            int ans2 = dp2(s);
            if (ans0 != ans1 || ans0 != ans2) {
                System.out.println("Wrong");
                System.out.println(s);
                System.out.println(ans0);
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
