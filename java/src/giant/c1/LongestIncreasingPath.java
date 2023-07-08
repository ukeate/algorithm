package giant.c1;

// 矩阵中从小到大最长增长路径
public class LongestIncreasingPath {
    private static int process(int[][] m, int i, int j, int[][] dp) {
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        int up = i > 0 && m[i][j] < m[i - 1][j] ? process(m, i - 1, j, dp) : 0;
        int down = i < (m.length - 1) && m[i][j] < m[i + 1][j] ? process(m, i + 1, j, dp) : 0;
        int left = j > 0 && m[i][j] < m[i][j - 1] ? process(m, i, j - 1, dp) : 0;
        int right = j < (m[0].length - 1) && m[i][j] < m[i][j + 1] ? process(m, i, j + 1, dp) : 0;
        int ans = Math.max(Math.max(up, down), Math.max(left, right)) + 1;
        dp[i][j] = ans;
        return ans;
    }

    public static int longest(int[][] matrix) {
        int ans = 0;
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] dp = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans = Math.max(ans, process(matrix, i, j, dp));
            }
        }
        return ans;
    }

}
