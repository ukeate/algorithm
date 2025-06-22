package base.dp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 咖啡机冲咖啡问题
 * 
 * 问题描述：
 * 数组arr代表每一个咖啡机冲一杯咖啡的时间，每个咖啡机只能串行的工作，
 * 现在有N个人需要喝咖啡，只有用咖啡机做出来的咖啡才能喝，
 * 时间点从0开始，返回所有人喝到咖啡的时间中最迟的时间点。
 * 
 * 扩展问题：
 * 喝完咖啡需要洗杯子，杯子可以自己洗（需要时间a），也可以自然晾干（需要时间b）。
 * 洗杯机只有一台，同一时刻只能洗一个杯子。
 * 所有的杯子都洗完或晾干的时间点最早是什么时候？
 * 
 * 解法分析：
 * 1. 贪心算法：用小根堆维护咖啡机的状态，每次选择最早完成的咖啡机
 * 2. 递归枚举：对每个杯子决定是洗还是晾干
 * 3. 动态规划：优化递归解法，避免重复计算
 * 
 * 时间复杂度：O(N * maxFree)，其中maxFree是洗杯机可能的最大空闲时间
 * 空间复杂度：O(N * maxFree)
 */
public class Coffee {
    
    /**
     * 暴力递归：清洗杯子的最优策略
     * 
     * @param drinks 每个人喝完咖啡的时间点数组
     * @param a 洗杯子需要的时间
     * @param b 杯子自然晾干需要的时间
     * @param idx 当前处理第几个杯子
     * @param washLine 洗杯机下次可用的时间点
     * @param time 当前已处理杯子的最晚完成时间
     * @return 所有杯子处理完毕的最早时间点
     */
    private static int forceWash(int[] drinks, int a, int b, int idx, int washLine, int time) {
        // base case：所有杯子都处理完了
        if (idx == drinks.length) {
            return time;
        }
        
        // 选择1：用洗杯机洗当前杯子
        int wash = Math.max(drinks[idx], washLine) + a;  // 洗完的时间点
        int ans1 = forceWash(drinks, a, b, idx + 1, wash, Math.max(wash, time));

        // 选择2：让当前杯子自然晾干
        int dry = drinks[idx] + b;  // 晾干的时间点
        int ans2 = forceWash(drinks, a, b, idx + 1, washLine, Math.max(dry, time));
        
        return Math.min(ans1, ans2);
    }

    /**
     * 暴力枚举：生成所有可能的喝咖啡方案
     * 
     * @param arr 每个咖啡机制作一杯咖啡的时间
     * @param times 每个咖啡机当前的可用时间点
     * @param idx 当前安排第几个人
     * @param drink 记录每个人喝咖啡的时间点
     * @param n 总人数
     * @param a 洗杯子时间
     * @param b 晾干时间
     * @return 最优的总完成时间
     */
    private static int forceMake(int[] arr, int[] times, int idx, int[] drink, int n, int a, int b) {
        // base case：所有人都安排完了
        if (idx == n) {
            // 将喝咖啡时间排序，然后计算洗杯子的最优时间
            int[] drinkSorted = Arrays.copyOf(drink, idx);
            Arrays.sort(drinkSorted);
            return forceWash(drinkSorted, a, b, 0, 0, 0);
        }
        
        int time = Integer.MAX_VALUE;
        
        // 尝试让当前这个人使用每一台咖啡机
        for (int i = 0; i < arr.length; i++) {
            int work = arr[i];     // 咖啡机制作时间
            int pre = times[i];    // 咖啡机当前可用时间
            
            drink[idx] = pre + work;  // 这个人喝到咖啡的时间
            times[i] = pre + work;    // 更新咖啡机下次可用时间
            
            time = Math.min(time, forceMake(arr, times, idx + 1, drink, n, a, b));
            
            // 回溯
            drink[idx] = 0;
            times[i] = pre;
        }
        return time;
    }

    /**
     * 暴力解法入口
     * 
     * @param arr 每个咖啡机制作一杯咖啡需要的时间
     * @param n 人数
     * @param a 洗杯子需要的时间
     * @param b 杯子自然晾干需要的时间
     * @return 所有人喝完咖啡并清洗完杯子的最早时间点
     */
    public static int minTimeSure(int[] arr, int n, int a, int b) {
        // 每个咖啡机的可用时间
        int[] times = new int[arr.length];
        // 冲完咖啡的时间
        int[] drink = new int[n];
        return forceMake(arr, times, 0, drink, n, a, b);
    }

