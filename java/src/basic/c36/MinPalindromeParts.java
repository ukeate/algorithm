package basic.c36;

// 最少分割回文子串个数
public class MinPalindromeParts {
    public static int min(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        if (s.length() == 1) {
            return 1;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        boolean[][] isP = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            isP[i][i] = true;
        }
        for (int i = 0; i < n - 1; i++) {
            isP[i][i + 1] = str[i] == str[i + 1];
        }
        for (int row = n - 3; row >= 0; row--) {
            for (int col = row + 2; col < n; col++) {
                isP[row][col] = str[row] == str[col] && isP[row + 1][col - 1];
            }
        }
        int[] dp = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = Integer.MAX_VALUE;
        }
        dp[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            for (int end = i; end < n; end++) {
                if (isP[i][end]) {
                    dp[i] = Math.min(dp[i], 1 + dp[end + 1]);
                }
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        String s = "aba12321412321TabaKFK";
        System.out.println(min(s));
    }
}
