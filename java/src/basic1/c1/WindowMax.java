package basic1.c1;

import java.util.LinkedList;

public class WindowMax {
    public static int[] maxWindow(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        int[] ans = new int[arr.length - w + 1];
        LinkedList<Integer> deque = new LinkedList<>();
        for (int r = 0; r < arr.length; r++) {
            while (!deque.isEmpty() && arr[deque.peekLast()] <= arr[r]) {
                deque.pollLast();
            }
            deque.addLast(r);
            if (deque.peekFirst() <= r - w) {
                deque.pollFirst();
            }
            if (r >= w - 1) {
                ans[r - w + 1] = arr[deque.peekFirst()];
            }
        }
        return ans;
    }

    public static int[] maxWindowSure(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        int[] ans = new int[arr.length - w + 1];
        for (int l = 0; l < ans.length; l++) {
            int max = Integer.MIN_VALUE;
            for (int ll = l; ll < l + w; ll++) {
                max = Math.max(arr[ll], max);
            }
            ans[l] = max;
        }
        return ans;
    }

    private static int[] randomArr(int ms, int mv) {
        int[] arr = new int[(int) (Math.random() * (ms + 1))];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (mv + 1));
        }
        return arr;
    }

    private static boolean equal(int[] arr1, int[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if ((arr1 == null ^ arr2 == null)) {
            return false;
        }
        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    private static void print(int[] arr) {
        if (arr == null || arr.length == 0) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLen = 100;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(maxLen, maxVal);
            int w = (int) (Math.random() * (arr.length + 1));
            int[] ans1 = maxWindow(arr, w);
            int[] ans2 = maxWindowSure(arr, w);
            if (!equal(ans1, ans2)) {
                System.out.println("Wrong");
                System.out.println(w);
                print(arr);
                print(ans1);
                print(ans2);
                break;
            }
        }
        System.out.println("test finish");
    }
}
