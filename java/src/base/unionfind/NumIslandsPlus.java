package base.unionfind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * LeetCode 305. 岛屿数量 II (Number of Islands II)
 * 
 * 问题描述：
 * 给定一个m x n的二维网格，初始时所有位置都是水('0')
 * 给定一个positions数组，每个position表示在该位置添加一块陆地('1')
 * 要求在每次添加陆地后，返回当前岛屿的总数量
 * 
 * 关键点：
 * 1. 这是一个动态问题，需要在线处理每次添加操作
 * 2. 每次添加陆地时，需要检查与相邻陆地的连通性
 * 3. 如果与已有陆地相邻，需要合并岛屿（岛屿数量可能减少）
 * 4. 重复添加同一位置的陆地不会改变岛屿数量
 * 
 * 两种解法对比：
 * 1. 数组实现的并查集 - numIslands1()
 *    - 使用二维坐标转一维索引的方式
 *    - 空间预分配，访问效率高
 *    - 适合坐标范围固定的情况
 * 
 * 2. HashMap实现的并查集 - numIslands2()
 *    - 使用字符串作为坐标标识
 *    - 动态分配空间，只为实际使用的位置分配内存
 *    - 适合坐标范围很大但实际使用稀疏的情况
 * 
 * 算法核心思想：
 * - 每次添加陆地时，检查上下左右四个方向的邻居
 * - 如果邻居也是陆地，就合并到同一个连通分量中
 * - 使用并查集维护连通分量的数量
 */
// https://leetcode.com/problems/number-of-islands-ii/
public class NumIslandsPlus {
    /**
     * 基于数组的并查集实现
     * 
     * 优点：
     * - 访问效率高，直接数组索引
     * - 空间局部性好，缓存友好
     * 
     * 缺点：
     * - 需要预分配整个网格的空间
     * - 对于稀疏的添加操作可能浪费空间
     */
    private static class UnionFind1 {
        private int[] parent;  // 父节点数组
        private int[] size;    // 集合大小数组（size[i] = 0 表示位置i没有陆地）
        private int[] help;    // 路径压缩辅助数组
        private final int row; // 网格行数
        private final int col; // 网格列数
        private int sets;      // 当前岛屿数量

        /**
         * 构造函数：初始化并查集
         * @param m 网格行数
         * @param n 网格列数
         */
        public UnionFind1(int m, int n) {
            row = m;
            col = n;
            sets = 0;
            int len = row * col;
            parent = new int[len];
            size = new int[len];
            help = new int[len];
            // 注意：初始时所有位置的size都是0，表示没有陆地
        }

        /**
         * 坐标转换：将二维坐标转换为一维索引
         * @param r 行坐标
         * @param c 列坐标
         * @return 一维索引
         */
        private int index(int r, int c) {
            return r * col + c;
        }

        /**
         * 查找操作：带路径压缩的查找
         * @param i 要查找的位置索引
         * @return 根节点索引
         */
        private int find(int i) {
            int hi = 0;
            // 向上查找根节点，记录路径
            while (i != parent[i]) {
                help[hi++] = i;
                i = parent[i];
            }
            // 路径压缩：将路径上的所有节点直接连接到根节点
            for (hi--; hi >= 0; hi--) {
                parent[help[hi]] = i;
            }
            return i;
        }

        /**
         * 合并操作：尝试合并两个位置所在的岛屿
         * @param r1 位置1的行坐标
         * @param c1 位置1的列坐标
         * @param r2 位置2的行坐标
         * @param c2 位置2的列坐标
         */
        private void union(int r1, int c1, int r2, int c2) {
            // 边界检查
            if (r1 < 0 || r1 == row || r2 < 0 || r2 == row || 
                c1 < 0 || c1 == col || c2 < 0 || c2 == col) {
                return;
            }
            
            int i1 = index(r1, c1);
            int i2 = index(r2, c2);
            
            // 检查两个位置是否都有陆地（size > 0）
            if (size[i1] == 0 || size[i2] == 0) {
                return;
            }
            
            int f1 = find(i1);
            int f2 = find(i2);
            
            // 如果不在同一个岛屿中，进行合并
            if (f1 != f2) {
                // 按大小合并：将小岛屿合并到大岛屿
                if (size[f1] >= size[f2]) {
                    size[f1] += size[f2];
                    parent[f2] = f1;
                } else {
                    size[f2] += size[f1];
                    parent[f1] = f2;
                }
                sets--;  // 合并后岛屿数量减1
            }
        }

        /**
         * 连接操作：在指定位置添加陆地
         * 
         * 算法步骤：
         * 1. 检查该位置是否已经有陆地，如果有则直接返回当前岛屿数
         * 2. 在该位置添加陆地，岛屿数+1
         * 3. 检查四个方向的邻居，如果邻居有陆地则合并
         * 4. 返回合并后的岛屿总数
         * 
         * @param r 行坐标
         * @param c 列坐标
         * @return 添加陆地后的岛屿总数
         */
        public int connect(int r, int c) {
            int i = index(r, c);
            // 如果该位置还没有陆地
            if (size[i] == 0) {
                parent[i] = i;   // 设置父节点为自己
                size[i] = 1;     // 设置岛屿大小为1
                sets++;          // 岛屿数量+1
                
                // 尝试与四个方向的邻居合并
                union(r - 1, c, r, c);  // 上
                union(r + 1, c, r, c);  // 下
                union(r, c - 1, r, c);  // 左
                union(r, c + 1, r, c);  // 右
            }
            return sets;
        }
    }

