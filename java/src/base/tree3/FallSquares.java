package base.tree3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * 落下的方块问题
 * 给定一系列方块的位置和边长，方块会依次落下，求每次落下后的最大高度
 * 使用线段树解决，支持区间更新和区间查询
 */
public class FallSquares {
    /**
     * 线段树实现，支持区间更新（设置值）和区间查询（最大值）
     */
    public static class SegmentTree {
        private int[] max;       // 存储区间最大值
        private int[] change;    // 延迟更新数组
        private boolean[] update; // 标记是否有更新操作

        /**
         * 构造线段树
         * @param size 数组大小
         */
        public SegmentTree(int size) {
            int n = size + 1;
            max = new int[n << 2];      // 4倍空间
            change = new int[n << 2];
            update = new boolean[n << 2];
        }

        /**
         * 向上更新，子节点信息合并到父节点
         * @param rt 当前节点编号
         */
        private void pushUp(int rt) {
            max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
        }

        /**
         * 向下推送延迟更新
         * @param rt 当前节点编号
         * @param ln 左子树节点数量
         * @param rn 右子树节点数量
         */
        private void pushDown(int rt, int ln, int rn) {
            int l = rt << 1, r = rt << 1 | 1;
            if (update[rt]) {
                // 将更新操作推送到子节点
                update[l] = true;
                update[r] = true;
                change[l] = change[rt];
                change[r] = change[rt];
                max[l] = change[rt];
                max[r] = change[rt];
                update[rt] = false;
            }
        }

        /**
         * 区间更新：将[tl,tr]范围内的所有值设置为tc
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param tc 要设置的值
         * @param l 当前区间左边界
         * @param r 当前区间右边界
         * @param rt 当前节点编号
         */
        public void update(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 当前区间完全被目标区间包含
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

        /**
         * 区间查询：查询[tl,tr]范围内的最大值
         * @param tl 目标区间左边界
         * @param tr 目标区间右边界
         * @param l 当前区间左边界
         * @param r 当前区间右边界
         * @param rt 当前节点编号
         * @return 区间最大值
         */
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

    /**
     * 坐标离散化处理
     * 将所有可能的坐标值映射到连续的整数区间
     * @param positions 方块位置数组
     * @return 坐标到离散化值的映射
     */
    private static HashMap<Integer, Integer> prepare(int[][] positions) {
        TreeSet<Integer> pos = new TreeSet<>();
        for (int[] arr : positions) {
            pos.add(arr[0]);                    // 左边界
            pos.add(arr[0] + arr[1] - 1);      // 右边界
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        int count = 0;
        for (Integer i : pos) {
            map.put(i, ++count);
        }
        return map;
    }

    /**
     * 解决落下方块问题
     * @param positions 方块数组，每个方块用[左边界, 边长]表示
     * @return 每次落下后的最大高度列表
     */
    public static List<Integer> fallSquares(int[][] positions) {
        HashMap<Integer, Integer> map = prepare(positions);
        int n = map.size();
        SegmentTree seg = new SegmentTree(n);
        int max = 0;
        List<Integer> res = new ArrayList<>();
        
        for (int[] arr : positions) {
            int tl = map.get(arr[0]);                    // 离散化后的左边界
            int tr = map.get(arr[0] + arr[1] - 1);      // 离散化后的右边界
            // 查询当前位置的最大高度，然后加上方块的边长
            int height = seg.query(tl, tr, 1, n, 1) + arr[1];
            max = Math.max(max, height);
            res.add(max);
            // 更新当前区间的高度
            seg.update(tl, tr, height, 1, n, 1);
        }
        return res;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例：三个方块分别在位置[1,2], [2,1], [9,1]落下
        int[][] pos = new int[][]{{1, 2}, {2, 1}, {9, 1}};
        System.out.println(fallSquares(pos));  // 输出每次落下后的最大高度
    }
}
