package base.graph;

import base.graph.o.Graph;
import base.graph.o.Node;

import java.util.*;

// https://www.lintcode.com/problem/topological-sorting
public class TopologicalBFS1 {

    public static List<Node> topSort0(Graph graph) {
        HashMap<Node, Integer> in = new HashMap<>();
        Queue<Node> zeroIn = new LinkedList<>();
        for (Node node : graph.nodes.values()) {
            in.put(node, node.in);
            if (node.in == 0) {
                zeroIn.add(node);
            }
        }
        List<Node> res = new ArrayList<>();
        while (!zeroIn.isEmpty()) {
            Node cur = zeroIn.poll();
            res.add(cur);
            for (Node next : cur.nexts) {
                in.put(next, in.get(next) - 1);
                if (in.get(next) == 0) {
                    zeroIn.add(next);
                }
            }
        }
        return res;
    }

    //

    public static class DirectedGraphNode {
        public int label;
        public ArrayList<DirectedGraphNode> neighbors;

        public DirectedGraphNode(int x) {
            label = x;
            neighbors = new ArrayList<>();
        }
    }

    public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
        HashMap<DirectedGraphNode, Integer> indegreeMap = new HashMap<>();
        for (DirectedGraphNode cur : graph) {
            indegreeMap.put(cur, 0);
        }
        for (DirectedGraphNode cur : graph) {
            for (DirectedGraphNode next : cur.neighbors) {
                indegreeMap.put(next, indegreeMap.get(next) + 1);
            }
        }
        Queue<DirectedGraphNode> zeroQueue = new LinkedList<>();
        for (DirectedGraphNode cur : indegreeMap.keySet()) {
            if (indegreeMap.get(cur) == 0) {
                zeroQueue.add(cur);
            }
        }
        ArrayList<DirectedGraphNode> ans = new ArrayList<>();
        while (!zeroQueue.isEmpty()) {
            DirectedGraphNode cur = zeroQueue.poll();
            ans.add(cur);
            for (DirectedGraphNode next : cur.neighbors) {
                indegreeMap.put(next, indegreeMap.get(next) - 1);
                if (indegreeMap.get(next) == 0) {
                    zeroQueue.offer(next);
                }
            }
        }
        return ans;
    }
}
