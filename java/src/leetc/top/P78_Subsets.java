package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class P78_Subsets {
    private static ArrayList<Integer> copy(LinkedList<Integer> path) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (Integer num : path) {
            ans.add(num);
        }
        return ans;
    }

    private static void process(int nums[], int idx, LinkedList<Integer> path, List<List<Integer>> ans) {
        if (idx == nums.length) {
            ans.add(copy(path));
            return;
        }
        process(nums, idx + 1, path, ans);
        path.addLast(nums[idx]);
        process(nums, idx + 1, path, ans);
        path.removeLast();
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        process(nums, 0, path, ans);
        return ans;
    }
}
