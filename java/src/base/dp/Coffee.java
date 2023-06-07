package base.dp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Coffee {
    // washLine为当前咖啡机可工作时间, time为当前时间
    private static int forceWash(int[] drinks, int a, int b, int idx, int washLine, int time) {
        if (idx == drinks.length) {
            return time;
        }
        int wash = Math.max(drinks[idx], washLine) + a;
        int ans1 = forceWash(drinks, a, b, idx + 1, wash, Math.max(wash, time));

        int dry = drinks[idx] + b;
        int ans2 = forceWash(drinks, a, b, idx + 1, washLine, Math.max(dry, time));
        return Math.min(ans1, ans2);
    }

    private static int forceMake(int[] arr, int[] times, int idx, int[] drink, int n, int a, int b) {
        if (idx == n) {
            int[] drinkSorted = Arrays.copyOf(drink, idx);
            Arrays.sort(drinkSorted);
            return forceWash(drinkSorted, a, b, 0, 0, 0);
        }
        int time = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            int work = arr[i];
            int pre = times[i];
            drink[idx] = pre + work;
            times[i] = pre + work;
            time = Math.min(time, forceMake(arr, times, idx + 1, drink, n, a, b));
            drink[idx] = 0;
            times[i] = pre;
        }
        return time;
    }

    // arr是每个咖啡机冲咖啡的能力时间，n个人喝，瞬间喝完
    // 杯子可以洗或挥发，1台洗杯机，洗的时间为a，杯子挥发时间为b。返回最早时间点
    public static int minTimeSure(int[] arr, int n, int a, int b) {
        // 每个咖啡机的可用时间
        int[] times = new int[arr.length];
        // 冲完咖啡的时间
        int[] drink = new int[n];
        return forceMake(arr, times, 0, drink, n, a, b);
    }

    //

    private static class Machine {
        public int timePoint;
        public int workTime;

        public Machine(int t, int w) {
            timePoint = t;
            workTime = w;
        }
    }

    private static class MachineComp implements Comparator<Machine> {
        @Override
        public int compare(Machine o1, Machine o2) {
            return (o1.timePoint + o1.workTime) - (o2.timePoint + o2.workTime);
        }
    }

    // free洗衣机可用时间
    private static int cleanTime(int[] drinks, int a, int b, int idx, int free) {
        if (idx == drinks.length) {
            return 0;
        }
        int washed = Math.max(drinks[idx], free) + a;
        int rest1 = cleanTime(drinks, a, b, idx + 1, washed);
        int p1 = Math.max(washed, rest1);

        int aired = drinks[idx] + b;
        int rest2 = cleanTime(drinks, a, b, idx + 1, free);
        int p2 = Math.max(aired, rest2);
        return Math.min(p1, p2);
    }

    public static int minTime1(int[] arr, int n, int a, int b) {
        PriorityQueue<Machine> heap = new PriorityQueue<>(new MachineComp());
        for (int i = 0; i < arr.length; i++) {
            heap.add(new Machine(0, arr[i]));
        }
        int[] drinks = new int[n];
        for (int i = 0; i < n; i++) {
            Machine cur = heap.poll();
            cur.timePoint += cur.workTime;
            drinks[i] = cur.timePoint;
            heap.add(cur);
        }
        return cleanTime(drinks, a, b, 0, 0);
    }

    //

    private static int cleanTimeDp(int[] drinks, int a, int b) {
        int n = drinks.length;
        int maxFree = 0;
        for (int i = 0; i < drinks.length; i++) {
            maxFree = Math.max(maxFree, drinks[i]) + a;
        }
        int[][] dp = new int[n + 1][maxFree + 1];
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int free = 0; free <= maxFree; free++) {
                int washed = Math.max(drinks[idx], free) + a;
                if (washed > maxFree) {
                    break;
                }
                int rest1 = dp[idx + 1][washed];
                int p1 = Math.max(washed, rest1);

                int aired = drinks[idx] + b;
                int rest2 = dp[idx + 1][free];
                int p2 = Math.max(aired, rest2);
                dp[idx][free] = Math.min(p1, p2);
            }
        }
        return dp[0][0];
    }

    public static int minTime2(int[] arr, int n, int a, int b) {
        PriorityQueue<Machine> heap = new PriorityQueue<>(new MachineComp());
        for (int i = 0; i < arr.length; i++) {
            heap.add(new Machine(0, arr[i]));
        }
        int[] drinks = new int[n];
        for (int i = 0; i < n; i++) {
            Machine cur = heap.poll();
            cur.timePoint += cur.workTime;
            drinks[i] = cur.timePoint;
            heap.add(cur);
        }
        return cleanTimeDp(drinks, a, b);
    }

    //

    private static int[] randomArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return arr;
    }

    private static void print(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int times = 10;
        int len = 10;
        int maxVal = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int[] arr = randomArr(len, maxVal);
            int n = (int) (Math.random() * 7) + 1;
            int a = (int) (Math.random() * 7) + 1;
            int b = (int) (Math.random() * 10) + 1;
            int ans1 = minTimeSure(arr, n, a, b);
            int ans2 = minTime1(arr, n, a, b);
            int ans3 = minTime2(arr, n, a, b);
            if (ans1 != ans2 || ans2 != ans3) {
                print(arr);
                System.out.println(n + "|" + a + "|" + b);
                System.out.println(ans1 + "|" + ans2 + "|" + ans3);
                break;
            }
        }
        System.out.println("test end");
    }
}
