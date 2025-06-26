package leetc.top;

import java.util.Stack;

/**
 * LeetCode 20. 有效的括号 (Valid Parentheses)
 * 
 * 问题描述：
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，
 * 判断字符串是否有效。
 * 
 * 有效字符串需满足：
 * 1. 左括号必须用相同类型的右括号闭合
 * 2. 左括号必须以正确的顺序闭合
 * 3. 每个右括号都有一个对应的相同类型的左括号
 * 
 * 示例：
 * 输入：s = "()"        输出：true
 * 输入：s = "()[]{}"    输出：true
 * 输入：s = "(]"        输出：false
 * 输入：s = "([)]"      输出：false
 * 输入：s = "{[]}"      输出：true
 * 
 * 解法思路：
 * 栈的经典应用：
 * 1. 栈的特性（后进先出）正好匹配括号的嵌套特性
 * 2. 遇到左括号时入栈，遇到右括号时出栈匹配
 * 3. 如果能成功匹配所有括号，最终栈应该为空
 * 
 * 算法步骤：
 * 1. 遍历字符串中的每个字符
 * 2. 如果是左括号 '('、'['、'{'，压入栈中
 * 3. 如果是右括号 ')'、']'、'}'：
 *    - 检查栈是否为空，为空则无法匹配，返回false
 *    - 弹出栈顶元素，检查是否与当前右括号匹配
 *    - 不匹配则返回false
 * 4. 遍历完成后，检查栈是否为空
 * 
 * 匹配规则：
 * - ')' 匹配 '('
 * - ']' 匹配 '['
 * - '}' 匹配 '{'
 * 
 * 边界情况：
 * - 空字符串：有效（返回true）
 * - 单个字符：无效（无法配对）
 * - 长度为奇数：无效（无法完全配对）
 * 
 * 时间复杂度：O(n) - 遍历字符串一次
 * 空间复杂度：O(n) - 栈空间，最坏情况下所有字符都是左括号
 * 
 * LeetCode链接：https://leetcode.com/problems/valid-parentheses/
 */
public class P20_ValidParentheses {
    
    /**
     * 判断括号字符串是否有效
     * 
     * @param s 括号字符串
     * @return 是否有效
     */
    public static boolean isValid(String s) {
        // 空字符串被认为是有效的
        if (s == null || s.length() == 0) {
            return true;
        }
        
        char[] str = s.toCharArray();
        Stack<Character> stack = new Stack<>();
        
        // 遍历每个字符
        for (int i = 0; i < str.length; i++) {
            char c = str[i];
            
            if (c == '(' || c == '[' || c == '{') {
                // 左括号：直接入栈
                stack.add(c);
            } else {
                // 右括号：需要匹配
                if (stack.isEmpty()) {
                    // 栈为空，无法匹配
                    return false;
                }
                
                char cc = stack.pop();  // 弹出栈顶的左括号
                
                // 检查括号类型是否匹配
                if ((c == ')' && cc != '(') || 
                    (c == ']' && cc != '[') || 
                    (c == '}' && cc != '{')) {
                    return false;
                }
            }
        }
        
        // 所有字符处理完后，栈应该为空
        return stack.isEmpty();
    }
}
