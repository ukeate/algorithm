package leetc.top;

public class P230_KthSmallestElementInBST {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    public static int kthSmallest(TreeNode head, int k) {
        if (head == null) {
            return -1;
        }
        TreeNode cur = head, r = null;
        int idx = 1;
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
            if (idx++ == k) {
                return cur.val;
            }
            cur = cur.right;
        }
        return -1;
    }
}
