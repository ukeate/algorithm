package leetc.top;

/**
 * LeetCode 79. 单词搜索 (Word Search)
 * 
 * 问题描述：
 * 给定一个 m x n 的二维字符网格 board 和一个字符串单词 word。
 * 如果 word 存在于网格中，返回 true；否则，返回 false。
 * 
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，
 * 其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母不允许被重复使用。
 * 
 * 示例：
 * board = [["A","B","C","E"],
 *          ["S","F","C","S"],
 *          ["A","D","E","E"]]
 * word = "ABCCED" → true
 * word = "SEE" → true  
 * word = "ABCB" → false
 * 
 * 解法思路：
 * 深度优先搜索 (DFS) + 回溯：
 * 1. 遍历网格的每个位置作为起点
 * 2. 从起点开始DFS搜索，尝试匹配单词的每个字符
 * 3. 搜索过程中标记已访问的位置，避免重复使用
 * 4. 如果匹配失败，回溯并恢复位置状态
 * 5. 如果完整匹配单词，返回true
 * 
 * 优化技巧：
 * - 使用原地标记：将访问过的位置临时置为特殊值（如0）
 * - 边界检查：提前判断越界和字符不匹配的情况
 * - 剪枝：不匹配时立即返回，避免无效搜索
 * 
 * 时间复杂度：O(m×n×4^k) - m×n个起点，每个位置最多4个方向，最大深度k
 * 空间复杂度：O(k) - 递归栈的深度，k为单词长度
 * 
 * LeetCode链接：https://leetcode.com/problems/word-search/
 */
public class P79_WordSearch {
    
    /**
     * DFS搜索核心方法
     * 
     * 算法流程：
     * 1. 递归终止：如果匹配完整个单词，返回true
     * 2. 边界检查：位置越界或字符不匹配，返回false
     * 3. 标记访问：将当前位置标记为已访问（置为0）
     * 4. 递归搜索：向四个方向继续匹配下一个字符
     * 5. 状态回溯：恢复当前位置的原始字符
     * 
     * @param b 字符网格
     * @param i 当前行位置
     * @param j 当前列位置
     * @param w 目标单词的字符数组
     * @param k 当前匹配到的字符索引
     * @return 从位置(i,j)开始能否匹配单词w[k...]
     */
    private static boolean process(char[][] b, int i, int j, char[] w, int k) {
        // 递归终止条件：匹配完整个单词
        if (k == w.length) {
            return true;
        }
        
        // 边界检查：位置越界
        if (i < 0 || i == b.length || j < 0 || j == b[0].length) {
            return false;
        }
        
        // 字符不匹配检查（包括已访问位置的0）
        if (b[i][j] != w[k]) {
            return false;
        }
        
        // 标记当前位置为已访问（避免重复使用）
        char tmp = b[i][j];  // 保存原始字符
        b[i][j] = 0;         // 标记为已访问
        
        // 向四个方向进行DFS搜索（上、下、左、右）
        boolean ans = process(b, i - 1, j, w, k + 1)      // 上
                   || process(b, i + 1, j, w, k + 1)      // 下
                   || process(b, i, j - 1, w, k + 1)      // 左
                   || process(b, i, j + 1, w, k + 1);     // 右
        
        // 回溯：恢复当前位置的原始字符
        b[i][j] = tmp;
        
        return ans;
    }
    
    /**
     * 判断单词是否存在于字符网格中
     * 
     * 算法流程：
     * 1. 遍历网格的每个位置作为搜索起点
     * 2. 对每个起点调用DFS搜索
     * 3. 如果任何起点能找到完整路径，返回true
     * 4. 如果所有起点都失败，返回false
     * 
     * @param board m x n 的字符网格
     * @param word 要搜索的目标单词
     * @return 单词是否存在于网格中
     */
    public boolean exist(char[][] board, String word) {
        char[] w = word.toCharArray();
        
        // 遍历网格的每个位置作为起点
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                // 从位置(i,j)开始DFS搜索
                if (process(board, i, j, w, 0)) {
                    return true;  // 找到匹配路径
                }
            }
        }
        
        return false;  // 所有起点都无法找到匹配路径
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P79_WordSearch solution = new P79_WordSearch();
        
        // 测试用例1：ABCCED存在
        char[][] board1 = {
            {'A','B','C','E'},
            {'S','F','C','S'},
            {'A','D','E','E'}
        };
        System.out.println("测试网格1:");
        printBoard(board1);
        System.out.println("搜索 'ABCCED': " + solution.exist(board1, "ABCCED"));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 测试用例2：SEE存在
        System.out.println("搜索 'SEE': " + solution.exist(board1, "SEE"));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 测试用例3：ABCB不存在（B被重复使用）
        System.out.println("搜索 'ABCB': " + solution.exist(board1, "ABCB"));
        System.out.println("期望结果: false");
        System.out.println();
        
        // 测试用例4：单字符
        char[][] board2 = {{'a'}};
        System.out.println("测试网格2: [['a']]");
        System.out.println("搜索 'a': " + solution.exist(board2, "a"));
        System.out.println("期望结果: true");
        System.out.println();
        
        // 性能测试
        char[][] largeBoard = generateLargeBoard(4, 4);
        long start = System.currentTimeMillis();
        boolean result = solution.exist(largeBoard, "ABCD");
        long end = System.currentTimeMillis();
        System.out.println("4x4网格搜索性能测试:");
        System.out.println("搜索结果: " + result);
        System.out.println("执行时间: " + (end - start) + "ms");
    }
    
    /**
     * 打印字符网格
     */
    private static void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
    
    /**
     * 生成测试用的大网格
     */
    private static char[][] generateLargeBoard(int rows, int cols) {
        char[][] board = new char[rows][cols];
        char ch = 'A';
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = (char)(ch + (i * cols + j) % 26);
            }
        }
        return board;
    }
}
