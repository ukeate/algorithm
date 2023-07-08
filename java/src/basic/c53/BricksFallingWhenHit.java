package basic.c53;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

// 打砖块，不连到天花板的会掉下来, 返回每次掉的砖块数
public class BricksFallingWhenHit {

    private static class Dot {
    }

    private static class UnionFind {
        private int[][] grid;
        private Dot[][] dots;
        private int n;
        private int m;
        // 天花板的1数
        private int cellingAll;
        // 天花板的点
        private HashSet<Dot> cellingSet;
        private HashMap<Dot, Dot> fatherMap;
        private HashMap<Dot, Integer> sizeMap;

        public UnionFind(int[][] matrix) {
            initSpace(matrix);
            initConnect();
        }

        private void initSpace(int[][] matrix) {
            grid = matrix;
            n = grid.length;
            m = grid[0].length;
            cellingAll = 0;
            dots = new Dot[n][m];
            cellingSet = new HashSet<>();
            fatherMap = new HashMap<>();
            sizeMap = new HashMap<>();
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < m; col++) {
                    if (grid[row][col] == 1) {
                        Dot cur = new Dot();
                        dots[row][col] = cur;
                        fatherMap.put(cur, cur);
                        sizeMap.put(cur, 1);
                        if (row == 0) {
                            cellingSet.add(cur);
                            cellingAll++;
                        }
                    }
                }
            }
        }

        private void initConnect() {
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < m; col++) {
                    union(row, col, row - 1, col);
                    union(row, col, row + 1, col);
                    union(row, col, row, col - 1);
                    union(row, col, row, col + 1);
                }
            }
        }

        private Dot find(int row, int col) {
            Dot cur = dots[row][col];
            Stack<Dot> stack = new Stack<>();
            while (cur != fatherMap.get(cur)) {
                stack.add(cur);
                cur = fatherMap.get(cur);
            }
            while (!stack.isEmpty()) {
                fatherMap.put(stack.pop(), cur);
            }
            return cur;
        }

        private boolean valid(int row, int col) {
            return row >= 0 && row < n && col >= 0 && col < m && grid[row][col] == 1;
        }

        private void union(int r1, int c1, int r2, int c2) {
            if (valid(r1, c1) && valid(r2, c2)) {
                Dot f1 = find(r1, c1);
                Dot f2 = find(r2, c2);
                if (f1 == f2) {
                    return;
                }
                int s1 = sizeMap.get(f1);
                int s2 = sizeMap.get(f2);
                boolean up1 = cellingSet.contains(f1);
                boolean up2 = cellingSet.contains(f2);
                if (s1 <= s2) {
                    fatherMap.put(f1, f2);
                    sizeMap.put(f2, s1 + s2);
                    if (up1 ^ up2) {
                        cellingSet.add(f2);
                        cellingAll += up1 ? s2 : s1;
                    }
                } else {
                    fatherMap.put(f2, f1);
                    sizeMap.put(f1, s1 + s2);
                    if (up1 ^ up2) {
                        cellingSet.add(f1);
                        cellingAll += up1 ? s2 : s1;
                    }
                }
            }
        }

        public int finger(int row, int col) {
            int pre = cellingAll;
            grid[row][col] = 1;
            Dot cur = new Dot();
            dots[row][col] = cur;
            if (row == 0) {
                cellingSet.add(cur);
                cellingAll++;
            }
            fatherMap.put(cur, cur);
            sizeMap.put(cur, 1);
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
            int now = cellingAll;
            return now == pre ? 0 : now - pre - 1;
        }
    }

    // grid用0、1表示砖块, hits[i][2]表示i时间打的坐标
    public static int[] hit(int[][] grid, int[][] hits) {
        for (int i = 0; i < hits.length; i++) {
            if (grid[hits[i][0]][hits[i][1]] == 1) {
                grid[hits[i][0]][hits[i][1]] = 2;
            }
        }
        UnionFind unionFind = new UnionFind(grid);
        int[] ans = new int[hits.length];
        for (int i = hits.length - 1; i >= 0; i--) {
            if (grid[hits[i][0]][hits[i][1]] == 2) {
                ans[i] = unionFind.finger(hits[i][0], hits[i][1]);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
		int[][] grid = {
				{1, 0, 1, 1, 0},
				{1, 1, 0, 1, 0},
				{1, 0, 0, 1, 1},
				{1, 1, 0, 1, 0}
		};
		int[][] hits = {{1,0},{2,3},{0,3}};
		int[] ans = hit(grid, hits);
		for (int i = 0; i < ans.length; i++) {
			System.out.println(ans[i]);
		}
    }
}
