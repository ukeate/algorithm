package basic.c35;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

// s2删除字符成为s1的子串, 返回最少删除数
public class MinDeleteCost {
    private static class LenComp implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o2.length() - o1.length();
        }
    }

    private static void process(char[] s2, int idx, String path, List<String> subs) {
        if (idx == s2.length) {
            subs.add(path);
            return;
        }
        process(s2, idx + 1, path, subs);
        process(s2, idx + 1, path + s2[idx], subs);
    }

    // s2子序列匹配s1, 适合s2小的情况
    public static int min1(String s1, String s2) {
        List<String> subs = new ArrayList<>();
        process(s2.toCharArray(), 0, "", subs);
        subs.sort(new LenComp());
        for (String sub : subs) {
            if (s1.indexOf(sub) != -1) {
                return s2.length() - sub.length();
            }
        }
        return s2.length();
    }

    //

    private static int distance(char[] s2, char[] s1) {
        int row = s2.length;
        int col = s1.length;
        int[][] dp = new int[row][col];
        dp[0][0] = s2[0] == s1[0] ? 0 : Integer.MAX_VALUE;
        for (int j = 1; j < col; j++) {
            dp[0][j] = Integer.MAX_VALUE;
        }
        for (int i = 1; i < row; i++) {
            dp[i][0] = (dp[i - 1][0] != Integer.MAX_VALUE || s2[i] == s1[0]) ? i : Integer.MAX_VALUE;
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                if (dp[i - 1][j] != Integer.MAX_VALUE) {
                    dp[i][j] = dp[i - 1][j] + 1;
                }
                if (s2[i] == s1[j] && dp[i - 1][j - 1] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
                }
            }
        }
        return dp[row - 1][col - 1];
    }

    // s1的子串，和s2比编辑距离
    public static int min2(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) {
            return s2.length();
        }
        int ans = Integer.MAX_VALUE;
        char[] str2 = s2.toCharArray();
        for (int i = 0; i < s1.length(); i++) {
            for (int j = i + 1; j <= s1.length(); j++) {
                ans = Math.min(ans, distance(str2, s1.substring(i, j).toCharArray()));
            }
        }
        return ans == Integer.MAX_VALUE ? s2.length() : ans;
    }

    //

    public static int min3(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) {
            return s2.length();
        }
        char[] str2 = s2.toCharArray();
        char[] str1 = s1.toCharArray();
        int m = str2.length;
        int n = str1.length;
        int[][] dp = new int[m][n];
        int ans = m;
        for (int l = 0; l < n; l++) {
            // 第一行
            dp[0][l] = str2[0] == str1[l] ? 0 : m;
            // 第一列
            for (int row = 1; row < m; row++) {
                dp[row][l] = (str2[row] == str1[l] || dp[row - 1][l] != m) ? row : m;
            }
            ans = Math.min(ans, dp[m - 1][l]);
            for (int r = l + 1; r < n && r - l < m; r++) {
                int first = r - l;
                // 第一行
                dp[first][r] = (str2[first] == str1[r] && dp[first - 1][r - 1] == 0) ? 0 : m;
                // [0,r-l]行编辑不到r列
                for (int row = first + 1; row < m; row++) {
                    dp[row][r] = m;
                    if (dp[row - 1][r] != m) {
                        dp[row][r] = dp[row - 1][r] + 1;
                    }
                    if (dp[row - 1][r - 1] != m && str2[row] == str1[r]) {
                        dp[row][r] = Math.min(dp[row][r], dp[row - 1][r - 1]);
                    }
                }
                ans = Math.min(ans, dp[m - 1][r]);
            }
        }
        return ans;
    }

    //

    // O(N * M^2), 枚举s1字符作为s2开头
    public static int min4(String s1, String s2) {
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        HashMap<Character, ArrayList<Integer>> map1 = new HashMap<>();
        for (int i = 0; i < str1.length; i++) {
            ArrayList<Integer> list = map1.getOrDefault(str1[i], new ArrayList<>());
            list.add(i);
            map1.put(str1[i], list);
        }
        int ans = 0;
        for (int i = 0; i < str2.length; i++) {
            if (map1.containsKey(str2[i])) {
                ArrayList<Integer> c1List = map1.get(str2[i]);
                for (int j = 0; j < c1List.size(); j++) {
                    int cur1 = c1List.get(j) + 1;
                    int cur2 = i + 1;
                    int count = 1;
                    for (int k = cur2; k < str2.length && cur1 < str1.length; k++) {
                        if (str2[k] == str1[cur1]) {
                            cur1++;
                            count++;
                        }
                    }
                    ans = Math.max(ans, count);
                }
            }
        }
        return s2.length() - ans;
    }

    //

    private static String randomStr(int l, int v) {
        int len = (int) (Math.random() * l);
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ('a' + (int) (v * Math.random()));
        }
        return String.valueOf(str);
    }

    public static void main(String[] args) {
        int times = 10000;
        int s1len = 20;
        int s2len = 10;
        int v = 5;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            String s1 = randomStr(s1len, v);
            String s2 = randomStr(s2len, v);
            int ans1 = min1(s1, s2);
            int ans2 = min2(s1, s2);
            int ans3 = min3(s1, s2);
            int ans4 = min4(s1, s2);
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("test end");
    }
}
