package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class P39_CombinationSum {
    private static List<List<Integer>> process1(int[] arr, int idx, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        if (target == 0) {
            ans.add(new ArrayList<>());
            return ans;
        }
        if (idx == -1) {
            return ans;
        }
        for (int take = 0; take * arr[idx] <= target; take++) {
            List<List<Integer>> preAns = process1(arr, idx - 1, target - (take * arr[idx]));
            for (List<Integer> path : preAns) {
                for (int i = 0; i < take; i++) {
                    path.add(arr[idx]);
                }
                ans.add(path);
            }
        }
        return ans;
    }

    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        return process1(candidates, candidates.length - 1, target);
    }
}
