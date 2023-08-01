package leetc.top;

public class P75_SortColors {
    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    public static void sortColors(int[] nums) {
        int less = -1, more = nums.length;
        int idx = 0;
        while (idx < more) {
            if (nums[idx] == 1) {
                idx++;
            } else if (nums[idx] == 0) {
                swap(nums, idx++, ++less);
            } else {
                swap(nums, idx, --more);
            }
        }
    }
}
