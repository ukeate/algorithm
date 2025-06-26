package leetc.top;

/**
 * LeetCode 87. 扰乱字符串 (Scramble String)
 * 
 * 问题描述：
 * 使用下面描述的算法可以扰乱字符串 s 得到字符串 t：
 * 1. 如果字符串的长度为 1，算法停止
 * 2. 如果字符串的长度 > 1，执行下述步骤：
 *    - 在一个随机下标处将字符串分割成两个非空的子字符串
 *    - 决定是要「交换两个子字符串」还是要「保持这两个子字符串的顺序不变」
 *    - 在两个子字符串上递归执行步骤 1
 * 
 * 给定两个长度相等的字符串 s1 和 s2，判断 s2 是否是 s1 的扰乱字符串。
 * 
 * 示例：
 * 输入：s1 = "great", s2 = "rgeat"，输出：true
 * 解释：可能的一种情形是：
 * "great" --> "gr/eat" --> "gr/eat" --> "r/g/e/at" --> "r/g/e/at"
 * --> "r/g/e/a/t" --> "r/g/e/a/t" --> "rgeat"
 * 
 * 解法思路：
 * 动态规划或记忆化搜索：
 * 1. 基础条件：两个字符串必须包含相同的字符（字符计数相同）
 * 2. 递归条件：对于位置i和j开始长度为len的子串，有两种可能：
 *    - 不交换：s1[i...i+k-1]对应s2[j...j+k-1]，s1[i+k...i+len-1]对应s2[j+k...j+len-1]
 *    - 交换：s1[i...i+k-1]对应s2[j+len-k...j+len-1]，s1[i+k...i+len-1]对应s2[j...j+len-k-1]
 * 3. 使用三维DP表记录已计算的结果
 * 
 * 时间复杂度：O(n^4) - 三层循环枚举位置和长度，内层循环枚举分割点
 * 空间复杂度：O(n^3) - 三维DP表的空间开销
 * 
 * LeetCode链接：https://leetcode.com/problems/scramble-string/
 */
public class P87_ScrambleString {
    
    /**
     * 检查两个字符数组是否包含相同的字符（字符计数相同）
     * 
     * @param str1 第一个字符数组
     * @param str2 第二个字符数组
     * @return 是否包含相同的字符
     */
    private static boolean sameType(char[] str1, char[] str2) {
        if (str1.length != str2.length) {
            return false;
        }
        
        // 使用字符计数数组统计字符频次
        int[] map = new int[256];
        for (int i = 0; i < str1.length; i++) {
            map[str1[i]]++;
        }
        for (int i = 0; i < str2.length; i++) {
            if (--map[str2[i]] < 0) {
                return false; // str2中某字符比str1中多
            }
        }
        return true;
    }

    /**
     * 记忆化搜索的递归方法
     * 
     * @param str1 第一个字符数组
     * @param str2 第二个字符数组
     * @param l1 str1的起始位置
     * @param l2 str2的起始位置
     * @param size 要比较的子串长度
     * @param dp 记忆化数组，dp[i][j][k]表示str1[i...]长度为k和str2[j...]长度为k是否扰乱
     * @return str1[l1...l1+size-1] 是否是 str2[l2...l2+size-1] 的扰乱字符串
     */
    private static boolean process1(char[] str1, char[] str2, int l1, int l2, int size, int[][][] dp) {
        // 检查是否已经计算过
        if (dp[l1][l2][size] != 0) {
            return dp[l1][l2][size] == 1;
        }
        
        boolean ans = false;
        
        // 基础情况：长度为1时，直接比较字符
        if (size == 1) {
            ans = str1[l1] == str2[l2];
        } else {
            // 尝试所有可能的分割点
            for (int leftSize = 1; leftSize < size; leftSize++) {
                // 情况1：不交换子串
                // str1的左半部分对应str2的左半部分，右半部分对应右半部分
                boolean case1Left = process1(str1, str2, l1, l2, leftSize, dp);
                boolean case1Right = process1(str1, str2, l1 + leftSize, l2 + leftSize, size - leftSize, dp);
                
                // 情况2：交换子串
                // str1的左半部分对应str2的右半部分，右半部分对应左半部分
                boolean case2Left = process1(str1, str2, l1, l2 + size - leftSize, leftSize, dp);
                boolean case2Right = process1(str1, str2, l1 + leftSize, l2, size - leftSize, dp);
                
                if ((case1Left && case1Right) || (case2Left && case2Right)) {
                    ans = true;
                    break; // 找到一种可能的分割方式
                }
            }
        }
        
        // 记录结果到dp数组
        dp[l1][l2][size] = ans ? 1 : -1;
        return ans;
    }

