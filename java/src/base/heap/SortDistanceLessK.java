package base.heap;

import java.util.Arrays;
import java.util.PriorityQueue;

public class SortDistanceLessK {
    public static void sortDistanceLessK(int[] arr, int k) {
        if (k == 0) {
            return;
        }
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        int r = 0;
        for (; r <= Math.min(arr.length - 1, k - 1); r++) {
            heap.add(arr[r]);
        }
        int l = 0;
        for (; r < arr.length; l++,r++) {
            heap.add(arr[r]);
            arr[l] = heap.poll();
        }
        while (!heap.isEmpty()) {
            arr[l++] = heap.poll();
        }
    }

    private static int[] randomArrDistanceLessK(int maxLen, int maxVal, int k) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxLen + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        Arrays.sort(arr);
        boolean[] hasSwap = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            int j = Math.min(i + (int) ((k + 1) * Math.random()), arr.length - 1);
            if (!hasSwap[i] && !hasSwap[j]) {
                hasSwap[i] = true;
                hasSwap[j] = true;
                int tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
            }
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
        if (arr1 == null ^ arr2 == null) {
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
            int k = (int) ((maxLen + 1) * Math.random());
            int[] arr = randomArrDistanceLessK(maxLen, maxVal, k);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            sortDistanceLessK(arr1, k);
            Arrays.sort(arr2);
            if (!isEqual(arr1, arr2)) {
                System.out.println("Wrong");
                print(arr);
                print(arr1);
                print(arr2);
                break;
            }
        }
        System.out.println("test end");
    }

}
