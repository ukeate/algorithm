package leetc;

/**
 * 区间查询三问题 (Range Query Three Problems)
 * 来源：美团面试题
 * 
 * 问题描述：
 * 给定一个数组arr，长度为N，设计一个数据结构支持以下三种高效查询：
 * 1) querySum(L,R)：查询arr[L...R]的累加和
 * 2) queryAim(L,R)：查询arr[L...R]的目标值，定义为：
 *    假设arr[L...R] = [a,b,c,d]，s = a+b+c+d
 *    目标值 = (s-a)² + (s-b)² + (s-c)² + (s-d)²
 * 3) queryMax(L,R)：查询arr[L...R]的最大值
 * 
 * 性能要求：
 * - 初始化时间复杂度：O(N×logN)
 * - 每次查询时间复杂度：O(logN)
 * - 下标从1开始计算
 * 
 * 示例：
 * arr = [1, 1, 2, 3]
 * querySum(1, 3) -> 4 (1+1+2)
 * queryAim(2, 4) -> 50 (计算过程见下方详解)
 * queryMax(1, 4) -> 3
 * 
 * 解法思路：
 * 1. 前缀和数组：支持O(1)区间求和查询
 * 2. 前缀平方和数组：支持目标值快速计算
 * 3. 线段树：支持O(logN)区间最大值查询
 * 
 * 目标值计算公式推导：
 * 对于区间[L,R]，设s为区间和，每个元素为arr[i]
 * 目标值 = Σ(s - arr[i])² = Σ(s² - 2s×arr[i] + arr[i]²)
 *        = (R-L+1)×s² - 2s×Σarr[i] + Σarr[i]²
 *        = (R-L+1)×s² - 2s² + Σarr[i]²
 *        = (R-L-1)×s² + Σarr[i]²
 * 
 * 时间复杂度：
 * - 初始化：O(N) + O(N×logN) = O(N×logN)
 * - 查询：O(1) + O(logN) = O(logN)
 * 
 * 空间复杂度：O(N) - 前缀和数组 + 线段树存储
 */
public class Query3Problems {
    /**
     * 线段树实现，支持区间最大值查询和单点更新
     */
    public static class SegmentTree {
        private int[] max;       // 存储每个节点的最大值
        private int[] change;    // 存储懒标记的更新值
        private boolean[] update; // 标记该节点是否有懒标记

        /**
         * 构造线段树
         * 
         * @param n 数组大小
         */
        public SegmentTree(int n) {
            max = new int[n << 2];    // 4倍空间保证足够
            change = new int[n << 2];
            update = new boolean[n << 2];
            // 初始化最大值为最小值
            for (int i = 0; i < max.length; i++) {
                max[i] = Integer.MIN_VALUE;
            }
        }

        /**
         * 向上更新：根据子节点更新父节点的最大值
         * 
         * @param rt 当前节点编号
         */
        private void pushUp(int rt) {
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }

        /**
         * 向下传递懒标记：将父节点的更新信息传递给子节点
         * 
         * @param rt 当前节点编号
         * @param ln 左子节点管理的范围大小
         * @param rn 右子节点管理的范围大小
         */
        private void pushDown(int rt, int ln, int rn) {
            if (update[rt]) {
                // 将懒标记传递给子节点
                update[rt << 1] = true;
                update[rt << 1 | 1] = true;
                change[rt << 1] = change[rt];
                change[rt << 1 | 1] = change[rt];
                max[rt << 1] = change[rt];
                max[rt << 1 | 1] = change[rt];
                update[rt] = false; // 清除当前节点的懒标记
            }
        }

        /**
         * 区间更新：将[tl, tr]范围内的所有值更新为tc
         * 
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param tc 更新的目标值
         * @param l 当前节点管理的左边界
         * @param r 当前节点管理的右边界
         * @param rt 当前节点编号
         */
        public void update(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前节点完全包含在目标区间内，直接更新
                update[rt] = true;
                change[rt] = tc;
                max[rt] = tc;
                return;
            }
            
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid); // 先下传懒标记
            
            // 递归更新左右子节点
            if (tl <= mid) {
                update(tl, tr, tc, l, mid, rt << 1);
            }
            if (tr > mid) {
                update(tl, tr, tc, mid + 1, r, rt << 1 | 1);
            }
            
            pushUp(rt); // 向上更新父节点
        }

        /**
         * 区间查询：查询[tl, tr]范围内的最大值
         * 
         * @param tl 查询区间左边界
         * @param tr 查询区间右边界
         * @param l 当前节点管理的左边界
         * @param r 当前节点管理的右边界
         * @param rt 当前节点编号
         * @return 区间内的最大值
         */
        public int query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前节点完全包含在查询区间内，直接返回
                return max[rt];
            }
            
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid); // 先下传懒标记
            
            int left = Integer.MIN_VALUE, right = Integer.MIN_VALUE;
            
            // 递归查询左右子节点
            if (tl <= mid) {
                left = query(tl, tr, l, mid, rt << 1);
            }
            if (tr > mid) {
                right = query(tl, tr, mid + 1, r, rt << 1 | 1);
            }
            
            return Math.max(left, right);
        }
    }

    /**
     * 查询类，整合前缀和、前缀平方和和线段树实现三种查询
     */
    public static class Query {
        public int[] sum1;      // 前缀和数组
        public int[] sum2;      // 前缀平方和数组
        public SegmentTree st;  // 线段树，用于区间最大值查询
        public int m;           // 数组大小+1（为了支持1-based索引）

        /**
         * 构造查询结构
         * 
         * @param arr 输入数组
         */
        public Query(int[] arr) {
            int n = arr.length;
            m = arr.length + 1;
            
            // 初始化前缀和数组（1-based索引）
            sum1 = new int[m];
            sum2 = new int[m];
            st = new SegmentTree(m);
            
            // 构建前缀和、前缀平方和并初始化线段树
            for (int i = 0; i < n; i++) {
                sum1[i + 1] = sum1[i] + arr[i];                    // 前缀和
                sum2[i + 1] = sum2[i] + arr[i] * arr[i];           // 前缀平方和
                st.update(i + 1, i + 1, arr[i], 1, m, 1);         // 线段树单点更新
            }
        }

        /**
         * 查询区间[l, r]的累加和
         * 
         * @param l 左边界（1-based）
         * @param r 右边界（1-based）
         * @return 区间累加和
         */
        public int querySum(int l, int r) {
            return sum1[r] - sum1[l - 1]; // 利用前缀和O(1)计算
        }

        /**
         * 查询区间[l, r]的最大值
         * 
         * @param l 左边界（1-based）
         * @param r 右边界（1-based）
         * @return 区间最大值
         */
        public int queryMax(int l, int r) {
            return st.query(l, r, 1, m, 1); // 线段树O(logN)查询
        }

        /**
         * 查询区间[l, r]的目标值
         * 目标值 = Σ(s - arr[i])²，其中s为区间和
         * 
         * 公式推导结果：(R-L-1) × s² + Σarr[i]²
         * 
         * @param l 左边界（1-based）
         * @param r 右边界（1-based）
         * @return 目标值
         */
        public int queryAim(int l, int r) {
            int s = querySum(l, r);                    // 区间和
            int sumPower2 = s * s;                     // s²
            int elementsSquareSum = sum2[r] - sum2[l - 1]; // 区间平方和
            
            // 根据公式：(R-L-1) × s² + Σarr[i]²
            return (r - l - 1) * sumPower2 + elementsSquareSum;
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
