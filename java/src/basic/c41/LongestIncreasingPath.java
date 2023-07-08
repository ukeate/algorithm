package basic.c41;

// 矩阵中最长增长路径长度
public class LongestIncreasingPath {
    private static int process(int[][] matrix, int i, int j, int[][] dp) {
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length) {
            return -1;
        }
        if (dp[i][j] != 0) {
            return dp[i][j];
        }
        int up = 0;
        int down = 0;
        int left = 0;
        int right = 0;
        if (i - 1 >= 0 && matrix[i - 1][j] > matrix[i][j]) {
            up = process(matrix, i - 1, j, dp);
        }
        if (i + 1 < matrix.length && matrix[i + 1][j] > matrix[i][j]) {
            down = process(matrix, i + 1, j, dp);
        }
        if (j - 1 >= 0 && matrix[i][j - 1] > matrix[i][j]) {
            left = process(matrix, i, j - 1, dp);
        }
        if (j + 1 < matrix[0].length && matrix[i][j + 1] > matrix[i][j]) {
            right = process(matrix, i, j + 1, dp);
        }
        dp[i][j] = 1 + Math.max(Math.max(up, down), Math.max(left, right));
        return dp[i][j];
    }

    public static int max(int[][] matrix) {
        int ans = Integer.MIN_VALUE;
        int[][] dp = new int[matrix.length][matrix[0].length];
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                ans = Math.max(ans, process(matrix, row, col, dp));
            }
        }
        return ans;
    }
}
