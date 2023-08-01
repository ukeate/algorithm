package leetc.top;

public class P98_ValidateBinarySearchTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        TreeNode cur = root, r = null;
        Integer pre = null;
        boolean ans = true;
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
            if (pre != null && pre >= cur.val) {
                ans = false;
            }
            pre = cur.val;
            cur = cur.right;
        }
        return ans;
    }
}
