package base.tree2;

import java.util.LinkedList;

// is complete binary tree
public class IsCBT {
    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            val = v;
        }
    }

    public static boolean isCBT1(Node head) {
        if (head == null) {
            return true;
        }
        LinkedList<Node> queue = new LinkedList<>();
        boolean leafEnd = false;
        Node l = null;
        Node r = null;
        queue.add(head);
        while (!queue.isEmpty()) {
            head = queue.poll();
            l = head.left;
            r = head.right;
            if ((leafEnd && (l != null || r != null)) || (l == null && r != null)) {
                return false;
            }
            if (l != null) {
                queue.add(l);
            }
            if (r != null) {
                queue.add(r);
            }
            if (l == null || r == null) {
                leafEnd = true;
            }
        }
        return true;
    }

    private static class Info {
        public boolean isFull;
        public boolean isCBT;
        public int height;
        public Info(boolean full, boolean cbt, int h) {
            isFull = full;
            isCBT = cbt;
            height = h;
        }
    }

    private static Info process(Node x) {
        if (x == null) {
            return new Info(true, true, 0);
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int height = Math.max(li.height, ri.height) + 1;
        boolean isFull = li.isFull && ri.isFull && li.height == ri.height;
        boolean isCBT = false;
        if (isFull) {
            isCBT = true;
        } else {
            if (li.isCBT && ri.isCBT) {
                if (li.isCBT && ri.isFull && li.height == ri.height + 1) isCBT = true;
                if (li.isFull && ri.isFull && li.height == ri.height + 1) isCBT = true;
                if (li.isFull && ri.isCBT && li.height == ri.height) isCBT = true;
            }
        }
        return new Info(isFull, isCBT, height);
    }
    public static boolean isCBT2(Node head) {
        if (head == null){
            return true;
        }
        return process(head).isCBT;
    }

    private static Node randomLevel(int level, int maxLevel, int maxVal) {
        if(level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        return head;
    }

    private static Node randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLevel = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            if (isCBT1(head) != isCBT2(head)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}