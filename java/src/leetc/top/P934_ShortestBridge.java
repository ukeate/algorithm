package leetc.top;

/**
 * LeetCode 934. 最短的桥 (Shortest Bridge)
 * 
 * 问题描述：
 * 给你一个大小为 n x n 的二元矩阵 grid，其中 1 表示陆地，0 表示水域。
 * 岛是由四面相连的 1 形成的一个最大组。现在恰好有两座岛，
 * 你需要找到这两座岛之间最短的桥。桥是可以在水域中任意方向连接两座岛的 1 的序列。
 * 
 * 返回必须翻转的 0 的最小数目，以连接两座岛。
 * 
 * 示例：
 * 输入：grid = [[0,1],[1,0]]
 * 输出：1
 * 解释：我们可以将一个 0 翻转为 1，连接两座岛。
 * 
 * 输入：grid = [[0,1,0],[0,0,0],[0,0,1]]
 * 输出：2
 * 解释：我们可以将 [1,1] 和 [1,2] 翻转为 1，连接两座岛。
 * 
 * 解法思路：
 * 多源BFS + 距离计算：
 * 1. 通过DFS标记两个岛屿，分别编号为岛屿0和岛屿1
 * 2. 对每个岛屿进行多源BFS，计算岛屿到所有位置的最短距离
 * 3. 遍历所有位置，找到 distance[岛屿0][位置] + distance[岛屿1][位置] 的最小值
 * 4. 最小值减去3就是答案（因为距离包含了两个岛屿的起始位置和连接位置）
 * 
 * 核心思想：
 * - 将问题转化为两个岛屿之间的最短路径问题
 * - 使用多源BFS同时从整个岛屿出发，而不是单点出发
 * - 通过距离叠加找到最优连接点
 * 
 * 算法步骤：
 * 1. 遍历矩阵，找到两个岛屿并分别标记
 * 2. 对每个岛屿进行多源BFS，记录到各位置的距离
 * 3. 计算所有位置的距离和，找到最小值
 * 4. 返回最小距离减去3（去除起始和终止位置的计数）
 * 
 * 时间复杂度：O(n^2) - 需要遍历整个矩阵，BFS覆盖所有位置
 * 空间复杂度：O(n^2) - 需要存储距离信息和BFS队列
 * 
 * LeetCode链接：https://leetcode.com/problems/shortest-bridge/
 */
public class P934_ShortestBridge {
    
    /**
     * DFS感染函数：将连通的岛屿标记为同一个岛屿，并收集岛屿的所有位置
     * 
     * @param m 二维矩阵
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @param ln 矩阵行数
     * @param lm 矩阵列数
     * @param curs 存储岛屿位置的队列数组
     * @param idx 队列的当前索引
     * @param record 记录岛屿位置的标记数组
     * @return 更新后的队列索引
     */
    private static int infect(int[][] m, int i, int j, int ln, int lm, int[] curs, int idx, int[] record) {
        // 边界检查：越界或不是陆地（1），直接返回
        if (i < 0 || i == ln || j < 0 || j == lm || m[i][j] != 1) {
            return idx;
        }
        
        // 将当前陆地标记为已访问（设为2）
        m[i][j] = 2;
        
        // 将二维坐标转换为一维坐标
        int p = i * lm + j;  // 注意这里应该是 i * lm + j
        record[p] = 1;       // 标记该位置属于当前岛屿
        curs[idx++] = p;     // 将位置加入队列
        
        // 递归处理四个方向的相邻位置
        idx = infect(m, i - 1, j, ln, lm, curs, idx, record);  // 上
        idx = infect(m, i + 1, j, ln, lm, curs, idx, record);  // 下
        idx = infect(m, i, j - 1, ln, lm, curs, idx, record);  // 左
        idx = infect(m, i, j + 1, ln, lm, curs, idx, record);  // 右
        
        return idx;
    }

