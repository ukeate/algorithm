package basic.c38;

import java.util.Arrays;
import java.util.HashSet;

// 排序后相邻数字都差1为可整合, 最大可整合子数组长度
public class LongestIntegratedLength {
    private static boolean can(int[] arr, int l, int r) {
        int[] arr2 = Arrays.copyOfRange(arr, l, r + 1);
        Arrays.sort(arr2);
        for (int i = 1; i < arr2.length; i++) {
            if (arr2[i - 1] != arr2[i] - 1) {
                return false;
            }
        }
        return true;
    }

    public static int longest1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (can(arr, i, j)) {
                    len = Math.max(len, j - i + 1);
                }
            }
        }
        return len;
    }

    //

    public static int longest2(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int len = 0;
        int max = 0;
        int min = 0;
        HashSet<Integer> set = new HashSet<>();
        for (int l = 0; l < arr.length; l++) {
            set.clear();
            max = Integer.MIN_VALUE;
            min = Integer.MAX_VALUE;
            for (int r = l; r < arr.length; r++) {
                if (set.contains(arr[r])) {
                    break;
                }
                set.add(arr[r]);
                max = Math.max(max, arr[r]);
                min = Math.min(min, arr[r]);
                if (max - min == r - l) {
                    len = Math.max(len, r - l + 1);
                }
            }
        }
        return len;
    }

    public static void main(String[] args) {
        int[] arr = {5, 5, 3, 2, 6, 4, 3};
        System.out.println(longest1(arr));
        System.out.println(longest2(arr));
    }
}
