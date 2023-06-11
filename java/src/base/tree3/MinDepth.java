package base.tree3;

// https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
public class MinDepth {
    private static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    private static int process(TreeNode x) {
        if (x.left == null && x.right == null) {
            return 1;
        }
        int leftH = Integer.MAX_VALUE;
        if (x.left != null) {
            leftH = process(x.left);
        }
        int rightH = Integer.MAX_VALUE;
        if (x.right != null) {
            rightH = process(x.right);
        }
        return 1 + Math.min(leftH, rightH);
    }

    public static int min1(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return process(head);
    }

    //

    public static int min2(TreeNode head) {
        if (head == null) {
            return 0;
        }
        TreeNode cur = head;
        TreeNode r = null;
        int curH = 0;
        int minH = Integer.MAX_VALUE;
        while (cur != null) {
            r = cur.left;
            if (r != null) {
                int rH = 1;
                while (r.right != null && r.right != cur) {
                    rH++;
                    r = r.right;
                }
                if (r.right == null) {
                    curH++;
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    if (r.left == null) {
                        minH = Math.min(minH, curH);
                    }
                    curH -= rH;
                    r.right = null;
                }
            } else {
                curH++;
            }
            cur = cur.right;
        }
        int finalRH = 1;
        cur = head;
        while (cur.right != null) {
            finalRH++;
            cur = cur.right;
        }
        if (cur.left == null && cur.right == null) {
            minH = Math.min(minH, finalRH);
        }
        return minH;
    }
}
