package base.unionfind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * LeetCode 200. 岛屿数量 (Number of Islands)
 * 
 * 问题描述：
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 
 * 三种解法对比：
 * 
 * 1. 并查集(HashMap实现) - numIslands1()
 *    - 为每个'1'创建Dot对象，使用HashMap实现并查集
 *    - 遍历相邻的'1'并合并到同一集合
 *    - 时间复杂度：O(m*n*α(m*n))，α是阿克曼函数的反函数
 *    - 空间复杂度：O(m*n)
 * 
 * 2. 并查集(数组实现) - numIslands2()
 *    - 使用一维数组模拟二维坐标，数组实现并查集
 *    - 通过坐标转换函数index(r,c) = r*col + c映射二维到一维
 *    - 时间复杂度：O(m*n*α(m*n))
 *    - 空间复杂度：O(m*n)
 *    - 性能优于HashMap实现
 * 
 * 3. DFS感染法 - numIslands3()
 *    - 遍历网格，遇到'1'就启动DFS将整个岛屿标记为已访问
 *    - 每启动一次DFS就代表发现了一个新岛屿
 *    - 时间复杂度：O(m*n)
 *    - 空间复杂度：O(m*n) - 递归栈最坏情况
 *    - 会修改原始数组
 * 
 * 性能对比：
 * - 小规模数据：DFS感染法 > 并查集(数组) > 并查集(HashMap)
 * - 大规模数据：并查集(数组) ≈ DFS感染法 > 并查集(HashMap)
 * 
 * 实际应用：
 * - 图像分割中的连通区域检测
 * - 网络连通性分析
 * - 地理信息系统中的区域分析
 */
public class NumIslands {
    /**
     * 标识类，用于区分不同的陆地点
     * 每个Dot对象代表网格中的一个'1'
     */
    private static class Dot {
    }

    /**
     * 并查集节点类
     * @param <V> 节点存储的值类型
     */
    private static class Node<V> {
        V val;

        public Node(V v) {
            val = v;
        }
    }

    /**
     * 基于HashMap的并查集实现
     * 优点：代码清晰，易于理解
     * 缺点：HashMap操作有额外开销，性能较差
     * @param <V> 存储的元素类型
     */
    private static class UnionFind1<V> {
        public HashMap<V, Node<V>> nodes;           // 值到节点的映射
        public HashMap<Node<V>, Node<V>> parents;   // 节点到父节点的映射
        public HashMap<Node<V>, Integer> sizes;     // 根节点到集合大小的映射

        /**
         * 构造函数：初始化并查集
         * @param list 要加入并查集的元素列表
         */
        public UnionFind1(List<V> list) {
            nodes = new HashMap<>();
            parents = new HashMap<>();
            sizes = new HashMap<>();
            for (V cur : list) {
                Node<V> node = new Node<>(cur);
                nodes.put(cur, node);
                parents.put(node, node);    // 初始时每个节点的父节点是自己
                sizes.put(node, 1);         // 初始时每个集合大小为1
            }
        }

        /**
         * 查找操作：找到节点所在集合的根节点
         * 使用路径压缩优化，将查找路径上的所有节点直接连接到根节点
         * @param cur 要查找的节点
         * @return 根节点
         */
        public Node<V> find(Node<V> cur) {
            Stack<Node<V>> path = new Stack<>();
            // 向上查找根节点，并记录路径
            while (cur != parents.get(cur)) {
                path.push(cur);
                cur = parents.get(cur);
            }
            // 路径压缩：将路径上的所有节点直接连接到根节点
            while (!path.isEmpty()) {
                parents.put(path.pop(), cur);
            }
            return cur;
        }

        /**
         * 合并操作：将两个元素所在的集合合并
         * 使用按大小合并优化，将小集合合并到大集合中
         * @param a 元素a
         * @param b 元素b
         */
        public void union(V a, V b) {
            Node<V> fa = find(nodes.get(a));
            Node<V> fb = find(nodes.get(b));
            if (fa != fb) {  // 如果不在同一集合中
                int sa = sizes.get(fa);
                int sb = sizes.get(fb);
                // 按大小合并：将小集合合并到大集合
                Node<V> big = sa >= sb ? fa : fb;
                Node<V> small = big == fa ? fb : fa;
                parents.put(small, big);
                sizes.put(big, sa + sb);
                sizes.remove(small);  // 移除小集合的大小记录
            }
        }

