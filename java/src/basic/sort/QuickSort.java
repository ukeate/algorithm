package basic.sort;

import java.util.ArrayDeque;
import java.util.Deque;

public class QuickSort {

    private static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

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


    public static void main(String[] args) {
    }
}
