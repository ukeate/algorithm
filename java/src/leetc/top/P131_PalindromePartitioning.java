package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class P131_PalindromePartitioning {
    private static boolean[][] dp(char[] str) {
        int n = str.length;
        boolean[][] dp = new boolean[n][n];
        for (int i = 0; i < n - 1; i++) {
            dp[i][i] = true;
            dp[i][i + 1] = str[i] == str[i + 1];
        }
        dp[n  - 1][n - 1] = true;
        for (int j = 2; j < n; j++) {
            int row = 0;
            int col = j;
            while (row < n && col < n) {
                dp[row][col] = str[row] == str[col] && dp[row + 1][col - 1];
                row++;
                col++;
            }
        }
        return dp;
    }

    private static List<String> copy(List<String> path) {
        List<String> ans = new ArrayList<>();
        for (String p : path) {
            ans.add(p);
        }
        return ans;
    }

    private static void process(String s, int idx, LinkedList<String> path, boolean[][] dp, List<List<String>> ans) {
        if (idx == s.length()) {
            ans.add(copy(path));
            return;
        }
        for (int end = idx; end < s.length(); end++) {
            if (dp[idx][end]) {
                path.addLast(s.substring(idx, end + 1));
                process(s, end + 1, path, dp, ans);
                path.pollLast();
            }
        }
    }

    public static List<List<String>> partition(String s) {
        boolean[][] dp = dp(s.toCharArray());
        LinkedList<String> path = new LinkedList<>();
        List<List<String>> ans = new ArrayList<>();
        process(s, 0, path, dp, ans);
        return ans;
    }
}
