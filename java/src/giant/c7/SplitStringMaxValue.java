package giant.c7;

import java.util.HashMap;

// 每个贴纸有得分，k步内拼完str, 求最高得分
public class SplitStringMaxValue {
    private static int process(String str, int idx, int rest, HashMap<String, Integer> records) {
        if (rest < 0) {
            return -1;
        }
        if (idx == str.length()) {
            return rest == 0 ? 0 : -1;
        }
        int ans = -1;
        for (int end = idx; end < str.length(); end++) {
            String first = str.substring(idx, end + 1);
            int next = records.containsKey(first) ? process(str, end + 1, rest - 1, records) : -1;
            if (next != -1) {
                ans = Math.max(ans, records.get(first) + next);
            }
        }
        return ans;
    }

    public static int max1(String str, int k, String[] parts, int[] record) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        HashMap<String, Integer> records = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            records.put(parts[i], record[i]);
        }
        return process(str, 0, k, records);
    }

    //

    public static int max2(String str, int k, String[] parts, int[] record) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        HashMap<String, Integer> records = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            records.put(parts[i], record[i]);
        }
        int n = str.length();
        int[][] dp = new int[n + 1][k + 1];
        for (int rest = 1; rest <= k; rest++) {
            dp[n][rest] = -1;
        }
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= k; rest++) {
                int ans = -1;
                for (int end = idx; end < n; end++) {
                    String first = str.substring(idx, end + 1);
                    int next = rest > 0 && records.containsKey(first) ? dp[end + 1][rest - 1] : -1;
                    if (next != -1) {
                        ans = Math.max(ans, records.get(first) + next);
                    }
                }
                dp[idx][rest] = ans;
            }
        }
        return dp[0][k];
    }

    //

    private static class Node {
        public Node[] nexts;
        public int val;

        public Node() {
            nexts = new Node[26];
            val = -1;
        }
    }

    private static Node rootNode(String[] parts, int[] record) {
        Node root = new Node();
        for (int i = 0; i < parts.length; i++) {
            char[] str = parts[i].toCharArray();
            Node cur = root;
            for (int j = 0; j < str.length; j++) {
                int path = str[j] - 'a';
                if (cur.nexts[path] == null) {
                    cur.nexts[path] = new Node();
                }
                cur = cur.nexts[path];
            }
            cur.val = record[i];
        }
        return root;
    }

    public static int max3(String s, int k, String[] parts, int[] record) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Node root = rootNode(parts, record);
        char[] str = s.toCharArray();
        int n = str.length;
        int[][] dp = new int[n + 1][k + 1];
        for (int rest = 1; rest <= k; rest++) {
            dp[n][rest] = -1;
        }
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= k; rest++) {
                int ans = -1;
                Node cur = root;
                for (int end = idx; end < n; end++) {
                    int path = str[end] - 'a';
                    if (cur.nexts[path] == null) {
                        break;
                    }
                    cur = cur.nexts[path];
                    int next = rest > 0 && cur.val != -1 ? dp[end + 1][rest - 1] : -1;
                    if (next != -1) {
                        ans = Math.max(ans, cur.val + next);
                    }
                }
                dp[idx][rest] = ans;
            }
        }
        return dp[0][k];
    }

    public static void main(String[] args) {
        String str = "abcdefg";
        int k = 3;
        String[] parts = {"abc", "def", "g", "ab", "cd", "efg", "defg"};
        int[] record = {1, 1, 1, 3, 3, 3, 2};
        System.out.println(max1(str, k, parts, record));
        System.out.println(max2(str, k, parts, record));
        System.out.println(max3(str, k, parts, record));
    }
}
