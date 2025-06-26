package leetc.top;

/**
 * LeetCode 91. 解码方法 (Decode Ways)
 * 
 * 问题描述：
 * 一条包含字母 A-Z 的消息通过以下映射进行编码：
 * 'A' -> "1", 'B' -> "2", ..., 'Z' -> "26"
 * 
 * 要解码已编码的消息，所有数字必须基于上述映射的方法，反向映射回字母。
 * 给你一个只含数字的非空字符串 s，请计算并返回解码方法的总数。
 * 
 * 示例：
 * - 输入：s = "12"，输出：2
 *   解释：它可以解码为 "AB"（1 2）或者 "L"（12）
 * - 输入：s = "226"，输出：3
 *   解释：它可以解码为 "BZ" (2 26), "VF" (22 6), 或者 "BBF" (2 2 6)
 * 
 * 解法思路：
 * 动态规划：
 * 1. 对于每个位置，可以单独解码（1-9）或与前一位组合解码（10-26）
 * 2. 状态转移方程：
 *    - 如果当前位不是'0'：dp[i] += dp[i+1]
 *    - 如果当前位和下一位能组成10-26：dp[i] += dp[i+2]
 * 3. 边界条件：字符串末尾返回1，遇到'0'且无法组合则返回0
 * 
 * 时间复杂度：O(n) - 每个位置计算一次
 * 空间复杂度：O(n) - 需要dp数组存储中间结果
 */
public class P91_DecodeWays {
    
    /**
     * 递归解法：从左到右尝试所有可能的解码方式
     * 
     * 递归思路：
     * 1. 到达字符串末尾，说明找到一种有效解码，返回1
     * 2. 遇到'0'开头，无法解码，返回0
     * 3. 尝试单独解码当前字符（1-9）
     * 4. 尝试与下一字符组合解码（10-26）
     * 
     * @param str 字符数组
     * @param idx 当前处理的位置
     * @return 从当前位置开始的解码方法数
     */
    private static int process(char[] str, int idx) {
        // 到达字符串末尾，找到一种有效解码
        if (idx == str.length) {
            return 1;
        }
        
        // 遇到'0'开头的情况，无法单独解码
        if (str[idx] == '0') {
            return 0;
        }
        
        // 尝试单独解码当前字符（1-9都可以）
        int ways = process(str, idx + 1);
        
        // 如果已经是最后一个字符，只能单独解码
        if (idx + 1 == str.length) {
            return ways;
        }
        
        // 尝试与下一字符组合解码
        int num = (str[idx] - '0') * 10 + str[idx + 1] - '0';
        if (num <= 26) {
            ways += process(str, idx + 2);
        }
        
        return ways;
    }
    
    /**
     * 递归解法（会超时）
     * 
     * @param s 编码字符串
     * @return 解码方法总数
     */
    public static int numDecodings1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        return process(str, 0);
    }

    /**
     * 动态规划解法（优化版）
     * 
     * 算法思路：
     * 1. dp[i]表示从位置i到字符串末尾的解码方法数
     * 2. 从右往左填表，避免重复计算
     * 3. 状态转移：
     *    - 如果str[i] != '0'，可以单独解码：dp[i] = dp[i+1]
     *    - 如果str[i]和str[i+1]能组成10-26，可以组合解码：dp[i] += dp[i+2]
     * 
     * @param s 编码字符串
     * @return 解码方法总数
     */
    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        int[] dp = new int[n + 1];
        
        // 边界条件：字符串末尾位置
        dp[n] = 1;
        
        // 从右往左填表
        for (int i = n - 1; i >= 0; i--) {
            if (str[i] != '0') {
                // 可以单独解码当前字符
                dp[i] = dp[i + 1];
                
                // 如果是最后一个字符，无法组合解码
                if (i + 1 == str.length) {
                    continue;
                }
                
                // 尝试与下一字符组合解码
                int num = (str[i] - '0') * 10 + str[i + 1] - '0';
                if (num <= 26) {
                    dp[i] += dp[i + 2];
                }
            }
            // 如果str[i] == '0'，dp[i]保持默认值0
        }
        
        return dp[0];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        String[] testCases = {"12", "226", "0", "10", "27"};
        
        for (String s : testCases) {
            System.out.println("输入: " + s);
            System.out.println("递归解法: " + numDecodings1(s));
            System.out.println("动态规划: " + numDecodings2(s));
            System.out.println("---");
        }
    }
}
