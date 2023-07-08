package basic.c35;

// s1编辑到s2
public class EditCost {
    // ic是插入代价, dc是删除代价, rc是替换代价
    public static int min1(String s1, String s2, int ic, int dc, int rc) {
        if (s1 == null || s2 == null) {
            return 0;
        }
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int n = str1.length + 1;
        int m = str2.length + 1;
        int[][] dp = new int[n][m];
        for (int i = 1; i < n; i++) {
            dp[i][0] = dc * i;
        }
        for (int j = 1; j < m; j++) {
            dp[0][j] = ic * j;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                if (str1[i - 1] == str2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + rc;
                }
                dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + ic);
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + dc);
            }
        }
        return dp[n - 1][m - 1];
    }

    //

    public static int min2(String str1, String str2, int ic, int dc, int rc) {
        if (str1 == null || str2 == null) {
            return 0;
        }
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        char[] longs = s1.length >= s2.length ? s1 : s2;
        char[] shorts = s1.length < s2.length ? s1 : s2;
        // longs编辑到shorts
        if (s1.length < s2.length) {
            int tmp = ic;
            ic = dc;
            dc = tmp;
        }
        int[] dp = new int[shorts.length + 1];
        // 0行
        for (int i = 1; i <= shorts.length; i++) {
            dp[i] = ic * i;
        }
        for (int i = 1; i <= longs.length; i++) {
            int pre = dp[0];
            // 0列
            dp[0] = dc * i;
            for (int j = 1; j <= shorts.length; j++) {
                int tmp = dp[j];
                if (longs[i - 1] == shorts[j - 1]) {
                    dp[j] = pre;
                } else {
                    dp[j] = pre + rc;
                }
                dp[j] = Math.min(dp[j], dp[j - 1] + ic);
                dp[j] = Math.min(dp[j], tmp + dc);
                pre = tmp;
            }
        }
        return dp[shorts.length];
    }

    //

    public static void main(String[] args) {
        String s1 = "ab12cd3";
        String s2 = "abcdf";
        System.out.println(min1(s1, s2, 5, 3, 2));
        System.out.println(min2(s1, s2, 5, 3, 2));

        s1 = "abcdf";
        s2 = "ab12cd3";
        System.out.println(min1(s1, s2, 3, 2, 4));
        System.out.println(min2(s1, s2, 3, 2, 4));

        s1 = "";
        s2 = "ab12cd3";
        System.out.println(min1(s1, s2, 1, 7, 5));
        System.out.println(min2(s1, s2, 1, 7, 5));

        s1 = "abcdf";
        s2 = "";
        System.out.println(min1(s1, s2, 2, 9, 8));
        System.out.println(min2(s1, s2, 2, 9, 8));
    }
}
