package leetc.top;

public class P72_EditDistance {
    // 编辑str1到str2
    private static int minDistance(String str1, String str2, int ic, int dc, int rc) {
        if (str1 == null || str2 == null) {
            return 0;
        }
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        char[] longs = s1.length >= s2.length ? s1 : s2;
        char[] shorts = s1.length < s2.length ? s1 : s2;
        if (s1.length < s2.length) {
            int tmp = ic;
            ic = dc;
            dc = tmp;
        }
        int[] dp = new int[shorts.length + 1];
        for (int i = 1; i <= shorts.length; i++) {
            dp[i] = ic * i;
        }
        for (int i = 1; i <= longs.length; i++) {
            int pre = dp[0];
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

    public static int minDistance(String word1, String word2) {
        return minDistance(word1, word2, 1, 1, 1);
    }
}
