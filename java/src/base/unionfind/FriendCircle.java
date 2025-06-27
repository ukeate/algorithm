package base.unionfind;

/**
 * 朋友圈问题
 * 给定一个N×N的矩阵M，表示班级中学生之间的朋友关系
 * M[i][j] = 1表示学生i和学生j是朋友，M[i][j] = 0表示不是朋友
 * 朋友关系具有传递性：如果A和B是朋友，B和C是朋友，那么A和C也在同一个朋友圈中
 * 求班级中朋友圈的总数
 * https://leetcode.com/problems/friend-circles/
 */
public class FriendCircle {
    /**
     * 计算朋友圈的数量
     * 使用并查集解决，将有朋友关系的学生合并到同一个集合中
     * @param m 朋友关系矩阵，m[i][j] = 1表示i和j是朋友
     * @return 朋友圈的数量
     */
    public static int circleNum(int[][] m) {
        int n = m.length;  // 学生数量
        UnionFind unionFind = new UnionFind(n);
        
        // 遍历矩阵的上三角部分（因为朋友关系是对称的）
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (m[i][j] == 1) {
                    // 如果i和j是朋友，将他们合并到同一个集合
                    unionFind.union(i, j);
                }
            }
        }
        return unionFind.sets();  // 返回集合数量，即朋友圈数量
    }

    /**
     * 并查集内部类实现
     * 用于维护朋友关系的连通性
     */
    private static class UnionFind {
        private int[] parent;  // 父节点数组
        private int[] size;    // 集合大小数组
        private int[] help;    // 路径压缩辅助数组
        private int sets;      // 集合数量

        /**
         * 构造函数：初始化并查集
         * @param n 元素数量
         */
        public UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            help = new int[n];
            sets = n;  // 初始时每个学生都是一个独立的朋友圈
            for (int i = 0; i < n; i++) {
                parent[i] = i;  // 每个学生的父节点是自己
                size[i] = 1;    // 每个朋友圈的大小是1
            }
        }

        /**
         * 查找元素i所在集合的根节点
         * 使用路径压缩优化
         * @param i 要查找的元素
         * @return 根节点
         */
        public int find(int i) {
            int hi = 0;
            // 找到根节点，同时记录路径
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
         * 合并两个元素所在的集合
         * 使用按大小合并优化
         * @param i 元素i
         * @param j 元素j
         */
        public void union(int i, int j) {
            int f1 = find(i);  // 找到i所在集合的根节点
            int f2 = find(j);  // 找到j所在集合的根节点
            if (f1 != f2) {    // 如果不在同一集合中
                if (size[f1] >= size[f2]) {
                    // 将小集合合并到大集合中
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
}
