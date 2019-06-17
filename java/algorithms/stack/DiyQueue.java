package stack;

import org.junit.Test;

import java.util.Stack;

/**
 * 两个栈实现一个队列
 * Created by outrun on 2/22/16.
 */
public class DiyQueue {
    public Stack<Integer> stack1 = new Stack<Integer>();
    public Stack<Integer> stack2 = new Stack<Integer>();
    public int size;

    public void push (int x) {
        stack1.push(x);
        size++;
    }

    public void pop() {
        if (!stack2.isEmpty()) {
            stack2.pop();
        } else {
            swap();
            stack2.pop();
        }
        size--;
    }

    private void swap() {
        while (!stack1.isEmpty()) {
            stack2.push(stack1.pop());
        }
    }

    public int peek() {
        if (!stack2.isEmpty()) {
            return stack2.peek();
        } else {
            swap();
            return stack2.peek();
        }
    }

    public boolean empty() {
        return size == 0;
    }

    @Test
    public void test() {
        DiyQueue queue = new DiyQueue();

        queue.push(1);
        queue.push(2);
        queue.push(3);
        System.out.println(queue.peek());
        queue.pop();
        System.out.println(queue.peek());
        queue.pop();
        System.out.println(queue.peek());
        queue.pop();
    }
}
