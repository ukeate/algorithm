package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 94. 二叉树的中序遍历 (Binary Tree Inorder Traversal)
 * 
 * 问题描述：
 * 给定一个二叉树的根节点 root，返回它的中序遍历结果。
 * 
 * 中序遍历顺序：左子树 -> 根节点 -> 右子树
 * 
 * 示例：
 * - 输入：root = [1,null,2,3]，输出：[1,3,2]
 * - 解释：中序遍历为：左子树(空) -> 根节点(1) -> 右子树的左子树(3) -> 右子树根节点(2)
 * 
 * 进阶要求：
 * 你可以使用迭代算法吗？能否用 O(1) 空间复杂度来实现？
 * 
 * 解法思路：
 * Morris遍历算法 - O(1)空间复杂度的中序遍历：
 * 1. 利用线索二叉树的思想，临时修改树的结构
 * 2. 对于每个节点，找到其前驱节点，建立临时连接
 * 3. 通过前驱节点的右指针指向当前节点，实现回溯
 * 4. 遍历完成后恢复树的原始结构
 * 
 * 核心思想：
 * - 当前节点有左子树：找到左子树的最右节点作为前驱，建立线索
 * - 当前节点无左子树：直接访问并转向右子树
 * - 通过线索的存在与否判断是第一次访问还是回溯访问
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P94_BinaryTreeInorderTraversal {
    
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
     * Morris中序遍历算法
     * 
     * 算法步骤：
     * 1. 初始化：cur指向root，结果列表ans
     * 2. 当cur不为空时循环：
     *    a) 如果cur有左子树：
     *       - 找到左子树的最右节点r（中序前驱节点）
     *       - 如果r.right为空：建立线索r.right=cur，cur左移
     *       - 如果r.right指向cur：说明左子树已访问完，断开线索，访问cur，cur右移
     *    b) 如果cur无左子树：直接访问cur，cur右移
     * 3. 返回遍历结果
     * 
     * @param root 二叉树根节点
     * @return 中序遍历结果列表
     */
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        if (root == null) {
            return ans;
        }
        
        TreeNode cur = root;  // 当前节点指针
        TreeNode r = null;    // 临时节点指针，用于寻找前驱
        
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
            // 访问当前节点
            ans.add(cur.val);
            cur = cur.right;  // 转向右子树
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 构建测试用例: [1,null,2,3]
        //     1
        //      \
        //       2
        //      /
        //     3
        TreeNode root = new TreeNode(1);
        root.right = new TreeNode(2);
        root.right.left = new TreeNode(3);
        
        List<Integer> result = inorderTraversal(root);
        System.out.println("中序遍历结果: " + result);  // 输出: [1, 3, 2]
        
        // 构建测试用例: [1,2,3,4,5]
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(3);
        root2.left.left = new TreeNode(4);
        root2.left.right = new TreeNode(5);
        
        List<Integer> result2 = inorderTraversal(root2);
        System.out.println("中序遍历结果: " + result2);  // 输出: [4, 2, 5, 1, 3]
    }
}
