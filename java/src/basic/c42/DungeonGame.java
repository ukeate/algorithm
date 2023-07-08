package basic.c42;

// 从左上到右下，正数加血负数减血，要求血量一直大于0，求最小初始血量
public class DungeonGame {
    private static int process(int[][] matrix, int n, int m, int row, int col) {
        if (row == n - 1 && col == m - 1) {
            return matrix[n - 1][m - 1] < 0 ? (-matrix[n - 1][m - 1] + 1) : 1;
        }
        if (row == n - 1) {
            int rightNeed = process(matrix, n, m, row, col + 1);
            if (matrix[row][col] < 0) {
                return -matrix[row][col] + rightNeed;
            } else if (matrix[row][col] >= rightNeed) {
                return 1;
            } else {
                return rightNeed - matrix[row][col];
            }
        }
        if (col == m - 1) {
            int downNeed = process(matrix, n, m, row + 1, col);
            if (matrix[row][col] < 0) {
                return -matrix[row][col] + downNeed;
            } else if (matrix[row][col] >= downNeed) {
                return 1;
            } else {
                return downNeed - matrix[row][col];
            }
        }
        int need = Math.min(process(matrix, n, m, row, col + 1),
                process(matrix, n, m, row + 1, col));
        if (matrix[row][col] < 0) {
            return -matrix[row][col] + need;
        } else if (matrix[row][col] >= need) {
            return 1;
        } else {
            return need - matrix[row][col];
        }
    }

    public static int min1(int[][] matrix) {
        return process(matrix, matrix.length, matrix[0].length, 0, 0);
    }

    //

    public static int min2(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 1;
        }
        int row = m.length;
        int col = m[0].length;
        int[][] dp = new int[row--][col--];
        dp[row][col] = m[row][col] > 0 ? 1 : -m[row][col] + 1;
        for (int j = col - 1; j >= 0; j--) {
            dp[row][j] = Math.max(dp[row][j + 1] - m[row][j], 1);
        }
        int right = 0;
        int down = 0;
        for (int i = row - 1; i >= 0; i--) {
            dp[i][col] = Math.max(dp[i + 1][col] - m[i][col], 1);
            for (int j = col - 1; j >= 0; j--) {
                right = Math.max(dp[i][j + 1] - m[i][j], 1);
                down = Math.max(dp[i + 1][j] - m[i][j], 1);
                dp[i][j] = Math.min(right, down);
            }
        }
        return dp[0][0];
    }

    //

    public static int min3(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 1;
        }
        int more = Math.max(m.length, m[0].length);
        int less = Math.min(m.length, m[0].length);
        boolean rowMore = more == m.length;
        int[] dp = new int[less];
        int tmp = m[m.length - 1][m[0].length - 1];
        dp[less - 1] = tmp > 0 ? 1 : -tmp + 1;
        int row = 0, col = 0;
        for (int j = less - 2; j >= 0; j--) {
            row = rowMore ? more - 1 : j;
            col = rowMore ? j : more - 1;
            dp[j] = Math.max(dp[j + 1] - m[row][col], 1);
        }
        for (int i = more - 2; i >= 0; i--) {
            row = rowMore ? i : less - 1;
            col = rowMore ? less - 1 : i;
            dp[less - 1] = Math.max(dp[less - 1] - m[row][col], 1);
            for (int j = less - 2; j >= 0; j--) {
                row = rowMore ? i : j;
                col = rowMore ? j : i;
                dp[j] = Math.min(Math.max(dp[j] - m[row][col], 1),
                        Math.max(dp[j + 1] - m[row][col], 1));
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        int[][] map = {{-2, -3, 3}, {-5, -10, 1}, {10, 30, -5}};
        System.out.println(min1(map));
        System.out.println(min2(map));
        System.out.println(min3(map));
    }
}
