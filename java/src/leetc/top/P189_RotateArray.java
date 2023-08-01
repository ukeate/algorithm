package leetc.top;

public class P189_RotateArray {
    private static void reverse(int[] nums, int l, int r) {
        while (l < r) {
            int tmp = nums[l];
            nums[l++] = nums[r];
            nums[r--] = tmp;
        }
    }

    public void rotate1(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        reverse(nums, 0, n - k - 1);
        reverse(nums, n - k, n - 1);
        reverse(nums, 0, n - 1);
    }

    //

    private static void exchange(int[] nums, int start, int end, int size) {
        for (int i = end - size + 1; size > 0; start++, i++, size--) {
            int tmp = nums[start];
            nums[start] = nums[i];
            nums[i] = tmp;
        }
    }

    public static void rotate2(int[] nums, int k) {
        int n = nums.length;
        k = k % n;
        if (k == 0) {
            return;
        }
        int l = 0, r = n - 1;
        int lpart = n - k, rpart = k;
        int same = Math.min(lpart, rpart);
        int diff = lpart - rpart;
        exchange(nums, l, r, same);
        while (diff != 0) {
            if (diff > 0) {
                l += same;
                lpart = diff;
            } else {
                r -= same;
                rpart = -diff;
            }
            same = Math.min(lpart, rpart);
            diff = lpart - rpart;
            exchange(nums, l, r, same);
        }
    }
}
