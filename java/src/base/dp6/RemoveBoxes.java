package base.dp6;

// https://leetcode.com/problems/remove-boxes/
public class RemoveBoxes {
    // 当前l同字符的，前边有k个
    private static int process1(int[] boxes, int l, int r, int k, int[][][] dp) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r][k] > 0) {
            return dp[l][r][k];
        }
        // k在l位置消
        int ans = process1(boxes, l + 1, r, 0, dp) + (k + 1) * (k + 1);
        // k传到i位置消
        for (int i = l + 1; i <= r; i++) {
            if (boxes[i] == boxes[l]) {
                ans = Math.max(ans, process1(boxes, l + 1, i - 1, 0, dp) + process1(boxes, i, r, k + 1, dp));
            }
        }
        return ans;
    }

    public static int remove1(int[] boxes) {
        int n = boxes.length;
        int[][][] dp = new int[n][n][n];
        int ans = process1(boxes, 0, n - 1, 0, dp);
        return ans;
    }

    //

    private static int process2(int[] boxes, int l, int r, int k, int[][][] dp) {
        if (l > r) {
            return 0;
        }
        if (dp[l][r][k] > 0) {
            return dp[l][r][k];
        }
        int last = l;
        while (last + 1 <= r && boxes[last + 1] == boxes[l]) {
            last++;
        }
        int pre = k + last - l;
        int ans = (pre + 1) * (pre + 1) + process2(boxes, last + 1, r, 0, dp);
        for (int i = last + 2; i <= r; i++) {
            if (boxes[i] == boxes[l] && boxes[i - 1] != boxes[l]) {
                ans = Math.max(ans, process2(boxes, last + 1, i - 1, 0, dp) + process2(boxes, i, r, pre + 1, dp));
            }
        }
        dp[l][r][k] = ans;
        return ans;
    }

    public static int remove2(int[] boxes) {
        int n = boxes.length;
        int[][][] dp = new int[n][n][n];
        int ans = process2(boxes, 0, n - 1, 0, dp);
        return ans;
    }
}
