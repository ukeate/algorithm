package basic.c53;

import java.util.HashMap;

// 数组元素不重复使用，产生相等两个集合, 求集合最大值
public class TallestBillboard {
    public int max(int[] arr) {
        // <差值, 和大的一对的小值>
        HashMap<Integer, Integer> dp = new HashMap<>(), cur;
        dp.put(0, 0);
        for (int num : arr) {
            if (num == 0) {
                continue;
            }
            cur = new HashMap<>(dp);
            for (int diff : cur.keySet()) {
                int less = cur.get(diff);
                // num加到大值
                dp.put(diff + num, Math.max(less, dp.getOrDefault(num + diff, 0)));
                // num加到小值
                int oldLess = dp.getOrDefault(Math.abs(num - diff), 0);
                if (diff >= num) {
                    dp.put(diff - num, Math.max(less + num, oldLess));
                } else {
                    dp.put(num - diff, Math.max(less + diff, oldLess));
                }
            }
        }
        return dp.get(0);
    }
}
