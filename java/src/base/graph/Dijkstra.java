package base.graph;

import base.graph.o.Edge;
import base.graph.o.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Dijkstra {
    private static Node minNode(HashMap<Node, Integer> distances, HashSet<Node> touched) {
        Node minNode = null;
        int minDistance = Integer.MAX_VALUE;
        for (Map.Entry<Node, Integer> entry : distances.entrySet()) {
            Node node = entry.getKey();
            int distance = entry.getValue();
            if (!touched.contains(node) && distance < minDistance) {
                minNode = node;
                minDistance = distance;
            }
        }
        return minNode;
    }

    public static HashMap<Node, Integer> dijkstra1(Node from) {
        HashMap<Node, Integer> distances = new HashMap<>();
        distances.put(from, 0);
        HashSet<Node> touched = new HashSet<>();
        Node minNode = minNode(distances, touched);
        while (minNode != null) {
            int distance = distances.get(minNode);
            for (Edge edge : minNode.edges) {
                Node to = edge.to;
                if (!distances.containsKey(to)) {
                    distances.put(to, distance + edge.weight);
                } else {
                    distances.put(to, Math.min(distances.get(to), distance + edge.weight));
                }
            }
            touched.add(minNode);
            minNode = minNode(distances, touched);
        }
        return distances;
    }

    //

    private static class NodeRecord {
        public Node node;
        public int distance;

        public NodeRecord(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    private static class Heap {
        private Node[] nodes;
        private HashMap<Node, Integer> idxMap;
        private HashMap<Node, Integer> distanceMap;
        private int size;

        public Heap(int size) {
            nodes = new Node[size];
            idxMap = new HashMap<>();
            distanceMap = new HashMap<>();
        }

        public boolean isEmpty() {
            return size == 0;
        }

        private boolean isEntered(Node node) {
            return idxMap.containsKey(node);
        }

        private boolean isIn(Node node) {
            return isEntered(node) && idxMap.get(node) != -1;
        }

        private void swap(int i1, int i2) {
            idxMap.put(nodes[i1], i2);
            idxMap.put(nodes[i2], i1);
            Node tmp = nodes[i1];
            nodes[i1] = nodes[i2];
            nodes[i2] = tmp;
        }

        private void heapInsert(int idx) {
            while (distanceMap.get(nodes[idx]) < distanceMap.get(nodes[(idx - 1) / 2])) {
                swap(idx, (idx - 1) / 2);
                idx = (idx - 1) / 2;
            }
        }

        private void heapify(int idx, int size) {
            int left = idx * 2 + 1;
            while (left < size) {
                int small = left + 1 < size && distanceMap.get(nodes[left + 1]) < distanceMap.get(nodes[left]) ? left + 1 : left;
                if (distanceMap.get(nodes[idx]) < distanceMap.get(nodes[small])) {
                    break;
                }
                swap(small, idx);
                idx = small;
                left = idx * 2 + 1;
            }
        }

        public void addOrUpdateOrIgnore(Node node, int distance) {
            // update || ignore -1
            if (isIn(node)) {
                distanceMap.put(node, Math.min(distanceMap.get(node), distance));
                heapInsert(idxMap.get(node));
            }
            // add
            if (!isEntered(node)) {
                nodes[size] = node;
                idxMap.put(node, size);
                distanceMap.put(node, distance);
                heapInsert(size++);
            }
        }

        public NodeRecord pop() {
            NodeRecord nodeRecord = new NodeRecord(nodes[0], distanceMap.get(nodes[0]));
            swap(0, size - 1);
            idxMap.put(nodes[size - 1], -1);
            distanceMap.remove(nodes[size - 1]);
            nodes[size - 1] = null;
            heapify(0, --size);
            return nodeRecord;
        }
    }

    public static HashMap<Node, Integer> dijkstra2(Node head, int size) {
        Heap heap = new Heap(size);
        heap.addOrUpdateOrIgnore(head, 0);
        HashMap<Node, Integer> rst = new HashMap<>();
        while (!heap.isEmpty()) {
            NodeRecord record = heap.pop();
            Node node = record.node;
            int distance = record.distance;
            for (Edge edge : node.edges) {
                heap.addOrUpdateOrIgnore(edge.to, edge.weight + distance);
            }
            rst.put(node, distance);
        }
        return rst;
    }


}
