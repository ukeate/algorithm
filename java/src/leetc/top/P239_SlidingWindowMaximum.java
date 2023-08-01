package leetc.top;

import java.util.LinkedList;

public class P239_SlidingWindowMaximum {
    public static int[] maxSlidingWindow(int[] arr, int w) {
        if (arr == null || w < 1 || arr.length < w) {
            return null;
        }
        LinkedList<Integer> qmax = new LinkedList<>();
        int[] ans = new int[arr.length - w + 1];
        int idx = 0;
        for (int r = 0; r < arr.length; r++) {
            while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[r]) {
                qmax.pollLast();
            }
            qmax.addLast(r);
            if (qmax.peekFirst() == r - w) {
                qmax.pollFirst();
            }
            if (r >= w - 1) {
                ans[idx++] = arr[qmax.peekFirst()];
            }
        }
        return ans;
    }
}
