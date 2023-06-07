package base.tree2;

public class IsBalanced {
    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    private static int process1(Node head, boolean[] ans) {
        if (!ans[0] || head == null) {
            return -1;
        }
        int leftHeight = process1(head.left, ans);
        int rightHeight = process1(head.right, ans);
        if (Math.abs(leftHeight - rightHeight) > 1) {
            ans[0] = false;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public static boolean isBalanced1(Node head) {
        boolean[] ans = new boolean[1];
        ans[0] = true;
        process1(head, ans);
        return ans[0];
    }

    private static class Info {
        public boolean isB;
        public int height;

        public Info(boolean i, int h) {
            this.isB = i;
            this.height = h;
        }
    }

    private static Info process2(Node root) {
        if (root == null) {
            return new Info(true, 0);
        }
        Info li = process2(root.left);
        Info ri = process2(root.right);
        int ht = Math.max(li.height, ri.height) + 1;
        boolean isB = li.isB && ri.isB && Math.abs(li.height - ri.height) < 2;
        return new Info(isB, ht);
    }

    public static boolean isBalanced2(Node root) {
        return process2(root).isB;
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
            if (isBalanced1(head) != isBalanced2(head)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
