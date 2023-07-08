package basic.c37;

import java.util.Arrays;

// arr中组合得到aim及之下所有数，求arr中最少缺几个数
public class MinPatches {
    public static int min(int[] arr, int aim) {
        int patches = 0;
        long range = 0;
        Arrays.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            // 已有数字左
            while (arr[i] - 1 > range) {
                range += range + 1;
                patches++;
                if (range >= aim) {
                    return patches;
                }
            }
            // 已有数字
            range += arr[i];
            if (range >= aim) {
                return patches;
            }
        }
        // 数字右
        while (aim >= range + 1) {
            range += range + 1;
            patches++;
        }
        return patches;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 31, 33};
        int n = 2147483647;
        System.out.println(min(arr, n));
    }
}
