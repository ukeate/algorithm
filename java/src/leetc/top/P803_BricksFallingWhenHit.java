package leetc.top;

public class P803_BricksFallingWhenHit {
    private static class UnionFind {
        private int n;
        private int m;
        private int cellingAll;
        private int[][] grid;
        private boolean[] cellingSet;
        private int[] fatherMap;
        private int[] sizeMap;
        private int[] stack;

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
            stack = new int[all];
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
            int stackSize = 0;
            int idx = row * m + col;
            while (idx != fatherMap[idx]) {
                stack[stackSize++] = idx;
                idx = fatherMap[idx];
            }
            while (stackSize != 0) {
                fatherMap[stack[--stackSize]] = idx;
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
                if (f1 != f2) {
                    int s1 = sizeMap[f1];
                    int s2 = sizeMap[f2];
                    boolean v1 = cellingSet[f1];
                    boolean v2 = cellingSet[f2];
                    if (s1 <= s2) {
                        fatherMap[f1] = f2;
                        sizeMap[f2] = s1 + s2;
                        if (v1 ^ v2) {
                            cellingSet[f2] = true;
                            cellingAll += v1 ? s2 : s1;
                        }
                    } else {
                        fatherMap[f2] = f1;
                        sizeMap[f1] = s1 + s2;
                        if (v1 ^ v2) {
                            cellingSet[f1] = true;
                            cellingAll += v1 ? s2 : s1;
                        }
                    }
                }
            }
        }

        private int finger(int row, int col) {
            grid[row][col] = 1;
            int cur = row * m + col;
            int pre = cellingAll;
            if (row == 0) {
                cellingSet[cur] = true;
                cellingAll++;
            }
            fatherMap[cur] = cur;
            sizeMap[cur] = 1;
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
            int now = cellingAll;
            return now == pre ? 0 : now - pre - 1;
        }
    }

    public int[] hitBricks(int[][] grid, int[][] hits) {
        for (int i = 0; i < hits.length; i++) {
            if (grid[hits[i][0]][hits[i][1]] == 1) {
                grid[hits[i][0]][hits[i][1]] = 2;
            }
        }
        UnionFind uf = new UnionFind(grid);
        int[] ans = new int[hits.length];
        for (int i = hits.length - 1; i >= 0; i--) {
            if (grid[hits[i][0]][hits[i][1]] == 2) {
                ans[i] = uf.finger(hits[i][0], hits[i][1]);
            }
        }
        return ans;
    }
}
