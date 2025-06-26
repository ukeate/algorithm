package base.graph.o;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 图的整体数据结构定义
 * 
 * 设计说明：
 * 这是一个通用的图数据结构，支持有向图和无向图。
 * 采用邻接表+哈希表的混合存储方式，兼顾了空间和时间效率。
 * 
 * 核心组件：
 * - nodes：节点ID到节点对象的映射，便于快速定位节点
 * - edges：所有边的集合，便于边相关的算法操作
 * 
 * 存储特点：
 * - 稀疏图友好：只存储实际存在的边，空间效率高
 * - 查找效率：O(1)时间复杂度定位任意节点
 * - 遍历友好：支持按节点或按边进行算法实现
 * 
 * 典型应用：
 * - 社交网络分析
 * - 路径规划系统
 * - 网络拓扑分析
 * - 依赖关系分析
 */
public class Graph {
    public HashMap<Integer, Node> nodes; // 节点ID -> 节点对象的映射
    public HashSet<Edge> edges;          // 图中所有边的集合

    /**
     * 构造空图
     */
    public Graph() {
        nodes = new HashMap<>();
        edges = new HashSet<>();
    }

    /**
     * 从边矩阵创建图的工厂方法
     * 
     * 输入格式：
     * matrix[i] = [weight, from, to] 表示一条从from到to权重为weight的边
     * 
     * 处理逻辑：
     * 1. 遍历每条边的描述
     * 2. 确保起点和终点节点存在
     * 3. 创建边对象并建立连接关系
     * 4. 更新节点的入度和出度信息
     * 
     * @param matrix 边的描述矩阵，每行格式：[权重, 起点, 终点]
     * @return 构建好的图对象
     */
    public static Graph createGraph(int[][] matrix) {
        Graph graph = new Graph();
        
        for (int i = 0; i < matrix.length; i++) {
            int weight = matrix[i][0];  // 边的权重
            int from = matrix[i][1];    // 起点ID
            int to = matrix[i][2];      // 终点ID
            
            // 确保起点节点存在
            if (!graph.nodes.containsKey(from)) {
                graph.nodes.put(from, new Node(from));
            }
            // 确保终点节点存在
            if (!graph.nodes.containsKey(to)) {
                graph.nodes.put(to, new Node(to));
            }
            
            Node fromNode = graph.nodes.get(from);
            Node toNode = graph.nodes.get(to);
            Edge newEdge = new Edge(weight, fromNode, toNode);
            
            // 建立邻接关系
            fromNode.nexts.add(toNode);
            fromNode.out++;              // 起点出度+1
            toNode.in++;                 // 终点入度+1
            fromNode.edges.add(newEdge); // 起点添加出边
            graph.edges.add(newEdge);    // 图添加边
        }
        return graph;
    }
}
