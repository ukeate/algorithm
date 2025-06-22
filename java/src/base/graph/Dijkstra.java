package base.graph;

import base.graph.o.Edge;
import base.graph.o.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Dijkstra算法实现
 * 用于求解单源最短路径问题，即从一个起点到图中所有其他点的最短距离
 * 
 * 算法特点：
 * 1. 适用于有向图或无向图
 * 2. 边权必须为非负数
 * 3. 时间复杂度：O(V²) 或 O((V+E)logV) 使用优先队列优化
 * 4. 采用贪心策略，每次选择距离起点最近的未访问节点
 */
public class Dijkstra {
    /**
     * 从未访问的节点中找出距离起点最近的节点
     * @param distances 存储各节点到起点的距离
     * @param touched 已访问的节点集合
     * @return 距离起点最近的未访问节点
     */
    private static Node minNode(HashMap<Node, Integer> distances, HashSet<Node> touched) {
        Node minNode = null;
        int minDistance = Integer.MAX_VALUE;
        // 遍历所有已知距离的节点
        for (Map.Entry<Node, Integer> entry : distances.entrySet()) {
            Node node = entry.getKey();
            int distance = entry.getValue();
            // 如果节点未被访问且距离更小，则更新最小节点
            if (!touched.contains(node) && distance < minDistance) {
                minNode = node;
                minDistance = distance;
            }
        }
        return minNode;
    }

    /**
     * Dijkstra算法的基础实现（朴素版本）
     * 时间复杂度：O(V²)，适用于稠密图
     * @param from 起始节点
     * @return 从起始节点到所有可达节点的最短距离
     */
    public static HashMap<Node, Integer> dijkstra1(Node from) {
        // 记录到各节点的最短距离
        HashMap<Node, Integer> distances = new HashMap<>();
        distances.put(from, 0);
        // 已访问的节点集合
        HashSet<Node> touched = new HashSet<>();
        
        Node minNode = minNode(distances, touched);
        while (minNode != null) {
            int distance = distances.get(minNode);
            // 松弛操作：更新当前节点邻接点的距离
            for (Edge edge : minNode.edges) {
                Node to = edge.to;
                if (!distances.containsKey(to)) {
                    // 第一次到达该节点
                    distances.put(to, distance + edge.weight);
                } else {
                    // 如果找到更短的路径，则更新距离
                    distances.put(to, Math.min(distances.get(to), distance + edge.weight));
                }
            }
            // 标记当前节点为已访问
            touched.add(minNode);
            minNode = minNode(distances, touched);
        }
        return distances;
    }

    //

    /**
     * 节点记录类，用于优化版Dijkstra算法
     * 包含节点信息和到起点的距离
     */
    private static class NodeRecord {
        public Node node;      // 节点
        public int distance;   // 到起点的距离

        public NodeRecord(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    /**
     * 手工实现的小根堆，用于优化Dijkstra算法
     * 支持节点距离的动态更新，避免重复插入
     */
    private static class Heap {
        private Node[] nodes;                           // 堆中的节点数组
        private HashMap<Node, Integer> idxMap;          // 节点在堆中的位置映射
        private HashMap<Node, Integer> distanceMap;     // 节点到起点的距离映射
        private int size;                               // 堆的大小

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

        /**
         * 添加新节点或更新现有节点的距离
         * @param node 要处理的节点
         * @param distance 新的距离值
         */
        public void addOrUpdateOrIgnore(Node node, int distance) {
            // 如果节点在堆中，尝试更新距离
            if (isIn(node)) {
                distanceMap.put(node, Math.min(distanceMap.get(node), distance));
                heapInsert(idxMap.get(node)); // 向上调整堆
            }
            // 如果是新节点，添加到堆中
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

    /**
     * Dijkstra算法的优化实现（使用手工堆）
     * 时间复杂度：O((V+E)logV)，适用于稀疏图
     * @param head 起始节点
     * @param size 图中节点的数量
     * @return 从起始节点到所有可达节点的最短距离
     */
    public static HashMap<Node, Integer> dijkstra2(Node head, int size) {
        Heap heap = new Heap(size);
        heap.addOrUpdateOrIgnore(head, 0);
        HashMap<Node, Integer> rst = new HashMap<>();
        
        while (!heap.isEmpty()) {
            // 弹出距离起点最近的节点
            NodeRecord record = heap.pop();
            Node node = record.node;
            int distance = record.distance;
            
            // 松弛操作：更新邻接节点的距离
            for (Edge edge : node.edges) {
                heap.addOrUpdateOrIgnore(edge.to, edge.weight + distance);
            }
            rst.put(node, distance);
        }
        return rst;
    }


}
