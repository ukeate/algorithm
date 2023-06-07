package base.que;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class QueueStack {
    public static class MyStack<T> {
        public Queue<T> queue;
        public Queue<T> help;
        public MyStack() {
            this.queue = new LinkedList<>();
            this.help = new LinkedList<>();
        }
        public void push(T val) {
            queue.offer(val);
        }
        public T poll() {
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            T ans = queue.poll();
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }

        public T peek() {
            while (queue.size() > 1) {
                help.offer(queue.poll());
            }
            T ans = queue.poll();
            help.offer(ans);
            Queue<T> tmp = queue;
            queue = help;
            help = tmp;
            return ans;
        }
        public boolean isEmpty() {
            return queue.isEmpty();
        }
    }

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
                int num = (int) ((max + 1) * Math.random());
                myStack.push(num);
                stack.push(num);
            } else {
                if (Math.random() < 0.25) {
                    int num = (int) ((max + 1) * Math.random());
                    myStack.push(num);
                    stack.push(num);
                } else if (Math.random() < 0.5) {
                    if (!myStack.peek().equals(stack.peek())) {
                        System.out.println("Wrong2");
                    }
                } else if (Math.random() < 0.75) {
                    if (!myStack.poll().equals(stack.pop())) {
                        System.out.println("Wrong3");
                    }
                } else {
                    if (myStack.isEmpty() != stack.isEmpty()) {
                        System.out.println("Wrong4");
                    }
                }
            }
        }
        System.out.println("test end");
    }
}
