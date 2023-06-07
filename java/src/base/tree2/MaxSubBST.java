package base.tree2;

import java.util.ArrayList;

// https://leetcode.com/problems/largest-bst-subtree
public class MaxSubBST {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    private static class Info {
        public int maxBst;
        public int size;
        public int max;
        public int min;

        public Info(int m, int s, int max, int min) {
            maxBst = m;
            size = s;
            this.max = max;
            this.min = min;
        }
    }

    private static Info process(TreeNode x) {
        if (x == null) {
            return null;
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int max = x.val;
        int min = x.val;
        int size = 1;
        if (li != null) {
            max = Math.max(li.max, max);
            min = Math.min(li.min, min);
            size += li.size;
        }
        if (ri != null) {
            max = Math.max(ri.max, max);
            min = Math.min(ri.min, min);
            size += ri.size;
        }
        int p1 = -1;
        if (li != null) {
            p1 = li.maxBst;
        }
        int p2 = -1;
        if (ri != null) {
            p2 = ri.maxBst;
        }
        int p0 = -1;
        boolean lBST = li == null ? true : (li.maxBst == li.size);
        boolean rBST = ri == null ? true : (ri.maxBst == ri.size);
        if (lBST && rBST) {
            boolean lLess = li == null ? true : (li.max < x.val);
            boolean rMore = ri == null ? true : (x.val < ri.min);
            if (lLess && rMore) {
                int lSize = li == null ? 0 : li.size;
                int rSize = ri == null ? 0 : ri.size;
                p0 = lSize + rSize + 1;
            }
        }
        return new Info(Math.max(p1, Math.max(p2, p0)), size, max, min);
    }

    public static int maxSubBST(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return process(head).maxBst;
    }

    //

    private static void in(TreeNode head, ArrayList<TreeNode> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);
        arr.add(head);
        in(head.right, arr);
    }

    private static int bstSize(TreeNode head) {
        if (head == null) {
            return 0;
        }
        ArrayList<TreeNode> arr = new ArrayList<>();
        in(head, arr);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).val <= arr.get(i - 1).val) {
                return 0;
            }
        }
        return arr.size();
    }

    private static int maxSubBstSure(TreeNode head) {
        if (head == null) {
            return 0;
        }
        int h = bstSize(head);
        if (h != 0) {
            return h;
        }
        return Math.max(maxSubBstSure(head.left), maxSubBstSure(head.right));
    }

    //

    private static TreeNode randomLevel(int level, int maxLevel, int maxVal) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        TreeNode head = new TreeNode((int) ((maxVal + 1) * Math.random()));
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        return head;
    }

    private static TreeNode randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

    public static void main(String[] args) {
        int times = 100000;
        int maxLevel = 10;
        int maxVal = 100;
        System.out.println("test begin");
        for (int i = 0; i < times; i++) {
            TreeNode head = randomTree(maxLevel, maxVal);
            int ans1 = maxSubBST(head);
            int ans2 = maxSubBstSure(head);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}

