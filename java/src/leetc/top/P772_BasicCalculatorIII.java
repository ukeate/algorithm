package leetc.top;

import java.util.LinkedList;

/**
 * LeetCode 772. 基本计算器 III (Basic Calculator III)
 * 
 * 问题描述：
 * 实现一个基本的计算器来计算简单的表达式字符串。
 * 表达式字符串只包含非负整数、算符 +、-、*、/ 、左括号 ( 和右括号 )。
 * 整数除法需要向下截断。
 * 
 * 示例：
 * - 输入："1 + 1"      输出：2
 * - 输入："6-4 / 2"    输出：4
 * - 输入："2*(5+5*2)/3+(6/2+8)"  输出：21
 * 
 * 解法思路：
 * 递归下降 + 栈处理：
 * 1. 使用递归处理括号的优先级
 * 2. 使用栈处理运算符的优先级（* / 优先于 + -）
 * 3. 将高优先级运算立即计算，低优先级运算放入栈中延后计算
 * 
 * 核心技巧：
 * - 运算符优先级：* / 立即计算，+ - 延后计算
 * - 括号处理：递归调用处理内部表达式
 * - 栈结构：存储待计算的数字和低优先级运算符
 * 
 * 时间复杂度：O(n) - 每个字符最多被处理一次
 * 空间复杂度：O(n) - 递归栈和计算栈的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/basic-calculator-iii/
 */
public class P772_BasicCalculatorIII {
    
    /**
     * 处理数字和高优先级运算符（* /）
     * 
     * 算法逻辑：
     * 1. 如果栈为空或栈顶是 + -，直接将数字入栈
     * 2. 如果栈顶是 * /，立即与前一个数字计算，结果入栈
     * 
     * 这样确保了 * / 的高优先级特性
     * 
     * @param que 计算栈，存储数字和运算符
     * @param num 当前要处理的数字
     */
    private static void addNum(LinkedList<String> que, int num) {
        if (!que.isEmpty()) {
            int cur = 0;
            String top = que.pollLast(); // 取出栈顶运算符
            
            if (top.equals("+") || top.equals("-")) {
                // 低优先级运算符，数字和运算符都重新入栈
                que.addLast(top);
            } else {
                // 高优先级运算符 * /，立即计算
                cur = Integer.parseInt(que.pollLast()); // 取出前一个数字
                num = top.equals("*") ? (cur * num) : (cur / num);
            }
        }
        // 将处理后的数字入栈
        que.addLast(String.valueOf(num));
    }

    /**
     * 计算栈中所有剩余的 + - 运算
     * 
     * 此时栈中只包含数字和 + - 运算符，按从左到右的顺序计算
     * 
     * @param que 计算栈
     * @return 最终计算结果
     */
    private static int getNum(LinkedList<String> que) {
        int res = 0;
        boolean add = true; // true表示加法，false表示减法
        String cur = null;
        int num = 0;
        
        while (!que.isEmpty()) {
            cur = que.pollFirst();
            if (cur.equals("+")) {
                add = true;
            } else if (cur.equals("-")) {
                add = false;
            } else {
                // 是数字，根据当前运算符号进行计算
                num = Integer.parseInt(cur);
                res += add ? num : (-num);
            }
        }
        
        return res;
    }

    /**
     * 递归解析表达式
     * 
     * 算法流程：
     * 1. 从左到右扫描字符
     * 2. 遇到数字：累积构建完整数字
     * 3. 遇到运算符：处理前面的数字，记录当前运算符
     * 4. 遇到左括号：递归处理括号内的子表达式
     * 5. 遇到右括号或字符串结束：结束当前层递归
     * 
     * @param str 表达式字符数组
     * @param i 当前处理位置
     * @return [计算结果, 处理结束位置]
     */
    private static int[] f(char[] str, int i) {
        LinkedList<String> que = new LinkedList<>(); // 计算栈
        int cur = 0;      // 当前构建的数字
        int[] bra = null; // 括号递归返回结果
        
        // 处理当前层的所有字符，直到遇到右括号或字符串结束
        while (i < str.length && str[i] != ')') {
            if (str[i] == ' ') {
                // 跳过空格
                i++;
                continue;
            }
            
            if (str[i] >= '0' && str[i] <= '9') {
                // 构建数字（处理多位数）
                cur = cur * 10 + str[i++] - '0';
            } else if (str[i] != '(') {
                // 遇到运算符，处理前面累积的数字
                addNum(que, cur);
                que.addLast(String.valueOf(str[i++])); // 运算符入栈
                cur = 0; // 重置数字累积器
            } else {
                // 遇到左括号，递归处理括号内表达式
                bra = f(str, i + 1);
                cur = bra[0];       // 括号内表达式的结果作为当前数字
                i = bra[1] + 1;     // 跳过右括号
            }
        }
        
        // 处理最后一个数字
        addNum(que, cur);
        
        // 返回 [当前表达式的计算结果, 当前处理位置]
        return new int[]{getNum(que), i};
    }

    /**
     * 计算表达式的值
     * 
     * @param str 表达式字符串
     * @return 计算结果
     */
    public static int calculate(String str) {
        return f(str.toCharArray(), 0)[0];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：简单加法
        String expr1 = "1 + 1";
        System.out.println("测试用例1:");
        System.out.println("表达式: \"" + expr1 + "\"");
        System.out.println("结果: " + calculate(expr1));
        System.out.println("期望: 2");
        System.out.println();
        
        // 测试用例2：混合运算
        String expr2 = "6-4 / 2";
        System.out.println("测试用例2:");
        System.out.println("表达式: \"" + expr2 + "\"");
        System.out.println("结果: " + calculate(expr2));
        System.out.println("期望: 4 (6 - (4/2) = 6 - 2 = 4)");
        System.out.println();
        
        // 测试用例3：复杂表达式
        String expr3 = "2*(5+5*2)/3+(6/2+8)";
        System.out.println("测试用例3:");
        System.out.println("表达式: \"" + expr3 + "\"");
        System.out.println("结果: " + calculate(expr3));
        System.out.println("期望: 21");
        System.out.println("计算过程: 2*(5+10)/3+(3+8) = 2*15/3+11 = 30/3+11 = 10+11 = 21");
        System.out.println();
        
        // 测试用例4：嵌套括号
        String expr4 = "((2+3)*4)";
        System.out.println("测试用例4 (嵌套括号):");
        System.out.println("表达式: \"" + expr4 + "\"");
        System.out.println("结果: " + calculate(expr4));
        System.out.println("期望: 20 ((2+3)*4 = 5*4 = 20)");
        System.out.println();
        
        // 测试用例5：除法截断
        String expr5 = "14/3*2";
        System.out.println("测试用例5 (除法截断):");
        System.out.println("表达式: \"" + expr5 + "\"");
        System.out.println("结果: " + calculate(expr5));
        System.out.println("期望: 8 (14/3=4向下截断, 4*2=8)");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 递归下降解析 + 栈处理运算符优先级");
        System.out.println("- 优先级处理: */立即计算，+-延后计算");
        System.out.println("- 括号处理: 递归调用，自然处理嵌套");
        System.out.println("- 时间复杂度: O(n) - 每个字符处理一次");
        System.out.println("- 空间复杂度: O(n) - 递归栈和计算栈");
        System.out.println("- 关键技巧: 高优先级运算符立即计算，避免优先级错误");
    }
}
