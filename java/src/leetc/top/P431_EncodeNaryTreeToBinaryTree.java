package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P431_EncodeNaryTreeToBinaryTree {
    public static class Node {
        public int val;
        public List<Node> children;
        public Node() {}
        public Node (int v) {
            val = v;
        }
        public Node (int v, List<Node> c) {
            val = v;
            children = c;
        }
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int v) {
            val = v;
        }
    }

    class Codec {
        private TreeNode enc(List<Node> children) {
            TreeNode head = null, cur = null;
            for (Node child : children) {
                TreeNode t = new TreeNode(child.val);
                if (head == null) {
                    head = t;
                } else {
                    cur.right = t;
                }
                cur = t;
                cur.left = enc(child.children);
            }
            return head;
        }

        public TreeNode encode(Node root) {
            if (root == null) {
                return null;
            }
            TreeNode head = new TreeNode(root.val);
            head.left = enc(root.children);
            return head;
        }

        private List<Node> dec(TreeNode root) {
            List<Node> children = new ArrayList<>();
            while (root != null) {
                Node cur = new Node(root.val, dec(root.left));
                children.add(cur);
                root = root.right;
            }
            return children;
        }

        public Node decode(TreeNode root) {
            if (root == null) {
                return null;
            }
            return new Node(root.val, dec(root.left));
        }
    }
}
