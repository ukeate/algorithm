package giant.c6;

import java.util.ArrayList;
import java.util.HashMap;

public class MostXorZero {
    private static int eorZeros(int[] eor, ArrayList<Integer> parts) {
        int l = 0;
        int ans = 0;
        for (Integer end : parts) {
            if ((eor[end - 1] ^ (l == 0 ? 0 : eor[l - 1])) == 0) {
                ans++;
            }
            l = end;
        }
        return ans;
    }

    private static int process(int[] eor, int idx, ArrayList<Integer> parts) {
        int ans = 0;
        if (idx == eor.length) {
            parts.add(eor.length);
            ans = eorZeros(eor, parts);
            parts.remove(parts.size() - 1);
        } else {
            int p1 = process(eor, idx + 1, parts);
            parts.add(idx);
            int p2 = process(eor, idx + 1, parts);
            parts.remove(parts.size() - 1);
            ans = Math.max(p1, p2);
        }
        return ans;
    }

    public static int sure(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        int[] eor = new int[n];
        eor[0] = arr[0];
        for (int i = 1; i < n; i++) {
            eor[i] = eor[i - 1] ^ arr[i];
        }
        return process(eor, 1, new ArrayList<>());
    }

    //

    public static int most(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        int n = arr.length;
        // 当前向前最多几段0
        int[] dp = new int[n];
        // <前缀和，最晚出现的位置>
        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int xor = 0;
        for (int i = 0; i < n; i++) {
            xor ^= arr[i];
            if (map.containsKey(xor)) {
                int pre = map.get(xor);
                dp[i] = pre == -1 ? 1 : (dp[pre] + 1);
            }
            if (i > 0) {
                dp[i] = Math.max(dp[i - 1], dp[i]);
            }
            map.put(xor, i);
        }
        return dp[n - 1];
    }
}
