package leetc.top;

import java.util.HashMap;

public class P992_SubarraysWithKDifferentIntegers {
    public static int subarraysWithKDistinct1(int[] nums, int k) {
        int n = nums.length;
        int[] lessCnt = new int[n + 1];
        int[] equalCnt = new int[n + 1];
        int lessLeft = 0, equalLeft = 0, lessKinds = 0, equalKinds = 0;
        int ans = 0;
        for (int r = 0; r < n; r++) {
            if (lessCnt[nums[r]] == 0) {
                lessKinds++;
            }
            if (equalCnt[nums[r]] == 0) {
                equalKinds++;
            }
            lessCnt[nums[r]]++;
            equalCnt[nums[r]]++;
            while (lessKinds == k) {
                if (lessCnt[nums[lessLeft]] == 1) {
                    lessKinds--;
                }
                lessCnt[nums[lessLeft++]]--;
            }
            while (equalKinds > k) {
                if (equalCnt[nums[equalLeft]] == 1) {
                    equalKinds--;
                }
                equalCnt[nums[equalLeft++]]--;
            }
            ans += lessLeft - equalLeft;
        }
        return ans;
    }

    //

    private static int numsMostK(int[] arr, int k) {
        int i = 0, res = 0;
        HashMap<Integer, Integer> count = new HashMap<>();
        for (int j = 0; j < arr.length; ++j) {
            if (count.getOrDefault(arr[j], 0) == 0) {
                k--;
            }
            count.put(arr[j], count.getOrDefault(arr[j], 0) + 1);
            while (k < 0) {
                count.put(arr[i], count.get(arr[i]) - 1);
                if (count.get(arr[i]) == 0) {
                    k++;
                }
                i++;
            }
            res += j - i + 1;
        }
        return res;
    }

    public static int subarraysWithKDistinct2(int[] arr, int k) {
        return numsMostK(arr, k) - numsMostK(arr, k - 1);
    }
}
