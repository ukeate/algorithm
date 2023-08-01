package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P46_Permutations {
    private static void swap(int[] nums, int i, int j ) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private static void process(int[] nums, int idx, List<List<Integer>> ans) {
        if (idx == nums.length) {
            ArrayList<Integer> cur = new ArrayList<>();
            for (int num : nums) {
                cur.add(num);
            }
            ans.add(cur);
            return;
        }
        for (int j = idx; j < nums.length; j++) {
            swap(nums, idx, j);
            process(nums, idx + 1, ans);
            swap(nums, idx, j);
        }
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        process(nums, 0, ans);
        return ans;
    }
}
