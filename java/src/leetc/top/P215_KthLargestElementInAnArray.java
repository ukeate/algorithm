package leetc.top;

public class P215_KthLargestElementInAnArray {
    private static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = tmp;
    }

    private static int[] partition(int[] arr, int l, int r, int pivot) {
        int less = l - 1, more = r + 1, cur = l;
        while (cur < more) {
            if (arr[cur] < pivot) {
                swap(arr, ++less, cur++);
            } else if (arr[cur] > pivot) {
                swap(arr, cur, --more);
            } else {
                cur++;
            }
        }
        return new int[] {less + 1, more - 1};
    }

    private static int process(int[] arr, int l, int r, int idx) {
        if (l == r) {
            return arr[l];
        }
        int pivot = arr[l + (int) (Math.random() * (r - l + 1))];
        int[] range = partition(arr, l, r, pivot);
        if (idx >= range[0] && idx <= range[1]) {
            return arr[idx];
        } else if (idx < range[0]) {
            return process(arr, l, range[0] - 1, idx);
        } else {
            return process(arr, range[1] + 1, r, idx);
        }
    }

    private static int minKth(int[] arr, int k) {
        return process(arr, 0, arr.length - 1, k - 1);
    }

    public static int findKthLargest(int[] nums, int k) {
        return minKth(nums, nums.length + 1 - k);
    }
}