    /**
     * 判断s2是否是s1的扰乱字符串（记忆化搜索版本）
     * 
     * @param s1 原字符串
     * @param s2 可能的扰乱字符串
     * @return s2是否是s1的扰乱字符串
     */
    public static boolean isScramble1(String s1, String s2) {
        // 处理边界情况
        if (s1 == null ^ s2 == null) {
            return false; // 一个为null一个不为null
        }
        if (s1 == null && s2 == null) {
            return true; // 都为null
        }
        if (s1.equals(s2)) {
            return true; // 完全相同
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 快速剪枝：字符计数不同则不可能是扰乱字符串
        if (!sameType(str1, str2)) {
            return false;
        }
        
        int n = s1.length();
        // dp[i][j][k] = 0未计算，1为true，-1为false
        int[][][] dp = new int[n][n][n + 1];
        return process1(str1, str2, 0, 0, n, dp);
    }

    /**
     * 判断s2是否是s1的扰乱字符串（动态规划版本）
     * 
     * 算法流程：
     * 1. 初始化：长度为1的子串直接比较字符是否相等
     * 2. 状态转移：对于长度size > 1的子串，枚举所有分割点leftSize
     *    - dp[l1][l2][size] = true，当且仅当存在某个分割点k使得：
     *      (不交换：dp[l1][l2][k] && dp[l1+k][l2+k][size-k]) 或
     *      (交换：dp[l1][l2+size-k][k] && dp[l1+k][l2][size-k])
     * 
     * @param s1 原字符串
     * @param s2 可能的扰乱字符串
     * @return s2是否是s1的扰乱字符串
     */
    public static boolean isScramble2(String s1, String s2) {
        // 处理边界情况
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
        
        // 快速剪枝：字符计数不同则不可能是扰乱字符串
        if (!sameType(str1, str2)) {
            return false;
        }
        
        int n = s1.length();
        boolean[][][] dp = new boolean[n][n][n + 1];
        
        // 初始化：长度为1的情况
        for (int l1 = 0; l1 < n; l1++) {
            for (int l2 = 0; l2 < n; l2++) {
                dp[l1][l2][1] = str1[l1] == str2[l2];
            }
        }
        
        // 动态规划：从长度2开始逐步计算到长度n
        for (int size = 2; size <= n; size++) {
            for (int l1 = 0; l1 <= n - size; l1++) {
                for (int l2 = 0; l2 <= n - size; l2++) {
                    // 尝试所有可能的分割点
                    for (int leftSize = 1; leftSize < size; leftSize++) {
                        // 情况1：不交换
                        boolean case1 = dp[l1][l2][leftSize] && dp[l1 + leftSize][l2 + leftSize][size - leftSize];
                        
                        // 情况2：交换
                        boolean case2 = dp[l1][l2 + size - leftSize][leftSize] && dp[l1 + leftSize][l2][size - leftSize];
                        
                        if (case1 || case2) {
                            dp[l1][l2][size] = true;
                            break; // 找到一种分割方式即可
                        }
                    }
                }
            }
        }
        
        return dp[0][0][n];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：great -> rgeat
        String s1 = "great", s2 = "rgeat";
        System.out.println("输入: s1=\"" + s1 + "\", s2=\"" + s2 + "\"");
        System.out.println("记忆化搜索结果: " + isScramble1(s1, s2));
        System.out.println("动态规划结果: " + isScramble2(s1, s2));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 测试用例2：abcde -> caebd
        s1 = "abcde"; s2 = "caebd";
        System.out.println("输入: s1=\"" + s1 + "\", s2=\"" + s2 + "\"");
        System.out.println("记忆化搜索结果: " + isScramble1(s1, s2));
        System.out.println("动态规划结果: " + isScramble2(s1, s2));
        System.out.println("期望结果: false");
        System.out.println();
        
        // 测试用例3：相同字符串
        s1 = "a"; s2 = "a";
        System.out.println("输入: s1=\"" + s1 + "\", s2=\"" + s2 + "\"");
        System.out.println("记忆化搜索结果: " + isScramble1(s1, s2));
        System.out.println("动态规划结果: " + isScramble2(s1, s2));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 测试用例4：字符计数不同
        s1 = "abc"; s2 = "def";
        System.out.println("输入: s1=\"" + s1 + "\", s2=\"" + s2 + "\"");
        System.out.println("记忆化搜索结果: " + isScramble1(s1, s2));
        System.out.println("动态规划结果: " + isScramble2(s1, s2));
        System.out.println("期望结果: false");
        System.out.println();
        
        // 性能测试
        s1 = "hwarzzstqhwarzzstqhwarzzstq"; 
        s2 = "qhwarzzstqhwarzzstqhwarzzst";
        long start = System.currentTimeMillis();
        boolean result1 = isScramble1(s1, s2);
        long mid = System.currentTimeMillis();
        boolean result2 = isScramble2(s1, s2);
        long end = System.currentTimeMillis();
        
        System.out.println("性能测试 (长度" + s1.length() + "):");
        System.out.println("记忆化搜索: " + result1 + ", 耗时: " + (mid - start) + "ms");
        System.out.println("动态规划: " + result2 + ", 耗时: " + (end - mid) + "ms");
    }
}
