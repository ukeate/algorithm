package basic.c45;

import java.util.LinkedList;

/**
 * 表达式计算器
 * 
 * 问题描述：
 * 给定一个包含整数、+、-、*、/、(、)的数学表达式字符串
 * 计算并返回表达式的结果
 * 
 * 支持的操作：
 * - 整数（可以是多位数）
 * - 四则运算：+、-、*、/
 * - 括号：(、)
 * - 负数（如-3*4）
 * 
 * 算法思路：
 * 递归下降解析 + 运算符优先级处理
 * 1. 使用递归处理括号的嵌套
 * 2. 使用队列分离不同优先级的运算
 * 3. 先处理高优先级运算（*、/），再处理低优先级运算（+、-）
 * 
 * 核心设计：
 * - cal方法：递归处理括号和数字解析
 * - addNum方法：处理乘除运算（立即计算）
 * - getNum方法：处理加减运算（最后计算）
 * 
 * 时间复杂度：O(n) - 每个字符最多访问一次
 * 空间复杂度：O(n) - 递归栈 + 队列空间
 * 
 * @author 算法学习
 */
public class ExpressionCompute {

    /**
     * 处理队列中的加减运算
     * 队列中此时只包含数字和+、-运算符
     * 
     * @param que 包含数字和+、-运算符的队列
     * @return 加减运算的最终结果
     * 
     * 算法思路：
     * 从左到右扫描队列，遇到+号则加上下一个数字，遇到-号则减去下一个数字
     * 这个方法处理的是最低优先级的运算
     */
    private static int getNum(LinkedList<String> que) {
        int res = 0;        // 累积结果
        boolean add = true; // 当前运算是加法还是减法
        String cur = null;  // 当前处理的元素
        int num = 0;        // 当前数字
        
        while (!que.isEmpty()) {
            cur = que.pollFirst();
            if (cur.equals("+")) {
                add = true;  // 下一个数字要加
            } else if (cur.equals("-")) {
                add = false; // 下一个数字要减
            } else {
                // 当前元素是数字
                num = Integer.valueOf(cur);
                res += add ? num : (-num);
            }
        }
        return res;
    }

    /**
     * 将数字添加到队列中，并处理乘除运算
     * 
     * @param que 运算队列
     * @param num 要添加的数字
     * 
     * 算法思路：
     * 如果队列最后一个运算符是*或/，则立即与前一个数字计算
     * 如果是+或-，则直接添加到队列末尾，等待后续处理
     * 这样确保了乘除运算的优先级高于加减运算
     */
    private static void addNum(LinkedList<String> que, int num) {
        if (!que.isEmpty()) {
            int cur = 0;
            String top = que.pollLast(); // 取出最后一个元素（可能是运算符）
            
            if (top.equals("+") || top.equals("-")) {
                // 如果是加减号，直接放回队列
                que.addLast(top);
            } else {
                // 如果是乘除号，立即计算
                cur = Integer.valueOf(que.pollLast()); // 取出被乘/除数
                num = top.equals("*") ? (cur * num) : (cur / num);
            }
        }
        // 将结果数字添加到队列末尾
        que.addLast(String.valueOf(num));
    }

    /**
     * 递归计算表达式
     * 
     * @param str 表达式字符数组
     * @param i 当前解析位置
     * @return [计算结果, 解析结束位置]
     * 
     * 算法步骤：
     * 1. 遍历字符数组，识别数字、运算符、括号
     * 2. 遇到数字：累积多位数字
     * 3. 遇到运算符：将当前数字添加到队列，运算符也入队
     * 4. 遇到左括号：递归计算括号内表达式
     * 5. 遇到右括号：结束当前层递归
     * 6. 最后处理队列中的所有运算
     */
    private static int[] cal(char[] str, int i) {
        LinkedList<String> que = new LinkedList<>(); // 运算队列
        int cur = 0;    // 当前正在构建的数字
        int[] bra = null; // 括号递归的返回值
        
        // 遍历字符，直到遇到右括号或字符串结束
        while (i < str.length && str[i] != ')') {
            if (str[i] >= '0' && str[i] <= '9') {
                // 数字字符：构建多位数
                cur = cur * 10 + str[i++] - '0';
            } else if (str[i] != '(') {
                // 运算符：将当前数字入队，运算符也入队
                addNum(que, cur);
                que.addLast(String.valueOf(str[i++]));
                cur = 0; // 重置当前数字
            } else {
                // 左括号：递归计算括号内表达式
                bra = cal(str, i + 1);
                cur = bra[0];    // 括号内的计算结果
                i = bra[1] + 1;  // 跳过右括号
            }
        }
        
        // 处理最后一个数字
        addNum(que, cur);
        
        // 返回[计算结果, 当前解析位置]
        return new int[] {getNum(que), i};
    }

    /**
     * 计算表达式的公共接口
     * 
     * @param str 表达式字符串
     * @return 计算结果
     */
    public static int cal(String str) {
        return cal(str.toCharArray(), 0)[0];
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：包含括号的复杂表达式
        String exp = "48*((70-65)-43)+8*1";
        System.out.println("表达式: " + exp);
        System.out.println("结果: " + cal(exp));
        System.out.println();

        // 测试用例2：混合运算
        exp = "4*(6+78)+53-9/2+45*8";
        System.out.println("表达式: " + exp);
        System.out.println("结果: " + cal(exp));
        System.out.println();

        // 测试用例3：运算符优先级
        exp = "10-5*3";
        System.out.println("表达式: " + exp);
        System.out.println("结果: " + cal(exp) + " (应该是-5，不是15)");
        System.out.println();

        // 测试用例4：负数
        exp = "-3*4";
        System.out.println("表达式: " + exp);
        System.out.println("结果: " + cal(exp));
        System.out.println();

        // 测试用例5：简单混合运算
        exp = "3+1*4";
        System.out.println("表达式: " + exp);
        System.out.println("结果: " + cal(exp) + " (应该是7，不是16)");
        
        // 额外测试用例
        System.out.println("\n=== 额外测试 ===");
        String[] tests = {
            "1+2*3",           // 7
            "(1+2)*3",         // 9  
            "2*3+4*5",         // 26
            "100/4/5",         // 5
            "(8-6)*3+4",       // 10
            "0*100+50"         // 50
        };
        
        for (String test : tests) {
            System.out.println(test + " = " + cal(test));
        }
    }
}
