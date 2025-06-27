package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 22. 括号生成
 * https://leetcode.cn/problems/generate-parentheses/
 * 
 * 问题描述：
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 * 
 * 解题思路：
 * 使用深度优先搜索(DFS) + 回溯的方法生成所有合法的括号组合
 * 核心思想：
 * 1. 在任意位置，左括号的数量不能超过n
 * 2. 在任意位置，右括号的数量不能超过左括号的数量
 * 3. 当字符串长度达到2n时，说明生成了一个完整的括号组合
 * 
 * 算法流程：
 * 1. 使用递归函数process进行深度优先搜索
 * 2. 参数包括：当前字符数组、当前位置、已使用的左括号数量、剩余可用的左括号数量
 * 3. 在每个位置，尝试放置左括号或右括号：
 *    - 如果还有剩余的左括号，可以放置左括号
 *    - 如果已使用的左括号数量大于0，可以放置右括号
 * 4. 当到达字符串末尾时，将结果加入答案列表
 * 
 * 时间复杂度：O(4^n / √n)，这是第n个卡特兰数的渐近复杂度
 * 空间复杂度：O(n)，递归栈的深度为2n
 */
public class P22_GenerateParentheses {
    
    /**
     * 递归生成括号组合的核心方法
     * 
     * @param str 当前构建的字符数组
     * @param idx 当前处理的位置索引
     * @param leftNum 已经使用的左括号数量
     * @param leftRest 剩余可用的左括号数量
     * @param ans 存储所有合法括号组合的结果列表
     */
    private static void process(char[] str, int idx, int leftNum, int leftRest, List<String> ans) {
        // 基础情况：已经填满了整个字符数组，生成了一个完整的括号组合
        if (idx == str.length) {
            ans.add(String.valueOf(str));
            return;
        }
        
        // 情况1：如果还有剩余的左括号，可以放置左括号
        if (leftRest > 0) {
            str[idx] = '(';
            process(str, idx + 1, leftNum + 1, leftRest - 1, ans);
        }
        
        // 情况2：如果已使用的左括号数量大于0，可以放置右括号
        if (leftNum > 0) {
            str[idx] = ')';
            process(str, idx + 1, leftNum - 1, leftRest, ans);
        }
    }

    /**
     * 生成 n 对括号的所有合法组合
     * 
     * @param n 括号对数
     * @return 所有合法的括号组合列表
     */
    public static List<String> generateParenthesis(int n) {
        // 创建长度为2n的字符数组
        char[] str = new char[n << 1];
        List<String> ans = new ArrayList<>();
        
        // 开始递归生成，初始状态：位置0，已使用左括号0个，剩余左括号n个
        process(str, 0, 0, n, ans);
        return ans;
    }
}
