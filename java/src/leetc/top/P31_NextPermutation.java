package leetc.top;

public class P31_NextPermutation {
    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private static void reverse(int[] nums, int l, int r) {
        while (l < r) {
            swap(nums, l++, r--);
        }
    }

    public static void nextPermutation(int[] nums) {
        int n = nums.length;
        // 从右往左第一次降序的位置
        int firstLess = -1;
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                firstLess = i;
                break;
            }
        }
        if (firstLess < 0) {
            reverse(nums, 0, n - 1);
            return;
        }
        // 最靠右，比nums[firstLess]大的数
        int rightClosestMore = -1;
        for (int i = n - 1; i > firstLess; i--) {
            if (nums[i] > nums[firstLess]) {
                rightClosestMore = i;
                break;
            }
        }
        swap(nums, firstLess, rightClosestMore);
        reverse(nums, firstLess + 1, n - 1);
    }
}
