package giant.c10;

// https://leetcode-cn.com/problems/boolean-evaluation-lcci/
public class BooleanEvaluation {
    private static class Info {
        public int t;
        public int f;

        public Info(int tr, int fa) {
            t = tr;
            f = fa;
        }
    }

    private static Info process1(char[] str, int l, int r, Info[][] dp) {
        if (dp[l][r] != null) {
            return dp[l][r];
        }
        int t = 0, f = 0;
        if (l == r) {
            t = str[l] == '1' ? 1 : 0;
            f = str[l] == '0' ? 1 : 0;
        } else {
            for (int split = l + 1; split < r; split += 2) {
                Info li = process1(str, l, split - 1, dp);
                Info ri = process1(str, split + 1, r, dp);
                int a = li.t, b = li.f, c = ri.t, d = ri.f;
                switch (str[split]) {
                    case '&':
                        t += a * c;
                        f += b * c + b * d + a * d;
                        break;
                    case '|':
                        t += a * c + a * d + b * c;
                        f += b * d;
                        break;
                    case '^':
                        t += a * d + b * c;
                        f += a * c + b * d;
                        break;
                }
            }
        }
        dp[l][r] = new Info(t, f);
        return dp[l][r];
    }

    public static int countEval1(String express, int desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        char[] exp = express.toCharArray();
        int n = exp.length;
        Info[][] dp = new Info[n][n];
        Info all = process1(exp, 0, exp.length - 1, dp);
        return desired == 1 ? all.t : all.f;
    }

    //

    public static int countEval2(String express, int desired) {
        if (express == null || express.equals("")) {
            return 0;
        }
        char[] exp = express.toCharArray();
        int n = exp.length;
        int[][][] dp = new int[2][n][n];
        dp[0][0][0] = exp[0] == '0' ? 1 : 0;
        dp[1][0][0] = dp[0][0][0] ^ 1;
        for (int i = 2; i < exp.length; i += 2) {
            dp[0][i][i] = exp[i] == '1' ? 0 : 1;
            dp[1][i][i] = exp[i] == '0' ? 0 : 1;
            for (int j = i - 2; j >= 0; j -= 2) {
                for (int k = j; k < i; k += 2) {
                    if (exp[k + 1] == '&') {
                        dp[1][j][i] += dp[1][j][k] * dp[1][k + 2][i];
                        dp[0][j][i] += (dp[0][j][k] + dp[1][j][k]) * dp[0][k + 2][i] + dp[0][j][k] * dp[1][k + 2][i];
                    } else if (exp[k + 1] == '|') {
                        dp[1][j][i] += (dp[0][j][k] + dp[1][j][k]) * dp[1][k + 2][i] + dp[1][j][k] * dp[0][k + 2][i];
                        dp[0][j][i] += dp[0][j][k] * dp[0][k + 2][i];
                    } else {
                        dp[1][j][i] += dp[0][j][k] * dp[1][k + 2][i] + dp[1][j][k] * dp[0][k + 2][i];
                        dp[0][j][i] += dp[0][j][k] * dp[0][k + 2][i] + dp[1][j][k] * dp[1][k + 2][i];
                    }
                }
            }
        }
        return dp[desired][0][n - 1];
    }
}