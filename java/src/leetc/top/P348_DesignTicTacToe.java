package leetc.top;

/**
 * LeetCode 348. 设计井字棋 (Design Tic-Tac-Toe)
 * 
 * 问题描述：
 * 请在 n × n 的棋盘上，实现一个判定井字棋（Tic-tac-toe）胜负的神器，判断每一次玩家落子后，
 * 是否有胜出的玩家。
 * 
 * 在这个井字棋游戏中，会有 2 名玩家，他们将轮流在棋盘上放置自己的棋子。
 * 
 * 在实现这个判定器的时候，你可以假设以下这些规则：
 * 1. 每一步棋都是有效的（例如，不会在已经被占用的位置下棋）
 * 2. 一旦游戏中有一名玩家胜出的时候，游戏将不会再继续
 * 3. 一名玩家如果在同一行、同一列或者同一斜对角线上都放置了自己的棋子，那么他就获得胜利
 * 
 * 示例：
 * TicTacToe toe = new TicTacToe(3);
 * toe.move(0, 0, 1); // 返回 0 (没有人获胜)
 * |X| | |
 * | | | |    // 玩家1在(0, 0)的位置放入棋子。
 * | | | |
 * 
 * toe.move(0, 2, 2); // 返回 0 (没有人获胜)
 * |X| |O|
 * | | | |    // 玩家2在(0, 2)的位置放入棋子。
 * | | | |
 * 
 * toe.move(2, 2, 1); // 返回 0 (没有人获胜)
 * |X| |O|
 * | | | |    // 玩家1在(2, 2)的位置放入棋子。
 * | | |X|
 * 
 * toe.move(1, 1, 2); // 返回 0 (没有人获胜)
 * |X| |O|
 * | |O| |    // 玩家2在(1, 1)的位置放入棋子。
 * | | |X|
 * 
 * toe.move(2, 0, 1); // 返回 1 (玩家1获胜)
 * |X| |O|
 * | |O| |    // 玩家1在(2, 0)的位置放入棋子。
 * |X| |X|
 * 
 * 解法思路：
 * 计数优化法：
 * 
 * 1. 核心思想：
 *    - 不需要维护整个棋盘状态
 *    - 只需要统计每行、每列、两条对角线上每个玩家的棋子数量
 *    - 当某个方向上的计数达到n时，该玩家获胜
 * 
 * 2. 数据结构设计：
 *    - rows[i]：第i行的计数（正数表示玩家1，负数表示玩家2）
 *    - cols[j]：第j列的计数
 *    - diagonal：主对角线计数（左上到右下）
 *    - antiDiagonal：反对角线计数（右上到左下）
 * 
 * 3. 算法步骤：
 *    - 根据玩家编号确定计数增量（玩家1: +1, 玩家2: -1）
 *    - 更新对应行、列的计数
 *    - 如果在对角线上，更新对角线计数
 *    - 检查是否有计数的绝对值等于n，若有则该玩家获胜
 * 
 * 4. 胜利条件检查：
 *    - 行胜利：rows[row] == ±n
 *    - 列胜利：cols[col] == ±n
 *    - 主对角线胜利：diagonal == ±n (当row == col时)
 *    - 反对角线胜利：antiDiagonal == ±n (当row + col == n - 1时)
 * 
 * 核心思想：
 * - 计数统计：用计数代替棋盘状态，节省空间
 * - 正负区分：用正负数区分两个玩家，简化逻辑
 * - 即时判断：每次落子后立即检查胜负
 * 
 * 关键技巧：
 * - 空间优化：O(n)空间而非O(n²)
 * - 时间优化：O(1)时间复杂度判断胜负
 * - 对角线判断：利用坐标关系判断是否在对角线上
 * 
 * 时间复杂度：O(1) - 每次move操作的时间复杂度
 * 空间复杂度：O(n) - 需要存储n个行、n个列和2个对角线的计数
 * 
 * LeetCode链接：https://leetcode.com/problems/design-tic-tac-toe/
 */
public class P348_DesignTicTacToe {
    
    /**
     * 井字棋游戏类
     */
    class TicTacToe {
        
        private int[] rows;         // 每行的计数数组
        private int[] cols;         // 每列的计数数组
        private int diagonal;       // 主对角线计数（左上到右下）
        private int antiDiagonal;   // 反对角线计数（右上到左下）
        private int size;           // 棋盘大小

        /**
         * 构造函数：初始化 n × n 的棋盘
         * 
         * @param n 棋盘大小
         */
        public TicTacToe(int n) {
            this.size = n;
            this.rows = new int[n];
            this.cols = new int[n];
            this.diagonal = 0;
            this.antiDiagonal = 0;
        }

