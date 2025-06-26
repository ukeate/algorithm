package leetc.top;

/**
 * LeetCode 1139. 最大 1 边框正方形 (Largest 1-Bordered Square)
 * 
 * 问题描述：
 * 给你一个由若干 0 和 1 组成的二维网格 grid，请你找出边界全部由 1 组成的最大正方形子网格，
 * 并返回该子网格中的元素数量。如果不存在，则返回 0。
 * 
 * 解法思路：
 * 动态规划 + 预处理优化：
 * 1. 预处理阶段：
 *    - right[i][j]: 从位置(i,j)开始向右连续1的个数
 *    - down[i][j]: 从位置(i,j)开始向下连续1的个数
 * 2. 检查阶段：
 *    - 枚举所有可能的正方形大小（从大到小）
 *    - 对于每个大小，检查是否存在符合条件的正方形
 * 3. 正方形边框检查：
 *    - 左上角向右的长度 >= size
 *    - 左上角向下的长度 >= size  
 *    - 左下角向右的长度 >= size
 *    - 右上角向下的长度 >= size
 * 
 * 核心观察：
 * - 正方形的四条边都必须全部由1组成
 * - 通过预处理避免重复计算连续1的长度
 * - 从大到小枚举正方形大小，找到第一个就是最大的
 * 
 * 优化技巧：
 * - 从右下角开始预处理，便于递推计算
 * - 提前终止：找到第一个满足条件的大小就返回
 * 
 * 时间复杂度：O(min(m,n) * m * n) - 枚举大小O(min(m,n))，检查O(m*n)
 * 空间复杂度：O(m * n) - 两个预处理数组
 * 
 * LeetCode链接：https://leetcode.com/problems/largest-1-bordered-square/
 */
public class P1139_Largest1BorderedSquare {
    
    /**
     * 预处理函数：计算每个位置向右和向下连续1的个数
     * 
     * @param m 原始矩阵
     * @param right right[i][j]表示从(i,j)向右连续1的个数
     * @param down down[i][j]表示从(i,j)向下连续1的个数
     */
    private static void setBorderMap(int[][] m, int[][] right, int[][] down) {
        int r = m.length, c = m[0].length;
        
        // 处理右下角
        if (m[r - 1][c - 1] == 1) {
            right[r - 1][c - 1] = 1;
            down[r - 1][c - 1] = 1;
        }
        
        // 处理最后一列（从下往上）
        for (int i = r - 2; i >= 0; i--) {
            if (m[i][c - 1] == 1) {
                right[i][c - 1] = 1;                    // 最后一列向右只有1个
                down[i][c - 1] = down[i + 1][c - 1] + 1; // 向下连续1的个数
            }
        }
        
        // 处理最后一行（从右往左）
        for (int i = c - 2; i >= 0; i--) {
            if (m[r - 1][i] == 1) {
                right[r - 1][i] = right[r - 1][i + 1] + 1; // 向右连续1的个数
                down[r - 1][i] = 1;                        // 最后一行向下只有1个
            }
        }
        
        // 处理其他位置（从右下往左上）
        for (int i = r - 2; i >= 0; i--) {
            for (int j = c - 2; j >= 0; j--) {
                if (m[i][j] == 1) {
                    right[i][j] = right[i][j + 1] + 1; // 当前位置向右连续1的个数
                    down[i][j] = down[i + 1][j] + 1;   // 当前位置向下连续1的个数
                }
            }
        }
    }

    /**
     * 检查是否存在指定大小的1边框正方形
     * 
     * @param size 正方形的边长
     * @param right 向右连续1的预处理数组
     * @param down 向下连续1的预处理数组
     * @return 是否存在
     */
    private static boolean hasSize(int size, int[][] right, int[][] down) {
        // 枚举所有可能的左上角位置
        for (int i = 0; i < right.length - size + 1; i++) {
            for (int j = 0; j < right[0].length - size + 1; j++) {
                // 检查正方形的四条边是否都由1组成
                if (right[i][j] >= size &&           // 上边：左上角向右
                    down[i][j] >= size &&            // 左边：左上角向下
                    right[i + size - 1][j] >= size && // 下边：左下角向右
                    down[i][j + size - 1] >= size) {  // 右边：右上角向下
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 寻找最大1边框正方形
     * 
     * @param m 二维网格
     * @return 最大正方形的面积
     */
    public static int largest1BorderedSquare(int[][] m) {
        // 创建预处理数组
        int[][] right = new int[m.length][m[0].length];
        int[][] down = new int[m.length][m[0].length];
        
        // 预处理：计算每个位置向右和向下连续1的个数
        setBorderMap(m, right, down);
        
        // 从最大可能的正方形开始检查
        for (int size = Math.min(m.length, m[0].length); size > 0; size--) {
            if (hasSize(size, right, down)) {
                return size * size; // 返回面积
            }
        }
        
        return 0; // 没有找到1边框正方形
    }
}
