package basic.c53;

import java.util.Stack;

public class BricksFallingWhenHit2 {
    public static class UnionFind {
        private int n;
        private int m;
        private int cellingAll;
        private int[][] grid;
        private boolean[] cellingSet;
        private int[] fatherMap;
        private int[] sizeMap;

        public UnionFind(int[][] matrix) {
            initSpace(matrix);
            initConnect();
        }

        private void initSpace(int[][] matrix) {
            grid = matrix;
            n = grid.length;
            m = grid[0].length;
            int all = n * m;
            cellingAll = 0;
            cellingSet = new boolean[all];
            fatherMap = new int[all];
            sizeMap = new int[all];
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < m; col++) {
                    if (grid[row][col] == 1) {
                        int idx = row * m + col;
                        fatherMap[idx] = idx;
                        sizeMap[idx] = 1;
                        if (row == 0) {
                            cellingSet[idx] = true;
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

        private int find(int row, int col) {
            Stack<Integer> stack = new Stack<>();
            int idx = row * m + col;
            while (idx != fatherMap[idx]) {
                stack.add(idx);
                idx = fatherMap[idx];
            }
            while (!stack.isEmpty()) {
                fatherMap[stack.pop()] = idx;
            }
            return idx;
        }

        private boolean valid(int row, int col) {
            return row >= 0 && row < n && col >= 0 && col < m && grid[row][col] == 1;
        }

        private void union(int r1, int c1, int r2, int c2) {
            if (valid(r1, c1) && valid(r2, c2)) {
                int f1 = find(r1, c1);
                int f2 = find(r2, c2);
                if (f1 == f2) {
                    return;
                }
                int s1 = sizeMap[f1];
                int s2 = sizeMap[f2];
                boolean up1 = cellingSet[f1];
                boolean up2 = cellingSet[f2];
                if (s1 <= s2) {
                    fatherMap[f1] = f2;
                    sizeMap[f2] = s1 + s2;
                    if (up1 ^ up2) {
                        cellingSet[f2] = true;
                        cellingAll += up1 ? s2 : s1;
                    }
                } else {
                    fatherMap[f2] = f1;
                    sizeMap[f1] = s1 + s2;
                    if (up1 ^ up2) {
                        cellingSet[f1] = true;
                        cellingAll += up1 ? s2 : s1;
                    }
                }
            }
        }

        private int finger(int row, int col) {
            grid[row][col] = 1;
            int cur = row * m + col;
            if (row == 0) {
                cellingSet[cur] = true;
                cellingAll++;
            }
            fatherMap[cur] = cur;
            sizeMap[cur] = 1;
            int pre = cellingAll;
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
            int now = cellingAll;
            return now == pre ? 0 : now - pre - 1;
        }
    }

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
        int[][] hits = {{1, 0}, {2, 3}, {0, 3}};
        int[] ans = hit(grid, hits);
        for (int i = 0; i < ans.length; i++) {
            System.out.println(ans[i]);
        }
    }
}
