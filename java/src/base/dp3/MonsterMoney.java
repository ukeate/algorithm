package base.dp3;

// 怪兽有能力和钱，给钱变成个人能力，能力高时直接通过,求最小钱数
public class MonsterMoney {
    private static long process1(int[] d, int[] p, int ability, int idx) {
        if (idx == d.length) {
            return 0;
        }
        if (ability < d[idx]) {
            return p[idx] + process1(d, p, ability + d[idx], idx + 1);
        } else {
            return Math.min(p[idx] + process1(d, p, ability + d[idx], idx + 1),
                    process1(d, p, ability, idx + 1));
        }
    }

    public static long min1(int[] d, int[] p) {
        return process1(d, p, 0, 0);
    }

    //

    private static long process2(int[] d, int[] p, int idx, int money) {
        if (idx == -1) {
            return money == 0 ? 0 : -1;
        }

        long ability = process2(d, p, idx - 1, money);
        long p1 = -1;
        if (ability != -1 && ability >= d[idx]) {
            p1 = ability;
        }
        long ability2 = process2(d, p, idx - 1, money - p[idx]);
        long p2 = -1;
        if (ability2 != -1) {
            p2 = d[idx] + ability2;
        }
        return Math.max(p1, p2);
    }

    public static long min2(int[] d, int[] p) {
        int allMoney = 0;
        for (int i = 0; i < p.length; i++) {
            allMoney += p[i];
        }
        int n = d.length;
        for (int money = 0; money < allMoney; money++) {
            if (process2(d, p, n - 1, money) != -1) {
                return money;
            }
        }
        return allMoney;
    }

    //

    public static long dp1(int[] d, int[] p) {
        int sum = 0;
        for (int ability : d) {
            sum += ability;
        }
        long[][] dp = new long[d.length + 1][sum + 1];
        for (int cur = d.length - 1; cur >= 0; cur--) {
            for (int hp = 0; hp <= sum; hp++) {
                if (hp + d[cur] > sum) {
                    continue;
                }
                if (hp < d[cur]) {
                    dp[cur][hp] = p[cur] + dp[cur + 1][hp + d[cur]];
                } else {
                    dp[cur][hp] = Math.min(p[cur] + dp[cur + 1][hp + d[cur]], dp[cur + 1][hp]);
                }
            }
        }
        return dp[0][0];
    }

    //

    public static long dp2(int[] d, int[] p) {
        int sum = 0;
        for (int money : p) {
            sum += money;
        }
        int[][] dp = new int[d.length][sum + 1];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j <= sum; j++) {
                dp[i][j] = -1;
            }
        }
        dp[0][p[0]] = d[0];
        for (int i = 1; i < d.length; i++) {
            for (int j = 0; j <= sum; j++) {
                if (j >= p[i] && dp[i - 1][j - p[i]] != -1) {
                    dp[i][j] = dp[i - 1][j - p[i]] + d[i];
                }
                if (dp[i - 1][j] >= d[i]) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
                }
            }
        }
        int ans = 0;
        for (int j = 0; j <= sum; j++) {
            if (dp[d.length - 1][j] != -1) {
                ans = j;
                break;
            }
        }
        return ans;
    }

    //

    public static int[][] randomTwoArr(int maxLen, int maxVal) {
        int size = (int) ((maxLen + 1) * Math.random()) + 1;
        int[][] res = new int[2][size];
        for (int i = 0; i < size; i++) {
            res[0][i] = (int) ((maxVal + 1) * Math.random()) + 1;
            res[1][i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return res;
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 10;
        int maxVal = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[][] twoArr = randomTwoArr(maxLen, maxVal);
            int[] d = twoArr[0];
            int[] p = twoArr[1];
            long ans1 = min1(d, p);
            long ans2 = min2(d, p);
            long ans3 = dp1(d, p);
            long ans4 = dp2(d, p);
            if (ans1 != ans2 || ans2 != ans3 || ans1 != ans4) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("test end");
    }
}
