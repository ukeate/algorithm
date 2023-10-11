package leetc.top;

import java.util.ArrayList;
import java.util.Arrays;

// 类似leetcode 631
public class P358_RearrangeStringKDistanceApart {
    private static boolean isValid(int n, int k, int maxCount, int maxKinds) {
        int restTasks = n - maxKinds;
        int spaces = k * (maxCount - 1);
        return spaces - restTasks <= 0;
    }

    public String rearrangeString(String s, int k) {
        if (s == null || s.length() < k) {
            return s;
        }
        char[] str = s.toCharArray();
        int[][] cnts = new int[256][2];
        for (int i = 0; i < 256; i++) {
            cnts[i] = new int[]{i, 0};
        }
        int maxCount = 0;
        for (char task : str) {
            cnts[task][1]++;
            maxCount = Math.max(maxCount, cnts[task][1]);
        }
        int maxKinds = 0;
        for (int task = 0; task < 256; task++) {
            if (cnts[task][1] == maxCount) {
                maxKinds++;
            }
        }
        int n = str.length;
        if (!isValid(n, k, maxCount, maxKinds)) {
            return "";
        }
        ArrayList<StringBuilder> ans = new ArrayList<>();
        for (int i = 0; i < maxCount; i++) {
            ans.add(new StringBuilder());
        }
        Arrays.sort(cnts, (a, b) -> (b[1] - a[1]));
        int i = 0;
        for (; i < 256 && cnts[i][1] == maxCount; i++) {
            for (int j = 0; j < maxCount; j++) {
                ans.get(j).append((char) cnts[i][0]);
            }
        }
        int out = 0;
        for (; i < 256; i++) {
            for (int j = 0; j < cnts[i][1]; j++) {
                ans.get(out).append((char) cnts[i][0]);
                out = out == ans.size() - 2 ? 0 : out + 1;
            }
        }
        StringBuilder builder = new StringBuilder();
        for (StringBuilder b : ans) {
            builder.append(b.toString());
        }
        return builder.toString();
    }
}
