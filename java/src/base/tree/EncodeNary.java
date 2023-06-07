package base.tree;

import java.util.ArrayList;
import java.util.List;

// https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree
public class EncodeNary {
    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {
        }

        public Node(int v) {
            val = v;
        }

        public Node(int v, List<Node> c) {
            val = v;
            children = c;
        }
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    class Codec {
        private TreeNode en(List<Node> children) {
            TreeNode head = null;
            for (Node child : children) {
                TreeNode cur = new TreeNode(child.val);
                if (head == null) {
                    head = cur;
                } else {
                    cur.right = cur;
                }
                cur.left = en(child.children);
            }
            return head;
        }

        public TreeNode encode(Node root) {
            if (root == null) {
                return null;
            }
            TreeNode head = new TreeNode(root.val);
            head.left = en(root.children);
            return head;
        }

        private List<Node> de(TreeNode root) {
            List<Node> children = new ArrayList<>();
            while (root != null) {
                Node cur = new Node(root.val, de(root.left));
                children.add(cur);
                root = root.right;
            }
            return children;
        }

        public Node decode(TreeNode root) {
            if (root == null) {
                return null;
            }
            return new Node(root.val, de(root.left));
        }
    }
}
