package leetc.top;

import java.util.LinkedList;
import java.util.Queue;

public class P297_SerializeAndDeserializeBinaryTree {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int value) {
            val = value;
        }
    }

    public String serialize(TreeNode root) {
        LinkedList<String> ans = new LinkedList<>();
        if (root == null) {
            ans.add(null);
        } else {
            ans.add(String.valueOf(root.val));
            Queue<TreeNode> que = new LinkedList<>();
            que.add(root);
            while (!que.isEmpty()) {
                root = que.poll();
                if (root.left != null) {
                    ans.add(String.valueOf(root.left.val));
                    que.add(root.left);
                } else {
                    ans.add(null);
                }
                if (root.right != null) {
                    ans.add(String.valueOf(root.right.val));
                    que.add(root.right);
                } else {
                    ans.add(null);
                }
            }
        }
        while (!ans.isEmpty() && ans.peekLast() == null) {
            ans.pollLast();
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        String str = ans.pollFirst();
        builder.append(str == null ? "null" : str);
        while (!ans.isEmpty()) {
            str = ans.pollFirst();
            builder.append("," + (str == null ? "null" : str));
        }
        builder.append("]");
        return builder.toString();
    }

    private TreeNode node(String val) {
        if (val.equals("null")) {
            return null;
        }
        System.out.println(val);
        return new TreeNode(Integer.valueOf(val));
    }

    public TreeNode deserialize(String data) {
        String[] strs = data.substring(1, data.length() - 1).split(",");
        int idx = 0;
        TreeNode root = node(strs[idx++]);
        Queue<TreeNode> que = new LinkedList<>();
        if (root != null){
            que.add(root);
        }
        TreeNode node = null;
        while (!que.isEmpty()) {
            node = que.poll();
            node.left = node(idx == strs.length ? "null" : strs[idx++]);
            node.right = node(idx == strs.length ? "null" : strs[idx++]);
            if (node.left != null) {
                que.add(node.left);
            }
            if (node.right != null) {
                que.add(node.right);
            }
        }
        return root;
    }
}
