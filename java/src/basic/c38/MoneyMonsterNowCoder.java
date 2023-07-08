package basic.c38;

import java.util.Scanner;

// https://www.nowcoder.com/questionTerminal/93bcd2190da34099b98dfc9a9fb77984
public class MoneyMonsterNowCoder {
    public static void main(String[] args) {
        Scanner sc = new Scanner((System.in));
        int n1 = sc.nextInt();
        int n2 = sc.nextInt();
        int m = sc.nextInt();
        int[] arr1 = new int[n1];
        int[] arr2 = new int[n2];
        for (int i = 0; i < n1; i++) {
            arr1[i] = sc.nextInt();
        }
        for (int i = 0; i < n2; i++) {
            arr2[i] = sc.nextInt();
        }
        System.out.println(ways(arr1, arr2, m));
        sc.close();
    }

    public static final long mod = 1000000007;

    public static long[][] getDp1(int[] arr, int money) {
        long[][] dp = new long[arr.length][money + 1];
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        for (int j = 1; arr[0] * j <= money; j++) {
            dp[0][arr[0] * j] = 1;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                dp[i][j] = (dp[i - 1][j] + (j - arr[i] >= 0 ? dp[i][j - arr[i]] : 0)) % mod;
            }
        }
        return dp;
    }

    public static long[][] getDp2(int[] arr, int money) {
        long[][] dp = new long[arr.length][money + 1];
        for (int i = 0; i < arr.length; i++) {
            dp[i][0] = 1;
        }
        if (arr[0] <= money) {
            dp[0][arr[0]] = 1;
        }
        for (int i = 1; i < arr.length; i++) {
            for (int j = 1; j <= money; j++) {
                dp[i][j] = (dp[i - 1][j] + (j - arr[i] >= 0 ? dp[i - 1][j - arr[i]] : 0)) % mod;
            }
        }
        return dp;
    }

    public static int ways(int[] arr1, int[] arr2, int money) {
        long[][] dp1 = getDp1(arr1, money);
        long[][] dp2 = getDp2(arr2, money);
        long res = 0;
        for (int i = 0; i <= money; i++) {
            res = (res + dp1[dp1.length - 1][i] * dp2[dp2.length - 1][money - i]) % mod;
        }
        return (int) res;
    }
}
