package basic.c42;

/**
 * 地牢游戏问题（Dungeon Game）
 * 
 * 问题描述：
 * 在一个m×n的地牢中，从左上角到右下角拯救公主
 * 地牢中正数表示血量增加，负数表示血量减少
 * 勇士的血量在任何时候都不能降到1以下（即必须>0）
 * 求勇士的最小初始血量
 * 
 * 关键洞察：
 * 这是一个逆向思维问题 - 从终点倒推而不是从起点正推
 * 因为需要保证路径上每个位置的血量都大于0
 * 
 * 算法思路：
 * 方法1：递归暴力搜索 - O(2^(m+n))
 * 方法2：二维DP - O(m*n)
 * 方法3：一维DP空间优化 - O(min(m,n))
 * 
 * 状态定义：dp[i][j] = 从(i,j)到终点需要的最小血量
 * 
 * @author 算法学习
 */
public class DungeonGame {
    
    /**
     * 方法1：递归暴力解法
     * 从当前位置计算到达终点需要的最小血量
     * 
     * @param matrix 地牢矩阵
     * @param n 行数
     * @param m 列数
     * @param row 当前行
     * @param col 当前列
     * @return 从(row,col)到终点需要的最小血量
     * 
     * 递归思路：
     * 1. 边界：到达终点的最小血量
     * 2. 递归：选择向右或向下的最优路径
     * 3. 血量计算：确保当前位置血量>0
     */
    private static int process(int[][] matrix, int n, int m, int row, int col) {
        // 递归边界：到达终点
        if (row == n - 1 && col == m - 1) {
            // 如果终点是负数，需要足够血量抵消；否则只需要1点血量
            return matrix[n - 1][m - 1] < 0 ? (-matrix[n - 1][m - 1] + 1) : 1;
        }
        
        // 边界情况：只能向右走
        if (row == n - 1) {
            int rightNeed = process(matrix, n, m, row, col + 1);
            return calculateNeed(matrix[row][col], rightNeed);
        }
        
        // 边界情况：只能向下走
        if (col == m - 1) {
            int downNeed = process(matrix, n, m, row + 1, col);
            return calculateNeed(matrix[row][col], downNeed);
        }
        
        // 一般情况：选择向右或向下的最优路径
        int need = Math.min(process(matrix, n, m, row, col + 1),
                           process(matrix, n, m, row + 1, col));
        return calculateNeed(matrix[row][col], need);
    }
    
    /**
     * 计算在当前位置需要的血量
     * 
     * @param currentValue 当前位置的值
     * @param nextNeed 下一步需要的血量
     * @return 当前位置需要的最小血量
     */
    private static int calculateNeed(int currentValue, int nextNeed) {
        if (currentValue < 0) {
            // 当前位置扣血，需要额外血量抵消
            return -currentValue + nextNeed;
        } else if (currentValue >= nextNeed) {
            // 当前位置加血足够，只需要1点血量
            return 1;
        } else {
            // 当前位置加血不足，需要补充
            return nextNeed - currentValue;
        }
    }

    /**
     * 方法1的入口函数
     * 
     * @param matrix 地牢矩阵
     * @return 最小初始血量
     */
    public static int min1(int[][] matrix) {
        return process(matrix, matrix.length, matrix[0].length, 0, 0);
    }

    /**
     * 方法2：二维DP优化
     * 使用动态规划从终点向起点填表
     * 
     * @param m 地牢矩阵
     * @return 最小初始血量
     * 
     * 状态转移方程：
     * dp[i][j] = min(max(dp[i][j+1] - m[i][j], 1), 
     *               max(dp[i+1][j] - m[i][j], 1))
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public static int min2(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 1;
        }
        
        int row = m.length;
        int col = m[0].length;
        int[][] dp = new int[row--][col--]; // 注意：这里同时进行了减1操作
        
        // 初始化终点位置
        dp[row][col] = m[row][col] > 0 ? 1 : -m[row][col] + 1;
        
        // 填充最后一行（只能向右走）
        for (int j = col - 1; j >= 0; j--) {
            dp[row][j] = Math.max(dp[row][j + 1] - m[row][j], 1);
        }
        
        // 从下往上填表
        for (int i = row - 1; i >= 0; i--) {
            // 填充最后一列（只能向下走）
            dp[i][col] = Math.max(dp[i + 1][col] - m[i][col], 1);
            
            // 填充其他位置（可以向右或向下）
            for (int j = col - 1; j >= 0; j--) {
                int right = Math.max(dp[i][j + 1] - m[i][j], 1); // 向右走的需求
                int down = Math.max(dp[i + 1][j] - m[i][j], 1);  // 向下走的需求
                dp[i][j] = Math.min(right, down); // 选择需求更小的路径
            }
        }
        
        return dp[0][0];
    }

    /**
     * 方法3：一维DP空间优化
     * 将二维DP优化为一维DP，节省空间
     * 
     * @param m 地牢矩阵
     * @return 最小初始血量
     * 
     * 优化思路：
     * 1. 判断行多还是列多，按较短的维度创建DP数组
     * 2. 逐行或逐列更新DP数组
     * 3. 复用空间，减少内存占用
     * 
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(min(m,n))
     */
    public static int min3(int[][] m) {
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 1;
        }
        
        int more = Math.max(m.length, m[0].length);   // 较长的维度
        int less = Math.min(m.length, m[0].length);   // 较短的维度
        boolean rowMore = more == m.length;           // 行是否是较长维度
        
        // 创建一维DP数组，长度为较短的维度
        int[] dp = new int[less];
        
        // 初始化终点位置
        int tmp = m[m.length - 1][m[0].length - 1];
        dp[less - 1] = tmp > 0 ? 1 : -tmp + 1;
        
        // 初始化边界（最后一行或最后一列）
        for (int j = less - 2; j >= 0; j--) {
            int row = rowMore ? more - 1 : j;
            int col = rowMore ? j : more - 1;
            dp[j] = Math.max(dp[j + 1] - m[row][col], 1);
        }
        
        // 逐行或逐列更新DP数组
        for (int i = more - 2; i >= 0; i--) {
            // 更新边界位置
            int row = rowMore ? i : less - 1;
            int col = rowMore ? less - 1 : i;
            dp[less - 1] = Math.max(dp[less - 1] - m[row][col], 1);
            
            // 更新其他位置
            for (int j = less - 2; j >= 0; j--) {
                row = rowMore ? i : j;
                col = rowMore ? j : i;
                // dp[j]代表从下方来，dp[j+1]代表从右方来
                dp[j] = Math.min(Math.max(dp[j] - m[row][col], 1),
                                Math.max(dp[j + 1] - m[row][col], 1));
            }
        }
        
        return dp[0];
    }

    /**
     * 测试方法：验证三种算法的正确性
     */
    public static void main(String[] args) {
        int[][] map = {
            {-2, -3, 3}, 
            {-5, -10, 1}, 
            {10, 30, -5}
        };
        
        System.out.println("递归方法结果: " + min1(map));
        System.out.println("二维DP结果: " + min2(map));
        System.out.println("一维DP结果: " + min3(map));
    }
}
