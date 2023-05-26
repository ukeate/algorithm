package basic.tree;

import java.util.HashMap;
import java.util.Map;

public class Build {

    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }


    private static Node buildF(int[] pre, int l1, int r1, int[] in, int l2, int r2, Map<Integer, Integer> inMap) {
        if (l1 > r1) {
            return null;
        }
        Node head = new Node(pre[l1]);
        if (l1 == r1) {
            return head;
        }
        int find = inMap.get(pre[l1]);
        head.left = buildF(pre, l1 + 1, l1 + find - l2, in, l2, find - 1, inMap);
        head.right = buildF(pre, l1 + find - l2 + 1, r1, in, find + 1, r2, inMap);
        return head;
    }

    public static Node build(int[] pre, int[] in) {
        if (pre == null || in == null || pre.length != in.length) {
            return null;
        }
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            inMap.put(in[i], i);
        }
        return buildF(pre, 0, pre.length - 1, in, 0, in.length - 1, inMap);
    }

    public static void main(String[] args) {

    }

}
