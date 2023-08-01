package leetc.top;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class P272_ClosestBinarySearchTreeValueII {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    private static void big(TreeNode root, double target, Stack<TreeNode> backUpRight) {
        while (root != null) {
            if (root.val == target) {
                backUpRight.push(root);
                break;
            } else if (root.val > target) {
                backUpRight.push(root);
                root = root.left;
            } else {
                root = root.right;
            }
        }
    }

    private static void small(TreeNode root, double target, Stack<TreeNode> backUpLeft) {
        while (root != null) {
            if (root.val == target) {
                backUpLeft.push(root);
                break;
            } else if (root.val < target) {
                backUpLeft.push(root);
                root = root.right;
            } else {
                root = root.left;
            }
        }
    }

    private static int smaller(Stack<TreeNode> small) {
        TreeNode cur = small.pop();
        int ret = cur.val;
        cur = cur.left;
        while (cur != null) {
            small.push(cur);
            cur = cur.right;
        }
        return ret;
    }

    private static int bigger(Stack<TreeNode> big) {
        TreeNode cur = big.pop();
        int ret = cur.val;
        cur = cur.right;
        while (cur != null) {
            big.push(cur);
            cur = cur.left;
        }
        return ret;
    }

    public static List<Integer> closestKValues(TreeNode root, double target, int k) {
        List<Integer> ret = new LinkedList<>();
        // [大、等]
        Stack<TreeNode> big = new Stack<>();
        // [小、等]
        Stack<TreeNode> small = new Stack<>();
        big(root, target, big);
        small(root, target, small);
        if (!big.isEmpty() && !small.isEmpty() && big.peek().val == small.peek().val) {
            smaller(small);
        }
        while (k-- > 0) {
            if (big.isEmpty()) {
                ret.add(smaller(small));
            } else if (small.isEmpty()) {
                ret.add(bigger(big));
            } else {
                double bigger = Math.abs((double) big.peek().val - target);
                double smaller = Math.abs((double) small.peek().val - target);
                if (bigger < smaller) {
                    ret.add(bigger(big));
                } else {
                    ret.add(smaller(small));
                }
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right = new TreeNode(6);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);
        for (int i : closestKValues(root, 1.5, 7)) {
            System.out.println(i);
        }
    }
}
