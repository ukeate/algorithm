package base.dp2;

public class KillMonster {
    private static long process(int times, int m, int hp) {
        if (times == 0) {
            return hp <= 0 ? 1 : 0;
        }
        if (hp <= 0) {
            return (long) Math.pow(m + 1, times);
        }
        long ways = 0;
        for (int i = 0; i <= m; i++) {
            ways += process(times - 1, m, hp - i);
        }
        return ways;
    }

    // n点血, 每次掉[0,m], 打k次
    public static double possibility(int n, int m, int k) {
        if (n < 1 || m < 1 || k < 1) {
            return 0;
        }
        return (double) process(k, m, n) / Math.pow(m + 1, k);
    }

    //

    public static double dp1(int n, int m, int k) {
        if (n < 1 || m < 1 || k < 1) {
            return 0;
        }
        long[][] dp = new long[k + 1][n + 1];
        dp[0][0] = 1;
        for (int times = 1; times <= k; times++) {
            dp[times][0] = (long) Math.pow(m + 1, times);
            for (int hp = 1; hp <= n; hp++) {
                long ways = 0;
                for (int i = 0; i <= m; i++) {
                    if (hp - i >= 0) {
                        ways += dp[times - 1][hp - i];
                    } else {
                        ways += (long) Math.pow(m + 1, times - 1);
                    }
                }
                dp[times][hp] = ways;
            }
        }
        return ((double) dp[k][n] / Math.pow(m + 1, k));
    }

    public static double dp2(int n, int m, int k) {
        if (n < 1 || m < 1 || k < 1) {
            return 0;
        }
        long[][] dp = new long[k + 1][n + 1];
        dp[0][0] = 1;
        for (int times = 1; times <= k; times++) {
            dp[times][0] = (long) Math.pow(m + 1, times);
            for (int hp = 1; hp <= n; hp++) {
                long ways = dp[times][hp - 1] + dp[times - 1][hp];
                if (hp - 1 - m >= 0) {
                    ways -= dp[times - 1][hp - 1 - m];
                } else {
                    ways -= Math.pow(m + 1, times - 1);
                }
                dp[times][hp] = ways;
            }
        }
        return (double) dp[k][n] / (double) Math.pow(m + 1, k);
    }

    public static void main(String [] args) {
        int times = 1000;
        int maxN = 10;
        int maxM = 10;
        int maxK = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int n = (int) ((maxN + 1) * Math.random());
            int m = (int) ((maxM + 1) * Math.random());
            int k = (int) ((maxK + 1) * Math.random());
            double ans1 = possibility(n, m, k);
            double ans2 = dp1(n, m, k);
            double ans3 = dp2(n, m, k);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3);
                break;
            }
        }
        System.out.println("test end");
    }
}
