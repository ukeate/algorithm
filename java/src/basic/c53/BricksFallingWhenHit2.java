package basic.c53;

import java.util.Stack;

/**
 * 打砖块掉落问题 - 优化版本
 * 
 * 问题描述：
 * 与BricksFallingWhenHit相同的问题，但使用了优化的数据结构。
 * 用数组代替HashMap来提高性能，减少对象创建开销。
 * 
 * 优化点：
 * 1. 用一维数组代替二维Dot对象数组
 * 2. 用boolean数组代替HashSet
 * 3. 用int数组代替HashMap
 * 4. 坐标映射：(row, col) -> row * m + col
 * 
 * 时间复杂度：O(m*n + hits.length * α(m*n))
 * 空间复杂度：O(m*n)
 * 
 * @author 算法学习
 */
public class BricksFallingWhenHit2 {
    
    /**
     * 优化版并查集类
     * 使用数组代替HashMap和HashSet，提高性能
     */
    public static class UnionFind {
        private int n, m;                // 网格尺寸
        private int cellingAll;          // 与天花板连通的砖块总数
        private int[][] grid;            // 砖块网格
        private boolean[] cellingSet;    // 标记是否与天花板连通
        private int[] fatherMap;         // 并查集父节点数组
        private int[] sizeMap;           // 连通分量大小数组

        /**
         * 构造优化版并查集
         * 
         * @param matrix 初始砖块矩阵
         */
        public UnionFind(int[][] matrix) {
            initSpace(matrix);
            initConnect();
        }

