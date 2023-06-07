package base.sort;

import java.util.Arrays;

// 返回前k大的有序数组
public class TopK {
    // O(N*logN)
    public static int[] topK1(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        k = Math.min(n, k);
        Arrays.sort(arr);
        int[] ans = new int[k];
        for (int i = n - 1, j = 0; j < k; i--, j++) {
            ans[j] = arr[i];
        }
        return ans;
    }

    //

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static void heapify(int[] arr, int idx, int heapSize) {
        int left = idx * 2 + 1;
        while (left < heapSize) {
            int big = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            if (arr[idx] > arr[big]) {
                break;
            }
            swap(arr, big, idx);
            idx = big;
            left = idx * 2 + 1;
        }
    }

    // O(N + K*logN)
    public static int[] topK2(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        k = Math.min(n, k);
        for (int i = n - 1; i >= 0; i--) {
            heapify(arr, i, n);
        }
        int heapSize = n;
        swap(arr, 0, --heapSize);
        int count = 1;
        while (heapSize > 0 && count < k) {
            heapify(arr, 0, heapSize);
            swap(arr, 0, --heapSize);
            count++;
        }
        int[] ans = new int[k];
        for (int i = n - 1, j = 0; j < k; i--, j++) {
            ans[j] = arr[i];
        }
        return ans;
    }

    //

    private static int[] partition(int[] arr, int l, int r, int pivot) {
        int less = l - 1;
        int more = r + 1;
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
        return new int[]{less + 1, more - 1};
    }

    private static int minKth(int[] arr, int idx) {
        int l = 0, r = arr.length - 1;
        while (l < r) {
            int pivot = arr[l + (int) ((r - l + 1) * Math.random())];
            int[] range = partition(arr, l, r, pivot);
            if (idx < range[0]) {
                r = range[0] - 1;
            } else if (idx > range[1]) {
                l = range[1] + 1;
            } else {
                return pivot;
            }
        }
        return arr[l];
    }

    // O(N + K*logK)
    public static int[] topK3(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        k = Math.min(n, k);
        int num = minKth(arr, n - k);
        int[] ans = new int[k];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] > num) {
                ans[idx++] = arr[i];
            }
        }
        for (; idx < k; idx++) {
            ans[idx] = num;
        }
        Arrays.sort(ans);
        for (int l = 0, r = k - 1; l < r; l++, r--) {
            swap(ans, l, r);
        }
        return ans;
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    private static int[] copy(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    private static boolean isEqual(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1 == null || arr2 == null) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    private static void print(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int k = (int) ((maxLen + 1) * Math.random()) + 1;
            int[] arr = randomArr(maxLen, maxVal);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int[] ans1 = topK1(arr1, k);
            int[] ans2 = topK2(arr2, k);
            int[] ans3 = topK3(arr3, k);
            if (!isEqual(ans1, ans2) || !isEqual(ans1, ans3)) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(k);
                print(ans1);
                print(ans2);
                print(ans3);
                break;
            }
        }
        System.out.println("test end");
    }
}