    /**
     * BFS扩展函数：从当前队列的所有位置向外扩展一层
     * 
     * @param ln 矩阵行数
     * @param lm 矩阵列数
     * @param all 总位置数 (ln * lm)
     * @param v 当前扩展的距离值
     * @param curs 当前层的位置队列
     * @param size 当前层的队列大小
     * @param nexts 下一层的位置队列
     * @param record 距离记录数组
     * @return 下一层队列的大小
     */
    private static int bfs(int ln, int lm, int all, int v, int[] curs, int size, int[] nexts, int[] record) {
        int nexti = 0;  // 下一层队列的索引
        
        // 遍历当前层的所有位置
        for (int i = 0; i < size; i++) {
            // 计算四个方向的邻接位置（一维坐标）
            int up = curs[i] < lm ? -1 : curs[i] - lm;              // 上方位置
            int down = curs[i] + lm >= all ? -1 : curs[i] + lm;     // 下方位置
            int left = curs[i] % lm == 0 ? -1 : curs[i] - 1;        // 左方位置
            int right = curs[i] % lm == lm - 1 ? -1 : curs[i] + 1;  // 右方位置
            
            // 检查上方位置
            if (up != -1 && record[up] == 0) {
                record[up] = v;        // 记录距离
                nexts[nexti++] = up;   // 加入下一层队列
            }
            
            // 检查下方位置
            if (down != -1 && record[down] == 0) {
                record[down] = v;
                nexts[nexti++] = down;
            }
            
            // 检查左方位置
            if (left != -1 && record[left] == 0) {
                record[left] = v;
                nexts[nexti++] = left;
            }
            
            // 检查右方位置
            if (right != -1 && record[right] == 0) {
                record[right] = v;
                nexts[nexti++] = right;
            }
        }
        
        return nexti;  // 返回下一层队列的大小
    }

    /**
     * 计算连接两个岛屿的最短桥长度
     * 
     * 算法流程：
     * 1. 遍历矩阵找到两个岛屿，分别进行DFS标记
     * 2. 对每个岛屿进行多源BFS，计算到所有位置的距离
     * 3. 找到使两个岛屿距离和最小的位置
     * 4. 返回最小距离减去3（去除起始位置计数）
     * 
     * @param m 二维矩阵，1表示陆地，0表示水域
     * @return 连接两个岛屿需要翻转的最少0的数量
     */
    public static int shortestBridge(int[][] m) {
        int ln = m.length, lm = m[0].length;  // 矩阵的行数和列数
        int all = ln * lm;                    // 总位置数
        int island = 0;                       // 岛屿计数
        
        // 队列：存储当前层和下一层的位置
        int[] curs = new int[all];   // 当前层队列
        int[] nexts = new int[all];  // 下一层队列
        
        // 二维数组：[岛屿编号][位置] = 距离值
        int[][] records = new int[2][all];
        
        // 遍历矩阵，找到两个岛屿
        for (int i = 0; i < ln; i++) {
            for (int j = 0; j < lm; j++) {
                if (m[i][j] == 1) {
                    // 找到未标记的陆地，开始DFS标记整个岛屿
                    int queueSize = infect(m, i, j, ln, lm, curs, 0, records[island]);
                    
                    // 从岛屿开始进行多源BFS
                    int v = 1;  // 距离值从1开始
                    while (queueSize != 0) {
                        v++;  // 距离值递增
                        // BFS扩展一层
                        queueSize = bfs(ln, lm, all, v, curs, queueSize, nexts, records[island]);
                        
                        // 交换当前层和下一层队列
                        int[] tmp = curs;
                        curs = nexts;
                        nexts = tmp;
                    }
                    
                    island++;  // 岛屿计数递增
                }
            }
        }
        
        // 寻找两个岛屿距离和的最小值
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < all; i++) {
            min = Math.min(min, records[0][i] + records[1][i]);
        }
        
        // 返回结果：最小距离减去3
        // 减去3的原因：
        // - 岛屿0的起始位置计数为1
        // - 岛屿1的起始位置计数为1  
        // - 连接点被计算了两次，需要减去1
        // 所以总共减去 1 + 1 + 1 = 3
        return min - 3;
    }
}
