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
            int v = t[i] % m;
            if (v != 0) {
                if (v != k) {
                    return -1;
                }
                ans |= (1 << i);
            }
        }
        if (ans == 0) {
            int cnt = 0;
            for (int num : arr) {
                if (num == 0) {
                    cnt++;
                }
            }
            if (cnt != k) {
                return -1;
            }
        }
        return ans;
    }

    private static int kTimesNumSure(int[] arr, int k, int m) {
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
        return -1;
    }

    private static int randomNumber(int range) {
        return (int) (Math.random() * (range + 1)) - (int) (Math.random() * (range + 1));
    }


    private static int[] randomArray(int maxKinds, int range, int k, int m) {
        int kTimes = k;
        int kNum = randomNumber(range);
        int numKinds = (int) (Math.random() * maxKinds) + 2;
        int[] arr = new int[kTimes + (numKinds - 1) * m];
        int index = 0;
        for (; index < kTimes; index++) {
            arr[index] = kNum;
        }
        numKinds--;
        HashSet<Integer> set = new HashSet<>();
        set.add(kNum);
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

    private static void print(int[] arr) {
        if (arr == null || arr.length < 1) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }


    public static void main(String[] args) {
        int times = 100000;
        int kind = 10;
        int range = 100;
        int max = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int a = (int) (Math.random() * max) + 1;
            int b = (int) (Math.random() * max) + 1;
            int k = Math.min(a, b);
            int m = Math.max(a, b);
            if (k == m) m++;
            int[] arr = randomArray(kind, range, k, m);
            int ans1 = kTimesNum(arr, k, m);
            int ans2 = kTimesNumSure(arr, k, m);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println("k = " + k + ", m = " + m);
                print(arr);
                System.out.println();
                System.out.println(ans1);
                System.out.println(ans2);
                break;
            }
        }
        System.out.println("test end");
    }
}
