package base.graph;

import base.graph.o.Graph;
import base.graph.o.Node;

import java.util.*;

/**
 * 拓扑排序的BFS实现（Kahn算法）
 * 
 * 算法思想：
 * 1. 统计所有节点的入度
 * 2. 将入度为0的节点加入队列
 * 3. 从队列中取出节点，将其加入结果，并将其所有邻接节点的入度减1
 * 4. 如果某个邻接节点的入度变为0，则将其加入队列
 * 5. 重复步骤3-4，直到队列为空
 * 
 * 时间复杂度：O(V+E)
 * 空间复杂度：O(V)
 * 
 * 题目链接：https://www.lintcode.com/problem/topological-sorting
 */
public class TopologicalBFS1 {

    /**
     * 拓扑排序实现（基于图对象）
     * @param graph 有向图
     * @return 拓扑排序的结果列表
     */
    public static List<Node> topSort0(Graph graph) {
        HashMap<Node, Integer> in = new HashMap<>();  // 记录每个节点的入度
        Queue<Node> zeroIn = new LinkedList<>();      // 入度为0的节点队列
        
        // 初始化入度信息
        for (Node node : graph.nodes.values()) {
            in.put(node, node.in);
            if (node.in == 0) {
                zeroIn.add(node);  // 将入度为0的节点加入队列
            }
        }
        
        List<Node> res = new ArrayList<>();
        while (!zeroIn.isEmpty()) {
            Node cur = zeroIn.poll();  // 取出一个入度为0的节点
            res.add(cur);              // 加入结果列表
            
            // 处理当前节点的所有邻接节点
            for (Node next : cur.nexts) {
                in.put(next, in.get(next) - 1);  // 邻接节点入度减1
                if (in.get(next) == 0) {          // 如果入度变为0
                    zeroIn.add(next);             // 加入队列
                }
            }
        }
        return res;
    }

    //

    /**
     * 有向图节点类
     * 用于LintCode平台的拓扑排序题目
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
     * 拓扑排序实现（基于节点列表）
     * @param graph 有向图的节点列表
     * @return 拓扑排序的结果列表
     */
    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode, Integer> indegreeMap = new HashMap<>();
        
        // 初始化所有节点的入度为0
        for (DirectedGraphNode cur : graph) {
            indegreeMap.put(cur, 0);
        }
        
        // 计算每个节点的入度
        for (DirectedGraphNode cur : graph) {
            for (DirectedGraphNode next : cur.neighbors) {
                indegreeMap.put(next, indegreeMap.get(next) + 1);
            }
        }
        
        // 将入度为0的节点加入队列
        Queue<DirectedGraphNode> zeroQueue = new LinkedList<>();
        for (DirectedGraphNode cur : indegreeMap.keySet()) {
            if (indegreeMap.get(cur) == 0) {
                zeroQueue.add(cur);
            }
        }
        
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        while (!zeroQueue.isEmpty()) {
            DirectedGraphNode cur = zeroQueue.poll();  // 取出入度为0的节点
            ans.add(cur);                              // 加入结果列表
            
            // 处理当前节点的所有邻接节点
            for (DirectedGraphNode next : cur.neighbors) {
                indegreeMap.put(next, indegreeMap.get(next) - 1);  // 邻接节点入度减1
                if (indegreeMap.get(next) == 0) {                  // 如果入度变为0
                    zeroQueue.offer(next);                         // 加入队列
                }
            }
        }
        return ans;
    }
}
