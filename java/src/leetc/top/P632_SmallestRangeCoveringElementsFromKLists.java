package leetc.top;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class P632_SmallestRangeCoveringElementsFromKLists {
    public static class Node {
        public int value;
        public int arrId;
        public int idx;

        public Node(int v, int ai, int i) {
            value = v;
            arrId = ai;
            idx = i;
        }
    }

    public static class Comp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.value != o2.value ? o1.value - o2.value : o1.arrId - o2.arrId;
        }
    }

    public static int[] smallestRange(List<List<Integer>> nums) {
        int n = nums.size();
        TreeSet<Node> set = new TreeSet<>(new Comp());
        for (int i = 0; i < n; i++) {
            set.add(new Node(nums.get(i).get(0), i, 0));
        }
        boolean hasSet = false;
        int a = 0, b = 0;
        while (set.size() == n) {
            Node min = set.first();
            Node max = set.last();
            if (!hasSet || (max.value - min.value < b - a)) {
                hasSet = true;
                a = min.value;
                b = max.value;
            }
            min = set.pollFirst();
            int arrId = min.arrId;
            int idx = min.idx + 1;
            if (idx != nums.get(arrId).size()) {
                set.add(new Node(nums.get(arrId).get(idx), arrId, idx));
            }
        }
        return new int[] {a, b};
    }
}