        /**
         * 初始化数据结构
         * 使用一维数组优化空间和时间性能
         * 
         * @param matrix 砖块矩阵
         */
        private void initSpace(int[][] matrix) {
            grid = matrix;
            n = grid.length;
            m = grid[0].length;
            int all = n * m;  // 总的位置数
            
            cellingAll = 0;
            cellingSet = new boolean[all];     // 标记连通性
            fatherMap = new int[all];          // 父节点数组
            sizeMap = new int[all];            // 大小数组
            
            // 初始化每个砖块位置
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < m; col++) {
                    if (grid[row][col] == 1) {
                        int idx = row * m + col;  // 二维坐标映射到一维
                        fatherMap[idx] = idx;     // 初始时自己是自己的父节点
                        sizeMap[idx] = 1;         // 初始大小为1
                        
                        // 第一行的砖块与天花板连通
                        if (row == 0) {
                            cellingSet[idx] = true;
                            cellingAll++;
                        }
                    }
                }
            }
        }

        /**
         * 初始化所有砖块之间的连接关系
         */
        private void initConnect() {
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < m; col++) {
                    // 尝试与四个方向的邻居连接
                    union(row, col, row - 1, col);  // 上
                    union(row, col, row + 1, col);  // 下
                    union(row, col, row, col - 1);  // 左
                    union(row, col, row, col + 1);  // 右
                }
            }
        }

        /**
         * 查找操作：带路径压缩的并查集查找
         * 
         * @param row 行坐标
         * @param col 列坐标
         * @return 连通分量的根节点索引
         */
        private int find(int row, int col) {
            Stack<Integer> stack = new Stack<>();
            int idx = row * m + col;  // 转换为一维索引
            
            // 找到根节点
            while (idx != fatherMap[idx]) {
                stack.add(idx);
                idx = fatherMap[idx];
            }
            
            // 路径压缩：将路径上所有节点直接连到根节点
            while (!stack.isEmpty()) {
                fatherMap[stack.pop()] = idx;
            }
            
            return idx;
        }

        /**
         * 检查坐标是否有效且有砖块
         * 
         * @param row 行坐标
         * @param col 列坐标
         * @return 是否有效
         */
        private boolean valid(int row, int col) {
            return row >= 0 && row < n && col >= 0 && col < m && grid[row][col] == 1;
        }

        /**
         * 合并操作：连接两个砖块
         * 
         * @param r1 第一个砖块行坐标
         * @param c1 第一个砖块列坐标
         * @param r2 第二个砖块行坐标
         * @param c2 第二个砖块列坐标
         */
        private void union(int r1, int c1, int r2, int c2) {
            if (valid(r1, c1) && valid(r2, c2)) {
                int f1 = find(r1, c1);
                int f2 = find(r2, c2);
                
                if (f1 == f2) {
                    return;  // 已经在同一连通分量中
                }
                
                int s1 = sizeMap[f1];
                int s2 = sizeMap[f2];
                boolean up1 = cellingSet[f1];  // f1是否与天花板连通
                boolean up2 = cellingSet[f2];  // f2是否与天花板连通
                
                // 按大小合并：小的连通分量合并到大的
                if (s1 <= s2) {
                    fatherMap[f1] = f2;
                    sizeMap[f2] = s1 + s2;
                    
                    // 更新天花板连通性
                    if (up1 ^ up2) {  // 一个连通一个不连通
                        cellingSet[f2] = true;
                        cellingAll += up1 ? s2 : s1;  // 加上新连通的砖块数
                    }
                } else {
                    fatherMap[f2] = f1;
                    sizeMap[f1] = s1 + s2;
                    
                    if (up1 ^ up2) {
                        cellingSet[f1] = true;
                        cellingAll += up1 ? s2 : s1;
                    }
                }
            }
        }

        /**
         * 恢复砖块：模拟击打的逆操作
         * 
         * @param row 砖块行坐标
         * @param col 砖块列坐标
         * @return 恢复该砖块后新增的连通砖块数量
         */
        private int finger(int row, int col) {
            // 恢复砖块
            grid[row][col] = 1;
            int cur = row * m + col;
            
            // 如果在第一行，直接与天花板连通
            if (row == 0) {
                cellingSet[cur] = true;
                cellingAll++;
            }
            
            fatherMap[cur] = cur;  // 初始化为自己的父节点
            sizeMap[cur] = 1;      // 初始大小为1
            
            int pre = cellingAll;  // 记录连接前的连通砖块数
            
            // 与四个方向的邻居尝试连接
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
            
            int now = cellingAll;  // 连接后的连通砖块数
            
            // 返回新增的连通砖块数（排除恢复的砖块本身）
            return now == pre ? 0 : now - pre - 1;
        }
    }

    /**
     * 主方法：计算每次击打后掉落的砖块数
     * 
     * @param grid 初始砖块网格
     * @param hits 击打序列
     * @return 每次击打后掉落的砖块数量数组
     */
    public static int[] hit(int[][] grid, int[][] hits) {
        // 第一步：标记所有要击打的砖块为2
        for (int i = 0; i < hits.length; i++) {
            if (grid[hits[i][0]][hits[i][1]] == 1) {
                grid[hits[i][0]][hits[i][1]] = 2;
            }
        }
        
        // 第二步：基于剩余砖块构建并查集
        UnionFind unionFind = new UnionFind(grid);
        
        // 第三步：逆序恢复砖块，计算每次恢复的收益
        int[] ans = new int[hits.length];
        for (int i = hits.length - 1; i >= 0; i--) {
            // 只有原来确实有砖块的位置才需要恢复
            if (grid[hits[i][0]][hits[i][1]] == 2) {
                ans[i] = unionFind.finger(hits[i][0], hits[i][1]);
            }
        }
        
        return ans;
    }

    /**
     * 测试方法
     * 验证优化版打砖块掉落算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 打砖块掉落问题测试（优化版） ===");
        
        // 测试用例
        int[][] grid = {
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 0},
                {1, 0, 0, 1, 1},
                {1, 1, 0, 1, 0}
        };
        int[][] hits = {{1, 0}, {2, 3}, {0, 3}};
        
        System.out.println("初始网格:");
        printGrid(grid);
        
        System.out.println("\n击打顺序:");
        for (int i = 0; i < hits.length; i++) {
            System.out.printf("第%d次击打: (%d, %d)%n", i+1, hits[i][0], hits[i][1]);
        }
        
        // 性能测试
        long startTime = System.currentTimeMillis();
        int[] ans = hit(grid, hits);
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n每次击打掉落的砖块数:");
        for (int i = 0; i < ans.length; i++) {
            System.out.printf("第%d次击打掉落: %d 个砖块%n", i+1, ans[i]);
        }
        
        System.out.println("\n执行时间: " + (endTime - startTime) + "ms");
        
        System.out.println("\n=== 优化对比分析 ===");
        System.out.println("优化1: 用int数组代替HashMap<Dot, Dot>");
        System.out.println("优化2: 用boolean数组代替HashSet<Dot>");
        System.out.println("优化3: 用一维索引代替二维Dot对象");
        System.out.println("优化4: 减少对象创建和内存分配");
        System.out.println("性能提升: 显著减少内存开销和提高缓存命中率");
    }
    
    /**
     * 打印网格的辅助方法
     * 
     * @param grid 要打印的网格
     */
    private static void printGrid(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
