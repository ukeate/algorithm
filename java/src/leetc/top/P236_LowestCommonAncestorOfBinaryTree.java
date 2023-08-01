package leetc.top;

public class P236_LowestCommonAncestorOfBinaryTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    private static class Info {
        public TreeNode ans;
        public boolean findO1;
        public boolean findO2;
        public Info(TreeNode a, boolean f1, boolean f2) {
            ans = a;
            findO1 = f1;
            findO2 = f2;
        }
    }

    private static Info process(TreeNode x, TreeNode o1, TreeNode o2) {
        if (x == null) {
            return new Info(null, false, false);
        }
        Info li = process(x.left, o1, o2);
        Info ri = process(x.right, o1, o2);
        boolean findO1 = x == o1 || li.findO1 || ri.findO1;
        boolean findO2 = x == o2 || li.findO2 || ri.findO2;
        TreeNode ans = null;
        if (li.ans != null) {
            ans = li.ans;
        }
        if (ri.ans != null) {
            ans = ri.ans;
        }
        if (ans == null) {
            if (findO1 && findO2) {
                ans = x;
            }
        }
        return new Info(ans, findO1, findO2);
    }

    public static TreeNode lowestCommonAncestor(TreeNode head, TreeNode o1, TreeNode o2) {
        return process(head, o1, o2).ans;
    }
}
