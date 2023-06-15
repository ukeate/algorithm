package base.dp4;

public class BestSplitRange {
    public static int[] split1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] ans = new int[n];
        ans[0] = 0;
        for (int range = 1; range < n; range++) {
            for (int s = 0; s < range; s++) {
                int sumL = 0;
                for (int l = 0; l <= s; l++) {
                    sumL += arr[l];
                }
                int sumR = 0;
                for (int r = s + 1; r <= range; r++) {
                    sumR += arr[r];

                }
                ans[range] = Math.max(ans[range], Math.min(sumL, sumR));
            }
        }
        return ans;
    }

    //

    private static int sum(int[] sum, int l, int r) {
        return sum[r + 1] - sum[l];
    }

    public static int[] split2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] ans = new int[n];
        ans[0] = 0;
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + arr[i];
        }
        for (int range = 1; range < n; range++) {
            for (int s = 0; s < range; s++) {
                int sumL = sum(sum, 0, s);
                int sumR = sum(sum, s + 1, range);
                ans[range] = Math.max(ans[range], Math.min(sumL, sumR));
            }
        }
        return ans;
    }

    //

    // best有单调性
    public static int[] split3(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] ans = new int[n];
        ans[0] = 0;
        int[] sums = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sums[i + 1] = sums[i] + arr[i];
        }
        int best = 0;
        for (int range = 1; range < n; range++) {
            while (best + 1 < range) {
                int before = Math.min(sum(sums, 0, best), sum(sums, best + 1, range));
                int after = Math.min(sum(sums, 0, best + 1), sum(sums, best + 2, range));
                if (before <= after) {
                    best++;
                } else {
                    break;
                }
            }
            ans[range] = Math.min(sum(sums, 0, best), sum(sums, best + 1, range));
        }
        return ans;
    }

    private static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1.length != arr2.length) {
            return false;
        }
        int n = arr1.length;
        for (int i = 0 ; i < n; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 20;
        int maxVal = 30;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random());
            int[] arr = randomArr(len, maxVal);
            int[] ans1 = split1(arr);
            int[] ans2 = split2(arr);
            int[] ans3 = split3(arr);
            if (!isEqual(ans1, ans2) || !isEqual(ans1, ans3)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
