package base.dp2;

// 不重复，无限张, 求方法数
public class CoinsWayNoLimit {

    private static int process(int[] arr, int idx, int rest) {
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0;
        }
        int ways = 0;
        for (int num = 0; num * arr[idx] <= rest; num++) {
            ways += process(arr, idx + 1, rest - (num * arr[idx]));
        }
        return ways;
    }

    public static int ways(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        return process(arr, 0, aim);
    }

    //

    public static int dp1(int[] arr, int aim) {
        if (arr == null || arr.length == 0 || aim < 0) {
            return 0;
        }
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                int ways = 0;
                for (int num = 0; num * arr[idx] <= rest; num++) {
                    ways += dp[idx + 1][rest - num * arr[idx]];
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
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                dp[idx][rest] = dp[idx + 1][rest];
                if (rest - arr[idx] >= 0) {
                    dp[idx][rest] += dp[idx][rest - arr[idx]];
                }
            }
        }
        return dp[0][aim];
    }

    //

    public static int[] randomArr(int maxLen, int maxVal) {
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
