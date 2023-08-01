package leetc.top;

public class P337_HouseRobberIII {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    private static class Info {
        public int no;
        public int yes;
        public Info(int n, int y) {
            no = n;
            yes = y;
        }
    }

    private static Info process(TreeNode x) {
        if (x == null) {
            return new Info(0, 0);
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int no = Math.max(li.no, li.yes) + Math.max(ri.no, ri.yes);
        int yes = x.val + li.no + ri.no;
        return new Info(no, yes);
    }

    public static int rob(TreeNode root) {
        Info info = process(root);
        return Math.max(info.no, info.yes);
    }
}
