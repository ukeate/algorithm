package leetc.top;

/**
 * LeetCode 37. 解数独
 * https://leetcode.cn/problems/sudoku-solver/
 * 
 * 问题描述：
 * 编写一个程序，通过填充空格来解决数独问题。
 * 数独的解法需 遵循如下规则：
 * 1. 数字 1-9 在每一行只能出现一次。
 * 2. 数字 1-9 在每一列只能出现一次。
 * 3. 数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。
 * 数独部分空格内已填入了数字，空白格用 '.' 表示。
 * 
 * 解题思路：
 * 使用回溯算法（深度优先搜索 + 剪枝）来解决数独问题
 * 
 * 核心思想：
 * 1. 使用三个二维数组分别记录每行、每列、每个3x3宫格中数字的使用情况
 * 2. 按行列顺序遍历每个空格，尝试填入1-9的数字
 * 3. 对于每个数字，检查是否违反数独规则
 * 4. 如果不违反，继续递归求解下一个位置
 * 5. 如果违反或者后续无解，则回溯，尝试下一个数字
 * 
 * 优化策略：
 * 1. 使用boolean数组快速检查数字是否可用
 * 2. 使用位运算计算3x3宫格的索引
 * 3. 按行列顺序遍历，保证了搜索的系统性
 * 
 * 时间复杂度：O(9^(空格数))，最坏情况下需要尝试所有可能的组合
 * 空间复杂度：O(1)，除了递归栈外，只使用了固定大小的标记数组
 */
public class P37_SudokuSolver {
    
    /**
     * 初始化标记数组，记录每行、每列、每个3x3宫格中已使用的数字
     * 
     * @param board 数独面板
     * @param row 行标记数组，row[i][num]表示第i行是否使用了数字num
     * @param col 列标记数组，col[j][num]表示第j列是否使用了数字num
     * @param bucket 宫格标记数组，bucket[bid][num]表示第bid个宫格是否使用了数字num
     */
    private static void initMaps(char[][] board, boolean[][] row, boolean[][] col, boolean[][] bucket) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                // 计算当前位置属于哪个3x3宫格
                // 宫格编号：0-8，按行列顺序编号
                int bid = 3 * (i / 3) + (j / 3);
                
                // 跳过空格
                if (board[i][j] == '.') {
                    continue;
                }
                
                // 记录已填入的数字
                int num = board[i][j] - '0';
                row[i][num] = true;     // 标记第i行使用了数字num
                col[j][num] = true;     // 标记第j列使用了数字num
                bucket[bid][num] = true; // 标记第bid个宫格使用了数字num
            }
        }
    }

    /**
     * 使用回溯算法递归求解数独
     * 
     * @param board 数独面板
     * @param i 当前处理的行
     * @param j 当前处理的列
     * @param row 行标记数组
     * @param col 列标记数组
     * @param bucket 宫格标记数组
     * @return 是否成功求解
     */
    private static boolean process(char[][] board, int i, int j, boolean[][] row, boolean[][] col, boolean[][] bucket) {
        // 基础情况：已经处理完所有行，求解成功
        if (i == 9) {
            return true;
        }
        
        // 计算下一个位置的坐标
        int nexti = j != 8 ? i : i + 1;     // 如果不是行末，行不变；否则下一行
        int nextj = j != 8 ? j + 1 : 0;     // 如果不是行末，列+1；否则回到第0列
        
        // 如果当前位置已经有数字，直接处理下一个位置
        if (board[i][j] != '.') {
            return process(board, nexti, nextj, row, col, bucket);
        } else {
            // 当前位置是空格，需要填入数字
            int bid = 3 * (i / 3) + (j / 3); // 计算当前位置的宫格编号
            
            // 尝试填入数字1-9
            for (int num = 1; num <= 9; num++) {
                // 检查当前数字是否可以填入（不违反数独规则）
                if (row[i][num] || col[j][num] || bucket[bid][num]) {
                    continue; // 如果违反规则，跳过这个数字
                }
                
                // 填入数字并更新标记数组
                row[i][num] = true;
                col[j][num] = true;
                bucket[bid][num] = true;
                board[i][j] = (char) (num + '0');
                
                // 递归处理下一个位置
                if (process(board, nexti, nextj, row, col, bucket)) {
                    return true; // 如果后续可以成功求解，返回true
                }
                
                // 回溯：撤销当前尝试
                row[i][num] = false;
                col[j][num] = false;
                bucket[bid][num] = false;
                board[i][j] = '.';
            }
        }
        
        // 所有数字都尝试过了，仍然无解
        return false;
    }

    /**
     * 解数独的主方法
     * 
     * @param board 待解决的数独面板，解完后直接修改原数组
     */
    public static void solveSudoku(char[][] board) {
        // 创建标记数组
        boolean[][] row = new boolean[9][10];    // row[i][num]：第i行是否使用了数字num
        boolean[][] col = new boolean[9][10];    // col[j][num]：第j列是否使用了数字num
        boolean[][] bucket = new boolean[9][10]; // bucket[bid][num]：第bid个宫格是否使用了数字num
        
        // 初始化标记数组
        initMaps(board, row, col, bucket);
        
        // 开始递归求解，从位置(0,0)开始
        process(board, 0, 0, row, col, bucket);
    }
}
