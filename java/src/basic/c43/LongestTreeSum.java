package basic.c43;

import java.util.HashMap;

// 树上累加和为k的最长路径
public class LongestTreeSum {
    public static class Node {
        int val;
        public Node left;
        public Node right;
        public Node(int v) {
            val = v;
        }
    }

    public static int ans = 0;
    private static void process(Node x, int level, int preSum, int k, HashMap<Integer, Integer> sumMap) {
        if (x != null) {
            int allSum = preSum + x.val;
            if (sumMap.containsKey(allSum - k)) {
                ans = Math.max(ans, level - sumMap.get(allSum - k));
            }
            if (!sumMap.containsKey(allSum)) {
                sumMap.put(allSum, level);
            }
            process(x.left, level + 1, allSum, k, sumMap);
            process(x.right, level + 1, allSum, k, sumMap);
            if (sumMap.get(allSum) == level) {
                sumMap.remove(allSum);
            }
        }
    }

    public static int longest(Node head, int k) {
        ans = 0;
        HashMap<Integer, Integer> sumMap = new HashMap<>();
        sumMap.put(0, -1);
        process(head, 0, 0, k, sumMap);
        return ans;
    }

    public static void main(String[] args) {
        int k = 0;
        Node head = new Node(3);
        head.left = new Node(-2);
        head.left.left = new Node(1);
        head.left.right = new Node(4);
        head.left.left.left = new Node(3);
        head.left.left.right = new Node(5);
        head.left.right.left = new Node(2);
        head.left.right.right = new Node(5);

        head.right = new Node(3);
        head.right.left = new Node(5);
        head.right.right = new Node(-7);
        head.right.left.left = new Node(-5);
        head.right.left.right = new Node(-3);
        head.right.right.left = new Node(1);
        head.right.right.right = new Node(5);

        System.out.println(longest(head, k));
    }
}
