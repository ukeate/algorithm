package base.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LevelPath {
    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }
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
