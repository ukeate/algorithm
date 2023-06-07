package base.binary;

import java.util.Arrays;

public class Find {

    // arr有序
    public static boolean binaryFind(int[] arr, int num) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        int l = 0;
        int r = arr.length - 1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (arr[mid] == num) {
                return true;
            } else if (arr[mid] < num) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return false;
    }

    public static boolean binaryFindSure(int[] arr, int num) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == num) {
                return true;
            }
        }
        return false;
    }

    public static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
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

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            Arrays.sort(arr);
            int val = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
            boolean ans1 = binaryFind(arr, val);
            boolean ans2 = binaryFindSure(arr, val);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(val + "|" + ans1 + "|" + ans2);
                print(arr);
                break;
            }
        }
        System.out.println("test end");
    }
}
