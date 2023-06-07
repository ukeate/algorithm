package base.que;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class DoubleEndsQueue2StackAndQueue {
    public static class Node<T> {
        public T val;
        public Node<T> last;
        public Node<T> next;
        public Node(T val) {
            this.val = val;
        }
    }

    public static class DoubleEndsQueue<T> {
        public Node<T> head;
        public Node<T> tail;
        public void addFromHead(T val) {
            Node<T> cur = new Node<>(val);
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                cur.next = head;
                head.last = cur;
                head = cur;
            }
        }

        public void addFromBottom(T val) {
            Node<T> cur = new Node<>(val);
            if (head == null) {
                head = cur;
                tail = cur;
            } else {
                cur.last = tail;
                tail.next = cur;
                tail = cur;
            }
        }

        public T popFromHead() {
            if (head == null) {
                return null;
            }
            Node<T> cur = head;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                head = head.next;
                cur.next = null;
                head.last = null;
            }
            return cur.val;
        }

        public T popFromBottom() {
            if (head == null) {
                return null;
            }
            Node<T> cur = tail;
            if (head == tail) {
                head = null;
                tail = null;
            } else {
                tail = tail.last;
                tail.next = null;
                cur.last = null;
            }
            return cur.val;
        }

        public boolean isEmpty() {
            return head == null;
        }
    }

    public static class MyStack<T> {
        private DoubleEndsQueue<T> queue;
        public MyStack() {
            queue = new DoubleEndsQueue<>();
        }
        public void push(T val) {
            queue.addFromHead(val);
        }
        public T pop() {
            return queue.popFromHead();
        }
    }

    public static class MyQueue<T> {
        private DoubleEndsQueue<T> queue;
        public MyQueue() {
            queue = new DoubleEndsQueue<>();
        }
        public void push(T val) {
            queue.addFromHead(val);
        }
        public T poll() {
            return queue.popFromBottom();
        }
    }

    private static boolean isEqual(Integer o1, Integer o2) {
        if (o1 == null && o2 == null) {
            return true;
        }
        if (o1 == null ^ o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    private static void print(DoubleEndsQueue q ) {
        if (q == null || q.head == null) {
            return;
        }
        Node cur = q.head;
        while (cur != null) {
            System.out.print(cur.val + ",");
            cur = cur.next;
        }
        System.out.println();
    }
    public static void main(String[] args) {
        int times = 10;
        int maxVal = 10000;
        int dataTimes = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            MyStack<Integer> myStack = new MyStack<>();
            MyQueue<Integer> myQueue = new MyQueue<>();
            Stack<Integer> stack = new Stack<>();
            Queue<Integer> queue = new LinkedList<>();
            for (int j = 0; j < dataTimes; j++) {
                int num = (int) ((maxVal + 1) * Math.random());
                if (stack.isEmpty()) {
                    myStack.push(num);
                    stack.push(num);
                } else {
                    if (Math.random() < 0.5) {
                        myStack.push(num);
                        stack.push(num);
                    } else {
                        int ans1 = myStack.pop();
                        int ans2 = stack.pop();
                        if (!isEqual(ans1, ans2)) {
                            System.out.println(ans1 + "|" + ans2);
                            print(myStack.queue);
                            System.out.println("Wrong");
                        }
                    }
                }
                int num2 = (int) ((maxVal + 1) * Math.random());
                if (stack.isEmpty()) {
                    myQueue.push(num2);
                    queue.offer(num2);
                } else {
                    if (Math.random() < 0.5) {
                        myQueue.push(num2);
                        queue.offer(num2);
                    } else {
                        if (!isEqual(myQueue.poll(), queue.poll())) {
                            System.out.println("Wrong2");
                        }
                    }
                }
            }
        }
        System.out.println("test end");
    }
}
