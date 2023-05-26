package basic.tree;

public class IsBst {

    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }

    private static class BstInfo {
        public boolean isBst;
        public int max;
        public int min;

        public BstInfo(boolean is, int max, int min) {
            this.isBst = is;
            this.max = max;
            this.min = min;
        }
    }

    public static BstInfo isBst(Node x) {
        if (x == null) {
            return null;
        }
        BstInfo li = isBst(x.left);
        BstInfo ri = isBst(x.right);
        int max = x.val;
        int min = x.val;
        if (li != null) {
            max = Math.max(li.max, max);
            min = Math.min(li.min, min);
        }
        if (ri != null) {
            max = Math.max(ri.max, max);
            min = Math.min(ri.min, min);
        }
        boolean isBst = false;
        boolean lb = li == null ? true : li.isBst;
        boolean rb = ri == null ? true : ri.isBst;
        boolean lm = li == null ? true : (li.max < x.val);
        boolean rm = ri == null ? true : (ri.min > x.val);
        if (lb && rb && lm && rm) {
            isBst = true;
        }
        return new BstInfo(isBst, max, min);
    }

    public static void main(String[] args) {

    }
}
