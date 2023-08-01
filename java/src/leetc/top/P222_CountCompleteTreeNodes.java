package leetc.top;

public class P222_CountCompleteTreeNodes {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    private static int leftLevel(TreeNode node, int level) {
        while (node != null) {
            level++;
            node = node.left;
        }
        return level - 1;
    }

    // level是当前层，h是总层数, 返回node节点数
    private static int bs(TreeNode node, int level, int h) {
        if (level == h) {
            return 1;
        }
        if (leftLevel(node.right, level + 1) == h) {
            return (1 << (h - level)) + bs(node.right, level + 1, h);
        } else {
            return (1 << (h - level - 1)) + bs(node.left, level + 1, h);
        }
    }

    public static int countNodes(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return bs(head, 1, leftLevel(head, 1));
    }
}
