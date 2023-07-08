package basic.c48;

// 匹配串中有. *, 返回是否匹配
public class RegularExpressionMatch {
    public static boolean isValid(char[] s, char[] e) {
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

    private static boolean process(char[] s, char[] e, int si, int ei) {
        if (ei == e.length) {
            return si == s.length;
        }
        // 为了不在ei面对*
        // ei+1 不是*
        if (ei == e.length - 1 || e[ei + 1] != '*') {
            return si < s.length
                    && (e[ei] == s[si] || e[ei] == '.')
                    && process(s, e, si + 1, ei + 1);
        }
        // ei+1 是*
        while (si < s.length && (e[ei] == s[si] || e[ei] == '.')) {
            if (process(s, e, si, ei + 2)) {
                return true;
            }
            si++;
        }
        return process(s, e, si, ei + 2);
    }

    private static boolean match(String str, String exp) {
        if (str == null || exp == null) {
            return false;
        }
        char[] s = str.toCharArray();
        char[] e = exp.toCharArray();
        return isValid(s, e) && process(s, e, 0, 0);
    }

    //

    // 填倒数2列与最后一行
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

    public static boolean dp(String str, String exp) {
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
                    int si = i;
                    while (si < s.length && (s[si] == e[j] || e[j] == '.')) {
                        if (dp[si][j + 2]) {
                            dp[i][j] = true;
                            break;
                        }
                        si++;
                    }
                    if (dp[i][j] != true) {
                        dp[i][j] = dp[si][j + 2];
                    }
                }
            }
        }
        return dp[0][0];
    }

    public static void main(String[] args) {
        String str = "abcccdefg";
        String exp = "ab.*d.*e.*";
        System.out.println(match(str, exp));
        System.out.println(dp(str, exp));
    }
}