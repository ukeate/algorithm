package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class P428_SerializeAndDeserializeNaryTree {
    public static class Node {
        public int val;
        public List<Node> children;

        public Node() {
            children = new ArrayList<>();
        }

        public Node(int v) {
            val = v;
            children = new ArrayList<>();
        }
    }

    public static class Codec {
        private static void serial(StringBuilder builder, Node head) {
            builder.append(head.val + ",");
            if (!head.children.isEmpty()) {
                builder.append("[,");
                for (Node next : head.children) {
                    serial(builder, next);
                }
                builder.append("],");
            }
        }

        public static String serialize(Node root) {
            if (root == null) {
                return "#";
            }
            StringBuilder builder = new StringBuilder();
            serial(builder, root);
            return builder.toString();
        }

        private static Node deserial(Queue<String> que) {
            Node cur = new Node(Integer.valueOf(que.poll()));
            cur.children = new ArrayList<>();
            if (!que.isEmpty() && que.peek().equals("[")) {
                que.poll();
                while (!que.peek().equals("]")) {
                    Node child = deserial(que);
                    cur.children.add(child);
                }
                que.poll();
            }
            return cur;
        }

        public static Node deserialize(String data) {
            if (data.equals("#")) {
                return null;
            }
            String[] splits = data.split(",");
            Queue<String> que = new LinkedList<>();
            for (String str : splits) {
                que.offer(str);
            }
            return deserial(que);
        }
    }

    //

    public static void main(String[] args) {
        Node a = new Node(1), b = new Node(2), c = new Node(3), d = new Node(4), e = new Node(5), f = new Node(6), g = new Node(7);
        a.children.add(b);
        a.children.add(c);
        a.children.add(d);
        b.children.add(e);
        b.children.add(f);
        e.children.add(g);
        String content = Codec.serialize(a);
        System.out.println(content);
        Node head = Codec.deserialize(content);
        System.out.println(content.equals(Codec.serialize(head)));
    }
}
