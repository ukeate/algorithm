package leetc.top;

/**
 * LeetCode 5. 最长回文子串 (Longest Palindromic Substring)
 * 
 * 问题描述：
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 * 
 * 示例：
 * - 输入：s = "babad"，输出："bab"（"aba" 同样是符合题意的答案）
 * - 输入：s = "cbbd"，输出："bb"
 * 
 * 解法思路：
 * Manacher算法（马拉车算法）- 线性时间复杂度的回文串算法：
 * 1. 字符串预处理：在每个字符间插入特殊字符，统一处理奇偶回文
 * 2. 维护最右回文边界和对应的中心
 * 3. 利用回文的对称性质，避免重复计算
 * 4. 动态更新最右边界和回文半径数组
 * 
 * 核心思想：
 * - 利用已知回文信息加速计算：如果当前位置在已知回文范围内，
 *   可以利用对称性质快速确定初始回文半径
 * - 边界更新：当发现更长回文时，更新最右边界
 * 
 * 时间复杂度：O(n) - 每个字符最多被访问常数次
 * 空间复杂度：O(n) - 需要额外的预处理字符串和回文半径数组
 */
public class P5_LongestPalindromicSubstring {
    
    /**
     * 字符串预处理：插入分隔符统一处理奇偶回文
     * 
     * 例如："abc" -> "#a#b#c#"
     * 这样所有回文都变成奇数长度，简化处理逻辑
     * 
     * @param s 原始字符串
     * @return 预处理后的字符数组
     */
    private static char[] manacherString(String s) {
        char[] chas = s.toCharArray();
        char[] res = new char[s.length() * 2 + 1];
        int idx = 0;
        
        // 在字符间插入'#'
        for (int i = 0; i < res.length; i++) {
            res[i] = (i & 1) == 0 ? '#' : chas[idx++];
        }
        return res;
    }

    /**
     * 使用Manacher算法寻找最长回文子串
     * 
     * 算法步骤：
     * 1. 预处理字符串，插入分隔符
     * 2. 初始化变量：p(回文中心)、pr(最右边界)、ra(回文半径数组)
     * 3. 对每个位置计算回文半径：
     *    - 如果在已知回文范围内，利用对称性质初始化
     *    - 然后尝试扩展边界
     * 4. 更新最右边界和回文中心
     * 5. 记录最长回文的位置和长度
     * 6. 还原到原字符串的坐标并返回结果
     * 
     * @param s 输入字符串
     * @return 最长回文子串
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        
        int p = -1, pr = -1;                    // p:当前回文中心, pr:最右回文边界
        char[] ss = manacherString(s);          // 预处理后的字符串
        int[] ra = new int[ss.length];          // 回文半径数组
        int max = Integer.MIN_VALUE, maxIdx = 0; // 最长回文的半径和中心位置
        
        // 遍历预处理后的每个位置
        for (int i = 0; i < ss.length; i++) {
            // 利用回文的对称性质确定初始回文半径
            ra[i] = pr > i ? Math.min(ra[2 * p - i], pr - i) : 1;
            
            // 尝试扩展回文边界
            while (i + ra[i] < ss.length && i - ra[i] > -1) {
                if (ss[i + ra[i]] == ss[i - ra[i]]) {
                    ra[i]++;  // 回文半径扩展
                } else {
                    break;    // 无法继续扩展
                }
            }
            
            // 更新最右回文边界和中心
            if (i + ra[i] > pr) {
                pr = i + ra[i];  // 更新最右边界
                p = i;           // 更新回文中心
            }
            
            // 记录最长回文
            if (max < ra[i]) {
                max = ra[i];     // 更新最大回文半径
                maxIdx = i;      // 更新最长回文的中心位置
            }
        }
        
        // 将预处理字符串的坐标还原到原字符串
        maxIdx = (maxIdx - 1) / 2;              // 还原到原字符串的中心位置
        int maxR = (max - 1) / 2;               // 还原到原字符串的回文半径
        
        // 根据回文长度的奇偶性确定起始位置
        return s.substring((max & 1) == 0 ? maxIdx - maxR : maxIdx - maxR + 1, 
                          maxIdx + maxR + 1);
    }
}
