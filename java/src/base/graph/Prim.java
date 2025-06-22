package base.graph;

import base.graph.o.Edge;
import base.graph.o.Graph;
import base.graph.o.Node;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Prim算法实现 - 最小生成树算法
 * 
 * 算法思想：
 * 1. 从任意一个节点开始，将其加入已访问节点集合
 * 2. 选择连接已访问节点和未访问节点的最小权重边，将对应的未访问节点加入集合
 * 3. 重复步骤2，直到所有节点都被访问
 * 
 * 时间复杂度：O(V²) 或 O((V+E)logV) 使用优先队列优化
 * 空间复杂度：O(V+E)
 * 
 * 适用场景：稠密图（边数相对较多）
 */
public class Prim {
    /**
     * Prim算法求最小生成树（基于对象的实现）
     * @param graph 输入图
     * @return 最小生成树的边集合
     */
    public static Set<Edge> primMST(Graph graph) {
        PriorityQueue<Edge> edges = new PriorityQueue<>((a, b) -> a.weight - b.weight);
        HashSet<Node> nodes = new HashSet<>();  // 已访问的节点集合
        Set<Edge> rst = new HashSet<>();        // 结果边集合
        
        // 处理图中的每个连通分量
        for (Node node : graph.nodes.values()) {
            if (nodes.contains(node)) continue;  // 跳过已处理的节点
            
            nodes.add(node);  // 将起始节点加入已访问集合
            // 将起始节点的所有边加入优先队列
            for (Edge edge : node.edges) edges.add(edge);
            
            while (!edges.isEmpty()) {
                Edge edge = edges.poll();  // 取出权重最小的边
                Node node2 = edge.to;
                if (nodes.contains(node2)) continue;  // 如果目标节点已访问，跳过
                
                nodes.add(node2);  // 将新节点加入已访问集合
                rst.add(edge);     // 将边加入结果集合
                // 将新节点的所有边加入优先队列
                for (Edge edge2 : node2.edges) edges.add(edge2);
            }
        }
        return rst;
    }

    //

    /**
     * Prim算法求最小生成树（基于数组的实现）
     * @param graph 邻接矩阵，graph[i][j]表示点i与点j的距离，Integer.MAX_VALUE表示无路径
     * @return 最小生成树的总权重
     */
    public static int prim(int[][] graph) {
        int size = graph.length;
        int[] distances = new int[size];  // 各节点到当前生成树的最短距离
        boolean[] visit = new boolean[size];  // 标记节点是否已加入生成树
        
        visit[0] = true;  // 从节点0开始
        // 初始化各节点到节点0的距离
        for (int i = 0; i < size; i++) {
            distances[i] = graph[0][i];
        }
        
        int sum = 0;  // 最小生成树的总权重
        // 依次加入剩余的n-1个节点
        for (int i = 1; i < size; i++) {
            int minPath = Integer.MAX_VALUE;
            int minIdx = -1;
            
            // 找到距离生成树最近的未访问节点
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] < minPath) {
                    minPath = distances[j];
                    minIdx = j;
                }
            }
            
            if (minIdx == -1) {
                return sum;  // 图不连通
            }
            
            visit[minIdx] = true;  // 将该节点加入生成树
            sum += minPath;        // 累加权重
            
            // 更新其他未访问节点到生成树的距离
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] > graph[minIdx][j]) {
                    distances[j] = graph[minIdx][j];
                }
            }
        }
        return sum;
    }
}
