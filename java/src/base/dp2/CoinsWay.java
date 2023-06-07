package base.dp2;

// 可重复，无限张，求方法数
public class CoinsWay {
    private static int process(int[] arr, int idx, int rest) {
        if (rest < 0) {
            return 0;
        }
        if (idx == arr.length) {
            return rest == 0 ? 1 : 0;
        }
        return process(arr, idx + 1, rest) + process(arr, idx + 1, rest - arr[idx]);
    }

    public static int ways(int[] arr, int aim) {
        return process(arr, 0, aim);
    }

    //

    public static int dp(int[] arr, int aim) {
        if (aim == 0) {
            return 1;
        }
        int n = arr.length;
        int[][] dp = new int[n + 1][aim + 1];
        dp[n][0] = 1;
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= aim; rest++) {
                dp[idx][rest] = dp[idx + 1][rest] + (rest - arr[idx] < 0 ? 0 : dp[idx + 1][rest - arr[idx]]);
            }
        }
        return dp[0][aim];
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] res = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < res.length; i++) {
            res[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return res;
    }

    private static void print(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 20;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int aim = (int) ((maxVal + 1) * Math.random());
            int ans1 = ways(arr, aim);
            int ans2 = dp(arr, aim);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(aim + "|" + ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}
