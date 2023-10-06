package leetc.top;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class P317_ShortestDistanceFromAllBuildings {
    private static class Position {
        public int r;
        public int c;
        public int v;

        public Position(int row, int col, int val) {
            r = row;
            c = col;
            v = val;
        }
    }

    private static void add(Queue<Position> q, HashMap<Position, Integer> l, Position[][] p, int i, int j, int level) {
        if (i >= 0 && i < p.length && j >= 0 && j < p[0].length && p[i][j].v != 2 && !l.containsKey(p[i][j])) {
            l.put(p[i][j], level);
            q.add(p[i][j]);
        }
    }

    private static int bfs(Position[][] positions, int buildings, int i, int j) {
        if (positions[i][j].v != 0) {
            return Integer.MAX_VALUE;
        }
        HashMap<Position, Integer> levels = new HashMap<>();
        Queue<Position> que = new LinkedList<>();
        Position from = positions[i][j];
        levels.put(from, 0);
        que.add(from);
        int ans = 0, solved = 0;
        while (!que.isEmpty() && solved != buildings) {
            Position cur = que.poll();
            int level = levels.get(cur);
            if (cur.v == 1) {
                ans += level;
                solved++;
            } else {
                add(que, levels, positions, cur.r - 1, cur.c, level + 1);
                add(que, levels, positions, cur.r + 1, cur.c, level + 1);
                add(que, levels, positions, cur.r, cur.c - 1, level + 1);
                add(que, levels, positions, cur.r, cur.c + 1, level + 1);
            }
        }
        return solved == buildings ? ans : Integer.MAX_VALUE;
    }

    // 0较少时
    public static int shortestDistance1(int[][] grid) {
        int ans = Integer.MAX_VALUE;
        int n = grid.length;
        int m = grid[0].length;
        int buildings = 0;
        Position[][] positions = new Position[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    buildings++;
                }
                positions[i][j] = new Position(i, j, grid[i][j]);
            }
        }
        if (buildings == 0) {
            return 0;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans = Math.min(ans, bfs(positions, buildings, i, j));
            }
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    //

    private static class Info {
        public int r;
        public int c;
        public int v;
        public int t;

        public Info(int row, int col, int val, int th) {
            r = row;
            c = col;
            v = val;
            t = th;
        }
    }

    private static void add(Queue<Info> q, HashMap<Info, Integer> l, Info[][] infos, int i, int j, int level) {
        if (i >= 0 && i < infos.length && j >= 0 && j < infos[0].length && infos[i][j].v == 0 && !l.containsKey(infos[i][j])) {
            l.put(infos[i][j], level);
            q.add(infos[i][j]);
        }
    }

    private static void bfs(Info[][] infos, int i, int j, int[][] distance) {
        HashMap<Info, Integer> levels = new HashMap<>();
        Queue<Info> que = new LinkedList<>();
        Info from = infos[i][j];
        add(que, levels, infos, from.r - 1, from.c, 1);
        add(que, levels, infos, from.r + 1, from.c, 1);
        add(que, levels, infos, from.r, from.c - 1, 1);
        add(que, levels, infos, from.r, from.c + 1, 1);
        while (!que.isEmpty()) {
            Info cur = que.poll();
            int level = levels.get(cur);
            distance[from.t][cur.t] = level;
            add(que, levels, infos, cur.r - 1, cur.c, level + 1);
            add(que, levels, infos, cur.r + 1, cur.c, level + 1);
            add(que, levels, infos, cur.r, cur.c - 1, level + 1);
            add(que, levels, infos, cur.r, cur.c + 1, level + 1);
        }
    }

    public static int shortestDistance2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int ones = 0, zeros = 0;
        Info[][] infos = new Info[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    infos[i][j] = new Info(i, j, 1, ones++);
                } else if (grid[i][j] == 0) {
                    infos[i][j] = new Info(i, j, 0, zeros++);
                } else {
                    infos[i][j] = new Info(i, j, 2, Integer.MAX_VALUE);
                }
            }
        }
        if (ones == 0) {
            return 0;
        }
        int[][] distance = new int[ones][zeros];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (infos[i][j].v == 1) {
                    bfs(infos, i, j, distance);
                }
            }
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < zeros; i++) {
            int sum = 0;
            for (int j = 0; j < ones; j++) {
                if (distance[j][i] == 0) {
                    sum = Integer.MAX_VALUE;
                    break;
                } else {
                    sum += distance[j][i];
                }
            }
            ans = Math.min(ans, sum);
        }
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    //

    private static int bfs(int[][] grid, int[][] dist, int row, int col, int pass, int[] trans) {
        Queue<int[]> que = new LinkedList<>();
        que.offer(new int[]{row, col});
        int level = 0;
        int ans = Integer.MAX_VALUE;
        while (!que.isEmpty()) {
            int size = que.size();
            level++;
            for (int k = 0; k < size; k++) {
                int[] node = que.poll();
                for (int i = 1; i < trans.length; i++) {
                    int nextr = node[0] + trans[i - 1];
                    int nextc = node[1] + trans[i];
                    if (nextr >= 0 && nextr < grid.length && nextc >= 0 && nextc < grid[0].length && grid[nextr][nextc] == pass) {
                        que.offer(new int[]{nextr, nextc});
                        dist[nextr][nextc] += level;
                        ans = Math.min(ans, dist[nextr][nextc]);
                        grid[nextr][nextc]--;
                    }
                }
            }
        }
        return ans;
    }

    public static int shortestDistance3(int[][] grid) {
        int[][] dist = new int[grid.length][grid[0].length];
        int pass = 0;
        int step = Integer.MAX_VALUE;
        // (0, 1) (1, 0) (0, -1) (-1, 0)
        int[] trans = {0, 1, 0, -1, 0};
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    step = bfs(grid, dist, i, j, pass--, trans);
                    // 不连通
                    if (step == Integer.MAX_VALUE) {
                        return -1;
                    }
                }
            }
        }
        return step == Integer.MAX_VALUE ? -1 : step;
    }
}
