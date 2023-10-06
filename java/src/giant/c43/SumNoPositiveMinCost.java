package giant.c43;

import java.util.Arrays;

// 来自微软面试
// 给定一个正数数组arr长度为n、正数x、正数y
// 你的目标是让arr整体的累加和<=0
// 你可以对数组中的数num执行以下三种操作中的一种，且每个数最多能执行一次操作 :
// 1）不变
// 2）可以选择让num变成0，承担x的代价
// 3）可以选择让num变成-num，承担y的代价
// 返回你达到目标的最小代价
// 数据规模 : 面试时面试官没有说数据规模
public class SumNoPositiveMinCost {

    private static int process1(int[] arr, int x, int y, int i, int sum) {
        if (sum <= 0) {
            return 0;
        }
        if (i == arr.length) {
            return Integer.MAX_VALUE;
        }
        int p1 = process1(arr, x, y, i + 1, sum);
        int p2 = Integer.MAX_VALUE;
        int next2 = process1(arr, x, y, i + 1, sum - arr[i]);
        if (next2 != Integer.MAX_VALUE) {
            p2 = x + next2;
        }
        int p3 = Integer.MAX_VALUE;
        int next3 = process1(arr, x, y, i + 1, sum - (arr[i] << 1));
        if (next3 != Integer.MAX_VALUE) {
            p3 = y + next3;
        }
        return Math.min(p1, Math.min(p2, p3));
    }

    public static int min1(int[] arr, int x, int y) {
        int sum = 0;
        for (int num : arr) {
            sum += num;
        }
        return process1(arr, x, y, 0, sum);
    }

    //
    public static int mostLeft(int[] arr, int l, int v) {
        int r = arr.length - 1;
        int m = 0;
        int ans = arr.length;
        while (l <= r) {
            m = (l + r) / 2;
            if (arr[m] <= v) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }

    public static int min2(int[] arr, int x, int y) {
        Arrays.sort(arr);
        int n = arr.length;
        for (int l = 0, r = n - 1; l <= r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        if (x >= y) {
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            int cost = 0;
            for (int i = 0; i < n && sum > 0; i++) {
                sum -= arr[i] << 1;
                cost += y;
            }
            return cost;
        } else {
            // y区, x区, 抵y区
            for (int i = n - 2; i >= 0; i--) {
                arr[i] += arr[i + 1];
            }
            // y为0的情况
            int benefit = 0;
            int left = mostLeft(arr, 0, benefit);
            int cost = left * x;
            for (int i = 0; i < n - 1; i++) {
                benefit += arr[i] - arr[i + 1];
                left = mostLeft(arr, i + 1, benefit);
                cost = Math.min(cost, (i + 1) * y + (left - i - 1) * x);
            }
            return cost;
        }
    }

    //

    public static int min3(int[] arr, int x, int y) {
        Arrays.sort(arr);
        int n = arr.length;
        for (int l = 0, r = n - 1; l <= r; l++, r--) {
            int tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
        }
        if (x >= y) {
            int sum = 0;
            for (int num : arr) {
                sum += num;
            }
            int cost = 0;
            for (int i = 0; i < n && sum > 0; i++) {
                sum -= arr[i] << 1;
                cost += y;
            }
            return cost;
        } else {
            int benefit = 0;
            int cost = arr.length * x;
            int benefitCut = 0;
            // 不会全y
            for (int i = 0, left = n; i < left - 1; i++) {
                benefit += arr[i];
                while (left - 1 > i && benefitCut + arr[left - 1] <= benefit) {
                    benefitCut += arr[left - 1];
                    left--;
                }
                cost = Math.min(cost, (i + 1) * y + (left - i - 1) * x);
            }
            return cost;
        }
    }

    //

    private static int[] randomArr(int len, int v) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * v) + 1;
        }
        return arr;
    }

    private static int[] copy(int[] arr) {
        int[] ans = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 10000;
        int maxLen = 12;
        int maxVal = 20;
        int maxCost = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * maxLen);
            int[] arr = randomArr(len, maxVal);
            int[] arr1 = copy(arr);
            int[] arr2 = copy(arr);
            int[] arr3 = copy(arr);
            int x = (int) (Math.random() * maxCost);
            int y = (int) (Math.random() * maxCost);
            int ans1 = min1(arr1, x, y);
            int ans2 = min2(arr2, x, y);
            int ans3 = min3(arr3, x, y);
            if (ans1 != ans2 || ans2 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
