package giant.c39;

/**
 * 矩阵跳跃游戏问题
 * 
 * 问题描述：
 * 给定一个二维数组matrix，matrix[i][j] = k代表：
 * - 从(i,j)位置可以随意往右跳≤k步，或者往下跳≤k步
 * - 如果matrix[i][j] = 0，代表来到(i,j)位置必须停止
 * 返回从matrix左上角到右下角的最少跳跃次数
 * 
 * 问题约束：
 * - matrix中行数n ≤ 5000, 列数m ≤ 5000
 * - matrix中的值 ≤ 5000
 * 
 * 解题思路：
 * 方法1：递归解法（直观但效率低）
 * - 从当前位置探索所有可能的跳跃
 * - 时间复杂度：O(k^(n+m))，会超时
 * 
 * 方法2：线段树优化的动态规划
 * - 使用线段树维护每行和每列的最小步数
 * - 从右下角开始逆向求解
 * - 时间复杂度：O(nm*log(max(n,m)))
 * 
 * 算法核心：
 * - 状态定义：dp[i][j]表示从(i,j)到右下角的最少步数
 * - 状态转移：选择所有可达位置中步数最少的
 * - 优化策略：使用线段树快速查询区间最小值
 * 
 * 时间复杂度：O(nm*log(max(n,m)))
 * 空间复杂度：O(nm)
 * 
 * 来源：京东面试题
 * 
 * @author Zhu Runqi
 */
public class JumpGameOnMatrix {
    
    /**
     * 递归解法：暴力搜索所有可能的跳跃路径
     * 
     * 算法思路：
     * 从当前位置出发，尝试所有可能的跳跃：
     * 1. 向右跳1到k步
     * 2. 向下跳1到k步
     * 3. 选择步数最少的路径
     * 
     * 注意：此方法时间复杂度过高，仅用于小规模测试
     * 
     * @param map 二维矩阵
     * @param row 当前行位置
     * @param col 当前列位置
     * @return 从当前位置到右下角的最少步数
     */
    private static int process(int[][] map, int row, int col) {
        // 到达目标位置
        if (row == map.length - 1 && col == map[0].length - 1) {
            return 0;
        }
        
        // 遇到0必须停止，无法继续
        if (map[row][col] == 0) {
            return Integer.MAX_VALUE;
        }
        
        int next = Integer.MAX_VALUE;
        
        // 尝试向下跳跃（1到k步）
        for (int down = row + 1; down < map.length && (down - row) <= map[row][col]; down++) {
            next = Math.min(next, process(map, down, col));
        }
        
        // 尝试向右跳跃（1到k步）
        for (int right = col + 1; right < map[0].length && (right - col) <= map[row][col]; right++) {
            next = Math.min(next, process(map, row, right));
        }
        
        return next != Integer.MAX_VALUE ? (next + 1) : next;
    }

    /**
     * 递归方法的入口函数
     * 
     * @param map 二维矩阵
     * @return 从左上角到右下角的最少跳跃次数
     */
    public static int jump1(int[][] map) {
        return process(map, 0, 0);
    }

    /**
     * 线段树数据结构，用于维护区间最小值
     * 
     * 功能：
     * - 支持区间更新
     * - 支持区间查询最小值
     * - 支持懒惰传播
     */
    private static class SegmentTree {
        private int[] min;      // 存储最小值
        private int[] change;   // 懒惰更新值
        private boolean[] update; // 是否需要更新
        private int size;       // 线段树大小

        /**
         * 构造线段树
         * 
         * @param size 数组大小
         */
        public SegmentTree(int size) {
            this.size = size;
            int n = size + 1;
            min = new int[n << 2];      // 4倍空间
            change = new int[n << 2];
            update = new boolean[n << 2];
            // 初始化为最大值
            update(1, size, Integer.MAX_VALUE);
        }

        /**
         * 向上更新节点信息
         * 
         * @param rt 当前节点
         */
        private void pushUp(int rt) {
            min[rt] = Math.min(min[rt << 1], min[rt << 1 | 1]);
        }

        /**
         * 向下推送懒惰标记
         * 
         * @param rt 当前节点
         * @param ln 左子树大小
         * @param rn 右子树大小
         */
        private void pushDown(int rt, int ln, int rn) {
            if (update[rt]) {
                // 推送到左右子树
                update[rt << 1] = true;
                update[rt << 1 | 1] = true;
                change[rt << 1] = change[rt];
                change[rt << 1 | 1] = change[rt];
                min[rt << 1] = change[rt];
                min[rt << 1 | 1] = change[rt];
                update[rt] = false;
            }
        }

