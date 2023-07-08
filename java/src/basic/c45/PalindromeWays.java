package basic.c45;

// 子序列形成回文的方法数，空串不算
public class PalindromeWays {
    public static int ways1(String str) {
        char[] s = str.toCharArray();
        int len = s.length;
        int[][] dp = new int[len + 1][len + 1];
        for (int i = 0; i <= len; i++) {
            dp[i][i] = 1;
        }
        for (int subLen = 2; subLen <= len; subLen++) {
            for (int l = 1; l <= len - subLen + 1; l++) {
                int r = l + subLen - 1;
                dp[l][r] += dp[l + 1][r];
                dp[l][r] += dp[l][r - 1];
                if (s[l - 1] == s[r - 1]) {
                    // 中间为空时，只保留l和r的1种情况
                    dp[l][r] += 1;
                } else {
                    // 减去被混合的数
                    dp[l][r] -= dp[l + 1][r - 1];
                }
            }
        }
        return dp[1][len];
    }

    //

    public static int ways2(String str) {
        char[] s = str.toCharArray();
        int n = s.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = s[i] == s[i + 1] ? 3 : 2;
        }
        for (int l = n - 3; l >= 0; l--) {
            for (int r = l + 2; r < n; r++) {
                dp[l][r] = dp[l + 1][r] + dp[l][r - 1] - dp[l + 1][r - 1];
                if (s[l] == s[r]) {
                    dp[l][r] += dp[l + 1][r - 1] + 1;
                }
            }
        }
        return dp[0][n - 1];
    }

    public static void main(String[] args) {
        System.out.println(ways1("ABA"));
        System.out.println(ways1("XXY"));
        System.out.println(ways2("ABA"));
        System.out.println(ways2("XXY"));
    }
}
