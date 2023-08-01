package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P1312_MinimumInsertionStepsToMakeAStringPalindrome {
    public static int minInsertions(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
        }
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
                if (str[i] == str[j]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }

    //

    public static String oneWay(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
        }
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
                if (str[i] == str[j]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
        }

        int l = 0, r = n - 1;
        char[] ans = new char[n + dp[l][r]];
        int ansl = 0, ansr = ans.length - 1;
        while (l < r) {
            if (dp[l][r - 1] == dp[l][r] - 1) {
                ans[ansl++] = str[r];
                ans[ansr--] = str[r--];
            } else if (dp[l + 1][r] == dp[l][r] - 1) {
                ans[ansl++] = str[l];
                ans[ansr--] = str[l++];
            } else {
                ans[ansl++] = str[l++];
                ans[ansr--] = str[r--];
            }
        }
        if (l == r) {
            ans[ansl] = str[l];
        }
        return String.valueOf(ans);
    }

    //

    public static void process(char[] str, int[][] dp, int l, int r, char[] path, int pl, int pr, List<String> ans) {
        if (l >= r) {
            if (l == r) {
                path[pl] = str[l];
            }
            ans.add(String.valueOf(path));
        } else {
            if (dp[l][r - 1] == dp[l][r] - 1) {
                path[pl] = str[r];
                path[pr] = str[r];
                process(str, dp, l, r - 1, path, pl + 1, pr - 1, ans);
            }
            if (dp[l + 1][r] == dp[l][r] - 1) {
                path[pl] = str[l];
                path[pr] = str[l];
                process(str, dp, l + 1, r, path, pl + 1, pr - 1, ans);
            }
            if (str[l] == str[r] && (l + 1 == r || dp[l + 1][r - 1] == dp[l][r])) {
                path[pl] = str[l];
                path[pr] = str[r];
                process(str, dp, l + 1, r - 1, path, pl + 1, pr - 1, ans);
            }
        }
    }

    public static List<String> allWays(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() < 2) {
            ans.add(s);
            return ans;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n][n];
        for (int i = 0; i < n - 1; i++) {
            dp[i][i + 1] = str[i] == str[i + 1] ? 0 : 1;
        }
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]) + 1;
                if (str[i] == str[j]) {
                    dp[i][j] = Math.min(dp[i][j], dp[i + 1][j - 1]);
                }
            }
        }
        int m = n + dp[0][n - 1];
        char[] path = new char[m];
        process(str, dp, 0, n - 1, path, 0, m - 1, ans);
        return ans;
    }

    public static void main(String[] args) {
        String s = "mbadm";
        String ans2 = oneWay(s);
        System.out.println(ans2);
        List<String> ans3 = allWays(s);
        for (String way : ans3) {
            System.out.println(way);
        }
        System.out.println();
    }
}
