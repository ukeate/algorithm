package leetc.top;

import java.util.HashMap;

public class P454_4SumII {
    public static int fourSumCount(int[] a1, int[] a2, int[] a3, int[] a4) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        for (int i = 0; i < a1.length; i++) {
            for (int j = 0; j < a2.length; j++) {
                sum = a1[i] + a2[j];
                if (!map.containsKey(sum)) {
                    map.put(sum, 1);
                } else {
                    map.put(sum, map.get(sum) + 1);
                }
            }
        }
        int ans = 0;
        for (int i = 0; i < a3.length; i++) {
            for (int j = 0; j < a4.length; j++) {
                sum = a3[i] + a4[j];
                if (map.containsKey(-sum)) {
                    ans += map.get(-sum);
                }
            }
        }
        return ans;
    }
}
