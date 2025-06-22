package base.recursive;

import java.util.Stack;

/**
 * 递归反转栈 - 仅使用递归函数和栈的基本操作反转栈
 * 
 * 问题描述：
 * 给定一个栈，要求只使用递归函数和栈的基本操作（push、pop、isEmpty）
 * 来反转这个栈，不能使用任何其他数据结构
 * 
 * 核心思想：
 * 1. 先递归地取出栈底元素
 * 2. 再递归地反转剩余部分
 * 3. 最后将栈底元素压入反转后的栈
 * 
 * 算法分解：
 * - last(stack)：递归取出并返回栈底元素，保持其他元素相对顺序不变
 * - reverse(stack)：递归反转整个栈
 * 
 * 时间复杂度：O(n²)
 * - 每层递归都要调用last函数，last函数的复杂度为O(n)
 * - 总共有n层递归，所以总复杂度为O(n²)
 * 
 * 空间复杂度：O(n²)
 * - 递归调用栈的深度为O(n)
 * - 每层递归中last函数也有O(n)的递归深度
 * 
 * 应用场景：
 * - 递归算法设计的经典案例
 * - 函数式编程思想的体现
 * - 算法面试中的高频题目
 */
public class ReverseStack {
    /**
     * 递归获取并移除栈底元素
     * 
     * 算法思路：
     * 1. 如果栈只有一个元素，直接返回这个元素
     * 2. 否则，弹出栈顶元素，递归获取剩余栈的栈底元素
     * 3. 将之前弹出的栈顶元素重新压入栈中
     * 4. 返回获取到的栈底元素
     * 
     * 关键观察：
     * - 这个函数会"挖出"栈底元素，同时保持其他元素的相对顺序
     * - 栈的结构在递归返回后会恢复到移除栈底元素前的状态
     * 
     * 例子：栈从顶到底为 [3, 2, 1]
     * - 调用last(stack)会返回1
     * - 调用后栈变为 [3, 2]
     * 
     * @param stack 目标栈
     * @return 栈底元素
     */
    private static int last(Stack<Integer> stack) {
        int rst = stack.pop();              // 弹出栈顶元素
        if (stack.isEmpty()) {
            // 递归边界：如果栈空了，说明刚才弹出的就是栈底元素
            return rst;
        }
        // 递归获取剩余栈的栈底元素
        int last = last(stack);
        // 将之前弹出的元素重新压入栈中（恢复栈结构）
        stack.push(rst);
        return last;                        // 返回真正的栈底元素
    }

    /**
     * 递归反转栈
     * 
     * 算法思路：
     * 1. 如果栈为空，直接返回
     * 2. 获取并移除栈底元素
     * 3. 递归反转剩余的栈
     * 4. 将栈底元素压入反转后的栈顶
     * 
     * 工作过程：
     * 原栈（从顶到底）：[5, 4, 3, 2, 1]
     * 
     * 第1次调用reverse：
     * - 取出栈底元素1，栈变为[5, 4, 3, 2]
     * - 递归反转[5, 4, 3, 2]
     * - 将1压入反转后的栈顶
     * 
     * 第2次调用reverse：
     * - 取出栈底元素2，栈变为[5, 4, 3]
     * - 递归反转[5, 4, 3]
     * - 将2压入反转后的栈顶
     * 
     * ... 依此类推
     * 
     * 最终结果：[1, 2, 3, 4, 5]
     * 
     * @param stack 要反转的栈
     */
    public static void reverse(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            // 递归边界：空栈无需反转
            return;
        }
        // 步骤1：获取并移除栈底元素
        int last = last(stack);
        
        // 步骤2：递归反转剩余部分
        reverse(stack);
        
        // 步骤3：将原栈底元素压入现在的栈顶
        // 此时栈已经反转，原栈底元素应该在新栈的顶部
        stack.push(last);
    }

    /**
     * 测试方法 - 演示栈反转功能
     */
    public static void main(String[] args) {
        System.out.println("=== 递归反转栈演示 ===");
        
        // 创建测试栈
        Stack<Integer> test = new Stack<>();
        test.push(1);
        test.push(2);
        test.push(3);
        test.push(4);
        test.push(5);
        
        // 显示原栈内容
        System.out.println("原始栈（从栈顶到栈底）：");
        Stack<Integer> temp = new Stack<>();
        while (!test.isEmpty()) {
            int val = test.pop();
            System.out.print(val + " ");
            temp.push(val);
        }
        System.out.println();
        
        // 恢复栈
        while (!temp.isEmpty()) {
            test.push(temp.pop());
        }
        
        // 反转栈
        System.out.println("\n正在反转栈...");
        reverse(test);
        
        // 显示反转后的栈内容
        System.out.println("反转后栈（从栈顶到栈底）：");
        while (!test.isEmpty()) {
            System.out.print(test.pop() + " ");
        }
        System.out.println();
        
        // 算法复杂度说明
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度：O(n²)");
        System.out.println("- 每次调用reverse都需要调用last函数");
        System.out.println("- last函数需要O(n)时间获取栈底元素");
        System.out.println("- 总共有n次reverse调用，因此总时间复杂度为O(n²)");
        
        System.out.println("\n空间复杂度：O(n²)");
        System.out.println("- reverse函数的递归深度为O(n)");
        System.out.println("- 每层reverse调用中，last函数也有O(n)的递归深度");
        System.out.println("- 总空间复杂度为O(n²)");
        
        System.out.println("\n核心思想：");
        System.out.println("- 利用递归的特性，将栈底元素'浮'到栈顶");
        System.out.println("- 这是一个经典的递归设计模式");
        System.out.println("- 体现了递归在数据结构操作中的强大能力");
    }
}
