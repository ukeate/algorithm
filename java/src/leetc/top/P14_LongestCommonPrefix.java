package leetc.top;

/**
 * LeetCode 14. 最长公共前缀 (Longest Common Prefix)
 * 
 * 问题描述：
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 * 如果不存在公共前缀，返回空字符串 ""。
 * 
 * 示例：
 * 输入：strs = ["flower","flow","flight"]
 * 输出："fl"
 * 
 * 输入：strs = ["dog","racecar","car"]
 * 输出：""
 * 解释：输入不存在公共前缀。
 * 
 * 解法思路：
 * 垂直扫描法：
 * 1. 以第一个字符串为基准，逐字符进行比较
 * 2. 对每个字符位置，检查所有字符串在该位置的字符是否相同
 * 3. 一旦发现不匹配，立即返回当前已匹配的前缀
 * 4. 同时维护最小长度，避免越界
 * 
 * 算法特点：
 * - 提前终止：遇到第一个不匹配字符立即停止
 * - 长度优化：动态维护可能的最大前缀长度
 * - 边界处理：处理空数组、空字符串等边界情况
 * 
 * 优化策略：
 * - 预先计算最短字符串长度作为上界
 * - 逐个字符比较，遇到不匹配立即返回
 * - 特殊情况提前返回（如最短长度为0）
 * 
 * 时间复杂度：O(S) - S为所有字符串字符总数，最坏情况下需要比较所有字符
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/longest-common-prefix/
 */
public class P14_LongestCommonPrefix {
    
    /**
     * 查找字符串数组的最长公共前缀
     * 
     * @param strs 字符串数组
     * @return 最长公共前缀字符串
     */
    public static String longestCommonPrefix(String[] strs) {
        // 边界情况：空数组
        if (strs == null || strs.length == 0) {
            return "";
        }
        
        // 以第一个字符串为基准
        char[] chs = strs[0].toCharArray();
        int min = chs.length;  // 当前可能的最大前缀长度
        
        // 遍历其他所有字符串
        for (String str : strs) {
            char[] tmp = str.toCharArray();
            int idx = 0;
            
            // 逐字符比较，直到遇到不匹配或达到边界
            while (idx < tmp.length && idx < min) {
                if (chs[idx] != tmp[idx]) {
                    break;  // 发现不匹配字符，停止比较
                }
                idx++;
            }
            
            // 更新最短公共前缀长度
            min = Math.min(idx, min);
            
            // 如果公共前缀长度为0，可以提前返回
            if (min == 0) {
                return "";
            }
        }
        
        // 返回最长公共前缀
        return strs[0].substring(0, min);
    }
}
