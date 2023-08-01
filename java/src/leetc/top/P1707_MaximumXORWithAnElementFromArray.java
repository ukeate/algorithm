package leetc.top;

public class P1707_MaximumXORWithAnElementFromArray {
    private static class Node {
        public int min;
        public Node[] nexts;

        public Node() {
            min = Integer.MAX_VALUE;
            nexts = new Node[2];
        }
    }

    private static class Trie {
        public Node head = new Node();

        public void add(int num) {
            Node cur = head;
            head.min = Math.min(head.min, num);
            for (int move = 30; move >= 0; move--) {
                int path = ((num >> move) & 1);
                cur.nexts[path] = cur.nexts[path] == null ? new Node() : cur.nexts[path];
                cur = cur.nexts[path];
                cur.min = Math.min(cur.min, num);
            }
        }

        public int maxXor(int x, int m) {
            if (head.min > m) {
                return -1;
            }
            Node cur = head;
            int ans = 0;
            for (int move = 30; move >= 0; move--) {
                int path = (x >> move) & 1;
                int best = (path ^ 1);
                best ^= (cur.nexts[best] == null || cur.nexts[best].min > m) ? 1 : 0;
                ans |= (path ^ best) << move;
                cur = cur.nexts[best];
            }
            return ans;
        }
    }

    public int[] maximizeXor(int[] nums, int[][] queries) {
        int n = nums.length;
        Trie trie = new Trie();
        for (int i = 0; i < n; i++) {
            trie.add(nums[i]);
        }
        int m = queries.length;
        int[] ans = new int[m];
        for (int i = 0; i < m; i++) {
            ans[i] = trie.maxXor(queries[i][0], queries[i][1]);
        }
        return ans;
    }
}
