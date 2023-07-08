package basic.c52;

// 3个长度为k不相交子数组，最大和, 返回子数组下标
// https://leetcode.com/problems/maximum-sum-of-3-non-overlapping-subarrays/
public class MaxSumOf3NonOverlappingSub {
    public static int[] max(int[] arr, int k) {
        int n = arr.length;
        int[] range = new int[n];

        int sum = 0;
        for (int i = 0; i < k; i++) {
            sum += arr[i];
        }
        // 当前位置向后的sumK
        range[0] = sum;
        int[] left = new int[n];
        // 向左maxK的位置
        left[k - 1] = 0;
        int max = sum;
        for (int i = k; i < n; i++) {
            sum = sum - arr[i - k] + arr[i];
            range[i - k + 1] = sum;
            left[i] = left[i - 1];
            if (sum > max) {
                max = sum;
                left[i] = i - k + 1;
            }
        }

        sum = 0;
        for (int i = n - 1; i >= n - k; i--) {
            sum += arr[i];
        }
        max = sum;
        // 向右maxK的位置
        int[] right = new int[n];
        right[n - k] = n - k;
        for (int i = n - k - 1; i >= 0; i--) {
            sum = sum - arr[i + k] + arr[i];
            right[i] = right[i + 1];
            if (sum >= max) {
                max = sum;
                right[i] = i;
            }
        }

        int a = 0, b = 0, c = 0;
        max = 0;
        for (int i = k; i < n - 2 * k + 1; i++) {
            int part1 = range[left[i - 1]];
            int part2 = range[i];
            int part3 = range[right[i + k]];
            if (part1 + part2 + part3 > max) {
                max = part1 + part2 + part3;
                a = left[i - 1];
                b = i;
                c = right[i + k];
            }
        }
        return new int[] {a, b, c};
    }
}
