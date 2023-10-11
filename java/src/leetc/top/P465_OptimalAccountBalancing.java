package leetc.top;

import java.util.HashMap;

// 需要证明：
// 一个集合中，假设整体的累加和为K，
// 不管该集合用了什么样的0集合划分方案，当一个新的数到来时：
// 1) 如果该数是-K，那么任何0集合的划分方案中，因为新数字的加入，0集合的数量都会+1
// 2) 如果该数不是-K，那么任何0集合的划分方案中，0集合的数量都会不变
public class P465_OptimalAccountBalancing {

    private static int[] debts(int[][] transactions) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int[] tran : transactions) {
            map.put(tran[0], map.getOrDefault(tran[0], 0) + tran[2]);
            map.put(tran[1], map.getOrDefault(tran[1], 0) - tran[2]);
        }
        int n = 0;
        for (int val : map.values()) {
            if (val != 0) {
                n++;
            }
        }
        int[] debt = new int[n];
        int idx = 0;
        for (int val : map.values()) {
            if (val != 0) {
                debt[idx++] = val;
            }
        }
        return debt;
    }

    // 返回值含义 : set这个集合中，最多能划分出多少个小集合累加和是0，返回累加和是0的小集合最多的数量
    private static int process1(int[] debt, int set, int sum, int n) {
        if ((set & (set - 1)) == 0) {
            return 0;
        }
        int val = 0;
        int max = 0;
        // 尝试，每一个人都最后考虑
        for (int i = 0; i < n; i++) {
            val = debt[i];
            if ((set & (1 << i)) != 0) {
                max = Math.max(max, process1(debt, set ^ (1 << i), sum - val, n));
            }
        }
        return sum == 0 ? max + 1 : max;
    }

    public static int minTransfers1(int[][] transactions) {
        int[] debt = debts(transactions);
        int n = debt.length;
        return n - process1(debt, (1 << n) - 1, 0, n);
    }
}
