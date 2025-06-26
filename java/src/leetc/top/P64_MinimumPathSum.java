package leetc.top;

/**
 * LeetCode 64. 最小路径和 (Minimum Path Sum)
 * 
 * 问题描述：
 * 给定一个包含非负整数的 m x n 网格 grid，
 * 请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * 
 * 说明：每次只能向下或者向右移动一步。
 * 
 * 示例：
 * 输入：grid = [[1,3,1],
 *               [1,5,1],
 *               [4,2,1]]
 * 输出：7
 * 解释：因为路径 1→3→1→1→1 的总和最小。
 * 
 * 解法思路：
 * 动态规划（空间优化版本）：
 * 1. 状态定义：dp[j]表示到达当前行第j列的最小路径和
 * 2. 状态转移：dp[j] = grid[i][j] + min(dp[j], dp[j-1])
 *    - dp[j]：从上方来（上一行同列）
 *    - dp[j-1]：从左方来（当前行前一列）
 * 3. 空间优化：使用一维数组滚动更新，将空间复杂度从O(m×n)优化到O(n)
 * 
 * 边界处理：
 * - 第一行：只能从左边来
 * - 第一列：只能从上边来
 * - 左上角：起始点，路径和为自身值
 * 
 * 时间复杂度：O(m×n) - 需要遍历整个网格
 * 空间复杂度：O(n) - 只使用一维DP数组
 * 
 * LeetCode链接：https://leetcode.com/problems/minimum-path-sum/
 */
public class P64_MinimumPathSum {
    
    /**
     * 计算从左上角到右下角的最小路径和
     * 
     * 算法思路：
     * 使用一维DP数组进行空间优化：
     * 1. dp[j]在处理第i行时表示到达(i,j)的最小路径和
     * 2. 从左到右更新dp数组，利用已更新的dp[j-1]和未更新的dp[j]
     * 3. 特殊处理边界情况（第一行、第一列、起始点）
     * 
     * @param grid m×n的非负整数网格
     * @return 从左上角到右下角的最小路径和
     */
    public static int minPathSum(int[][] grid) {
        // 边界条件检查
        if (grid == null || grid[0] == null) {
            return 0;
        }
        
        int n = grid.length, m = grid[0].length;  // n行m列
        if (m == 0) {
            return 0;
        }
        
        // dp[j]表示到达当前行第j列的最小路径和
        int[] dp = new int[m];
        
        // 逐行处理网格
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 && j == 0) {
                    // 起始点：路径和为自身值
                    dp[j] = grid[i][j];
                } else {
                    // 状态转移：选择从上方或左方来的最小路径
                    int fromUp = (i > 0) ? dp[j] : Integer.MAX_VALUE;      // 从上方来
                    int fromLeft = (j > 0) ? dp[j - 1] : Integer.MAX_VALUE; // 从左方来
                    
                    dp[j] = grid[i][j] + Math.min(fromUp, fromLeft);
                }
            }
        }
        
        // 返回到达右下角的最小路径和
        return dp[m - 1];
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[][] grid1 = {
            {1, 3, 1},
            {1, 5, 1},
            {4, 2, 1}
        };
        System.out.println("测试用例1:");
        printGrid(grid1);
        System.out.println("最小路径和: " + minPathSum(grid1));
        System.out.println("期望结果: 7");
        System.out.println("最优路径: 1→3→1→1→1");
        System.out.println();
        
        // 测试用例2：单行网格
        int[][] grid2 = {{1, 2, 3, 4, 5}};
        System.out.println("测试用例2 (单行):");
        printGrid(grid2);
        System.out.println("最小路径和: " + minPathSum(grid2));
        System.out.println("期望结果: 15");
        System.out.println("最优路径: 1→2→3→4→5");
        System.out.println();
        
        // 测试用例3：单列网格
        int[][] grid3 = {{1}, {2}, {3}, {4}};
        System.out.println("测试用例3 (单列):");
        printGrid(grid3);
        System.out.println("最小路径和: " + minPathSum(grid3));
        System.out.println("期望结果: 10");
        System.out.println("最优路径: 1→2→3→4");
        System.out.println();
        
        // 测试用例4：单个元素
        int[][] grid4 = {{5}};
        System.out.println("测试用例4 (单元素):");
        printGrid(grid4);
        System.out.println("最小路径和: " + minPathSum(grid4));
        System.out.println("期望结果: 5");
        System.out.println();
        
        // 测试用例5：较大网格
        int[][] grid5 = {
            {1, 2, 5},
            {3, 2, 1},
            {4, 1, 2},
            {2, 3, 1}
        };
        System.out.println("测试用例5 (4×3网格):");
        printGrid(grid5);
        System.out.println("最小路径和: " + minPathSum(grid5));
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点：");
        System.out.println("- 动态规划：自底向上构建最优解");
        System.out.println("- 空间优化：O(m×n) → O(min(m,n))");
        System.out.println("- 时间复杂度：O(m×n)");
        System.out.println("- 只能向右或向下移动的约束使得问题具有最优子结构");
    }
    
    /**
     * 辅助方法：打印网格
     */
    private static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            System.out.print("[");
            for (int i = 0; i < row.length; i++) {
                System.out.print(row[i]);
                if (i < row.length - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }
}
