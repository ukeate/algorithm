package base.tree3;

// 下标从1开始
public class SimpleSegmentTree {
    public static class SegmentTree {
        private int MAXN;
        // 从1开始
        private int[] arr;
        // 累加和
        private int[] sum;
        // add值
        private int[] lazy;
        // 是否update
        private boolean[] update;
        // update值
        private int[] change;

        public SegmentTree(int[] origin) {
            MAXN = origin.length + 1;
            arr = new int[MAXN];
            for (int i = 1; i < MAXN; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[MAXN << 2];
            lazy = new int[MAXN << 2];
            change = new int[MAXN << 2];
            update = new boolean[MAXN << 2];
        }

        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        private void pushDown(int rt, int ln, int rn) {
            int l = rt << 1, r = rt << 1 | 1;
            if (update[rt]) {
                update[l] = true;
                update[r] = true;
                change[l] = change[rt];
                change[r] = change[rt];
                lazy[l] = 0;
                lazy[r] = 0;
                sum[l] = change[rt] * ln;
                sum[r] = change[rt] * rn;
                update[rt] = false;
            }
            if (lazy[rt] != 0) {
                lazy[l] += lazy[rt];
                sum[l] += lazy[rt] * ln;
                lazy[r] += lazy[rt];
                sum[r] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }

        public void build(int l, int r, int rt) {
            if (l == r) {
                sum[rt] = arr[l];
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1);
            build(mid + 1, r, rt << 1 | 1);
            pushUp(rt);
        }

        public void update(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                update[rt] = true;
                change[rt] = tc;
                sum[rt] = tc * (r - l + 1);
                lazy[rt] = 0;
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

        public void add(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                sum[rt] += tc * (r - l + 1);
                lazy[rt] += tc;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            if (tl <= mid) {
                add(tl, tr, tc, l, mid, rt << 1);
            }
            if (tr > mid) {
                add(tl, tr, tc, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);
        }

        public long query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            long ans = 0;
            if (tl <= mid) {
                ans += query(tl, tr, l, mid, rt << 1);
            }
            if (tr > mid) {
                ans += query(tl, tr, mid + 1, r, rt << 1 | 1);
            }
            return ans;
        }
    }

    //

    public static class ArrSure {
        public int[] arr;

        public ArrSure(int[] origin) {
            arr = new int[origin.length + 1];
            for (int i = 0; i < origin.length; i++) {
                arr[i + 1] = origin[i];
            }
        }

        public void update(int tl, int tr, int tc) {
            for (int i = tl; i <= tr; i++) {
                arr[i] = tc;
            }
        }

        public void add(int tl, int tr, int tc) {
            for (int i = tl; i <= tr; i++) {
                arr[i] += tc;
            }
        }

        public long query(int tl, int tr) {
            long ans = 0;
            for (int i = tl; i <= tr; i++) {
                ans += arr[i];
            }
            return ans;
        }
    }

    //

    private static int[] randomArr(int maxLen, int maxVal) {
        int[] origin = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < origin.length; i++) {
            origin[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return origin;
    }

    public static boolean test() {
        int times = 5000;
        int maxLen = 100;
        int maxVal = 1000;
        int addOrUpdateTimes = 1000;
        int queryTimes = 500;
        for (int i = 0; i < times; i++) {
            int[] origin = randomArr(maxLen, maxVal);
            SegmentTree seg = new SegmentTree(origin);
            int start = 1;
            int n = origin.length;
            int root = 1;
            seg.build(start, n, root);
            ArrSure sure = new ArrSure(origin);
            for (int j = 0; j < addOrUpdateTimes; j++) {
                int num1 = (int) (n * Math.random()) + 1;
                int num2 = (int) (n * Math.random()) + 1;
                int tl = Math.min(num1, num2);
                int tr = Math.max(num1, num2);
                int tc = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
                if (Math.random() < 0.5) {
                    seg.add(tl, tr, tc, start, n, root);
                    sure.add(tl, tr, tc);
                } else {
                    seg.update(tl, tr, tc, start, n, root);
                    sure.update(tl, tr, tc);
                }
            }
            for (int k = 0; k < queryTimes; k++) {
                int num1 = (int) (n * Math.random()) + 1;
                int num2 = (int) (n * Math.random()) + 1;
                int tl = Math.min(num1, num2);
                int tr = Math.max(num1, num2);
                long ans1 = seg.query(tl, tr, start, n, root);
                long ans2 = sure.query(tl, tr);
                if (ans1 != ans2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] origin = {2, 1, 1, 2, 3, 4, 5};
        SegmentTree seg = new SegmentTree(origin);
        int start = 1;
        int n = origin.length;
        int root = 1;
        int tl = 2;
        int tr = 5;
        int tc = 4;
        seg.build(start, n, root);
        seg.add(tl, tr, tc, start, n, root);
        long sum = seg.query(tl, tr, start, n, root);
        System.out.println(sum);
        seg.update(tl, tr, tc, start, n, root);
        sum = seg.query(tl, tr, start, n, root);
        System.out.println(sum);
        System.out.println("test begin");
        if (!test()) {
            System.out.println("Wrong");
        }
        System.out.println("test end");
    }
}
