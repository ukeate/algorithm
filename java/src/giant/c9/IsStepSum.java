package giant.c9;

import java.util.HashMap;

// StepSum(680) = 680 + 68 + 6, 求一个数是否为某个StepSum
public class IsStepSum {
    private static int stepSum(int num) {
        int sum = 0;
        while (num != 0) {
            sum += num;
            num /= 10;
        }
        return sum;
    }

    public static boolean isStepSum(int stepSum) {
        int l = 0, r = stepSum, m = 0, cur = 0;
        while (l <= r) {
            m = l + ((r - l) >> 1);
            cur = stepSum(m);
            if (cur == stepSum) {
                return true;
            } else if (cur < stepSum) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return false;
    }

    //

    private static HashMap<Integer, Integer> stepSumMap(int max) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i <= max; i++) {
            map.put(stepSum(i), i);
        }
        return map;
    }

    public static void main(String[] args) {
        int max = 1000000;
        int maxStepSum = stepSum(max);
        HashMap<Integer, Integer> ans = stepSumMap(max);
        System.out.println("test begin");
        for (int i = 0; i <= maxStepSum; i++) {
            if (isStepSum(i) ^ ans.containsKey(i)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
