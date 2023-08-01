package leetc.top;

public class P99_RecoverBinarySearchTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int v) {
            val = v;
        }
    }

    private static TreeNode[] twoErrors(TreeNode head) {
        TreeNode[] ans = new TreeNode[2];
        if (head == null) {
            return ans;
        }
        TreeNode cur = head, r = null, pre = null, e1 = null, e2 = null;
        while (cur != null) {
            r = cur.left;
            if (r != null) {
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    r.right = null;
                }
            }
            if (pre != null && pre.val >= cur.val) {
                e1 = e1 == null ? pre : e1;
                e2 = cur;
            }
            pre = cur;
            cur = cur.right;
        }
        ans[0] = e1;
        ans[1] = e2;
        return ans;
    }

    public static void recoverTree(TreeNode root) {
        TreeNode[] errors = twoErrors(root);
        if (errors[0] != null && errors[1] != null) {
            int tmp = errors[0].val;
            errors[0].val = errors[1].val;
            errors[1].val = tmp;
        }
    }
}
