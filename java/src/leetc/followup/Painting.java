package leetc.followup;

import java.util.ArrayList;
import java.util.List;

/**
 * 棋盘涂色问题 (Chessboard Coloring Problem)
 * 
 * 问题描述：
 * 给定一个n×m的棋盘，需要为每个格子涂上颜色
 * 
 * 约束条件：
 * 1. 所有颜色的格子数必须相同（即n×m必须能被颜色数整除）
 * 2. 相邻的格子不能涂相同的颜色（四连通，即上下左右）
 * 3. 求最少需要多少种颜色
 * 
 * 解法思路：
 * 回溯算法 + 剪枝：
 * 1. 从最少颜色数开始尝试（至少需要2种颜色，因为相邻不能同色）
 * 2. 计算每种颜色需要使用的格子数：total_cells / color_count
 * 3. 使用回溯算法逐个格子填色：
 *    - 按行列顺序遍历每个位置
 *    - 对于每个位置，尝试所有可能的颜色
 *    - 检查颜色约束：不能与左边和上边的格子同色
 *    - 检查数量约束：该颜色剩余可用次数
 * 4. 如果成功填完整个棋盘，说明当前颜色数可行
 * 
 * 优化策略：
 * - 从小到大尝试颜色数，找到第一个可行解即为最优解
 * - 提前判断：如果n×m不能被颜色数整除，直接跳过
 * - 剪枝：如果某种颜色用完了，不再尝试该颜色
 * 
 * 时间复杂度：O(k^(n×m)) - k为颜色数，最坏情况下需要尝试每个位置的每种颜色
 * 空间复杂度：O(n×m) - 棋盘存储空间 + 递归栈空间
 */
public class Painting {
    /**
     * 使用回溯算法尝试为棋盘填色
     * 
     * @param matrix 棋盘矩阵
     * @param n 棋盘行数
     * @param m 棋盘列数
     * @param kind 颜色数量
     * @param row 当前处理的行
     * @param col 当前处理的列
     * @param rest 每种颜色的剩余可用次数
     * @return 是否成功填完整个棋盘
     */
    private static boolean process(int[][] matrix, int n, int m, int kind, int row, int col, List<Integer> rest) {
        if (row == n) {
            // 所有行都处理完毕，成功填完整个棋盘
            return true;
        }
        if (col == m) {
            // 当前行处理完毕，进入下一行
            return process(matrix, n, m, kind, row + 1, 0, rest);
        }
        
        // 获取左边和上边格子的颜色（0表示边界或未填色）
        int left = col == 0 ? 0 : matrix[row][col - 1];   // 左边格子颜色
        int up = row == 0 ? 0 : matrix[row - 1][col];     // 上边格子颜色
        
        // 尝试所有可能的颜色（颜色编号从1开始）
        for (int clr = 1; clr <= kind; clr++) {
            // 检查颜色约束：不能与相邻格子同色，且该颜色还有剩余次数
            if (clr != left && clr != up && rest.get(clr) > 0) {
                // 使用该颜色
                int cnt = rest.get(clr);
                rest.set(clr, cnt - 1);      // 减少该颜色的可用次数
                matrix[row][col] = clr;      // 填色
                
                // 递归处理下一个位置
                if (process(matrix, n, m, kind, row, col + 1, rest)) {
                    return true; // 找到可行解
                }
                
                // 回溯：恢复现场
                rest.set(clr, cnt);          // 恢复颜色次数
                matrix[row][col] = 0;        // 清除填色
            }
        }
        
        return false; // 所有颜色都尝试过，无法填色
    }

    private static boolean can(int[][] matrix, int n, int m, int kind) {
        int all = n * m;
        int every = all / kind;
        ArrayList<Integer> rest = new ArrayList<>();
        rest.add(0);
        for (int i = 1; i <= kind; i++) {
            rest.add(every);
        }
        return process(matrix, n, m, kind, 0, 0, rest);
    }

    public static int min(int n, int m) {
        for (int i = 2; i <= n * m; i++) {
            int[][] matrix = new int[n][m];
            if ((n * m) % i == 0 && can(matrix, n, m, i)) {
                return i;
            }
        }
        return n * m;
    }

    public static void main(String[] args) {
        // ans为n或m的最小因子
        for (int n = 2; n < 11; n++) {
            for (int m = 2; m < 11; m++) {
                System.out.println("n = " + n);
                System.out.println("m = " + m);
                System.out.println("ans = " + min(n, m));
                System.out.println("=====");
            }
        }
    }
}
