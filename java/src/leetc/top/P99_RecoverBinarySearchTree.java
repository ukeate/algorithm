package leetc.top;

/**
 * LeetCode 99. 恢复二叉搜索树 (Recover Binary Search Tree)
 * 
 * 问题描述：
 * 给你二叉搜索树的根节点 root，该树中的恰好两个节点的值被错误地交换。
 * 请在不改变其结构的情况下，恢复这棵树。
 * 
 * 进阶要求：使用 O(1) 空间复杂度的解法。
 * 
 * 示例：
 * - 输入：root = [1,3,null,null,2]，输出：[3,1,null,null,2]
 * - 输入：root = [3,1,4,null,null,2]，输出：[2,1,4,null,null,3]
 * 
 * 解法思路：
 * Morris中序遍历 + 错误节点检测：
 * 1. 正确的BST中序遍历应该是严格递增的
 * 2. 两个节点交换后，中序遍历中会出现1-2个逆序对
 * 3. 情况1：相邻节点交换 -> 1个逆序对，交换这两个节点
 * 4. 情况2：不相邻节点交换 -> 2个逆序对，交换第一个逆序对的第一个节点和第二个逆序对的第二个节点
 * 
 * 核心思想：
 * - 使用Morris遍历实现O(1)空间的中序遍历
 * - 在遍历过程中检测逆序对，记录错误节点
 * - 找到两个错误节点后交换它们的值
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P99_RecoverBinarySearchTree {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int val;        // 节点值
        public TreeNode left;  // 左子节点
        public TreeNode right; // 右子节点
        
        public TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 使用Morris中序遍历找到两个错误交换的节点
     * 
     * 算法思路：
     * 1. 在中序遍历过程中，正常的BST应该严格递增
     * 2. 两个节点交换后会产生逆序对：
     *    - 相邻交换：产生1个逆序对 (a > b)，错误节点就是a和b
     *    - 不相邻交换：产生2个逆序对 (a > b, c > d)，错误节点是a和d
     * 3. 记录第一个和第二个错误节点的位置
     * 
     * 详细逻辑：
     * - e1记录第一个错误节点（第一个逆序对的前节点）
     * - e2记录第二个错误节点（每个逆序对的后节点，不断更新）
     * - 遍历结束后，e1和e2就是需要交换的两个节点
     * 
     * @param head 二叉树根节点
     * @return 包含两个错误节点的数组 [错误节点1, 错误节点2]
     */
    private static TreeNode[] twoErrors(TreeNode head) {
        TreeNode[] ans = new TreeNode[2];
        if (head == null) {
            return ans;
        }
        
        TreeNode cur = head;   // 当前遍历节点
        TreeNode r = null;     // 临时节点，用于寻找前驱
        TreeNode pre = null;   // 前驱节点
        TreeNode e1 = null;    // 第一个错误节点
        TreeNode e2 = null;    // 第二个错误节点
        
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
            // 检查是否存在逆序对
            if (pre != null && pre.val >= cur.val) {
                // 发现逆序对
                e1 = e1 == null ? pre : e1;  // 第一个错误节点：第一个逆序对的前节点
                e2 = cur;                     // 第二个错误节点：当前逆序对的后节点（持续更新）
            }
            
            pre = cur;           // 更新前驱节点
            cur = cur.right;     // 转向右子树
        }
        
        ans[0] = e1;
        ans[1] = e2;
        return ans;
    }

    /**
     * 恢复二叉搜索树
     * 
     * 算法步骤：
     * 1. 使用Morris中序遍历找到两个错误交换的节点
     * 2. 交换这两个节点的值来恢复BST性质
     * 3. 树的结构保持不变，只修改节点值
     * 
     * @param root 被破坏的BST根节点
     */
    public static void recoverTree(TreeNode root) {
        // 找到两个错误的节点
        TreeNode[] errors = twoErrors(root);
        
        // 交换两个错误节点的值
        if (errors[0] != null && errors[1] != null) {
            int tmp = errors[0].val;
            errors[0].val = errors[1].val;
            errors[1].val = tmp;
        }
    }
    
    /**
     * 辅助方法：中序遍历打印树节点（用于测试）
     */
    private static void inorderPrint(TreeNode root) {
        if (root == null) return;
        inorderPrint(root.left);
        System.out.print(root.val + " ");
        inorderPrint(root.right);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1: [1,3,null,null,2] -> [3,1,null,null,2]
        //   1       ->     3
        //    \            /
        //     3          1
        //    /            \
        //   2              2
        TreeNode root1 = new TreeNode(1);
        root1.right = new TreeNode(3);
        root1.right.left = new TreeNode(2);
        
        System.out.println("测试用例1:");
        System.out.print("修复前中序遍历: ");
        inorderPrint(root1);
        System.out.println();
        
        recoverTree(root1);
        
        System.out.print("修复后中序遍历: ");
        inorderPrint(root1);
        System.out.println();
        System.out.println("---");
        
        // 测试用例2: [3,1,4,null,null,2] -> [2,1,4,null,null,3]
        //     3       ->     2
        //    / \            / \
        //   1   4          1   4
        //      /              /
        //     2              3
        TreeNode root2 = new TreeNode(3);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(4);
        root2.right.left = new TreeNode(2);
        
        System.out.println("测试用例2:");
        System.out.print("修复前中序遍历: ");
        inorderPrint(root2);
        System.out.println();
        
        recoverTree(root2);
        
        System.out.print("修复后中序遍历: ");
        inorderPrint(root2);
        System.out.println();
    }
}
