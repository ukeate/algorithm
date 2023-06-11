package base.sort;

// https://leetcode.com/problems/count-of-range-sum/
public class CountRangeSum {
    private static int merge(long[] arr, int l, int m, int r, int lower, int upper) {
        int ans = 0;
        int ll = l;
        int rr = l;
        for (int i = m + 1; i <= r; i++) {
            long min = arr[i] - upper;
            long max = arr[i] - lower;
            while (ll <= m && arr[ll] < min) {
                ll++;
            }
            while (rr <= m && arr[rr] <= max) {
                rr++;
            }
            ans += rr - ll;
        }
        long[] help = new long[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = m + 1;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
        return ans;
    }

    private static int process1(long[] arr, int l, int r, int lower, int upper) {
        if (l == r) {
            return arr[l] >= lower && arr[l] <= upper ? 1 : 0;
        }
        int m = l + ((r - l) >> 1);
        return process1(arr, l, m, lower, upper) + process1(arr, m + 1, r, lower, upper) +
                merge(arr, l, m, r, lower, upper);
    }

    public static int count1(int[] nums, int lower, int upper) {
        if (nums == null || nums.length < 1) {
            return 0;
        }
        long[] arr = new long[nums.length];
        arr[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            arr[i] = arr[i - 1] + nums[i];
        }
        return process1(arr, 0, arr.length - 1, lower, upper);
    }

    //

    private static int process2(long[] sums, int start, int end, int lower, int upper) {
        if (end - start <= 1)
            return 0;
        int mid = (start + end) / 2;
        int count = process2(sums, start, mid, lower, upper)
                + process2(sums, mid, end, lower, upper);
        int j = mid, k = mid, t = mid;
        long[] cache = new long[end - start];
        for (int i = start, r = 0; i < mid; i++, r++) {
            while (j < end && sums[j] - sums[i] < lower)
                j++;
            while (k < end && sums[k] - sums[i] <= upper)
                k++;
            while (t < end && sums[t] < sums[i])
                cache[r++] = sums[t++];
            cache[r] = sums[i];
            count += k - j;
        }
        System.arraycopy(cache, 0, sums, start, t - start);
        return count;
    }

    public static int count2(int[] nums, int lower, int upper) {
        int n = nums.length;
        long[] sums = new long[n + 1];
        for (int i = 0; i < n; i++)
            sums[i + 1] = sums[i] + nums[i];
        return process2(sums, 0, n + 1, lower, upper);
    }

    public static void main(String[] args) {
        count2(new int[]{-2, 5, -1}, -2, 2);
    }
}
