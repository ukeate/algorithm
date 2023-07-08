package giant.c1;

import java.util.HashMap;

// https://leetcode.com/problems/target-sum/
// 添加加减号，得到目标数，返回方法数
public class TargetSum {
    private static int process1(int[] arr, int idx, int rest) {
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0;
        }
        return process1(arr, idx + 1, rest - arr[idx]) + process1(arr, idx + 1, rest + arr[idx]);
    }

    public static int ways1(int[] arr, int s) {
        return process1(arr, 0, s);
    }

    //

    private static int process2(int[] arr, int idx, int rest, HashMap<Integer, HashMap<Integer, Integer>> dp) {
        if (dp.containsKey(idx) && dp.get(idx).containsKey(rest)) {
            return dp.get(idx).get(rest);
        }
        int ans = 0;
        if (idx == arr.length) {
            ans = rest == 0 ? 1 : 0;
        } else {
            ans = process2(arr, idx + 1, rest - arr[idx], dp) + process2(arr, idx + 1, rest + arr[idx], dp);
        }
        if (!dp.containsKey(idx)) {
            dp.put(idx, new HashMap<>());
        }
        dp.get(idx).put(rest, ans);
        return ans;
    }

    public static int ways2(int[] arr, int s) {
        return process2(arr, 0, s, new HashMap<>());
    }

    //

    private static int subset1(int[] nums, int s) {
        if (s < 0) {
            return 0;
        }
        int n = nums.length;
        int[][] dp = new int[n + 1][s + 1];
        // 1个空集
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= s; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j - nums[i - 1] >= 0) {
                    dp[i][j] += dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        return dp[n][s];
    }

    private static int subset2(int[] nums, int s) {
        if (s < 0) {
            return 0;
        }
        int[] dp = new int[s + 1];
        dp[0] = 1;
        for (int n : nums) {
            for (int i = s; i >= n; i--) {
                dp[i] += dp[i - n];
            }
        }
        return dp[s];
    }

    // 优化1：可认为都非负
    // 优化2：累加sum, target <= sum
    // 优化3：累加sum与target奇偶性相同
    // 优化4：target = P - N, P是正集合, N是负集合, 推出target + sum = 2P。转化成了非负背包求P的问题
    // 优化5：dp空间压缩
    public static int ways3(int[] arr, int target) {
        int sum = 0;
        for (int n : arr) {
            sum += n;
        }
        return sum < target || ((target & 1) ^ (sum & 1)) != 0 ? 0 : subset2(arr, (target + sum) >> 1);
    }
}
