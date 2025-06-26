package leetc.top;

/**
 * LeetCode 361. 炸弹敌人 (Bomb Enemy)
 * 
 * 问题描述：
 * 给你一个 m x n 的矩阵 grid ，其中：
 * - 'M' 表示一堵墙
 * - 'E' 表示一个敌人
 * - '0' 表示一个空位（可以放置炸弹）
 * 
 * 一颗炸弹可以炸死同一行和同一列中在炸弹位置和墙之间的所有敌人。
 * 墙体足够坚固，能够阻止爆炸，所以墙后的敌人不会被炸死。
 * 
 * 返回你能用一颗炸弹最多能炸死多少敌人。
 * 
 * 示例：
 * 输入：grid = [["0","E","0","0"],
 *               ["E","0","W","E"],
 *               ["0","E","0","0"]]
 * 输出：3
 * 解释：对于给定的网格：
 * 0 E 0 0
 * E 0 W E
 * 0 E 0 0
 * 
 * 假如在位置 (1,1) 放置炸弹的话，可以炸到 3 个敌人：
 * - 在 (0,1) 的敌人
 * - 在 (1,0) 的敌人  
 * - 在 (2,1) 的敌人
 * 
 * 注意：炸弹只能放在空位上
 * 
 * 提示：
 * - m == grid.length
 * - n == grid[i].length
 * - 1 <= m, n <= 500
 * - grid[i][j] 为 '0'、'E' 或者 'W'
 * 
 * 解法思路：
 * 动态规划 + 预处理优化：
 * 
 * 1. 核心思想：
 *    - 对于每个空位，计算放置炸弹后能炸死的敌人数量
 *    - 分别计算四个方向（上下左右）能炸死的敌人数量
 *    - 使用动态规划优化计算过程
 * 
 * 2. 算法优化：
 *    - 预处理每行和每列的敌人分布
 *    - 使用滚动数组减少重复计算
 *    - 利用墙体分割的特性优化计算
 * 
 * 3. 实现策略：
 *    - 方法一：暴力法，对每个空位进行四方向搜索
 *    - 方法二：预处理优化，计算每个位置四个方向的敌人数量
 *    - 方法三：滚动数组优化，降低空间复杂度
 * 
 * 核心思想：
 * - 预处理优化：避免重复计算相同方向的敌人数量
 * - 分治思想：分别处理四个方向，最后合并结果
 * - 动态规划：利用之前计算的结果
 * 
 * 关键技巧：
 * - 墙体分割：利用墙体自然分割区域的特性
 * - 方向预处理：提前计算每个方向的敌人数量
 * - 空间优化：使用滚动数组减少内存使用
 * 
 * 时间复杂度：O(mn) - 预处理优化版本
 * 空间复杂度：O(mn) - 存储四个方向的敌人数量
 * 
 * LeetCode链接：https://leetcode.com/problems/bomb-enemy/
 */
public class P361_BombEnemy {
    
