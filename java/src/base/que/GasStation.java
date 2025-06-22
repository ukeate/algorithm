package base.que;

import java.util.LinkedList;

/**
 * 加油站问题 - 环形路上的加油站
 * 
 * LeetCode 134: https://leetcode.com/problems/gas-station
 * 
 * 问题描述：
 * 在一条环路上有n个加油站，其中第i个加油站有汽油gas[i]升。
 * 你有一辆油箱容量无限的汽车，从第i个加油站开往第i+1个加油站需要消耗汽油cost[i]升。
 * 你从其中的一个加油站出发，开始时油箱为空。
 * 给定两个整数数组gas和cost，如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回-1。
 * 如果题目有解，该答案即为唯一答案。
 * 
 * 算法思路：
 * 1. 将环形数组转换为线性数组，长度扩展为2n
 * 2. 计算每个位置的净收益：gain[i] = gas[i] - cost[i]
 * 3. 计算前缀和数组，用于快速查询区间和
 * 4. 使用单调双端队列维护滑动窗口中的最小值
 * 5. 对于每个起始位置，检查从该位置开始的n个位置中，前缀和的最小值是否非负
 * 
 * 核心观察：
 * 从位置i开始，如果能够成功绕一圈，则意味着从i开始的任何前缀和都不能为负数
 * 即在[i, i+n-1]这个窗口中，所有前缀和都要 >= 前缀和[i-1]
 * 
 * 时间复杂度：O(n) - 使用单调队列优化滑动窗口最小值查询
 * 空间复杂度：O(n) - 存储扩展数组和单调队列
 */
public class GasStation {
    /**
     * 预处理数组，生成可以成功出发的位置数组
     * 
     * 算法核心思想：
     * 1. 构造长度为2n的数组，存储净收益值
     * 2. 计算前缀和数组
     * 3. 使用滑动窗口最小值技术，判断每个位置是否可以作为起点
     * 
     * @param g 加油量数组
     * @param c 消耗量数组
     * @return 布尔数组，表示每个位置是否可以作为起点
     */
    private static boolean[] goodArr(int[] g, int[] c) {
        int n = g.length;
        int m = n << 1;  // 扩展为2倍长度
        
        // 构造净收益数组，长度为2n
        int[] arr = new int[m];
        for (int i = 0; i < n; i++) {
            arr[i] = g[i] - c[i];           // 第一轮
            arr[i + n] = g[i] - c[i];       // 第二轮（环形扩展）
        }
        
        // 计算前缀和数组
        for (int i = 1; i < m; i++) {
            arr[i] += arr[i - 1];
        }
        
        // 使用单调双端队列维护滑动窗口最小值
        LinkedList<Integer> w = new LinkedList<>();
        
        // 初始化第一个窗口[0, n-1]
        for (int i = 0; i < n; i++) {
            // 维护单调递增队列（队头是最小值）
            while (!w.isEmpty() && arr[w.peekLast()] >= arr[i]) {
                w.pollLast();
            }
            w.addLast(i);
        }
        
        boolean[] ans = new boolean[n];
        
        // 滑动窗口，检查每个起始位置
        for (int offset = 0, i = 0, j = n; j < m; offset = arr[i++], j++) {
            // 检查当前窗口[i, j-1]的最小前缀和是否 >= offset
            // offset是arr[i-1]，即起始位置前一个位置的前缀和
            if (arr[w.peekFirst()] - offset >= 0) {
                ans[i] = true;  // 位置i可以作为起点
            }
            
            // 移除窗口左边界
            if (w.peekFirst() == i) {
                w.pollFirst();
            }
            
            // 添加窗口右边界
            while (!w.isEmpty() && arr[w.peekLast()] >= arr[j]) {
                w.pollLast();
            }
            w.addLast(j);
        }
        return ans;
    }

    /**
     * 主函数 - 寻找可以完成环形旅行的起始加油站
     * 
     * @param gas 每个加油站的汽油量
     * @param cost 从每个加油站到下一个加油站的消耗量
     * @return 起始加油站的索引，如果无解则返回-1
     */
    public static int completeCircuitNum(int[] gas, int[] cost) {
        boolean[] good = goodArr(gas, cost);
        for (int i = 0; i < gas.length; i++) {
            if (good[i]) {
                return i;  // 返回第一个可行的起始位置
            }
        }
        return -1;  // 无解
    }

    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1：有解的情况
        int[] gas1 = {1, 2, 3, 4, 5};
        int[] cost1 = {3, 4, 5, 1, 2};
        System.out.println("测试用例1:");
        System.out.println("gas:  " + java.util.Arrays.toString(gas1));
        System.out.println("cost: " + java.util.Arrays.toString(cost1));
        System.out.println("结果: " + completeCircuitNum(gas1, cost1));
        System.out.println("预期: 3\n");
        
        // 测试用例2：无解的情况
        int[] gas2 = {2, 3, 4};
        int[] cost2 = {3, 4, 3};
        System.out.println("测试用例2:");
        System.out.println("gas:  " + java.util.Arrays.toString(gas2));
        System.out.println("cost: " + java.util.Arrays.toString(cost2));
        System.out.println("结果: " + completeCircuitNum(gas2, cost2));
        System.out.println("预期: -1");
    }
}

