package leetc.top;

public class P324_WiggleSortII {
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static int[] partition(int[] arr, int l, int r, int pivot) {
        int less = l - 1, more = r + 1;
        int cur = l;
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

    private static int median(int[] arr, int l, int r, int idx) {
        int pivot = 0;
        int[] range = null;
        while (l < r) {
            pivot = arr[l + (int) (Math.random() * (r - l + 1))];
            range = partition(arr, l, r, pivot);
            if (idx >= range[0] && idx <= range[1]) {
                return arr[idx];
            } else if (idx < range[0]) {
                r = range[0] - 1;
            } else {
                l = range[1] + 1;
            }
        }
        return arr[l];
    }

    private static void reverse(int[] arr, int l, int r) {
        while (l < r) {
            swap(arr, l++, r--);
        }
    }

    private static void rotate(int[] arr, int l, int m, int r) {
        reverse(arr, l, m);
        reverse(arr, m + 1, r);
        reverse(arr, l, r);
    }

    private static void cycles(int[] nums, int base, int bloom, int k) {
        for (int i = 0, trigger = 1; i < k; i++, trigger *= 3) {
            int cur = (2 * trigger) % bloom;
            int curVal = nums[cur + base];
            nums[cur + base] = nums[trigger + base];
            while (cur != trigger) {
                int next = (2 * cur) % bloom;
                int nextVal = nums[next + base];
                nums[next + base] = curVal;
                cur = next;
                curVal = nextVal;
            }
        }
    }

    // 3^k-1个数的出发点为 1,3,..3^(k-1)
    private static void shuffle(int[] nums, int l, int r) {
        while (r - l + 1 > 0) {
            int lenAndOne = r - l + 2;
            int bloom = 3;
            int k = 1;
            while (bloom <= lenAndOne / 3) {
                bloom *= 3;
                k++;
            }
            int m = (bloom - 1) / 2;
            int mid = (l + r) / 2;
            rotate(nums, l + m, mid, mid + m);
            cycles(nums, l - 1, bloom, k);
            l = l + bloom - 1;
        }
    }

    public static void wiggleSort(int[] nums) {
        if (nums == null || nums.length < 2) {
            return;
        }
        int n = nums.length;
        median(nums, 0, nums.length - 1, n / 2);
        if ((n & 1) == 0) {
            shuffle(nums, 0, nums.length - 1);
            reverse(nums, 0, nums.length - 1);
        } else {
            shuffle(nums, 1, nums.length - 1);
        }
    }
}
