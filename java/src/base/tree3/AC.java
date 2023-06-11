package base.tree3;

import java.util.LinkedList;
import java.util.Queue;

public class AC {
    public static class Node {
        public int end;
        public Node fail;
        public Node[] nexts;

        public Node() {
            end = 0;
            fail = null;
            nexts = new Node[26];
        }
    }

    public static class ACAutomation {
        private Node root;

        public ACAutomation() {
            root = new Node();
        }

        public void insert(String s) {
            char[] str = s.toCharArray();
            Node cur = root;
            for (int i = 0; i < str.length; i++) {
                int ni = str[i] - 'a';
                if (cur.nexts[ni] == null) {
                    cur.nexts[ni] = new Node();
                }
                cur = cur.nexts[ni];
            }
            cur.end++;
        }

        public void build() {
            Queue<Node> que = new LinkedList<>();
            que.add(root);
            while (!que.isEmpty()) {
                Node cur = que.poll();
                Node cfail = cur.fail;
                for (int i = 0; i < 26; i++) {
                    if (cur.nexts[i] != null) {
                        cur.nexts[i].fail = root;
                        while (cfail != null) {
                            if (cfail.nexts[i] != null) {
                                cur.nexts[i].fail = cfail.nexts[i];
                                break;
                            }
                            cfail = cfail.fail;
                        }
                        que.add(cur.nexts[i]);
                    }
                }
            }
        }

        public int containNum(String content) {
            char[] str = content.toCharArray();
            int ans = 0;
            Node cur = root;
            for (int i = 0; i < str.length; i++) {
                int ni = str[i] - 'a';
                while (cur.nexts[ni] == null && cur != root) {
                    cur = cur.fail;
                }
                cur = cur.nexts[ni] != null ? cur.nexts[ni] : root;
                Node follow = cur;
                while (follow != root) {
                    if (follow.end == -1) {
                        break;
                    }
                    // end可以记匹配串
                    ans += follow.end;
                    follow.end = -1;
                    follow = follow.fail;
                }
            }
            return ans;
        }
    }

    public static void main(String[] args) {
        ACAutomation ac = new ACAutomation();
        ac.insert("dhe");
        ac.insert("he");
        ac.insert("c");
        ac.build();
        System.out.println(ac.containNum("cdhe"));
    }
}
