package base.greed;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * IPO问题 - Initial Public Offering（首次公开募股）
 * 
 * 问题描述：
 * 假设你有一些项目，每个项目都有成本和利润
 * 你有初始资金W，最多可以完成K个项目
 * 每次做完一个项目后，你会获得该项目的利润（资金会增加）
 * 求经过K个项目后能获得的最大资金
 * 
 * 贪心策略：
 * 1. 使用两个优先队列：按成本排序的小根堆 + 按利润排序的大根堆
 * 2. 每次从能够承担的项目中选择利润最大的项目
 * 3. 完成项目后资金增加，可能解锁更多项目
 * 
 * LeetCode链接：https://leetcode.cn/problems/ipo/
 */
// Initial Public Offering, 本金不扣除
// https://leetcode.cn/problems/ipo/
public class IPO {
    /**
     * 项目类 - 包含利润和成本
     */
    private static class Item {
        public int p;   // profit - 利润
        public int c;   // cost - 成本

        public Item(int p, int c) {
            this.p = p;
            this.c = c;
        }
    }

    /**
     * 按成本升序排列的比较器
     * 用于小根堆，优先处理成本低的项目
     */
    private static class MinCostComp implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.c - o2.c;
        }
    }

    /**
     * 按利润降序排列的比较器
     * 用于大根堆，优先选择利润高的项目
     */
    private static class MaxProfitComp implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return o2.p - o1.p;
        }
    }

    /**
     * 求解IPO问题的贪心算法
     * 
     * 算法思路：
     * 1. 将所有项目按成本放入小根堆
     * 2. 对于每个可做的项目：
     *    - 将当前资金能承担的所有项目放入利润大根堆
     *    - 从利润大根堆中选择利润最大的项目执行
     *    - 更新资金，重复上述过程
     * 
     * 时间复杂度：O(n log n + k log n)
     * 空间复杂度：O(n)
     * 
     * @param k 最多可做的项目数
     * @param w 初始资本
     * @param profits 项目利润数组
     * @param capital 项目成本数组
     * @return 完成k个项目后的最大资金
     */
    // k个项目, w初始资本
    public static int maxCapital(int k, int w, int[] profits, int[] capital) {
        // 按成本排序的小根堆 - 存储所有待选项目
        PriorityQueue<Item> minCostQ = new PriorityQueue<>(new MinCostComp());
        // 按利润排序的大根堆 - 存储当前资金能承担的项目
        PriorityQueue<Item> maxProfitQ = new PriorityQueue<>(new MaxProfitComp());
        
        // 将所有项目放入成本小根堆
        for (int i = 0; i < profits.length; i++) {
            minCostQ.add(new Item(profits[i], capital[i]));
        }
        
        // 最多做k个项目
        for (int i = 0; i < k; i++) {
            // 将当前资金能承担的所有项目移到利润大根堆
            while (!minCostQ.isEmpty() && minCostQ.peek().c <= w) {
                maxProfitQ.add(minCostQ.poll());
            }
            
            // 如果没有项目可做，直接返回当前资金
            if (maxProfitQ.isEmpty()) {
                return w;
            }
            
            // 选择利润最大的项目，更新资金
            w += maxProfitQ.poll().p;
        }
        return w;
    }
}
