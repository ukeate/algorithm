package base.dp2;

public class SplitSumClosed {
    // arr[i...]自由选择，累加和最接近rest的值
    private static int process(int[] arr, int i, int rest) {
        if (i == arr.length) {
            return 0;
        }
        int p1 = process(arr, i + 1, rest);
        int p2 = 0;
        if (arr[i] <= rest) {
            p2 = arr[i] + process(arr, i + 1, rest - arr[i]);
        }
        return Math.max(p1, p2);
    }

    // 分两个集合，sum尽量接近，返回小的sum
    public static int closedSum(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return process(arr, 0, sum / 2);
    }

    //

    public static int dp(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        sum /= 2;
        int n = arr.length;
        int[][] dp = new int[n + 1][sum + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int rest = 0; rest <= sum; rest++) {
                int p1 = dp[i + 1][rest];
                int p2 = 0;
                if (arr[i] <= rest) {
                    p2 = arr[i] + dp[i + 1][rest - arr[i]];
                }
                dp[i][rest] = Math.max(p1, p2);
            }
        }
        return dp[0][sum];
    }

    //

    public static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 20;
        int maxVal = 50;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int ans1 = closedSum(arr);
            int ans2 = dp(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
