package base.tree2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MaxDistance {
    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            val = v;
        }
    }

    private static int distance(HashMap<Node, Node> parent, Node n1, Node n2) {
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
        Node lca = cur;
        cur = n1;
        int distance1 = 1;
        while (cur != lca) {
            cur = parent.get(cur);
            distance1++;
        }
        cur = n2;
        int distance2 = 1;
        while (cur != lca) {
            cur = parent.get(cur);
            distance2++;
        }
        return distance1 + distance2 - 1;
    }

    private static void fillParent(Node head, HashMap<Node, Node> map) {
        if (head.left != null) {
            map.put(head.left, head);
            fillParent(head.left, map);
        }
        if (head.right != null) {
            map.put(head.right, head);
            fillParent(head.right, map);
        }
    }

    private static HashMap<Node, Node> parentMap(Node head) {
        HashMap<Node, Node> map = new HashMap<>();
        map.put(head, null);
        fillParent(head, map);
        return map;
    }

    private static void fillPre(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        arr.add(head);
        fillPre(head.left, arr);
        fillPre(head.right, arr);
    }

    private static ArrayList<Node> preList(Node head) {
        ArrayList<Node> arr = new ArrayList<>();
        fillPre(head, arr);
        return arr;
    }

    public static int maxDistance1(Node head) {
        if (head == null) {
            return 0;
        }
        ArrayList<Node> arr = preList(head);
        HashMap<Node, Node> parentMap = parentMap(head);
        int max = 0;
        for (int i = 0; i < arr.size(); i++) {
            for (int j = i; j < arr.size(); j++) {
                max = Math.max(max, distance(parentMap, arr.get(i), arr.get(j)));
            }
        }
        return max;
    }

    //

    private static class Info {
        public int maxDistance;
        public int height;

        public Info(int m, int h) {
            maxDistance = m;
            height = h;
        }
    }

    private static Info process(Node x) {
        if (x == null) {
            return new Info(0, 0);
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int p1 = li.maxDistance;
        int p2 = ri.maxDistance;
        int p0 = li.height + ri.height + 1;
        return new Info(Math.max(Math.max(p1, p2), p0), Math.max(li.height, ri.height) + 1);
    }

    public static int maxDistance2(Node head) {
        return process(head).maxDistance;
    }

    //

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
            int ans1 = maxDistance1(head);
            int ans2 = maxDistance2(head);
            if ( ans1 != ans2 ) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}
