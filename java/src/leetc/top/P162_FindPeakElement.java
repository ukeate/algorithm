package leetc.top;

public class P162_FindPeakElement {
    public static int findPeakElement(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return 0;
        }
        if (nums[0] > nums[1]) {
            return 0;
        }
        if (nums[n - 1] > nums[n - 2]) {
            return n - 1;
        }
        int l = 1, r = n - 2, m = 0;
        while (l < r) {
            m = (l + r) / 2;
            if (nums[m - 1] < nums[m] && nums[m] > nums[m + 1]) {
                return m;
            } else if (nums[m - 1] > nums[m]) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return l;
    }
}
