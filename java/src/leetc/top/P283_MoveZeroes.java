package leetc.top;

public class P283_MoveZeroes {
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void moveZeroes(int[] nums) {
        int to = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                swap(nums, to++, i);
            }
        }
    }
}
