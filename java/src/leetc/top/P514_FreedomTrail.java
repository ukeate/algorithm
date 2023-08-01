package leetc.top;

import java.util.ArrayList;
import java.util.HashMap;

public class P514_FreedomTrail {

    private static int dial(int i1, int i2, int size) {
        return Math.min(Math.abs(i1 - i2), Math.min(i1, i2) + size - Math.max(i1, i2));
    }

    private static int process(int preButton, int idx, char[] str, HashMap<Character, ArrayList<Integer>> map, int n, int[][] dp) {
        if (dp[preButton][idx] != -1) {
            return dp[preButton][idx];
        }
        int ans = Integer.MAX_VALUE;
        if (idx == str.length) {
            ans = 0;
        } else {
            char cur = str[idx];
            for (int next : map.get(cur)) {
                int cost = dial(preButton, next, n) + 1 + process(next, idx + 1, str, map, n, dp);
                ans = Math.min(ans, cost);
            }
        }
        dp[preButton][idx] = ans;
        return ans;
    }

    public static int findRotateSteps(String r, String k) {
        char[] ring = r.toCharArray();
        int n = ring.length;
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (!map.containsKey(ring[i])) {
                map.put(ring[i], new ArrayList<>());
            }
            map.get(ring[i]).add(i);
        }
        char[] str = k.toCharArray();
        int m = str.length;
        int[][] dp = new int[n][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= m; j++) {
                dp[i][j] = -1;
            }
        }
        return process(0, 0, str, map, n, dp);
    }

}
