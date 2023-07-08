package basic.c43;

import java.util.Arrays;

// 求如果数组排序，相邻数间最大差值
public class MaxGap {
    private static int bucket(long num, long len, long min, long max) {
        return (int) ((num - min) * len / (max - min));
    }

    public static int max(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int len = arr.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < len; i++) {
            min = Math.min(min, arr[i]);
            max = Math.max(max, arr[i]);
        }
        if (min == max) {
            return 0;
        }
        boolean[] hasNum = new boolean[len + 1];
        int[] maxs = new int[len + 1];
        int[] mins = new int[len + 1];
        int bid = 0;
        for (int i = 0; i < len; i++) {
            bid = bucket(arr[i], len, min, max);
            mins[bid] = hasNum[bid] ? Math.min(mins[bid], arr[i]) : arr[i];
            maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], arr[i]) : arr[i];
            hasNum[bid] = true;
        }
        int res = 0;
        int lastMax = maxs[0];
        for (int i = 1; i <= len; i++) {
            if (hasNum[i]) {
                res = Math.max(res, mins[i] - lastMax);
                lastMax = maxs[i];
            }
        }
        return res;
    }

    //

    public static int maxSure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        Arrays.sort(arr);
        int gap = Integer.MIN_VALUE;
        for (int i = 1; i < arr.length; i++) {
            gap = Math.max(arr[i] - arr[i - 1], gap);
        }
        return gap;
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random() - (int) ((maxVal + 1) * Math.random()));
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
        int times = 500000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArr(maxLen, maxVal);
            int[] arr2 = copy(arr1);
            if (max(arr1) != maxSure(arr2)) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
