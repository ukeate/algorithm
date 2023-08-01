package leetc.top;

public class P10_RegularExpressionMatching {
    private static boolean isValid(char[] s, char[] e) {
        for (int i = 0; i < s.length; i++) {
            if (s[i] == '*' || s[i] == '.') {
                return false;
            }
        }
        for (int i = 0; i < e.length; i++) {
            if (e[i] == '*' && (i == 0 || e[i - 1] == '*')) {
                return false;
            }
        }
        return true;
    }

    private static boolean[][] initDp(char[] s, char[] e) {
        int sl = s.length;
        int el = e.length;
        boolean[][] dp = new boolean[sl + 1][el + 1];
        dp[sl][el] = true;
        for (int j = el - 2; j >= 0; j = j - 2) {
            if (e[j] != '*' && e[j + 1] == '*') {
                dp[sl][j] = true;
            } else {
                break;
            }
        }
        if (sl > 0 && el > 0) {
            if ((e[el - 1] == '.' || s[sl - 1] == e[el - 1])) {
                dp[sl - 1][el - 1] = true;
            }
        }
        return dp;
    }

    public static boolean isMatch(String str, String exp) {
        if (str == null || exp == null) {
            return false;
        }
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        if (!isValid(s, e)) {
            return false;
        }
        boolean[][] dp = initDp(s, e);
        for (int i = s.length - 1; i >= 0; i--) {
            for (int j = e.length - 2; j >= 0; j--) {
                if (e[j + 1] != '*') {
                    dp[i][j] = (s[i] == e[j] || e[j] == '.') && dp[i + 1][j + 1];
                } else {
                    // 消s
                    int si = i;
                    for (; si < s.length && (s[si] == e[j] || e[j] == '.'); si++) {
                        if (dp[si][j + 2]) {
                            dp[i][j] = true;
                            break;
                        }
                    }
                    // 消e
                    if (dp[i][j] != true) {
                        dp[i][j] = dp[si][j + 2];
                    }
                }
            }
        }
        return dp[0][0];
    }
}
