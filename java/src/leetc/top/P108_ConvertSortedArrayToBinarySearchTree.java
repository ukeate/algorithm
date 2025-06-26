package leetc.top;

/**
 * LeetCode 108. 将有序数组转换为二叉搜索树 (Convert Sorted Array to Binary Search Tree)
 * 
 * 问题描述：
 * 将一个按照升序排列的有序数组，转换为一棵高度平衡的二叉搜索树。
 * 本题中，一个高度平衡二叉树是指一个二叉树每个节点的左右两个子树的高度差的绝对值不超过1。
 * 
 * 示例：
 * 给定有序数组: [-10,-3,0,5,9]
 * 一个可能的答案是：[0,-3,9,-10,null,5]，它可以表示下面这个高度平衡BST：
 *       0
 *      / \
 *    -3   9
 *    /   /
 *  -10  5
 * 
 * 解法思路：
 * 分治递归算法：
 * 1. 选择数组的中间元素作为根节点，这样可以保证左右子树节点数量尽可能平衡
 * 2. 中间元素左边的数组元素构成左子树（都小于根节点）
 * 3. 中间元素右边的数组元素构成右子树（都大于根节点）
 * 4. 递归构造左右子树
 * 
 * 核心思想：
 * - 利用有序数组的性质：中间元素天然满足BST的性质
 * - 通过选择中点保证树的平衡性：左右子树高度差不超过1
 * - 分治算法：将大问题分解为两个规模更小的相同子问题
 * 
 * BST性质保证：
 * - 由于数组有序，中点左边的元素都小于中点
 * - 中点右边的元素都大于中点
 * - 递归构造保证了整体BST性质
 * 
 * 平衡性保证：
 * - 每次选择中点，左右子数组长度差不超过1
 * - 递归构造的左右子树高度差不超过1
 * 
 * 时间复杂度：O(n) - 每个数组元素被访问一次
 * 空间复杂度：O(log n) - 递归栈深度（平衡二叉树高度）
 * 
 * LeetCode链接：https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/
 */
public class P108_ConvertSortedArrayToBinarySearchTree {
    
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
     * 递归构造平衡BST的核心函数
     * 
     * @param nums 有序数组
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     * @return 构造的子树根节点
     */
    private static TreeNode process(int[] nums, int l, int r) {
        // 边界条件：区间为空
        if (l > r) {
            return null;
        }
        
        // 只有一个元素的情况：直接创建叶子节点
        if (l == r) {
            return new TreeNode(nums[l]);
        }
        
        // 选择中间位置作为根节点（偏左中位数）
        // 这样可以保证左右子树尽可能平衡
        int m = (l + r) / 2;
        TreeNode head = new TreeNode(nums[m]);
        
        // 递归构造左子树：使用左半部分数组 [l, m-1]
        head.left = process(nums, l, m - 1);
        
        // 递归构造右子树：使用右半部分数组 [m+1, r]
        head.right = process(nums, m + 1, r);
        
        return head;
    }

    /**
     * 将有序数组转换为平衡BST主方法
     * 
     * @param nums 升序排列的有序数组
     * @return 平衡BST的根节点
     */
    public static TreeNode sortedArrayToBST(int[] nums) {
        return process(nums, 0, nums.length - 1);
    }
}
