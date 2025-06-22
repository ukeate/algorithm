package base.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 拓扑排序的DFS实现（基于节点数量）
 * 
 * 算法思想：
 * 1. 计算每个节点的后续节点总数（从该节点出发能到达的所有节点数量，包括自己）
 * 2. 按照节点数量从大到小排序，节点数量越多的在拓扑序中越靠前
 * 
 * 时间复杂度：O(V+E)
 * 空间复杂度：O(V)
 * 
 * 题目链接：https://www.lintcode.com/problem/topological-sorting
 */
public class TopologicalDFS2 {
    /**
     * 有向图节点类
     */
    public static class DirectedGraphNode {
        public int label;                                    // 节点标签
        public ArrayList<DirectedGraphNode> neighbors;       // 邻接节点列表

        public DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<>();
        }
    }

    /**
     * 记录节点及其后续节点数量的类
     */
    private static class Record {
        public DirectedGraphNode node;  // 节点
        public long nodes;              // 从该节点出发能到达的节点总数（包括自己）

        public Record(DirectedGraphNode n, long s) {
            node = n;
            nodes = s;
        }
    }

    /**
     * 按节点数量从大到小排序的比较器
     */
    private static class Comp implements Comparator<Record> {
        @Override
        public int compare(Record o1, Record o2) {
            return o1.nodes == o2.nodes ? 0 : (o1.nodes > o2.nodes ? -1 : 1);  // 节点数量多的在前
        }
    }

    /**
     * 递归计算节点的后续节点总数
     * @param cur 当前节点
     * @param order 存储节点记录的映射
     * @return 节点的记录
     */
    private static Record nodes(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> order) {
        if (order.containsKey(cur)) {
            return order.get(cur);  // 如果已计算过，直接返回
        }
        long nodes = 0;
        // 计算所有邻接节点的节点总数之和
        for (DirectedGraphNode next : cur.neighbors) {
            nodes += nodes(next, order).nodes;
        }
        Record ans = new Record(cur, nodes + 1);  // 当前节点数 = 所有邻接节点数之和 + 1（自己）
        order.put(cur, ans);
        return ans;
    }

    /**
     * 拓扑排序的主方法
     * @param graph 有向图的节点列表
     * @return 拓扑排序的结果列表
     */
    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode, Record> order = new HashMap<>();
        // 计算所有节点的后续节点数量
        for (DirectedGraphNode cur : graph) {
            nodes(cur, order);
        }
        
        // 将记录转换为列表并排序
        ArrayList<Record> recordArr = new ArrayList<>();
        for (Record r : order.values()) {
            recordArr.add(r);
        }
        recordArr.sort(new Comp());  // 按节点数量从大到小排序
        
        // 提取排序后的节点
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        for (Record r : recordArr) {
            ans.add(r.node);
        }
        return ans;
    }
}
