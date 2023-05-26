package basic.sort;

import java.util.Arrays;

public class InsertSort {
    private static void swap(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

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


    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
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
