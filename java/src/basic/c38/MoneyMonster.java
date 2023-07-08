package basic.c38;

// 怪兽武力与要钱数组，求通关最小钱数
public class MoneyMonster {
    private static long process(int[] d, int[] p, int ability, int idx) {
        if (idx == d.length) {
            return 0;
        }
        if (ability < d[idx]) {
            return p[idx] + process(d, p, ability + d[idx], idx + 1);
        } else {
            return Math.min(p[idx] + process(d, p, ability + d[idx], idx + 1),
                    process(d, p, ability, idx + 1));
        }
    }

    public static long min1(int[] d, int[] p) {
        return process(d, p, 0, 0);
    }

    //

    public static long min2(int[] d, int[] p) {
        int sum = 0;
        for (int num : d) {
            sum += num;
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

    public static long min3(int[] d, int[] p) {
        int sum = 0;
        for (int num : p) {
            sum += num;
        }
        int[][] dp = new int[d.length][sum + 1];
        for (int i = 0; i < dp.length; i++) {
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

    private static int[][] randomArr(int maxLen, int maxVal) {
        int size = (int) (maxLen * Math.random()) + 1;
        int[][] arrs = new int[2][size];
        for (int i = 0; i < size; i++) {
            arrs[0][i] = (int) (maxVal * Math.random()) + 1;
            arrs[1][i] = (int) (maxVal * Math.random()) + 1;
        }
        return arrs;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 10;
        int maxVal = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[][] arrs = randomArr(maxLen, maxVal);
            int[] d = arrs[0];
            int[] p = arrs[1];
            long ans1 = min1(d, p);
            long ans2 = min2(d, p);
            long ans3 = min3(d, p);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
