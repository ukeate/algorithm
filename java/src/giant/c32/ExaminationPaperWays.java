package giant.c32;

import java.util.Arrays;

// 给定一个数组arr，arr[i] = j，表示第i号试题的难度为j。给定一个非负数M
// 想出一张卷子，对于任何相邻的两道题目，前一题的难度不能超过后一题的难度+M
// 返回所有可能的卷子种数
public class ExaminationPaperWays {
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    private static int process(int[] arr, int idx, int m) {
        if (idx == arr.length) {
            for (int i = 1; i < idx; i++) {
                if (arr[i - 1] > arr[i] + m) {
                    return 0;
                }
            }
            return 1;
        }
        int ans = 0;
        for (int i = idx; i < arr.length; i++) {
            swap(arr, idx, i);
            ans += process(arr, idx + 1, m);
            swap(arr, idx, i);
        }
        return ans;
    }

    public static int ways1(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return process(arr, 0, m);
    }

    //

    // 二分找>=t的数量
    private static int num(int[] arr, int r, int t) {
        int i = 0, j = r, m = 0, a = r + 1;
        while (i <= j) {
            m = (i + j) / 2;
            if (arr[m] >= t) {
                a = m;
                j = m - 1;
            } else {
                i = m + 1;
            }
        }
        return r - a + 1;
    }

    // O(N * logN)
    public static int ways2(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        Arrays.sort(arr);
        int all = 1;
        for (int i = 1; i < arr.length; i++) {
            all = all * (num(arr, i - 1, arr[i] - m) + 1);
        }
        return all;
    }

    //

    private static class IndexTree {
        private int[] tree;
        private int n;

        public IndexTree(int size) {
            n = size;
            tree = new int[n + 1];
        }

        public void add(int idx, int d) {
            while (idx <= n) {
                tree[idx] += d;
                idx += idx & -idx;
            }
        }

        public int sum(int idx) {
            int ret = 0;
            while (idx > 0) {
                ret += tree[idx];
                idx -= idx & -idx;
            }
            return ret;
        }
    }

    // O(N * logV)
    public static int ways3(int[] arr, int m) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int num : arr) {
            max = Math.max(max, num);
            min = Math.min(min, num);
        }
        IndexTree tree = new IndexTree(max - min + 2);
        Arrays.sort(arr);
        int a = 0, b = 0, all = 1;
        tree.add(arr[0] - min + 1, 1);
        for (int i = 1; i < arr.length; i++) {
            a = arr[i] - min + 1;
            b = i - (a - m - 1 >= 1 ? tree.sum(a - m - 1) : 0);
            all = all * (b + 1);
            tree.add(a, 1);
        }
        return all;
    }

    //

    private static int[] randomArr(int len, int val) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) (Math.random() * (val + 1));
        }
        return arr;
    }

    public static void main(String[] args) {
        int n = 10;
        int val = 20;
        int times = 1000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * (n + 1));
            int[] arr = randomArr(len, val);
            int m = (int) (Math.random() * (val + 1));
            int ans1 = ways1(arr, m);
            int ans2 = ways2(arr, m);
            int ans3 = ways3(arr, m);
            if (ans1 != ans2 || ans1 != ans3) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");

        int[] arr = randomArr(100000000, 10000000);
        long start, end;
        start = System.currentTimeMillis();
        int ans2 = ways2(arr, 10);
        end = System.currentTimeMillis();
        System.out.println("ways2 " + ans2 + " times: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        int ans3 = ways3(arr, 10);
        end = System.currentTimeMillis();
        System.out.println("ways3 " + ans3 + " times: " + (end - start) + " ms");
    }
}
