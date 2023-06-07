package base.stack;

import java.util.Stack;

// https://leetcode.com/problems/maximal-rectangle/
public class MaxRectangle {
    private static int bottomMax(int[] heights) {
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
            int k = stack.isEmpty() ?-1 : stack.peek();
            max = Math.max(max, (heights.length - k - 1) * heights[j]);
        }
        return max;
    }

    public static int max(char[][] map) {
        if (map == null || map.length == 0 || map[0].length == 0) {
            return 0;
        }
        int max = 0;
        int[] heights = new int[map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                heights[j] = map[i][j] == '0' ? 0 : heights[j] + 1;
            }
            max = Math.max(max, bottomMax(heights));
        }
        return max;
    }
}
