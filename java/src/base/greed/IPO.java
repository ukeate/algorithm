package base.greed;

import java.util.Comparator;
import java.util.PriorityQueue;

// Initial Public Offering, 本金不扣除
// https://leetcode.cn/problems/ipo/
public class IPO {
    private static class Item {
        public int p;
        public int c;

        public Item(int p, int c) {
            this.p = p;
            this.c = c;
        }
    }

    private static class MinCostComp implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.c - o2.c;
        }
    }

    private static class MaxProfitComp implements Comparator<Item> {
        @Override
        public int compare(Item o1, Item o2) {
            return o2.p - o1.p;
        }
    }

    // k个项目, w初始资本
    public static int maxCapital(int k, int w, int[] profits, int[] capital) {
        PriorityQueue<Item> minCostQ = new PriorityQueue<>(new MinCostComp());
        PriorityQueue<Item> maxProfitQ = new PriorityQueue<>(new MaxProfitComp());
        for (int i = 0; i < profits.length; i++) {
            minCostQ.add(new Item(profits[i], capital[i]));
        }
        for (int i = 0; i < k; i++) {
            while (!minCostQ.isEmpty() && minCostQ.peek().c <= w) {
                maxProfitQ.add(minCostQ.poll());
            }
            if (maxProfitQ.isEmpty()) {
                return w;
            }
            w += maxProfitQ.poll().p;
        }
        return w;
    }
}
