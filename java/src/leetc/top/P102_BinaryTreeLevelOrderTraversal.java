package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class P102_BinaryTreeLevelOrderTraversal {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        ans.add(new ArrayList<>());
        Queue<TreeNode> que = new LinkedList<>();
        que.add(root);
        TreeNode curEnd = root, nextEnd = null;
        while (!que.isEmpty()) {
            TreeNode cur = que.poll();
            ans.get(ans.size() - 1).add(cur.val);
            if (cur.left != null) {
                que.add(cur.left);
                nextEnd = cur.left;
            }
            if (cur.right != null) {
                que.add(cur.right);
                nextEnd = cur.right;
            }
            if (cur == curEnd) {
                curEnd = nextEnd;
                ans.add(new ArrayList<>());
            }
        }
        ans.remove(ans.size() - 1);
        return ans;
    }
}
