package basic.c47;

import java.util.HashSet;
import java.util.Stack;

// arr是环形山，返回有多少能相互看见的环形山
public class VisibleMountains {
    private static class Record {
        public int val;
        public int times;
        public Record(int v) {
            val = v;
            times = 1;
        }
    }

    private static int nextIdx(int i, int size) {
        return i < (size - 1) ? (i + 1) : 0;
    }

    private static int getInternalSum(int k) {
        return k == 1 ? 0 : (k * (k - 1) / 2);
    }

    public static int num(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int n = arr.length;
        int maxIdx = 0;
        for (int i = 0; i < n; i++) {
            maxIdx = arr[maxIdx] < arr[i] ? i : maxIdx;
        }
        Stack<Record> stack = new Stack<>();
        stack.push(new Record(arr[maxIdx]));
        int idx = nextIdx(maxIdx, n);
        int res = 0;
        while (idx != maxIdx) {
            while (stack.peek().val < arr[idx]) {
                int k = stack.pop().times;
                res += getInternalSum(k) + 2 * k;
            }
            if (stack.peek().val == arr[idx]) {
                stack.peek().times++;
            } else {
                stack.push(new Record(arr[idx]));
            }
            idx = nextIdx(idx, n);
        }
        while (stack.size() > 2) {
            int times = stack.pop().times;
            res += getInternalSum(times) + 2 * times;
        }
        if (stack.size() == 2) {
            int times = stack.pop().times;
            res += getInternalSum(times) + (stack.peek().times == 1 ? times : 2 * times);
        }
        res += getInternalSum(stack.pop().times);
        return res;
    }

    //

    private static int lastIdx(int i, int size) {
        return i > 0 ? (i - 1) : (size - 1);
    }

    private static boolean isVisible(int[] arr, int lowIdx, int highIdx) {
        // 不相等时小找大
        if (arr[lowIdx] > arr[highIdx]) {
            return false;
        }
        int size = arr.length;
        boolean walkNext = true;
        int mid = nextIdx(lowIdx, size);
        while (mid != highIdx) {
            if (arr[mid] > arr[lowIdx]) {
                walkNext = false;
                break;
            }
            mid = nextIdx(mid, size);
        }
        boolean walkLast = true;
        mid = lastIdx(lowIdx, size);
        while (mid != highIdx) {
            if (arr[mid] > arr[lowIdx]) {
                walkLast = false;
                break;
            }
            mid = lastIdx(mid, size);
        }
        return walkNext || walkLast;
    }

    private static int visibleNum(int[] arr, int idx, HashSet<String> equalCounted) {
        int res = 0;
        for (int i = 0; i < arr.length; i++) {
            if (i != idx) {
                if (arr[i] == arr[idx]) {
                    String key = Math.min(idx, i) + "_" + Math.max(idx, i);
                    // 相等时，确保之前没找过
                    if (equalCounted.add(key) && isVisible(arr, idx, i)) {
                        res++;
                    }
                } else if (isVisible(arr, idx, i)) {
                    res++;
                }
            }
        }
        return res;
    }

    public static int numSure(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int res = 0;
        HashSet<String> equalCounted = new HashSet<>();
        for (int i = 0 ; i < arr.length; i++) {
            res += visibleNum(arr, i, equalCounted);
        }
        return res;
    }

    //

    private static int[] randomArr(int size, int max) {
        int[] arr = new int[(int) (size * Math.random())];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (max * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 3000000;
        int size = 10;
        int max = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(size, max);
            if (num(arr) != numSure(arr)) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
