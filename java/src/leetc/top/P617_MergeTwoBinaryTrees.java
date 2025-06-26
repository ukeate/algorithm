package leetc.top;

/**
 * LeetCode 617. 合并二叉树 (Merge Two Binary Trees)
 * 
 * 问题描述：
 * 给你两棵二叉树：root1 和 root2。
 * 想象一下，当你将其中一棵覆盖到另一棵之上时，两棵树上的一些节点将会重叠。
 * 你需要将这两棵树合并成一棵新二叉树。
 * 合并的规则是：如果两个节点重叠，那么将这两个节点的值相加作为合并后节点的新值；
 * 否则，不为 null 的节点将直接作为新二叉树的节点。
 * 
 * 示例：
 * 输入：
 * Tree 1     Tree 2
 *   1          2
 *  / \        / \
 * 3   2      1   3
 * |           \   \
 * 5            4   7
 * 
 * 输出：
 * Merged tree:
 *     3
 *    / \
 *   4   5
 *  / \   \
 * 5   4   7
 * 
 * 解法思路：
 * 递归合并：
 * 1. 如果其中一棵树为空，直接返回另一棵树
 * 2. 如果两棵树都不为空，创建新节点，值为两个节点值的和
 * 3. 递归合并左右子树
 * 4. 每个节点的处理都是独立的，符合递归的思想
 * 
 * 时间复杂度：O(min(m,n)) - m和n分别是两棵树的节点数，只需遍历较小树的所有节点
 * 空间复杂度：O(min(m,n)) - 递归栈的深度，最坏情况下为较小树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/merge-two-binary-trees/
 */
public class P617_MergeTwoBinaryTrees {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int val;          // 节点值
        public TreeNode left;    // 左子节点
        public TreeNode right;   // 右子节点
        
        public TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 合并两棵二叉树
     * 
     * 递归思路：
     * 1. 基础情况：如果某棵树为空，返回另一棵树（可能也为空）
     * 2. 递归情况：两棵树都不为空时，创建新节点并递归合并子树
     * 3. 新节点的值是两个对应节点值的和
     * 4. 递归地合并左右子树
     * 
     * @param t1 第一棵二叉树的根节点
     * @param t2 第二棵二叉树的根节点
     * @return 合并后的二叉树根节点
     */
    public static TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        // 基础情况1：第一棵树为空，返回第二棵树
        if (t1 == null) {
            return t2;
        }
        
        // 基础情况2：第二棵树为空，返回第一棵树
        if (t2 == null) {
            return t1;
        }
        
        // 递归情况：两棵树都不为空，创建新节点
        TreeNode merge = new TreeNode(t1.val + t2.val);
        
        // 递归合并左子树
        merge.left = mergeTrees(t1.left, t2.left);
        
        // 递归合并右子树
        merge.right = mergeTrees(t1.right, t2.right);
        
        return merge;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 构建测试用例1
        // Tree 1:    1
        //           / \
        //          3   2
        //         /
        //        5
        TreeNode t1 = new TreeNode(1);
        t1.left = new TreeNode(3);
        t1.right = new TreeNode(2);
        t1.left.left = new TreeNode(5);
        
        // Tree 2:    2
        //           / \
        //          1   3
        //           \   \
        //            4   7
        TreeNode t2 = new TreeNode(2);
        t2.left = new TreeNode(1);
        t2.right = new TreeNode(3);
        t2.left.right = new TreeNode(4);
        t2.right.right = new TreeNode(7);
        
        System.out.println("测试用例1:");
        System.out.println("Tree 1: " + treeToString(t1));
        System.out.println("Tree 2: " + treeToString(t2));
        
        TreeNode merged = mergeTrees(t1, t2);
        System.out.println("Merged: " + treeToString(merged));
        System.out.println("期望结果: [3,4,5,5,null,null,7]");
        System.out.println();
        
        // 测试用例2：一棵树为空
        TreeNode t3 = new TreeNode(1);
        TreeNode t4 = null;
        System.out.println("测试用例2 (一棵树为空):");
        System.out.println("Tree 1: " + treeToString(t3));
        System.out.println("Tree 2: null");
        TreeNode merged2 = mergeTrees(t3, t4);
        System.out.println("Merged: " + treeToString(merged2));
        System.out.println("期望结果: [1]");
        System.out.println();
        
        // 测试用例3：两棵树都为空
        System.out.println("测试用例3 (两棵树都为空):");
        TreeNode merged3 = mergeTrees(null, null);
        System.out.println("Merged: " + treeToString(merged3));
        System.out.println("期望结果: null");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点：");
        System.out.println("- 简洁的递归解法，代码易理解");
        System.out.println("- 时间复杂度：O(min(m,n))");
        System.out.println("- 空间复杂度：O(min(m,n)) (递归栈)");
        System.out.println("- 处理节点为空的情况很自然");
    }
    
    /**
     * 辅助方法：将二叉树转换为字符串表示（层序遍历）
     */
    private static String treeToString(TreeNode root) {
        if (root == null) return "null";
        
        StringBuilder sb = new StringBuilder();
        java.util.Queue<TreeNode> queue = new java.util.LinkedList<>();
        queue.offer(root);
        
        sb.append("[");
        boolean first = true;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (!first) sb.append(",");
            first = false;
            
            if (node == null) {
                sb.append("null");
            } else {
                sb.append(node.val);
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        
        // 移除尾部的null
        String result = sb.toString();
        while (result.endsWith(",null")) {
            result = result.substring(0, result.lastIndexOf(",null"));
        }
        
        return result + "]";
    }
}
