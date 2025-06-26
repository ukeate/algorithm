package leetc.top;

/**
 * LeetCode 296. 最佳会面地点 (Best Meeting Point)
 * 
 * 问题描述：
 * 一群朋友住在不同的房子里。他们想要在某个地方聚会，使得所有人走到聚会地点的距离之和最小。
 * 给你一个 m x n 的二进制网格 grid，其中 1 表示某个朋友的家的位置。
 * 返回最小的总行走距离。
 * 
 * 注意：这里使用曼哈顿距离，即 |xi - xj| + |yi - yj|。
 * 
 * 示例：
 * 输入：grid = [[1,0,0,0,1],[0,0,0,0,0],[0,0,1,0,0]]
 * 输出：6
 * 解释：给定的点为 (0,0)，(0,4) 和 (2,2)。
 * 最优的聚会地点为 (0,2)，总距离为 2 + 2 + 2 = 6
 * 
 * 解法思路：
 * 分别处理X轴和Y轴的最佳会面点：
 * 1. 曼哈顿距离可以分解为X轴距离 + Y轴距离
 * 2. X轴和Y轴可以独立求解最优位置
 * 3. 对于一维数组，最优会面点是中位数
 * 4. 使用双指针从两端向中间计算总距离
 * 
 * 核心思想：
 * - 曼哈顿距离的可分离性：分别优化X轴和Y轴
 * - 数学性质：中位数是使总距离最小的点
 * - 双指针技巧：逐步计算距离，避免直接枚举所有点
 * 
 * 算法步骤：
 * 1. 统计每行和每列的人数
 * 2. 使用双指针从两端开始，逐步向中位数移动
 * 3. 累加每次移动的距离贡献
 * 
 * 时间复杂度：O(m × n) - 遍历整个网格
 * 空间复杂度：O(m + n) - 存储行列人数统计
 * 
 * LeetCode链接：https://leetcode.com/problems/best-meeting-point/
 */
public class P296_BestMeetingPoint {
    
    /**
     * 计算到最佳会面地点的最小总距离
     * 
     * @param grid 二进制网格，1表示朋友的家
     * @return 最小总行走距离
     */
    public static int minTotalDistance(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        int[] iOnes = new int[n], jOnes = new int[m]; // 记录每行每列的人数
        
        // 统计每行每列的人数
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (grid[i][j] == 1) {
                    iOnes[i]++;  // 第i行的人数
                    jOnes[j]++;  // 第j列的人数
                }
            }
        }
        
        int total = 0; // 总距离
        
        // 计算行方向的最小距离（X轴）
        int p = 0, q = n - 1;           // 双指针：p从上向下，q从下向上
        int pRest = 0, qRest = 0;       // 累积的人数
        while (p < q) {
            if (iOnes[p] + pRest <= iOnes[q] + qRest) {
                // 从p端移动一步的总贡献更小
                total += iOnes[p] + pRest;  // 当前行及之前的人都需要移动1步
                pRest += iOnes[p++];        // 更新累积人数
            } else {
                // 从q端移动一步的总贡献更小
                total += iOnes[q] + qRest;  // 当前行及之后的人都需要移动1步
                qRest += iOnes[q--];        // 更新累积人数
            }
        }
        
        // 计算列方向的最小距离（Y轴）
        p = 0;
        q = m - 1;           // 双指针：p从左向右，q从右向左
        pRest = 0;
        qRest = 0;       // 累积的人数
        while (p < q) {
            if (jOnes[p] + pRest <= jOnes[q] + qRest) {
                // 从p端移动一步的总贡献更小
                total += jOnes[p] + pRest;  // 当前列及之前的人都需要移动1步
                pRest += jOnes[p++];        // 更新累积人数
            } else {
                // 从q端移动一步的总贡献更小
                total += jOnes[q] + qRest;  // 当前列及之后的人都需要移动1步
                qRest += jOnes[q--];        // 更新累积人数
            }
        }
        
        return total;
    }
}
