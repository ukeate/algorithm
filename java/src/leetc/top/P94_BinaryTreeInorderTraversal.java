package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P94_BinaryTreeInorderTraversal {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        TreeNode cur = root;
        TreeNode r = null;
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
            ans.add(cur.val);
            cur = cur.right;
        }
        return ans;
    }
}
