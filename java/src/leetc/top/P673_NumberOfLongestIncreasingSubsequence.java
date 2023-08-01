package leetc.top;

import java.util.ArrayList;
import java.util.TreeMap;

public class P673_NumberOfLongestIncreasingSubsequence {
    public static int findNumberOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        // [序列长度]<历史小数, 历史个数>
        ArrayList<TreeMap<Integer, Integer>> dp = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int l = 0, r = dp.size() - 1;
            int find = -1;
            while (l <= r) {
                int mid = (l + r) / 2;
                if (dp.get(mid).firstKey() >= nums[i]) {
                    find = mid;
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            int num = 1;
            int idx = find == -1 ? dp.size() : find;
            if (idx > 0) {
                TreeMap<Integer, Integer> lastMap = dp.get(idx - 1);
                num = lastMap.get(lastMap.firstKey());
                if (lastMap.ceilingKey(nums[i]) != null) {
                    num -= lastMap.get(lastMap.ceilingKey(nums[i]));
                }
            }
            if (idx == dp.size()) {
                TreeMap<Integer, Integer> newMap = new TreeMap<>();
                newMap.put(nums[i], num);
                dp.add(newMap);
            } else {
                TreeMap<Integer, Integer> curMap = dp.get(idx);
                curMap.put(nums[i], curMap.get(curMap.firstKey()) + num);
            }
        }
        return dp.get(dp.size() - 1).firstEntry().getValue();
    }

}
