package base.tree;

public class Traversal {

    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    public static void traversal(Node head) {
        if (head == null) {
            return;
        }
        traversal(head.left);
        traversal(head.right);
    }

    public static void main(String[] args) {

    }
}
