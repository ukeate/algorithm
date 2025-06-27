package basic.c36;

/**
 * 字符串交错组成问题
 * 
 * 问题描述：
 * 给定三个字符串s1、s2和aim，判断aim是否能由s1和s2交错组成。
 * 交错组成是指：aim中的每个字符都来自s1或s2，且在原字符串中的相对顺序保持不变。
 * 
 * 例如：
 * s1 = "1234", s2 = "abcd", aim = "1a23bcd4"
 * 可以看出aim由s1和s2交错组成：1(s1) + a(s2) + 23(s1) + bcd(s2) + 4(s1)
 * 
 * 算法思路：
 * 这是一个经典的动态规划问题：
 * 方法1：二维DP - 时间O(M*N)，空间O(M*N)
 * 方法2：空间优化DP - 时间O(M*N)，空间O(min(M,N))
 * 
 * 状态定义：
 * dp[i][j]表示s1的前i个字符和s2的前j个字符能否交错组成aim的前i+j个字符
 * 
 * 状态转移：
 * dp[i][j] = (dp[i-1][j] && s1[i-1] == aim[i+j-1]) || 
 *            (dp[i][j-1] && s2[j-1] == aim[i+j-1])
 */
public class StringCross {
    
    /**
     * 方法1：二维动态规划解法
     * 
     * 算法步骤：
     * 1. 边界检查：长度必须匹配
     * 2. 初始化：dp[0][0] = true，处理边界情况
     * 3. 状态转移：检查从s1或s2取字符的可能性
     * 4. 返回dp[s1.length][s2.length]
     * 
     * 时间复杂度：O(M*N)，M和N分别是s1和s2的长度
     * 空间复杂度：O(M*N)
     * 
     * @param s1 第一个源字符串
     * @param s2 第二个源字符串
     * @param ai 目标字符串（aim的缩写）
     * @return 是否能交错组成
     */
    public static boolean isCross1(String s1, String s2, String ai) {
        // 边界条件检查
        if (s1 == null || s2 == null || ai == null) {
            return false;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        char[] aim = ai.toCharArray();
        
        // 长度必须匹配
        if (aim.length != str1.length + str2.length) {
            return false;
        }
        
        // 动态规划表：dp[i][j]表示str1前i个字符和str2前j个字符
        // 能否交错组成aim的前i+j个字符
        boolean[][] dp = new boolean[str1.length + 1][str2.length + 1];
        
        // 初始化：空字符串可以组成空字符串
        dp[0][0] = true;
        
        // 初始化第一行：只使用str1的字符
        for (int i = 1; i <= str1.length; i++) {
            if (str1[i - 1] != aim[i - 1]) {
                break;  // 一旦不匹配，后续都不可能匹配
            }
            dp[i][0] = true;
        }
        
        // 初始化第一列：只使用str2的字符
        for (int j = 1; j <= str2.length; j++) {
            if (str2[j - 1] != aim[j - 1]) {
                break;  // 一旦不匹配，后续都不可能匹配
            }
            dp[0][j] = true;
        }
        
        // 填充DP表
        for (int i = 1; i <= str1.length; i++) {
            for (int j = 1; j <= str2.length; j++) {
                // 两种可能：
                // 1. 从str1取当前字符：检查str1[i-1]是否等于aim[i+j-1]，且dp[i-1][j]为true
                // 2. 从str2取当前字符：检查str2[j-1]是否等于aim[i+j-1]，且dp[i][j-1]为true
                if ((str1[i - 1] == aim[i + j - 1] && dp[i - 1][j])
                        || (str2[j - 1] == aim[i + j - 1] && dp[i][j - 1])) {
                    dp[i][j] = true;
                }
            }
        }
        
        return dp[str1.length][str2.length];
    }

    /**
     * 方法2：空间优化的动态规划解法
     * 
     * 核心优化思想：
     * 由于dp[i][j]只依赖于dp[i-1][j]和dp[i][j-1]，
     * 可以使用一维数组进行空间优化。
     * 
     * 技巧：
     * 1. 选择较短的字符串作为列维度，减少空间占用
     * 2. 使用滚动数组思想，用一维数组模拟二维DP
     * 
     * 时间复杂度：O(M*N)
     * 空间复杂度：O(min(M,N))
     * 
     * @param s1 第一个源字符串
     * @param s2 第二个源字符串
     * @param ai 目标字符串
     * @return 是否能交错组成
     */
    public static boolean isCross2(String s1, String s2, String ai) {
        // 边界条件检查
        if (s1 == null || s2 == null || ai == null) {
            return false;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        char[] aim = ai.toCharArray();
        
        // 长度必须匹配
        if (aim.length != str1.length + str2.length) {
            return false;
        }
        
        // 空间优化：让较长的数组作为外层循环，较短的作为DP数组维度
        char[] longs = str1.length >= str2.length ? str1 : str2;
        char[] shorts = str1.length < str2.length ? str1 : str2;
        
        // 一维DP数组：dp[j]表示当前处理longs的某个前缀时，
        // shorts的前j个字符能否与longs的前缀交错组成aim的对应前缀
        boolean[] dp = new boolean[shorts.length + 1];
        
        // 初始化：空字符串可以组成空字符串
        dp[0] = true;
        
        // 初始化：只使用shorts的字符
        for (int i = 1; i <= shorts.length; i++) {
            if (shorts[i - 1] != aim[i - 1]) {
                break;
            }
            dp[i] = true;
        }
        
        // 外层循环：遍历longs的每个字符
        for (int i = 1; i <= longs.length; i++) {
            // 更新dp[0]：只使用longs的前i个字符能否组成aim的前i个字符
            dp[0] = dp[0] && longs[i - 1] == aim[i - 1];
            
            // 内层循环：遍历shorts的每个字符
            for (int j = 1; j <= shorts.length; j++) {
                // 状态转移：
                // 从longs取字符：longs[i-1] == aim[i+j-1] && dp[j]（上一行同列）
                // 从shorts取字符：shorts[j-1] == aim[i+j-1] && dp[j-1]（同行前一列）
                if ((longs[i - 1] == aim[i + j - 1] && dp[j])
                        || (shorts[j - 1] == aim[i + j - 1] && dp[j - 1])) {
                    dp[j] = true;
                } else {
                    dp[j] = false;
                }
            }
        }
        
        return dp[shorts.length];
    }

    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串交错组成测试 ===");
        
        // 测试用例1：正常情况
        String s1 = "1234";
        String s2 = "abcd";
        String aim = "1a23bcd4";
        
        System.out.println("测试用例1：");
        System.out.println("s1 = \"" + s1 + "\"");
        System.out.println("s2 = \"" + s2 + "\"");
        System.out.println("aim = \"" + aim + "\"");
        
        boolean result1_1 = isCross1(s1, s2, aim);
        boolean result1_2 = isCross2(s1, s2, aim);
        
        System.out.println("方法1结果: " + result1_1);
        System.out.println("方法2结果: " + result1_2);
        System.out.println("结果一致: " + (result1_1 == result1_2));
        System.out.println();
        
        // 测试用例2：不能组成的情况
        String s3 = "abc";
        String s4 = "def";
        String aim2 = "abcfed";
        
        System.out.println("测试用例2（不能组成）：");
        System.out.println("s1 = \"" + s3 + "\"");
        System.out.println("s2 = \"" + s4 + "\"");
        System.out.println("aim = \"" + aim2 + "\"");
        
        boolean result2_1 = isCross1(s3, s4, aim2);
        boolean result2_2 = isCross2(s3, s4, aim2);
        
        System.out.println("方法1结果: " + result2_1);
        System.out.println("方法2结果: " + result2_2);
        System.out.println("结果一致: " + (result2_1 == result2_2));
        
        System.out.println("\n=== 测试完成 ===");
    }
}
