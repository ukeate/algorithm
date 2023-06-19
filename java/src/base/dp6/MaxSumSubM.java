package base.dp6;

import java.util.LinkedList;

// 不超过m长度子数组最大和
public class MaxSumSubM {
    public static int max(int[] arr, int m) {
        if (arr == null || arr.length == 0 || m < 1) {
            return 0;
        }
        int n = arr.length;
        int[] sum = new int[n];
        sum[0] = arr[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }
        LinkedList<Integer> qmax = new LinkedList<>();
        int i = 0;
        int end = Math.min(n, m);
        for (; i < end; i++) {
            while (!qmax.isEmpty() && sum[qmax.peekLast()] <= sum[i]) {
                qmax.pollLast();
            }
            qmax.add(i);
        }
        int max = sum[qmax.peekFirst()];
        int l = 0;
        for (; i < n; l++, i++) {
            if (qmax.peekFirst() == l) {
                qmax.pollFirst();
            }
            while (!qmax.isEmpty() && sum[qmax.peekLast()] <= sum[i]) {
                qmax.pollLast();
            }
            qmax.add(i);
            max = Math.max(max, sum[qmax.peekFirst()] - sum[l]);
        }
        for (; l < n - 1; l++) {
            if (qmax.peekFirst() == l) {
                qmax.pollFirst();
            }
            max = Math.max(max, sum[qmax.peekFirst()] - sum[l]);
        }
        return max;
    }

    //

    public static int sure(int[] arr, int m) {
        if (arr == null || arr.length == 0 || m < 1) {
            return 0;
        }
        int n = arr.length;
        int max = Integer.MIN_VALUE;
        for (int l = 0; l < n; l++) {
            int sum = 0;
            for (int r = l; r < n; r++) {
                if (r - l + 1 > m) {
                    break;
                }
                sum += arr[r];
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    //

    private static int[] randomArr(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) ((max + 1) * Math.random()) - (int) ((max + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 50;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random());
            int m = (int) ((maxLen + 1) * Math.random());
            int[] arr = randomArr(len, maxVal);
            int ans1 = max(arr, m);
            int ans2 = sure(arr, m);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
