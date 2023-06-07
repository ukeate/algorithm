package base.tree2;

import java.util.ArrayList;

public class IsBST {

    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    private static void in(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);
        arr.add(head);
        in(head.right, arr);
    }

    public static boolean isBST1(Node head) {
        if (head == null) {
            return true;
        }
        ArrayList<Node> arr = new ArrayList<>();
        in(head, arr);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).val <= arr.get(i - 1).val) {
                return false;
            }
        }
        return true;
    }

    private static class Info {
        public boolean isBST;
        public int max;
        public int min;

        public Info(boolean is, int max, int min) {
            this.isBST = is;
            this.max = max;
            this.min = min;
        }
    }

    private static Info process(Node x) {
        if (x == null) {
            return null;
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int max = x.val;
        int min = x.val;
        if (li != null) {
            max = Math.max(li.max, max);
            min = Math.min(li.min, min);
        }
        if (ri != null) {
            max = Math.max(ri.max, max);
            min = Math.min(ri.min, min);
        }

        boolean isBST = false;
        boolean lb = li == null ? true : li.isBST;
        boolean rb = ri == null ? true : ri.isBST;
        boolean lm = li == null ? true : (li.max < x.val);
        boolean rm = ri == null ? true : (ri.min > x.val);
        if (lb && rb && lm && rm) {
            isBST = true;
        }
        return new Info(isBST, max, min);
    }

    public static boolean isBST2(Node head) {
        if (head == null) {
            return true;
        }
        return process(head).isBST;
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
            if (isBST1(head) != isBST2(head)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
