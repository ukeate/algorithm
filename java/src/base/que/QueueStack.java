package base.que;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 用两个队列实现栈
 * 
 * 问题描述：
 * 使用两个队列来实现栈的基本操作：push(入栈)、poll(出栈)、peek(查看栈顶)、isEmpty(判空)
 * 
 * 算法思路：
 * 1. 使用两个队列：queue和help
 * 2. push操作：直接将元素加入queue
 * 3. pop/peek操作：
 *    - 将queue中除最后一个元素外的所有元素转移到help
 *    - queue中的最后一个元素就是栈顶元素
 *    - 处理完后交换queue和help的引用
 * 
 * 核心思想：
 * 利用队列的FIFO特性，通过倒腾元素的方式模拟栈的LIFO特性
 * 最后进入的元素总是被留在队列的最后，通过转移其他元素来访问它
 * 
 * 时间复杂度：
 * - push: O(1)
 * - pop/peek: O(n) - 需要转移n-1个元素
 * 
 * 空间复杂度：O(n) - 需要两个队列的空间
 */
public class QueueStack {
    /**
     * 用两个队列实现的栈类
     */
    public static class MyStack<T> {
        public Queue<T> queue;    // 主队列，存储所有元素
        public Queue<T> help;     // 辅助队列，用于元素转移
        
        /**
         * 构造函数
         */
        public MyStack() {
            this.queue = new LinkedList<>();
            this.help = new LinkedList<>();
        }
        
        /**
         * 入栈操作
         * 
         * 直接将元素加入主队列即可
         * 
         * @param val 要入栈的元素
         */
        public void push(T val) {
            queue.offer(val);
        }
        
        /**
         * 出栈操作
         * 
         * 算法步骤：
         * 1. 将主队列中除最后一个元素外的所有元素转移到辅助队列
         * 2. 主队列中剩下的最后一个元素就是栈顶元素，将其弹出
         * 3. 交换主队列和辅助队列的引用，为下次操作做准备
         * 
         * @return 栈顶元素
         */
        public T poll() {
            // 将除最后一个元素外的所有元素转移到辅助队列
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            // 获取栈顶元素（队列中的最后一个元素）
            T ans = queue.poll();
            // 交换两个队列的引用
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }

        /**
         * 查看栈顶元素（不移除）
         * 
         * 算法步骤：
         * 1. 将主队列中除最后一个元素外的所有元素转移到辅助队列
         * 2. 获取主队列中剩下的最后一个元素（栈顶）
         * 3. 将栈顶元素也放入辅助队列
         * 4. 交换主队列和辅助队列的引用
         * 
         * @return 栈顶元素
         */
        public T peek() {
            // 将除最后一个元素外的所有元素转移到辅助队列
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            // 获取栈顶元素
            T ans = queue.poll();
            // 将栈顶元素也放入辅助队列（因为peek不移除元素）
            help.offer(ans);
            // 交换两个队列的引用
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }
        
        /**
         * 判断栈是否为空
         * 
         * @return 栈为空返回true，否则返回false
         */
        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

    /**
     * 测试方法 - 验证用队列实现的栈的正确性
     */
    public static void main(String[] args) {
        int times = 100000;
        int max = 10000000;
        MyStack<Integer> myStack = new MyStack<>();
        Stack<Integer> stack = new Stack<>();
        
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            if (myStack.isEmpty()) {
                if (!stack.isEmpty()) {
                    System.out.println("Wrong");
                }
                // 栈为空时，只能进行push操作
                int num = (int) ((max + 1) * Math.random());
                myStack.push(num);
                stack.push(num);
            } else {
                // 随机选择操作类型
                if (Math.random() < 0.25) {
                    // 25%概率：push操作
                    int num = (int) ((max + 1) * Math.random());
                    myStack.push(num);
                    stack.push(num);
                } else if (Math.random() < 0.5) {
                    // 25%概率：peek操作
                    if (!myStack.peek().equals(stack.peek())) {
                        System.out.println("Wrong2");
                    }
                } else if (Math.random() < 0.75) {
                    // 25%概率：poll操作
                    if (!myStack.poll().equals(stack.pop())) {
                        System.out.println("Wrong3");
                    }
                } else {
                    // 25%概率：isEmpty操作
                    if (myStack.isEmpty() != stack.isEmpty()) {
                        System.out.println("Wrong4");
                    }
                }
            }
        }
        System.out.println("test end");
    }
}
