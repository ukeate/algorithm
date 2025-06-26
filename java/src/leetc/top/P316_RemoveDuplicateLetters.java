package leetc.top;

import java.util.Stack;

/**
 * LeetCode 316. 去除重复字母 (Remove Duplicate Letters)
 * 
 * 问题描述：
 * 给你一个字符串 s，请你去除字符串中重复的字母，使得每个字母只出现一次。
 * 需保证返回结果的字典序最小（要求不能打乱其他字符的相对位置）。
 * 
 * 示例：
 * 输入：s = "bcabc"
 * 输出："abc"
 * 
 * 输入：s = "cbacdcbc"
 * 输出："acdb"
 * 
 * 解法思路：
 * 贪心算法 + 单调栈：
 * 
 * 1. 预处理阶段：
 *    - 统计每个字符在字符串中的出现次数
 *    - 用于判断后续是否还有机会遇到某个字符
 * 
 * 2. 核心算法：
 *    - 使用栈维护当前的结果序列
 *    - 对每个字符，决定是否加入结果：
 *      * 如果已在结果中，跳过
 *      * 如果栈顶字符 > 当前字符 且 后续还会出现栈顶字符，则弹出栈顶
 *      * 将当前字符入栈
 * 
 * 3. 贪心策略：
 *    - 尽可能让较小的字符排在前面
 *    - 只有在确保后续还能遇到被弹出字符时，才执行弹出操作
 *    - 避免重复字符进入结果
 * 
 * 核心思想：
 * - 单调栈：维护字典序递增的字符序列
 * - 贪心选择：在保证所有字符都能出现的前提下，优先选择较小的字符
 * - 机会成本：只有在后续还有机会时，才牺牲当前的较大字符
 * 
 * 关键技巧：
 * - counts数组：跟踪每个字符的剩余出现次数
 * - inStack数组：快速判断字符是否已在结果中
 * - 栈结构：便于回退和调整结果序列
 * 
 * 时间复杂度：O(n) - 每个字符最多入栈出栈一次
 * 空间复杂度：O(1) - 栈最大长度为26（字母个数）
 * 
 * LeetCode链接：https://leetcode.com/problems/remove-duplicate-letters/
 */
public class P316_RemoveDuplicateLetters {
    
    /**
     * 去除重复字母，返回字典序最小的结果
     * 
     * 算法步骤：
     * 1. 统计每个字符的出现次数
     * 2. 遍历字符串，对每个字符：
     *    - 减少其剩余计数
     *    - 如果已在结果中，跳过
     *    - 否则，尝试优化栈顶（弹出后续还能遇到的较大字符）
     *    - 将当前字符入栈并标记
     * 3. 构建最终结果字符串
     * 
     * @param s 输入字符串
     * @return 去重后字典序最小的字符串
     */
    public String removeDuplicateLetters(String s) {
        int[] counts = new int[26];         // 统计每个字符的出现次数
        boolean[] inStack = new boolean[26]; // 标记字符是否已在结果栈中
        Stack<Character> stack = new Stack<>(); // 维护结果序列的栈
        
        // 第一遍遍历：统计每个字符的出现次数
        for (char c : s.toCharArray()) {
            counts[c - 'a']++;
        }
        
        // 第二遍遍历：构建最优结果
        for (char c : s.toCharArray()) {
            counts[c - 'a']--;  // 当前字符的剩余次数减1
            
            // 如果当前字符已在结果中，跳过
            if (inStack[c - 'a']) {
                continue;
            }
            
            // 贪心策略：尝试移除栈顶的较大字符
            // 条件：栈顶字符 > 当前字符 且 栈顶字符后续还会出现
            while (!stack.isEmpty() && 
                   stack.peek() > c && 
                   counts[stack.peek() - 'a'] > 0) {
                // 弹出栈顶字符，为当前较小字符让位
                char removed = stack.pop();
                inStack[removed - 'a'] = false; // 标记为不在栈中
            }
            
            // 将当前字符加入栈和结果
            stack.push(c);
            inStack[c - 'a'] = true; // 标记为已在栈中
        }
        
        // 构建最终结果字符串
        StringBuilder result = new StringBuilder();
        for (char c : stack) {
            result.append(c);
        }
        
        return result.toString();
    }
}