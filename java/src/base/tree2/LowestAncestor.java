package base.tree2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LowestAncestor {
    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            val = v;
        }
    }

    private static void fillParent(Node head, HashMap<Node, Node> parent) {
        if (head.left != null) {
            parent.put(head.left, head);
            fillParent(head.left, parent);
        }
        if (head.right != null) {
            parent.put(head.right, head);
            fillParent(head.right, parent);
        }
    }

    public static Node lowestAncestor1(Node head, Node n1, Node n2) {
        if (head == null) {
            return null;
        }
        HashMap<Node, Node> parent = new HashMap<>();
        parent.put(head, null);
        fillParent(head, parent);
        HashSet<Node> set1 = new HashSet<>();
        Node cur = n1;
        set1.add(cur);
        while (parent.get(cur) != null) {
            cur = parent.get(cur);
            set1.add(cur);
        }
        cur = n2;
        while (!set1.contains(cur)) {
            cur = parent.get(cur);
        }
        return cur;
    }

    //

    private static class Info {
        public boolean findA;
        public boolean findB;
        public Node ans;

        public Info(boolean a, boolean b, Node an) {
            findA = a;
            findB = b;
            ans = an;
        }
    }

    private static Info process(Node x, Node a, Node b) {
        if (x == null) {
            return new Info(false, false, null);
        }
        Info li = process(x.left, a, b);
        Info ri = process(x.right, a, b);
        boolean findA = (x == a) || li.findA || ri.findA;
        boolean findB = (x == b) || li.findB || ri.findB;
        Node ans = null;
        if (li.ans != null) {
            ans = li.ans;
        } else if (ri.ans != null) {
            ans = ri.ans;
        } else {
            if (findA && findB) {
                ans = x;
            }
        }
        return new Info(findA, findB, ans);
    }

    public static Node lowestAncestor2(Node head, Node a, Node b) {
        return process(head, a, b).ans;
    }


    private static Node randomLevel(int level, int maxLevel, int maxVal) {
        if (level > maxLevel || Math.random() < 0.5) {
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

    private static void fillPre(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        arr.add(head);
        fillPre(head.left, arr);
        fillPre(head.right, arr);
    }

    private static Node randomPick(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> arr = new ArrayList<>();
        fillPre(head, arr);
        return arr.get((int) (arr.size() * Math.random()));
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLevel = 3;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            Node n1 = randomPick(head);
            Node n2 = randomPick(head);
            Node ans1 = lowestAncestor1(head, n1, n2);
            Node ans2 = lowestAncestor2(head, n1, n2);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}
