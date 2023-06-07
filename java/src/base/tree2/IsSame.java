package base.tree2;

public class IsSame {

    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    public static boolean isSame(Node p, Node q) {
        if (p == null ^ q == null) {
            return false;
        }
        if (p == null && q == null) {
            return true;
        }
        return p.val == q.val && isSame(p.left, q.left) && isSame(p.right, q.right);
    }

    public static void main(String[] args) {

    }

}
