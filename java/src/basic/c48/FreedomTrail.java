package basic.c48;

import java.util.ArrayList;
import java.util.HashMap;

// https://leetcode.com/problems/freedom-trail/
// s1在圆盘上，从前向后拼s2,每拼一个要按次按钮, 求最小步数
public class FreedomTrail {
    private static int dial(int i1, int i2, int size) {
        return Math.min(Math.abs(i1 - i2), Math.min(i1, i2) + size - Math.max(i1, i2));
    }

    // preStrIdx目前对齐的位置, keyIdx目标idx, 返回最小代价
    private static int minSteps(int preStrIdx, int keyIdx, char[] key, HashMap<Character, ArrayList<Integer>> map, int size, int[][] dp) {
        if (dp[preStrIdx][keyIdx] != -1) {
            return dp[preStrIdx][keyIdx];
        }
        if (keyIdx == key.length) {
            dp[preStrIdx][keyIdx] = 0;
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        for (int curStrIdx : map.get(key[keyIdx])) {
            int step = dial(preStrIdx, curStrIdx, size) + 1
                    + minSteps(curStrIdx, keyIdx + 1, key, map, size, dp);
            ans = Math.min(ans, step);
        }
        dp[preStrIdx][keyIdx] = ans;
        return ans;
    }

    public static int min(String r, String k) {
        char[] ring = r.toCharArray();
        int size = ring.length;
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            if (!map.containsKey(ring[i])) {
                map.put(ring[i], new ArrayList<>());
            }
            map.get(ring[i]).add(i);
        }
        int[][] dp = new int[size][k.length() + 1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= k.length(); j++) {
                dp[i][j] = -1;
            }
        }
        return minSteps(0, 0, k.toCharArray(), map, size, dp);
    }
}
