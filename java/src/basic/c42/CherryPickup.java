package basic.c42;

/**
 * 摘樱桃问题（Cherry Pickup）
 * 
 * 问题描述：
 * 给定一个N×M的矩阵，矩阵中的值为0或1（1表示有樱桃）
 * 需要先从左上角(0,0)走到右下角(n-1,m-1)，再从右下角走回左上角
 * 每个位置的樱桃只能摘一次，只能向右或向下移动
 * 求能摘到的樱桃最大数量
 * 
 * 核心洞察：
 * 将往返问题转化为两个人同时从起点走到终点的问题
 * 这样避免了复杂的路径回溯，简化了状态表示
 * 
 * 算法思路：
 * 方法1：递归暴力搜索 - O(4^(m+n))
 * 方法2：记忆化搜索优化 - O(n*m*n)
 * 
 * 状态表示：用三个参数(ar, ac, br)表示两人位置
 * bc可以通过ar+ac-br计算得出，减少一个维度
 * 
 * @author 算法学习
 */
public class CherryPickup {
    
    /**
     * 方法1：递归暴力搜索
     * 两个人A和B同时从(0,0)走向(n-1,m-1)
     * 
     * @param matrix 樱桃矩阵
     * @param ar A的行坐标
     * @param ac A的列坐标  
     * @param br B的行坐标
     * @return 从当前状态开始能摘到的最大樱桃数
     * 
     * 状态压缩技巧：
     * bc = ar + ac - br （因为两人走的步数相同）
     * 
     * 转移方程：
     * 四种组合：A下B右、A下B下、A右B右、A右B下
     */
    private static int process(int[][] matrix, int ar, int ac, int br) {
        int n = matrix.length;
        int m = matrix[0].length;
        
        // 递归边界：A到达终点
        if (ar == n - 1 && ac == m - 1) {
            return matrix[ar][ac];
        }
        
        // 计算B的列坐标（状态压缩）
        int bc = ar + ac - br;
        
        // 尝试四种移动组合
        int aDownBRight = -1;  // A向下，B向右
        if (ar + 1 < n && bc + 1 < m) {
            aDownBRight = process(matrix, ar + 1, ac, br);
        }
        
        int aDownBDown = -1;   // A向下，B向下
        if (ar + 1 < n && br + 1 < n) {
            aDownBDown = process(matrix, ar + 1, ac, br + 1);
        }
        
        int aRightBRight = -1; // A向右，B向右
        if (ac + 1 < m && bc + 1 < m) {
            aRightBRight = process(matrix, ar, ac + 1, br);
        }
        
        int aRightBDown = -1;  // A向右，B向下
        if (ac + 1 < m && br + 1 < n) {
            aRightBDown = process(matrix, ar, ac + 1, br + 1);
        }
        
        // 选择最优的下一步
        int nextBest = Math.max(Math.max(aDownBRight, aDownBDown), 
                               Math.max(aRightBRight, aRightBDown));
        
        // 如果A和B在同一位置，樱桃只能拿一次
        if (ar == br) {
            return matrix[ar][ac] + nextBest;
        }
        
        // A和B在不同位置，各自拿各自位置的樱桃
        return matrix[ar][ac] + matrix[br][bc] + nextBest;
    }

    /**
     * 方法1的入口函数
     * 
     * @param matrix 樱桃矩阵
     * @return 最大樱桃数量
     */
    public static int max1(int[][] matrix) {
        return process(matrix, 0, 0, 0);
    }

    /**
     * 方法2：记忆化搜索优化
     * 使用三维dp数组缓存计算结果
     * 
     * @param grid 樱桃矩阵（-1表示障碍物）
     * @param x1 A的行坐标
     * @param y1 A的列坐标
     * @param x2 B的行坐标
     * @param dp 记忆化数组
     * @return 从当前状态开始能摘到的最大樱桃数
     * 
     * 扩展功能：支持-1表示障碍物（无法通过）
     */
    private static int process2(int[][] grid, int x1, int y1, int x2, int[][][] dp) {
        // 边界检查：越界返回无效值
        if (x1 == grid.length || y1 == grid[0].length || x2 == grid.length || 
            x1 + y1 - x2 == grid[0].length) {
            return Integer.MIN_VALUE;
        }
        
        // 记忆化检查：已计算过直接返回
        if (dp[x1][y1][x2] != Integer.MIN_VALUE) {
            return dp[x1][y1][x2];
        }
        
        // 递归边界：到达终点
        if (x1 == grid.length - 1 && y1 == grid[0].length - 1) {
            dp[x1][y1][x2] = grid[x1][y1];
            return dp[x1][y1][x2];
        }
        
        // 尝试四种移动方向，找到最优解
        int next = Integer.MIN_VALUE;
        next = Math.max(next, process2(grid, x1 + 1, y1, x2 + 1, dp)); // A下B下
        next = Math.max(next, process2(grid, x1 + 1, y1, x2, dp));     // A下B右
        next = Math.max(next, process2(grid, x1, y1 + 1, x2 + 1, dp)); // A右B下
        next = Math.max(next, process2(grid, x1, y1 + 1, x2, dp));     // A右B右
        
        // 障碍物检查：如果当前位置或下一步无法到达
        if (grid[x1][y1] == -1 || grid[x2][x1 + y1 - x2] == -1 || next == -1) {
            dp[x1][y1][x2] = -1;
            return dp[x1][y1][x2];
        }
        
        // 状态转移：同位置只拿一次，不同位置各拿各的
        if (x1 == x2) {
            dp[x1][y1][x2] = grid[x1][y1] + next;
        } else {
            dp[x1][y1][x2] = grid[x1][y1] + grid[x2][x1 + y1 - x2] + next;
        }
        
        return dp[x1][y1][x2];
    }

    /**
     * 方法2的入口函数：记忆化搜索版本
     * 
     * @param grid 樱桃矩阵（支持-1障碍物）
     * @return 最大樱桃数量，无法到达返回0
     * 
     * 时间复杂度：O(n*m*n)
     * 空间复杂度：O(n*m*n)
     */
    public static int max2(int[][] grid) {
        int n = grid.length;
        int m = grid[0].length;
        
        // 初始化dp数组为最小值
        int[][][] dp = new int[n][m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < n; k++) {
                    dp[i][j][k] = Integer.MIN_VALUE;
                }
            }
        }
        
        int ans = process2(grid, 0, 0, 0, dp);
        return ans < 0 ? 0 : ans; // 无法到达返回0
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int[][] matrix = {
            { 1, 1, 1, 1, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 },
            { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 1, 1, 1, 1, 1, 1 }
        };
        
        System.out.println("方法1结果: " + max1(matrix));
        System.out.println("方法2结果: " + max2(matrix));
    }
}
