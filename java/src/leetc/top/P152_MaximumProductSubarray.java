package leetc.top;

public class P152_MaximumProductSubarray {
    public static int maxProduct(int[] nums) {
        int ans = nums[0], min = nums[0], max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int curMin = Math.min(nums[i], Math.min(min * nums[i], max * nums[i]));
            int curMax = Math.max(nums[i], Math.max(min * nums[i], max * nums[i]));
            min = curMin;
            max = curMax;
            ans = Math.max(ans, max);
        }
        return ans;
    }
}
