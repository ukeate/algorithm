package base.graph;

import base.graph.o.Edge;
import base.graph.o.Graph;
import base.graph.o.Node;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Prim {
    public static Set<Edge> primMST(Graph graph) {
        PriorityQueue<Edge> edges = new PriorityQueue<>((a, b) -> a.weight - b.weight);
        HashSet<Node> nodes = new HashSet<>();
        Set<Edge> rst = new HashSet<>();
        for (Node node : graph.nodes.values()) {
            if (nodes.contains(node)) continue;
            nodes.add(node);
            for (Edge edge : node.edges) edges.add(edge);
            while (!edges.isEmpty()) {
                Edge edge = edges.poll();
                Node node2 = edge.to;
                if (nodes.contains(node2)) continue;
                nodes.add(node2);
                rst.add(edge);
                for (Edge edge2 : node2.edges) edges.add(edge2);
            }
        }
        return rst;
    }

    //

    // graph是连通图, graph[i][j]表示点i与点j的距离, 系统最大表示无路
    public static int prim(int[][] graph) {
        int size = graph.length;
        int[] distances = new int[size];
        boolean[] visit = new boolean[size];
        visit[0] = true;
        for (int i = 0; i < size; i++) {
            distances[i] = graph[0][i];
        }
        int sum = 0;
        for (int i = 1; i < size; i++) {
            int minPath = Integer.MAX_VALUE;
            int minIdx = -1;
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] < minPath) {
                    minPath = distances[j];
                    minIdx = j;
                }
            }
            if (minIdx == -1) {
                return sum;
            }
            visit[minIdx] = true;
            sum += minPath;
            for (int j = 0; j < size; j++) {
                if (!visit[j] && distances[j] > graph[minIdx][j]) {
                    distances[j] = graph[minIdx][j];
                }
            }
        }
        return sum;
    }
}
