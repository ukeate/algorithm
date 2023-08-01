package leetc.top;

public class P34_FindFirstAndLastPositionOfElementInSortedArray {
    private static int first(int[] arr, int num) {
        int l = 0, r = arr.length - 1, mid = 0;
        int ans = -1;
        while (l <= r) {
            mid = l + ((r - l) >> 1);
            if (arr[mid] < num) {
                l = mid + 1;
            } else if (arr[mid] > num) {
                r = mid - 1;
            } else {
                ans = mid;
                r = mid - 1;
            }
        }
        return ans;
    }

    private static int last(int[] arr, int num) {
        int l = 0, r = arr.length - 1, mid = 0;
        int ans = -1;
        while (l <= r) {
            mid = l + ((r - l) >> 1);
            if (arr[mid] < num) {
                l = mid + 1;
            } else if (arr[mid] > num) {
                r = mid - 1;
            } else {
                ans = mid;
                l = mid + 1;
            }
        }
        return ans;
    }

    public static int[] searchRange(int[] nums, int target) {
        int[] ans = {-1, -1};
        if (nums == null || nums.length == 0) {
            return ans;
        }
        ans[0] = first(nums, target);
        ans[1] = last(nums, target);
        return ans;
    }
}
