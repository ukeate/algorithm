package base.dp4;

// https://leetcode.com/problems/super-egg-drop
public class ThrowChess {
    // 返回至少扔几次
    private static int process1(int rest, int k) {
        if (rest == 0) {
            return 0;
        }
        if (k == 1) {
            return rest;
        }
        int min = Integer.MAX_VALUE;
        for (int i = 1; i <= rest; i++) {
            min = Math.min(min, Math.max(process1(i - 1, k - 1), process1(rest - i, k)));
        }
        return min + 1;
    }

    public static int times1(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        return process1(level, chess);
    }

    //

    public static int times2(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        if (chess == 1) {
            return level;
        }
        int[][] dp = new int[level + 1][chess + 1];
        for (int i = 1; i < dp.length; i++) {
            dp[i][1] = i;
        }
        for (int i = 1; i < dp.length; i++) {
            for (int j = 2; j < dp[0].length; j++) {
                int min = Integer.MAX_VALUE;
                for (int k = 1; k <= i; k++) {
                    min = Math.min(min, Math.max(dp[k - 1][j - 1], dp[i - k][j]));
                }
                dp[i][j] = min + 1;
            }
        }
        return dp[level][chess];
    }

    //

    public static int times22(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        if (chess == 1) {
            return level;
        }
        int[] preArr = new int [level + 1];
        int[] curArr = new int [level + 1];
        for (int i = 1; i < curArr.length; i++) {
            curArr[i] = i;
        }
        for (int i = 1; i < chess; i++) {
            int[] tmp = preArr;
            preArr = curArr;
            curArr = tmp;
            for (int j = 1; j < curArr.length; j++) {
                int min = Integer.MAX_VALUE;
                for (int k = 1; k <= j; k++) {
                    min = Math.min(min, Math.max(preArr[k - 1], curArr[j - k]));
                }
                curArr[j] = min + 1;
            }
        }
        return curArr[level];
    }

    //

    public static int times3(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        if (chess == 1) {
            return level;
        }
        int[][] dp = new int[level + 1][chess + 1];
        for (int i = 1; i < dp.length; i++) {
            dp[i][1] = i;
        }
        int[][] best = new int[level + 1][chess + 1];
        for (int i = 1; i < dp[0].length; i++) {
            dp[1][i] = 1;
            best[1][i] = 1;
        }
        for (int i = 2; i <= level; i++) {
            for (int j = chess; j > 1; j--) {
                int ans = Integer.MAX_VALUE;
                int bestChoose = -1;
                int down = best[i - 1][j];
                int up = j == chess ? i : best[i][j + 1];
                for (int first = down; first <= up; first++) {
                    int cur = Math.max(dp[first - 1][j - 1], dp[i - first][j]);
                    if (cur <= ans) {
                        ans = cur;
                        bestChoose = first;
                    }
                }
                dp[i][j] = ans + 1;
                best[i][j] = bestChoose;
            }
        }
        return dp[level][chess];
    }

    //

    public static int times4(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        int[] dp = new int[chess];
        int ans = 0;
        while (true) {
            ans++;
            int pre = 0;
            for (int i = 0; i < dp.length; i++) {
                int tmp = dp[i];
                dp[i] = dp[i] + pre + 1;
                pre = tmp;
                if (dp[i] >= level) {
                    return ans;
                }
            }
        }
    }

    //

    private static int log2N(int n) {
        int res = -1;
        while (n != 0) {
            res++;
            n >>>= 1;
        }
        return res;
    }

    public static int times5(int chess, int level) {
        if (chess < 1 || level < 1) {
            return 0;
        }
        int bsTimes = log2N(level) + 1;
        if (chess >= bsTimes) {
            return bsTimes;
        }
        int[] dp = new int[chess];
        int ans = 0;
        while (true) {
            ans++;
            int pre = 0;
            for (int i = 0; i < dp.length; i++) {
                int tmp = dp[i];
                dp[i] = dp[i] + pre + 1;
                pre = tmp;
                if (dp[i] >= level) {
                    return ans;
                }
            }
        }
    }

    public static void main(String[] args) {
        int times = 10;
        int maxLevel = 30;
        int maxChess = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int level = (int) ((maxLevel + 1) * Math.random()) + 1;
            int chess = (int) ((maxChess + 1) * Math.random()) + 1;
//            int ans1 = times1(chess, level);
            int ans2 = times2(chess, level);
            int ans22 = times22(chess, level);
            int ans3 = times3(chess, level);
            int ans4 = times4(chess, level);
            int ans5 = times5(chess, level);
            if (ans2 != ans3 || ans4 != ans5 || ans2 != ans4 || ans2 != ans22) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
