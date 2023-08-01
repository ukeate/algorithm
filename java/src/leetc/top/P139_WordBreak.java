package leetc.top;

import java.util.List;

public class P139_WordBreak {

    private static class Node {
        public Node[] nexts;
        public boolean end;
        public Node() {
            end = false;
            nexts = new Node[26];
        }
    }

    public boolean wordBreak(String s, List<String> wordDict) {
        Node root = new Node();
        for (String str : wordDict) {
            char[] word = str.toCharArray();
            Node node = root;
            int idx = 0;
            for (int i = 0; i < word.length; i++) {
                idx = word[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            node.end = true;
        }
        char[] str = s.toCharArray();
        int n = str.length;
        boolean[] dp = new boolean[n + 1];
        dp[n] = true;
        for (int i = n - 1; i >= 0; i--) {
            Node cur = root;
            for (int end = i; end < n; end++) {
                int path = str[end] - 'a';
                if (cur.nexts[path] == null) {
                    break;
                }
                cur = cur.nexts[path];
                if (cur.end && dp[end + 1]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[0];
    }
}
