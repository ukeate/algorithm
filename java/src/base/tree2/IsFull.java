package base.tree2;

public class IsFull {
    public static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            val = v;
        }
    }

    private static class Info1 {
        public int height;
        public int size;

        public Info1(int h, int n) {
            height = h;
            this.size = n;
        }
    }

    private static Info1 process1(Node head) {
        if (head == null) {
            return new Info1(0, 0);
        }
        Info1 li = process1(head.left);
        Info1 ri = process1(head.right);
        int height = Math.max(li.height, ri.height) + 1;
        int n = li.size + ri.size + 1;
        return new Info1(height, n);
    }

    public static boolean isFull1(Node head) {
        if (head == null) {
            return true;
        }
        Info1 info = process1(head);
        return (1 << info.height) - 1 == info.size;
    }

    private static class Info2 {
        public boolean isFull;
        public int height;

        public Info2(boolean f, int h) {
            isFull = f;
            height = h;
        }
    }

    private static Info2 process2(Node h) {
        if (h == null) {
            return new Info2(true, 0);
        }
        Info2 li = process2(h.left);
        Info2 ri = process2(h.right);
        boolean isFull = li.isFull && ri.isFull && li.height == ri.height;
        int height = Math.max(li.height, ri.height) + 1;
        return new Info2(isFull, height);
    }

    public static boolean isFull2(Node head) {
        if (head == null) {
            return true;
        }
        return process2(head).isFull;
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

    public static void main(String[] args) {
        int times = 100000;
        int maxLevel = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            boolean ans1 = isFull1(head);
            boolean ans2 = isFull2(head);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}
