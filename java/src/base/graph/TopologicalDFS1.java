package base.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 拓扑排序的DFS实现（基于深度）
 * 
 * 算法思想：
 * 1. 计算每个节点的深度（从该节点出发能到达的最大深度）
 * 2. 按照深度从大到小排序，深度越大的节点在拓扑序中越靠前
 * 
 * 时间复杂度：O(V+E)
 * 空间复杂度：O(V)
 * 
 * 题目链接：https://www.lintcode.com/problem/topological-sorting
 */
public class TopologicalDFS1 {
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
     * 记录节点及其深度的类
     */
    private static class Record {
        public DirectedGraphNode node;  // 节点
        public int deep;                // 从该节点出发能到达的最大深度

        public Record(DirectedGraphNode n, int o) {
            node = n;
            deep = o;
        }
    }

    /**
     * 按深度从大到小排序的比较器
     */
    private static class Comp implements Comparator<Record> {
        @Override
        public int compare(Record o1, Record o2) {
            return o2.deep - o1.deep;  // 深度大的在前
        }
    }

    /**
     * 递归计算节点的深度
     * @param cur 当前节点
     * @param order 存储节点深度记录的映射
     * @return 节点的深度记录
     */
    private static Record deep(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> order) {
        if (order.containsKey(cur)) {
            return order.get(cur);  // 如果已计算过，直接返回
        }
        int follow = 0;
        // 计算所有邻接节点的最大深度
        for (DirectedGraphNode next : cur.neighbors) {
            follow = Math.max(follow, deep(next, order).deep);
        }
        Record ans = new Record(cur, follow + 1);  // 当前节点深度 = 最大邻接节点深度 + 1
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
        // 计算所有节点的深度
        for (DirectedGraphNode cur : graph) {
            deep(cur, order);
        }
        
        // 将记录转换为列表并排序
        ArrayList<Record> recordArr = new ArrayList<>();
        for (Record r : order.values()) {
            recordArr.add(r);
        }
        recordArr.sort(new Comp());  // 按深度从大到小排序
        
        // 提取排序后的节点
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        for (Record r : recordArr) {
            ans.add(r.node);
        }
        return ans;
    }

}
