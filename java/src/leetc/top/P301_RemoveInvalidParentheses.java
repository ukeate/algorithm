package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 301. 移除无效的括号 (Remove Invalid Parentheses)
 * 
 * 问题描述：
 * 给你一个由若干括号和字母组成的字符串 s ，删除最小数量的无效括号，使得输入的字符串有效。
 * 返回所有可能的结果。答案可以按任意顺序返回。
 * 
 * 有效括号字符串需满足：
 * 1. 左括号必须有相应的右括号来匹配
 * 2. 右括号必须有相应的左括号来匹配
 * 3. 左括号必须在对应的右括号之前
 * 
 * 示例：
 * 输入：s = "()())"
 * 输出：["()()", "(())"]
 * 解释：删除一个 ')' 即可
 * 
 * 输入：s = "((("
 * 输出：[""]
 * 解释：删除所有的 '(' 即可
 * 
 * 解法思路：
 * DFS递归回溯 + 剪枝优化：
 * 
 * 1. 预处理阶段：
 *    - 统计需要删除的左括号数量(leftRemove)
 *    - 统计需要删除的右括号数量(rightRemove)
 * 
 * 2. DFS搜索阶段：
 *    - 对每个字符，选择保留或删除
 *    - 维护当前的左右括号删除数和当前匹配状态
 *    - 使用剪枝避免重复和无效路径
 * 
 * 3. 剪枝策略：
 *    - 如果删除数量超过预计，剪枝
 *    - 如果右括号数量超过左括号，剪枝
 *    - 跳过连续相同字符避免重复结果
 * 
 * 核心思想：
 * - 先确定最少删除数量，然后枚举所有删除方案
 * - 使用DFS探索所有可能的删除组合
 * - 通过剪枝策略大幅减少搜索空间
 * 
 * 时间复杂度：O(2^n) - 最坏情况下需要尝试所有删除组合
 * 空间复杂度：O(n) - 递归栈深度
 * 
 * LeetCode链接：https://leetcode.com/problems/remove-invalid-parentheses/
 */
public class P301_RemoveInvalidParentheses {
    
    /**
     * 移除无效括号，返回所有可能的有效结果
     * 
     * @param s 输入字符串
     * @return 所有可能的有效括号字符串列表
     */
    public List<String> removeInvalidParentheses(String s) {
        List<String> ans = new ArrayList<>();
        
        // 预处理：计算需要删除的左右括号数量
        int leftRemove = 0;   // 需要删除的左括号数量
        int rightRemove = 0;  // 需要删除的右括号数量
        
        // 第一遍扫描：计算多余的括号数量
        for (char cha : s.toCharArray()) {
            if (cha == '(') {
                leftRemove++;           // 遇到左括号，计数+1
            } else if (cha == ')') {
                if (leftRemove > 0) {
                    leftRemove--;       // 右括号与左括号匹配，左括号计数-1
                } else {
                    rightRemove++;      // 多余的右括号
                }
            }
        }
        
        // DFS搜索所有可能的删除方案
        dfs(s, 0, leftRemove, rightRemove, 0, new StringBuilder(), ans);
        return ans;
    }
    
    /**
     * DFS递归搜索有效的括号组合
     * 
     * @param s 原始字符串
     * @param index 当前处理的字符位置
     * @param leftRemove 还需删除的左括号数量
     * @param rightRemove 还需删除的右括号数量
     * @param leftMinusRight 当前左括号数量减去右括号数量（匹配状态）
     * @param path 当前构建的路径
     * @param ans 结果集合
     */
    public void dfs(String s, int index, int leftRemove, int rightRemove, int leftMinusRight, 
                   StringBuilder path, List<String> ans) {
        
        // 递归终止条件：处理完所有字符
        if (index == s.length()) {
            // 检查是否满足有效条件：所有需删除的括号都删除了，且左右括号匹配
            if (leftRemove == 0 && rightRemove == 0 && leftMinusRight == 0) {
                ans.add(path.toString());
            }
            return;
        }
        
        char cha = s.charAt(index);
        int pathSize = path.length();
        
        // 决策1：删除当前字符
        if ((cha == '(' && leftRemove > 0) || (cha == ')' && rightRemove > 0)) {
            // 剪枝：跳过连续相同的括号，避免产生重复结果
            // 例如：对于"((("，只在第一个'('位置尝试删除
            if (index == 0 || s.charAt(index - 1) != cha) {
                // 递归：删除当前括号
                dfs(s, index + 1, 
                   leftRemove - (cha == '(' ? 1 : 0),    // 如果删除左括号，leftRemove-1
                   rightRemove - (cha == ')' ? 1 : 0),   // 如果删除右括号，rightRemove-1
                   leftMinusRight, path, ans);
            }
        }
        
        // 决策2：保留当前字符
        path.append(cha);
        if (cha != '(' && cha != ')') {
            // 普通字符：直接保留，继续处理下一个字符
            dfs(s, index + 1, leftRemove, rightRemove, leftMinusRight, path, ans);
        } else if (cha == '(') {
            // 左括号：增加leftMinusRight计数
            dfs(s, index + 1, leftRemove, rightRemove, leftMinusRight + 1, path, ans);
        } else if (cha == ')' && leftMinusRight > 0) {
            // 右括号：只有当有多余的左括号时才能匹配（剪枝）
            dfs(s, index + 1, leftRemove, rightRemove, leftMinusRight - 1, path, ans);
        }
        
        // 回溯：恢复path状态
        path.setLength(pathSize);
    }
}
