package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P132_PalindromePartitioningII {

    private static boolean[][] checkMap(char[] str, int n) {
        boolean[][] ans = new boolean[n][n];
        for (int i = 0; i < n - 1; i++) {
            ans[i][i] = true;
            ans[i][i + 1] = str[i] == str[i + 1];
        }
        ans[n - 1][n - 1] = true;
        for (int i = n - 3; i >= 0; i--) {
            for (int j = i + 2; j < n; j++) {
                ans[i][j] = str[i] == str[j] && ans[i + 1][j - 1];
            }
        }
        return ans;
    }

    public static int minCut(String s) {
        if (s == null || s.length() < 2) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        boolean[][] checkMap = checkMap(str, n);
        int[] dp = new int[n + 1];
        dp[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (checkMap[i][n - 1]) {
                dp[i] = 1;
            } else {
                int next = Integer.MAX_VALUE;
                for (int j = i; j < n; j++) {
                    if (checkMap[i][j]) {
                        next = Math.min(next, dp[j + 1]);
                    }
                }
                dp[i] = 1 + next;
            }
        }
        return dp[0] - 1;
    }

    //

    public static List<String> oneWay(String s) {
        List<String> ans = new ArrayList<>();
        if (s == null || s.length() < 2) {
            ans.add(s);
        } else {
            char[] str = s.toCharArray();
            int n = str.length;
            boolean[][] checkMap = checkMap(str, n);
            int[] dp = new int[n + 1];
            dp[n] = 0;
            for (int i = n - 1; i >= 0; i--) {
                if (checkMap[i][n - 1]) {
                    dp[i] = 1;
                } else {
                    int next = Integer.MAX_VALUE;
                    for (int j = i; j < n; j++) {
                        if (checkMap[i][j]) {
                            next = Math.min(next, dp[j + 1]);
                        }
                    }
                    dp[i] = 1 + next;
                }
            }
            for (int i = 0, j = 1; j <= n; j++) {
                if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
                    ans.add(s.substring(i, j));
                    i = j;
                }
            }
        }
        return ans;
    }

    //

    private static List<String> copy(List<String> list) {
        List<String> ans = new ArrayList<>();
        for (String str : list) {
            ans.add(str);
        }
        return ans;
    }

    private static void process(String s, int i, int j, boolean[][] checkMap, int[] dp, List<String> path, List<List<String>> ans) {
        if (j == s.length()) {
            if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
                path.add(s.substring(i, j));
                ans.add(copy(path));
                path.remove(path.size() - 1);
            }
        } else {
            if (checkMap[i][j - 1] && dp[i] == dp[j] + 1) {
                path.add(s.substring(i, j));
                process(s, j, j + 1, checkMap, dp, path, ans);
                path.remove(path.size() - 1);
            }
            process(s, i, j + 1, checkMap, dp, path, ans);
        }
    }

    public static List<List<String>> allWays(String s) {
        List<List<String>> ans = new ArrayList<>();
        if (s == null || s.length() < 2) {
            List<String> cur = new ArrayList<>();
            cur.add(s);
            ans.add(cur);
        } else {
            char[] str = s.toCharArray();
            int n = str.length;
            boolean[][] checkMap = checkMap(str, n);
            int[] dp = new int[n + 1];
            dp[n] = 0;
            for (int i = n - 1; i >= 0; i--) {
                if (checkMap[i][n - 1]) {
                    dp[i] = 1;
                } else {
                    int next = Integer.MAX_VALUE;
                    for (int j = i; j < n; j++) {
                        if (checkMap[i][j]) {
                            next = Math.min(next, dp[j + 1]);
                        }
                    }
                    dp[i] = 1 + next;
                }
            }
            process(s, 0, 1, checkMap, dp, new ArrayList<>(), ans);
        }
        return ans;
    }

    public static void main(String[] args) {
        String s = "cbbbcbc";
        List<String> ans2 = oneWay(s);
        for (String str : ans2) {
            System.out.print(str + " ");
        }
        System.out.println();

        List<List<String>> ans3 = allWays(s);
        for (List<String> way : ans3) {
            for (String str : way) {
                System.out.print(str + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
