package base.recursive;

import java.util.Stack;

public class ReverseStack {
    private static int last(Stack<Integer> stack) {
        int rst = stack.pop();
        if (stack.isEmpty()) {
            return rst;
        }
        int last = last(stack);
        stack.push(rst);
        return last;
    }

    public static void reverse(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            return;
        }
        int last = last(stack);
        reverse(stack);
        stack.push(last);
    }

    public static void main(String[] args) {
        Stack<Integer> test = new Stack<>();
        test.push(1);
        test.push(2);
        test.push(3);
        test.push(4);
        test.push(5);
        reverse(test);
        while (!test.isEmpty()) {
            System.out.println(test.pop());
        }
    }
}
