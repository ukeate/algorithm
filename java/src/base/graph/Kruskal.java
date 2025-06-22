package base.graph;

import base.graph.o.Edge;
import base.graph.o.Graph;
import base.graph.o.Node;

import java.util.*;

/**
 * Kruskal算法实现 - 最小生成树算法
 * 
 * 算法思想：
 * 1. 将图中所有边按权重从小到大排序
 * 2. 依次选择权重最小的边，如果该边连接的两个节点不在同一连通分量中，则加入最小生成树
 * 3. 重复步骤2，直到生成树包含n-1条边（n为节点数）
 * 
 * 时间复杂度：O(ElogE)，主要消耗在边的排序上
 * 空间复杂度：O(V)，用于并查集
 * 
 * 适用场景：稀疏图（边数相对较少）
 */
public class Kruskal {
    /**
     * 并查集数据结构
     * 用于高效地判断两个节点是否属于同一连通分量，以及合并两个连通分量
     */
    private static class UnionFind {
        private HashMap<Node, Node> father;     // 父节点映射，用于路径压缩
        private HashMap<Node, Integer> size;    // 集合大小映射，用于按秩合并

        public UnionFind() {
            father = new HashMap<>();
            size = new HashMap<>();
        }

        /**
         * 初始化并查集，将每个节点设为独立的集合
         * @param nodes 所有节点的集合
         */
        public void makeSets(Collection<Node> nodes) {
            father.clear();
            size.clear();
            for (Node node : nodes) {
                father.put(node, node);  // 每个节点的父节点初始化为自己
                size.put(node, 1);       // 每个集合的大小初始化为1
            }
        }

        /**
         * 查找节点的根节点（代表元素），使用路径压缩优化
         * @param n 要查找的节点
         * @return 节点所在集合的根节点
         */
        private Node find(Node n) {
            Stack<Node> path = new Stack<>();
            // 沿着父节点链向上找到根节点
            while (n != father.get(n)) {
                path.add(n);
                n = father.get(n);
            }
            // 路径压缩：将路径上的所有节点直接指向根节点
            while (!path.isEmpty()) {
                father.put(path.pop(), n);
            }
            return n;
        }

        /**
         * 判断两个节点是否属于同一个集合
         * @param a 节点a
         * @param b 节点b
         * @return 如果两个节点属于同一集合返回true，否则返回false
         */
        public boolean isSameSet(Node a, Node b) {
            return find(a) == find(b);
        }

        /**
         * 合并两个节点所在的集合（按秩合并优化）
         * @param a 节点a
         * @param b 节点b
         */
        public void union(Node a, Node b) {
            if (a == null || b == null) {
                return;
            }
            Node fa = find(a);  // 找到a的根节点
            Node fb = find(b);  // 找到b的根节点
            if (fa != fb) {     // 如果不在同一集合中
                int sa = size.get(fa);
                int sb = size.get(fb);
                // 按秩合并：将小集合合并到大集合中，保持树的平衡
                if (sa >= sb) {
                    father.put(fb, fa);
                    size.put(fa, sa + sb);
                    size.remove(fb);
                } else {
                    father.put(fa, fb);
                    size.put(fb, sa + sb);
                    size.remove(fa);
                }
            }
        }
    }

    /**
     * 边的比较器，用于按权重从小到大排序
     */
    private static class EdgeComp implements Comparator<Edge> {
        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.weight - o2.weight;  // 按权重升序排列
        }
    }

    /**
     * Kruskal算法求最小生成树
     * @param graph 输入图
     * @return 最小生成树的边集合
     */
    public static Set<Edge> kruskalMST(Graph graph) {
        // 初始化并查集
        UnionFind uf = new UnionFind();
        uf.makeSets(graph.nodes.values());
        
        // 将所有边按权重排序
        PriorityQueue<Edge> queue = new PriorityQueue<>(new EdgeComp());
        for (Edge edge : graph.edges) {
            queue.add(edge);
        }
        
        Set<Edge> rst = new HashSet<>();
        // 依次处理每条边
        while (!queue.isEmpty()) {
            Edge edge = queue.poll();
            // 如果边的两个端点不在同一连通分量中，则加入最小生成树
            if (!uf.isSameSet(edge.from, edge.to)) {
                rst.add(edge);
                uf.union(edge.from, edge.to);
            }
        }
        return rst;
    }
}
