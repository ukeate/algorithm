package basic;

import java.util.ArrayList;
import java.util.List;

public class Tree2 {
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

    //

    private static List<Integer> copy(List<Integer> path) {
        List<Integer> ans = new ArrayList<>();
        for (Integer num : path) {
            ans.add(num);
        }
        return ans;
    }

    private static List<List<Integer>> pProcess(Node x, List<Integer> path, int rest, List<List<Integer>> ans) {
        if (x.left == null && x.right == null) {
            if (x.val == rest) {
                path.add(x.val);
                ans.add(copy(path));
                path.remove(path.size() - 1);
            }
            return ans;
        }
        path.add(x.val);
        if (x.left != null) {
            pProcess(x.left, path, rest - x.val, ans);
        }
        if (x.right != null) {
            pProcess(x.right, path, rest - x.val, ans);
        }
        path.remove(path.size() - 1);
        return ans;
    }

    public static List<List<Integer>> hasPathSum(Node root, int sum) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        List<Integer> path = new ArrayList<>();
        return pProcess(root, path, sum, ans);
    }

    //

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
}
