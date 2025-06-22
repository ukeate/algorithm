package base.tree3;

import java.util.LinkedList;
import java.util.Queue;

/**
 * AC自动机（Aho-Corasick算法）实现
 * 用于多模式字符串匹配，可以同时查找多个模式串在文本中的出现次数
 */
public class AC {
    /**
     * AC自动机节点类
     */
    public static class Node {
        public int end;            // 以当前节点结尾的模式串数量
        public Node fail;          // 失配指针，指向最长的可匹配后缀
        public Node[] nexts;       // 子节点数组，支持26个小写字母

        public Node() {
            end = 0;
            fail = null;
            nexts = new Node[26];
        }
    }

    /**
     * AC自动机主类
     */
    public static class ACAutomation {
        private Node root;

        public ACAutomation() {
            root = new Node();
        }

        /**
         * 向AC自动机中插入一个模式串
         * @param s 要插入的字符串
         */
        public void insert(String s) {
            char[] str = s.toCharArray();
            Node cur = root;
            // 按字符逐个插入到Trie树中
            for (int i = 0; i < str.length; i++) {
                int ni = str[i] - 'a';
                if (cur.nexts[ni] == null) {
                    cur.nexts[ni] = new Node();
                }
                cur = cur.nexts[ni];
            }
            cur.end++;  // 标记模式串结尾
        }

        /**
         * 构建AC自动机的失配指针
         * 使用BFS层序遍历构建fail指针
         */
        public void build() {
            Queue<Node> que = new LinkedList<>();
            que.add(root);
            while (!que.isEmpty()) {
                Node cur = que.poll();
                Node cfail = cur.fail;
                // 遍历当前节点的所有子节点
                for (int i = 0; i < 26; i++) {
                    if (cur.nexts[i] != null) {
                        // 初始设置fail指针指向根节点
                        cur.nexts[i].fail = root;
                        // 寻找最长的可匹配后缀
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

        /**
         * 在目标文本中查找所有模式串的出现次数总和
         * @param content 目标文本
         * @return 所有模式串在文本中的出现次数总和
         */
        public int containNum(String content) {
            char[] str = content.toCharArray();
            int ans = 0;
            Node cur = root;
            for (int i = 0; i < str.length; i++) {
                int ni = str[i] - 'a';
                // 如果当前字符无法匹配，则沿fail指针回退
                while (cur.nexts[ni] == null && cur != root) {
                    cur = cur.fail;
                }
                // 移动到下一个状态
                cur = cur.nexts[ni] != null ? cur.nexts[ni] : root;
                Node follow = cur;
                // 沿fail指针查找所有可能的匹配
                while (follow != root) {
                    if (follow.end == -1) {
                        break;  // 已经计算过，避免重复计算
                    }
                    // end可以记录匹配的模式串信息
                    ans += follow.end;
                    follow.end = -1;  // 标记为已计算
                    follow = follow.fail;
                }
            }
            return ans;
        }
    }

    /**
     * 测试AC自动机
     */
    public static void main(String[] args) {
        ACAutomation ac = new ACAutomation();
        ac.insert("dhe");
        ac.insert("he");
        ac.insert("c");
        ac.build();
        System.out.println(ac.containNum("cdhe"));  // 输出: 3 (匹配"c", "dhe", "he")
    }
}
