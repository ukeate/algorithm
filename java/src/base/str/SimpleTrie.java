package base.str;

// https://leetcode.cn/problems/implement-trie-ii-prefix-tree/
public class SimpleTrie {
    private static class Trie {
        public static class Node {
            public int pass;
            public int end;
            public Node[] nexts;

            public Node() {
                nexts = new Node[26];
            }
        }

        private Node root;

        public Trie() {
            root = new Node();
        }

        private int countWordsEqualTo(String word) {
            if (word == null) {
                return 0;
            }
            char[] chs = word.toCharArray();
            Node node = root;
            for (int i = 0; i < chs.length; i++) {
                int ii = chs[i] - 'a';
                if (node.nexts[ii] == null) {
                    return 0;
                }
                node = node.nexts[ii];
            }
            return node.end;
        }

        private int countWordsStartWith(String pre) {
            if (pre == null) {
                return 0;
            }
            char[] chs = pre.toCharArray();
            Node node = root;
            for (int i = 0; i < chs.length; i++) {
                int ii = chs[i] - 'a';
                if (node.nexts[ii] == null) {
                    return 0;
                }
                node = node.nexts[ii];
            }
            return node.pass;
        }

        public void insert(String word) {
            if (word == null) {
                return;
            }
            char[] str = word.toCharArray();
            Node node = root;
            node.pass++;
            int path = 0;
            for (int i = 0; i < str.length; i++) {
                path = str[i] - 'a';
                if (node.nexts[path] == null) {
                    node.nexts[path] = new Node();
                }
                node = node.nexts[path];
                node.pass++;
            }
            node.end++;
        }

        public void erase(String word) {
            if (countWordsEqualTo(word) != 0) {
                char[] chs = word.toCharArray();
                Node node = root;
                node.pass--;
                int path = 0;
                for (int i = 0; i < chs.length; i++) {
                    path = chs[i] - 'a';
                    if (--node.nexts[path].pass == 0) {
                        node.nexts[path] = null;
                        return;
                    }
                    node = node.nexts[path];
                }
                node.end--;
            }
        }
    }
}
