package leetc.top;

/**
 * LeetCode 222. 完全二叉树的节点个数 (Count Complete Tree Nodes)
 * 
 * 问题描述：
 * 给你一棵完全二叉树的根节点 root ，求出该树的节点个数。
 * 
 * 完全二叉树的定义如下：
 * 在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，
 * 并且最下面一层的节点都集中在该层最左边的若干位置。
 * 若最底层为第 h 层，则该层包含 1~ 2^(h-1) 个节点。
 * 
 * 进阶：遍历树来统计节点是一种时间复杂度为 O(n) 的简单解决方案。你可以设计一个更快的算法吗？
 * 
 * 解法思路：
 * 二分搜索 + 完全二叉树性质：
 * 1. 利用完全二叉树的性质：除最后一层外，其他层都是满的
 * 2. 核心观察：通过检查子树的深度，可以判断子树是否为满二叉树
 * 3. 如果右子树的最左深度等于总深度，说明左子树是满的
 * 4. 如果右子树的最左深度小于总深度，说明右子树是满的
 * 5. 利用满二叉树节点数公式：2^h - 1，递归计算非满子树
 * 
 * 核心技巧：
 * - leftLevel(node, level)：计算从node开始一直向左到达的最大深度
 * - 通过比较右子树的最左深度与总深度，判断哪个子树是满的
 * - 满二叉树节点数：1 << (h - level) = 2^(h-level)
 * 
 * 算法优势：
 * - 避免遍历所有节点，利用完全二叉树的结构特性
 * - 每次递归都能确定一个满子树，大大减少计算量
 * - 时间复杂度优化到O(log²n)
 * 
 * 时间复杂度：O(log²n) - 每次递归O(log n)，最多递归log n次
 * 空间复杂度：O(log n) - 递归栈深度
 * 
 * LeetCode链接：https://leetcode.com/problems/count-complete-tree-nodes/
 */
public class P222_CountCompleteTreeNodes {
    
    /**
     * 二叉树节点定义
     */
    public class TreeNode {
        int val;           // 节点值
        TreeNode left;     // 左子节点
        TreeNode right;    // 右子节点
    }

    /**
     * 计算从指定节点开始，一直向左走能到达的最大深度
     * 
     * 这个函数用于快速判断子树的结构特征
     * 在完全二叉树中，最左路径的深度能反映子树的完整性
     * 
     * @param node 起始节点
     * @param level 起始层数
     * @return 最左路径的最大深度
     */
    private static int leftLevel(TreeNode node, int level) {
        while (node != null) {
            level++;           // 层数递增
            node = node.left;  // 一直向左走
        }
        return level - 1;  // 返回实际深度（减去多加的1）
    }

    /**
     * 二分搜索计算完全二叉树节点数的核心递归函数
     * 
     * 算法思路：
     * 1. 如果当前层就是最后一层，返回1（只有当前节点）
     * 2. 检查右子树的最左深度：
     *    - 如果等于总深度h，说明左子树是满的，递归计算右子树
     *    - 如果小于总深度h，说明右子树是满的，递归计算左子树
     * 3. 满子树的节点数可以用公式直接计算：2^(h-level)
     * 
     * 关键观察：
     * - 完全二叉树中，总有一个子树是满二叉树
     * - 通过比较深度可以快速确定哪个子树是满的
     * - 满子树不需要递归，直接用公式计算
     * 
     * @param node 当前节点
     * @param level 当前层数（从1开始）
     * @param h 总深度
     * @return 以node为根的子树的节点个数
     */
    private static int bs(TreeNode node, int level, int h) {
        // 递归边界：到达最后一层
        if (level == h) {
            return 1;  // 叶子节点，只有1个节点
        }
        
        // 计算右子树的最左深度
        if (leftLevel(node.right, level + 1) == h) {
            // 情况1：右子树的最左深度等于总深度
            // 说明左子树是满的，可以直接计算左子树节点数
            // 左子树节点数：2^(h-level)，然后递归计算右子树
            return (1 << (h - level)) + bs(node.right, level + 1, h);
        } else {
            // 情况2：右子树的最左深度小于总深度  
            // 说明右子树是满的（但缺少最后一层的部分节点）
            // 右子树节点数：2^(h-level-1)，然后递归计算左子树
            return (1 << (h - level - 1)) + bs(node.left, level + 1, h);
        }
    }

    /**
     * 计算完全二叉树的节点个数
     * 
     * 算法步骤：
     * 1. 计算树的总深度（从根节点一直向左走）
     * 2. 调用二分搜索函数计算节点总数
     * 
     * 为什么向左走计算深度：
     * - 在完全二叉树中，最左路径总是最长的
     * - 这样可以快速得到树的总深度
     * 
     * @param head 完全二叉树的根节点
     * @return 树的节点总数
     */
    public static int countNodes(TreeNode head) {
        // 边界情况：空树
        if (head == null) {
            return 0;
        }
        
        // 计算树的深度并开始二分搜索
        // leftLevel(head, 1)：从根节点开始计算深度，根节点为第1层
        return bs(head, 1, leftLevel(head, 1));
    }
}
