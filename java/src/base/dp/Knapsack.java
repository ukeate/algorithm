package base.dp;

public class Knapsack {
    private static int process(int[] w, int[] v, int idx, int rest) {
        if (rest < 0) {
            return -1;
        }
        if (idx == w.length) {
            return 0;
        }
        int p1 = process(w, v, idx + 1, rest);
        int p2 = 0;
        int next = process(w, v, idx + 1, rest - w[idx]);
        if (next != -1) {
            p2 = v[idx] + next;
        }
        return Math.max(p1, p2);
    }
    public static int maxVal(int[] w, int[] v, int bag) {
        if (w == null || v == null || w.length != v.length || w.length == 0) {
            return 0;
        }
        return process(w, v, 0, bag);
    }

    //

    public static int dp(int[] w, int[] v, int bag) {
        if (w == null || v == null || w.length != v.length || w.length == 0) {
            return 0;
        }
        int n = w.length;
        int[][] dp = new int[n + 1][bag + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int rest = 0; rest <= bag; rest++) {
                int p1 = dp[i + 1][rest];
                int p2 = 0;
                int next = rest - w[i] < 0 ? -1 : dp[i + 1][rest - w[i]];
                if (next != -1) {
                    p2 = v[i] + next;
                }
                dp[i][rest] = Math.max(p1, p2);
            }
        }
        return dp[0][bag];
    }

    public static void main(String[] args) {
        int[] w = {3, 2, 4, 7, 3, 1, 7};
        int[] v = {5, 6, 3, 19, 12, 4, 2};
        int bag = 15;
        System.out.println(maxVal(w, v, bag));
        System.out.println(dp(w, v, bag));
    }
}
