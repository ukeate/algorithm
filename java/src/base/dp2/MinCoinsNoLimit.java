package base.dp2;

// 可重复，不限张, 求最少张数
public class MinCoinsNoLimit {
    private static int process(int[] arr, int idx, int rest) {
        if (idx == arr.length) {
            return rest == 0 ? 0 : Integer.MAX_VALUE;
        }
        int ans = Integer.MAX_VALUE;
        for (int num = 0; num * arr[idx] <= rest; num++) {
            int next = process(arr, idx + 1, rest - num * arr[idx]);
            if (next != Integer.MAX_VALUE) {
                ans = Math.min(ans, num + next);
            }
        }
        return ans;
    }

    public static int minNum(int[] arr, int aim) {
        return process(arr, 0, aim);
    }

    //

    public static int dp1(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;
        }
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ans = Integer.MAX_VALUE;
                for (int num = 0; num * arr[idx] <= rest; num++) {
                    int next = dp[idx + 1][rest - num * arr[idx]];
                    if (next != Integer.MAX_VALUE) {
                        ans = Math.min(ans, num + next);
                    }
                }
                dp[idx][rest] = ans;
            }
        }
        return dp[0][aim];
    }

    //

    public static int dp2(int[] arr, int aim) {
        if (aim == 0) {
            return 0;
        }
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 0;
        for (int j = 1; j <= aim; j++) {
            dp[n][j] = Integer.MAX_VALUE;
        }
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ans = dp[idx + 1][rest];
                if (rest - arr[idx] >= 0 && dp[idx][rest - arr[idx]] != Integer.MAX_VALUE) {
                    ans = Math.min(ans, dp[idx][rest - arr[idx]] + 1);
                }
                dp[idx][rest] = ans;
            }
        }
        return dp[0][aim];
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int n = (int) ((maxLen + 1) * Math.random());
        int[] arr = new int[n];
        boolean[] has = new boolean[maxVal + 2];
        for (int i = 0; i < n; i++) {
            do {
                arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
            } while (has[arr[i]]);
            has[arr[i]] = true;
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
        int times = 100000;
        int maxLen = 20;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int n = (int) ((maxLen + 1) * Math.random());
            int[] arr = randomArr(n, maxVal);
            int aim = (int) ((maxVal * 2) * Math.random());
            int ans1 = minNum(arr, aim);
            int ans2 = dp1(arr, aim);
            int ans3 = dp2(arr, aim);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim + "|" + ans1 + "|" + ans2 + "+" + ans3);
            }
        }
        System.out.println("test end");
    }
}
