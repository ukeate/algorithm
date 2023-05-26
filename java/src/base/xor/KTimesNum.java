package base.xor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class KTimesNum {
    // k < m, 只有一种数出现k次, 其它m次
    public static int kTimesNum(int[] arr, int k, int m) {
        int[] t = new int[32];
        for (int num : arr) {
            for (int i = 0; i < 32; i++) {
                t[i] += ((num >> i) & 1);
            }
        }
        int ans = 0;
        for (int i = 0; i < 32; i++) {
            if (t[i] % m != 0) {
                ans |= (1 << i);
            }
        }
        return ans;
    }

    public static int kTimesNumSure(int[] arr, int k, int m) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }
        for (int num : map.keySet()) {
            if (map.get(num) == k) {
                return num;
            }
        }
        return 0;
    }

    private static int randomNumber(int range) {
        return (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));
    }


    private static int[] randomArray(int maxKinds, int range, int k, int m) {
        int ktimeNum = randomNumber(range);
        int times = k;
        int numKinds = (int) (Math.random() * maxKinds) + 2;
        int[] arr = new int[times + (numKinds - 1) * m];
        int index = 0;
        for (; index < times; index++) {
            arr[index] = ktimeNum;
        }
        numKinds--;
        HashSet<Integer> set = new HashSet<>();
        set.add(ktimeNum);
        while (numKinds != 0) {
            int curNum = 0;
            do {
                curNum = randomNumber(range);
            } while (set.contains(curNum));
            set.add(curNum);
            numKinds--;
            for (int i = 0; i < m; i++) {
                arr[index++] = curNum;
            }
        }
        for (int i = 0; i < arr.length; i++) {
            int j = (int) (Math.random() * arr.length);
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }


    public static void main(String[] args) {
        int kind = 5;
        int range = 30;
        int testTimes = (int) 1e5;
        int max = 9;
        System.out.println("testKTimes start");
        for (int i = 0; i < testTimes; i++) {
            int a = (int) (Math.random() * max) + 1;
            int b = (int) (Math.random() * max) + 1;
            int k = Math.min(a, b);
            int m = Math.max(a, b);
            if (k == m) m++;
            int[] arr = randomArray(kind, range, k, m);
            int ans1 = kTimesNumSure(arr, k, m);
            int ans2 = kTimesNum(arr, k, m);
            if (ans1 != ans2) {
                System.out.println("k = " + k + ", m = " + m);
                for (int ii = 0; ii < arr.length; ii++) {
                    System.out.print(arr[ii] + ",");
                }
                System.out.println();
                System.out.println(ans1);
                System.out.println(ans2);
                System.out.println("Oops");
            }
        }
        System.out.println("testKTimes end");
    }
}
