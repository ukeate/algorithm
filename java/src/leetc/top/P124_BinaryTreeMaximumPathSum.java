package leetc.top;

/**
 * LeetCode 124. 二叉树中的最大路径和 (Binary Tree Maximum Path Sum)
 * 
 * 问题描述：
 * 路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
 * 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
 * 路径和 是路径中各节点值的总和。
 * 给你一个二叉树的根节点 root，返回其最大路径和。
 * 
 * 解法思路：
 * 树形动态规划 + 递归信息收集：
 * 1. 对于每个节点，需要收集两种信息：
 *    - maxSumFromHead: 从当前节点开始向下的最大路径和
 *    - maxSum: 经过当前子树的所有路径中的最大路径和
 * 
 * 2. 路径类型分析（以节点x为根）：
 *    - 只包含x: x.val
 *    - x + 左子树: x.val + left.maxSumFromHead
 *    - x + 右子树: x.val + right.maxSumFromHead
 *    - x + 左子树 + 右子树: x.val + left.maxSumFromHead + right.maxSumFromHead
 *    - 不经过x的路径: left.maxSum 或 right.maxSum
 * 
 * 3. 状态计算：
 *    - maxSumFromHead = max(x.val, x.val + max(left.maxSumFromHead, right.maxSumFromHead))
 *    - maxSum = max(所有可能的路径和)
 * 
 * 核心观察：
 * - 对于任意一个节点，经过它的路径最多只能选择一个子树继续延伸
 * - 但是在计算最大路径和时，可以同时考虑左右两个子树
 * - 递归过程中需要同时维护两种不同的信息
 * 
 * 算法特点：
 * - 典型的树形DP问题
 * - 后序遍历收集子树信息
 * - 每个节点的信息基于子节点信息计算
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈深度等于树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/binary-tree-maximum-path-sum/
 */
public class P124_BinaryTreeMaximumPathSum {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    private static class Info {
        public int maxSum;
        public int maxSumFromHead;
        public Info(int path, int fromHead) {
            maxSum = path;
            maxSumFromHead = fromHead;
        }
    }

    private static Info process(TreeNode x) {
        if (x == null) {
            return null;
        }
        Info li = process(x.left);
        Info ri = process(x.right);
        int p1 = Integer.MIN_VALUE;
        if (li != null) {
            p1 = li.maxSum;
        }
        int p2 = Integer.MIN_VALUE;
        if (ri != null) {
            p2 = ri.maxSum;
        }
        int p3 = x.val;
        int p4 = Integer.MIN_VALUE;
        if (li != null) {
            p4 = x.val + li.maxSumFromHead;
        }
        int p5 = Integer.MIN_VALUE;
        if (ri != null) {
            p5 = x.val + ri.maxSumFromHead;
        }
        int p6 = Integer.MIN_VALUE;
        if (li != null && ri != null) {
            p6 = x.val + li.maxSumFromHead + ri.maxSumFromHead;
        }
        int maxSumFromHead = Math.max(p3, Math.max(p4, p5));
        int maxSum = Math.max(Math.max(p1, p2), Math.max(maxSumFromHead, p6));
        return new Info(maxSum, maxSumFromHead);
    }

    public static int maxPathSum(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return process(root).maxSum;
    }
}
