package base.binary;

import java.util.Arrays;

public class FindNear {

    public static int leftGe(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        int l = 0, r = arr.length - 1;
        int ans = -1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] >= val) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }

    private static int leftGeSure(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= val) {
                return i;
            }
        }
        return -1;
    }

    public static int rightLe(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        int l = 0, r = arr.length - 1;
        int ans = -1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] <= val) {
                ans = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return ans;
    }

    private static int rightLeSure(int[] arr, int val) {
        if (arr == null || arr.length < 1) {
            return -1;
        }
        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] <= val) {
                return i;
            }
        }
        return -1;
    }

    private static int[] randomArr(int maxLen, int maxVal) {
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
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            Arrays.sort(arr);
            int val = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
            int ans1 = rightLe(arr, val);
            int ans2 = rightLeSure(arr, val);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                print(arr);
                System.out.println(val);
                break;
            }
        }
        System.out.println("test end");
    }
}
