package giant.c1;

import java.util.Arrays;

/**
 * AOE（Area of Effect）范围攻击最少次数问题
 * 
 * 问题描述：
 * 在一条x轴上有若干怪兽，每个怪兽有自己的位置和血量。玩家可以释放AOE技能，
 * 每次AOE攻击可以选择一个起始位置left，攻击范围是[left, left+range]，
 * 范围内所有怪兽血量-1。求杀死所有怪兽的最少AOE次数。
 * 
 * 核心思想：
 * 1. 暴力递归：每次选择一个可以打击到活着怪兽的位置进行AOE，递归求解
 * 2. 贪心优化：从左到右处理每个怪兽，当遇到血量>0的怪兽时，从该位置开始AOE
 * 3. 线段树优化：使用线段树维护区间血量，支持区间修改和单点查询
 * 
 * 时间复杂度：
 * - minSure: O(k^n)，k为平均每次可选择的攻击位置数，n为怪兽数量
 * - min1: O(n^2)，每个位置最多被攻击其血量次数
 * - min2: O(n*log(n))，线段树优化的版本
 * 
 * 空间复杂度：O(n)
 */
public class AOE {
    /**
     * 检查从left位置开始的AOE攻击是否能击中活着的怪兽
     * @param x 怪兽位置数组（已排序）
     * @param hp 怪兽血量数组
     * @param left 攻击起始位置的索引
     * @param range AOE攻击范围
     * @return 如果攻击范围内有活着的怪兽则返回true
     */
    private static boolean hasHp(int[] x, int[] hp, int left, int range) {
        // 遍历攻击范围内的所有怪兽
        for (int i = left; i < x.length && x[i] - x[left] <= range; i++) {
            if (hp[i] > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 执行一次AOE攻击，对范围内所有怪兽造成1点伤害
     * @param x 怪兽位置数组
     * @param hp 怪兽血量数组
     * @param left 攻击起始位置的索引
     * @param range AOE攻击范围
     */
    private static void minusHp(int[] x, int[] hp, int left, int range) {
        // 对攻击范围内的所有怪兽减血
        for (int i = left; i < x.length && x[i] - x[left] <= range; i++) {
            hp[i]--;
        }
    }

    /**
     * 恢复一次AOE攻击的效果（用于递归回溯）
     * @param x 怪兽位置数组
     * @param hp 怪兽血量数组
     * @param left 攻击起始位置的索引
     * @param range AOE攻击范围
     */
    private static void addHp(int[] x, int[] hp, int left, int range) {
        // 恢复攻击范围内所有怪兽的血量（回溯）
        for (int i = left; i < x.length && x[i] - x[left] <= range; i++) {
            hp[i]++;
        }
    }

    /**
     * 暴力递归解法：尝试所有可能的AOE攻击位置
     * 递归思路：如果还有怪兽存活，尝试每个可能的攻击位置，选择最优解
     * @param x 怪兽位置数组（已排序）
     * @param hp 怪兽血量数组
     * @param range AOE攻击范围
     * @return 杀死所有怪兽的最少AOE次数
     */
    public static int minSure(int[] x, int[] hp, int range) {
        // 检查是否所有怪兽都已死亡
        boolean clear = true;
        for (int i = 0; i < hp.length; i++) {
            if (hp[i] > 0) {
                clear = false;
                break;
            }
        }
        if (clear) {
            return 0; // 基础情况：所有怪兽都死了
        }
        
        int ans = Integer.MAX_VALUE;
        // 尝试每个可能的攻击起始位置
        for (int left = 0; left < x.length; left++) {
            if (hasHp(x, hp, left, range)) { // 该位置攻击能击中活着的怪兽
                minusHp(x, hp, left, range);  // 执行攻击
                ans = Math.min(ans, 1 + minSure(x, hp, range)); // 递归求解剩余问题
                addHp(x, hp, left, range);    // 回溯恢复状态
            }
        }
        return ans;
    }

    /**
     * 贪心算法优化解法：从左到右处理每个怪兽
     * 核心思想：当遇到血量>0的怪兽时，从该位置开始进行AOE攻击，直到该怪兽死亡
     * 贪心策略的正确性：由于怪兽位置有序，从最左边的活着怪兽开始攻击是最优的
     * 
     * @param x 怪兽位置数组（已排序）
     * @param hp 怪兽血量数组
     * @param range AOE攻击范围
     * @return 杀死所有怪兽的最少AOE次数
     */
    public static int min1(int[] x, int[] hp, int range) {
        int n = x.length;
        
        // 预计算每个位置i作为攻击起点时能覆盖到的最远位置
        int[] cover = new int[n];
        int r = 0;
        for (int i = 0; i < n; i++) {
            // 使用双指针技巧，r指针只增不减，总复杂度O(n)
            while (r < n && x[r] - x[i] <= range) {
                r++;
            }
            cover[i] = r; // cover[i]表示从位置i攻击时，第一个覆盖不到的位置索引
        }
        
        int ans = 0;
        // 从左到右遍历每个怪兽
        for (int i = 0; i < n; i++) {
            if (hp[i] > 0) { // 如果当前怪兽还活着
                int minus = hp[i]; // 需要攻击的次数等于当前怪兽的血量
                // 从位置i开始攻击minus次，影响范围[i, cover[i])
                for (int ii = i; ii < cover[i]; ii++) {
                    hp[ii] -= minus;
                }
                ans += minus;
            }
        }
        return ans;
    }

    /**
     * 线段树实现：支持区间修改和单点查询
     * 用于优化min2方法中的区间血量更新操作
     */
    private static class SegmentTree {
        private int MAXN;   // 数组大小
        private int[] arr;  // 原始数组（1-indexed）
        private int[] sum;  // 线段树节点和
        private int[] lazy; // 懒标记数组

        /**
         * 线段树构造函数
         * @param origin 原始数组（0-indexed）
         */
        public SegmentTree(int[] origin) {
            MAXN = origin.length + 1;
            arr = new int[MAXN];
            // 将原始数组转为1-indexed
            for (int i = 1; i < MAXN; i++) {
                arr[i] = origin[i - 1];
            }
            sum = new int[MAXN << 2];  // 线段树需萂4倍空间
            lazy = new int[MAXN << 2]; // 懒标记数组
        }

        /**
         * 向上更新：用子节点的值更新父节点
         * @param rt 当前节点编号
         */
        private void pushUp(int rt) {
            sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        }

        /**
         * 向下传播懒标记：将父节点的懒标记传播给子节点
         * @param rt 当前节点编号
         * @param ln 左子节点的区间长度
         * @param rn 右子节点的区间长度
         */
        private void pushDown(int rt, int ln, int rn) {
            if (lazy[rt] != 0) {
                // 将懒标记传播给子节点
                lazy[rt << 1] += lazy[rt];
                sum[rt << 1] += lazy[rt] * ln;
                lazy[rt << 1 | 1] += lazy[rt];
                sum[rt << 1 | 1] += lazy[rt] * rn;
                lazy[rt] = 0; // 清空当前节点的懒标记
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
                sum[rt] = arr[l]; // 叶子节点直接赋值
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1);           // 递归构建左子树
            build(mid + 1, r, rt << 1 | 1);   // 递归构建右子树
            pushUp(rt);                       // 用子节点更新父节点
        }

        /**
         * 区间更新：对区间[tl, tr]内的所有元素加上tc
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param tc 要加的值
         * @param l 当前节点表示的区间左边界
         * @param r 当前节点表示的区间右边界
         * @param rt 当前节点编号
         */
        public void add(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前节点表示的区间完全包含在目标区间内
                sum[rt] += tc * (r - l + 1);
                lazy[rt] += tc;
                return;
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid); // 先向下传播懒标记
            if (tl <= mid) {
                add(tl, tr, tc, l, mid, rt << 1);
            }
            if (tr > mid) {
                add(tl, tr, tc, mid + 1, r, rt << 1 | 1);
            }
            pushUp(rt); // 向上更新
        }

        /**
         * 区间查询：查询区间[tl, tr]内所有元素的和
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param l 当前节点表示的区间左边界
         * @param r 当前节点表示的区间右边界
         * @param rt 当前节点编号
         * @return 区间内所有元素的和
         */
        public int query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                return sum[rt]; // 当前节点表示的区间完全包含在目标区间内
            }
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid); // 先向下传播懒标记
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

    /**
     * 线段树优化版本：使用线段树优化区间更新操作
     * 相比min1方法，线段树版本在处理大范围攻击时性能更优
     * 
     * @param x 怪兽位置数组（已排序）
     * @param hp 怪兽血量数组
     * @param range AOE攻击范围
     * @return 杀死所有怪兽的最少AOE次数
     */
    public static int min2(int[] x, int[] hp, int range) {
        int n = x.length;
        
        // 预计算每个位置的攻击覆盖范围
        int[] cover = new int[n];
        int r = 0;
        for (int i = 0; i < n; i++) {
            while (r < n && x[r] - x[i] <= range) {
                r++;
            }
            cover[i] = r - 1; // cover[i]表示从位置i攻击时能覆盖到的最远位置索引
        }
        
        // 构建线段树来维护怪兽血量
        SegmentTree st = new SegmentTree(hp);
        st.build(1, n, 1);
        
        int ans = 0;
        // 从左到右处理每个怪兽（注意线段树使用1-indexed）
        for (int i = 1; i <= n; i++) {
            int leftHP = st.query(i, i, 1, n, 1); // 查询当前怪兽的剩余血量
            if (leftHP > 0) {
                ans += leftHP; // 攻击次数等于剩余血量
                // 对攻击范围内的所有怪兽减少血量
                st.add(i, cover[i - 1] + 1, -leftHP, 1, n, 1);
            }
        }
        return ans;
    }

    /**
     * 生成随机数组（用于测试）
     * @param len 数组长度
     * @param maxVal 数组元素的最大值
     * @return 随机数组
     */
    private static int[] randomArr(int len, int maxVal) {
        int[] ans = new int[len];
        for (int i = 0; i < len; i++) {
            ans[i] = (int) (maxVal * Math.random()) + 1;
        }
        return ans;
    }

    /**
     * 复制数组（用于测试）
     * @param arr 原数组
     * @return 原数组的副本
     */
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
