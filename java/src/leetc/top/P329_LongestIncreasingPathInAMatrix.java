package leetc.top;

/**
 * LeetCode 329. 矩阵中的最长递增路径 (Longest Increasing Path in a Matrix)
 * 
 * 问题描述：
 * 给定一个 m x n 整数矩阵 matrix ，找出其中最长递增路径的长度。
 * 对于每个单元格，你可以往上，下，左，右四个方向移动。
 * 你不能在对角线方向上移动或移动到边界外（即不允许环绕）。
 * 
 * 示例：
 * 输入：matrix = [[9,9,4],[6,6,8],[2,1,1]]
 * 输出：4
 * 解释：最长递增路径为 [1, 2, 6, 9] 或者 [1, 2, 8, 9]。
 * 
 * 输入：matrix = [[3,4,5],[3,2,6],[2,2,1]]
 * 输出：4
 * 解释：最长递增路径是 [3, 4, 5, 6]。移动方向不能是对角线。
 * 
 * 解法思路：
 * DFS + 记忆化搜索（动态规划）：
 * 
 * 1. 核心思想：
 *    - 对每个单元格进行DFS，寻找以该单元格为起点的最长递增路径
 *    - 使用记忆化避免重复计算
 *    - 递增路径的约束保证了无环性，可以安全使用递归
 * 
 * 2. 算法步骤：
 *    - 遍历矩阵中的每个单元格作为起点
 *    - 对每个起点进行DFS搜索
 *    - 在DFS中，只访问值更大的相邻单元格
 *    - 使用memo数组缓存每个位置的最长路径长度
 * 
 * 3. 状态定义：
 *    - memo[i][j] 表示以位置(i,j)为起点的最长递增路径长度
 *    - 状态转移：memo[i][j] = max(memo[next_i][next_j]) + 1
 * 
 * 4. 递归终止条件：
 *    - 当前位置已计算过（memo[i][j] != 0）
 *    - 没有更大的相邻单元格可以访问
 * 
 * 核心思想：
 * - 记忆化搜索：将递归结果缓存，避免重复计算
 * - 有向无环图：递增约束确保路径无环，适合DFS
 * - 局部最优：每个位置的最优解由其邻居的最优解决定
 * 
 * 关键技巧：
 * - 四方向遍历：上下左右四个方向的移动
 * - 边界检查：确保不越界且值递增
 * - 自顶向下：从任意起点开始，递归计算最优解
 * 
 * 时间复杂度：O(m×n) - 每个单元格最多被访问一次
 * 空间复杂度：O(m×n) - 记忆化数组 + 递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/longest-increasing-path-in-a-matrix/
 */
public class P329_LongestIncreasingPathInAMatrix {
    
    // 四个方向：上、下、左、右
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    
    /**
     * 计算矩阵中的最长递增路径长度
     * 
     * 算法流程：
     * 1. 初始化记忆化数组
     * 2. 遍历矩阵中的每个位置作为起点
     * 3. 对每个起点执行DFS搜索
     * 4. 返回所有路径中的最大长度
     * 
     * @param matrix 输入矩阵
     * @return 最长递增路径的长度
     */
    public int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        int[][] memo = new int[m][n];  // 记忆化数组，memo[i][j]表示从(i,j)开始的最长路径
        int maxLength = 0;
        
        // 尝试以每个位置为起点
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int pathLength = dfs(matrix, i, j, memo);
                maxLength = Math.max(maxLength, pathLength);
            }
        }
        
        return maxLength;
    }
    
    /**
     * DFS搜索以(i, j)为起点的最长递增路径
     * 
     * 搜索策略：
     * 1. 如果当前位置已计算过，直接返回缓存结果
     * 2. 尝试向四个方向移动到值更大的相邻位置
     * 3. 递归计算相邻位置的最长路径
     * 4. 更新当前位置的最优解并缓存
     * 
     * @param matrix 原始矩阵
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @param memo 记忆化数组
     * @return 从当前位置开始的最长递增路径长度
     */
    private int dfs(int[][] matrix, int i, int j, int[][] memo) {
        // 如果已经计算过，直接返回缓存结果
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        
        int m = matrix.length;
        int n = matrix[0].length;
        int maxPath = 1;  // 至少包含当前位置，路径长度为1
        
        // 尝试向四个方向移动
        for (int[] direction : DIRECTIONS) {
            int newI = i + direction[0];  // 新的行坐标
            int newJ = j + direction[1];  // 新的列坐标
            
            // 检查边界和递增条件
            if (newI >= 0 && newI < m && newJ >= 0 && newJ < n && 
                matrix[newI][newJ] > matrix[i][j]) {
                
                // 递归计算从新位置开始的最长路径
                int pathFromNeighbor = dfs(matrix, newI, newJ, memo);
                // 更新当前位置的最长路径：当前位置 + 邻居的最长路径
                maxPath = Math.max(maxPath, 1 + pathFromNeighbor);
            }
        }
        
        // 缓存结果并返回
        memo[i][j] = maxPath;
        return maxPath;
    }
}
