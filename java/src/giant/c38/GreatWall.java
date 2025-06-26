package giant.c38;

/**
 * 长城守卫军战斗力优化问题
 * 
 * 问题描述：
 * 长城上有n个烽火台排成一排，每个烽火台有ai个士兵驻守。
 * 有m位将军，每位将军可以驻守一个烽火台，可以影响距离≤x的所有烽火台。
 * 每个烽火台的战斗力 = 士兵数 + 能影响此烽火台的将军数 * k
 * 长城的战斗力 = 所有烽火台战斗力的最小值
 * 
 * 问题目标：
 * 求长城的最大战斗力
 * 
 * 解题思路：
 * 使用二分搜索 + 贪心策略 + 线段树优化：
 * 1. 二分搜索最终答案（长城的最大战斗力）
 * 2. 对于每个候选答案，使用贪心策略验证是否可达
 * 3. 贪心策略：从左到右扫描，当烽火台战斗力不足时，尽可能靠右放置将军
 * 4. 使用线段树维护区间加法操作，高效更新将军影响范围
 * 
 * 算法核心：
 * - 二分搜索：在[0, maxWall + m*k]范围内搜索答案
 * - 贪心验证：对于目标值limit，检查是否能用m个将军达到
 * - 线段树：支持区间加法和单点查询，维护将军的影响效果
 * 
 * 时间复杂度：O(n * log(max_val) * log n)
 * 空间复杂度：O(n)
 * 
 * 来源：360笔试题
 * 
 * @author Zhu Runqi
 */
public class GreatWall {
    
    /**
     * 线段树实现（支持区间修改和查询）
     * 功能：区间加法、区间覆盖、单点查询
     */
    private static class SegmentTree {
        private int MAXN;           // 数组最大长度
        private int[] arr;          // 原始数组
        private int[] sum;          // 线段树节点和
        private int[] lazy;         // 懒惰标记（加法）
        private int[] change;       // 覆盖值
        private boolean[] update;   // 覆盖标记

