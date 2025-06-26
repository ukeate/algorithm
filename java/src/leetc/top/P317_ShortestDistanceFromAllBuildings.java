package leetc.top;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 317. 离建筑物最近的距离 (Shortest Distance from All Buildings)
 * 
 * 问题描述：
 * 你想要在一个空地上建一栋房子，使得从这个房子到所有建筑物的距离之和最小。
 * 
 * 给定一个二维网格：
 * - 0 表示空地，你可以自由通过
 * - 1 表示建筑物，你不能通过  
 * - 2 表示障碍物，你不能通过
 * 
 * 你只能水平或垂直移动。返回最短距离和，如果无法到达所有建筑物则返回 -1。
 * 
 * 示例：
 * 输入：grid = [[1,0,2,0,1],[0,0,0,0,0],[0,0,1,0,0]]
 * 输出：7
 * 解释：给定三个建筑物 (0,0), (0,4), (2,2) 和一个障碍物 (0,2)。
 * 在位置 (1,2) 建房子能够使到所有建筑物的距离和最小，总距离为 3+3+1=7。
 * 
 * 解法思路：
 * 多源BFS + 距离累加：
 * 
 * 1. 基本策略：
 *    - 从每个建筑物出发，使用BFS计算到所有空地的距离
 *    - 累加每个空地到所有建筑物的距离和
 *    - 找到距离和最小的空地位置
 * 
 * 2. 优化思路：
 *    - 第一次BFS时，标记所有可达的空地
 *    - 后续BFS只考虑前面能到达所有建筑物的空地
 *    - 使用标记值递减的技巧过滤无效空地
 * 
 * 3. 算法步骤：
 *    - 对每个建筑物进行BFS
 *    - 累加距离到distances数组
 *    - 更新空地的可达标记
 *    - 返回能到达所有建筑物的空地的最小距离和
 * 
 * 核心思想：
 * - 多源BFS：从每个建筑物出发探索可达范围
 * - 距离累加：统计每个空地到所有建筑物的总距离
 * - 渐进过滤：通过标记值递减逐步筛选有效空地
 * 
 * 关键技巧：
 * - 使用empty标记值区分不同轮次的BFS
 * - 只有被所有建筑物访问过的空地才是有效候选
 * - BFS保证计算的是最短距离
 * 
 * 时间复杂度：O(m²n²) - 每个建筑物都要遍历整个网格
 * 空间复杂度：O(mn) - 距离数组和BFS队列
 * 
 * LeetCode链接：https://leetcode.com/problems/shortest-distance-from-all-buildings/
 */
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

    /**
     * 计算到所有建筑物的最短距离和
     * 
     * 算法流程：
     * 1. 遍历网格，找到所有建筑物位置
     * 2. 对每个建筑物执行BFS，计算到所有可达空地的距离
     * 3. 累加每个空地的总距离，并标记访问轮次
     * 4. 找到能被所有建筑物访问且总距离最小的空地
     * 
     * @param grid 二维网格
     * @return 最短距离和，无法到达所有建筑物则返回-1
     */
    public int shortestDistance(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int[][] distances = new int[m][n];  // 累计距离数组
        int empty = 0;                      // 空地标记值（递减）
        int buildingCount = 0;              // 建筑物总数
        
        // 四个方向：上、下、左、右
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        // 统计建筑物数量并执行BFS
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {  // 找到建筑物
                    buildingCount++;
                    // 从当前建筑物出发进行BFS
                    if (!bfs(grid, i, j, distances, empty, directions)) {
                        return -1;  // 存在建筑物无法到达任何空地
                    }
                    empty--;  // 更新空地标记值，用于下一轮BFS
                }
            }
        }
        
        // 寻找最短距离和
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 只考虑被所有建筑物访问过的空地
                if (grid[i][j] == empty && distances[i][j] < minDistance) {
                    minDistance = distances[i][j];
                }
            }
        }
        
        return minDistance == Integer.MAX_VALUE ? -1 : minDistance;
    }
    
    /**
     * 从指定建筑物出发的BFS搜索
     * 
     * 搜索过程：
     * 1. 从建筑物位置开始BFS
     * 2. 只访问标记为empty的空地（确保是前面轮次都能到达的）
     * 3. 计算距离并累加到distances数组
     * 4. 更新访问标记为empty-1
     * 
     * @param grid 原始网格
     * @param startI 建筑物起始行
     * @param startJ 建筑物起始列
     * @param distances 累计距离数组
     * @param empty 当前轮次的空地标记值
     * @param directions 移动方向数组
     * @return 是否至少访问了一个空地
     */
    private boolean bfs(int[][] grid, int startI, int startJ, int[][] distances, 
                       int empty, int[][] directions) {
        int m = grid.length;
        int n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startI, startJ});
        
        int distance = 0;    // 当前BFS层的距离
        boolean foundEmpty = false;  // 是否找到可达的空地
        
        // BFS层序遍历
        while (!queue.isEmpty()) {
            distance++;  // 进入下一层，距离+1
            int size = queue.size();
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                
                // 探索四个方向
                for (int[] dir : directions) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    
                    // 边界检查和访问条件检查
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == empty) {
                        // 累加距离到该空地
                        distances[nx][ny] += distance;
                        // 标记该空地已被当前建筑物访问
                        grid[nx][ny] = empty - 1;
                        // 将该空地加入下一层搜索
                        queue.offer(new int[]{nx, ny});
                        foundEmpty = true;
                    }
                }
            }
        }
        
        return foundEmpty;  // 返回是否找到了可达的空地
    }
}
