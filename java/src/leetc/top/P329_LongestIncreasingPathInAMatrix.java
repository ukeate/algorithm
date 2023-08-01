package leetc.top;

public class P329_LongestIncreasingPathInAMatrix {
    private static boolean canWalk(int[][] matrix, int i1, int j1, int i2, int j2) {
        return i2 >= 0 && i2 < matrix.length && j2 >= 0 && j2 < matrix[0].length && matrix[i1][j1] < matrix[i2][j2];
    }

    private static int lip(int[][] matrix, int i, int j, int[][] dp) {
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        int next = 0;
        if (canWalk(matrix, i, j, i - 1, j)) {
            next = Math.max(next, lip(matrix, i - 1, j, dp));
        }
        if (canWalk(matrix, i, j, i + 1, j)) {
            next = Math.max(next, lip(matrix, i + 1, j, dp));
        }
        if (canWalk(matrix, i, j, i, j - 1)) {
            next = Math.max(next, lip(matrix, i, j - 1, dp));
        }
        if (canWalk(matrix, i, j, i, j + 1)) {
            next = Math.max(next, lip(matrix, i, j + 1, dp));
        }
        dp[i][j] = 1 + next;
        return dp[i][j];
    }

    public static int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            return 0;
        }
        int ans = 0;
        int n = matrix.length, m = matrix[0].length;
        int[][] dp = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans = Math.max(ans, lip(matrix, i, j, dp));
            }
        }
        return ans;
    }
}
