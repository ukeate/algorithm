package base.sort;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MinKth {
    public static class Comp implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }

    public static int minKth1(int[] arr, int k) {
        PriorityQueue<Integer> heap = new PriorityQueue<>(new Comp());
        for (int i = 0; i < k; i++) {
            heap.add(arr[i]);
        }
        for (int i = k; i < arr.length; i++) {
            if (arr[i] < heap.peek()) {
                heap.poll();
                heap.add(arr[i]);
            }
        }
        return heap.peek();
    }

    //

    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < ans.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    private static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = tmp;
    }

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
        return new int[] { less + 1, more - 1};
    }

    private static int process2(int[] arr, int l, int r, int idx) {
        if (l == r) {
            return arr[l];
        }
        int pivot = arr[l + (int) ((r - l + 1) * Math.random())];
        int[] range = partition(arr, l, r, pivot);
        if (idx >= range[0] && idx <= range[1]) {
            return arr[idx];
        } else if (idx < range[0]) {
            return process2(arr, l, range[0] - 1, idx);
        } else {
            return process2(arr, range[1] + 1, r, idx);
        }
    }

    public static int minKth2(int[] arr, int k) {
        int[] arr2 = copy(arr);
        return process2(arr2, 0, arr.length - 1, k - 1);
    }

    //

    private static void insertionSort(int[] arr, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            for (int j = i - 1; j >= l && arr[j] > arr[j + 1]; j--) {
                swap(arr, j, j + 1);
            }
        }
    }
    private static int median(int[] arr, int l, int r) {
        insertionSort(arr, l, r);
        return arr[(l + r) / 2];
    }

    private static int medianOfMedians(int[] arr, int l, int r) {
        int size = r - l + 1;
        int offset = size % 5 == 0 ? 0 : 1;
        int[] medians = new int[size / 5 + offset];
        for (int team = 0; team < medians.length; team++) {
            int teamFirst = l + team * 5;
            medians[team] = median(arr, teamFirst, Math.min(r, teamFirst + 4));
        }
        return bfprt(medians, 0, medians.length - 1, medians.length / 2);
    }

    private static int bfprt(int[] arr, int l, int r, int idx) {
        if (l == r) {
            return arr[l];
        }
        int pivot = medianOfMedians(arr, l, r);
        int[] range = partition(arr, l, r, pivot);
        if (idx >= range[0] && idx <= range[1]) {
            return arr[idx];
        } else if (idx < range[0]) {
            return bfprt(arr, l, range[0] - 1, idx);
        } else {
            return bfprt(arr, range[1] + 1, r, idx);
        }
    }

    public static int minKth3(int[] arr, int k) {
        int[] arr2 = copy(arr);
        return bfprt(arr, 0, arr.length - 1, k - 1);
    }

    public static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int k = (int) (arr.length * Math.random()) + 1;
            int ans1 = minKth1(arr, k);
            int ans2 = minKth2(arr, k);
            int ans3 = minKth3(arr, k);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
