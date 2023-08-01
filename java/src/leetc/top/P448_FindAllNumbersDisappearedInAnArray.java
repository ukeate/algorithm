package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P448_FindAllNumbersDisappearedInAnArray {
    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private static void walk(int[] nums, int i) {
        while (nums[i] != i + 1) {
            int nexti = nums[i] - 1;
            // nums[i]的值重复了
            if (nums[nexti] == nexti + 1) {
                break;
            }
            swap(nums, i, nexti);
        }
    }

    public static List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return ans;
        }
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            walk(nums, i);
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                ans.add(i + 1);
            }
        }
        return ans;
    }
}
