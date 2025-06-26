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
    private static class Dot {
    }

    private static class Node<V> {
        V val;

        public Node(V v) {
            val = v;
        }
    }

    private static class UnionFind1<V> {
        public HashMap<V, Node<V>> nodes;
        public HashMap<Node<V>, Node<V>> parents;
        public HashMap<Node<V>, Integer> sizes;

        public UnionFind1(List<V> list) {
            nodes = new HashMap<>();
            parents = new HashMap<>();
            sizes = new HashMap<>();
            for (V cur : list) {
                Node<V> node = new Node<>(cur);
                nodes.put(cur, node);
                parents.put(node, node);
                sizes.put(node, 1);
            }
        }

        public Node<V> find(Node<V> cur) {
            Stack<Node<V>> path = new Stack<>();
            while (cur != parents.get(cur)) {
                path.push(cur);
                cur = parents.get(cur);
            }
            while (!path.isEmpty()) {
                parents.put(path.pop(), cur);
            }
            return cur;
        }

        public void union(V a, V b) {
            Node<V> fa = find(nodes.get(a));
            Node<V> fb = find(nodes.get(b));
            if (fa != fb) {
                int sa = sizes.get(fa);
                int sb = sizes.get(fb);
                Node<V> big = sa >= sb ? fa : fb;
                Node<V> small = big == fa ? fb : fa;
                parents.put(small, big);
                sizes.put(big, sa + sb);
                sizes.remove(small);
            }
        }

        public int sets() {
            return sizes.size();
        }
    }

    public static int numIslands1(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        Dot[][] dots = new Dot[row][col];
        List<Dot> dotList = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] == '1') {
                    dots[i][j] = new Dot();
                    dotList.add(dots[i][j]);
                }
            }
        }
        UnionFind1<Dot> uf = new UnionFind1<>(dotList);
        for (int j = 1; j < col; j++) {
            if (board[0][j - 1] == '1' && board[0][j] == '1') {
                uf.union(dots[0][j - 1], dots[0][j]);
            }
        }
        for (int i = 1; i < row; i++) {
            if (board[i - 1][0] == '1' && board[i][0] == '1') {
                uf.union(dots[i - 1][0], dots[i][0]);
            }
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (board[i][j] == '1') {
                    if (board[i][j - 1] == '1') {
                        uf.union(dots[i][j], dots[i][j - 1]);
                    }
                    if (board[i - 1][j] == '1') {
                        uf.union(dots[i][j], dots[i - 1][j]);
                    }
                }
            }
        }
        return uf.sets();
    }

    //

    private static class UnionFind2 {
        private int[] parent;
        private int[] size;
        private int[] help;
        private int col;
        private int sets;

        private int index(int r, int c) {
            return r * col + c;
        }

        public UnionFind2(char[][] board) {
            col = board[0].length;
            sets = 0;
            int row = board.length;
            int len = row * col;

            parent = new int[len];
            size = new int[len];
            help = new int[len];
            for (int r = 0; r < row; r++) {
                for (int c = 0; c < col; c++) {
                    if (board[r][c] == '1') {
                        int i = index(r, c);
                        parent[i] = i;
                        size[i] = 1;
                        sets++;
                    }
                }
            }
        }

        public int find(int i) {
            int hi = 0;
            while (i != parent[i]) {
                help[hi++] = i;
                i = parent[i];
            }
            for (hi--; hi >= 0; hi--) {
                parent[help[hi]] = i;
            }
            return i;
        }

        public void union(int r1, int c1, int r2, int c2) {
            int i1 = index(r1, c1);
            int i2 = index(r2, c2);
            int f1 = find(i1);
            int f2 = find(i2);
            if (f1 != f2) {
                if (size[f1] >= size[f2]) {
                    size[f1] += size[f2];
                    parent[f2] = f1;
                } else {
                    size[f2] += size[f1];
                    parent[f1] = f2;
                }
                sets--;
            }
        }

        public int sets() {
            return sets;
        }
    }

    public static int numIslands2(char[][] board) {
        int row = board.length;
        int col = board[0].length;
        UnionFind2 uf = new UnionFind2(board);
        for (int j = 1; j < col; j++) {
            if (board[0][j - 1] == '1' && board[0][j] == '1') {
                uf.union(0, j - 1, 0, j);
            }
        }
        for (int i = 1; i < row; i++) {
            if (board[i - 1][0] == '1' && board[i][0] == '1') {
                uf.union(i - 1, 0, i, 0);
            }
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                if (board[i][j] == '1') {
                    if (board[i][j - 1] == '1') {
                        uf.union(i, j - 1, i, j);
                    }
                    if (board[i - 1][j] == '1') {
                        uf.union(i - 1, j, i, j);
                    }
                }
            }
        }
        return uf.sets();
    }

    //

    private static void infect(char[][] board, int i, int j) {
        if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != '1') {
            return;
        }
        board[i][j] = 0;
        infect(board, i - 1, j);
        infect(board, i + 1, j);
        infect(board, i, j - 1);
        infect(board, i, j + 1);
    }

    public static int numIslands3(char[][] board) {
        int islands = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == '1') {
                    islands++;
                    infect(board, i, j);
                }
            }
        }
        return islands;
    }

    //

    private static char[][] randomMatrix(int row, int col) {
        char[][] board = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = Math.random() < 0.5 ? '1' : '0';
            }
        }
        return board;
    }

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

    public static void main(String[] args) {
        int row = 0;
        int col = 0;
        char[][] board1 = null;
        char[][] board2 = null;
        char[][] board3 = null;
        long start = 0;
        long end = 0;

        row = 1000;
        col = 1000;
        board1 = randomMatrix(row, col);
        board2 = copy(board1);
        board3 = copy(board1);

        System.out.println("感染方法、并查集(map实现)、并查集(数组实现)的运行结果和运行时间");
        System.out.println("随机生成的二维矩阵规模 : " + row + " * " + col);

        start = System.currentTimeMillis();
        System.out.println("感染方法的运行结果: " + numIslands3(board1));
        end = System.currentTimeMillis();
        System.out.println("感染方法的运行时间: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        System.out.println("并查集(map实现)的运行结果: " + numIslands1(board2));
        end = System.currentTimeMillis();
        System.out.println("并查集(map实现)的运行时间: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        System.out.println("并查集(数组实现)的运行结果: " + numIslands2(board3));
        end = System.currentTimeMillis();
        System.out.println("并查集(数组实现)的运行时间: " + (end - start) + " ms");

        System.out.println();

        row = 10000;
        col = 10000;
        board1 = randomMatrix(row, col);
        board3 = copy(board1);
        System.out.println("感染方法、并查集(数组实现)的运行结果和运行时间");
        System.out.println("随机生成的二维矩阵规模 : " + row + " * " + col);

        start = System.currentTimeMillis();
        System.out.println("感染方法的运行结果: " + numIslands3(board1));
        end = System.currentTimeMillis();
        System.out.println("感染方法的运行时间: " + (end - start) + " ms");

        start = System.currentTimeMillis();
        System.out.println("并查集(数组实现)的运行结果: " + numIslands2(board3));
        end = System.currentTimeMillis();
        System.out.println("并查集(数组实现)的运行时间: " + (end - start) + " ms");

    }
}
