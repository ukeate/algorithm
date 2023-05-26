package base.link;

import java.util.Comparator;
import java.util.PriorityQueue;

public class LinkMergeK {

    static class Node {
        public Node next;
        public int val;

        public Node(int val) {
            this.val = val;
        }
    }

    private static class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val - o2.val;
        }
    }

    public static Node mergeK(Node[] lists) {
        if (lists == null) {
            return null;
        }
        PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComparator());
        for (int i = 0; i < lists.length; i++) {
            if (lists[i] != null) {
                heap.add(lists[i]);
            }
        }
        if (heap.isEmpty()) {
            return null;
        }
        Node head = heap.poll();
        Node pre = head;
        if (pre.next != null) {
            heap.add(pre.next);
        }
        while (!heap.isEmpty()) {
            Node cur = heap.poll();
            pre.next = cur;
            pre = cur;
            if (cur.next != null) {
                heap.add(cur.next);
            }
        }
        return head;
    }

    public static void main(String[] args) {

    }
}