        /**
         * 区间更新
         * 
         * @param tl 目标左边界
         * @param tr 目标右边界
         * @param tc 更新值
         */
        public void update(int tl, int tr, int tc) {
            update(tl, tr, tc, 1, size, 1);
        }

        /**
         * 区间更新的递归实现
         * 
         * @param tl 目标左边界
         * @param tr 目标右边界
         * @param tc 更新值
         * @param l 当前区间左边界
         * @param r 当前区间右边界
         * @param rt 当前节点
         */
        private void update(int tl, int tr, int tc, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                // 完全覆盖，懒惰更新
                update[rt] = true;
                change[rt] = tc;
                min[rt] = tc;
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
         * 区间查询最小值
         * 
         * @param tl 目标左边界
         * @param tr 目标右边界
         * @return 区间最小值
         */
        public int query(int tl, int tr) {
            return query(tl, tr, 1, size, 1);
        }

        /**
         * 区间查询的递归实现
         * 
         * @param tl 目标左边界
         * @param tr 目标右边界
         * @param l 当前区间左边界
         * @param r 当前区间右边界
         * @param rt 当前节点
         * @return 区间最小值
         */
        private int query(int tl, int tr, int l, int r, int rt) {
            if (tl <= l && r <= tr) {
                return min[rt];
            }
            
            int mid = (l + r) >> 1;
            pushDown(rt, mid - l + 1, r - mid);
            
            int left = Integer.MAX_VALUE;
            int right = Integer.MAX_VALUE;
            
            if (tl <= mid) {
                left = query(tl, tr, l, mid, rt << 1);
            }
            if (tr > mid) {
                right = query(tl, tr, mid + 1, r, rt << 1 | 1);
            }
            
            return Math.min(left, right);
        }
    }

    /**
     * 线段树优化的动态规划解法
     * 
     * 算法思路：
     * 1. 创建行线段树和列线段树
     * 2. 从右下角开始逆向填表
     * 3. 对每个位置，查询所有可达位置的最小步数
     * 4. 使用线段树快速维护和查询区间最小值
     * 
     * @param arr 二维矩阵
     * @return 从左上角到右下角的最少跳跃次数
     */
    public static int jump2(int[][] arr) {
        int n = arr.length;
        int m = arr[0].length;
        
        // 将数组索引从1开始，便于线段树操作
        int[][] map = new int[n + 1][m + 1];
        for (int a = 0, b = 1; a < n; a++, b++) {
            for (int c = 0, d = 1; c < m; c++, d++) {
                map[b][d] = arr[a][c];
            }
        }
        
        // 为每一行创建线段树（维护该行每个位置的最小步数）
        SegmentTree[] rowTrees = new SegmentTree[n + 1];
        for (int i = 1; i <= n; i++) {
            rowTrees[i] = new SegmentTree(m);
        }
        
        // 为每一列创建线段树（维护该列每个位置的最小步数）
        SegmentTree[] colTrees = new SegmentTree[m + 1];
        for (int i = 1; i <= m; i++) {
            colTrees[i] = new SegmentTree(n);
        }
        
        // 初始化目标位置（右下角）
        rowTrees[n].update(m, m, 0);
        colTrees[m].update(n, n, 0);
        
        // 处理最后一行（只能向右跳）
        for (int col = m - 1; col >= 1; col--) {
            if (map[n][col] != 0) {
                int left = col + 1;
                int right = Math.min(col + map[n][col], m);
                int next = rowTrees[n].query(left, right);
                if (next != Integer.MAX_VALUE) {
                    rowTrees[n].update(col, col, next + 1);
                    colTrees[col].update(n, n, next + 1);
                }
            }
        }
        
        // 处理最后一列（只能向下跳）
        for (int row = n - 1; row >= 1; row--) {
            if (map[row][m] != 0) {
                int up = row + 1;
                int down = Math.min(row + map[row][m], n);
                int next = colTrees[m].query(up, down);
                if (next != Integer.MAX_VALUE) {
                    rowTrees[row].update(m, m, next + 1);
                    colTrees[m].update(row, row, next + 1);
                }
            }
        }
        
        // 处理其他位置（可以向右或向下跳）
        for (int row = n - 1; row >= 1; row--) {
            for (int col = m - 1; col >= 1; col--) {
                if (map[row][col] != 0) {
                    // 查询向右跳跃的最优选择
                    int left = col + 1;
                    int right = Math.min(col + map[row][col], m);
                    int next1 = rowTrees[row].query(left, right);
                    
                    // 查询向下跳跃的最优选择
                    int up = row + 1;
                    int down = Math.min(row + map[row][col], n);
                    int next2 = colTrees[col].query(up, down);
                    
                    // 选择最优方案
                    int next = Math.min(next1, next2);
                    if (next != Integer.MAX_VALUE) {
                        rowTrees[row].update(col, col, next + 1);
                        colTrees[col].update(row, row, next + 1);
                    }
                }
            }
        }
        
        // 返回起始位置的最小步数
        return rowTrees[1].query(1, 1);
    }