        /**
         * 玩家在(row, col)位置放置棋子
         * 
         * 算法流程：
         * 1. 根据玩家编号确定计数增量（玩家1: +1, 玩家2: -1）
         * 2. 更新对应行和列的计数
         * 3. 如果位置在对角线上，更新对角线计数
         * 4. 检查是否有计数达到±n，判断是否获胜
         * 
         * @param row 行坐标（0-indexed）
         * @param col 列坐标（0-indexed）
         * @param player 玩家编号（1或2）
         * @return 获胜的玩家编号，如果没有人获胜则返回0
         */
        public int move(int row, int col, int player) {
            // 确定计数增量：玩家1为+1，玩家2为-1
            int toAdd = (player == 1) ? 1 : -1;
            
            // 更新行计数
            rows[row] += toAdd;
            
            // 更新列计数
            cols[col] += toAdd;
            
            // 如果在主对角线上（row == col），更新主对角线计数
            if (row == col) {
                diagonal += toAdd;
            }
            
            // 如果在反对角线上（row + col == size - 1），更新反对角线计数
            if (row + col == size - 1) {
                antiDiagonal += toAdd;
            }
            
            // 检查当前玩家是否获胜
            // 获胜条件：任一方向的计数绝对值等于棋盘大小
            if (Math.abs(rows[row]) == size ||      // 行获胜
                Math.abs(cols[col]) == size ||      // 列获胜
                Math.abs(diagonal) == size ||       // 主对角线获胜
                Math.abs(antiDiagonal) == size) {   // 反对角线获胜
                return player;
            }
            
            return 0; // 没有人获胜
        }
    }
    
    /**
     * 另一种实现方式：使用二维数组跟踪棋盘状态
     * 
     * 这种方法更直观但空间复杂度较高
     */
    class TicTacToeWithBoard {
        
        private int[][] board;  // 棋盘状态
        private int size;       // 棋盘大小

        /**
         * 构造函数
         * 
         * @param n 棋盘大小
         */
        public TicTacToeWithBoard(int n) {
            this.size = n;
            this.board = new int[n][n];
        }

        /**
         * 玩家落子并检查胜负
         * 
         * @param row 行坐标
         * @param col 列坐标
         * @param player 玩家编号
         * @return 获胜玩家编号或0
         */
        public int move(int row, int col, int player) {
            // 放置棋子
            board[row][col] = player;
            
            // 检查是否获胜
            if (checkWin(row, col, player)) {
                return player;
            }
            
            return 0;
        }

        /**
         * 检查指定位置的落子是否导致获胜
         * 
         * @param row 行坐标
         * @param col 列坐标
         * @param player 玩家编号
         * @return 是否获胜
         */
        private boolean checkWin(int row, int col, int player) {
            // 检查行
            if (checkRow(row, player)) return true;
            
            // 检查列
            if (checkCol(col, player)) return true;
            
            // 检查主对角线
            if (row == col && checkMainDiagonal(player)) return true;
            
            // 检查反对角线
            if (row + col == size - 1 && checkAntiDiagonal(player)) return true;
            
            return false;
        }

        /**
         * 检查指定行是否被某玩家占满
         */
        private boolean checkRow(int row, int player) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] != player) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 检查指定列是否被某玩家占满
         */
        private boolean checkCol(int col, int player) {
            for (int row = 0; row < size; row++) {
                if (board[row][col] != player) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 检查主对角线是否被某玩家占满
         */
        private boolean checkMainDiagonal(int player) {
            for (int i = 0; i < size; i++) {
                if (board[i][i] != player) {
                    return false;
                }
            }
            return true;
        }

        /**
         * 检查反对角线是否被某玩家占满
         */
        private boolean checkAntiDiagonal(int player) {
            for (int i = 0; i < size; i++) {
                if (board[i][size - 1 - i] != player) {
                    return false;
                }
            }
            return true;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P348_DesignTicTacToe solution = new P348_DesignTicTacToe();
        
        // 测试用例：3x3棋盘
        TicTacToe toe = solution.new TicTacToe(3);
        
        System.out.println("玩家1在(0,0)落子: " + toe.move(0, 0, 1)); // 返回0
        System.out.println("玩家2在(0,2)落子: " + toe.move(0, 2, 2)); // 返回0
        System.out.println("玩家1在(2,2)落子: " + toe.move(2, 2, 1)); // 返回0
        System.out.println("玩家2在(1,1)落子: " + toe.move(1, 1, 2)); // 返回0
        System.out.println("玩家1在(2,0)落子: " + toe.move(2, 0, 1)); // 返回1（玩家1获胜）
        
        // 分析：玩家1在反对角线上连成了一线：(0,0) -> (1,1) -> (2,2)
        // 等等，这里有个错误，应该是 (2,0) -> (1,1) -> (0,2) 才是反对角线
        // 让我重新测试正确的情况
        
        TicTacToe toe2 = solution.new TicTacToe(3);
        System.out.println("\n重新测试:");
        System.out.println("玩家1在(0,0)落子: " + toe2.move(0, 0, 1)); // 返回0
        System.out.println("玩家2在(1,0)落子: " + toe2.move(1, 0, 2)); // 返回0  
        System.out.println("玩家1在(0,1)落子: " + toe2.move(0, 1, 1)); // 返回0
        System.out.println("玩家2在(1,1)落子: " + toe2.move(1, 1, 2)); // 返回0
        System.out.println("玩家1在(0,2)落子: " + toe2.move(0, 2, 1)); // 返回1（玩家1第一行获胜）
    }
}