        /**
         * 获取当前集合数量
         * @return 集合数量
         */
        public int sets() {
            return sizes.size();
        }
    }

    /**
     * 解法1：使用HashMap实现的并查集
     * 
     * 算法步骤：
     * 1. 为每个'1'创建Dot对象并加入并查集
     * 2. 扫描网格，对相邻的'1'进行合并操作
     * 3. 返回最终的集合数量
     * 
     * @param board 二维网格
     * @return 岛屿数量
     */
    public static int numIslands1(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        Dot[][] dots = new Dot[row][col];
        List<Dot> dotList = new ArrayList<>();
        
        // 为每个'1'创建Dot对象
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == '1') {
                    dots[i][j] = new Dot();
                    dotList.add(dots[i][j]);
                }
            }
        }
        
        UnionFind1<Dot> uf = new UnionFind1<>(dotList);
        
        // 处理第一行的水平相邻关系
        for (int j = 1; j < col; j++) {
            if (board[0][j - 1] == '1' && board[0][j] == '1') {
                uf.union(dots[0][j - 1], dots[0][j]);
            }
        }
        
        // 处理第一列的垂直相邻关系
        for (int i = 1; i < row; i++) {
            if (board[i - 1][0] == '1' && board[i][0] == '1') {
                uf.union(dots[i - 1][0], dots[i][0]);
            }
        }
        
        // 处理其余位置的相邻关系
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (board[i][j] == '1') {
                    // 检查左邻居
                    if (board[i][j - 1] == '1') {
                        uf.union(dots[i][j], dots[i][j - 1]);
                    }
                    // 检查上邻居
                    if (board[i - 1][j] == '1') {
                        uf.union(dots[i][j], dots[i - 1][j]);
                    }
                }
            }
        }
        return uf.sets();
    }

    /**
     * 基于数组的并查集实现
     * 优点：性能更好，内存使用更高效
     * 缺点：需要坐标转换，代码稍复杂
     */
    private static class UnionFind2 {
        private int[] parent;  // 父节点数组
        private int[] size;    // 集合大小数组
        private int[] help;    // 路径压缩辅助数组
        private int col;       // 网格列数
        private int sets;      // 集合数量

        /**
         * 坐标转换函数：将二维坐标转换为一维索引
         * @param r 行坐标
         * @param c 列坐标
         * @return 一维索引
         */
        private int index(int r, int c) {
            return r * col + c;
        }

        /**
         * 构造函数：根据网格初始化并查集
         * @param board 二维网格
         */
        public UnionFind2(char[][] board) {
            col = board[0].length;
            sets = 0;
            int row = board.length;
            int len = row * col;

            parent = new int[len];
            size = new int[len];
            help = new int[len];
            
            // 初始化：只为'1'的位置创建集合
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    if (board[r][c] == '1') {
                        int i = index(r, c);
                        parent[i] = i;  // 初始时父节点是自己
                        size[i] = 1;    // 初始集合大小为1
                        sets++;         // 集合数量加1
                    }
                }
            }
        }

        /**
         * 查找操作：带路径压缩的查找
         * @param i 要查找的元素索引
         * @return 根节点索引
         */
        public int find(int i) {
            int hi = 0;
            // 向上查找根节点，记录路径
            while (i != parent[i]) {
                help[hi++] = i;
                i = parent[i];
            }
            // 路径压缩
            for (hi--; hi >= 0; hi--) {
                parent[help[hi]] = i;
            }
            return i;
        }

        /**
         * 合并操作：合并两个位置所在的集合
         * @param r1 位置1的行坐标
         * @param c1 位置1的列坐标
         * @param r2 位置2的行坐标
         * @param c2 位置2的列坐标
         */
        public void union(int r1, int c1, int r2, int c2) {
            int i1 = index(r1, c1);
            int i2 = index(r2, c2);
            int f1 = find(i1);
            int f2 = find(i2);
            if (f1 != f2) {
                // 按大小合并
                if (size[f1] >= size[f2]) {
                    size[f1] += size[f2];
                    parent[f2] = f1;
                } else {
                    size[f2] += size[f1];
                    parent[f1] = f2;
                }
                sets--;  // 合并后集合数量减1
            }
        }

        /**
         * 获取当前集合数量
         * @return 集合数量
         */
        public int sets() {
            return sets;
        }
    }

    /**
     * 解法2：使用数组实现的并查集
     * 
     * 算法优化：
     * 1. 使用一维数组替代HashMap，提高性能
     * 2. 通过坐标转换函数映射二维到一维
     * 3. 只为'1'的位置创建并查集节点
     * 
     * @param board 二维网格
     * @return 岛屿数量
     */
    public static int numIslands2(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        UnionFind2 uf = new UnionFind2(board);
        
        // 合并相邻的'1'
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == '1') {
                    // 检查右邻居
                    if (j + 1 < col && board[i][j + 1] == '1') {
                        uf.union(i, j, i, j + 1);
                    }
                    // 检查下邻居
                    if (i + 1 < row && board[i + 1][j] == '1') {
                        uf.union(i, j, i + 1, j);
                    }
                }
            }
        }
        return uf.sets();
    }

    /**
     * DFS感染函数：将当前位置及其连通的所有'1'标记为已访问
     * 采用递归方式进行深度优先搜索
     * 
     * @param board 二维网格
     * @param i 当前行坐标
     * @param j 当前列坐标
     */
    private static void infect(char[][] board, int i, int j) {
        // 边界检查和状态检查
        if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != '1') {
            return;
        }
        
        board[i][j] = '2';  // 标记为已访问（使用'2'避免与'0'和'1'冲突）
        
        // 递归访问四个方向的邻居
        infect(board, i - 1, j);  // 上
        infect(board, i + 1, j);  // 下
        infect(board, i, j - 1);  // 左
        infect(board, i, j + 1);  // 右
    }

    /**
     * 解法3：DFS感染法
     * 
     * 算法思路：
     * 1. 遍历整个网格
     * 2. 遇到'1'时启动DFS，将整个连通区域标记为已访问
     * 3. 每次启动DFS就代表发现了一个新岛屿
     * 
     * 优点：
     * - 实现简单直观
     * - 时间复杂度O(m*n)，常数因子小
     * 
     * 缺点：
     * - 会修改原始数组
     * - 递归可能导致栈溢出（网格很大时）
     * 
     * @param board 二维网格（注意：此方法会修改原数组）
     * @return 岛屿数量
     */
    public static int numIslands3(char[][] board) {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == '1') {
                    count++;              // 发现新岛屿
                    infect(board, i, j);  // 感染整个岛屿
                }
            }
        }
        return count;
    }

    /**
     * 生成随机测试矩阵
     * @param row 行数
     * @param col 列数
     * @return 随机生成的二维字符数组
     */
    private static char[][] randomMatrix(int row, int col) {
        char[][] board = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = Math.random() < 0.5 ? '1' : '0';
            }
        }
        return board;
    }

    /**
     * 复制二维数组
     * @param board 原数组
     * @return 复制后的数组
     */
    private static char[][] copy(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        char[][] ans = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                ans[i][j] = board[i][j];
            }
        }
        return ans;
    }

    /**
     * 性能测试和正确性验证
     */
    public static void main(String[] args) {
        int row = 1000;
        int col = 1000;
        char[][] board1 = randomMatrix(row, col);
        char[][] board2 = copy(board1);
        char[][] board3 = copy(board1);

        System.out.println("测试开始...");
        
        // 测试三种方法的正确性和性能
        long start = System.currentTimeMillis();
        int ans1 = numIslands1(board1);
        long end = System.currentTimeMillis();
        System.out.println("方法1（HashMap并查集）: " + ans1 + ", 用时: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        int ans2 = numIslands2(board2);
        end = System.currentTimeMillis();
        System.out.println("方法2（数组并查集）: " + ans2 + ", 用时: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        int ans3 = numIslands3(board3);
        end = System.currentTimeMillis();
        System.out.println("方法3（DFS感染法）: " + ans3 + ", 用时: " + (end - start) + "ms");

        System.out.println("结果一致性检验: " + (ans1 == ans2 && ans2 == ans3 ? "通过" : "失败"));
    }
}
