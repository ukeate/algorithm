package leetc.top;

/**
 * LeetCode 285. 二叉搜索树中的中序后继 (Inorder Successor in BST)
 * 
 * 问题描述：
 * 给定一棵二叉搜索树和其中的一个节点 p，找到该节点在树中的中序后继。
 * 如果节点没有中序后继，请返回 null。
 * 
 * 中序后继的定义：节点 p 的中序后继是中序遍历顺序中 p 的下一个节点。
 * 
 * 示例：
 * 输入：root = [2,1,3], p = 1
 * 输出：2
 * 解释：这里 1 的中序后继是 2。注意 p 和返回值都应是 TreeNode 类型。
 * 
 * 解法思路：
 * Morris中序遍历 + 前驱节点跟踪：
 * 1. 使用Morris遍历实现O(1)空间复杂度的中序遍历
 * 2. 在遍历过程中跟踪前一个访问的节点
 * 3. 当前一个节点恰好是目标节点p时，当前节点就是中序后继
 * 4. Morris遍历通过临时建立线索来实现无栈遍历
 * 
 * 核心思想：
 * - BST的中序遍历是有序序列
 * - 中序后继就是中序遍历中目标节点的下一个节点
 * - Morris遍历避免使用递归栈，实现O(1)空间复杂度
 * - 通过维护前驱节点来识别后继关系
 * 
 * Morris遍历原理：
 * 1. 如果当前节点有左子树，找到左子树的最右节点（中序前驱）
 * 2. 建立前驱到当前节点的线索，然后访问左子树
 * 3. 当从左子树回到当前节点时，拆除线索，访问当前节点
 * 4. 然后访问右子树
 * 
 * 后继查找逻辑：
 * - 维护pre指针记录前一个访问的节点
 * - 当pre == p时，当前节点cur就是p的中序后继
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/inorder-successor-in-bst/
 */
public class P285_InorderSuccessorInBST {
    
    /**
     * 二叉搜索树节点定义
     */
    public static class TreeNode {
        public int val;      // 节点值
        public TreeNode left;  // 左子节点
        public TreeNode right; // 右子节点
    }

    /**
     * 查找BST中节点p的中序后继
     * 
     * 算法步骤：
     * 1. 使用Morris中序遍历BST
     * 2. 维护pre指针记录前一个访问的节点
     * 3. 在遍历过程中，当pre等于目标节点p时，当前节点就是中序后继
     * 4. 如果遍历完成仍未找到，说明p是最大节点，返回null
     * 
     * Morris遍历详细过程：
     * 1. 当前节点有左子树时：
     *    - 找到左子树的最右节点（中序前驱节点）
     *    - 如果前驱的right为空，建立线索，访问左子树
     *    - 如果前驱的right指向当前节点，拆除线索，访问当前节点
     * 2. 当前节点无左子树时：直接访问当前节点，然后访问右子树
     * 
     * 中序后继判断：
     * - 在每次访问节点时，检查前一个节点是否为目标节点p
     * - 如果是，当前节点就是p的中序后继
     * 
     * @param head BST的根节点
     * @param p 要查找中序后继的目标节点
     * @return p的中序后继节点，如果不存在返回null
     */
    public static TreeNode inorderSuccessor(TreeNode head, TreeNode p) {
        // 边界检查
        if (head == null) {
            return null;
        }
        
        TreeNode cur = head;  // 当前遍历节点
        TreeNode r = null;    // 临时节点，用于寻找前驱
        TreeNode pre = null;  // 前一个访问的节点
        
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
            // 访问当前节点，检查是否找到中序后继
            if (pre == p) {
                return cur;  // 找到p的中序后继
            } else {
                pre = cur;   // 更新前驱节点
            }
            
            cur = cur.right;  // 转向右子树
        }
        
        return null;  // 未找到中序后继（p是最大节点）
    }
}
