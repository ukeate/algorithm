package leetc.top;

public class P499_TheMazeIII {

    private static class Node {
        // (r,c)位置
        public int r;
        public int c;
        // 从哪个方向来, 0, 1, 2, 3, 4
        public int d;
        // 路径
        public String p;

        public Node(int row, int col, int dir, String path) {
            r = row;
            c = col;
            d = dir;
            p = path;
        }
    }

    private static int[][] to = {{1, 0}, {0, -1}, {0, 1}, {-1, 0}, {0, 0}};
    private static String[] re = {"d", "l", "r", "u"};

    // v[行][列][方向]
    // q 下一层的队列, s 下一层队列size
    // 返回q的size
    private static int spread(int[][] maze, int n, int m, Node cur, boolean[][][] v, Node[] q, int s) {
        int d = cur.d;
        int r = cur.r + to[d][0];
        int c = cur.c + to[d][1];
        if (d == 4 || r < 0 || r == n || c < 0 || c == m || maze[r][c] != 0) {
            for (int i = 0; i < 4; i++) {
                if (i != d) {
                    r = cur.r + to[i][0];
                    c = cur.c + to[i][1];
                    if (r >= 0 && r < n && c >= 0 && c < m && maze[r][c] == 0 && !v[r][c][i]) {
                        v[r][c][i] = true;
                        Node next = new Node(r, c, i, cur.p + re[i]);
                        q[s++] = next;
                    }
                }
            }
        } else {
            if (!v[r][c][d]) {
                v[r][c][d] = true;
                q[s++] = new Node(r, c, d, cur.p);
            }
        }
        return s;
    }

    public static String findShortestWay(int[][] maze, int[] ball, int[] hole) {
        int n = maze.length;
        int m = maze[0].length;
        Node[] q1 = new Node[n * m], q2 = new Node[n * m];
        int s1 = 0, s2 = 0;
        boolean[][][] visited = new boolean[maze.length][maze[0].length][4];
        s1 = spread(maze, n, m, new Node(ball[0], ball[1], 4, ""), visited, q1, s1);
        while (s1 != 0) {
            for (int i = 0; i < s1; i++) {
                Node cur = q1[i];
                if (hole[0] == cur.r && hole[1] == cur.c) {
                    return cur.p;
                }
                s2 = spread(maze, n, m, cur, visited, q2, s2);
            }
            Node[] tmp = q1;
            q1 = q2;
            q2 = tmp;
            s1 = s2;
            s2 = 0;
        }
        return "impossible";
    }
}
