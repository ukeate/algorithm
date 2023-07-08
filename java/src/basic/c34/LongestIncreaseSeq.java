package basic.c34;

// 最长递增子序列
public class LongestIncreaseSeq {
    private static int[] getDp1(int[] arr) {
        int[] dp = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            dp[i] = 1;
            for (int j = 1; j < i; j++) {
                if (arr[i] > arr[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        return dp;
    }

    private static int[] lisArr(int[] arr, int[] dp) {
        int len = 0;
        int idx = 0;
        for (int i = 0; i < dp.length; i++) {
            if (dp[i] > len) {
                len = dp[i];
                idx = i;
            }
        }
        int[] lis = new int[len];
        lis[--len] = arr[idx];
        for (int i = idx; i >= 0; i--) {
            if (arr[i] < arr[idx] && dp[i] == dp[idx] - 1) {
                lis[--len] = arr[i];
                idx = i;
            }
        }
        return lis;
    }

    public static int[] lis1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        int[] dp = getDp1(arr);
        return lisArr(arr, dp);
    }

    //

    private static int[] getDp2(int[] arr) {
        int[] dp = new int[arr.length];
        // 长度到i+1的子序列，最小的结尾数字
        int[] ends = new int[arr.length];
        ends[0] = arr[0];
        dp[0] = 1;
        int endR = 0;
        int l = 0, r = 0, m = 0;
        for (int i = 1; i < arr.length; i++) {
            l = 0;
            r = endR;
            while (l <= r) {
                m = (l + r) / 2;
                if (arr[i] > ends[m]) {
                    l = m + 1;
                } else {
                    r = m - 1;
                }
            }
            endR = Math.max(endR, l);
            ends[l] = arr[i];
            dp[i] = l + 1;
        }
        return dp;
    }

    public static int[] lis2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return null;
        }
        int[] dp = getDp2(arr);
        return lisArr(arr, dp);
    }

    //

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] arr = {2, 1, 5, 3, 6, 4, 8, 9, 7};
        print(arr);
        print(lis1(arr));
        print(lis2(arr));
    }
}
