package leetc.top;

public class P639_DecodeWaysII {
    private static int f(char[] str, int i) {
        if (i == str.length) {
            return 1;
        }
        if (str[i] == '0') {
            return 0;
        }
        if (str[i] != '*') {
            // 转1个
            int p1 = f(str, i + 1);
            if (i + 1 == str.length) {
                return p1;
            }
            // 转2个
            if (str[i + 1] != '*') {
                int num = (str[i] - '0') * 10 + str[i + 1] - '0';
                int p2 = 0;
                if (num < 27) {
                    p2 = f(str, i + 2);
                }
                return p1 + p2;
            } else {
                int p2 = 0;
                if (str[i] < '3') {
                    p2 = f(str, i + 2) * (str[i] == '1' ? 9 : 6);
                }
                return p1 + p2;
            }
        } else {
            // 转1个
            int p1 = 9 * f(str, i + 1);
            if (i + 1 == str.length) {
                return p1;
            }
            // 转2个
            if (str[i + 1] != '*') {
                int p2 = (str[i + 1] < '7' ? 2 : 1) * f(str, i + 2);
                return p1 + p2;
            } else {
                // 9: 11-19, 6: 21-26
                int p2 = 15 * f(str, i + 2);
                return p1 + p2;
            }
        }
    }

    public static int numDecodings1(String s) {
        return f(s.toCharArray(), 0);
    }

    //

    private static long mod = 1000000007;

    private static long ways1(char[] s, int i, long[] dp) {
        if (i == s.length) {
            return 1;
        }
        if (s[i] == '0') {
            return 0;
        }
        if (dp[i] != 0) {
            return dp[i];
        }
        long ans = ways1(s, i + 1, dp) * (s[i] == '*' ? 9 : 1);
        if (s[i] == '1' || s[i] == '2' || s[i] == '*') {
            if (i + 1 < s.length) {
                if (s[i + 1] == '*') {
                    ans += ways1(s, i + 2, dp) * (s[i] == '*' ? 15 : (s[i] == '1' ? 9 : 6));
                } else {
                    if (s[i] == '*') {
                        ans += ways1(s, i + 2, dp) * (s[i + 1] < '7' ? 2 : 1);
                    } else {
                        ans += ((s[i] - '0') * 10 + s[i + 1] - '0') < 27 ? ways1(s, i + 2, dp) : 0;
                    }
                }
            }
        }
        ans %= mod;
        dp[i] = ans;
        return ans;
    }

    public static int numDecodings2(String str) {
        long[] dp = new long[str.length()];
        return (int) ways1(str.toCharArray(), 0, dp);
    }

    //

    public static int numDecodings3(String str) {
        char[] s = str.toCharArray();
        int n = s.length;
        // a=[i+2], b=[i+1], c=[i]
        long a = 1, b = 1, c = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (s[i] != '0') {
                c = b * (s[i] == '*' ? 9 : 1);
                if (s[i] == '1' || s[i] == '2' || s[i] == '*') {
                    if (i + 1 < n) {
                        if (s[i + 1] == '*') {
                            c += a * (s[i] == '*' ? 15 : (s[i] == '1' ? 9 : 6));
                        } else {
                            if (s[i] == '*') {
                                c += a * (s[i + 1] < '7' ? 2 : 1);
                            } else {
                                c += a * (((s[i] - '0') * 10 + s[i + 1] - '0') < 27 ? 1 : 0);
                            }
                        }
                    }
                }
            }
            c %= mod;
            a = b;
            b = c;
            c = 0;
        }
        return (int) b;
    }

}
