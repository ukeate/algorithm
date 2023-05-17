package basic;

import java.util.*;

public class Tree {
    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    public static boolean isSame(Node p, Node q) {
        if (p == null ^ q == null) {
            return false;
        }
        if (p == null && q == null) {
            return true;
        }
        return p.val == q.val && isSame(p.left, q.left) && isSame(p.right, q.right);
    }

    //

    private static boolean isMirror(Node h1, Node h2) {
        if (h1 == null ^ h2 == null) {
            return false;
        }
        if (h1 == null && h2 == null) {
            return true;
        }
        return h1.val == h2.val && isMirror(h1.left, h2.right) && isMirror(h1.right, h2.left);
    }

    public static boolean isSymmetric(Node root) {
        return isMirror(root, root);
    }

    //

    public static int depth(Node root) {
        if (root == null) {
            return 0;
        }
        return Math.max(depth(root.left), depth(root.right)) + 1;
    }

    //

    private static Node buildF(int[] pre, int l1, int r1, int[] in, int l2, int r2, Map<Integer, Integer> inMap) {
        if (l1 > r1) {
            return null;
        }
        Node head = new Node(pre[l1]);
        if (l1 == r1) {
            return head;
        }
        int find = inMap.get(pre[l1]);
        head.left = buildF(pre, l1 + 1, l1 + find - l2, in, l2, find - 1, inMap);
        head.right = buildF(pre, l1 + find - l2 + 1, r1, in, find + 1, r2, inMap);
        return head;
    }

    public static Node build(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            inMap.put(in[i], i);
        }
        return buildF(pre, 0, pre.length - 1, in, 0, in.length - 1, inMap);
    }

    //

    public static void traversal(Node head) {
        if (head == null) {
            return;
        }
        traversal(head.left);
        traversal(head.right);
    }

    //

    public static List<List<Integer>> levelPath(Node root) {
        List<List<Integer>> ans = new LinkedList<>();
        if (root == null) {
            return ans;
        }
        Queue<Node> que = new LinkedList<>();
        que.add(root);
        while (!que.isEmpty()) {
            int size = que.size();
            List<Integer> l = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                Node node = que.poll();
                l.add(node.val);
                if (node.left != null) {
                    que.add(node.left);
                }
                if (node.right != null) {
                    que.add(node.right);
                }
            }
            ans.add(0, l);
        }
        return ans;
    }


    public static void main(String[] args) {

    }
}
