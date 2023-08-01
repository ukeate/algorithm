package leetc.top;

import java.util.HashMap;

public class P128_LongestConsecutiveSequence {
    public static int longestConsecutive(int[] nums) {
        HashMap<Integer, Integer> map = new HashMap<>();
        int max = 0;
        for (int num : nums) {
            if (!map.containsKey(num)) {
                map.put(num, 1);
                int preLen = map.containsKey(num - 1) ? map.get(num - 1) : 0;
                int posLen = map.containsKey(num + 1) ? map.get(num + 1) : 0;
                int all = preLen + posLen + 1;
                map.put(num - preLen, all);
                map.put(num + posLen, all);
                max = Math.max(max, all);
            }
        }
        return max;
    }
}
