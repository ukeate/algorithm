package base.dp4_quad;

import java.util.Arrays;

// 小镇设num个邮局, 使总路程最小
public class PostOffice {
    public static int min1(int[] arr, int num) {
        if (arr == null || num < 1 || arr.length < num) {
            return 0;
        }
        int n = arr.length;
        int[][] w = new int[n + 1][n + 1];
        for (int l = 0; l < n; l++) {
            for (int r = l + 1; r < n; r++) {
                w[l][r] = w[l][r - 1] + arr[r] - arr[(l + r) >> 1];
            }
        }
        int[][] dp = new int[n][num + 1];
        for (int i = 0; i < n; i++) {
            dp[i][1] = w[0][i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 2; j <= Math.min(i, num); j++) {
                int ans = Integer.MAX_VALUE;
                for (int k = 0; k <= i; k++) {
                    ans = Math.min(ans, dp[k][j - 1] + w[k + 1][i]);
                }
                dp[i][j] = ans;
            }
        }
        return dp[n - 1][num];
    }

    //

    public static int min2(int[] arr, int num) {
        if (arr == null || num < 1 || arr.length < num) {
            return 0;
        }
        int n = arr.length;
        int[][] w = new int[n + 1][n + 1];
        for (int l = 0; l < n; l++) {
            for (int r = l + 1; r < n; r++) {
                w[l][r] = w[l][r - 1] + arr[r] - arr[(l + r) >> 1];
            }
        }
        int[][] dp = new int[n][num + 1];
        int[][] best = new int[n][num + 1];
        for (int i = 0; i < n; i++) {
            dp[i][1] = w[0][i];
            best[i][1] = -1;
        }
        for (int j = 2; j <= num; j++) {
            for (int i = n - 1; i >= j; i--) {
                int down = best[i][j - 1];
                int up = i == n - 1 ? n - 1 : best[i + 1][j];
                int ans = Integer.MAX_VALUE;
                int bestChoose = -1;
                for (int leftEnd = down; leftEnd <= up; leftEnd++) {
                    int leftCost = leftEnd == -1 ? 0 : dp[leftEnd][j - 1];
                    int rightCost = leftEnd == i ? 0 : w[leftEnd + 1][i];
                    int cur = leftCost + rightCost;
                    if (cur <= ans) {
                        ans = cur;
                        bestChoose = leftEnd;
                    }
                }
                dp[i][j] = ans;
                best[i][j] = bestChoose;
            }
        }
        return dp[n - 1][num];
    }

    //

    private static int[] randomSortedArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i != len; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        Arrays.sort(arr);
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 30;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random()) + 1;
            int[] arr = randomSortedArr(len, maxVal);
            int num = (int) ((maxLen + 1) * Math.random()) + 1;
            int ans1 = min1(arr, num);
            int ans2 = min2(arr, num);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