    /**
     * 解法1：使用数组实现的并查集
     * 
     * 适用场景：
     * - 网格大小固定且不太大
     * - 添加操作相对密集
     * - 对性能要求较高
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @param positions 陆地添加位置序列
     * @return 每次添加后的岛屿数量列表
     */
    public static List<Integer> numIslands1(int m, int n, int[][] positions) {
        UnionFind1 uf = new UnionFind1(m, n);
        List<Integer> ans = new ArrayList<>();
        
        // 按顺序处理每个添加操作
        for (int[] position : positions) {
            ans.add(uf.connect(position[0], position[1]));
        }
        return ans;
    }

    /**
     * 基于HashMap的并查集实现
     * 
     * 优点：
     * - 动态分配内存，只为实际使用的位置分配空间
     * - 适合坐标范围很大但实际使用稀疏的情况
     * - 理论上支持无限大的坐标范围
     * 
     * 缺点：
     * - HashMap操作有额外开销
     * - 字符串拼接和比较的成本
     */
    private static class UnionFind2 {
        private HashMap<String, String> parent;  // 坐标字符串到父节点的映射
        private HashMap<String, Integer> size;   // 坐标字符串到集合大小的映射
        private List<String> help;               // 路径压缩辅助列表
        private int sets;                        // 当前岛屿数量

        /**
         * 构造函数：初始化空的并查集
         */
        public UnionFind2() {
            parent = new HashMap<>();
            size = new HashMap<>();
            help = new LinkedList<>();
            sets = 0;
        }

        /**
         * 查找操作：带路径压缩的查找
         * @param cur 要查找的坐标字符串
         * @return 根节点的坐标字符串
         */
        private String find(String cur) {
            // 向上查找根节点，记录路径
            while (!cur.equals(parent.get(cur))) {
                help.add(cur);
                cur = parent.get(cur);
            }
            // 路径压缩
            for (String str : help) {
                parent.put(str, cur);
            }
            help.clear();
            return cur;
        }

        /**
         * 合并操作：尝试合并两个坐标所在的岛屿
         * @param s1 坐标字符串1
         * @param s2 坐标字符串2
         */
        private void union(String s1, String s2) {
            // 只有当两个坐标都存在时才进行合并
            if (parent.containsKey(s1) && parent.containsKey(s2)) {
                String f1 = find(s1);
                String f2 = find(s2);
                
                // 如果不在同一个岛屿中
                if (!f1.equals(f2)) {
                    int sz1 = size.get(f1);
                    int sz2 = size.get(f2);
                    // 按大小合并
                    String big = sz1 >= sz2 ? f1 : f2;
                    String small = big == f1 ? f2 : f1;
                    parent.put(small, big);
                    size.put(big, sz1 + sz2);
                    sets--;  // 合并后岛屿数量减1
                }
            }
        }

        /**
         * 连接操作：在指定位置添加陆地
         * @param r 行坐标
         * @param c 列坐标
         * @return 添加陆地后的岛屿总数
         */
        private int connect(int r, int c) {
            String key = r + "-" + c;  // 坐标转字符串
            
            // 如果该位置还没有陆地
            if (!parent.containsKey(key)) {
                parent.put(key, key);  // 设置父节点为自己
                size.put(key, 1);      // 设置岛屿大小为1
                sets++;                // 岛屿数量+1
                
                // 生成四个方向邻居的坐标字符串
                String up = (r - 1) + "-" + c;
                String down = (r + 1) + "-" + c;
                String left = r + "-" + (c - 1);
                String right = r + "-" + (c + 1);
                
                // 尝试与四个方向的邻居合并
                union(up, key);
                union(down, key);
                union(left, key);
                union(right, key);
            }
            return sets;
        }
    }

    /**
     * 解法2：使用HashMap实现的并查集
     * 
     * 适用场景：
     * - 网格大小很大或者坐标范围不确定
     * - 添加操作相对稀疏
     * - 内存使用优先于性能
     * 
     * @param m 网格行数（实际上在这个实现中不会用到）
     * @param n 网格列数（实际上在这个实现中不会用到）
     * @param positions 陆地添加位置序列
     * @return 每次添加后的岛屿数量列表
     */
    public static List<Integer> numIslands2(int m, int n, int[][] positions) {
        UnionFind2 uf = new UnionFind2();
        List<Integer> ans = new ArrayList<>();
        
        // 按顺序处理每个添加操作
        for (int[] position : positions) {
            ans.add(uf.connect(position[0], position[1]));
        }
        return ans;
    }
}
