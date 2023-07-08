package basic.c48;

// 相邻k个合并石子，每次代价是合并的和，求最小代价
public class MergeStones {
    // part表示合并成多少份
    private static int process1(int l, int r, int part, int[] arr, int k, int[] presum, int[][][] dp) {
        if (dp[l][r][part] != 0) {
            return dp[l][r][part];
        }
        // 前置条件不会归纳到 l!=r 的情况
        if (l == r) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        if (part == 1) {
            ans = process1(l, r, k, arr, k, presum, dp) + presum[r + 1] - presum[l];
        } else {
            // 总可以归纳到 l==r 的情况
            for (int mid = l; mid < r; mid += k - 1) {
                int next1 = process1(l, mid, 1, arr, k, presum, dp);
                int next2 = process1(mid + 1, r, part - 1, arr, k, presum, dp);
                ans = Math.min(ans, next1 + next2);
            }
        }
        dp[l][r][part] = ans;
        return ans;
    }

    public static int min1(int[] stones, int k) {
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

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) (maxLen * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int maxLen = 12;
        int maxVal = 100;
        int[] arr = randomArr(maxLen, maxVal);
        int k = (int) (Math.random() * 7) + 2;
        print(arr);
        System.out.println(k);
        System.out.println(min1(arr, k));
    }
}
