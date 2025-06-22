package base.dp6_biz;

import java.util.LinkedList;

/**
 * 不超过m长度的子数组最大和问题
 * 给定数组和长度限制m，求长度不超过m的连续子数组的最大和
 * 使用前缀和+单调双端队列优化，时间复杂度O(n)
 */
public class MaxSumSubM {
    /**
     * 滑动窗口 + 单调队列解法
     * 核心思想：维护窗口内前缀和的最大值，用于快速计算子数组和
     * @param arr 输入数组
     * @param m 最大长度限制
     * @return 最大子数组和
     */
    public static int max(int[] arr, int m) {
        if (arr == null || arr.length == 0 || m < 1) {
            return 0;
        }
        int n = arr.length;
        // 计算前缀和数组
        int[] sum = new int[n];
        sum[0] = arr[0];
        for (int i = 1; i < n; i++) {
            sum[i] = sum[i - 1] + arr[i];
        }
        
        // 单调递减双端队列，维护窗口内前缀和的最大值
        LinkedList<Integer> qmax = new LinkedList<>();
        int i = 0;
        int end = Math.min(n, m);
        
        // 初始化：处理从0开始长度不超过m的子数组
        for (; i < end; i++) {
            // 保持队列单调递减
            while (!qmax.isEmpty() && sum[qmax.peekLast()] <= sum[i]) {
                qmax.pollLast();
            }
            qmax.add(i);
        }
        int max = sum[qmax.peekFirst()]; // 当前窗口最大前缀和
        
        // 滑动窗口：考虑不以0开头的子数组
        int l = 0; // 窗口左边界
        for (; i < n; l++, i++) {
            // 移除过期元素
            if (qmax.peekFirst() == l) {
                qmax.pollFirst();
            }
            // 维护单调性
            while (!qmax.isEmpty() && sum[qmax.peekLast()] <= sum[i]) {
                qmax.pollLast();
            }
            qmax.add(i);
            // 更新最大值：sum[qmax.peekFirst()] - sum[l] 表示子数组[l+1, qmax.peekFirst()]的和
            max = Math.max(max, sum[qmax.peekFirst()] - sum[l]);
        }
        
        // 处理剩余窗口收缩阶段
        for (; l < n - 1; l++) {
            if (qmax.peekFirst() == l) {
                qmax.pollFirst();
            }
            max = Math.max(max, sum[qmax.peekFirst()] - sum[l]);
        }
        return max;
    }

    //

    /**
     * 暴力解法 - 用于验证正确性
     * 枚举所有长度不超过m的子数组
     */
    public static int sure(int[] arr, int m) {
        if (arr == null || arr.length == 0 || m < 1) {
            return 0;
        }
        int n = arr.length;
        int max = Integer.MIN_VALUE;
        for (int l = 0; l < n; l++) {
            int sum = 0;
            for (int r = l; r < n; r++) {
                if (r - l + 1 > m) {
                    break; // 超出长度限制
                }
                sum += arr[r];
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    //

    /**
     * 生成随机数组用于测试
     */
    private static int[] randomArr(int len, int max) {
        int[] arr = new int[len];
        for (int i = 0; i < len; i++) {
            arr[i] = (int) ((max + 1) * Math.random()) - (int) ((max + 1) * Math.random());
        }
        return arr;
    }

    public static void main(String[] args) {
        int times = 1000000;
        int maxLen = 50;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int) ((maxLen + 1) * Math.random());
            int m = (int) ((maxLen + 1) * Math.random());
            int[] arr = randomArr(len, maxVal);
            int ans1 = max(arr, m);
            int ans2 = sure(arr, m);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                break;
            }
        }
        System.out.println("test end");
    }
}
