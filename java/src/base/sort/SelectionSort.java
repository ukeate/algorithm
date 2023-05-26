package base.sort;

import java.util.Arrays;

public class SelectionSort {

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void selectionSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        for (int i = 0; i < arr.length - 1; i++) {
            int idx = i;
            for (int j = i + 1; j < arr.length; j++) {
                idx = arr[j] < arr[idx] ? j : idx;
            }
            swap(arr, i, idx);
        }
    }

    public static void sortSure(int[] arr) {
        Arrays.sort(arr);
    }

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) (Math.random() * (maxLen + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (maxVal + 1)) - (int) (Math.random() * (maxVal + 1));
        }
        return arr;
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
        int times = 1000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(2, 2);
            int[] arr2 = copy(arr1);
            selectionSort(arr1);
            sortSure(arr2);
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
