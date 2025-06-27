package leetc.top;

/**
 * LeetCode 226. 翻转二叉树 (Invert Binary Tree)
 * 
 * 问题描述：
 * 翻转一棵二叉树。
 * 
 * 示例：
 * 输入：
 *      4
 *    /   \
 *   2     7
 *  / \   / \
 * 1   3 6   9
 * 
 * 输出：
 *      4
 *    /   \
 *   7     2
 *  / \   / \
 * 9   6 3   1
 * 
 * 解法思路：
 * 递归解法：
 * 1. 递归的定义：翻转一棵二叉树就是交换每个节点的左右子树
 * 2. 递归的边界：空节点不需要翻转，直接返回
 * 3. 递归的过程：
 *    - 递归翻转左子树
 *    - 递归翻转右子树  
 *    - 交换当前节点的左右子树
 * 
 * 核心思想：
 * - 自顶向下的递归：先处理当前节点，再处理子问题
 * - 子问题的独立性：每个子树的翻转互不影响
 * - 原地操作：直接修改树的结构，不需要额外空间
 * 
 * 算法特点：
 * - 简洁优雅：代码量很少，逻辑清晰
 * - 效率高：每个节点只访问一次
 * - 空间优化：除递归栈外不需要额外空间
 * 
 * 这道题的著名背景：
 * 据说Google曾经因为候选人无法在白板上写出这道题的解法而拒绝了他，
 * 后来这个候选人就是Homebrew的作者Max Howell，他发推特说：
 * "Google: 90% of our engineers use the software you wrote (Homebrew), 
 *  but you can't invert a binary tree on a whiteboard so f*** off."
 * 
 * 时间复杂度：O(n) - 需要访问树中的每个节点
 * 空间复杂度：O(h) - 递归栈的深度，h为树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/invert-binary-tree/
 */
public class P226_InvertBinaryTree {
    
    /**
     * 二叉树节点定义
     */
    public class TreeNode {
        public int val;          // 节点值
        public TreeNode left;    // 左子节点
        public TreeNode right;   // 右子节点
    }

    /**
     * 翻转二叉树
     * 
     * 算法步骤：
     * 1. 递归终止条件：如果当前节点为空，直接返回
     * 2. 交换当前节点的左右子树
     * 3. 递归翻转左子树
     * 4. 递归翻转右子树
     * 5. 返回当前节点（作为翻转后的根节点）
     * 
     * 注意事项：
     * - 需要先保存左右子树的引用，再进行交换
     * - 交换后再递归，确保递归的是正确的子树
     * 
     * @param root 二叉树的根节点
     * @return 翻转后的二叉树根节点
     */
    public static TreeNode invertTree(TreeNode root) {
        if (root == null) {
            return null;  // 空节点，无需翻转
        }
        
        // 保存左右子树的引用
        TreeNode left = root.left;
        TreeNode right = root.right;
        
        // 交换左右子树
        root.left = right;
        root.right = left;
        
        // 递归翻转左右子树
        invertTree(root.left);
        invertTree(root.right);
        
        return root;  // 返回翻转后的根节点
    }
}
