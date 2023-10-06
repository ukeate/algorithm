package giant.c43;

import java.util.Arrays;

// 来自360笔试
// 给定一个正数数组arr，长度为n，下标0~n-1
// arr中的0、n-1位置不需要达标，它们分别是最左、最右的位置
// 中间位置i需要达标，达标的条件是 : arr[i-1] > arr[i] 或者 arr[i+1] > arr[i]哪个都可以
// 你每一步可以进行如下操作：对任何位置的数让其-1
// 你的目的是让arr[1~n-2]都达标，这时arr称之为yeah！数组
// 返回至少要多少步可以让arr变成yeah！数组
// 数据规模 : 数组长度 <= 10000，数组中的值<=500
public class MinCostToYeahArray {
    public static final int INVALID = Integer.MAX_VALUE;

    private static int process0(int[] arr, int base, int idx) {
        if (idx == arr.length) {
            for (int i = 1; i < arr.length - 1; i++) {
                if (arr[i - 1] <= arr[i] && arr[i] >= arr[i + 1]) {
                    return INVALID;
                }
            }
            return 0;
        } else {
            int ans = INVALID;
            int tmp = arr[idx];
            for (int cost = 0; arr[idx] >= base; cost++, arr[idx]--) {
                int next = process0(arr, base, idx + 1);
                if (next != INVALID) {
                    ans = Math.min(ans, cost + next);
                }
            }
            arr[idx] = tmp;
            return ans;
        }
    }

    public static int min0(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length;
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int base = min - n;
        return process0(arr, base, 0);
    }

    //

    // pre之前值，preOk之前值已小于前前
    private static int process1(int[] arr, int idx, int pre, boolean preOk) {
        if (idx == arr.length - 1) {
            return preOk || pre < arr[idx] ? 0 : INVALID;
        }
        int ans = INVALID;
        if (preOk) {
            for (int cur = arr[idx]; cur >= 0; cur--) {
                int next = process1(arr, idx + 1, cur, cur < pre);
                if (next != INVALID) {
                    ans = Math.min(ans, arr[idx] - cur + next);
                }
            }
        } else {
            for (int cur = arr[idx]; cur > pre; cur--) {
                int next = process1(arr, idx + 1, cur, false);
                if (next != INVALID) {
                    ans = Math.min(ans, arr[idx] - cur + next);
                }
            }
        }
        return ans;
    }

    public static int min1(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] += arr.length - min;
        }
        return process1(arr, 1, arr[0], true);
    }

    //

    public static int min2(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            arr[i] += n - min;
        }
        int[][][] dp = new int[n][2][];
        for (int i = 1; i < n; i++) {
            dp[i][0] = new int[arr[i - 1] + 1];
            dp[i][1] = new int[arr[i - 1] + 1];
            Arrays.fill(dp[i][0], INVALID);
            Arrays.fill(dp[i][1], INVALID);
        }
        for (int pre = 0; pre <= arr[n - 2]; pre++) {
            dp[n - 1][0][pre] = pre < arr[n - 1] ? 0 : INVALID;
            dp[n - 1][1][pre] = 0;
        }
        for (int idx = n - 2; idx >= 1; idx--) {
            for (int pre = 0; pre <= arr[idx - 1]; pre++) {
                for (int cur = arr[idx]; cur > pre; cur--) {
                    int next = dp[idx + 1][0][cur];
                    if (next != INVALID) {
                        dp[idx][0][pre] = Math.min(dp[idx][0][pre], arr[idx] - cur + next);
                    }
                }
                for (int cur = arr[idx]; cur >= 0; cur--) {
                    int next = dp[idx + 1][cur < pre ? 1 : 0][cur];
                    if (next != INVALID) {
                        dp[idx][1][pre] = Math.min(dp[idx][1][pre], arr[idx] - cur + next);
                    }
                }
            }
        }
        return dp[1][1][arr[0]];
    }

    //

    private static int[][] best(int[][][] dp, int i, int v) {
        int[][] best = new int[2][v + 1];
        best[0][v] = dp[i][0][v];
        for (int p = v - 1; p >= 0; p--) {
            best[0][p] = dp[i][0][p] == INVALID ? INVALID : v - p + dp[i][0][p];
            best[0][p] = Math.min(best[0][p], best[0][p + 1]);
        }
        best[1][0] = dp[i][1][0] == INVALID ? INVALID : v + dp[i][1][0];
        for (int p = 1; p <= v; p++) {
            best[1][p] = dp[i][1][p] == INVALID ? INVALID : v - p + dp[i][1][p];
            best[1][p] = Math.min(best[1][p], best[1][p - 1]);
        }
        return best;
    }

    public static int min3(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int min = INVALID;
        for (int num : arr) {
            min = Math.min(min, num);
        }
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            arr[i] += n - min;
        }
        int[][][] dp = new int[n][2][];
        for (int i = 1; i < n; i++) {
            dp[i][0] = new int[arr[i - 1] + 1];
            dp[i][1] = new int[arr[i - 1] + 1];
        }
        for (int p = 0; p <= arr[n - 2]; p++) {
            dp[n - 1][0][p] = p < arr[n - 1] ? 0 : INVALID;
        }
        int[][] best = best(dp, n - 1, arr[n - 2]);
        for (int i = n - 2; i >= 1; i--) {
            for (int p = 0; p <= arr[i - 1]; p++) {
                if (arr[i] < p) {
                    dp[i][1][p] = best[1][arr[i]];
                } else {
                    dp[i][1][p] = Math.min(best[0][p], p > 0 ? best[1][p - 1] : INVALID);
                }
                dp[i][0][p] = arr[i] <= p ? INVALID : best[0][p + 1];
            }
            best = best(dp, i, arr[i - 1]);
        }
        return dp[1][1][arr[0]];
    }

    //

    public static int min4(int[] arr) {
        if (arr == null || arr.length < 3) {
            return 0;
        }
        int n = arr.length;
        int[] nums = new int[n + 2];
        nums[0] = Integer.MAX_VALUE;
        nums[n + 1] = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            nums[i + 1] = arr[i];
        }
        int[] leftCost = new int[n + 2];
        int pre = nums[0];
        int change = 0;
        for (int i = 1; i <= n; i++) {
            change = Math.min(pre - 1, nums[i]);
            leftCost[i] = nums[i] - change + leftCost[i - 1];
            pre = change;
        }
        int[] rightCost = new int[n + 2];
        pre = nums[n + 1];
        for (int i = n; i >= 1; i--) {
            change = Math.min(pre - 1, nums[i]);
            rightCost[i] = nums[i] - change + rightCost[i + 1];
            pre = change;
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            ans = Math.min(ans, leftCost[i] + rightCost[i + 1]);
        }
        return ans;
    }

    //

    private static int[] randomArr(int len, int v) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * v) + 1;
        }
        return arr;
    }

    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 100;
        int len = 7;
        int v = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int n = (int) (Math.random() * len) + 1;
            int[] arr = randomArr(n, v);
            int[] arr0 = copy(arr);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int[] arr4 = copy(arr);
            int ans0 = min0(arr0);
            int ans1 = min1(arr1);
            int ans2 = min2(arr2);
            int ans3 = min3(arr3);
            int ans4 = min4(arr4);
            if (ans0 != ans1 || ans0 != ans2 || ans0 != ans3 || ans0 != ans4) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("test end");
    }
}
