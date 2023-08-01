package leetc.top;

import java.util.HashMap;

public class P560_SubarraySumEqualsK {
    public static int subarraySum(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        int all = 0, ans = 0;
        for (int i = 0; i < nums.length; i++) {
            all += nums[i];
            if (map.containsKey(all - k)) {
                ans += map.get(all - k);
            }
            if (!map.containsKey(all)) {
                map.put(all, 1);
            } else {
                map.put(all, map.get(all) + 1);
            }
        }
        return ans;
    }
}
