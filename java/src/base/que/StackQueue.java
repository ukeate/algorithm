package base.que;

import java.util.Stack;

public class StackQueue {
    public static class MyQueue {
        public Stack<Integer> stackPush;
        public Stack<Integer> stackPop;
        public MyQueue() {
            this.stackPush = new Stack<>();
            this.stackPop = new Stack<>();
        }
        private void pushToPop() {
            if (stackPop.empty()) {
                while (!stackPush.empty()) {
                    stackPop.push(stackPush.pop());
                }
            }
        }

        public void add(int val) {
            stackPush.push(val);
            pushToPop();
        }

        public int poll() {
            if (stackPop.empty() && stackPush.empty()) {
                throw new RuntimeException("Queue is empty");
            }
            pushToPop();
            return stackPop.pop();
        }

        public int peek() {
            if (stackPop.empty() && stackPush.empty()) {
                throw new RuntimeException("Queue is empty");
            }
            pushToPop();
            return stackPop.peek();
        }
    }

    public static void main(String[] args) {
        MyQueue que = new MyQueue();
        que.add(1);
        que.add(2);
        que.add(3);
        System.out.println(que.peek());
        System.out.println(que.poll());
        System.out.println(que.peek());
        System.out.println(que.poll());
        System.out.println(que.peek());
        System.out.println(que.poll());
    }
}
