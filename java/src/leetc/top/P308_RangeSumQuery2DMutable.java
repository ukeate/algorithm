package leetc.top;

/**
 * LeetCode 308. 二维区域和检索 - 可变 (Range Sum Query 2D - Mutable)
 * 
 * 问题描述：
 * 给你一个二维矩阵 matrix，请你设计并实现一个数据结构来处理以下操作：
 * 1. update(row, col, val) - 将 matrix[row][col] 的值更新为 val
 * 2. sumRegion(row1, col1, row2, col2) - 返回矩阵中指定矩形区域元素的总和
 * 
 * 注意：矩形区域由左上角坐标 (row1, col1) 和右下角坐标 (row2, col2) 定义。
 * 
 * 示例：
 * NumMatrix numMatrix = new NumMatrix([[1,2,3],[4,5,6],[7,8,9]]);
 * numMatrix.sumRegion(0, 0, 1, 1); // 返回 12 (1+2+4+5)
 * numMatrix.update(1, 1, 10);      // 将 matrix[1][1] 更新为 10
 * numMatrix.sumRegion(0, 0, 1, 1); // 返回 17 (1+2+4+10)
 * 
 * 解法思路：
 * 使用二维树状数组(Binary Indexed Tree, BIT)：
 * 
 * 1. 数据结构特点：
 *    - 支持单点更新：O(log m × log n)
 *    - 支持区间查询：O(log m × log n)
 *    - 相比二维前缀和，更适合频繁更新的场景
 * 
 * 2. 核心操作：
 *    - add(i, j, val)：在位置(i,j)增加值val
 *    - sum(i, j)：计算从(0,0)到(i,j)的矩形区域和
 *    - update：先减去旧值，再加上新值
 *    - sumRegion：使用容斥原理计算任意矩形区域和
 * 
 * 3. 容斥原理：
 *    sum(r1,c1,r2,c2) = sum(r2,c2) - sum(r1-1,c2) - sum(r2,c1-1) + sum(r1-1,c1-1)
 * 
 * 核心思想：
 * - 二维树状数组的lowbit操作实现高效的区间更新和查询
 * - 通过容斥原理将任意矩形查询转化为四个前缀和的运算
 * - 在频繁更新的场景下性能优于朴素前缀和
 * 
 * 时间复杂度：
 * - update: O(log m × log n)
 * - sumRegion: O(log m × log n)
 * 
 * 空间复杂度：O(m × n) - 存储原矩阵和树状数组
 * 
 * LeetCode链接：https://leetcode.com/problems/range-sum-query-2d-mutable/
 */
public class P308_RangeSumQuery2DMutable {

    /**
     * 二维区域和检索数据结构
     */
    public static class NumMatrix {
        private int[][] tree;    // 二维树状数组
        private int[][] nums;    // 原始数组，用于计算更新差值
        private int N;           // 矩阵行数
        private int M;           // 矩阵列数

        /**
         * 构造函数：初始化数据结构
         * 
         * @param matrix 输入的二维矩阵
         */
        public NumMatrix(int[][] matrix) {
            if (matrix.length == 0 || matrix[0].length == 0) {
                return;
            }
            N = matrix.length;
            M = matrix[0].length;
            tree = new int[N + 1][M + 1];  // 树状数组，下标从1开始
            nums = new int[N][M];          // 原始数组备份
            
            // 初始化：构建树状数组
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    update(i, j, matrix[i][j]);
                }
            }
        }

        /**
         * 更新矩阵中指定位置的值
         * 
         * 算法步骤：
         * 1. 计算新旧值的差值
         * 2. 使用add方法在树状数组中更新差值
         * 3. 更新原始数组的备份
         * 
         * @param row 行索引
         * @param col 列索引  
         * @param val 新值
         */
        public void update(int row, int col, int val) {
            add(row + 1, col + 1, val - nums[row][col]); // 树状数组下标从1开始
            nums[row][col] = val;  // 更新原始数组备份
        }

        /**
         * 查询指定矩形区域的元素和
         * 
         * 使用容斥原理：
         * sum(r1,c1,r2,c2) = sum(r2,c2) - sum(r1-1,c2) - sum(r2,c1-1) + sum(r1-1,c1-1)
         * 
         * @param row1 左上角行索引
         * @param col1 左上角列索引
         * @param row2 右下角行索引
         * @param col2 右下角列索引
         * @return 矩形区域的元素和
         */
        public int sumRegion(int row1, int col1, int row2, int col2) {
            // 转换为树状数组的1-based索引，并应用容斥原理
            return sum(row2 + 1, col2 + 1) 
                 - sum(row1, col2 + 1) 
                 - sum(row2 + 1, col1) 
                 + sum(row1, col1);
        }

        /**
         * 在树状数组的指定位置增加值
         * 
         * 二维树状数组的更新操作：
         * - 对于位置(i,j)，需要更新所有包含该位置的树状数组节点
         * - 使用lowbit操作找到下一个需要更新的位置
         * 
         * @param i 行索引（1-based）
         * @param j 列索引（1-based）
         * @param val 要增加的值
         */
        private void add(int i, int j, int val) {
            for (int a = i; a <= N; a += a & (-a)) {      // 行方向的树状数组更新
                for (int b = j; b <= M; b += b & (-b)) {  // 列方向的树状数组更新
                    tree[a][b] += val;
                }
            }
        }

        /**
         * 查询从(1,1)到(i,j)的矩形区域和
         * 
         * 二维树状数组的前缀和查询：
         * - 累加所有对(i,j)位置有贡献的树状数组节点
         * - 使用lowbit操作遍历所有相关节点
         * 
         * @param i 右下角行索引（1-based）
         * @param j 右下角列索引（1-based）
         * @return 矩形区域的前缀和
         */
        private int sum(int i, int j) {
            int ans = 0;
            for (int a = i; a > 0; a -= a & (-a)) {       // 行方向的树状数组查询
                for (int b = j; b > 0; b -= b & (-b)) {   // 列方向的树状数组查询
                    ans += tree[a][b];
                }
            }
            return ans;
        }
    }
}
