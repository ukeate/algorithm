package basic.c34;

import java.util.TreeMap;

// 给目录路径，输出整体树结构
public class FolderTree {
    private static class Node {
        public String path;
        public TreeMap<String, Node> nexts;
        public Node(String p) {
            this.path = p;
            nexts = new TreeMap<>();
        }
    }

    private static Node getTree(String[] paths) {
        Node head = new Node("");
        for (String path : paths) {
            String[] ps = path.split("\\\\");
            Node cur = head;
            for (int i = 0; i < ps.length; i++) {
                if (!cur.nexts.containsKey(ps[i])) {
                    cur.nexts.put(ps[i], new Node(ps[i]));
                }
                cur = cur.nexts.get(ps[i]);
            }
        }
        return head;
    }

    private static String space(int n) {
        String rst = "";
        for (int i = 1; i < n; i++) {
            rst += "    ";
        }
        return rst;
    }

    private static void printTree(Node node, int level) {
        if (level > 0) {
            System.out.println(space(level) + node.path);
        }
        for (Node next : node.nexts.values()) {
            printTree(next, level + 1);
        }
    }

    public static void tree(String[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }
        Node head = getTree(paths);
        printTree(head, 0);
    }

    public static void main(String[] args) {
        String[] arr = { "b\\st", "d\\", "a\\d\\e", "a\\b\\c" };
        tree(arr);
    }
}
