package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;

public class P446_ArithmeticSlicesIISubsequence {
    // O(N^2)
    public static int numberOfArithmeticSlices(int[] arr) {
        int n = arr.length;
        int ans = 0;
        ArrayList<HashMap<Integer, Integer>> maps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            maps.add(new HashMap<>());
            for (int j = i - 1; j >= 0; j--) {
                long diff = (long) arr[i] - (long) arr[j];
                if (diff <= Integer.MIN_VALUE || diff > Integer.MAX_VALUE) {
                    continue;
                }
                int dif = (int) diff;
                int count = maps.get(j).getOrDefault(dif, 0);
                ans += count;
                maps.get(i).put(dif, maps.get(i).getOrDefault(dif, 0) + count + 1);
            }
        }
        return ans;
    }
}
