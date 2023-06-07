package base.sort;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class QuickSort {

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static int[] partition(int[] arr, int l, int r) {
        int less = l - 1;
        int more = r;
        int i = l;
        while (i < more) {
            if (arr[i] < arr[r]) {
                swap(arr, ++less, i++);
            } else if (arr[i] > arr[r]) {
                swap(arr, --more, i);
            } else {
                i++;
            }
        }
        swap(arr, more, r);
        return new int[]{less + 1, more};
    }

    private static void process(int[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        swap(arr, l + (int) ((r - l + 1) * Math.random()), r);
        int[] eq = partition(arr, l, r);
        process(arr, l, eq[0] - 1);
        process(arr, eq[1] + 1, r);
    }

    public static void quickSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    private static class Job {
        public int l;
        public int r;

        public Job(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    public static void quickSort2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        Deque<Job> stack = new ArrayDeque<>();
        stack.push(new Job(0, arr.length - 1));
        while (!stack.isEmpty()) {
            Job op = stack.pop();
            if (op.l < op.r) {
                int[] eq = partition(arr, op.l, op.r);
                stack.push(new Job(op.l, eq[0] - 1));
                stack.push(new Job(eq[1] + 1, op.r));
            }
        }
    }


    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxVal + 1) * Math.random())];
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
        if (arr1 == null & arr2 == null) {
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
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(maxLen, maxVal);
            int[] arr2 = copy(arr1);
            quickSort(arr1);
            quickSort2(arr2);
            if (!isEqual(arr1, arr2)) {
                System.out.println("Wrong");
                print(arr1);
                print(arr2);
                break;
            }
        }
        System.out.println("test end");
    }
}
