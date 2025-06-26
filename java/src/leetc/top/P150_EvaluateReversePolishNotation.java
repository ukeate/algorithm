package leetc.top;

import java.util.Stack;

/**
 * LeetCode 150. 逆波兰表达式求值 (Evaluate Reverse Polish Notation)
 * 
 * 问题描述：
 * 给你一个字符串数组 tokens ，表示一个根据逆波兰表示法表示的算术表达式。
 * 请你计算该表达式。返回一个表示表达式值的整数。
 * 
 * 逆波兰表达式（后缀表达式）规则：
 * - 有效的算符为 '+'、'-'、'*' 和 '/'
 * - 每个操作数（运算对象）都是整数
 * - 两个整数之间的除法只保留整数部分
 * - 给定逆波兰表达式总是有效的，且结果不会超出整数范围
 * 
 * 示例：
 * 输入：tokens = ["2","1","+","3","*"]
 * 输出：9
 * 解释：该算式转化为常见的中缀算术表达式为：((2 + 1) * 3) = 9
 * 
 * 解法思路：
 * 栈数据结构：
 * 1. 遍历表达式中的每个token
 * 2. 如果是数字，直接入栈
 * 3. 如果是运算符，从栈中弹出两个操作数进行运算
 * 4. 将运算结果入栈
 * 5. 最终栈中剩余的唯一元素就是表达式的值
 * 
 * 运算顺序：
 * - 注意操作数的顺序：num1 op num2（num1是倒数第二个弹出的）
 * - 例如：栈中有[5, 3]，遇到'-'，计算5-3=2，而不是3-5
 * 
 * 算法特点：
 * - 逆波兰表达式无需考虑运算符优先级
 * - 无需括号，从左到右线性计算
 * - 栈操作保证了正确的运算顺序
 * 
 * 时间复杂度：O(n) - 遍历所有token一次
 * 空间复杂度：O(n) - 栈空间，最坏情况下所有token都是数字
 * 
 * LeetCode链接：https://leetcode.com/problems/evaluate-reverse-polish-notation/
 */
public class P150_EvaluateReversePolishNotation {
    
    /**
     * 执行运算并将结果入栈
     * 
     * @param stack 操作数栈
     * @param op 运算符
     */
    private static void compute(Stack<Integer> stack, String op) {
        // 注意弹出顺序：后弹出的是第一个操作数
        int num2 = stack.pop();  // 第二个操作数
        int num1 = stack.pop();  // 第一个操作数
        int ans = 0;
        
        // 根据运算符执行相应运算
        switch(op) {
            case "+":
                ans = num1 + num2;
                break;
            case "-":
                ans = num1 - num2;  // 注意顺序：num1 - num2
                break;
            case "*":
                ans = num1 * num2;
                break;
            case "/":
                ans = num1 / num2;  // 注意顺序：num1 / num2，整数除法
                break;
        }
        
        // 将运算结果入栈
        stack.push(ans);
    }

    /**
     * 计算逆波兰表达式的值
     * 
     * @param tokens 逆波兰表达式的token数组
     * @return 表达式的计算结果
     */
    public static int evalRPN(String[] tokens) {
        Stack<Integer> stack = new Stack<>();
        
        // 遍历每个token
        for (String str : tokens) {
            if (str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/")) {
                // 运算符：弹出两个操作数进行运算
                compute(stack, str);
            } else {
                // 数字：直接入栈
                stack.push(Integer.valueOf(str));
            }
        }
        
        // 最终栈中只剩一个元素，即表达式的结果
        return stack.peek();
    }
}
