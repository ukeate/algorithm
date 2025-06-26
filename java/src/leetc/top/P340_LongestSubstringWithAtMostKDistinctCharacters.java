package leetc.top;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 340. 至多包含 K 个不同字符的最长子串 (Longest Substring with At Most K Distinct Characters)
 * 
 * 问题描述：
 * 给定一个字符串 s 和一个整数 k，返回至多包含 k 个不同字符的最长子串的长度。
 * 
 * 示例：
 * 输入：s = "eceba", k = 2
 * 输出：3
 * 解释：满足题意的子串是 "ece"，长度为 3。
 * 
 * 输入：s = "aa", k = 1
 * 输出：2
 * 解释：满足题意的子串是 "aa"，长度为 2。
 * 
 * 解法思路：
 * 滑动窗口 + 哈希表：
 * 
 * 1. 核心思想：
 *    - 使用滑动窗口维护一个至多包含k个不同字符的子串
 *    - 用哈希表记录窗口内每个字符的出现次数
 *    - 当不同字符数超过k时，收缩左边界
 * 
 * 2. 算法步骤：
 *    - 使用左右双指针维护滑动窗口
 *    - 右指针扩展窗口，加入新字符
 *    - 当窗口内不同字符数 > k时，左指针收缩窗口
 *    - 在整个过程中记录最大窗口长度
 * 
 * 3. 窗口状态维护：
 *    - charCount：记录每个字符在窗口内的出现次数
 *    - distinctCount：记录窗口内不同字符的数量
 *    - 当某字符计数归零时，不同字符数减1
 * 
 * 4. 边界处理：
 *    - k = 0：最长子串长度为0
 *    - k >= 字符串中不同字符数：整个字符串都满足条件
 * 
 * 核心思想：
 * - 滑动窗口：动态维护满足条件的子串
 * - 双指针：left和right协调移动，保持窗口有效性
 * - 贪心策略：尽可能扩大窗口，必要时收缩
 * 
 * 关键技巧：
 * - 哈希表统计：快速统计字符频次和种类
 * - 窗口收缩：当条件不满足时，及时调整窗口
 * - 最大值记录：在扩展过程中更新答案
 * 
 * 时间复杂度：O(n) - 每个字符最多被访问两次
 * 空间复杂度：O(min(n, k)) - 哈希表最多存储k个不同字符
 * 
 * LeetCode链接：https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
 */
public class P340_LongestSubstringWithAtMostKDistinctCharacters {
    
    /**
     * 计算至多包含k个不同字符的最长子串长度
     * 
     * 算法流程：
     * 1. 初始化滑动窗口的左右边界和哈希表
     * 2. 右指针向右移动，扩展窗口
     * 3. 当窗口内不同字符数超过k时，左指针向右收缩窗口
     * 4. 在每次窗口扩展时更新最大长度
     * 5. 返回最终的最大长度
     * 
     * @param s 输入字符串
     * @param k 最多允许的不同字符数
     * @return 满足条件的最长子串长度
     */
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (s == null || s.length() == 0 || k == 0) {
            return 0; // 边界情况：空字符串或k为0
        }
        
        int n = s.length();
        if (k >= n) {
            return n; // 如果k大于等于字符串长度，整个字符串都满足条件
        }
        
        // 滑动窗口的左右边界
        int left = 0;
        int maxLength = 0;
        
        // 哈希表：记录窗口内每个字符的出现次数
        Map<Character, Integer> charCount = new HashMap<>();
        
        // 右指针遍历字符串
        for (int right = 0; right < n; right++) {
            char rightChar = s.charAt(right);
            
            // 将右边字符加入窗口
            charCount.put(rightChar, charCount.getOrDefault(rightChar, 0) + 1);
            
            // 当窗口内不同字符数超过k时，收缩左边界
            while (charCount.size() > k) {
                char leftChar = s.charAt(left);
                int count = charCount.get(leftChar);
                
                if (count == 1) {
                    // 如果左边字符在窗口内只出现1次，移除后不同字符数减1
                    charCount.remove(leftChar);
                } else {
                    // 否则只减少计数
                    charCount.put(leftChar, count - 1);
                }
                
                left++; // 左边界右移
            }
            
            // 更新最大长度（当前窗口满足条件）
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
    
    /**
     * 优化版本：使用数组代替哈希表（适用于ASCII字符）
     * 
     * 当字符集较小时，可以用数组代替哈希表提高性能
     * 
     * @param s 输入字符串
     * @param k 最多允许的不同字符数
     * @return 满足条件的最长子串长度
     */
    public int lengthOfLongestSubstringKDistinctArray(String s, int k) {
        if (s == null || s.length() == 0 || k == 0) {
            return 0;
        }
        
        int n = s.length();
        if (k >= n) {
            return n;
        }
        
        int left = 0;
        int maxLength = 0;
        int distinctCount = 0; // 当前窗口内不同字符的数量
        
        // 假设只包含ASCII字符，使用数组代替哈希表
        int[] charCount = new int[128];
        
        for (int right = 0; right < n; right++) {
            char rightChar = s.charAt(right);
            
            // 如果这是新字符，增加不同字符计数
            if (charCount[rightChar] == 0) {
                distinctCount++;
            }
            charCount[rightChar]++;
            
            // 收缩窗口直到满足条件
            while (distinctCount > k) {
                char leftChar = s.charAt(left);
                charCount[leftChar]--;
                
                // 如果某字符计数归零，减少不同字符计数
                if (charCount[leftChar] == 0) {
                    distinctCount--;
                }
                
                left++;
            }
            
            // 更新最大长度
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}
