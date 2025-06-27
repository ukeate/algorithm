package leetc.top;

import java.util.HashSet;

/**
 * LeetCode 291. 单词规律 II (Word Pattern II)
 * 
 * 问题描述：
 * 给定一个模式 pattern 和一个字符串 s，判断 s 是否遵循相同的模式。
 * 这里的 遵循 指完全匹配，例如， pattern 里的每个字母和字符串 s 中的每个非空单词之间存在着双向连接的对应模式。
 * 
 * 与LeetCode 290不同的是，这里pattern中的字符可以匹配任意长度的子串，而不仅仅是单个单词。
 * 
 * 示例：
 * 输入: pattern = "abab", s = "redblueredblue"
 * 输出: true
 * 解释: 一种可能的映射方式是：'a' -> "red", 'b' -> "blue"
 * 
 * 输入: pattern = "aaaa", s = "asdasdasdasd"
 * 输出: true
 * 解释: 一种可能的映射方式是：'a' -> "asd"
 * 
 * 解法思路：
 * DFS递归回溯 + 映射约束：
 * 
 * 1. 核心思想：
 *    - 对pattern中的每个字符，尝试匹配s中不同长度的子串
 *    - 维护字符到字符串的映射关系（双向唯一）
 *    - 使用回溯法尝试所有可能的匹配方案
 * 
 * 2. 数据结构：
 *    - String[] map：pattern字符到字符串的映射（基于字符ASCII）
 *    - HashSet<String> set：已被使用的字符串集合（防止多对一映射）
 * 
 * 3. 剪枝优化：
 *    - 提前计算剩余字符串的最小长度需求
 *    - 避免无效的字符串分配
 * 
 * 4. 约束条件：
 *    - 同一字符必须映射到同一字符串
 *    - 不同字符不能映射到同一字符串
 * 
 * 时间复杂度：O(N^M)，其中N是字符串长度，M是模式长度
 * 空间复杂度：O(M)，递归栈和映射存储
 * 
 * LeetCode链接：https://leetcode.com/problems/word-pattern-ii/
 */
public class P291_WordPatternII {
    
    /**
     * 递归匹配字符串是否符合模式
     * 
     * @param s 待匹配的字符串
     * @param p 模式字符串
     * @param si 当前字符串位置
     * @param pi 当前模式位置
     * @param map 字符到字符串的映射数组（基于a-z的26个字符）
     * @param set 已使用字符串的集合
     * @return 是否匹配成功
     */
    private static boolean match(String s, String p, int si, int pi, String[] map, HashSet<String> set) {
        // 递归终止条件：模式和字符串都处理完毕
        if (pi == p.length() && si == s.length()) {
            return true;
        }
        
        // 递归终止条件：只有一个处理完毕（长度不匹配）
        if (pi == p.length() || si == s.length()) {
            return false;
        }
        
        char ch = p.charAt(pi);            // 当前模式字符
        String cur = map[ch - 'a'];        // 查看该字符是否已有映射
        
        if (cur != null) {
            // 情况1：该字符已有映射，检查是否匹配
            return si + cur.length() <= s.length() && 
                   cur.equals(s.substring(si, si + cur.length())) &&
                   match(s, p, si + cur.length(), pi + 1, map, set);
        }
        
        // 情况2：该字符第一次出现，需要建立新的映射
        
        // 剪枝优化：计算剩余模式字符的最小字符串长度需求
        int end = s.length();
        for (int i = p.length() - 1; i > pi; i--) {
            // 对于每个剩余字符，预留至少1个字符的空间（如果还没映射）
            // 或者预留已映射字符串的确切长度
            end -= map[p.charAt(i) - 'a'] == null ? 1 : map[p.charAt(i) - 'a'].length();
        }
        
        // 尝试不同长度的字符串映射
        for (int i = si; i < end; i++) {
            cur = s.substring(si, i + 1);   // 尝试的子字符串
            
            // 检查该字符串是否已被其他字符使用
            if (!set.contains(cur)) {
                // 建立新的映射
                set.add(cur);
                map[ch - 'a'] = cur;
                
                // 递归检查剩余部分
                if (match(s, p, i + 1, pi + 1, map, set)) {
                    return true;
                }
                
                // 回溯：撤销映射
                map[ch - 'a'] = null;
                set.remove(cur);
            }
        }
        
        return false;  // 所有尝试都失败
    }

    /**
     * 判断字符串是否匹配给定的模式
     * 
     * 算法思路：
     * 1. 题目限制pattern和str中的字符都是a~z小写字母
     * 2. 使用长度为26的数组作为映射表：map[0]对应'a'，map[25]对应'z'
     * 3. 使用HashSet记录已经被使用的字符串，保证映射的唯一性
     * 4. 递归尝试所有可能的字符到子字符串的映射
     * 
     * @param pattern 模式字符串
     * @param str 待匹配的字符串
     * @return 是否匹配成功
     */
    public static boolean wordPatternMatch(String pattern, String str) {
        // 初始化映射数组（26个小写字母）和已使用字符串集合
        return match(str, pattern, 0, 0, new String[26], new HashSet<>());
    }
}
