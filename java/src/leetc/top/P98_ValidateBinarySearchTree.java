package leetc.top;

/**
 * LeetCode 98. 验证二叉搜索树 (Validate Binary Search Tree)
 * 
 * 问题描述：
 * 给你一个二叉树的根节点 root，判断其是否是一个有效的二叉搜索树。
 * 
 * 有效二叉搜索树定义如下：
 * - 节点的左子树只包含小于当前节点的数
 * - 节点的右子树只包含大于当前节点的数
 * - 所有左子树和右子树自身必须也是二叉搜索树
 * 
 * 示例：
 * - 输入：root = [2,1,3]，输出：true
 * - 输入：root = [5,1,4,null,null,3,6]，输出：false
 *   解释：根节点的值是5，但右子节点的值是4
 * 
 * 解法思路：
 * Morris中序遍历 + 严格递增性检查：
 * 1. 二叉搜索树的中序遍历结果必须是严格递增的
 * 2. 使用Morris遍历算法实现O(1)空间复杂度的中序遍历
 * 3. 在遍历过程中检查前一个节点值是否小于当前节点值
 * 4. 一旦发现逆序对，立即返回false
 * 
 * 核心思想：
 * - 利用BST的性质：中序遍历必须严格递增
 * - Morris遍历：通过临时修改树结构实现常数空间遍历
 * - 在线检查：遍历过程中实时验证，无需存储完整序列
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P98_ValidateBinarySearchTree {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;           // 节点值
        TreeNode left;     // 左子节点
        TreeNode right;    // 右子节点
        
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * 使用Morris中序遍历验证二叉搜索树
     * 
     * 算法步骤：
     * 1. 初始化：cur指向root，pre记录前驱节点值，ans记录结果
     * 2. Morris遍历过程：
     *    a) 如果cur有左子树：建立/断开线索，控制遍历流程
     *    b) 访问当前节点：检查是否满足BST性质（pre < cur.val）
     *    c) 更新前驱节点值，继续遍历右子树
     * 3. 遍历完成后返回验证结果
     * 
     * 关键点：
     * - pre使用Integer类型处理第一个节点的情况
     * - 严格小于检查：pre >= cur.val时违反BST性质
     * - 及时更新ans，但继续遍历以恢复树结构
     * 
     * @param root 二叉树根节点
     * @return 是否为有效的二叉搜索树
     */
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;  // 空树是有效的BST
        }
        
        TreeNode cur = root;     // 当前遍历节点
        TreeNode r = null;       // 临时节点，用于寻找前驱
        Integer pre = null;      // 前驱节点的值（使用Integer处理第一个节点）
        boolean ans = true;      // 验证结果
        
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
                    // 第二次到达cur（从左子树回溯），断开线索
                    r.right = null;  // 恢复树的原始结构
                }
            }
            
            // 情况2：当前节点无左子树 或 左子树已访问完毕
            // 访问当前节点，检查BST性质
            if (pre != null && pre >= cur.val) {
                ans = false;  // 发现逆序对，不是有效BST
                // 注意：这里不立即返回，而是继续遍历以恢复树结构
            }
            
            pre = cur.val;        // 更新前驱节点值
            cur = cur.right;      // 转向右子树
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P98_ValidateBinarySearchTree validator = new P98_ValidateBinarySearchTree();
        
        // 测试用例1: [2,1,3] - 有效BST
        //     2
        //    / \
        //   1   3
        TreeNode root1 = new TreeNode(2);
        root1.left = new TreeNode(1);
        root1.right = new TreeNode(3);
        System.out.println("测试用例1 [2,1,3]: " + validator.isValidBST(root1));  // 输出: true
        
        // 测试用例2: [5,1,4,null,null,3,6] - 无效BST
        //       5
        //      / \
        //     1   4
        //        / \
        //       3   6
        TreeNode root2 = new TreeNode(5);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(4);
        root2.right.left = new TreeNode(3);
        root2.right.right = new TreeNode(6);
        System.out.println("测试用例2 [5,1,4,null,null,3,6]: " + validator.isValidBST(root2));  // 输出: false
        
        // 测试用例3: [1,1] - 无效BST（不允许相等）
        //   1
        //  /
        // 1
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(1);
        System.out.println("测试用例3 [1,1]: " + validator.isValidBST(root3));  // 输出: false
    }
}
