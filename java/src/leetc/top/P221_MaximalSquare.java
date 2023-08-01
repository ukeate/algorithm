package leetc.top;

public class P221_MaximalSquare {
    public static int maximalSquare(char[][] m) {
        if (m == null || m.length == 0 || m[0].length == 0) {
            return 0;
        }
        int ln = m.length;
        int lm = m[0].length;
        // 最长边长
        int[][] dp = new int[ln + 1][lm + 1];
        int max = 0;
        for (int i = 0; i < ln; i++) {
            if (m[i][0] == '1') {
                dp[i][0] = 1;
                max = 1;
            }
        }
        for (int j = 1; j < lm; j++) {
            if (m[0][j] == '1') {
                dp[0][j] = 1;
                max = 1;
            }
        }
        for (int i = 1; i < ln; i++) {
            for (int j = 1; j < lm; j++) {
                if (m[i][j] == '1') {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                    max = Math.max(max, dp[i][j]);
                }
            }
        }
        return max * max;
    }
}
