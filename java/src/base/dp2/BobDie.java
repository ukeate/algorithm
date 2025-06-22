package base.dp2;

/**
 * Bob死亡概率问题 - 网格生存概率计算
 * 
 * 问题描述：
 * Bob被放置在一个n×m的网格上的位置(row, col)。他需要走恰好k步。
 * 每一步，他以相等的概率移动到4个相邻的格子之一（上、下、左、右）。
 * 如果他走出网格边界，他就死了。计算Bob在k步后存活的概率。
 * 
 * 解法分析：
 * 1. 递归方法：对于每个位置和剩余步数，计算总的生存路径数
 * 2. 动态规划：使用3D DP表避免重复计算
 * 
 * 时间复杂度：DP方法 O(n * m * k)
 * Space Complexity: O(n * m * k) for DP table
 * 
 * @author Algorithm Practice
 */
public class BobDie {
    
    /**
     * 递归方法计算生存路径数
     * 
     * @param row 当前行位置
     * @param col 当前列位置  
     * @param rest 剩余步数
     * @param n 网格行数
     * @param m 网格列数
     * @return 在给定参数下的生存方法数
     */
    private static long process(int row, int col, int rest, int n, int m) {
        // base case：如果走出网格边界，Bob死亡（0种生存方法）
        if (row < 0 || row == n || col < 0 || col == m) {
            return 0;
        }
        // base case：如果没有更多步数且仍在网格上，Bob生存（1种方法）
        if (rest == 0) {
            return 1;
        }
        
        // 尝试所有4个方向并累加生存路径
        long up = process(row - 1, col, rest - 1, n, m);
        long down = process(row + 1, col, rest - 1, n, m);
        long left = process(row, col - 1, rest - 1, n, m);
        long right = process(row, col + 1, rest - 1, n, m);
        return up + down + left + right;
    }

    /**
     * 使用递归方法计算生存概率
     * 
     * @param row 起始行位置
     * @param col 起始列位置
     * @param k 总步数
     * @param n 网格高度
     * @param m 网格宽度
     * @return 生存概率（0.0到1.0）
     */
    // n*m的棋盘，空降(row, col)，走k步不掉下去的概率
    public static double possibility(int row, int col, int k, int n, int m) {
        // 总生存路径数 / 总可能路径数
        // 每步有4种选择，所以总路径数 = 4^k
        return (double) process(row, col, k, n, m) / Math.pow(4, k);
    }

    /**
     * 安全访问DP表的辅助方法，带边界检查
     * 
     * @param dp 3D DP表
     * @param n 网格高度
     * @param m 网格宽度
     * @param r 行索引
     * @param c 列索引
     * @param rest 剩余步数
     * @return DP值，越界返回0
     */
    private static long pick(long[][][] dp, int n, int m, int r, int c, int rest) {
        // 对越界位置返回0（死亡）
        if (r < 0 || r == n || c < 0 || c == m) {
            return 0;
        }
        return dp[r][c][rest];
    }

    /**
     * 使用动态规划方法计算生存概率
     * 
     * @param row 起始行位置
     * @param col 起始列位置
     * @param k 总步数
     * @param n 网格高度
     * @param m 网格宽度
     * @return 生存概率（0.0到1.0）
     */
    public static double dp(int row, int col, int k, int n, int m) {
        // dp[i][j][rest] = 从位置(i,j)出发，剩余rest步的生存方法数
        long[][][] dp = new long[n][m][k + 1];
        
        // base case：剩余0步时，如果在网格上则有1种生存方法
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                dp[i][j][0] = 1;
            }
        }
        
        // 为每个剩余步数填充DP表
        for (int rest = 1; rest <= k; rest++) {
            for (int r = 0; r < n; r++) {
                for (int c = 0; c < m; c++) {
                    // 从所有4个相邻位置累加生存路径数
                    dp[r][c][rest] = pick(dp, n, m, r - 1, c, rest - 1);      // 从上方
                    dp[r][c][rest] += pick(dp, n, m, r + 1, c, rest - 1);     // 从下方
                    dp[r][c][rest] += pick(dp, n, m, r, c - 1, rest - 1);     // 从左方
                    dp[r][c][rest] += pick(dp, n, m, r, c + 1, rest - 1);     // 从右方
                }
            }
        }
        
        // 返回概率：从起始位置的生存路径数 / 总可能路径数
        return (double) dp[row][col][k] / Math.pow(4, k);
    }

    /**
     * 测试方法验证两种方法给出相同结果
     */
    public static void main(String[] args) {
        System.out.println(possibility(6, 6, 10, 50, 50));
        System.out.println(dp(6, 6, 10, 50, 50));
    }
}
