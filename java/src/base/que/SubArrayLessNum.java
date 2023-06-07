package base.que;

import java.util.LinkedList;

public class SubArrayLessNum {
    public static int num(int[] arr, int sum) {
        if (arr == null || arr.length == 0 || sum < 0) {
            return 0;
        }
        int n = arr.length;
        int count = 0;
        LinkedList<Integer> maxWindow = new LinkedList<>();
        LinkedList<Integer> minWindow = new LinkedList<>();
        int r = 0;
        for (int l = 0; l < n; l++) {
            while (r < n) {
                while (!maxWindow.isEmpty() && arr[maxWindow.peekLast()] <= arr[r]) {
                    maxWindow.pollLast();
                }
                maxWindow.addLast(r);
                while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[r]) {
                    minWindow.pollLast();
                }
                minWindow.addLast(r);
                if (arr[maxWindow.peekFirst()] - arr[minWindow.peekFirst()] > sum) {
                    break;
                } else {
                    r++;
                }
            }
            count += r - l;
            if (maxWindow.peekFirst() == l) {
                maxWindow.pollFirst();
            }
            if (minWindow.peekFirst() == l) {
                minWindow.pollFirst();
            }
        }
        return count;
    }

    public static int numSure(int[] arr, int sum) {
        if (arr == null || arr.length == 0 || sum < 0) {
            return 0;
        }
        int n = arr.length;
        int count = 0;
        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                int max = arr[l];
                int min = arr[l];
                for (int i = l + 1; i <= r; i++) {
                    max = Math.max(max, arr[i]);
                    min = Math.min(min, arr[i]);
                }
                if (max - min <= sum) {
                    count++;
                }
            }
        }
        return count;
    }

    //

    public static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 100;
        int maxVal = 200;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int sum = (int) ((maxVal + 1) * Math.random());
            int ans1 = num(arr, sum);
            int ans2 = numSure(arr, sum);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
