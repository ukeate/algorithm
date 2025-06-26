package leetc.top;

/**
 * LeetCode 230. 二叉搜索树中第K小的元素 (Kth Smallest Element in a BST)
 * 
 * 问题描述：
 * 给定一个二叉搜索树的根节点 root ，和一个整数 k ，请你设计一个算法查找其中第 k 个最小的元素（从 1 开始计数）。
 * 
 * 示例：
 * 输入：root = [3,1,4,null,2], k = 1
 * 输出：1
 * 
 * 输入：root = [5,3,6,2,4,null,null,1], k = 3
 * 输出：3
 * 
 * 解法思路：
 * Morris中序遍历：
 * 1. 利用BST的性质：中序遍历得到递增序列
 * 2. 使用Morris遍历实现O(1)空间复杂度的中序遍历
 * 3. 在遍历过程中计数，当计数到第k个时返回当前节点值
 * 4. Morris遍历通过建立临时线索来实现无栈遍历
 * 
 * 核心思想：
 * - BST的中序遍历结果是有序的
 * - Morris遍历避免使用递归栈或显式栈
 * - 通过建立和拆除线索来控制遍历流程
 * - 实现了O(n)时间、O(1)空间的解决方案
 * 
 * Morris遍历原理：
 * 1. 如果当前节点有左子树，找到左子树的最右节点作为前驱
 * 2. 建立前驱到当前节点的线索，然后访问左子树
 * 3. 当从左子树回到当前节点时，拆除线索，访问当前节点
 * 4. 然后访问右子树
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/kth-smallest-element-in-a-bst/
 */
public class P230_KthSmallestElementInBST {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int val;      // 节点值
        public TreeNode left;  // 左子节点
        public TreeNode right; // 右子节点
    }

    /**
     * 查找BST中第k小的元素
     * 
     * 算法步骤：
     * 1. 使用Morris中序遍历BST
     * 2. 在遍历过程中计数访问的节点
     * 3. 当计数器达到k时，返回当前节点的值
     * 4. 如果遍历完成仍未找到，返回-1
     * 
     * Morris遍历详细过程：
     * 1. 当前节点有左子树时：
     *    - 找到左子树的最右节点（中序前驱节点）
     *    - 如果前驱的right为空，建立线索，访问左子树
     *    - 如果前驱的right指向当前节点，拆除线索，访问当前节点
     * 2. 当前节点无左子树时：直接访问当前节点，然后访问右子树
     * 
     * @param head BST的根节点
     * @param k 要查找的第k小元素（从1开始计数）
     * @return 第k小的元素值，如果不存在返回-1
     */
    public static int kthSmallest(TreeNode head, int k) {
        // 边界检查
        if (head == null) {
            return -1;
        }
        
        TreeNode cur = head;  // 当前遍历节点
        TreeNode r = null;    // 临时节点，用于寻找前驱
        int idx = 1;          // 当前访问的节点计数（第几小）
        
        // Morris中序遍历
        while (cur != null) {
            r = cur.left;  // 指向当前节点的左子树
            
            if (r != null) {
                // 情况1：当前节点有左子树
                // 寻找左子树的最右节点（中序前驱节点）
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                
                if (r.right == null) {
                    // 第一次到达cur，建立线索：前驱节点指向cur
                    r.right = cur;
                    cur = cur.left;  // 转向左子树
                    continue;
                } else {
                    // 第二次到达cur（从左子树回溯），拆除线索
                    r.right = null;  // 恢复树的原始结构
                }
            }
            
            // 情况2：当前节点无左子树 或 左子树已访问完毕
            // 访问当前节点
            if (idx++ == k) {
                return cur.val;  // 找到第k小的元素
            }
            
            cur = cur.right;  // 转向右子树
        }
        
        return -1;  // 未找到第k小的元素
    }
}
