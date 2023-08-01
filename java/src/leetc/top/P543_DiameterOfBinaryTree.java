package leetc.top;

public class P543_DiameterOfBinaryTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
    }

    private static class Info {
        public int maxDistance;
        public int height;
        public Info(int m, int h) {
            maxDistance = m;
            height = h;
        }
    }

    private static Info process(TreeNode x) {
        if (x == null) {
            return new Info(0, 0);
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int maxDistance = Math.max(Math.max(li.maxDistance, ri.maxDistance),
                li.height + ri.height);
        int height = Math.max(li.height, ri.height) + 1;
        return new Info(maxDistance, height);
    }

    public static int diameterOfBinaryTree(TreeNode root) {
        return process(root).maxDistance;
    }
}
