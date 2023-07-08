package basic.c42;

// 矩阵值是0或1, 矩阵先从左上到右下，再右下到左上，数字只能获得一份，求最大数字和
public class CherryPickup {
    // A,B同时走，bc可算出
    private static int process(int[][] matrix, int ar, int ac, int br) {
        int n = matrix.length;
        int m = matrix[0].length;
        if (ar == n - 1 && ac == m - 1) {
            return matrix[ar][ac];
        }
        int bc = ar + ac - br;
        int aDownBRight = -1;
        if (ar + 1 < n && bc + 1 < m) {
            aDownBRight = process(matrix, ar + 1, ac, br);
        }
        int aDownBDown = -1;
        if (ar + 1 < n && br + 1 < n) {
            aDownBDown = process(matrix, ar + 1, ac, br + 1);
        }
        int aRightBRight = -1;
        if (ac + 1 < m && bc + 1 < m) {
            aRightBRight = process(matrix, ar, ac + 1, br);
        }
        int aRightBDown = -1;
        if (ac + 1 < m && br + 1 < n) {
            aRightBDown = process(matrix, ar, ac + 1, br + 1);
        }
        int nextBest = Math.max(Math.max(aDownBRight, aDownBDown), Math.max(aRightBRight, aRightBDown));
        if (ar == br) {
            return matrix[ar][ac] + nextBest;
        }
        return matrix[ar][ac] + matrix[br][bc] + nextBest;
    }

    public static int max1(int[][] matrix) {
        return process(matrix, 0, 0, 0);
    }

    //

    private static int process2(int[][] grid, int x1, int y1, int x2, int[][][] dp) {
        if (x1 == grid.length || y1 == grid[0].length || x2 == grid.length || x1 + y1 - x2 == grid[0].length) {
            return Integer.MIN_VALUE;
        }
        if (dp[x1][y1][x2] != Integer.MIN_VALUE) {
            return dp[x1][y1][x2];
        }
        if (x1 == grid.length - 1 && y1 == grid[0].length - 1) {
            dp[x1][y1][x2] = grid[x1][y1];
            return dp[x1][y1][x2];
        }
        int next = Integer.MIN_VALUE;
        next = Math.max(next, process2(grid, x1 + 1, y1, x2 + 1, dp));
        next = Math.max(next, process2(grid, x1 + 1, y1, x2, dp));
        next = Math.max(next, process2(grid, x1, y1 + 1, x2 + 1, dp));
        next = Math.max(next, process2(grid, x1, y1 + 1, x2, dp));
        // -1表示走不通
        if (grid[x1][y1] == -1 || grid[x2][x1 + y1 - x2] == -1 || next == -1) {
            dp[x1][y1][x2] = -1;
            return dp[x1][y1][x2];
        }
        if (x1 == x2) {
            dp[x1][y1][x2] = grid[x1][y1] + next;
            return dp[x1][y1][x2];
        }
        dp[x1][y1][x2] = grid[x1][y1] + grid[x2][x1 + y1 - x2] + next;
        return dp[x1][y1][x2];
    }

    public static int max2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        int[][][] dp = new int[n][m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < n; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }
        int ans = process2(grid, 0, 0, 0, dp);
        return ans < 0 ? 0 : ans;
    }


    public static void main(String[] args) {
        int[][] matrix = {
				{ 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 } };
        System.out.println(max1(matrix));
        System.out.println(max2(matrix));
    }
}
