package giant.c13;

import java.text.DecimalFormat;

// 谷歌面试题
// 面值为1~10的牌组成一组，
// 每次你从组里等概率的抽出1~10中的一张
// 下次抽会换一个新的组，有无限组
// 当累加和<17时，你将一直抽牌
// 当累加和>=17且<21时，你将获胜
// 当累加和>=21时，你将失败
// 返回获胜的概率
public class NCardsABWin {
    private static double p1(int cur) {
        if (cur >= 17 && cur < 21) {
            return 1.0;
        }
        if (cur >= 21) {
            return 0.0;
        }
        double w = 0.0;
        for (int i = 1; i <= 10; i++) {
            w += p1(cur + i);
        }
        return w / 10;
    }

    public static double rate1() {
        return p1(0);
    }

    //

    private static double p2(int cur, int n, int a, int b) {
        if (cur >= a && cur < b) {
            return 1.0;
        }
        if (cur >= b) {
            return 0.0;
        }
        double w = 0.0;
        for (int i = 1; i <= n; i++) {
            w += p2(cur + i, n, a, b);
        }
        return w / n;
    }

    // 面值为1~N的牌组成一组，
    // 每次你从组里等概率的抽出1~N中的一张
    // 下次抽会换一个新的组，有无限组
    // 当累加和<a时，你将一直抽牌
    // 当累加和>=a且<b时，你将获胜
    // 当累加和>=b时，你将失败
    public static double rate2(int n, int a, int b) {
        if (n < 1 || a >= b || a < 0 || b < 0) {
            return 0.0;
        }
        if (b - a >= n) {
            return 1.0;
        }
        return p2(0, n, a, b);
    }

    //

    // (i+1) = ((i+2) + (i+3) + (i+4)) / 3
    // (i) = ((i+1) + (i+2) + (i+3)) / 3
    // (i) = ((i+1) + (i+1)*n - (i+n+1)) / n
    private static double p3(int cur, int n, int a, int b) {
        if (cur >= a && cur < b) {
            return 1.0;
        }
        if (cur >= b) {
            return 0.0;
        }
        // (i+1)公式在(a-1)时失效, 公式单算(n >= b-a)
        if (cur == a - 1) {
            return 1.0 * (b - a) / n;
        }
        double w = p3(cur + 1, n, a, b) + p3(cur + 1, n, a, b) * n;
        if (cur + 1 + n < b) {
            w -= p3(cur + 1 + n, n, a, b);
        }
        return w / n;
    }

    public static double rate3(int n, int a, int b) {
        if (n < 1 || a >= b || a < 0 || b < 0) {
            return 0.0;
        }
        if (b - a >= n) {
            return 1.0;
        }
        return p3(0, n, a, b);
    }

    //

    public static double rate4(int n, int a, int b) {
        if (n < 1 || a >= b || a < 0 || b < 0) {
            return 0.0;
        }
        if (b - a >= n) {
            return 1.0;
        }
        double[] dp = new double[b];
        for (int i = a; i < b; i++) {
            dp[i] = 1.0;
        }
        if (a - 1 >= 0) {
            dp[a - 1] = 1.0 * (b - a) / n;
        }
        for (int cur = a - 2; cur >= 0; cur--) {
            double w = dp[cur + 1] + dp[cur + 1] * n;
            if (cur + 1 + n < b) {
                w -= dp[cur + 1 + n];
            }
            dp[cur] = w / n;
        }
        return dp[0];
    }

    //

    public static void main(String[] args) {
        int n = 10, a = 17, b = 21;
        System.out.println(rate1());
        System.out.println(rate2(n, a, b));
        System.out.println(rate3(n, a, b));
        System.out.println(rate4(n, a, b));

        int times = 100000;
        int maxN = 15, maxM = 20;
        DecimalFormat df = new DecimalFormat("#.####");
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            n = (int) (Math.random() * maxN);
            a = (int) (Math.random() * maxM);
            b = (int) (Math.random() * maxM);
            double ans2 = Double.valueOf(df.format(rate2(n, a, b)));
            double ans3 = Double.valueOf(df.format(rate2(n, a, b)));
            double ans4 = Double.valueOf(df.format(rate4(n, a, b)));
            if (ans2 != ans3 || ans2 != ans4) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
