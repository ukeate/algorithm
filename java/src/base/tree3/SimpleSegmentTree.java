package base.tree3;

/**
 * 线段树（Segment Tree）实现
 * 支持区间查询（求和）、区间更新（设置值）、区间增加操作
 * 下标从1开始，使用延迟标记（Lazy Propagation）优化
 */
public class SimpleSegmentTree {
    /**
     * 线段树实现类
     */
    public static class SegmentTree {
        private int MAXN;           // 最大节点数
        private int[] arr;          // 原始数组，从1开始
        private int[] sum;          // 存储区间和
        private int[] lazy;         // 延迟标记：记录待传播的add值
        private boolean[] update;   // 是否有update操作的标记
        private int[] change;       // update操作的值

        /**
         * 构造函数：根据原始数组构建线段树
         * @param origin 原始数组
         */
        public SegmentTree(int[] origin) {
            MAXN = origin.length + 1;
            arr = new int[MAXN];
            // 将原始数组拷贝到arr中，下标从1开始
            for (int i = 1; i < MAXN; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[MAXN << 2];      // 4倍空间存储线段树
            lazy = new int[MAXN << 2];     // 延迟标记数组
            change = new int[MAXN << 2];   // update值数组
            update = new boolean[MAXN << 2]; // update标记数组
        }

        /**
         * 向上更新：用子节点信息更新父节点
         * @param rt 当前节点编号
         */
        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        /**
         * 向下传播延迟标记
         * @param rt 当前节点编号
         * @param ln 左子树节点数量
         * @param rn 右子树节点数量
         */
        private void pushDown(int rt, int ln, int rn) {
            int l = rt << 1, r = rt << 1 | 1;  // 左右子节点编号
            if (update[rt]) {
                // 如果有update操作，先处理update
                update[l] = true;
                update[r] = true;
                change[l] = change[rt];
                change[r] = change[rt];
                lazy[l] = 0;    // update会覆盖之前的add操作
                lazy[r] = 0;
                sum[l] = change[rt] * ln;
                sum[r] = change[rt] * rn;
                update[rt] = false;
            }
            if (lazy[rt] != 0) {
                // 处理add操作的延迟标记
                lazy[l] += lazy[rt];
                sum[l] += lazy[rt] * ln;
                lazy[r] += lazy[rt];
                sum[r] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }

        /**
         * 构建线段树
         * @param l 区间左边界
         * @param r 区间右边界
         * @param rt 当前节点编号
         */
        public void build(int l, int r, int rt) {
            if (l == r) {
                // 叶子节点，直接赋值
                sum[rt] = arr[l];
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1);          // 构建左子树
            build(mid + 1, r, rt << 1 | 1);  // 构建右子树
            pushUp(rt);                      // 向上更新
        }

        /**
         * 区间更新：将[tl,tr]区间内所有值设置为tc
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param tc 设置的值
         * @param l 当前区间左边界
         * @param r 当前区间右边界
         * @param rt 当前节点编号
         */
        public void update(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前区间完全被目标区间包含
                update[rt] = true;
                change[rt] = tc;
                sum[rt] = tc * (r - l + 1);
                lazy[rt] = 0;  // 清除之前的add标记
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);  // 向下传播标记
            if (tl <= mid) {
                update(tl, tr, tc, l, mid, rt << 1);
            }
            if (tr > mid) {
                update(tl, tr, tc, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);  // 向上更新
        }

        /**
         * 区间增加：将[tl,tr]区间内所有值增加tc
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param tc 增加的值
         * @param l 当前区间左边界
         * @param r 当前区间右边界
         * @param rt 当前节点编号
         */
        public void add(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前区间完全被目标区间包含
                sum[rt] += tc * (r - l + 1);
                lazy[rt] += tc;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);  // 向下传播标记
            if (tl <= mid) {
                add(tl, tr, tc, l, mid, rt << 1);
            }
            if (tr > mid) {
                add(tl, tr, tc, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt);  // 向上更新
        }

        /**
         * 区间查询：查询[tl,tr]区间的和
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param l 当前区间左边界
         * @param r 当前区间右边界
         * @param rt 当前节点编号
         * @return 区间和
         */
        public long query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前区间完全被目标区间包含
                return sum[rt];
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);  // 向下传播标记
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

    /**
     * 暴力实现类，用于验证线段树正确性
     */
    public static class ArrSure {
        public int[] arr;

        /**
         * 构造函数
         * @param origin 原始数组
         */
        public ArrSure(int[] origin) {
            arr = new int[origin.length + 1];
            for (int i = 0; i < origin.length; i++) {
                arr[i + 1] = origin[i];
            }
        }

        /**
         * 区间更新：设置值
         * @param tl 区间左边界
         * @param tr 区间右边界
         * @param tc 设置的值
         */
        public void update(int tl, int tr, int tc) {
            for (int i = tl; i <= tr; i++) {
                arr[i] = tc;
            }
        }

        /**
         * 区间增加
         * @param tl 区间左边界
         * @param tr 区间右边界
         * @param tc 增加的值
         */
        public void add(int tl, int tr, int tc) {
            for (int i = tl; i <= tr; i++) {
                arr[i] += tc;
            }
        }

        /**
         * 区间查询
         * @param tl 区间左边界
         * @param tr 区间右边界
         * @return 区间和
         */
        public long query(int tl, int tr) {
            long ans = 0;
            for (int i = tl; i <= tr; i++) {
                ans += arr[i];
            }
            return ans;
        }
    }

    /**
     * 生成随机数组用于测试
     * @param maxLen 最大长度
     * @param maxVal 最大值
     * @return 随机数组
     */
    private static int[] randomArr(int maxLen, int maxVal) {
        int[] origin = new int[(int) ((maxLen + 1) * Math.random()) + 1];
        for (int i = 0; i < origin.length; i++) {
            origin[i] = (int) ((maxVal + 1) * Math.random()) - (int) ((maxVal + 1) * Math.random());
        }
        return origin;
    }

    /**
     * 测试线段树正确性
     * @return 测试是否通过
     */
    public static boolean test() {
        int times = 5000;            // 测试轮数
        int maxLen = 100;            // 最大数组长度
        int maxVal = 1000;           // 最大值
        int addOrUpdateTimes = 1000; // 操作次数
        int queryTimes = 500;        // 查询次数
        
        for (int i = 0; i < times; i++) {
            int[] origin = randomArr(maxLen, maxVal);
            SegmentTree seg = new SegmentTree(origin);
            int start = 1;
            int n = origin.length;
            int root = 1;
            seg.build(start, n, root);
            ArrSure sure = new ArrSure(origin);
            
            // 随机执行add和update操作
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
            
            // 随机执行查询操作验证正确性
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

    /**
     * 主函数：演示线段树的使用
     */
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
        seg.add(tl, tr, tc, start, n, root);           // 区间增加
        long sum = seg.query(tl, tr, start, n, root);
        System.out.println(sum);
        
        seg.update(tl, tr, tc, start, n, root);        // 区间更新
        sum = seg.query(tl, tr, start, n, root);
        System.out.println(sum);
        
        System.out.println("test begin");
        if (!test()) {
            System.out.println("Wrong");
        }
        System.out.println("test end");
    }
}
