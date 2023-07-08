package basic.c32;

import java.util.Comparator;
import java.util.PriorityQueue;

// 二维柱状图存水
public class TrappingRainWaterII {
    public static class Node {
        public int val;
        public int row;
        public int col;

        public Node(int v, int r, int c) {
            val = v;
            row = r;
            col = c;
        }
    }

    public static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val - o2.val;
        }
    }

    public static int water(int[][] map) {
        if (map == null || map.length == 0 || map[0] == null || map[0].length == 0) {
            return 0;
        }
        int n = map.length;
        int m = map[0].length;
        boolean[][] visited = new boolean[n][m];
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        for (int col = 0; col < m - 1; col++) {
            visited[0][col] = true;
            heap.add(new Node(map[0][col], 0, col));
        }
        for (int row = 0; row < n - 1; row++) {
            visited[row][m - 1] = true;
            heap.add(new Node(map[row][m - 1], row, m - 1));
        }
        for (int col = m - 1; col > 0; col--) {
            visited[n - 1][col] = true;
            heap.add(new Node(map[n - 1][col], n - 1, col));
        }
        for (int row = n - 1; row > 0; row--) {
            visited[row][0] = true;
            heap.add(new Node(map[row][0], row, 0));
        }

        int water = 0;
        int max = 0;
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            max = Math.max(max, cur.val);
            int r = cur.row;
            int c = cur.col;
            if (r > 0 && !visited[r - 1][c]) {
                water += Math.max(0, max - map[r - 1][c]);
                visited[r - 1][c] = true;
                heap.add(new Node(map[r - 1][c], r - 1, c));
            }
            if (r < n - 1 && !visited[r + 1][c]) {
                water += Math.max(0, max - map[r + 1][c]);
                visited[r + 1][c] = true;
                heap.add(new Node(map[r + 1][c], r + 1, c));
            }
            if (c > 0 && !visited[r][c - 1]) {
                water += Math.max(0, max - map[r][c - 1]);
                visited[r][c - 1] = true;
                heap.add(new Node(map[r][c - 1], r, c - 1));
            }
            if (c < m - 1 && !visited[r][c + 1]) {
                water += Math.max(0, max - map[r][c + 1]);
                visited[r][c + 1] = true;
                heap.add(new Node(map[r][c + 1], r, c + 1));
            }
        }
        return water;
    }
}
