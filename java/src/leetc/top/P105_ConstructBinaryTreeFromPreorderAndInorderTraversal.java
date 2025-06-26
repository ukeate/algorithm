package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 105. 从前序与中序遍历序列构造二叉树 (Construct Binary Tree from Preorder and Inorder Traversal)
 * 
 * 问题描述：
 * 根据一棵树的前序遍历与中序遍历构造二叉树。
 * 注意: 你可以假设树中没有重复的元素。
 * 
 * 示例：
 * 前序遍历 preorder = [3,9,20,15,7]
 * 中序遍历 inorder = [9,3,15,20,7]
 * 构造出的二叉树：
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 
 * 解法思路：
 * 递归分治算法：
 * 1. 前序遍历的第一个元素总是根节点
 * 2. 在中序遍历中找到根节点的位置，左边是左子树，右边是右子树
 * 3. 根据中序遍历中左子树的长度，在前序遍历中划分左右子树
 * 4. 递归构造左右子树
 * 
 * 关键观察：
 * - 前序遍历：根 -> 左子树 -> 右子树
 * - 中序遍历：左子树 -> 根 -> 右子树
 * - 根节点在前序遍历中的位置确定了根的值
 * - 根节点在中序遍历中的位置确定了左右子树的边界
 * 
 * 优化技巧：
 * - 使用HashMap预存中序遍历的索引，O(1)时间找到根节点位置
 * - 精确计算子数组的边界，避免创建新数组
 * 
 * 时间复杂度：O(n) - 每个节点创建一次，HashMap查找O(1)
 * 空间复杂度：O(n) - HashMap存储 + 递归栈空间O(h)
 * 
 * LeetCode链接：https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 */
public class P105_ConstructBinaryTreeFromPreorderAndInorderTraversal {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 递归构造二叉树的核心函数
     * 
     * @param pre 前序遍历数组
     * @param l1 前序遍历的左边界
     * @param r1 前序遍历的右边界
     * @param in 中序遍历数组
     * @param l2 中序遍历的左边界
     * @param r2 中序遍历的右边界
     * @param inMap 中序遍历值到索引的映射
     * @return 构造的二叉树根节点
     */
    private static TreeNode process(int[] pre, int l1, int r1, int[] in, int l2, int r2, HashMap<Integer, Integer> inMap) {
        // 边界条件：区间为空
        if (l1 > r1) {
            return null;
        }
        
        // 前序遍历的第一个元素是根节点
        TreeNode head = new TreeNode(pre[l1]);
        
        // 只有一个节点的情况
        if (l1 == r1) {
            return head;
        }
        
        // 在中序遍历中找到根节点的位置
        int find = inMap.get(pre[l1]);
        
        // 计算左子树的节点数量：find - l2
        // 在前序遍历中，左子树的范围是 [l1+1, l1+leftSize]
        // 在中序遍历中，左子树的范围是 [l2, find-1]
        head.left = process(pre, l1 + 1, l1 + find - l2, in, l2, find - 1, inMap);
        
        // 右子树的范围：
        // 在前序遍历中：[l1+leftSize+1, r1] = [l1+find-l2+1, r1]
        // 在中序遍历中：[find+1, r2]
        head.right = process(pre, l1 + find - l2 + 1, r1, in, find + 1, r2, inMap);
        
        return head;
    }

    /**
     * 从前序和中序遍历构造二叉树主方法
     * 
     * @param preorder 前序遍历数组
     * @param inorder 中序遍历数组
     * @return 构造的二叉树根节点
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 构建中序遍历的值到索引的映射，便于快速查找根节点位置
        HashMap<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inMap.put(inorder[i], i);
        }
        
        // 开始递归构造
        return process(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1, inMap);
    }
}