    /**
     * 方法一：暴力搜索法
     * 
     * 对每个空位进行四个方向的搜索
     * 
     * @param grid 游戏网格
     * @return 一颗炸弹最多能炸死的敌人数量
     */
    public int maxKilledEnemies(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int maxKills = 0;
        
        // 遍历每个空位
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '0') {
                    // 计算在位置(i,j)放置炸弹能炸死的敌人数量
                    int kills = calculateKills(grid, i, j);
                    maxKills = Math.max(maxKills, kills);
                }
            }
        }
        
        return maxKills;
    }
    
    /**
     * 计算在指定位置放置炸弹能炸死的敌人数量
     * 
     * @param grid 游戏网格
     * @param row 炸弹行位置
     * @param col 炸弹列位置
     * @return 能炸死的敌人数量
     */
    private int calculateKills(char[][] grid, int row, int col) {
        int m = grid.length;
        int n = grid[0].length;
        int kills = 0;
        
        // 四个方向：上、下、左、右
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            
            // 在当前方向上搜索敌人
            while (r >= 0 && r < m && c >= 0 && c < n && grid[r][c] != 'W') {
                if (grid[r][c] == 'E') {
                    kills++;
                }
                r += dir[0];
                c += dir[1];
            }
        }
        
        return kills;
    }
    
    /**
     * 方法二：预处理优化法（推荐）
     * 
     * 预先计算每个位置四个方向的敌人数量
     * 
     * @param grid 游戏网格
     * @return 一颗炸弹最多能炸死的敌人数量
     */
    public int maxKilledEnemiesOptimized(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        
        // 预处理四个方向的敌人数量
        int[][] up = new int[m][n];       // 向上方向的敌人数量
        int[][] down = new int[m][n];     // 向下方向的敌人数量
        int[][] left = new int[m][n];     // 向左方向的敌人数量
        int[][] right = new int[m][n];    // 向右方向的敌人数量
        
        // 预处理向上和向左方向
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'W') {
                    up[i][j] = 0;
                    left[i][j] = 0;
                } else {
                    // 向上方向
                    up[i][j] = (i > 0 ? up[i-1][j] : 0) + (grid[i][j] == 'E' ? 1 : 0);
                    // 向左方向
                    left[i][j] = (j > 0 ? left[i][j-1] : 0) + (grid[i][j] == 'E' ? 1 : 0);
                }
            }
        }
        
        // 预处理向下和向右方向
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (grid[i][j] == 'W') {
                    down[i][j] = 0;
                    right[i][j] = 0;
                } else {
                    // 向下方向
                    down[i][j] = (i < m - 1 ? down[i+1][j] : 0) + (grid[i][j] == 'E' ? 1 : 0);
                    // 向右方向
                    right[i][j] = (j < n - 1 ? right[i][j+1] : 0) + (grid[i][j] == 'E' ? 1 : 0);
                }
            }
        }
        
        int maxKills = 0;
        
        // 计算每个空位放置炸弹的效果
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '0') {
                    // 四个方向的敌人数量之和，但要减去当前位置的重复计算
                    int kills = getKillsInDirection(grid, up, i, j, -1, 0) +
                               getKillsInDirection(grid, down, i, j, 1, 0) +
                               getKillsInDirection(grid, left, i, j, 0, -1) +
                               getKillsInDirection(grid, right, i, j, 0, 1);
                    
                    maxKills = Math.max(maxKills, kills);
                }
            }
        }
        
        return maxKills;
    }
    
    /**
     * 获取指定方向上能炸死的敌人数量
     * 
     * @param grid 游戏网格
     * @param dirArray 方向数组
     * @param row 当前行
     * @param col 当前列
     * @param dr 行方向增量
     * @param dc 列方向增量
     * @return 该方向上能炸死的敌人数量
     */
    private int getKillsInDirection(char[][] grid, int[][] dirArray, int row, int col, int dr, int dc) {
        int m = grid.length;
        int n = grid[0].length;
        int kills = 0;
        
        int r = row + dr;
        int c = col + dc;
        
        while (r >= 0 && r < m && c >= 0 && c < n && grid[r][c] != 'W') {
            if (grid[r][c] == 'E') {
                kills++;
            }
            r += dr;
            c += dc;
        }
        
        return kills;
    }
    
    /**
     * 方法三：滚动数组优化法
     * 
     * 使用一维数组代替二维数组，节省空间
     * 
     * @param grid 游戏网格
     * @return 一颗炸弹最多能炸死的敌人数量
     */
    public int maxKilledEnemiesRolling(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int maxKills = 0;
        
        int rowKills = 0;                    // 当前行能炸死的敌人数量
        int[] colKills = new int[n];         // 每列能炸死的敌人数量
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 如果是新行的开始或者遇到墙，重新计算行杀敌数
                if (j == 0 || grid[i][j-1] == 'W') {
                    rowKills = 0;
                    for (int k = j; k < n && grid[i][k] != 'W'; k++) {
                        if (grid[i][k] == 'E') rowKills++;
                    }
                }
                
                // 如果是新列的开始或者遇到墙，重新计算列杀敌数
                if (i == 0 || grid[i-1][j] == 'W') {
                    colKills[j] = 0;
                    for (int k = i; k < m && grid[k][j] != 'W'; k++) {
                        if (grid[k][j] == 'E') colKills[j]++;
                    }
                }
                
                // 如果当前位置是空位，计算总杀敌数
                if (grid[i][j] == '0') {
                    maxKills = Math.max(maxKills, rowKills + colKills[j]);
                }
            }
        }
        
        return maxKills;
    }
    
    /**
     * 方法四：四方向分别处理
     * 
     * 分别计算每个位置四个方向的贡献
     * 
     * @param grid 游戏网格
     * @return 一颗炸弹最多能炸死的敌人数量
     */
    public int maxKilledEnemiesFourDirections(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        
        // 四个方向的敌人计数数组
        int[][] hits = new int[m][n];
        
        // 处理每一行（左右方向）
        for (int i = 0; i < m; i++) {
            // 从左到右
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'W') {
                    count = 0;
                } else {
                    if (grid[i][j] == 'E') count++;
                    hits[i][j] += count;
                }
            }
            
            // 从右到左
            count = 0;
            for (int j = n - 1; j >= 0; j--) {
                if (grid[i][j] == 'W') {
                    count = 0;
                } else {
                    if (grid[i][j] == 'E') count++;
                    hits[i][j] += count;
                    if (grid[i][j] == 'E') hits[i][j]--; // 避免重复计算当前位置的敌人
                }
            }
        }
        
        // 处理每一列（上下方向）
        for (int j = 0; j < n; j++) {
            // 从上到下
            int count = 0;
            for (int i = 0; i < m; i++) {
                if (grid[i][j] == 'W') {
                    count = 0;
                } else {
                    if (grid[i][j] == 'E') count++;
                    hits[i][j] += count;
                }
            }
            
            // 从下到上
            count = 0;
            for (int i = m - 1; i >= 0; i--) {
                if (grid[i][j] == 'W') {
                    count = 0;
                } else {
                    if (grid[i][j] == 'E') count++;
                    hits[i][j] += count;
                    if (grid[i][j] == 'E') hits[i][j]--; // 避免重复计算当前位置的敌人
                }
            }
        }
        
        // 找出空位中的最大杀敌数
        int maxKills = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '0') {
                    maxKills = Math.max(maxKills, hits[i][j]);
                }
            }
        }
        
        return maxKills;
    }
    
    /**
     * 方法五：动态规划优化版本
     * 
     * 使用动态规划思想，优化计算过程
     * 
     * @param grid 游戏网格
     * @return 一颗炸弹最多能炸死的敌人数量
     */
    public int maxKilledEnemiesDP(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int m = grid.length;
        int n = grid[0].length;
        int maxKills = 0;
        
        // 使用滚动数组优化空间
        int[] colEnemies = new int[n];  // 每列的敌人数量（向下方向）
        int rowEnemies = 0;             // 当前行的敌人数量（向右方向）
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 重新计算行敌人数量（当遇到墙或行首时）
                if (j == 0 || grid[i][j-1] == 'W') {
                    rowEnemies = calculateRowEnemies(grid, i, j);
                }
                
                // 重新计算列敌人数量（当遇到墙或列首时）
                if (i == 0 || grid[i-1][j] == 'W') {
                    colEnemies[j] = calculateColEnemies(grid, i, j);
                }
                
                // 如果是空位，更新最大杀敌数
                if (grid[i][j] == '0') {
                    maxKills = Math.max(maxKills, rowEnemies + colEnemies[j]);
                }
            }
        }
        
        return maxKills;
    }
    
    /**
     * 计算从指定位置开始的行方向敌人数量
     * 
     * @param grid 游戏网格
     * @param row 起始行
     * @param startCol 起始列
     * @return 行方向敌人数量
     */
    private int calculateRowEnemies(char[][] grid, int row, int startCol) {
        int count = 0;
        int n = grid[0].length;
        
        for (int j = startCol; j < n && grid[row][j] != 'W'; j++) {
            if (grid[row][j] == 'E') {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 计算从指定位置开始的列方向敌人数量
     * 
     * @param grid 游戏网格
     * @param startRow 起始行
     * @param col 起始列
     * @return 列方向敌人数量
     */
    private int calculateColEnemies(char[][] grid, int startRow, int col) {
        int count = 0;
        int m = grid.length;
        
        for (int i = startRow; i < m && grid[i][col] != 'W'; i++) {
            if (grid[i][col] == 'E') {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 辅助方法：打印网格（用于调试）
     * 
     * @param grid 游戏网格
     */
    private void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P361_BombEnemy solution = new P361_BombEnemy();
        
        // 测试用例1
        char[][] grid1 = {
            {'0', 'E', '0', '0'},
            {'E', '0', 'W', 'E'},
            {'0', 'E', '0', '0'}
        };
        
        System.out.println("测试用例1:");
        solution.printGrid(grid1);
        
        int result1a = solution.maxKilledEnemies(grid1);
        int result1b = solution.maxKilledEnemiesOptimized(grid1);
        int result1c = solution.maxKilledEnemiesRolling(grid1);
        int result1d = solution.maxKilledEnemiesDP(grid1);
        
        System.out.println("方法1结果: " + result1a);
        System.out.println("方法2结果: " + result1b);
        System.out.println("方法3结果: " + result1c);
        System.out.println("方法4结果: " + result1d);
        System.out.println();
        
        // 测试用例2
        char[][] grid2 = {
            {'W', 'W', 'W'},
            {'0', 'E', '0'},
            {'W', 'W', 'W'}
        };
        
        System.out.println("测试用例2:");
        solution.printGrid(grid2);
        
        int result2a = solution.maxKilledEnemies(grid2);
        int result2b = solution.maxKilledEnemiesOptimized(grid2);
        int result2c = solution.maxKilledEnemiesRolling(grid2);
        int result2d = solution.maxKilledEnemiesDP(grid2);
        
        System.out.println("方法1结果: " + result2a);
        System.out.println("方法2结果: " + result2b);
        System.out.println("方法3结果: " + result2c);
        System.out.println("方法4结果: " + result2d);
    }
} 