package basic.c31;

// 到叶节点的最大路径和
public class MaxSumInTree {
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node(int v) {
            val = v;
        }
    }
    public static int maxSum = Integer.MIN_VALUE;
    private static void process1(Node x, int pre) {
        if (x.left == null && x.right == null) {
            maxSum = Math.max(maxSum, pre + x.val);
        }
        if (x.left != null) {
            process1(x.left, pre + x.val);
        }
        if (x.right != null) {
            process1(x.right, pre + x.val);
        }
    }

    public static int max1(Node head) {
        maxSum = Integer.MIN_VALUE;
        process1(head, 0);
        return maxSum;
    }

    //

    private static int process2(Node x) {
        if (x.left == null && x.right == null) {
            return x.val;
        }
        int next = Integer.MIN_VALUE;
        if (x.left != null) {
            next = process2(x.left);
        }
        if (x.right != null) {
            next = Math.max(next, process2(x.right));
        }
        return x.val + next;
    }

    public static int max2(Node head) {
        if (head == null) {
            return 0;
        }
        return process2(head);
    }

    //

    public static class Info {
        public int allMax;
        public int fromHeadMax;
        public Info(int all, int from) {
            allMax = all;
            fromHeadMax = from;
        }
    }

    private static Info f3(Node x) {
        if (x == null) {
            return null;
        }
        Info li = f3(x.left);
        Info ri = f3(x.right);
        int p1 = Integer.MIN_VALUE;
        if (li != null) {
            p1 = li.allMax;
        }
        int p2 = Integer.MIN_VALUE;
        if (ri != null) {
            p2 = ri.allMax;
        }
        int p3 = x.val;
        int p4 = Integer.MIN_VALUE;
        if (li != null) {
            p4 = x.val + li.fromHeadMax;
        }
        if (ri != null) {
            p4 = Math.max(p4, x.val + ri.fromHeadMax);
        }
        int allMax = Math.max(Math.max(Math.max(p1, p2), p3), p4);
        int fromHeadMax = Math.max(p3, p4);
        return new Info(allMax, fromHeadMax);
    }

    public static int max3(Node head) {
        if (head == null) {
            return 0;
        }
        return f3(head).allMax;
    }


}
