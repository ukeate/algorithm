package leetc.top;

public class P421_MaximumXOROfTwoNumbersInAnArray {
    public static class Node {
        public Node[] nexts = new Node[2];
    }
    private static class Trie {
        public Node head = new Node();
        public void add(int num) {
            Node cur = head;
            for (int move = 31; move >= 0; move--) {
                int path = ((num >> move) & 1);
                cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
                cur = cur.nexts[path];
            }
        }

        public int maxXor(int sum) {
            Node cur = head;
            int res = 0;
            for (int move = 31; move >= 0; move--) {
                int path = (sum >> move) & 1;
                int best = move == 31 ? path : (path ^ 1);
                best = cur.nexts[best] != null ? best : (best ^ 1);
                res |= (path ^ best) << move;
                cur = cur.nexts[best];
            }
            return res;
        }
    }

    public int findMaximumXOR(int[] arr) {
        if (arr == null || arr.length < 2) {
            return 0;
        }
        int max = Integer.MIN_VALUE;
        Trie trie = new Trie();
        trie.add(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            max = Math.max(max, trie.maxXor(arr[i]));
            trie.add(arr[i]);
        }
        return max;
    }
}
