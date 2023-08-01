package leetc.top;

public class P208_ImplementTrie {
    public static class Node {
        public boolean end;
        public Node[] nexts;
        public Node() {
            end = false;
            nexts = new Node[26];
        }
    }

    private Node root;

    public P208_ImplementTrie() {
        root = new Node();
    }

    public void insert(String word) {
        if (word == null) {
            return;
        }
        char[] str = word.toCharArray();
        Node node = root;
        int path = 0;
        for (int i = 0; i < str.length; i++) {
            path = str[i] - 'a';
            if (node.nexts[path] == null) {
                node.nexts[path] = new Node();
            }
            node = node.nexts[path];
        }
        node.end = true;
    }

    public boolean search(String word) {
        if (word == null) {
            return false;
        }
        char[] chs = word.toCharArray();
        Node node = root;
        int idx = 0;
        for (int i = 0; i < chs.length; i++) {
            idx = chs[i] - 'a';
            if (node.nexts[idx] == null) {
                return false;
            }
            node = node.nexts[idx];
        }
        return node.end;
    }

    public boolean startsWith(String pre) {
        if (pre == null) {
            return false;
        }
        char[] chs = pre.toCharArray();
        Node node = root;
        int idx = 0;
        for (int i = 0; i < chs.length; i++) {
            idx = chs[i] - 'a';
            if (node.nexts[idx] == null) {
                return false;
            }
            node = node.nexts[idx];
        }
        return true;
    }
}
