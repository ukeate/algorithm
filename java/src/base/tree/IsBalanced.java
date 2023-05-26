package base.tree;

public class IsBalanced {
    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    private static class BInfo {
        public boolean isB;
        public int ht;

        public BInfo(boolean i, int h) {
            this.isB = i;
            this.ht = h;
        }
    }

    private static BInfo bProcess(Node root) {
        if (root == null) {
            return new BInfo(true, 0);
        }
        BInfo li = bProcess(root.left);
        BInfo ri = bProcess(root.right);
        int ht = Math.max(li.ht, ri.ht) + 1;
        boolean isB = li.isB && ri.isB && Math.abs(li.ht - ri.ht) < 2;
        return new BInfo(isB, ht);
    }

    public static boolean isBalanced(Node root) {
        return bProcess(root).isB;
    }

    public static void main(String[] args) {

    }
}
