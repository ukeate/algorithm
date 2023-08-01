package leetc.top;

import java.util.LinkedList;
import java.util.Queue;

public class P116_PopulatingNextRightPointersInEachNode {
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;
    }

    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
        Queue<Node> que = new LinkedList<>();
        que.offer(root);
        while (!que.isEmpty()) {
            Node pre = null;
            int size = que.size();
            for (int i = 0; i < size; i++) {
                Node cur = que.poll();
                if (cur.left != null) {
                    que.offer(cur.left);
                }
                if (cur.right != null) {
                    que.offer(cur.right);
                }
                if (pre != null) {
                    pre.next = cur;
                }
                pre = cur;
            }
        }
        return root;
    }
}
