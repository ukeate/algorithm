package leetc.top;

public class P494_TargetSum {
    private static int process1(int[] arr, int idx, int rest) {
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0;
        }
        return process1(arr, idx + 1, rest - arr[idx])
                + process1(arr, idx + 1, rest + arr[idx]);
    }
    public static int findTargetSumWays1(int[] arr, int s) {
        return process1(arr, 0, s);
    }

    //

    private static int subset(int[] nums, int s) {
        int[] dp = new int[s + 1];
        dp[0] = 1;
        for (int n : nums) {
            for (int i = s; i >= n; i--) {
                dp[i] += dp[i - n];
            }
        }
        return dp[s];
    }

    // 可看作非负
    // 2 * 正数集 = s + SUM
    public static int findTargetSumWays3(int[] arr, int s) {
        int sum = 0;
        for (int n : arr) {
            sum += n;
        }
        return sum < Math.abs(s) || ((s & 1) ^ (sum & 1)) != 0 ? 0 : subset(arr, (s + sum) >> 1);
    }

    public static void main(String[] args) {
        findTargetSumWays3(new int[]{100}, -200);
    }
}
