package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class P210_CourseScheduleII {
    public static class Node {
        public int name;
        public int in;
        public ArrayList<Node> nexts;
        public Node(int n) {
            name = n;
            in = 0;
            nexts= new ArrayList<>();
        }
    }

    public static int[] findOrder(int numCourses, int[][] prerequisites) {
       int[] ans = new int[numCourses];
       for (int i = 0; i < numCourses; i++) {
           ans[i] = i;
       }
       if (prerequisites == null || prerequisites.length == 0) {
           return ans;
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
       int idx = 0;
        Queue<Node> que = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (!nodes.containsKey(i)) {
                ans[idx++] = i;
            } else {
                if (nodes.get(i).in == 0) {
                    que.add(nodes.get(i));
                }
            }
        }
        int needPrerequisiteNums = nodes.size();
        int count = 0;
        while (!que.isEmpty()) {
            Node cur = que.poll();
            ans[idx++] = cur.name;
            count++;
            for (Node next : cur.nexts) {
                if (--next.in == 0) {
                    que.add(next);
                }
            }
        }
        return count == needPrerequisiteNums ? ans : new int[0];
    }
}
