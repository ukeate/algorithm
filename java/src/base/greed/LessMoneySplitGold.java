package base.greed;

import java.util.PriorityQueue;

public class LessMoneySplitGold {
    private static int[] copyAndMergeTwo(int[] arr, int i, int j) {
        int[] ans = new int[arr.length - 1];
        int ansIdx = 0;
        for (int arrIdx = 0; arrIdx < arr.length; arrIdx++) {
            if (arrIdx != i && arrIdx != j) {
                ans[ansIdx++] = arr[arrIdx];
            }
        }
        ans[ansIdx] = arr[i] + arr[j];
        return ans;
    }

    private static int process1(int[] arr, int pre) {
        if (arr.length == 1) {
            return pre;
        }
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                ans = Math.min(ans, process1(copyAndMergeTwo(arr, i, j), pre + arr[i] + arr[j]));
            }
        }
        return ans;
    }

    public static int lessMoney1(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        return process1(arr, 0);
    }

    //

    public static int lessMoney2(int[] arr) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < arr.length; i++) {
            pq.add(arr[i]);
        }
        int sum = 0;
        int cur = 0;
        while (pq.size() > 1) {
            cur = pq.poll() + pq.poll();
            sum += cur;
            pq.add(cur);
        }
        return sum;
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 6;
        int maxVal = 1000;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            if (lessMoney1(arr) != lessMoney2(arr)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
