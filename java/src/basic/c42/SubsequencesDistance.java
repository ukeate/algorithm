package basic.c42;

// 两字符串子序列相同的数量，删除位置不同即不同匹配方法
public class SubsequencesDistance {
    private static int process(char[] s, char[] t, int i, int j) {
        if (j == 0) {
            return 1;
        }
        if (i == 0) {
            return 0;
        }
        int res = process(s, t, i - 1, j);
        if (s[i - 1] == t[j - 1]) {
            res += process(s, t, i - 1, j - 1);
        }
        return res;
    }

    public static int ways1(String s, String t) {
        char[] ss = s.toCharArray();
        char[] tt = t.toCharArray();
        return process(ss, tt, s.length(), t.length());
    }

    //

    public static int ways2(String ss, String tt) {
        char[] s = ss.toCharArray();
        char[] t = tt.toCharArray();
        int[][] dp = new int[s.length + 1][t.length + 1];
        for (int j = 0; j <= t.length; j++) {
            dp[0][j] = 0;
        }
        for (int i = 0; i <= s.length; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= s.length; i++) {
            for (int j = 1; j <= t.length; j++) {
                dp[i][j] = dp[i - 1][j] + (s[i - 1] == t[j - 1] ? dp[i - 1][j - 1] : 0);
            }
        }
        return dp[s.length][t.length];
    }

    //

    public static int min3(String ss, String tt) {
        char[] s = ss.toCharArray();
        char[] t = tt.toCharArray();
        int[] dp = new int[t.length - 1];
        dp[0] = 1;
        for (int i = 1; i <= s.length; i++) {
            for (int j = t.length; j >= 1; j--) {
                dp[j] += s[i - 1] == t[j - 1] ? dp[j - 1] : 0;
            }
        }
        return dp[t.length];
    }
}
