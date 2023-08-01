package leetc.top;

import java.util.Stack;

public class P155_MinStack {
    private Stack<Integer> data;
    private Stack<Integer> min;
    public P155_MinStack() {
        data = new Stack<>();
        min = new Stack<>();
    }

    public void push(int x) {
        data.push(x);
        if (min.isEmpty()) {
            min.push(x);
        } else {
            min.push(Math.min(min.peek(), x));
        }
    }

    public void pop() {
        data.pop();
        min.pop();
    }
    public int top() {
        return data.peek();
    }
    public int getMin() {
        return min.peek();
    }
}
