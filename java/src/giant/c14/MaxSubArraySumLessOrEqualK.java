package giant.c14;

import java.util.TreeSet;

// 子数组累加和<=k的，求最大累加和
public class MaxSubArraySumLessOrEqualK {
    public static int max(int[] arr, int k) {
        // 前缀和
        TreeSet<Integer> set = new TreeSet<>();
        set.add(0);
        int max = Integer.MIN_VALUE;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (set.ceiling(sum - k) != null) {
                max = Math.max(max, sum - set.ceiling(sum - k));
            }
            set.add(sum);
        }
        return max;
    }
}
