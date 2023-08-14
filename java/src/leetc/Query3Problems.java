package leetc;

// 来自美团
// 给定一个数组arr，长度为N，做出一个结构，可以高效的做如下的查询
// 1) int querySum(L,R) : 查询arr[L...R]上的累加和
// 2) int queryAim(L,R) : 查询arr[L...R]上的目标值，目标值定义如下：
//        假设arr[L...R]上的值为[a,b,c,d]，a+b+c+d = s
//        目标值为 : (s-a)^2 + (s-b)^2 + (s-c)^2 + (s-d)^2
// 3) int queryMax(L,R) : 查询arr[L...R]上的最大值
// 要求：
// 1) 初始化该结构的时间复杂度不能超过O(N*logN)
// 2) 三个查询的时间复杂度不能超过O(logN)
// 3) 查询时，认为arr的下标从1开始，比如 :
//    arr = [ 1, 1, 2, 3 ];
//    querySum(1, 3) -> 4
//    queryAim(2, 4) -> 50
//    queryMax(1, 4) -> 3
public class Query3Problems {
    public static class SegmentTree {
        private int[] max;
        private int[] change;
        private boolean[] update;

        public SegmentTree(int n) {
            max = new int[n << 2];
            change = new int[n << 2];
            update = new boolean[n << 2];
            for (int i = 0; i < max.length; i++) {
                max[i] = Integer.MIN_VALUE;
            }
        }

        private void pushUp(int rt) {
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }

        private void pushDown(int rt, int ln, int rn) {
            if (update[rt]) {
                update[rt << 1] = true;
                update[rt << 1 | 1] = true;
                change[rt << 1] = change[rt];
                change[rt << 1 | 1] = change[rt];
                max[rt << 1] = change[rt];
                max[rt << 1 | 1] = change[rt];
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
            pushDown(r, mid - l + 1, r - mid);
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
            int left = 0, right = 0;
            if (tl <= mid) {
                left = query(tl, tr, l, mid, rt << 1);
            }
            if (tr > mid) {
                right = query(tl, tr, mid + 1, r, rt << 1 | 1);
            }
            return Math.max(left, right);
        }
    }

    public static class Query {
        public int[] sum1;
        public int[] sum2;
        public SegmentTree st;
        public int m;

        public Query(int[] arr) {
            int n = arr.length;
            m = arr.length + 1;
            sum1 = new int[m];
            sum2 = new int[m];
            st = new SegmentTree(m);
            for (int i = 0; i < n; i++) {
                sum1[i + 1] = sum1[i] + arr[i];
                sum2[i + 1] = sum2[i] + arr[i] * arr[i];
                st.update(i + 1, i + 1, arr[i], 1, m, 1);
            }
        }

        public int querySum(int l, int r) {
            return sum1[r] - sum1[l - 1];
        }

        public int queryMax(int l, int r) {
            return st.query(l, r, 1, m, 1);
        }

        public int queryAim(int l, int r) {
            int sumPower2 = querySum(l, r);
            sumPower2 *= sumPower2;
            return sum2[r] - sum2[l - 1] + (r - l - 1) * sumPower2;
        }

    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 2, 3};
        Query q = new Query(arr);
        System.out.println(q.querySum(1, 3));
        System.out.println(q.queryMax(1, 4));
        System.out.println(q.queryAim(2, 4));
    }
}
