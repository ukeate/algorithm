package leetc.top;

import java.util.HashMap;

public class P956_TallestBillboard {
    public int tallestBillboard(int[] rods) {
        // <差值, 最大的小集合值>
        HashMap<Integer, Integer> dp = new HashMap<>(), cur;
        dp.put(0, 0);
        for (int num : rods) {
            if (num != 0) {
                cur = new HashMap<>(dp);
                for (int d : cur.keySet()) {
                    int small = cur.get(d);
                    // num放入big中
                    dp.put(d + num, Math.max(small, dp.getOrDefault(d + num, 0)));
                    // num放入small中
                    int dpSmall = dp.getOrDefault(Math.abs(num - d), 0);
                    if (d >= num) {
                        dp.put(d - num, Math.max(small + num, dpSmall));
                    } else {
                        dp.put(num - d, Math.max(small + d, dpSmall));
                    }
                }
            }
        }
        return dp.get(0);
    }
}
