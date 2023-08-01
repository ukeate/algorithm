package leetc.top;

public class P114_FlattenBinaryTreeToLinkedList {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int v) {
            val = v;
        }
    }

    // 中->左->右
    public static void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode pre = null;
        TreeNode cur = root;
        TreeNode r = null;
        while (cur != null) {
            r = cur.left;
            if(r != null) {
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    r.right = cur;
                    if (pre != null) {
                        pre.left = cur;
                    }
                    // case1: 中->左
                    pre = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // case3: 中->右
                    r.right = null;
                }
            } else {
                // case2: 左->中
                if (pre != null) {
                    pre.left = cur;
                }
                pre = cur;
            }
            cur = cur.right;
        }
        cur = root;
        TreeNode next = null;
        while (cur != null) {
            cur.right = cur.left;;
            cur.left = null;
            cur = cur.right;
        }
    }
}
