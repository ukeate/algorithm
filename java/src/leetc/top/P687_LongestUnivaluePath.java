package leetc.top;

public class P687_LongestUnivaluePath {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    private static class Info {
        public int len;
        public int max;

        public Info(int l, int m) {
            len = l;
            max = m;
        }
    }

    private static Info process(TreeNode x) {
        if (x == null) {
            return new Info(0, 0);
        }
        TreeNode l = x.left, r = x.right;
        Info li = process(l);
        Info ri = process(r);
        int len = 1;
        if (l != null && l.val == x.val) {
            len = li.len + 1;
        }
        if (r != null && r.val == x.val) {
            len = Math.max(len, ri.len + 1);
        }
        int max = Math.max(Math.max(li.max, ri.max), len);
        if (l != null && r != null && l.val == x.val && r.val == x.val) {
            max = Math.max(max, li.len + ri.len + 1);
        }
        return new Info(len, max);
    }

    public static int longestUnivaluePath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return process(root).max - 1;
    }
}
