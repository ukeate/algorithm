package stack;

import org.junit.Test;

import java.util.LinkedList;

/**
 * Created by outrun on 2/22/16.
 */
public class MinStack {
    public LinkedList<Integer> stack = new LinkedList<Integer>();
    public LinkedList<Integer> minStack = new LinkedList<Integer>();

    public void push(int x) {
        if (stack.size() == 0) {
            stack.push(x);
            minStack.push(x);
        } else {
            stack.push(x);
            int min = minStack.peek();
            minStack.push(Math.min(x, min));
        }
    }

    public void pop() {
        stack.pop();
        minStack.pop();
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }

    @Test
    public void test() {
        MinStack ms = new MinStack();
        ms.push(5);
        ms.push(6);
        ms.push(7);
        ms.push(4);
        ms.push(9);
        System.out.println(ms.getMin());
        System.out.println(ms.top());
        ms.pop();
        ms.pop();
        System.out.println(ms.getMin());
        System.out.println(ms.top());
    }
}
