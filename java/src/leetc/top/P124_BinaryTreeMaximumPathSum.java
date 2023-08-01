package leetc.top;

public class P124_BinaryTreeMaximumPathSum {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    private static class Info {
        public int maxSum;
        public int maxSumFromHead;
        public Info(int path, int fromHead) {
            maxSum = path;
            maxSumFromHead = fromHead;
        }
    }

    private static Info process(TreeNode x) {
        if (x == null) {
            return null;
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int p1 = Integer.MIN_VALUE;
        if (li != null) {
            p1 = li.maxSum;
        }
        int p2 = Integer.MIN_VALUE;
        if (ri != null) {
            p2 = ri.maxSum;
        }
        int p3 = x.val;
        int p4 = Integer.MIN_VALUE;
        if (li != null) {
            p4 = x.val + li.maxSumFromHead;
        }
        int p5 = Integer.MIN_VALUE;
        if (ri != null) {
            p5 = x.val + ri.maxSumFromHead;
        }
        int p6 = Integer.MIN_VALUE;
        if (li != null && ri != null) {
            p6 = x.val + li.maxSumFromHead + ri.maxSumFromHead;
        }
        int maxSumFromHead = Math.max(p3, Math.max(p4, p5));
        int maxSum = Math.max(Math.max(p1, p2), Math.max(maxSumFromHead, p6));
        return new Info(maxSum, maxSumFromHead);
    }

    public static int maxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return process(root).maxSum;
    }
}
