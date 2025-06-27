package leetc.top;

/**
 * LeetCode 28. 找出字符串中第一个匹配项的下标
 * https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
 * 
 * 问题描述：
 * 给你两个字符串 haystack 和 needle ，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
 * 如果 needle 不是 haystack 的一部分，则返回 -1 。
 * 
 * 解题思路：
 * 使用KMP算法进行字符串匹配，这是一种高效的字符串匹配算法
 * 
 * KMP算法核心思想：
 * 1. 预处理模式串，构建next数组（失效函数）
 * 2. next[i]表示模式串中以i结尾的最长真前缀与真后缀相等的长度
 * 3. 当匹配失败时，利用next数组跳转，避免重复比较
 * 
 * next数组的作用：
 * - 当在位置i匹配失败时，下一次从next[i]位置开始匹配
 * - 这样可以跳过一些不必要的比较，提高效率
 * 
 * 时间复杂度：O(m + n)，其中m是主串长度，n是模式串长度
 * 空间复杂度：O(n)，用于存储next数组
 */
public class P28_FindTheIndexOfTheFirstOccurrenceInString {
    
    /**
     * 构建KMP算法的next数组（失效函数）
     * next[i]表示模式串中以i结尾的最长真前缀与真后缀相等的长度
     * 
     * @param m 模式串的字符数组
     * @return next数组
     */
    public static int[] next(char[] m) {
        // 特殊情况：模式串长度为1
        if (m.length == 1) {
            return new int[] {-1};
        }
        
        int[] next = new int[m.length];
        // 初始化：第0个位置设为-1，第1个位置设为0
        next[0] = -1;
        next[1] = 0;
        
        int i = 2;  // 当前计算next值的位置
        int cn = 0; // 当前匹配的最长前缀长度
        
        while (i < next.length) {
            // 如果当前字符与最长前缀的下一个字符匹配
            if (m[i - 1] == m[cn]) {
                next[i++] = ++cn;
            } 
            // 如果不匹配且cn > 0，跳转到更短的前缀
            else if (cn > 0) {
                cn = next[cn];
            } 
            // 如果cn == 0，说明没有匹配的前缀
            else {
                next[i++] = 0;
            }
        }
        return next;
    }

    /**
     * 使用KMP算法在主串中查找模式串的第一个出现位置
     * 
     * @param s 主串（haystack）
     * @param m 模式串（needle）
     * @return 第一个匹配项的下标，如果不存在则返回-1
     */
    public static int strStr(String s, String m) {
        // 边界条件检查
        if (s == null || m == null || s.length() < m.length()) {
            return -1;
        }
        // 空模式串的情况
        if (m.length() == 0) {
            return 0;
        }
        
        char[] str1 = s.toCharArray();  // 主串字符数组
        char[] str2 = m.toCharArray();  // 模式串字符数组
        int x = 0;  // 主串的当前位置指针
        int y = 0;  // 模式串的当前位置指针
        
        // 构建模式串的next数组
        int[] next = next(str2);
        
        // KMP主匹配过程
        while (x < str1.length && y < str2.length) {
            // 如果当前字符匹配，两个指针都向前移动
            if (str1[x] == str2[y]) {
                x++;
                y++;
            } 
            // 如果next[y] == -1，说明模式串的第一个字符就不匹配，主串指针向前移动
            else if (next[y] == -1) {
                x++;
            } 
            // 利用next数组进行跳转，模式串指针跳到next[y]位置
            else {
                y = next[y];
            }
        }
        
        // 如果模式串完全匹配，返回匹配开始的位置；否则返回-1
        return y == str2.length ? x - y : -1;
    }
}
