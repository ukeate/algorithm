package giant.c1;

import java.util.Arrays;

// x数组表示怪兽在x轴的位置, hp数组是血量，range是aoe范围，返回最少aoe次数
public class AOE {
    private static boolean hasHp(int[] x, int[] hp, int left, int range) {
        for (int i = left; i < x.length && x[i] - x[left] <= range; i++) {
            if (hp[i] > 0) {
                return true;
            }
        }
        return false;
    }

    private static void minusHp(int[] x, int[] hp, int left, int range) {
        for (int i = left; i < x.length && x[i] - x[left] <= range; i++) {
            hp[i]--;
        }
    }

    private static void addHp(int[] x, int[] hp, int left, int range) {
        for (int i = left; i < x.length && x[i] - x[left] <= range; i++) {
            hp[i]++;
        }
    }

    public static int minSure(int[] x, int[] hp, int range) {
        boolean clear = true;
        for (int i = 0; i < hp.length; i++) {
            if (hp[i] > 0) {
                clear = false;
                break;
            }
        }
        if (clear) {
            return 0;
        }
        int ans = Integer.MAX_VALUE;
        for (int left = 0; left < x.length; left++) {
            if (hasHp(x, hp, left, range)) {
                minusHp(x, hp, left, range);
                ans = Math.min(ans, 1 + minSure(x, hp, range));
                addHp(x, hp, left, range);
            }
        }
        return ans;
    }

    //

    public static int min1(int[] x, int[] hp, int range) {
        int n = x.length;
        // i第一个覆盖不到的位置
        int[] cover = new int[n];
        int r = 0;
        for (int i = 0; i < n; i++) {
            while (r < n && x[r] - x[i] <= range) {
                r++;
            }
            cover[i] = r;
        }
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (hp[i] > 0) {
                int minus = hp[i];
                for (int ii = i; ii < cover[i]; ii++) {
                    hp[ii] -= minus;
                }
                ans += minus;
            }
        }
        return ans;
    }

    //

    private static class SegmentTree {
        private int MAXN;
        private int[] arr;
        private int[] sum;
        private int[] lazy;

        public SegmentTree(int[] origin) {
            MAXN = origin.length + 1;
            arr = new int[MAXN];
            for (int i = 1; i < MAXN; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[MAXN << 2];
            lazy = new int[MAXN << 2];
        }

        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        private void pushDown(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
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

        public int query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            int ans = 0;
            if (tl <= mid) {
                ans += query(tl, tr, l, mid, rt << 1);
            }
            if (tr > mid) {
                ans += query(tl, tr, mid + 1, r, rt << 1 | 1);
            }
            return ans;
        }
    }

    public static int min2(int[] x, int[] hp, int range) {
        int n = x.length;
        int[] cover = new int[n];
        int r = 0;
        for (int i = 0; i < n; i++) {
            while (r < n && x[r] - x[i] <= range) {
                r++;
            }
            cover[i] = r - 1;
        }
        SegmentTree st = new SegmentTree(hp);
        st.build(1, n, 1);
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            int leftHP = st.query(i, i, 1, n, 1);
            if (leftHP > 0) {
                ans += leftHP;
                st.add(i, cover[i - 1] + 1, -leftHP, 1, n, 1);
            }
        }
        return ans;
    }

    //

    private static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) (maxVal * Math.random()) + 1;
        }
        return ans;
    }

    private static int[] copy(int[] arr) {
        int n = arr.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[i] = arr[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int times = 50000;
        int maxLen = 50;
        int maxX = 500;
        int maxHP = 60;
        int maxRange = 10;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            int len = (int)(maxLen * Math.random()) + 1;
            int[] x1 = randomArr(len, maxX);
            int[] x2 = copy(x1);
            int[] hp1 = randomArr(len, maxHP);
            int[] hp2 = copy(hp1);
            int range = (int) (maxRange * Math.random()) + 1;
            int ans1 = min1(x1, hp1, range);
            int ans2 = min2(x2, hp2, range);
            if (ans1 != ans2) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");

        int len = 500000;
        long start, end;
        int[] x1 = randomArr(len, len);
        Arrays.sort(x1);
        int[] x2 = copy(x1);
        int[] hp1 = new int[len];
        for (int i = 0; i < len; i++) {
            hp1[i] = i * 5 + 10;
        }
        int[] hp2 = copy(hp1);
        int range = 10000;

        start = System.currentTimeMillis();
        System.out.println(min1(x1, hp1, range));
        end = System.currentTimeMillis();
        System.out.println("min1 运行时间: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        System.out.println(min2(x2, hp2, range));
        end = System.currentTimeMillis();
        System.out.println("min2 运行时间: " + (end - start) + " ms");
    }
}
