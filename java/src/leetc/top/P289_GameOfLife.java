package leetc.top;

/**
 * LeetCode 289. 生命游戏 (Game of Life)
 * 
 * 问题描述：
 * 根据百度百科，生命游戏是英国数学家约翰·何顿·康威在 1970 年设计的细胞自动机。
 * 
 * 给定一个包含 m × n 个格子的面板，每一个格子都可以看成是一个细胞。
 * 每个细胞都具有一个初始状态：1 即为活细胞（live），或 0 即为死细胞（dead）。
 * 每个细胞与其八个相邻位置（水平，垂直，对角线）的细胞都遵循以下四条生存定律：
 * 
 * 1. 如果活细胞周围八个位置的活细胞数少于两个，则该位置活细胞死亡
 * 2. 如果活细胞周围八个位置有两个或三个活细胞，则该位置活细胞仍然存活
 * 3. 如果活细胞周围八个位置有超过三个活细胞，则该位置活细胞死亡
 * 4. 如果死细胞周围正好有三个活细胞，则该位置死细胞复活
 * 
 * 下一个状态是通过将上述规则同时应用于当前状态下的每个细胞所形成的，其中出生和死亡是同时发生的。
 * 给你 m x n 网格面板 board 的当前状态，返回下一个状态。
 * 
 * 进阶：
 * - 你可以使用原地算法解决本题吗？请注意，面板上所有格子需要同时被更新：
 *   你不能先更新某些格子，然后使用它们的更新后的值再更新其他格子。
 * - 本题中，我们使用二维数组来表示面板。原则上，面板是无限的，但当活细胞侵占了面板边界时会造成问题。
 *   你将如何解决这些问题？
 * 
 * 解法思路：
 * 位运算 + 原地更新：
 * 1. 由于需要原地更新，不能边计算边修改，因为会影响其他细胞的计算
 * 2. 使用位运算技巧：用每个位置的第二位记录下一状态，第一位保持当前状态
 * 3. 状态编码：
 *    - 00：当前死，下一状态死
 *    - 01：当前活，下一状态死  
 *    - 10：当前死，下一状态活
 *    - 11：当前活，下一状态活
 * 4. 遍历两次：第一次计算下一状态并用第二位记录，第二次提取第二位作为最终结果
 * 
 * 核心技巧：
 * - board[i][j] & 1：获取当前状态（第一位）
 * - board[i][j] |= 2：设置下一状态为活（第二位置1）
 * - board[i][j] >> 1：获取下一状态（右移一位，第二位变第一位）
 * 
 * 时间复杂度：O(m*n) - 需要遍历整个面板两次
 * 空间复杂度：O(1) - 原地更新，只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/game-of-life/
 */
public class P289_GameOfLife {
    
    /**
     * 检查指定位置是否为有效的活细胞
     * 
     * @param board 面板
     * @param i 行坐标
     * @param j 列坐标
     * @return 是否为边界内的活细胞
     */
    private static boolean ok(int[][] board, int i, int j) {
        // 检查边界 && 检查当前状态是否为活细胞（第一位为1）
        return i >= 0 && i < board.length && j >= 0 && j < board[0].length && (board[i][j] & 1) == 1;
    }

    /**
     * 计算指定位置周围八个方向的活细胞数量
     * 
     * @param board 面板
     * @param i 行坐标
     * @param j 列坐标
     * @return 周围活细胞的数量
     */
    private static int neighbors(int[][] board, int i, int j) {
        int count = 0;
        // 检查周围8个方向的细胞状态
        count += ok(board, i - 1, j - 1) ? 1 : 0;  // 左上
        count += ok(board, i - 1, j) ? 1 : 0;      // 上
        count += ok(board, i - 1, j + 1) ? 1 : 0;  // 右上
        count += ok(board, i, j - 1) ? 1 : 0;      // 左
        count += ok(board, i, j + 1) ? 1 : 0;      // 右
        count += ok(board, i + 1, j - 1) ? 1 : 0;  // 左下
        count += ok(board, i + 1, j) ? 1 : 0;      // 下
        count += ok(board, i + 1, j + 1) ? 1 : 0;  // 右下
        return count;
    }

    /**
     * 设置下一状态为活细胞（将第二位置1）
     * 
     * @param board 面板
     * @param i 行坐标  
     * @param j 列坐标
     */
    private static void set(int[][] board, int i, int j) {
        board[i][j] |= 2;  // 将第二位置1，表示下一状态为活
    }

    /**
     * 获取下一状态（提取第二位）
     * 
     * @param board 面板
     * @param i 行坐标
     * @param j 列坐标
     * @return 下一状态（0或1）
     */
    private static int get(int[][] board, int i, int j) {
        return board[i][j] >> 1;  // 右移一位，获取第二位的值
    }

    /**
     * 生命游戏主函数：计算下一个状态
     * 
     * 算法步骤：
     * 1. 第一次遍历：根据生存规则计算每个细胞的下一状态
     *    - 统计周围活细胞数量
     *    - 应用生存规则，将下一状态记录在第二位
     * 2. 第二次遍历：提取下一状态并更新为最终结果
     * 
     * 生存规则总结：
     * - 活细胞周围有2或3个活细胞：继续存活
     * - 死细胞周围正好有3个活细胞：复活
     * - 其他情况：死亡或保持死亡
     * 
     * @param board m x n 的面板，原地修改
     */
    public static void gameOfLife(int[][] board) {
        int n = board.length;      // 行数
        int m = board[0].length;   // 列数
        
        // 第一次遍历：计算下一状态并记录在第二位
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int neighbors = neighbors(board, i, j);  // 统计周围活细胞数量
                
                // 应用生存规则
                if (neighbors == 3 ||                     // 规则4：死细胞周围正好3个活细胞→复活
                    (board[i][j] == 1 && neighbors == 2)) { // 规则2：活细胞周围2个活细胞→存活
                    set(board, i, j);  // 设置下一状态为活
                }
                // 其他情况默认为死亡（第二位保持0）
            }
        }
        
        // 第二次遍历：提取下一状态作为最终结果
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j] = get(board, i, j);  // 用下一状态替换当前状态
            }
        }
    }
}
