package leetc.top;

public class P108_ConvertSortedArrayToBinarySearchTree {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int v) {
            val = v;
        }
    }

    private static TreeNode process(int[] nums, int l, int r) {
        if (l > r) {
            return null;
        }
        if (l == r) {
            return new TreeNode(nums[l]);
        }
        int m = (l + r) / 2;
        TreeNode head = new TreeNode(nums[m]);
        head.left = process(nums, l, m - 1);
        head.right = process(nums, m + 1, r);
        return head;
    }

    public static TreeNode sortedArrayToBST(int[] nums) {
        return process(nums, 0, nums.length - 1);
    }
}
