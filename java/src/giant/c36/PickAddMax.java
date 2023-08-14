package giant.c36;

import java.util.Arrays;

// 来自腾讯
// 给定一个数组arr，当拿走某个数a的时候，其他所有的数都+a
// 请返回最终所有数都拿走的最大分数
// 比如: [2,3,1]
// 当拿走3时，获得3分，数组变成[5,4]
// 当拿走5时，获得5分，数组变成[9]
// 当拿走9时，获得9分，数组变成[]
// 这是最大的拿取方式，返回总分17
public class PickAddMax {
    public static int pick(int[] arr) {
        Arrays.sort(arr);
        int ans = 0;
        for (int i = arr.length - 1; i >= 0; i--) {
            ans = (ans << 1) + arr[i];
        }
        return ans;
    }

    //

    private static int[] removeAddOthers(int[] arr, int i) {
        int[] rest = new int[arr.length - 1];
        int ri = 0;
        for (int j = 0; j < i; j++) {
            rest[ri++] = arr[j] + arr[i];
        }
        for (int j = i + 1; j < arr.length; j++) {
            rest[ri++] = arr[j] + arr[i];
        }
        return rest;
    }

    public static int sure(int[] arr) {
        if (arr.length == 1) {
            return arr[0];
        }
        int ans = 0;
        for (int i = 0; i < arr.length; i++) {
            int[] rest = removeAddOthers(arr, i);
            ans = Math.max(ans, arr[i] + sure(rest));
        }
        return ans;
    }

    //

    private static int[] randomArr(int len, int val) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * val) + 1;
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 10000;
        int n = 7, v = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * n) + 1;
            int[] arr = randomArr(len, v);
            int ans1 = pick(arr);
            int ans2 = sure(arr);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