    /**
     * 生成随机测试矩阵
     * 
     * @param n 行数
     * @param m 列数
     * @param v 最大值
     * @return 随机矩阵
     */
    private static int[][] randomMatrix(int n, int m, int v) {
        int[][] ans = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                ans[i][j] = (int) (Math.random() * v);
            }
        }
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 矩阵跳跃游戏问题 ===\n");
        
        // 测试用例1：简单矩阵
        System.out.println("测试用例1：简单矩阵");
        int[][] matrix1 = {
            {2, 3, 1, 1},
            {2, 1, 1, 1},
            {1, 1, 1, 1}
        };
        
        System.out.println("矩阵:");
        for (int[] row : matrix1) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        
        int result1 = jump2(matrix1);
        System.out.println("最少跳跃次数: " + result1);
        System.out.println("分析: 从(0,0)开始，可以选择多种路径到达右下角");
        System.out.println();
        
        // 测试用例2：包含障碍的矩阵
        System.out.println("测试用例2：包含障碍的矩阵");
        int[][] matrix2 = {
            {3, 2, 1},
            {1, 0, 1},
            {2, 1, 1}
        };
        
        System.out.println("矩阵:");
        for (int[] row : matrix2) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
        
        int result2 = jump2(matrix2);
        System.out.println("最少跳跃次数: " + result2);
        System.out.println("分析: 中间有障碍(0)，需要绕过障碍到达目标");
        System.out.println();
        
        // 测试用例3：单行矩阵
        System.out.println("测试用例3：单行矩阵");
        int[][] matrix3 = {{3, 2, 1, 4, 1}};
        
        System.out.println("矩阵:");
        for (int val : matrix3[0]) {
            System.out.print(val + " ");
        }
        System.out.println();
        
        int result3 = jump2(matrix3);
        System.out.println("最少跳跃次数: " + result3);
        System.out.println("分析: 单行情况，只能向右跳跃");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int times = 1000;
        int len = 10;
        int val = 8;
        
        System.out.println("开始对比测试...");
        boolean allPassed = true;
        
        for (int i = 0; i < times; i++) {
            int n = (int) (Math.random() * len) + 1;
            int m = (int) (Math.random() * len) + 1;
            int[][] map = randomMatrix(n, m, val);
            
            int ans1 = jump1(map);
            int ans2 = jump2(map);
            
            if (ans1 != ans2) {
                System.out.println("测试失败: 结果不一致");
                allPassed = false;
                break;
            }
        }
        
        if (allPassed) {
            System.out.println("所有测试通过！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 问题特征：");
        System.out.println("   - 只能向右或向下跳跃");
        System.out.println("   - 跳跃步数受当前位置值限制");
        System.out.println("   - 0值位置是障碍，必须停止");
        System.out.println();
        System.out.println("2. 递归解法：");
        System.out.println("   - 暴力枚举所有可能的跳跃方式");
        System.out.println("   - 时间复杂度过高，只适合小规模问题");
        System.out.println("   - 存在大量重复计算");
        System.out.println();
        System.out.println("3. 线段树优化：");
        System.out.println("   - 使用线段树维护每行每列的最小步数");
        System.out.println("   - 支持区间更新和区间查询");
        System.out.println("   - 大幅减少查询时间复杂度");
        System.out.println();
        System.out.println("4. 动态规划思想：");
        System.out.println("   - 从右下角开始逆向求解");
        System.out.println("   - 状态转移：选择所有可达位置中的最优解");
        System.out.println("   - 避免重复计算，提高效率");
        System.out.println();
        System.out.println("5. 复杂度分析：");
        System.out.println("   - 时间复杂度：O(nm*log(max(n,m)))");
        System.out.println("   - 空间复杂度：O(nm)");
        System.out.println("   - 适合处理大规模矩阵跳跃问题");
        System.out.println();
        System.out.println("6. 应用场景：");
        System.out.println("   - 游戏路径规划");
        System.out.println("   - 最短路径问题");
        System.out.println("   - 动态规划优化");
    }
}
