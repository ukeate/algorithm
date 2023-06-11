package base.tree3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class FallSquares {
    public static class SegmentTree {
        private int[] max;
        private int[] change;
        private boolean[] update;

        public SegmentTree(int size) {
            int n = size + 1;
            max = new int[n << 2];
            change = new int[n << 2];
            update = new boolean[n << 2];
        }

        private void pushUp(int rt) {
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }

        private void pushDown(int rt, int ln, int rn) {
            int l = rt << 1, r = rt << 1 | 1;
            if (update[rt]) {
                update[l] = true;
                update[r] = true;
                change[l] = change[rt];
                change[r] = change[rt];
                max[l] = change[rt];
                max[r] = change[rt];
                update[rt] = false;
            }
        }

        public void update(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                update[rt] = true;
                change[rt] = tc;
                max[rt] = tc;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            if (tl <= mid) {
                update(tl, tr, tc, l, mid, rt << 1);
            }
            if (tr > mid) {
                update(tl, tr, tc, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public int query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                return max[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            int lm = 0;
            int rm = 0;
            if (tl <= mid) {
                lm = query(tl, tr, l, mid, rt << 1);
            }
            if (tr > mid) {
                rm = query(tl, tr, mid + 1, r, rt << 1 | 1);
            }
            return Math.max(lm, rm);
        }
    }

    private static HashMap<Integer, Integer> prepare(int[][] positions) {
        TreeSet<Integer> pos = new TreeSet<>();
        for (int[] arr : positions) {
            pos.add(arr[0]);
            pos.add(arr[0] + arr[1] - 1);
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        int count = 0;
        for (Integer i : pos) {
            map.put(i, ++count);
        }
        return map;
    }

    public static List<Integer> fallSquares(int[][] positions) {
        HashMap<Integer, Integer> map = prepare(positions);
        int n = map.size();
        SegmentTree seg = new SegmentTree(n);
        int max = 0;
        List<Integer> res = new ArrayList<>();
        for (int[] arr : positions) {
            int tl = map.get(arr[0]);
            int tr = map.get(arr[0] + arr[1] - 1);
            int height = seg.query(tl, tr, 1, n, 1) + arr[1];
            max = Math.max(max, height);
            res.add(max);
            seg.update(tl, tr, height, 1, n, 1);
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] pos = new int[][]{{1, 2}, {2, 1}, {9, 1}};
        System.out.println(fallSquares(pos));
    }

}
