package basic.c38;

// 普通币不限数量，纪念币只能取一个，拼出money，求方法数
public class MoneyWays {
    private static int[][] getDp1(int[] arr, int money) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        int[][] dp = new int[arr.length][money + 1];
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        for (int j = 1; arr[0] * j <= money; j++) {
            dp[0][arr[0] * j] = 1;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                dp[i][j] = dp[i - 1][j];
                dp[i][j] += j - arr[i] >= 0 ? dp[i][j - arr[i]] : 0;
            }
        }
        return dp;
    }

    private static int[][] getDp2(int[] arr, int money) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        int[][] dp = new int[arr.length][money + 1];
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        if (arr[0] <= money) {
            dp[0][arr[0]] = 1;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                dp[i][j] = dp[i - 1][j];
                dp[i][j] += j - arr[i] >= 0 ? dp[i - 1][j - arr[i]] : 0;
            }
        }
        return dp;
    }

    public static int ways(int[] arr1, int[] arr2, int money) {
        if (money < 0) {
            return 0;
        }
        if ((arr1 == null || arr1.length == 0) && (arr2 == null || arr2.length == 0)) {
            return money == 0 ? 1 : 0;
        }
        int[][] dp1 = getDp1(arr1, money);
        int[][] dp2 = getDp2(arr2, money);
        if (dp1 == null) {
            return dp2[dp2.length - 1][money];
        }
        if (dp2 == null) {
            return dp1[dp1.length - 1][money];
        }
        int res = 0;
        for (int i = 0; i <= money; i++) {
            res += dp1[dp1.length - 1][i] * dp2[dp2.length - 1][money - i];
        }
        return res;
    }
}
