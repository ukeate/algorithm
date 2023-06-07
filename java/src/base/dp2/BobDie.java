package base.dp2;

public class BobDie {
    private static long process(int row, int col, int rest, int n, int m) {
        if (row < 0 || row == n || col < 0 || col == m) {
            return 0;
        }
        if (rest == 0) {
            return 1;
        }
        long up = process(row - 1, col, rest - 1, n, m);
        long down = process(row + 1, col, rest - 1, n, m);
        long left = process(row, col - 1, rest - 1, n, m);
        long right = process(row, col + 1, rest - 1, n, m);
        return up + down + left + right;
    }

    // n*m的棋盘，空降(row, col)，走k步不掉下去的概率
    public static double possibility(int row, int col, int k, int n, int m) {
        return (double) process(row, col, k, n, m) / Math.pow(4, k);
    }

    //

    private static long pick(long[][][] dp, int n, int m, int r, int c, int rest) {
        if (r < 0 || r == n || c < 0 || c == m) {
            return 0;
        }
        return dp[r][c][rest];
    }

    public static double dp(int row, int col, int k, int n, int m) {
        long[][][] dp = new long[n][m][k + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dp[i][j][0] = 1;
            }
        }
        for (int rest = 1; rest <= k; rest++) {
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < m; c++) {
                    dp[r][c][rest] = pick(dp, n, m, r - 1, c, rest - 1);
                    dp[r][c][rest] += pick(dp, n, m, r + 1, c, rest - 1);
                    dp[r][c][rest] += pick(dp, n, m, r, c - 1, rest - 1);
                    dp[r][c][rest] += pick(dp, n, m, r, c + 1, rest - 1);
                }
            }
        }
        return (double) dp[row][col][k] / Math.pow(4, k);
    }

    public static void main(String[] args) {
        System.out.println(possibility(6, 6, 10, 50, 50));
        System.out.println(dp(6, 6, 10, 50, 50));
    }
}
