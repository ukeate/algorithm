package base.stack;

import java.util.Stack;

public class MinStack {
    public static class MyStack {
        private Stack<Integer> stackData;
        private Stack<Integer> stackMin;

        public MyStack() {
            this.stackData = new Stack<>();
            this.stackMin = new Stack<>();
        }

        public int getMin() {
            if (this.stackMin.isEmpty()) {
                throw new RuntimeException("stack is empty");
            }
            return this.stackMin.peek();
        }

        public void push(int num) {
            if (this.stackMin.isEmpty() || num <= this.getMin()) {
                this.stackMin.push(num);
            }
            this.stackData.push(num);
        }

        public int pop() {
            if (this.stackData.isEmpty()) {
                throw new RuntimeException("stack is empty");
            }
            int val = this.stackData.pop();
            if (val == this.getMin()) {
                this.stackMin.pop();
            }
            return val;
        }
    }

    public static void main(String[] args) {
        MyStack stack = new MyStack();
        stack.push(3);
        System.out.println(stack.getMin());
        stack.push(4);
        System.out.println(stack.getMin());
        stack.push(1);
        System.out.println(stack.getMin());
        System.out.println(stack.pop());
        System.out.println(stack.getMin());
    }

}
