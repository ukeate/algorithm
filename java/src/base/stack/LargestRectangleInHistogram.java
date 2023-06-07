package base.stack;

import java.util.Stack;

// https://leetcode.com/problems/largest-rectangle-in-histogram
public class LargestRectangleInHistogram {
    public static int largest1(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        int max = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < heights.length; i++) {
            while (!stack.isEmpty() && heights[i] <= heights[stack.peek()]) {
                int j = stack.pop();
                int k = stack.isEmpty() ? -1 : stack.peek();
                max = Math.max(max, (i - k - 1) * heights[j]);
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int k = stack.isEmpty() ? - 1 :stack.peek();
            max = Math.max(max, (heights.length - k - 1) * heights[j]);
        }
        return max;
    }

    public static int largest2(int[] heights) {
        if (heights == null || heights.length == 0) {
            return 0;
        }
        int n = heights.length;
        int[] stack = new int[n];
        int si = -1;
        int max = 0;
        for (int i = 0; i < heights.length; i++) {
            while (si != -1 && heights[i] <= heights[stack[si]]) {
                int j = stack[si--];
                int k = si == -1 ? -1 :stack[si];
                max = Math.max(max, (i - k - 1) * heights[j]);
            }
            stack[++si] = i;
        }
        while (si != -1) {
            int j = stack[si--];
            int k = si == -1 ? -1 : stack[si];
            max = Math.max(max, (heights.length - k - 1) * heights[j]);
        }
        return max;
    }
}

