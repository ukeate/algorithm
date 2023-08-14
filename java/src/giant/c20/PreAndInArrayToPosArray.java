package giant.c20;

import java.util.HashMap;

public class PreAndInArrayToPosArray {
    private static void process(int[] pre, int l1, int r1, int[] in, int l2, int r2, int[] pos, int l3, int r3, HashMap<Integer, Integer> inMap) {
        if (l1 > r1) {
            return;
        }
        if (l1 == r1) {
            pos[l3] = pre[l1];
        } else {
            pos[r3] = pre[l1];
            int idx = inMap.get(pre[l1]);
            process(pre, l1 + 1, l1 + idx - l2, in, l2, idx - 1, pos, l3, l3 + idx - l2 - 1, inMap);
            process(pre, l1 + idx - l2 + 1, r1, in, idx + 1, r2, pos, l3 + idx - l2, r3 - 1, inMap);
        }
    }

    public static int[] preInToPos1(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        int n = pre.length;
        // <val, idx>
        HashMap<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            inMap.put(in[i], i);
        }
        int[] pos = new int[n];
        process(pre, 0, n - 1, in, 0, n - 1, pos, 0, n - 1, inMap);
        return pos;
    }

}
