package basic.c56;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

// https://leetcode.com/problems/smallest-range-covering-elements-from-k-lists/
// k个有序list，求最小范围，要求每个list都包含一个元素
public class SmallestRangeCoveringElementsFromKLists {
    public static class Node {
        public int val;
        public int arrId;
        public int idx;

        public Node(int v, int ai, int i) {
            val = v;
            arrId = ai;
            idx = i;
        }
    }

    public static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.val != o2.val ? o1.val - o2.val : o1.arrId - o2.arrId;
        }
    }

    public static int[] smallest(List<List<Integer>> arr) {
        int n = arr.size();
        TreeSet<Node> set = new TreeSet<>(new Comp());
        for (int i = 0; i < n; i++) {
            set.add(new Node(arr.get(i).get(0), i, 0));
        }
        boolean hasSet = false;
        int a = 0;
        int b = 0;
        while (set.size() == n) {
            Node min = set.first();
            Node max = set.last();
            if (!hasSet || (max.val - min.val < b - a)) {
                hasSet = true;
                a = min.val;
                b = max.val;
            }
            min = set.pollFirst();
            int arrId = min.arrId;
            int idx = min.idx + 1;
            if (idx < arr.get(arrId).size()) {
                set.add(new Node(arr.get(arrId).get(idx), arrId, idx));
            }
        }
        return new int[]{a, b};
    }
}
