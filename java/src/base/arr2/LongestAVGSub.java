package base.arr2;

import java.util.TreeMap;

public class LongestAVGSub {
    public static int ways1(int[] arr, int v) {
        int ans = 0;
        for (int l = 0; l < arr.length; l++) {
            for (int r = l; r < arr.length; r++) {
                int sum = 0;
                int k = r - l + 1;
                for (int i = l; i <= r; i++) {
                    sum += arr[i];
                }
                double avg = (double) sum / (double) k;
                if (avg <= v) {
                    ans = Math.max(ans, k);
                }
            }
        }
        return ans;
    }

    //

    public static int ways2(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= v;
        }
        TreeMap<Integer, Integer> map = new TreeMap<>();
        map.put(0, -1);
        int sum = 0;
        int len = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            Integer ceiling = map.ceilingKey(sum);
            if (ceiling != null) {
                len = Math.max(len, i - map.get(ceiling));
            } else {
                map.put(sum, i);
            }
        }
        return len;
    }

    //

    private static int maxLength(int[] arr, int k) {
        int n = arr.length;
        int[] sums = new int[n];
        int[] ends = new int[n];
        sums[n - 1] = arr[n - 1];
        ends[n - 1] = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            if (sums[i + 1] < 0) {
                sums[i] = arr[i] + sums[i + 1];
                ends[i] = ends[i + 1];
            } else {
                sums[i] = arr[i];
                ends[i] = i;
            }
        }
        int end = 0;
        int sum = 0;
        int res = 0;
        for (int i = 0; i < n; i++) {
            while (end < n && sum + sums[end] <= k) {
                sum += sums[end];
                end = ends[end] + 1;
            }
            res = Math.max(res, end - i);
            if (end > i) {
                sum -= arr[i];
            } else {
                end = i + 1;
            }
        }
        return res;
    }

    public static int ways3(int[] arr, int v) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] -= v;
        }
        return maxLength(arr, 0);
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int len = (int) ((maxLen + 1) * Math.random()) + 1;
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) ((maxVal + 1) * Math.random());
        }
        return ans;
    }

    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 20;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int val = (int) ((maxVal + 1) * Math.random());
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int ans1 = ways1(arr1, val);
            int ans2 = ways2(arr2, val);
            int ans3 = ways3(arr3, val);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
                print(arr);
            }
        }
        System.out.println("test end");
    }
}