package basic.c44;

import java.util.Arrays;

// 两个有序数组中，找第k小
public class KthMinTwoArr {
    // 上中位数
    private static int upMid(int[] a1, int s1, int e1, int[] a2, int s2, int e2) {
        int mid1 = 0, mid2 = 0;
        while (s1 < e1) {
            mid1 = (s1 + e1) / 2;
            mid2 = (s2 + e2) / 2;
            if (a1[mid1] == a2[mid2]) {
                return a1[mid1];
            }
            if (((e1 - s1 + 1) & 1) == 1) {
                if (a1[mid1] > a2[mid2]) {
                    if (a2[mid2] >= a1[mid1 - 1]) {
                        return a2[mid2];
                    }
                    e1 = mid1 - 1;
                    s2 = mid2 + 1;
                } else {
                    if (a1[mid1] >= a2[mid2 - 1]) {
                        return a1[mid1];
                    }
                    e2 = mid2 - 1;
                    s1 = mid1 + 1;
                }
            } else {
                if (a1[mid1] > a2[mid2]) {
                    e1 = mid1;
                    s2 = mid2 + 1;
                } else {
                    e2 = mid2;
                    s1 = mid1 + 1;
                }
            }
        }
        return Math.min(a1[s1], a2[s2]);
    }

    public static int kth(int[] arr1, int[] arr2, int k) {
        if (arr1 == null || arr2 == null) {
            return -1;
        }
        if (k < 1 || k > arr1.length + arr2.length) {
            return -1;
        }
        int[] longs = arr1.length >= arr2.length ? arr1 : arr2;
        int[] shorts = arr1.length < arr2.length ? arr1 : arr2;
        int l = longs.length;
        int s = shorts.length;
        if (k <= s) {
            return upMid(shorts, 0, k - 1, longs, 0, k - 1);
        }
        if (k > l) {
            if (shorts[k - l - 1] >= longs[l - 1]) {
                return shorts[k - l - 1];
            }
            if (longs[k - s - 1] >= shorts[s - 1]) {
                return longs[k - s - 1];
            }
            return upMid(shorts, k - l, s - 1, longs, k - s, l - 1);
        }
        // s < k <= l
        if (longs[k - s - 1] >= shorts[s - 1]) {
            return longs[k - s - 1];
        }
        return upMid(shorts, 0, s - 1, longs, k - s, k - 1);
    }

    //

    private static int[] randomSortedArr(int len, int maxVal) {
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            res[i] = (int) ((maxVal + 1) * Math.random());
        }
        Arrays.sort(res);
        return res;
    }

    private static int[] sortAll(int[] arr1, int[] arr2) {
        if (arr1 == null || arr2 == null) {
            return null;
        }
        int[] all = new int[arr1.length + arr2.length];
        int idx = 0;
        for (int i = 0; i < arr1.length; i++) {
            all[idx++] = arr1[i];
        }
        for (int i = 0; i < arr2.length; i++) {
            all[idx++] = arr2[i];
        }
        Arrays.sort(all);
        return all;
    }

    private static void print(int[] arr) {
        for (int i = 0 ; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int len1 = 10;
        int len2 = 23;
        int maxVal1 = 20;
        int maxVal2 = 100;
        int[] arr1 = randomSortedArr(len1, maxVal1);
        int[] arr2 = randomSortedArr(len2, maxVal2);
        print(arr1);
        print(arr2);
        int[] all = sortAll(arr1, arr2);
        print(all);
        int k = 17;
        System.out.println(kth(arr1, arr2, k));
        System.out.println(all[k - 1]);
    }
}
