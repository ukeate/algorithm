package leetc.top;

/**
 * LeetCode 130. 被围绕的区域 (Surrounded Regions)
 * 
 * 问题描述：
 * 给你一个 m x n 的矩阵 board ，由若干字符 'X' 和 'O' ，找到所有被 'X' 围绕的区域，
 * 并将这些区域里所有的 'O' 替换为 'X' 。
 * 被围绕的区域不会存在于边界上，即任何边界上的 'O' 都不会被填充为 'X'。
 * 
 * 解法思路：
 * 反向思维 + DFS标记：
 * 1. 与边界相连的'O'不会被围绕，从边界的'O'开始DFS标记
 * 2. 将所有与边界相连的'O'标记为特殊字符'F'（Free）
 * 3. 剩余的'O'就是被围绕的区域，将其改为'X'
 * 4. 将标记的'F'还原为'O'
 * 
 * 两种实现方法：
 * 方法一：朴素DFS（solve1）
 * - 对每个'O'区域进行DFS检查是否能到达边界
 * - 时间复杂度较高，可能TLE
 * 
 * 方法二：边界DFS优化（solve2）
 * - 只从边界的'O'开始DFS，标记所有连通的'O'
 * - 时间复杂度更优，推荐使用
 * 
 * 核心思想：
 * - 被围绕 = 不与边界相连
 * - 使用DFS/BFS找连通分量
 * - 反向标记避免重复计算
 * 
 * 时间复杂度：O(m*n) - 每个格子最多访问一次
 * 空间复杂度：O(m*n) - DFS递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/surrounded-regions/
 */
public class P130_SurroundedRegions {
    
    /**
     * 方法一：朴素DFS检查每个区域是否被围绕
     * DFS过程中检查是否能到达边界，如果能到达则不被围绕
     * 
     * @param board 二维字符数组
     * @param i 当前行
     * @param j 当前列
     * @param ans 是否能到达边界的标记
     */
    private static void can(char[][] board, int i, int j, boolean[] ans) {
        // 到达边界，说明不被围绕
        if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
            ans[0] = false;
            return;
        }
        
        if (board[i][j] == 'O') {
            board[i][j] = '.';  // 标记为已访问
            // 向四个方向继续搜索
            can(board, i - 1, j, ans);
            can(board, i + 1, j, ans);
            can(board, i, j - 1, ans);
            can(board, i, j + 1, ans);
        }
    }

    /**
     * 根据DFS结果更改区域状态
     * 
     * @param board 二维字符数组
     * @param i 当前行
     * @param j 当前列  
     * @param can 'T'表示被围绕改为'X'，'F'表示不被围绕还原为'O'
     */
    private static void change(char[][] board, int i, int j, char can) {
        if (i < 0 || i == board.length || j < 0 || j == board[0].length) {
            return;
        }
        
        if (board[i][j] == '.') {
            board[i][j] = can == 'T' ? 'X' : 'O';
            // 递归处理连通区域
            change(board, i - 1, j, can);
            change(board, i + 1, j, can);
            change(board, i, j - 1, can);
            change(board, i, j + 1, can);
        }
    }

    /**
     * 方法一：朴素解法（可能TLE）
     * 
     * @param board 二维字符数组
     */
    public static void solve1(char[][] board) {
        boolean[] ans = new boolean[1];
        
        // 第一轮：检查每个'O'区域是否被围绕
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 'O') {
                    ans[0] = true;  // 假设被围绕
                    can(board, i, j, ans);
                    // 标记结果：'T'=被围绕，'F'=不被围绕
                    board[i][j] = ans[0] ? 'T' : 'F';
                }
            }
        }
        
        // 第二轮：根据标记结果修改board
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                char can = board[i][j];
                if (can == 'T' || can == 'F') {
                    board[i][j] = '.';
                    change(board, i, j, can);
                }
            }
        }
    }

    /**
     * 方法二：边界DFS优化
     * 从边界的'O'开始DFS，标记所有与边界相连的'O'为'F'
     * 
     * @param board 二维字符数组
     * @param i 当前行
     * @param j 当前列
     */
    private static void free(char[][] board, int i, int j) {
        if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != 'O') {
            return;
        }
        
        board[i][j] = 'F';  // 标记为Free（不被围绕）
        // 递归标记所有连通的'O'
        free(board, i + 1, j);
        free(board, i - 1, j);
        free(board, i, j + 1);
        free(board, i, j - 1);
    }

    /**
     * 方法二：优化解法（推荐）
     * 
     * @param board 二维字符数组
     */
    public static void solve2(char[][] board) {
        if (board == null || board.length == 0 || board[0] == null || board[0].length == 0) {
            return;
        }
        
        int n = board.length, m = board[0].length;
        
        // 处理上下边界
        for (int j = 0; j < m; j++) {
            if (board[0][j] == 'O') {          // 上边界
                free(board, 0, j);
            }
            if (board[n - 1][j] == 'O') {      // 下边界
                free(board, n - 1, j);
            }
        }
        
        // 处理左右边界（避免重复处理角落）
        for (int i = 1; i < n - 1; i++) {
            if (board[i][0] == 'O') {          // 左边界
                free(board, i, 0);
            }
            if (board[i][m - 1] == 'O') {      // 右边界
                free(board, i, m - 1);
            }
        }
        
        // 最后处理：剩余'O'改为'X'，'F'还原为'O'
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';  // 被围绕的区域
                }
                if (board[i][j] == 'F') {
                    board[i][j] = 'O';  // 与边界相连的区域
                }
            }
        }
    }
}
