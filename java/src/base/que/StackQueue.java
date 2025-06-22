package base.que;

import java.util.Stack;

/**
 * 用两个栈实现队列
 * 
 * 问题描述：
 * 使用两个栈来实现队列的基本操作：add(入队)、poll(出队)、peek(查看队头)
 * 
 * 算法思路：
 * 1. 使用两个栈：stackPush(负责入队)、stackPop(负责出队)
 * 2. 入队操作：直接将元素压入stackPush
 * 3. 出队操作：从stackPop弹出，如果stackPop为空，先将stackPush的所有元素转移过来
 * 4. 关键规则：只有当stackPop为空时，才能从stackPush向stackPop转移元素
 * 
 * 时间复杂度：
 * - add: O(1)
 * - poll/peek: 均摊O(1)，最坏O(n)
 * 
 * 空间复杂度：O(n) - 需要两个栈的空间
 */
public class StackQueue {
    /**
     * 用两个栈实现的队列类
     */
    public static class MyQueue {
        public Stack<Integer> stackPush;    // 负责入队的栈
        public Stack<Integer> stackPop;     // 负责出队的栈
        
        /**
         * 构造函数
         */
        public MyQueue() {
            this.stackPush = new Stack<>();
            this.stackPop = new Stack<>();
        }
        
        /**
         * 数据转移函数 - 将stackPush中的所有元素转移到stackPop
         * 
         * 转移规则：
         * 1. 只有当stackPop为空时才能转移
         * 2. 必须一次性将stackPush中的所有元素都转移完
         * 
         * 这样保证了队列的FIFO特性：
         * 最先进入stackPush的元素会最后被转移到stackPop，
         * 因此会最先从stackPop弹出
         */
        private void pushToPop() {
            if (stackPop.empty()) {
                while (!stackPush.empty()) {
                    stackPop.push(stackPush.pop());
                }
            }
        }

        /**
         * 入队操作
         * 
         * @param val 要入队的元素
         */
        public void add(int val) {
            stackPush.push(val);
            pushToPop();    // 尝试转移数据
        }

        /**
         * 出队操作
         * 
         * @return 队头元素
         * @throws RuntimeException 队列为空时抛出异常
         */
        public int poll() {
            if (stackPop.empty() && stackPush.empty()) {
                throw new RuntimeException("Queue is empty");
            }
            pushToPop();    // 确保stackPop有数据
            return stackPop.pop();
        }

        /**
         * 查看队头元素（不移除）
         * 
         * @return 队头元素
         * @throws RuntimeException 队列为空时抛出异常
         */
        public int peek() {
            if (stackPop.empty() && stackPush.empty()) {
                throw new RuntimeException("Queue is empty");
            }
            pushToPop();    // 确保stackPop有数据
            return stackPop.peek();
        }
    }

    /**
     * 测试方法 - 验证队列实现的正确性
     */
    public static void main(String[] args) {
        MyQueue que = new MyQueue();
        
        // 测试入队操作
        que.add(1);
        que.add(2);
        que.add(3);
        
        // 测试peek操作
        System.out.println("队头元素: " + que.peek());  // 应该是1
        
        // 测试出队操作
        System.out.println("出队: " + que.poll());      // 应该是1
        System.out.println("队头元素: " + que.peek());  // 应该是2
        System.out.println("出队: " + que.poll());      // 应该是2
        System.out.println("队头元素: " + que.peek());  // 应该是3
        System.out.println("出队: " + que.poll());      // 应该是3
    }
}
