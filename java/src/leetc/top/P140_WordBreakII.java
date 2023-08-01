package leetc.top;

import java.util.ArrayList;
import java.util.List;

public class P140_WordBreakII {
    private static class Node {
        public String path;
        public boolean end;
        public Node[] nexts;
        public Node() {
            path = null;
            end = false;
            nexts= new Node[26];
        }
    }

    private static Node trie(List<String> wordDict) {
        Node root = new Node();
        for (String str : wordDict) {
            char[] chs = str.toCharArray();
            Node node = root;
            int idx = 0;
            for (int i = 0; i < chs.length; i++) {
                idx = chs[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            node.path = str;
            node.end = true;
        }
        return root;
    }

    private static boolean[] dp(String s, Node root) {
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
        return dp;
    }

    private static void process(char[] str, int idx, Node root, boolean[] dp, ArrayList<String> path, List<String> ans) {
        if (idx == str.length) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < path.size() - 1; i++) {
                builder.append(path.get(i) + " ");
            }
            builder.append(path.get(path.size() - 1));
            ans.add(builder.toString());
            return;
        }
        Node cur = root;
        for (int end = idx; end < str.length; end++) {
            int next = str[end] - 'a';
            if (cur.nexts[next] == null) {
                break;
            }
            cur = cur.nexts[next];
            if (cur.end && dp[end + 1]) {
                path.add(cur.path);
                process(str, end + 1, root, dp, path, ans);
                path.remove(path.size() - 1);
            }
        }
    }

    public static List<String> wordBreak(String s, List<String> wordDict) {
        char[] str = s.toCharArray();
        Node root = trie(wordDict);
        boolean[] dp = dp(s, root);
        ArrayList<String> path = new ArrayList<>();
        List<String> ans = new ArrayList<>();
        process(str, 0, root, dp, path, ans);
        return ans;
    }
}