        /**
         * 构造线段树
         * 
         * @param origin 原始数组（0-indexed），线段树内部转为1-indexed
         */
        public SegmentTree(int[] origin) {
            MAXN = origin.length + 1;
            arr = new int[MAXN];
            // 将0-indexed数组转为1-indexed
            for (int i = 1; i < MAXN; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[MAXN << 2];      // 4倍空间
            lazy = new int[MAXN << 2];     // 懒惰加法标记
            change = new int[MAXN << 2];   // 覆盖值
            update = new boolean[MAXN << 2]; // 覆盖标记
        }

        /**
         * 向上更新：子节点信息合并到父节点
         */
        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        /**
         * 向下传递：将父节点的懒惰标记传递给子节点
         * 
         * @param rt 当前节点
         * @param ln 左子树节点个数
         * @param rn 右子树节点个数
         */
        private void pushDown(int rt, int ln, int rn) {
            // 处理覆盖标记
            if (update[rt]) {
                update[rt << 1] = true;
                update[rt << 1 | 1] = true;
                change[rt << 1] = change[rt];
                change[rt << 1 | 1] = change[rt];
                lazy[rt << 1] = 0;          // 覆盖后清空加法标记
                lazy[rt << 1 | 1] = 0;
                sum[rt << 1] = change[rt] * ln;
                sum[rt << 1 | 1] = change[rt] * rn;
                update[rt] = false;
            }
            
            // 处理加法标记
            if (lazy[rt] != 0) {
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0;
            }
        }

        /**
         * 构建线段树
         * 
         * @param l 当前区间左端点
         * @param r 当前区间右端点  
         * @param rt 当前节点编号
         */
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

        /**
         * 区间覆盖更新
         * 
         * @param tl 目标区间左端点
         * @param tr 目标区间右端点
         * @param tc 覆盖值
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param rt 当前节点编号
         */
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

        /**
         * 区间加法更新
         * 
         * @param tl 目标区间左端点
         * @param tr 目标区间右端点
         * @param tc 加法值
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param rt 当前节点编号
         */
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

        /**
         * 区间查询
         * 
         * @param tl 目标区间左端点
         * @param tr 目标区间右端点
         * @param l 当前区间左端点
         * @param r 当前区间右端点
         * @param rt 当前节点编号
         * @return 区间和
         */
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

    /**
     * 验证是否能用m个将军使长城战斗力达到limit
     * 
     * 贪心策略：
     * 1. 从左到右扫描每个烽火台
     * 2. 如果当前烽火台战斗力不足，计算需要多少将军
     * 3. 将这些将军尽可能靠右放置（贪心），以影响更多后续烽火台
     * 4. 使用线段树维护将军的影响效果
     * 
     * @param wall 烽火台士兵数组
     * @param m 将军总数
     * @param x 将军影响范围
     * @param k 每个将军提供的战斗力加成
     * @param limit 目标战斗力
     * @return 是否可以达到目标战斗力
     */
    private static boolean can(int[] wall, int m, int x, int k, long limit) {
        int n = wall.length;
        SegmentTree st = new SegmentTree(wall);
        st.build(1, n, 1);
        
        int need = 0;  // 已使用的将军数
        
        // 从左到右贪心放置将军
        for (int i = 0; i < n; i++) {
            // 查询当前烽火台的战斗力
            long cur = st.query(i + 1, i + 1, 1, n, 1);
            
            if (cur < limit) {
                // 计算需要多少个将军才能达到目标战斗力
                int add = (int) ((limit - cur + k - 1) / k);  // 向上取整
                need += add;
                
                if (need > m) {
                    return false;  // 将军不够
                }
                
                // 贪心策略：将将军放在能影响当前烽火台的最右边位置
                // 这样可以影响更多后续的烽火台
                int rightMost = Math.min(i + x, n - 1);  // 能影响位置i的最右将军位置
                
                // 更新线段树：在将军影响范围内增加战斗力
                // 将军在rightMost位置，影响范围为[rightMost-x, rightMost+x]
                int left = Math.max(rightMost - x, 0) + 1;   // 转为1-indexed
                int right = Math.min(rightMost + x, n - 1) + 1;
                st.add(left, right, add * k, 1, n, 1);
            }
        }
        
        return true;
    }

    /**
     * 求长城的最大战斗力
     * 
     * 使用二分搜索在可能的答案范围内查找最大值
     * 
     * @param wall 烽火台士兵数组
     * @param m 将军总数
     * @param x 将军影响范围
     * @param k 每个将军提供的战斗力加成
     * @return 长城的最大战斗力
     */
    public static int max(int[] wall, int m, int x, int k) {
        long l = 0, r = 0;
        
        // 确定二分搜索的上界
        for (int num : wall) {
            r = Math.max(r, num);
        }
        r += (long) m * k;  // 理论最大值：最强烽火台 + 所有将军加成
        
        long ans = 0;
        
        // 二分搜索最大可达到的战斗力
        while (l <= r) {
            long mid = (l + r) / 2;
            if (can(wall, m, x, k, mid)) {
                ans = mid;    // 可以达到，尝试更大的值
                l = mid + 1;
            } else {
                r = mid - 1;  // 无法达到，尝试更小的值
            }
        }
        
        return (int) ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 长城守卫军战斗力优化问题 ===\n");
        
        // 测试用例1：题目示例
        System.out.println("测试用例1：题目示例");
        int[] wall1 = {4, 4, 2, 4, 4};
        int m1 = 2, x1 = 1, k1 = 2;
        int result1 = max(wall1, m1, x1, k1);
        System.out.println("烽火台士兵: " + java.util.Arrays.toString(wall1));
        System.out.println("将军数: " + m1 + ", 影响范围: " + x1 + ", 加成: " + k1);
        System.out.println("最大战斗力: " + result1 + " (期望: 6)");
        System.out.println("策略分析: 将军放在位置1和3，分别影响(0,1,2)和(2,3,4)");
        System.out.println("战斗力变化: [4+2, 4+2, 2+4, 4+2, 4+2] = [6, 6, 6, 6, 6]");
        System.out.println();
        
        // 测试用例2：简单情况
        System.out.println("测试用例2：简单线性情况");
        int[] wall2 = {1, 1, 1, 1, 1};
        int m2 = 1, x2 = 2, k2 = 3;
        int result2 = max(wall2, m2, x2, k2);
        System.out.println("烽火台士兵: " + java.util.Arrays.toString(wall2));
        System.out.println("将军数: " + m2 + ", 影响范围: " + x2 + ", 加成: " + k2);
        System.out.println("最大战斗力: " + result2);
        System.out.println("策略分析: 将军放在中间位置可以影响最多烽火台");
        System.out.println();
        
        // 测试用例3：无将军情况
        System.out.println("测试用例3：无将军情况");
        int[] wall3 = {3, 1, 5, 2, 4};
        int m3 = 0, x3 = 1, k3 = 2;
        int result3 = max(wall3, m3, x3, k3);
        System.out.println("烽火台士兵: " + java.util.Arrays.toString(wall3));
        System.out.println("将军数: " + m3);
        System.out.println("最大战斗力: " + result3 + " (期望: 1，最弱烽火台的战斗力)");
        System.out.println();
        
        // 测试用例4：大范围影响
        System.out.println("测试用例4：大范围影响");
        int[] wall4 = {1, 1, 1, 1, 1};
        int m4 = 2, x4 = 10, k4 = 1;
        int result4 = max(wall4, m4, x4, k4);
        System.out.println("烽火台士兵: " + java.util.Arrays.toString(wall4));
        System.out.println("将军数: " + m4 + ", 影响范围: " + x4 + ", 加成: " + k4);
        System.out.println("最大战斗力: " + result4);
        System.out.println("策略分析: 影响范围覆盖所有烽火台，每个台都能获得2个将军的加成");
        System.out.println();
        
        System.out.println("=== 算法原理解析 ===");
        System.out.println("1. 问题建模：");
        System.out.println("   - 目标：最大化所有烽火台战斗力的最小值");
        System.out.println("   - 约束：将军数量有限，每个将军有影响范围");
        System.out.println("   - 策略：最优分配将军位置");
        System.out.println();
        System.out.println("2. 算法设计：");
        System.out.println("   - 二分搜索：在答案空间中查找最大可行值");
        System.out.println("   - 贪心验证：对于每个候选答案，贪心分配将军");
        System.out.println("   - 线段树优化：高效维护区间加法操作");
        System.out.println();
        System.out.println("3. 贪心策略：");
        System.out.println("   - 从左到右扫描，发现不足时立即补充");
        System.out.println("   - 将军尽可能靠右放置，影响更多后续位置");
        System.out.println("   - 这样的贪心策略是最优的");
        System.out.println();
        System.out.println("4. 复杂度分析：");
        System.out.println("   - 时间：O(n * log(max_val) * log n)");
        System.out.println("   - 空间：O(n)");
        System.out.println("   - 二分搜索 × 线性验证 × 线段树操作");
        System.out.println();
        System.out.println("5. 实际应用：");
        System.out.println("   - 网络覆盖：基站布置优化问题");
        System.out.println("   - 资源分配：有限资源的最优配置");
        System.out.println("   - 防御系统：防御设施的战略部署");
    }
}
