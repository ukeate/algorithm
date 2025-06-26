package leetc.top;

/**
 * LeetCode 200. 岛屿数量 (Number of Islands)
 * 
 * 问题描述：
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 此外，你可以假设该网格的四条边均被水包围。
 * 
 * 示例：
 * 输入：grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]
 * 输出：1
 * 
 * 解法思路：
 * 深度优先搜索（DFS）+ 感染算法：
 * 1. 遍历整个二维网格
 * 2. 当遇到陆地'1'时，岛屿数量+1，并开始DFS感染
 * 3. 感染过程：将当前陆地标记为'2'（已访问），然后递归感染四个方向的相邻陆地
 * 4. 感染结束后，整个岛屿都被标记为'2'，不会被重复计算
 * 
 * 核心思想：
 * - 利用DFS将连通的陆地区域一次性全部标记
 * - 通过修改原数组避免使用额外的visited数组
 * - 每次DFS代表发现了一个完整的岛屿
 * 
 * 算法特点：
 * - 感染算法：从一个点开始，将连通区域全部"感染"
 * - 原地修改：直接修改原数组，节省空间
 * - 边界检查：避免数组越界和重复访问
 * 
 * 时间复杂度：O(m*n) - 每个格子最多被访问一次
 * 空间复杂度：O(m*n) - 最坏情况下递归栈深度为所有格子
 * 
 * LeetCode链接：https://leetcode.com/problems/number-of-islands/
 */
public class P200_NumberOfIslands {
    
    /**
     * DFS感染函数：将连通的陆地区域全部标记为已访问
     * 
     * @param m 二维网格
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @param ln 网格行数
     * @param lm 网格列数
     */
    private static void infect(char[][] m, int i, int j, int ln, int lm) {
        // 边界检查：越界或不是陆地，直接返回
        if (i < 0 || i >= ln || j < 0 || j >= lm || m[i][j] != '1') {
            return;
        }
        
        // 将当前陆地标记为已访问（'2'）
        m[i][j] = '2';
        
        // 递归感染四个方向的相邻格子
        infect(m, i + 1, j, ln, lm);  // 下
        infect(m, i - 1, j, ln, lm);  // 上
        infect(m, i, j + 1, ln, lm);  // 右
        infect(m, i, j - 1, ln, lm);  // 左
    }

    /**
     * 计算岛屿数量的主函数
     * 
     * @param m 二维字符网格，'1'表示陆地，'0'表示水
     * @return 岛屿数量
     */
    public static int numIslands(char[][] m) {
        // 边界检查：空网格
        if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
            return 0;
        }
        
        int ln = m.length;      // 网格行数
        int lm = m[0].length;   // 网格列数
        int res = 0;            // 岛屿数量计数器
        
        // 遍历整个网格
        for (int i = 0; i < ln; i++) {
            for (int j = 0; j < lm; j++) {
                // 发现新的陆地（未访问的'1'）
                if (m[i][j] == '1') {
                    res++;  // 岛屿数量+1
                    infect(m, i, j, ln, lm);  // DFS感染整个岛屿
                }
            }
        }
        
        return res;
    }
}
