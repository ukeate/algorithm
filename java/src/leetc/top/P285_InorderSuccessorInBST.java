package leetc.top;

public class P285_InorderSuccessorInBST {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    public static TreeNode inorderSuccessor(TreeNode head, TreeNode p) {
        if (head == null) {
            return null;
        }
        TreeNode cur = head, r = null, pre = null;
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
            if (pre == p) {
                return cur;
            } else {
                pre = cur;
            }
            cur = cur.right;
        }
        return null;
    }
}
