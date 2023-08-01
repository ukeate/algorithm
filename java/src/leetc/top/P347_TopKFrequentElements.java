package leetc.top;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class P347_TopKFrequentElements {
    public static class Node {
        public int key;
        public int count;
        public Node(int k) {
            key = k;
            count = 1;
        }
    }

    private static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.count - o2.count;
        }
    }

    public static int[] topKFrequent(int[] nums, int k) {
        HashMap<Integer, Node> map = new HashMap<>();
        for (int num : nums) {
            if (!map.containsKey(num)) {
                map.put(num, new Node(num));
            } else {
                map.get(num).count++;
            }
        }
        PriorityQueue<Node> heap = new PriorityQueue<>(new Comp());
        for (Node node : map.values()) {
            if (heap.size() < k || (heap.size() == k && node.count > heap.peek().count)) {
                heap.add(node);
            }
            if (heap.size() > k) {
                heap.poll();
            }
        }
        int[] ans = new int[k];
        int idx = 0;
        while (!heap.isEmpty()) {
            ans[idx++] = heap.poll().key;
        }
        return ans;
    }
}
