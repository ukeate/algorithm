package leetc.top;

public class P44_WildcardMatching {
    private static boolean process1(char[] s, char[] p, int si, int pi) {
        if (si == s.length) {
            if (pi == p.length) {
                return true;
            } else {
                return p[pi] == '*' && process1(s, p, si, pi + 1);
            }
        }
        if (pi == p.length) {
            return si == s.length;
        }
        if (p[pi] != '?' && p[pi] != '*') {
            return s[si] == p[pi] && process1(s, p, si + 1, pi + 1);
        }
        if (p[pi] == '?') {
            return process1(s, p, si + 1, pi + 1);
        }
        for (int len = 0; len <= s.length - si; len++) {
            if (process1(s, p, si + len, pi + 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMatch1(String s, String p) {
        char[] str = s.toCharArray();
        char[] pp = p.toCharArray();
        return process1(str, pp, 0, 0);
    }

    //

    public static boolean isMatch2(String str, String pattern) {
        char[] s = str.toCharArray();
        char[] p = pattern.toCharArray();
        int n = s.length;
        int m = p.length;
        boolean[][] dp = new boolean[n + 1][m + 1];
        dp[n][m] = true;
        for (int pi = m - 1; pi >= 0; pi--) {
            dp[n][pi] = p[pi] == '*' && dp[n][pi + 1];
        }
        for (int si = n - 1; si >= 0; si--) {
            for (int pi = m - 1; pi >= 0; pi--) {
                if (p[pi] != '*') {
                    dp[si][pi] = (p[pi] == '?' || s[si] == p[pi]) && dp[si + 1][pi + 1];
                } else {
                    dp[si][pi] = dp[si][pi + 1] || dp[si + 1][pi];
                }
            }
        }
        return dp[0][0];
    }
}