    /**
     * 咖啡机状态类，用于优先队列
     */
    private static class Machine {
        public int timePoint;  // 下次可用时间点
        public int workTime;   // 制作一杯咖啡需要的时间

        public Machine(int t, int w) {
            timePoint = t;
            workTime = w;
        }
    }

    /**
     * 咖啡机比较器，按照完成时间排序（timePoint + workTime）
     */
    private static class MachineComp implements Comparator<Machine> {
        @Override
        public int compare(Machine o1, Machine o2) {
            return (o1.timePoint + o1.workTime) - (o2.timePoint + o2.workTime);
        }
    }

    /**
     * 清洗杯子的递归解法
     * 
     * @param drinks 每个人喝完咖啡的时间点
     * @param a 洗杯子时间
     * @param b 晾干时间
     * @param idx 当前处理第几个杯子
     * @param free 洗杯机下次可用时间
     * @return 从idx开始的所有杯子处理完的最早时间
     */
    private static int cleanTime(int[] drinks, int a, int b, int idx, int free) {
        // base case：所有杯子都处理完了
        if (idx == drinks.length) {
            return 0;
        }
        
        // 选择1：用洗杯机洗当前杯子
        int washed = Math.max(drinks[idx], free) + a;
        int rest1 = cleanTime(drinks, a, b, idx + 1, washed);
        int p1 = Math.max(washed, rest1);

        // 选择2：让当前杯子自然晾干
        int aired = drinks[idx] + b;
        int rest2 = cleanTime(drinks, a, b, idx + 1, free);
        int p2 = Math.max(aired, rest2);
        
        return Math.min(p1, p2);
    }

    /**
     * 贪心+递归解法
     * 
     * @param arr 咖啡机数组
     * @param n 人数
     * @param a 洗杯子时间
     * @param b 晾干时间
     * @return 最早完成时间
     */
    public static int minTime1(int[] arr, int n, int a, int b) {
        // 使用小根堆维护咖啡机状态，按完成时间排序
        PriorityQueue<Machine> heap = new PriorityQueue<>(new MachineComp());
        for (int i = 0; i < arr.length; i++) {
            heap.add(new Machine(0, arr[i]));
        }
        
        // 贪心分配：每次选择最早能完成的咖啡机
        int[] drinks = new int[n];
        for (int i = 0; i < n; i++) {
            Machine cur = heap.poll();
            cur.timePoint += cur.workTime;  // 更新下次可用时间
            drinks[i] = cur.timePoint;      // 记录喝咖啡时间
            heap.add(cur);                  // 重新加入堆
        }
        
        return cleanTime(drinks, a, b, 0, 0);
    }

    /**
     * 动态规划解法：清洗杯子
     * 
     * @param drinks 每个人喝完咖啡的时间点
     * @param a 洗杯子时间
     * @param b 晾干时间
     * @return 所有杯子处理完的最早时间
     */
    private static int cleanTimeDp(int[] drinks, int a, int b) {
        int n = drinks.length;
        
        // 计算洗杯机可能的最大空闲时间
        int maxFree = 0;
        for (int i = 0; i < drinks.length; i++) {
            maxFree = Math.max(maxFree, drinks[i]) + a;
        }
        
        // dp[idx][free]表示从idx开始，洗杯机在free时间可用时的最优解
        int[][] dp = new int[n + 1][maxFree + 1];
        
        // 从后往前填充DP表
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int free = 0; free <= maxFree; free++) {
                // 选择1：用洗杯机洗
                int washed = Math.max(drinks[idx], free) + a;
                if (washed > maxFree) {
                    break;  // 超出边界，跳出内层循环
                }
                int rest1 = dp[idx + 1][washed];
                int p1 = Math.max(washed, rest1);

                // 选择2：自然晾干
                int aired = drinks[idx] + b;
                int rest2 = dp[idx + 1][free];
                int p2 = Math.max(aired, rest2);
                
                dp[idx][free] = Math.min(p1, p2);
            }
        }
        
        return dp[0][0];
    }

    /**
     * 贪心+动态规划解法
     * 
     * @param arr 咖啡机数组
     * @param n 人数
     * @param a 洗杯子时间
     * @param b 晾干时间
     * @return 最早完成时间
     */
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

    /**
     * 生成随机数组用于测试
     * 
     * @param len 数组长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int len, int maxVal) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) ((maxVal + 1) * Math.random()) + 1;
        }
        return arr;
    }

    /**
     * 打印数组
     * 
     * @param arr 要打印的数组
     */
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
