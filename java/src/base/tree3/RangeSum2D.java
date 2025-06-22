package base.tree3;

/**
 * 二维区域和查询 - 可变
 * 使用二维树状数组（Binary Indexed Tree）实现
 * 支持单点更新和矩形区域查询
 * https://leetcode.com/problems/range-sum-query-2d-mutable
 */
public class RangeSum2D {
    private int[][] tree;  // 二维树状数组
    private int[][] nums;  // 原始数组的副本
    private int n;         // 行数
    private int m;         // 列数

    /**
     * 构造函数：初始化二维树状数组
     * @param matrix 原始二维数组
     */
    public RangeSum2D(int[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        n = matrix.length;
        m = matrix[0].length;
        tree = new int[n + 1][m + 1];  // 树状数组下标从1开始
        nums = new int[n][m];
        // 初始化所有元素
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                update(i, j, matrix[i][j]);
            }
        }
    }

    /**
     * 计算从(0,0)到(row,col)的矩形区域和
     * @param row 行坐标
     * @param col 列坐标
     * @return 区域和
     */
    private int sum(int row, int col) {
        int sum = 0;
        // 二维树状数组查询：沿着二进制表示向前移动
        for (int i = row + 1; i > 0; i -= i & -i) {
            for (int j = col + 1; j > 0; j -= j & -j) {
                sum += tree[i][j];
            }
        }
        return sum;
    }

    /**
     * 更新指定位置的值
     * @param row 行坐标
     * @param col 列坐标
     * @param val 新值
     */
    public void update(int row, int col, int val) {
        if (n == 0 || m == 0) {
            return;
        }
        int add = val - nums[row][col];  // 计算差值
        nums[row][col] = val;            // 更新原始数组
        // 在树状数组中传播更新：沿着二进制表示向后移动
        for (int i = row + 1; i <= n; i += i & -i) {
            for (int j = col + 1; j <= m; j += j & -j) {
                tree[i][j] += add;
            }
        }
    }

    /**
     * 查询矩形区域(row1,col1)到(row2,col2)的和
     * 使用容斥原理：S(row2,col2) - S(row1-1,col2) - S(row2,col1-1) + S(row1-1,col1-1)
     * @param row1 左上角行坐标
     * @param col1 左上角列坐标
     * @param row2 右下角行坐标
     * @param col2 右下角列坐标
     * @return 矩形区域和
     */
    public int sumRegion(int row1, int col1, int row2, int col2) {
        if (n == 0 || m == 0) {
            return 0;
        }
        return sum(row2, col2) + sum(row1 - 1, col1 - 1) - sum(row1 - 1, col2) - sum(row2, col1 - 1);
    }
}
