package base.arr2;

import java.util.HashMap;

// 数有正负，最长和为k的子串
public class LongestSumSub {
    public static int max(int[] arr, int k) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int len = 0;
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            if (map.containsKey(sum - k)) {
                len = Math.max(i - map.get(sum - k), len);
            }
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }
        return len;
    }

    //

    private static boolean valid(int[] arr, int l, int r, int k) {
        int sum = 0;
        for (int i = l; i <= r; i++) {
            sum += arr[i];
        }
        return sum == k;
    }

    public static int sure(int[] arr, int k) {
        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j < arr.length; j++) {
                if (valid(arr, i, j, k)) {
                    max = Math.max(max, j - i + 1);
                }
            }
        }
        return max;
    }

    //

    public static int[] randomArr(int len, int maxVal) {
        int[] arr = new int[(int) (len * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 500000;
        int len = 50;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, maxVal);
            int k = (int) (((maxVal << 1) + 1) * Math.random()) - maxVal;
            int ans1 = max(arr, k);
            int ans2 = sure(arr, k);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
