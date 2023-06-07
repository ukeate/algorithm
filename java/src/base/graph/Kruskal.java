package base.graph;

import base.graph.o.Edge;
import base.graph.o.Graph;
import base.graph.o.Node;

import java.util.*;

// minimum spanning tree
public class Kruskal {
    private static class UnionFind {
        private HashMap<Node, Node> father;
        private HashMap<Node, Integer> size;

        public UnionFind() {
            father = new HashMap<>();
            size = new HashMap<>();
        }

        public void makeSets(Collection<Node> nodes) {
            father.clear();
            size.clear();
            for (Node node : nodes) {
                father.put(node, node);
                size.put(node, 1);
            }
        }

        private Node find(Node n) {
            Stack<Node> path = new Stack<>();
            while (n != father.get(n)) {
                path.add(n);
                n = father.get(n);
            }
            while (!path.isEmpty()) {
                father.put(path.pop(), n);
            }
            return n;
        }

        public boolean isSameSet(Node a, Node b) {
            return find(a) == find(b);
        }

        public void union(Node a, Node b) {
            if (a == null || b == null) {
                return;
            }
            Node fa = find(a);
            Node fb = find(b);
            if (fa != fb) {
                int sa = size.get(fa);
                int sb = size.get(fb);
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

    private static class EdgeComp implements Comparator<Edge> {
        @Override
        public int compare(Edge o1, Edge o2) {
            return o1.weight - o2.weight;
        }
    }

    public static Set<Edge> kruskalMST(Graph graph) {
        UnionFind uf = new UnionFind();
        uf.makeSets(graph.nodes.values());
        PriorityQueue<Edge> queue = new PriorityQueue<>(new EdgeComp());
        for (Edge edge : graph.edges) {
            queue.add(edge);
        }
        Set<Edge> rst = new HashSet<>();
        while (!queue.isEmpty()) {
            Edge edge = queue.poll();
            if (!uf.isSameSet(edge.from, edge.to)) {
                rst.add(edge);
                uf.union(edge.from, edge.to);
            }
        }
        return rst;
    }
}
