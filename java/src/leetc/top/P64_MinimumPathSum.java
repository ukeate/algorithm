package leetc.top;

public class P64_MinimumPathSum {
    public static int minPathSum(int[][] grid) {
        if (grid == null || grid[0] == null) {
            return 0;
        }
        int n = grid.length, m = grid[0].length;
        if (m == 0) {
            return 0;
        }
        int[] dp = new int[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0) {
                    dp[j] = grid[i][j];
                } else {
                    dp[j] = grid[i][j] + Math.min((i > 0 ? dp[j] : Integer.MAX_VALUE),
                            (j > 0 ? dp[j - 1] : Integer.MAX_VALUE));
                }
            }
        }
        return dp[m - 1];
    }
}
