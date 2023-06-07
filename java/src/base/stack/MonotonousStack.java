package base.stack;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MonotonousStack {
    public static int[][] nearLessNoRepeat(int[] arr) {
        int[][] res = new int[arr.length][2];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                int j = stack.pop();
                int leftLess = stack.isEmpty() ? -1 : stack.peek();
                res[j][0] = leftLess;
                res[j][1] = i;
            }
            stack.push(i);
        }
        while (!stack.isEmpty()) {
            int j = stack.pop();
            int leftLess = stack.isEmpty() ? -1 : stack.peek();
            res[j][0] = leftLess;
            res[j][1] = -1;
        }
        return res;
    }

    public static int[][] nearLess(int[] arr) {
        int[][] res = new int[arr.length][2];
        Stack<List<Integer>> stack = new Stack<>();
        for (int i = 0; i < arr.length; i++) {
            while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
                List<Integer> pops = stack.pop();
                int leftLess = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
                for (Integer pop : pops) {
                    res[pop][0] = leftLess;
                    res[pop][1] = i;
                }
            }
            if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
                stack.peek().add(Integer.valueOf(i));
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(i);
                stack.push(list);
            }
        }
        while (!stack.isEmpty()) {
            List<Integer> pops = stack.pop();
            int leftLess = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
            for (Integer pop : pops) {
                res[pop][0] = leftLess;
                res[pop][1] = -1;
            }
        }
        return res;
    }

    //

    private static int[] randomArrNoRepeat(int maxLen) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        for (int i = 0; i < arr.length; i++) {
            int swapIdx = (int) (arr.length * Math.random());
            int tmp = arr[swapIdx];
            arr[swapIdx] = arr[i];
            arr[i] = tmp;
        }
        return arr;
    }

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] arr = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return arr;
    }

    public static int[][] nearLessSure(int[] arr) {
        int[][] res = new int[arr.length][2];
        for (int i = 0; i < arr.length; i++) {
            int leftLess = -1;
            int rightLess = -1;
            int cur = i - 1;
            while (cur >= 0) {
                if (arr[cur] < arr[i]) {
                    leftLess = cur;
                    break;
                }
                cur--;
            }
            cur = i + 1;
            while (cur < arr.length) {
                if (arr[cur] < arr[i]) {
                    rightLess = cur;
                    break;
                }
                cur++;
            }
            res[i][0] = leftLess;
            res[i][1] = rightLess;
        }
        return res;
    }

    private static boolean isEqual(int[][] res1, int[][] res2) {
        if (res1.length != res2.length) {
            return false;
        }
        for (int i = 0; i < res1.length; i++) {
            if (res1[i][0] != res2[i][0] || res1[i][1] != res2[i][1]) {
                return false;
            }
        }
        return true;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 10;
        int maxVal = 20;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr1 = randomArrNoRepeat(maxLen);
            int[] arr2 = randomArr(maxLen, maxVal);
            if (!isEqual(nearLessNoRepeat(arr1), nearLessSure(arr1))) {
                System.out.println("Wrong");
                print(arr1);
                break;
            }
            if (!isEqual(nearLess(arr2), nearLessSure(arr2))) {
                System.out.println("Wrong2");
                print(arr2);
                break;
            }
        }
        System.out.println("test end");
    }
}
