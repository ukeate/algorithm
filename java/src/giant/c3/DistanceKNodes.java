package giant.c3;

import java.util.*;

// 求距离为k的节点
public class DistanceKNodes {
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node(int v) {
            val = v;
        }
    }

    private static void parents(Node cur, HashMap<Node, Node> parents) {
        if (cur == null) {
            return;
        }
        if (cur.left != null) {
            parents.put(cur.left, cur);
            parents(cur.left, parents);
        }
        if (cur.right != null) {
            parents.put(cur.right, cur);
            parents(cur.right, parents);
        }
    }

    public static List<Node> find(Node root, Node target, int k) {
        HashMap<Node, Node> parents = new HashMap<>();
        parents.put(root, null);
        parents(root, parents);
        Queue<Node> que = new LinkedList<>();
        HashSet<Node> visited = new HashSet<>();
        que.offer(target);
        visited.add(target);
        int curLevel = 0;
        List<Node> ans = new ArrayList<>();
        while (!que.isEmpty()) {
            int size = que.size();
            while (size-- > 0) {
                Node cur = que.poll();
                if (curLevel == k) {
                    ans.add(cur);
                }
                if (cur.left != null && !visited.contains(cur.left)) {
                    visited.add(cur.left);
                    que.offer(cur.left);
                }
                if (cur.right != null && !visited.contains(cur.right)) {
                    visited.add(cur.right);
                    que.offer(cur.right);
                }
                if (parents.get(cur) != null && !visited.contains(parents.get(cur))) {
                    visited.add(parents.get(cur));
                    que.offer(parents.get(cur));
                }
            }
            curLevel++;
            if (curLevel > k) {
                break;
            }
        }
        return ans;
    }
}
