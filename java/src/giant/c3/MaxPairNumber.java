package giant.c3;

import java.util.Arrays;

// 给定一个数组arr，代表每个人的能力值。再给定一个非负数k。
// 如果两个人能力差值正好为k，那么可以凑在一起比赛，一局比赛只有两个人
// 返回最多可以同时有多少场比赛
public class MaxPairNumber {
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static int process1(int[] arr, int idx, int k) {
        int ans = 0;
        if (idx == arr.length) {
            for (int i = 1; i < arr.length; i += 2) {
                if (arr[i] - arr[i - 1] == k) {
                    ans++;
                }
            }
        } else {
            for (int r = idx; r < arr.length; r++) {
                swap(arr, idx, r);
                ans = Math.max(ans, process1(arr, idx + 1, k));
                swap(arr, idx, r);
            }
        }
        return ans;
    }

    public static int max1(int[] arr, int k) {
        if (k < 0) {
            return -1;
        }
        return process1(arr, 0, k);
    }

    //

    public static int max2(int[] arr, int k) {
        if (k < 0 || arr == null || arr.length < 2) {
            return 0;
        }
        Arrays.sort(arr);
        int ans = 0, n = arr.length;
        int l = 0, r = 0;
        boolean[] usedR = new boolean[n];
        while (l < n && r < n) {
            if (usedR[l]) {
                l++;
            } else if (l >= r) {
                r++;
            } else {
                int distance = arr[r] - arr[l];
                if (distance == k) {
                    ans++;
                    usedR[r++] = true;
                    l++;
                } else if (distance < k) {
                    r++;
                } else {
                    l++;
                }
            }
        }
        return ans;
    }

    //

    private static int[] randomArr(int len, int val) {
        int[] arr = new int[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * val);
        }
        return arr;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 1000;
        int maxLen = 10;
        int maxVal = 20;
        int maxK = 5;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int n = (int) (Math.random() * (maxLen + 1));
            int[] arr = randomArr(n, maxVal);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int k = (int) (Math.random() * (maxK + 1));
            int ans1 = max1(arr1, k);
            int ans2 = max2(arr2, k);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
