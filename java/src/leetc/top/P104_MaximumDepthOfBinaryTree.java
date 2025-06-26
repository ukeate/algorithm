package leetc.top;

/**
 * LeetCode 104. 二叉树的最大深度 (Maximum Depth of Binary Tree)
 * 
 * 问题描述：
 * 给定一个二叉树，找出其最大深度。
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 * 
 * 示例：
 * 给定二叉树 [3,9,20,null,null,15,7]
 * 最大深度为 3
 * 
 * 解法思路：
 * 递归算法：
 * 1. 如果根节点为空，深度为0
 * 2. 如果根节点是叶子节点，深度为1
 * 3. 否则，深度 = max(左子树深度, 右子树深度) + 1
 * 
 * 递归的本质：
 * - 将大问题分解为两个子问题：左子树的最大深度和右子树的最大深度
 * - 当前节点的深度 = 较深子树的深度 + 1（加上当前节点）
 * - 边界条件：空节点深度为0，叶子节点深度为1
 * 
 * 算法特点：
 * - 典型的树形动态规划思想
 * - 后序遍历的应用（先处理子树，再处理根节点）
 * - 递归栈的深度等于树的高度
 * 
 * 时间复杂度：O(n) - 需要访问树中的每个节点一次
 * 空间复杂度：O(h) - h为树的高度，递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/maximum-depth-of-binary-tree/
 */
public class P104_MaximumDepthOfBinaryTree {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    /**
     * 计算二叉树的最大深度
     * 
     * @param root 二叉树根节点
     * @return 最大深度
     */
    public static int maxDepth(TreeNode root) {
        // 边界条件：空节点深度为0
        if (root == null) {
            return 0;
        }
        
        // 优化：叶子节点直接返回1，避免不必要的递归
        if (root.left == null && root.right == null) {
            return 1;
        }
        
        // 递归计算：当前节点深度 = 子树最大深度 + 1
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }
}
