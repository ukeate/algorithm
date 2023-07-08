package basic.c32;

import java.util.Arrays;
import java.util.Comparator;

// 数组值两个一对(包含自己), 找排序后第k小对
public class KthMinPair {
    public static class Pair {
        public int x;
        public int y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Comp implements Comparator<Pair> {
        @Override
        public int compare(Pair o1, Pair o2) {
            return o1.x != o2.x ? o1.x - o2.x : o1.y - o2.y;
        }
    }

    public static int[] kth1(int[] arr, int k) {
        int n = arr.length;
        if (k > n * n) {
            return null;
        }
        Pair[] pairs = new Pair[n * n];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pairs[idx++] = new Pair(arr[i], arr[j]);
            }
        }
        Arrays.sort(pairs, new Comp());
        return new int[] {pairs[k - 1].x, pairs[k - 1].y};
    }

    //

    // O(NlogN)
    public static int[] kth2(int[] arr, int k) {
        int n = arr.length;
        if (k > n * n) {
            return null;
        }
        Arrays.sort(arr);
        // -1因为下标从0开始
        int firstNum = arr[(k - 1) / n];
        int lessFirstNumSize = 0;
        int firstNumSize = 0;
        for (int i = 0; i < n && arr[i] <= firstNum; i++) {
            if (arr[i] < firstNum) {
                lessFirstNumSize++;
            } else {
                firstNumSize++;
            }
        }
        int rest = k - (lessFirstNumSize * n);
        return new int[] {firstNum, arr[(rest - 1) / firstNumSize]};
    }

    //

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
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
        return new int[] {less + 1, more - 1};
    }

    private static int kMin(int[] arr, int idx) {
        int l = 0;
        int r = arr.length - 1;
        int pivot = 0;
        int[] range = null;
        while (l < r) {
            pivot = arr[l + (int) ((r - l + 1) * Math.random())];
            range = partition(arr, l, r, pivot);
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

    // O(N)
    public static int[] kth3(int[] arr, int k) {
        int n = arr.length;
        if (k > n * n) {
            return null;
        }
        int firstNum = kMin(arr, (k - 1) / n);
        int lessFirstNumSize = 0;
        int firstNumSize = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] < firstNum) {
                lessFirstNumSize++;
            }
            if (arr[i] == firstNum) {
                firstNumSize++;
            }
        }
        int rest = k - (lessFirstNumSize * n);
        return new int[] {firstNum, kMin(arr, (rest - 1) / firstNumSize)};
    }

    //

    private static int[] randomArr(int len, int max) {
        int[] arr = new int[(int) (len * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((max + 1) * Math.random() - (int) ((max + 1) * Math.random()));
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

    public static void main(String[] args) {
        int times = 1000000;
        int len = 30;
        int max = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, max);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int n = arr.length * arr.length;
            int k = (int) (n * Math.random()) + 1;
            int[] ans1 = kth1(arr1, k);
            int[] ans2 = kth2(arr2, k);
            int[] ans3 = kth3(arr3, k);
            if (ans1[0] != ans2[0] || ans2[0] != ans3[0] || ans1[1] != ans2[1] || ans2[1] != ans3[1]) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
