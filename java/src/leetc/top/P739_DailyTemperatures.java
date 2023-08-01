package leetc.top;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class P739_DailyTemperatures {
    public static int[] dailyTemperatures(int[] arr) {
        if (arr == null || arr.length == 0) {
            return new int[0];
        }
        int n = arr.length;
        int[] ans = new int[n];
        Stack<List<Integer>> stack = new Stack<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr[stack.peek().get(0)] < arr[i]) {
                List<Integer> cs = stack.pop();
                for (Integer c : cs) {
                    ans[c] = i - c;
                }
            }
            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
                stack.peek().add(i);
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.push(list);
            }
        }
        return ans;
    }
}
