package basic;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class Sort {

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[j];
        arr[j] = arr[i];
        arr[i] = tmp;
    }

    public static void selectSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                minIdx = arr[j] < arr[minIdx] ? j : minIdx;
            }
            swap(arr, i, minIdx);
        }
    }

    //

    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;

        for (int end = n - 1; end >= 0; end--) {
            for (int i = 0; i < end; i++) {
                if (arr[i] > arr[i + 1]) {
                    swap(arr, i, i + 1);
                }
            }
        }
    }

    //

    public static void insertSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;

        for (int end = 1; end < n; end++) {
            for (int i = end; i > 0 && arr[i - 1] > arr[i]; i--) {
                swap(arr, i - 1, i);
            }
        }
    }

    //
    private static void merge(int[] arr, int l, int m, int r) {
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l;
        int p2 = m + 1;
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
    }

    private static void mProcess(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        int m = l + ((r + l) >> 1);
        mProcess(arr, l, m);
        mProcess(arr, m + 1, r);
        merge(arr, l, m, r);
    }

    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        mProcess(arr, 0, arr.length - 1);
    }

    //


    public static void mergeSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int n = arr.length;
        int size = 1;
        while (size < n) {
            int l = 0;
            while (l < n) {
                if (n - l <= size) {
                    break;
                }
                int m = l + size - 1;
                int r = m + Math.min(size, n - 1 - m);
                merge(arr, l, m, r);
                l = r + 1;
            }
            if (size > (n >> 1)) {
                break;
            }
            size <<= 1;
        }
    }

    //

    public static void split(int[] arr) {
        int l = -1;
        int i = 0;
        int n = arr.length;
        while (i < n) {
            if (arr[i] <= arr[n - 1]) {
                swap(arr, ++l, i++);
            } else {
                i++;
            }
        }
    }

    public static void split2(int[] arr) {
        int n = arr.length;
        int l = -1;
        int r = n - 1;
        int i = 0;
        while (i < r) {
            if (arr[i] < arr[n - 1]) {
                swap(arr, ++l, i++);
            } else if (arr[i] > arr[n - 1]) {
                swap(arr, --r, i);
            } else {
                i++;
            }
        }
        swap(arr, r, n - 1);
    }

    //

    public static int[] partition(int[] arr, int l, int r) {
        int ll = l - 1;
        int rr = r;
        int i = l;
        while (i < rr) {
            if (arr[i] < arr[r]) {
                swap(arr, ++ll, i++);
            } else if (arr[i] > arr[r]) {
                swap(arr, --ll, i);
            } else {
                i++;
            }
        }
        swap(arr, rr, r);
        return new int[]{ll + 1, rr};
    }

    private static void qProcess(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        int[] eq = partition(arr, l, r);
        qProcess(arr, l, eq[0] - 1);
        qProcess(arr, eq[1] + 1, r);
    }

    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        qProcess(arr, 0, arr.length - 1);
    }

    private static class qJob {
        public int l;
        public int r;

        public qJob(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    public static void quickSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        Deque<qJob> stack = new ArrayDeque<>();
        stack.push(new qJob(0, arr.length - 1));
        while (!stack.isEmpty()) {
            qJob cur = stack.pop();
            int[] eq = partition(arr, cur.l, cur.r);
            if (eq[0] > cur.l) {
                stack.push(new qJob(cur.l, eq[0] - 1));
            }
            if (eq[1] < cur.r) {
                stack.push(new qJob(eq[1] + 1, cur.r));
            }
        }
    }

    private static int[] generateRandom(int size, int val) {
        int[] arr = new int[(int) ((size + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((val + 1) * Math.random()) - (int) (val * Math.random());
        }
        return arr;
    }

    private static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
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

    public static void main(String[] args) {
        int times = 500000;
        int maxSize = 100;
        int maxVal = 100;
        boolean succeed = true;
        for (int i = 0; i < times; i++) {
            int[] arr1 = generateRandom(maxSize, maxVal);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            insertSort(arr1);
            Arrays.sort(arr2);
            if (!isEqual(arr1, arr2)) {
                System.out.println("Wrong Answer.");
                print(arr1);
                print(arr2);
                succeed = false;
                break;
            }
        }
        if (succeed) {
            System.out.println("Correct.");
        }
    }
}
