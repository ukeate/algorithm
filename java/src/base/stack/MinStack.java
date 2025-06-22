package base.stack;

import java.util.Stack;

/**
 * 最小栈实现 - 支持在O(1)时间内获取栈中最小元素的栈
 * 
 * 问题描述：
 * 设计一个栈，支持push、pop、top、getMin操作，且所有操作的时间复杂度都是O(1)
 * 
 * 解决方案：
 * 使用两个栈来实现：
 * 1. stackData：主栈，存储所有元素
 * 2. stackMin：辅助栈，存储每个时刻的最小值
 * 
 * 核心思想：
 * - 入栈时：如果新元素小于等于当前最小值，同时压入辅助栈
 * - 出栈时：如果弹出的元素等于当前最小值，辅助栈也要弹出
 * - 这样辅助栈的栈顶永远是当前栈中的最小值
 * 
 * 算法特点：
 * - 时间复杂度：所有操作都是O(1)
 * - 空间复杂度：O(n) - 最坏情况下辅助栈存储所有元素
 * 
 * 应用场景：
 * - 需要快速获取最小值的栈操作
 * - 表达式求值中需要追踪最小值
 * - 算法优化中的辅助数据结构
 */
public class MinStack {
    
    /**
     * 最小栈的实现类
     * 
     * 数据结构设计：
     * - stackData：主栈，正常的栈操作
     * - stackMin：辅助栈，维护当前栈中的最小值
     * 
     * 核心不变式：
     * stackMin.peek() 永远等于 stackData 中所有元素的最小值
     */
    public static class MyStack {
        private Stack<Integer> stackData;  // 主栈，存储实际数据
        private Stack<Integer> stackMin;   // 辅助栈，存储最小值

        /**
         * 构造函数 - 初始化两个栈
         */
        public MyStack() {
            this.stackData = new Stack<>();
            this.stackMin = new Stack<>();
        }

        /**
         * 获取栈中的最小元素
         * 
         * 时间复杂度：O(1)
         * 
         * @return 栈中的最小值
         * @throws RuntimeException 当栈为空时
         */
        public int getMin() {
            if (this.stackMin.isEmpty()) {
                throw new RuntimeException("栈为空，无法获取最小值");
            }
            return this.stackMin.peek();
        }

        /**
         * 将元素压入栈
         * 
         * 算法逻辑：
         * 1. 新元素总是压入主栈
         * 2. 如果辅助栈为空，或新元素小于等于当前最小值，也压入辅助栈
         * 3. 这样确保辅助栈顶永远是当前最小值
         * 
         * 为什么是"小于等于"而不是"小于"？
         * 考虑这种情况：push(1), push(1), pop()
         * 如果用"小于"，第二个1不会入辅助栈，pop后最小值就丢失了
         * 
         * 举例说明：
         * push(3): stackData=[3], stackMin=[3]
         * push(4): stackData=[3,4], stackMin=[3]  (4>3,不入辅助栈)
         * push(1): stackData=[3,4,1], stackMin=[3,1]  (1<3,入辅助栈)
         * push(1): stackData=[3,4,1,1], stackMin=[3,1,1]  (1=1,入辅助栈)
         * 
         * 时间复杂度：O(1)
         * 
         * @param num 要压入的元素
         */
        public void push(int num) {
            // 新元素总是压入主栈
            this.stackData.push(num);
            
            // 如果辅助栈为空或新元素小于等于当前最小值，也压入辅助栈
            if (this.stackMin.isEmpty() || num <= this.getMin()) {
                this.stackMin.push(num);
            }
        }

        /**
         * 弹出栈顶元素
         * 
         * 算法逻辑：
         * 1. 弹出主栈的栈顶元素
         * 2. 如果弹出的元素等于当前最小值，辅助栈也要弹出
         * 3. 这样确保辅助栈顶仍然是剩余元素的最小值
         * 
         * 举例说明（接上面的例子）：
         * pop(): 弹出1，stackData=[3,4,1], stackMin=[3,1]  (1是最小值，辅助栈也弹出)
         * pop(): 弹出1，stackData=[3,4], stackMin=[3]      (1是最小值，辅助栈也弹出)
         * pop(): 弹出4，stackData=[3], stackMin=[3]        (4不是最小值，辅助栈不变)
         * 
         * 时间复杂度：O(1)
         * 
         * @return 弹出的元素
         * @throws RuntimeException 当栈为空时
         */
        public int pop() {
            if (this.stackData.isEmpty()) {
                throw new RuntimeException("栈为空，无法执行出栈操作");
            }
            
            // 弹出主栈的栈顶元素
            int val = this.stackData.pop();
            
            // 如果弹出的元素是当前最小值，辅助栈也要弹出
            if (val == this.getMin()) {
                this.stackMin.pop();
            }
            
            return val;
        }
    }

    /**
     * 测试方法 - 演示最小栈的功能
     */
    public static void main(String[] args) {
        System.out.println("=== 最小栈功能测试 ===");
        
        MyStack stack = new MyStack();
        
        System.out.println("1. 测试push和getMin操作：");
        
        System.out.println("push(3)");
        stack.push(3);
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 3
        
        System.out.println("push(4)");
        stack.push(4);
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 3
        
        System.out.println("push(1)");
        stack.push(1);
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 1
        
        System.out.println("\n2. 测试pop操作：");
        
        System.out.println("pop(): " + stack.pop());         // 输出: 1
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 3
        
        System.out.println("pop(): " + stack.pop());         // 输出: 4
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 3
        
        System.out.println("\n3. 测试重复最小值的情况：");
        stack.push(3);  // 再次压入3
        stack.push(2);
        stack.push(2);  // 压入重复的最小值
        
        System.out.println("push(3), push(2), push(2)");
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 2
        
        System.out.println("pop(): " + stack.pop());         // 输出: 2
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 2 (仍然是2)
        
        System.out.println("pop(): " + stack.pop());         // 输出: 2
        System.out.println("当前最小值: " + stack.getMin());  // 输出: 3
        
        System.out.println("\n=== 测试完成 ===");
    }
}
