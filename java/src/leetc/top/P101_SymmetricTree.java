package leetc.top;

public class P101_SymmetricTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    public static boolean isMirror(TreeNode h1, TreeNode h2) {
        if (h1 == null && h2 == null) {
            return true;
        }
        if (h1 != null && h2 != null) {
            return h1.val == h2.val && isMirror(h1.left, h2.right) && isMirror(h1.right, h2.left);
        }
        return false;
    }

    public static boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);
    }
}
