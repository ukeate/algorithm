package base.graph.o;

import java.util.ArrayList;

/**
 * 图的节点类定义
 * 
 * 设计说明：
 * 这是图算法中节点的标准表示，采用邻接表方式存储图结构。
 * 既保存了节点的基本信息，也维护了入度、出度等统计信息。
 * 
 * 数据结构特点：
 * - nexts：存储所有可直达的邻居节点（邻接表）
 * - edges：存储从该节点出发的所有边
 * - in/out：分别记录入度和出度，便于拓扑排序等算法使用
 * 
 * 适用算法：
 * - 图的遍历（DFS、BFS）
 * - 最短路径算法（Dijkstra、Bellman-Ford）
 * - 拓扑排序
 * - 网络流算法
 * - 强连通分量算法
 * 
 * 空间复杂度：O(度数) - 每个节点的空间开销与其度数成正比
 */
public class Node {
    public int val;                    // 节点值/标识
    public int in;                     // 入度：有多少边指向该节点
    public int out;                    // 出度：该节点指向多少其他节点
    public ArrayList<Node> nexts;      // 邻接表：所有可直达的邻居节点
    public ArrayList<Edge> edges;      // 从该节点出发的所有边
    
    /**
     * 构造函数
     * @param v 节点值
     */
    public Node(int v) {
        val = v;
        in = 0;
        out = 0;
        nexts = new ArrayList<>();
        edges = new ArrayList<>();
    }
}
