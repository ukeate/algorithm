package base.dp2;

import java.util.HashMap;
import java.util.Map;

// 可重复，限张数, 求方法数
public class CoinsWayLimit {
    private static class Info {
        public int[] coins;
        public int[] nums;
        public Info(int[] c, int[] n) {
            coins = c;
            nums = n;
        }
    }

    private static Info getInfo(int[] arr) {
        HashMap<Integer, Integer> counts = new HashMap<>();
        for (int val : arr) {
            if (!counts.containsKey(val)) {
                counts.put(val, 1);
            } else {
                counts.put(val, counts.get(val) + 1);
            }
        }
        int n = counts.size();
        int[] coins = new int[n];
        int[] nums = new int[n];
        int idx = 0;
        for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
            coins[idx] = entry.getKey();
            nums[idx++] = entry.getValue();
        }
        return new Info(coins, nums);
    }

    private static int process(int[] coins, int[] nums, int idx, int rest) {
        if (idx == coins.length) {
            return rest == 0 ? 1 : 0;
        }
        int ways = 0;
        for (int num = 0; num * coins[idx] <= rest && num <= nums[idx]; num++) {
            ways += process(coins, nums, idx + 1, rest - (num * coins[idx]));
        }
        return ways;
    }

    public static int ways(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        Info info = getInfo(arr);
        return process(info.coins, info.nums, 0, aim);
    }

    //

    public static int dp1(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }

        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] nums = info.nums;
        int n = coins.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ways = 0;
                for (int num = 0; num * coins[idx] <= rest && num <= nums[idx]; num++) {
                    ways += dp[idx + 1][rest - num * coins[idx]];
                }
                dp[idx][rest] = ways;
            }
        }
        return dp[0][aim];
    }

    //

    public static int dp2(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        Info info = getInfo(arr);
        int[] coins = info.coins;
        int[] nums = info.nums;
        int n = coins.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ways = dp[idx + 1][rest];
                if (rest - coins[idx] >= 0) {
                    ways += dp[idx][rest - coins[idx]];
                }
                if (rest - coins[idx] * (nums[idx] + 1) >= 0) {
                    ways -= dp[idx + 1][rest - coins[idx] * (nums[idx] + 1)];
                }
                dp[idx][rest] = ways;
            }
        }
        return dp[0][aim];
    }

    //


    public static int[] randomArr(int maxLen, int maxVal) {
        int n = (int) ((maxLen + 1) * Math.random());
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
                arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return arr;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 10;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int aim = (int) ((maxVal + 1) * Math.random());
            int ans1 = ways(arr, aim);
            int ans2 = dp1(arr, aim);
            int ans3 = dp2(arr, aim);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim + "|" + ans1 + "|" + ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
