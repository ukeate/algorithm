package leetc.top;

/**
 * LeetCode 647. 回文子串 (Palindromic Substrings)
 * 
 * 问题描述：
 * 给你一个字符串 s，请你统计并返回这个字符串中回文子串的数目。
 * 回文字符串是正着读和倒着读都一样的字符串。
 * 子字符串是字符串中的一个连续的字符序列。
 * 
 * 示例：
 * 输入：s = "abc"
 * 输出：3
 * 解释：三个回文子串: "a", "b", "c"
 * 
 * 输入：s = "aaa"  
 * 输出：6
 * 解释：六个回文子串: "a", "a", "a", "aa", "aa", "aaa"
 * 
 * 解法思路：
 * 使用Manacher算法（马拉车算法）高效求解：
 * 1. 字符串预处理：在每个字符间插入'#'，统一处理奇偶长度回文串
 * 2. 利用回文串的对称性，避免重复计算
 * 3. 维护当前最右回文边界和中心位置，加速计算
 * 4. 对于每个位置，计算以该位置为中心的最长回文半径
 * 5. 回文半径除以2就是该中心产生的回文子串个数
 * 
 * 算法优势：
 * - 线性时间复杂度，相比暴力方法的O(n³)大幅优化
 * - 利用已计算信息，减少重复计算
 * 
 * 时间复杂度：O(n) - Manacher算法的线性复杂度
 * 空间复杂度：O(n) - 需要额外的字符数组和回文半径数组
 */
public class P647_PalindromicSubstrings {
    /**
     * Manacher算法预处理：在字符间插入'#'
     * 
     * 作用：统一处理奇偶长度的回文串
     * 例如："aba" -> "#a#b#a#"，"abba" -> "#a#b#b#a#"
     * 
     * @param str 原始字符串
     * @return 预处理后的字符数组
     */
    private static char[] manachers(String str) {
        char[] chs = str.toCharArray();
        char[] ans = new char[str.length() * 2 + 1];
        int idx = 0;
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (i & 1) == 0 ? '#' : chs[idx++];
        }
        return ans;
    }

    /**
     * Manacher算法核心：计算每个位置的回文半径
     * 
     * @param s 原始字符串
     * @return 每个位置的回文半径数组
     */
    private static int[] dp(String s) {
        char[] str = manachers(s);
        int[] pa = new int[str.length]; // 回文半径数组
        int c = -1, r = -1; // c是回文中心，r是最右回文边界
        
        for (int i = 0; i < str.length; i++) {
            // 利用对称性初始化pa[i]
            pa[i] = r > i ? Math.min(pa[2 * c - i], r - i) : 1;
            
            // 尝试扩展回文半径
            while (i + pa[i] < str.length && i - pa[i] > -1) {
                if (str[i + pa[i]] == str[i - pa[i]]) {
                    pa[i]++;
                } else {
                    break;
                }
            }
            
            // 更新最右回文边界和中心
            if (i + pa[i] > r) {
                r = i + pa[i];
                c = i;
            }
        }
        return pa;
    }

    /**
     * 统计回文子串数量主方法
     * 
     * @param s 输入字符串
     * @return 回文子串的总数
     */
    public static int countSubstrings(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        int[] dp = dp(s); // 获取每个位置的回文半径
        int ans = 0;
        
        // 累加每个中心点产生的回文子串数量
        // 回文半径右移1位等价于除以2（去掉插入的'#'影响）
        for (int i = 0; i < dp.length; i++) {
            ans += dp[i] >> 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        countSubstrings("ababa");
    }
}
