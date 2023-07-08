package basic.c41;

import java.util.TreeSet;

// 子数组累加和小于等于K的最大值
public class MaxSubSumUnderK {
    public static int max(int[] arr, int k) {
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
