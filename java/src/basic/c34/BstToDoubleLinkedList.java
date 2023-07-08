package basic.c34;

import java.util.LinkedList;
import java.util.Queue;

public class BstToDoubleLinkedList {
    private static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node(int v) {
            this.val = v;
        }
    }

    private static void in(Node head, Queue<Node> que) {
        if (head == null) {
            return;
        }
        in(head.left, que);
        que.offer(head);
        in(head.right, que);
    }

    public static Node convert1(Node head) {
        Queue<Node> que = new LinkedList<>();
        in(head, que);
        if (que.isEmpty()) {
            return head;
        }
        head = que.poll();
        Node pre = head;
        pre.left = null;
        Node cur = null;
        while (!que.isEmpty()) {
            cur = que.poll();
            pre.right = cur;
            cur.left = pre;
            pre = cur;
        }
        pre.right = null;
        return head;
    }

    //

    private static class Info {
        public Node start;
        public Node end;
        public Info(Node start, Node end) {
            this.start = start;
            this.end = end;
        }
    }

    private static Info process(Node x) {
        if (x == null) {
            return new Info(null, null);
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        if (li.end != null) {
            li.end.right = x;
        }
        x.left = li.end;
        x.right = ri.start;
        if (ri.start != null) {
            ri.start.left = x;
        }
        return new Info(li.start != null ? li.start : x, ri.end != null ? ri.end : x);
    }

    public static Node convert2(Node head) {
        if (head == null) {
            return null;
        }
        return process(head).start;
    }

    //

    private static void printIn(Node head) {
        if (head == null) {
            return;
        }
        printIn(head.left);
        System.out.print(head.val + " ");
        printIn(head.right);
    }

    private static void printLink(Node head) {
        Node end = null;
        while (head != null) {
            System.out.print(head.val + " ");
            end = head;
            head = head.right;
        }
        System.out.print("| ");
        while (end != null) {
            System.out.print(end.val + " ");
            end = end.left;
        }
        System.out.println();
    }

    public static void main(String[] args) {
		Node head = new Node(5);
		head.left = new Node(2);
		head.right = new Node(9);
		head.left.left = new Node(1);
		head.left.right = new Node(3);
		head.left.right.right = new Node(4);
		head.right.left = new Node(7);
		head.right.right = new Node(10);
		head.left.left = new Node(1);
		head.right.left.left = new Node(6);
		head.right.left.right = new Node(8);

        System.out.println("=====");
		printIn(head);
        System.out.println();
		head = convert1(head);
        System.out.println("=====");
		printLink(head);

		head = new Node(5);
		head.left = new Node(2);
		head.right = new Node(9);
		head.left.left = new Node(1);
		head.left.right = new Node(3);
		head.left.right.right = new Node(4);
		head.right.left = new Node(7);
		head.right.right = new Node(10);
		head.left.left = new Node(1);
		head.right.left.left = new Node(6);
		head.right.left.right = new Node(8);

        System.out.println("=====");
		printIn(head);
        System.out.println();
		head = convert2(head);
        System.out.println("=====");
		printLink(head);

	}
}
