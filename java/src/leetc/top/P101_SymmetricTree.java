package leetc.top;

/**
 * LeetCode 101. 对称二叉树 (Symmetric Tree)
 * 
 * 问题描述：
 * 给定一个二叉树，检查它是否是镜像对称的。
 * 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
 * 但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的。
 * 
 * 解法思路：
 * 递归对比法：
 * 1. 一个树是对称的等价于：左子树是右子树的镜像
 * 2. 两个树互为镜像的条件：
 *    - 它们的根节点具有相同的值
 *    - 每个树的右子树都与另一个树的左子树镜像对称
 *    - 每个树的左子树都与另一个树的右子树镜像对称
 * 3. 递归终止条件：
 *    - 两个节点都为空：对称
 *    - 一个为空一个不为空：不对称
 *    - 两个都不为空但值不同：不对称
 * 
 * 核心技巧：
 * - 将对称性检查转化为镜像检查
 * - 同一棵树的左右子树互为镜像
 * - 镜像检查：左.left vs 右.right，左.right vs 右.left
 * 
 * 时间复杂度：O(n) - 需要遍历树中的每个节点
 * 空间复杂度：O(h) - 递归栈深度等于树的高度h
 * 
 * LeetCode链接：https://leetcode.com/problems/symmetric-tree/
 */
public class P101_SymmetricTree {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    /**
     * 判断两个树是否互为镜像
     * 
     * @param h1 第一个树的根节点
     * @param h2 第二个树的根节点
     * @return 是否互为镜像
     */
    public static boolean isMirror(TreeNode h1, TreeNode h2) {
        // 两个节点都为空，镜像对称
        if (h1 == null && h2 == null) {
            return true;
        }
        
        // 两个节点都不为空
        if (h1 != null && h2 != null) {
            // 根节点值相同 && 左树的左子树与右树的右子树镜像 && 左树的右子树与右树的左子树镜像
            return h1.val == h2.val 
                && isMirror(h1.left, h2.right)   // 镜像检查：左.左 vs 右.右
                && isMirror(h1.right, h2.left);  // 镜像检查：左.右 vs 右.左
        }
        
        // 一个为空一个不为空，不镜像
        return false;
    }

    /**
     * 判断二叉树是否对称
     * 
     * @param root 二叉树根节点
     * @return 是否对称
     */
    public static boolean isSymmetric(TreeNode root) {
        // 对称性检查 = 左右子树互为镜像
        // 这里巧妙地用同一个根节点进行镜像检查
        // 相当于检查 root.left 和 root.right 是否镜像
        return isMirror(root, root);
    }
}
