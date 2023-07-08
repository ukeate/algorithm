package basic.c35;

// Completed Binary Tree节点个数，时间复杂度小于O(N)
public class CBTNodeNum {
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node(int v) {
            this.val = v;
        }
    }

    private static int mostLeftLevel(Node node, int level) {
        while (node != null) {
            level++;
            node = node.left;
        }
        return level - 1;
    }

    private static int nodeNum(Node node, int level, int h) {
        if (level == h) {
            return 1;
        }
        if (mostLeftLevel(node.right, level + 1) == h) {
            // 左满结算
            return (1 << (h - level)) + nodeNum(node.right, level + 1, h);
        } else {
            // 右满结算
            return (1 << (h - level - 1)) + nodeNum(node.left, level + 1, h);
        }
    }
    public static int num(Node head) {
        if (head == null) {
            return 0;
        }
        return nodeNum(head, 1, mostLeftLevel(head, 1));
    }

    public static void main(String[] args) {
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        System.out.println(num(head));
    }
}
