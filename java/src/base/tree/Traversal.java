package base.tree;

import java.util.Stack;

public class Traversal {

    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    public static void traversal(Node head) {
        if (head == null) {
            return;
        }
        traversal(head.left);
        traversal(head.right);
    }

    public static void pre(Node cur) {
        if (cur == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(cur);
        while (!stack.isEmpty()) {
            cur = stack.pop();
            System.out.print(cur.val + ",");
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
    }

    public static void in(Node cur) {
        if (cur == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        while (cur != null || !stack.isEmpty()) {
            // 最左到底
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                // 弹出时打印
                cur = stack.pop();
                System.out.print(cur.val + ",");
                cur = cur.right;
            }
        }
    }

    public static void pos1(Node cur) {
        if (cur == null) {
            return;
        }
        Stack<Node> s1 = new Stack<>();
        Stack<Node> s2 = new Stack<>();
        s1.push(cur);
        while (!s1.isEmpty()) {
            cur = s1.pop();
            s2.push(cur);
            if (cur.left != null) {
                s1.push(cur.left);
            }
            if (cur.right != null) {
                s1.push(cur.right);
            }
        }
        while (!s2.isEmpty()) {
            System.out.print(s2.pop().val + ",");
        }
    }

    public static void pos2(Node h) {
        if (h == null) {
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(h);
        Node c = null;
        while (!stack.isEmpty()) {
            c = stack.peek();
            if (c.left != null && h != c.left && h != c.right) {
                stack.push(c.left);
            } else if (c.right != null && h != c.right) {
                stack.push(c.right);
            } else {
                System.out.print(stack.pop().val + ",");
                h = c;
            }
        }
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        pre(head);
        System.out.println("========");
        in(head);
        System.out.println("========");
        pos1(head);
        System.out.println("========");
        pos2(head);
        System.out.println("========");
    }
}
