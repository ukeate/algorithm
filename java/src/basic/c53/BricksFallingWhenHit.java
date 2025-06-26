package basic.c53;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * 打砖块掉落问题
 * 
 * 问题描述：
 * 有一个二维网格，其中1表示砖块，0表示空气。砖块可能悬挂在空中。
 * 当一个砖块被击中后，它会消失。如果砖块不再与天花板（第一行）连通，
 * 就会掉落。给定击打顺序，返回每次击打后掉落的砖块数量。
 * 
 * 算法思路：
 * 1. 逆向思维：先把所有要击打的砖块移除，然后逆序恢复
 * 2. 使用并查集维护连通性
 * 3. 跟踪与天花板连通的砖块数量
 * 4. 每次恢复砖块时，计算新增的连通砖块数量
 * 
 * 时间复杂度：O(m*n + hits.length)
 * 空间复杂度：O(m*n)
 * 
 * LeetCode: https://leetcode.com/problems/bricks-falling-when-hit/
 * 
 * @author 算法学习
 */
public class BricksFallingWhenHit {

    /**
     * 辅助类：表示砖块位置的对象
     */
    private static class Dot {
    }

    /**
     * 并查集类：维护砖块的连通性和与天花板的连接关系
     */
    private static class UnionFind {
        private int[][] grid;           // 砖块网格
        private Dot[][] dots;           // 位置对象数组
        private int n, m;               // 网格尺寸
        private int cellingAll;         // 与天花板连通的砖块总数
        private HashSet<Dot> cellingSet;       // 与天花板连通的连通分量集合
        private HashMap<Dot, Dot> fatherMap;   // 并查集父节点映射
        private HashMap<Dot, Integer> sizeMap; // 连通分量大小映射

        /**
         * 构造并查集
         * 
         * @param matrix 初始砖块矩阵
         */
        public UnionFind(int[][] matrix) {
            initSpace(matrix);
            initConnect();
        }

        /**
         * 初始化数据结构
         * 
         * @param matrix 砖块矩阵
         */
        private void initSpace(int[][] matrix) {
            grid = matrix;
            n = grid.length;
            m = grid[0].length;
            cellingAll = 0;
            dots = new Dot[n][m];
            cellingSet = new HashSet<>();
            fatherMap = new HashMap<>();
            sizeMap = new HashMap<>();
            
            // 为每个砖块创建对象并初始化并查集
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < m; col++) {
                    if (grid[row][col] == 1) {
                        Dot cur = new Dot();
                        dots[row][col] = cur;
                        fatherMap.put(cur, cur);
                        sizeMap.put(cur, 1);
                        
                        // 第一行的砖块与天花板连通
                        if (row == 0) {
                            cellingSet.add(cur);
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
         * @return 连通分量的根节点
         */
        private Dot find(int row, int col) {
            Dot cur = dots[row][col];
            Stack<Dot> stack = new Stack<>();
            
            // 找到根节点
            while (cur != fatherMap.get(cur)) {
                stack.add(cur);
                cur = fatherMap.get(cur);
            }
            
            // 路径压缩
            while (!stack.isEmpty()) {
                fatherMap.put(stack.pop(), cur);
            }
            
            return cur;
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
                Dot f1 = find(r1, c1);
                Dot f2 = find(r2, c2);
                
                if (f1 == f2) {
                    return;  // 已经在同一连通分量中
                }
                
                int s1 = sizeMap.get(f1);
                int s2 = sizeMap.get(f2);
                boolean up1 = cellingSet.contains(f1);  // f1是否与天花板连通
                boolean up2 = cellingSet.contains(f2);  // f2是否与天花板连通
                
                // 按大小合并，小的合并到大的
                if (s1 <= s2) {
                    fatherMap.put(f1, f2);
                    sizeMap.put(f2, s1 + s2);
                    
                    // 如果一个连通一个不连通，需要更新天花板连通数量
                    if (up1 ^ up2) {
                        cellingSet.add(f2);
                        cellingAll += up1 ? s2 : s1;
                    }
                } else {
                    fatherMap.put(f2, f1);
                    sizeMap.put(f1, s1 + s2);
                    
                    if (up1 ^ up2) {
                        cellingSet.add(f1);
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
        public int finger(int row, int col) {
            int pre = cellingAll;  // 恢复前的连通砖块数
            
            // 恢复砖块
            grid[row][col] = 1;
            Dot cur = new Dot();
            dots[row][col] = cur;
            
            // 如果在第一行，直接与天花板连通
            if (row == 0) {
                cellingSet.add(cur);
                cellingAll++;
            }
            
            fatherMap.put(cur, cur);
            sizeMap.put(cur, 1);
            
            // 与四个方向的邻居尝试连接
            union(row, col, row - 1, col);
            union(row, col, row + 1, col);
            union(row, col, row, col - 1);
            union(row, col, row, col + 1);
            
            int now = cellingAll;  // 恢复后的连通砖块数
            
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
     * 验证打砖块掉落算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 打砖块掉落问题测试 ===");
        
        // 测试用例
        int[][] grid = {
                {1, 0, 1, 1, 0},
                {1, 1, 0, 1, 0},
                {1, 0, 0, 1, 1},
                {1, 1, 0, 1, 0}
        };
        int[][] hits = {{1,0}, {2,3}, {0,3}};
        
        System.out.println("初始网格:");
        printGrid(grid);
        
        System.out.println("\n击打顺序:");
        for (int i = 0; i < hits.length; i++) {
            System.out.printf("第%d次击打: (%d, %d)%n", i+1, hits[i][0], hits[i][1]);
        }
        
        int[] ans = hit(grid, hits);
        
        System.out.println("\n每次击打掉落的砖块数:");
        for (int i = 0; i < ans.length; i++) {
            System.out.printf("第%d次击打掉落: %d 个砖块%n", i+1, ans[i]);
        }
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(m*n + hits.length * α(m*n))");
        System.out.println("空间复杂度: O(m*n)");
        System.out.println("核心思想: 逆向思维 + 并查集");
        System.out.println("关键技巧: 先移除所有击打点，再逆序恢复计算收益");
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
