package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * LeetCode 30. 串联所有单词的子串 (Substring with Concatenation of All Words)
 * 
 * 问题描述：
 * 给定一个字符串 s 和一些长度相同的单词 words。
 * 找出 s 中恰好可以由 words 中所有单词串联形成的子串的起始位置。
 * 
 * 注意子串要与 words 中的单词完全匹配，中间不能有其他字符，但不需要考虑 words 中单词串联的顺序。
 * 
 * 示例：
 * 输入：s = "barfoothefoobarman", words = ["foo","bar"]
 * 输出：[0,9]
 * 解释：
 * 从索引 0 和 9 开始的子串分别是 "barfoo" 和 "foobar"。
 * 输出的顺序不重要, [9,0] 也是有效答案。
 * 
 * 解法思路：
 * 滑动窗口 + 哈希表：
 * 1. 所有单词长度相同，可以按固定长度切分字符串
 * 2. 使用滑动窗口技术，窗口大小为所有单词的总长度
 * 3. 为了避免重复计算，按单词长度为步长移动窗口
 * 4. 使用哈希表跟踪窗口内单词的出现次数
 * 5. 维护债务变量debt，表示还需要匹配的单词数量
 * 
 * 核心观察：
 * - 由于单词长度固定，只需要考虑wordLen种不同的起始位置
 * - 对于每种起始位置，使用滑动窗口按wordLen步长移动
 * - 窗口内恰好包含wordNum个单词，检查是否匹配
 * 
 * 算法优化：
 * - 预处理：统计words中每个单词的出现次数
 * - 滑动窗口：避免重复计算窗口内容
 * - 债务机制：快速判断窗口是否满足条件
 * 
 * 时间复杂度：O(n * wordLen) - n为字符串长度，每个位置最多被访问常数次
 * 空间复杂度：O(wordNum * wordLen) - 哈希表存储单词及其计数
 * 
 * LeetCode链接：https://leetcode.com/problems/substring-with-concatenation-of-all-words/
 */
public class P30_SubstringWithConcatenationOfAllWords {
    
    /**
     * 在指定起始位置开始寻找匹配的子串
     * 
     * 算法步骤：
     * 1. 构建第一个完整窗口，统计其中每个单词的出现次数
     * 2. 计算债务debt（需要匹配的单词数）
     * 3. 滑动窗口：移出一个单词，移入一个单词
     * 4. 更新窗口内单词计数和债务值
     * 5. 当债务为0时，找到一个匹配的起始位置
     * 
     * @param s 原字符串
     * @param start 起始位置
     * @param wordLen 单词长度
     * @param wordNum 单词数量
     * @param map words中单词及其出现次数的映射
     * @param ans 结果列表
     */
    private static void find(String s, int start, int wordLen, int wordNum, HashMap<String, Integer> map, List<Integer> ans) {
        HashMap<String, Integer> window = new HashMap<>();  // 当前窗口内单词计数
        int debt = wordNum;  // 债务：还需要匹配的单词数量
        
        // 构建首个完整窗口
        for (int part = 0; part < wordNum; part++) {
            int l = start + part * wordLen;      // 当前单词起始位置
            int r = l + wordLen;                 // 当前单词结束位置
            
            // 边界检查
            if (r > s.length()) {
                break;
            }
            
            String cur = s.substring(l, r);      // 当前单词
            
            // 更新窗口内单词计数
            if (!window.containsKey(cur)) {
                window.put(cur, 1);
            } else {
                window.put(cur, window.get(cur) + 1);
            }
            
            // 更新债务：如果当前单词在目标中且未超出需要的数量
            if (map.containsKey(cur) && window.get(cur) <= map.get(cur)) {
                debt--;
            }
        }
        
        // 检查第一个窗口是否匹配
        if (debt == 0) {
            ans.add(start);
        }
        
        // 滑动窗口到字符串结尾
        int n = s.length();
        int allLen = wordLen * wordNum;  // 窗口总长度
        
        for (int next = start + wordLen; next + allLen <= n; next += wordLen) {
            // 移出窗口左侧的单词
            String out = s.substring(next - wordLen, next);
            window.put(out, window.get(out) - 1);
            
            // 更新债务：如果移出的单词在目标中且窗口中该单词不足
            if (map.containsKey(out) && window.get(out) < map.get(out)) {
                debt++;
            }
            
            // 移入窗口右侧的单词
            String in = s.substring(next + allLen - wordLen, next + allLen);
            
            // 更新窗口内单词计数
            if (!window.containsKey(in)) {
                window.put(in, 1);
            } else {
                window.put(in, window.get(in) + 1);
            }
            
            // 更新债务：如果移入的单词在目标中且未超出需要的数量
            if (map.containsKey(in) && window.get(in) <= map.get(in)) {
                debt--;
            }
            
            // 检查当前窗口是否匹配
            if (debt == 0) {
                ans.add(next);
            }
        }
    }

    /**
     * 找出所有可以由words中所有单词串联形成的子串起始位置
     * 
     * 算法思路：
     * 1. 预处理：统计words中每个单词的出现次数
     * 2. 枚举所有可能的起始位置模wordLen的结果（0到wordLen-1）
     * 3. 对每种起始位置，使用滑动窗口查找匹配的子串
     * 4. 收集所有匹配的起始位置
     * 
     * 为什么只需要wordLen种起始位置：
     * - 由于单词长度固定，相差wordLen的位置具有相同的单词边界
     * - 例如：wordLen=3时，位置0,3,6...具有相同的单词切分方式
     * 
     * @param s 原字符串
     * @param words 单词数组，所有单词长度相同
     * @return 所有匹配子串的起始位置列表
     */
    public static List<Integer> findSubstring(String s, String[] words) {
        List<Integer> ans = new ArrayList<>();
        
        // 边界情况检查
        if (s == null || s.length() == 0 || words == null || words.length == 0) {
            return ans;
        }
        
        int wordLen = words[0].length();  // 单词长度
        int wordNum = words.length;       // 单词数量
        
        // 字符串长度不足以包含所有单词
        if (s.length() < wordLen * wordNum) {
            return ans;
        }
        
        // 统计words中每个单词的出现次数
        HashMap<String, Integer> map = new HashMap<>();
        for (String key : words) {
            if (!map.containsKey(key)) {
                map.put(key, 1);
            } else {
                map.put(key, map.get(key) + 1);
            }
        }
        
        // 枚举所有可能的窗口开始位置（按单词边界对齐）
        for (int start = 0; start < wordLen; start++) {
            find(s, start, wordLen, wordNum, map, ans);
        }
        
        return ans;
    }
}
