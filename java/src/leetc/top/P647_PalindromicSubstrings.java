package leetc.top;

public class P647_PalindromicSubstrings {
    private static char[] manachers(String str) {
        char[] chs = str.toCharArray();
        char[] ans = new char[str.length() * 2 + 1];
        int idx = 0;
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (i & 1) == 0 ? '#' : chs[idx++];
        }
        return ans;
    }

    private static int[] dp(String s) {
        char[] str = manachers(s);
        int[] pa = new int[str.length];
        int c = -1, r = -1;
        for (int i = 0; i < str.length; i++) {
            pa[i] = r > i ? Math.min(pa[2 * c - i], r - i) : 1;
            while (i + pa[i] < str.length && i - pa[i] > -1) {
                if (str[i + pa[i]] == str[i - pa[i]]) {
                    pa[i]++;
                } else {
                    break;
                }
            }
            if (i + pa[i] > r) {
                r = i + pa[i];
                c = i;
            }
        }
        return pa;
    }

    public static int countSubstrings(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int[] dp = dp(s);
        int ans = 0;
        for (int i = 0; i < dp.length; i++) {
            ans += dp[i] >> 1;
        }
        return ans;
    }

    public static void main(String[] args) {
        countSubstrings("ababa");
    }
}
