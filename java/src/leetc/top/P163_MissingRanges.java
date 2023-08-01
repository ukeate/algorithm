package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P163_MissingRanges {
    private static String miss(int lower, int upper) {
        String left = String.valueOf(lower);
        String right = "";
        if (upper > lower) {
            right = "->" + upper;
        }
        return left + right;
    }
    public static List<String> findMissingRanges(int[] nums, int lower, int upper) {
        List<String> ans = new ArrayList<>();
        for (int num : nums) {
            if (num > lower) {
                ans.add(miss(lower, num - 1));
            }
            if (num == upper) {
                return ans;
            }
            lower = num + 1;
        }
        if (lower <= upper) {
            ans.add(miss(lower, upper));
        }
        return ans;
    }
}
