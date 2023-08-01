package leetc.top;

import java.util.HashMap;

public class P105_ConstructBinaryTreeFromPreorderAndInorderTraversal {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int v) {
            val = v;
        }
    }

    private static TreeNode process(int[] pre, int l1, int r1, int[] in, int l2, int r2, HashMap<Integer, Integer> inMap) {
        if (l1 > r1) {
            return null;
        }
        TreeNode head = new TreeNode(pre[l1]);
        if (l1 == r1) {
            return head;
        }
        int find = inMap.get(pre[l1]);
        head.left = process(pre, l1 + 1, l1 + find - l2, in, l2, find - 1, inMap);
        head.right = process(pre, l1 + find - l2 + 1, r1, in, find + 1, r2, inMap);
        return head;
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        HashMap<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inMap.put(inorder[i], i);
        }
        return process(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1, inMap);
    }
}
