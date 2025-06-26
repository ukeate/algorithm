package base.graph.o;

/**
 * 图的边类定义
 * 
 * 设计说明：
 * 这是图算法中边的标准表示，包含了权重和起终点信息。
 * 支持有向图和无向图的统一表示。
 * 
 * 主要用途：
 * - 最短路径算法（Dijkstra、Floyd等）
 * - 最小生成树算法（Kruskal、Prim等）
 * - 网络流算法
 * - 图的遍历和搜索
 * 
 * 注意事项：
 * - 无向图中，一条边通常需要创建两个Edge对象（双向）
 * - 权重为int类型，适用于大多数整数权重的图问题
 * - from和to字段便于快速获取边的方向信息
 */
public class Edge {
    public int weight; // 边的权重（距离、代价等）
    public Node from;  // 起始节点
    public Node to;    // 终止节点

    /**
     * 构造函数
     * @param weight 边的权重
     * @param from 起始节点  
     * @param to 终止节点
     */
    public Edge(int weight, Node from, Node to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }
}
