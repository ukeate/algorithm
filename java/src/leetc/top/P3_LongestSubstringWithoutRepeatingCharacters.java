package leetc.top;

/**
 * LeetCode 3. 无重复字符的最长子串 (Longest Substring Without Repeating Characters)
 * 
 * 问题描述：
 * 给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
 * 
 * 示例：
 * - 输入: s = "abcabcbb"，输出: 3，解释：因为无重复字符的最长子串是 "abc"
 * - 输入: s = "bbbbb"，输出: 1，解释：因为无重复字符的最长子串是 "b"
 * 
 * 解法思路：
 * 滑动窗口 + 字符位置记录：
 * 1. 使用数组记录每个字符最后出现的位置
 * 2. 维护一个滑动窗口，当遇到重复字符时，调整窗口左边界
 * 3. 窗口左边界 = max(当前左边界, 重复字符的上次位置+1)
 * 4. 实时更新最长无重复子串的长度
 * 
 * 核心技巧：
 * - pre变量记录当前有效子串的起始位置的前一个位置
 * - 当遇到重复字符时，pre更新为该字符上次出现位置，确保新子串无重复
 * - cur = i - pre 表示当前位置为结尾的最长无重复子串长度
 * 
 * 时间复杂度：O(n) - 每个字符最多被访问两次
 * 空间复杂度：O(1) - 使用固定大小的字符位置数组（256个ASCII字符）
 */
public class P3_LongestSubstringWithoutRepeatingCharacters {
    
    /**
     * 找出无重复字符的最长子串长度
     * 
     * @param s 输入字符串
     * @return 最长无重复子串的长度
     */
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.equals("")) {
            return 0;
        }
        
        char[] chas = s.toCharArray();
        // 记录每个字符最后出现的位置，初始化为-1表示未出现
        int[] map = new int[256];
        for (int i = 0; i < 256; i++) {
            map[i] = -1;
        }
        
        int pre = -1;  // 当前有效子串起始位置的前一个位置
        int cur = 0;   // 当前位置结尾的最长无重复子串长度
        int len = 0;   // 全局最长无重复子串长度
        
        for (int i = 0; i < chas.length; i++) {
            // 更新有效起始位置：要么保持原来的pre，要么更新为当前字符上次出现的位置
            pre = Math.max(pre, map[chas[i]]);
            
            // 计算以当前位置结尾的最长无重复子串长度
            cur = i - pre;
            
            // 更新全局最大长度
            len = Math.max(len, cur);
            
            // 记录当前字符的位置
            map[chas[i]] = i;
        }
        
        return len;
    }
}
