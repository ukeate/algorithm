package leetc.top;

/**
 * LeetCode 97. 交错字符串 (Interleaving String)
 * 
 * 问题描述：
 * 给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错组成的。
 * 
 * 两个字符串 s 和 t 交错的定义与过程如下，其中每个字符串都会被分割成若干非空子字符串：
 * - s = s1 + s2 + ... + sn
 * - t = t1 + t2 + ... + tm
 * - |n - m| <= 1
 * - 交错是 s1 + t1 + s2 + t2 + ... 或者 t1 + s1 + t2 + s2 + ...
 * 
 * 示例：
 * - 输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"，输出：true
 * - 输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"，输出：false
 * 
 * 解法思路：
 * 二维动态规划：
 * 1. dp[i][j]表示s1的前i个字符和s2的前j个字符能否交错组成s3的前i+j个字符
 * 2. 状态转移方程：
 *    - 如果s1[i-1] == s3[i+j-1]，则dp[i][j] |= dp[i-1][j]
 *    - 如果s2[j-1] == s3[i+j-1]，则dp[i][j] |= dp[i][j-1]
 * 3. 边界条件：dp[0][0] = true，第一行和第一列需要特殊处理
 * 
 * 核心思想：
 * - 对于s3的每个位置，只能由s1或s2中的某个字符贡献
 * - 需要保证s1和s2的字符在s3中保持相对顺序
 * 
 * 时间复杂度：O(m×n) - m和n分别是s1和s2的长度
 * 空间复杂度：O(m×n) - 需要二维dp数组
 */
public class P97_InterleavingString {
    
    /**
     * 判断s3是否是s1和s2的交错字符串
     * 
     * 算法步骤：
     * 1. 边界检查：长度必须匹配
     * 2. 初始化dp数组：dp[i][j]表示s1前i个字符和s2前j个字符能否交错组成s3前i+j个字符
     * 3. 处理边界情况：
     *    - dp[0][0] = true（空字符串可以交错组成空字符串）
     *    - 第一行：只使用s1的字符
     *    - 第一列：只使用s2的字符
     * 4. 填充dp表：
     *    - 当前位置可以由上方转移（使用s1的字符）
     *    - 当前位置可以由左方转移（使用s2的字符）
     * 5. 返回dp[s1.length][s2.length]
     * 
     * @param s1 第一个输入字符串
     * @param s2 第二个输入字符串
     * @param s3 目标交错字符串
     * @return s3是否是s1和s2的交错字符串
     */
    public static boolean isInterleave(String s1, String s2, String s3) {
        // 边界检查
        if (s1 == null || s2 == null || s3 == null) {
            return false;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        char[] str3 = s3.toCharArray();
        
        // 长度必须匹配
        if (str3.length != str1.length + str2.length) {
            return false;
        }
        
        // dp[i][j]表示s1的前i个字符和s2的前j个字符能否交错组成s3的前i+j个字符
        boolean[][] dp = new boolean[str1.length + 1][str2.length + 1];
        
        // 边界条件：空字符串可以交错组成空字符串
        dp[0][0] = true;
        
        // 初始化第一行：只使用s1的字符来匹配s3
        for (int i = 1; i <= str1.length; i++) {
            if (str1[i - 1] != str3[i - 1]) {
                break;  // 一旦不匹配，后续都无法匹配
            }
            dp[i][0] = true;
        }
        
        // 初始化第一列：只使用s2的字符来匹配s3
        for (int j = 1; j <= str2.length; j++) {
            if (str2[j - 1] != str3[j - 1]) {
                break;  // 一旦不匹配，后续都无法匹配
            }
            dp[0][j] = true;
        }
        
        // 填充dp表
        for (int i = 1; i <= str1.length; i++) {
            for (int j = 1; j <= str2.length; j++) {
                // 状态转移：
                // 1. 如果s1[i-1]等于s3[i+j-1]，且dp[i-1][j]为true，则当前状态为true
                // 2. 如果s2[j-1]等于s3[i+j-1]，且dp[i][j-1]为true，则当前状态为true
                if ((str1[i - 1] == str3[i + j - 1] && dp[i - 1][j])
                        || (str2[j - 1] == str3[i + j - 1] && dp[i][j - 1])) {
                    dp[i][j] = true;
                }
            }
        }
        
        return dp[str1.length][str2.length];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        String s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac";
        System.out.println("输入: s1=\"" + s1 + "\", s2=\"" + s2 + "\", s3=\"" + s3 + "\"");
        System.out.println("结果: " + isInterleave(s1, s2, s3));  // 输出: true
        
        // 测试用例2
        s1 = "aabcc"; s2 = "dbbca"; s3 = "aadbbbaccc";
        System.out.println("输入: s1=\"" + s1 + "\", s2=\"" + s2 + "\", s3=\"" + s3 + "\"");
        System.out.println("结果: " + isInterleave(s1, s2, s3));  // 输出: false
        
        // 测试用例3
        s1 = ""; s2 = ""; s3 = "";
        System.out.println("输入: s1=\"" + s1 + "\", s2=\"" + s2 + "\", s3=\"" + s3 + "\"");
        System.out.println("结果: " + isInterleave(s1, s2, s3));  // 输出: true
    }
}
