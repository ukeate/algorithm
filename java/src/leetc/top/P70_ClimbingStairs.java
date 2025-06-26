package leetc.top;

/**
 * LeetCode 70. 爬楼梯 (Climbing Stairs)
 * 
 * 问题描述：
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 
 * 示例：
 * - 输入：n = 2，输出：2（1阶+1阶 或 2阶）
 * - 输入：n = 3，输出：3（1+1+1 或 1+2 或 2+1）
 * 
 * 解法思路：
 * 这是一个经典的斐波那契数列问题。设f(n)为爬到第n阶的方法数：
 * - f(n) = f(n-1) + f(n-2)
 * - 边界条件：f(1) = 1, f(2) = 2
 * 
 * 本实现使用矩阵快速幂优化：
 * 1. 将递推关系表示为矩阵乘法
 * 2. 使用快速幂算法计算矩阵的n次方
 * 3. 时间复杂度从O(n)优化到O(log n)
 * 
 * 矩阵推导：
 * [f(n)]   = [1 1] * [f(n-1)]
 * [f(n-1)]   [1 0]   [f(n-2)]
 * 
 * 时间复杂度：O(log n)
 * 空间复杂度：O(1)
 */
public class P70_ClimbingStairs {
    
    /**
     * 矩阵乘法
     * 
     * @param m1 第一个矩阵
     * @param m2 第二个矩阵
     * @return 两个矩阵的乘积
     */
    private static int[][] multiMatrix(int[][] m1, int[][] m2) {
        int[][] res = new int[m1.length][m2[0].length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2[0].length; j++) {
                for (int k = 0; k < m2.length; k++) {
                    res[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return res;
    }

    /**
     * 矩阵快速幂算法
     * 
     * 算法思路：
     * 利用快速幂的思想，将矩阵的n次方分解为log n次矩阵乘法
     * 
     * @param m 底数矩阵
     * @param p 指数
     * @return 矩阵m的p次方
     */
    private static int[][] matrixPower(int[][] m, int p) {
        // 初始化单位矩阵
        int[][] res = new int[m.length][m[0].length];
        for (int i = 0; i < res.length; i++) {
            res[i][i] = 1;
        }
        
        int[][] tmp = m;
        // 快速幂算法
        for (; p > 0; p >>= 1) {
            if ((p & 1) == 1) {
                res = multiMatrix(res, tmp);  // 指数为奇数时累乘
            }
            tmp = multiMatrix(tmp, tmp);      // 底数平方
        }
        return res;
    }

    /**
     * 爬楼梯的主算法
     * 
     * 算法思路：
     * 1. 处理边界情况：n=1返回1，n=2返回2
     * 2. 使用转移矩阵[[1,1],[1,0]]进行快速幂计算
     * 3. 根据矩阵幂的结果计算f(n)
     * 
     * @param n 楼梯阶数
     * @return 爬到楼顶的不同方法数
     */
    public int climbStairs(int n) {
        if (n < 1) {
            return 0;
        }
        if (n == 1 || n == 2) {
            return n;
        }
        
        // 斐波那契数列的转移矩阵
        int[][] base = {{1, 1}, {1, 0}};
        
        // 计算base^(n-2)，因为我们从f(2)=2, f(1)=1开始
        int[][] res = matrixPower(base, n - 2);
        
        // f(n) = res[0][0] * f(2) + res[1][0] * f(1)
        //      = res[0][0] * 2 + res[1][0] * 1
        return 2 * res[0][0] + res[1][0];
    }
}
