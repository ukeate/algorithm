package leetc.top;

/**
 * LeetCode 221. 最大正方形 (Maximal Square)
 * 
 * 问题描述：
 * 在一个由 '0' 和 '1' 组成的二维矩阵内，找到只包含 '1' 的最大正方形，并返回其面积。
 * 
 * 示例：
 * 输入：matrix = [["1","0","1","0","0"],
 *                ["1","0","1","1","1"],
 *                ["1","1","1","1","1"],
 *                ["1","0","0","1","0"]]
 * 输出：4
 * 解释：最大正方形的边长为2，面积为4
 * 
 * 解法思路：
 * 动态规划：
 * 1. 状态定义：dp[i][j] 表示以位置(i,j)为右下角的最大正方形的边长
 * 2. 状态转移：
 *    - 如果 matrix[i][j] == '0'，则 dp[i][j] = 0
 *    - 如果 matrix[i][j] == '1'，则 dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
 * 3. 边界条件：第一行和第一列的dp值等于对应的matrix值
 * 
 * 转移方程的理解：
 * - 要形成边长为k的正方形，需要：
 *   * 左边有边长为k-1的正方形
 *   * 上边有边长为k-1的正方形  
 *   * 左上角有边长为k-1的正方形
 * - 三者的最小值决定了当前位置能形成的最大正方形边长
 * 
 * 优化思路：
 * - 可以将二维DP优化为一维DP，节省空间
 * - 只需要保存上一行的状态和当前行的前一个状态
 * 
 * 核心思想：
 * - 利用已知的小正方形信息构建大正方形
 * - 正方形的约束比矩形更严格，需要考虑三个方向的限制
 * - 动态规划的无后效性：当前状态只依赖于之前的状态
 * 
 * 时间复杂度：O(m*n) - m为行数，n为列数
 * 空间复杂度：O(m*n) - DP数组的空间，可优化为O(n)
 * 
 * LeetCode链接：https://leetcode.com/problems/maximal-square/
 */
public class P221_MaximalSquare {
    
    /**
     * 寻找最大正方形的面积
     * 
     * 算法思路：
     * 1. 创建dp数组，dp[i][j]表示以(i,j)为右下角的最大正方形边长
     * 2. 遍历矩阵，根据状态转移方程更新dp值
     * 3. 记录过程中出现的最大边长，最后返回面积（边长的平方）
     * 
     * 状态转移详解：
     * - 当前位置要形成正方形，必须满足：
     *   * 当前位置是'1' 
     *   * 左边、上边、左上角都能提供支撑
     * - 边长受限于三个方向中最小的那个
     * 
     * @param m 字符矩阵，'0'表示空，'1'表示有方块
     * @return 最大正方形的面积
     */
    public static int maximalSquare(char[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        
        int row = m.length;      // 矩阵行数
        int col = m[0].length;   // 矩阵列数
        int maxSide = 0;         // 记录最大边长
        
        // dp[i][j] 表示以(i,j)为右下角的最大正方形边长
        int[][] dp = new int[row][col];
        
        // 处理第一列
        for (int i = 0; i < row; i++) {
            dp[i][0] = m[i][0] - '0';  // 字符转数字
            maxSide = Math.max(maxSide, dp[i][0]);
        }
        
        // 处理第一行
        for (int j = 1; j < col; j++) {
            dp[0][j] = m[0][j] - '0';  // 字符转数字  
            maxSide = Math.max(maxSide, dp[0][j]);
        }
        
        // 处理其余位置
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (m[i][j] == '1') {
                    // 状态转移：取三个方向的最小值，再加1
                    dp[i][j] = Math.min(
                        Math.min(dp[i-1][j], dp[i][j-1]),  // 上边和左边的最小值
                        dp[i-1][j-1]                        // 左上角
                    ) + 1;
                    
                    // 更新最大边长
                    maxSide = Math.max(maxSide, dp[i][j]);
                } else {
                    // 当前位置是'0'，无法形成正方形
                    dp[i][j] = 0;
                }
            }
        }
        
        // 返回最大正方形的面积
        return maxSide * maxSide;
    }
}
