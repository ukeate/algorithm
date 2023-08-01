package leetc.top;

import java.util.Arrays;

public class P1755_ClosestSubsequenceSum {
    private static int[] l = new int[1 << 20];
    private static int[] r = new int[1 << 20];

    private static int process(int[] nums, int idx, int end, int sum, int fill, int[] arr) {
        if (idx == end) {
            arr[fill++] = sum;
        } else {
            fill = process(nums, idx + 1, end, sum, fill, arr);
            // 使用填好的fill位置
            fill = process(nums, idx + 1, end, sum + nums[idx], fill, arr);
        }
        return fill;
    }

    public static int minAbsDifference(int[] nums, int goal) {
        if (nums == null || nums.length == 0) {
            return goal;
        }
        int le = process(nums, 0, nums.length >> 1, 0, 0, l);
        int re = process(nums, nums.length >> 1, nums.length, 0, 0, r);
        Arrays.sort(l, 0, le--);
        Arrays.sort(r, 0, re--);
        int ans = Math.abs(goal);
        for (int i = 0; i <= le; i++) {
            int rest = goal - l[i];
            while (re > 0 && Math.abs(rest - r[re - 1]) <= Math.abs(rest - r[re])) {
                re--;
            }
            ans = Math.min(ans, Math.abs(rest - r[re]));
        }
        return ans;
    }
}
