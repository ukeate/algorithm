package basic.c46;

import java.util.HashMap;
import java.util.Map;

// 求符合最大拓扑结构(可取一部分节点)搜索二叉树的大小
public class BiggestBSTTopology {
    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int v) {
            this.val = v;
        }
    }

    private static boolean isBSTNode(Node h, Node n, int val) {
        if (h == null) {
            return false;
        }
        if (h == n) {
            return true;
        }
        return isBSTNode(val < h.val ? h.left : h.right, n, val);
    }

    private static int size(Node h, Node n) {
        if (h != null && n != null && isBSTNode(h, n, n.val)) {
            return size(h, n.left) + size(h, n.right) + 1;
        }
        return 0;
    }

    public static int max1(Node head) {
        if (head == null) {
            return 0;
        }
        int max = size(head, head);
        max = Math.max(max1(head.left), max);
        max = Math.max(max1(head.right), max);
        return max;
    }

    //

    // bst可用节点数
    private static class Record {
        public int l;
        public int r;
        public Record(int left, int right) {
            this.l = left;
            this.r = right;
        }
    }

    // 返回要减的数
    private static int modifyMap(Node node, int upVal, Map<Node, Record> map, boolean isLeft) {
        if (node == null || (!map.containsKey(node))) {
            return 0;
        }
        Record cur = map.get(node);
        if ((isLeft && node.val > upVal) || ((!isLeft) && node.val < upVal)) {
            map.remove(node);
            return cur.l + cur.r + 1;
        } else {
            int minus = modifyMap(isLeft ? node.right : node.left, upVal, map, isLeft);
            if (isLeft) {
                cur.r = cur.r - minus;
            } else {
                cur.l = cur.l - minus;
            }
            map.put(node, cur);
            return minus;
        }
    }

    private static int posOrder(Node h, Map<Node, Record> map) {
        if (h == null) {
            return 0;
        }
        int ls = posOrder(h.left, map);
        int rs = posOrder(h.right, map);
        modifyMap(h.left, h.val, map, true);
        modifyMap(h.right, h.val, map, false);
        Record lr = map.get(h.left);
        Record rr = map.get(h.right);
        int lbst = lr == null ? 0 : lr.l + lr.r + 1;
        int rbst = rr == null ? 0 : rr.l + rr.r + 1;
        map.put(h, new Record(lbst, rbst));
        return Math.max(lbst + rbst + 1, Math.max(ls, rs));
    }

    private static int max2(Node head) {
        Map<Node, Record> map = new HashMap<>();
        return posOrder(head, map);
    }

    //

    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    private static void printIn(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printIn(head.right, height + 1, "v", len);
        String val = to + head.val + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printIn(head.left, height + 1, "^", len);
    }

    private static void print(Node head) {
        printIn(head, 0, "H", 17);
        System.out.println();
    }

    public static void main(String[] args) {
        Node head = new Node(6);
        head.left = new Node(1);
        head.left.left = new Node(0);
        head.left.right = new Node(3);
        head.right = new Node(12);
        head.right.left = new Node(10);
        head.right.left.left = new Node(4);
        head.right.left.left.left = new Node(2);
        head.right.left.left.right = new Node(5);
        head.right.left.right = new Node(14);
        head.right.left.right.left = new Node(11);
        head.right.left.right.right = new Node(15);
        head.right.right = new Node(13);
        head.right.right.left = new Node(20);
        head.right.right.right = new Node(16);
        print(head);

        System.out.println(max1(head));
        System.out.println(max2(head));

    }
}
