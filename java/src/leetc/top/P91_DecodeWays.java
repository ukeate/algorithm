package leetc.top;

public class P91_DecodeWays {
    private static int process(char[] str, int idx) {
        if (idx == str.length) {
            return 1;
        }
        if (str[idx] == '0') {
            return 0;
        }
        int ways = process(str, idx + 1);
        if (idx + 1 == str.length) {
            return ways;
        }
        int num = (str[idx] - '0') * 10 + str[idx + 1] - '0';
        if (num <= 26) {
            ways += process(str, idx + 2);
        }
        return ways;
    }
    public static int numDecodings1(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        return process(str, 0);
    }

    //

    public static int numDecodings2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        int[] dp = new int[n + 1];
        dp[n] = 1;
        for (int i = n - 1; i >= 0; i--) {
            if (str[i] != '0') {
                dp[i] = dp[i + 1];
                if (i + 1 == str.length) {
                    continue;
                }
                int num = (str[i] - '0') * 10 + str[i + 1] - '0';
                if (num <= 26) {
                    dp[i] += dp[i + 2];
                }
            }
        }
        return dp[0];
    }

    //


}
