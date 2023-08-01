package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class P207_CourseSchedule {
    public static class Node {
        public int name;
        public int in;
        public ArrayList<Node> nexts;
        public Node(int n) {
            name = n;
            nexts = new ArrayList<>();
        }
    }

    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        if (prerequisites == null || prerequisites.length == 0) {
            return true;
        }
        HashMap<Integer, Node> nodes = new HashMap<>();
        for (int[] arr : prerequisites) {
            int to = arr[0];
            int from = arr[1];
            if (!nodes.containsKey(to)) {
                nodes.put(to, new Node(to));
            }
            if (!nodes.containsKey(from)) {
                nodes.put(from, new Node(from));
            }
            Node t = nodes.get(to);
            Node f = nodes.get(from);
            f.nexts.add(t);
            t.in++;
        }
        int need = nodes.size();
        Queue<Node> que = new LinkedList<>();
        for (Node node : nodes.values()) {
            if (node.in == 0) {
                que.add(node);
            }
        }
        int count = 0;
        while (!que.isEmpty()) {
            Node cur = que.poll();
            count++;
            for (Node next : cur.nexts) {
                if (--next.in == 0) {
                    que.add(next);
                }
            }
        }
        return count == need;
    }
}
