package leetc.top;

public class P1000_MinimumCostToMergeStones {
    private static int process1(int l, int r, int p, int[] arr, int k, int[] presum, int[][][] dp) {
        if (dp[l][r][p] != 0) {
            return dp[l][r][p];
        }
        if (l == r) {
            return p == 1 ? 0 : -1;
        }
        if (p == 1) {
            int next = process1(l, r, k, arr, k, presum, dp);
            if (next == -1) {
                dp[l][r][p] = -1;
                return -1;
            } else {
                dp[l][r][p] = next + presum[r + 1] - presum[l];
                return dp[l][r][p];
            }
        } else {
            int ans = Integer.MAX_VALUE;
            for (int mid = l; mid < r; mid += k - 1) {
                int next1 = process1(l, mid, 1, arr, k, presum, dp);
                int next2 = process1(mid + 1, r, p - 1, arr, k, presum, dp);
                if (next1 != -1 && next2 != -1) {
                    ans = Math.min(ans, next1 + next2);
                } else {
                    dp[l][r][p] = -1;
                    return -1;
                }
            }
            dp[l][r][p] = ans;
            return ans;
        }
    }

    public static int mergeStones1(int[] stones, int k) {
        int n = stones.length;
        if ((n - 1) % (k - 1) > 0) {
            return -1;
        }
        int[] presum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            presum[i + 1] = presum[i] + stones[i];
        }
        int[][][] dp = new int[n][n][k + 1];
        return process1(0, n - 1, 1, stones, k, presum, dp);
    }

}
