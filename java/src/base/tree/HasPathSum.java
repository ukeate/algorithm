package base.tree;

import java.util.ArrayList;
import java.util.List;

public class HasPathSum {


    private static class Node {
        public int val;
        public Node left;
        public Node right;

        public Node(int val) {
            this.val = val;
        }
    }
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

    public static void main(String[] args) {

    }
}
