package leetc.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P15_3Sum {
    private static List<List<Integer>> twoSum(int[] nums, int begin, int target) {
        int l = begin, r = nums.length - 1;
        List<List<Integer>> ans = new ArrayList<>();
        while (l < r) {
            if (nums[l] + nums[r] > target) {
                r--;
            } else if (nums[l] + nums[r] < target) {
                l++;
            } else {
                if (l == begin || nums[l - 1] != nums[l]) {
                    List<Integer> cur = new ArrayList<>();
                    cur.add(nums[l]);
                    cur.add(nums[r]);
                    ans.add(cur);
                }
                l++;
            }
        }
        return ans;
    }

    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> ans = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i == 0 || nums[i - 1] != nums[i]) {
                List<List<Integer>> nexts = twoSum(nums, i + 1, -nums[i]);
                for (List<Integer> cur : nexts) {
                    cur.add(0, nums[i]);
                    ans.add(cur);
                }
            }
        }
        return ans;
    }
}
