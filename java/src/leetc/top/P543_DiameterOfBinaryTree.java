package leetc.top;

/**
 * LeetCode 543. 二叉树的直径 (Diameter of Binary Tree)
 * 
 * 问题描述：
 * 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
 * 这条路径可能穿过也可能不穿过根结点。
 * 
 * 注意：两结点之间的路径长度是以它们之间边的数目表示。
 * 
 * 示例：
 * 给定二叉树
 *           1
 *          / \
 *         2   3
 *        / \
 *       4   5
 * 返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。
 * 
 * 解法思路：
 * 树形DP + 递归信息收集：
 * 1. 直径可能经过当前节点，也可能完全在左子树或右子树中
 * 2. 经过当前节点的最大直径 = 左子树高度 + 右子树高度
 * 3. 不经过当前节点的最大直径 = max(左子树直径, 右子树直径)
 * 4. 使用递归同时收集两个信息：子树的最大直径和子树的高度
 * 
 * 核心思想：
 * - 每个节点都可能是直径路径的"转折点"
 * - 需要向上传递高度信息，同时维护全局最大直径
 * - 一次遍历同时解决高度计算和直径计算
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈深度，h为树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/diameter-of-binary-tree/
 */
public class P543_DiameterOfBinaryTree {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int val;          // 节点值
        public TreeNode left;    // 左子节点
        public TreeNode right;   // 右子节点
        
        public TreeNode(int val) {
            this.val = val;
        }
    }

    /**
     * 封装递归返回的信息
     * 包含子树的最大直径和子树的高度
     */
    private static class Info {
        public int maxDistance; // 子树中的最大直径（边的数量）
        public int height;      // 子树的高度（从当前节点到叶子节点的最大边数 + 1）
        
        public Info(int m, int h) {
            maxDistance = m;
            height = h;
        }
    }

    /**
     * 递归处理每个节点，返回以该节点为根的子树信息
     * 
     * 对于每个节点x，需要考虑三种情况的直径：
     * 1. 完全在左子树中的最大直径
     * 2. 完全在右子树中的最大直径  
     * 3. 经过节点x的最大直径（左子树高度 + 右子树高度）
     * 
     * @param x 当前处理的节点
     * @return 包含最大直径和树高的信息对象
     */
    private static Info process(TreeNode x) {
        // 基础情况：空节点
        if (x == null) {
            return new Info(0, 0); // 空树的直径为0，高度为0
        }
        
        // 递归获取左右子树的信息
        Info li = process(x.left);  // 左子树信息
        Info ri = process(x.right); // 右子树信息
        
        // 计算以x为根的子树的最大直径
        // 三种情况的最大值：
        // 1. 左子树内部的最大直径
        // 2. 右子树内部的最大直径  
        // 3. 经过节点x的直径（左子树高度 + 右子树高度）
        int maxDistance = Math.max(
            Math.max(li.maxDistance, ri.maxDistance), // 情况1和2
            li.height + ri.height                     // 情况3：经过当前节点
        );
        
        // 计算以x为根的子树的高度
        // 当前节点的高度 = max(左子树高度, 右子树高度) + 1
        int height = Math.max(li.height, ri.height) + 1;
        
        return new Info(maxDistance, height);
    }

    /**
     * 计算二叉树的直径
     * 
     * @param root 二叉树根节点
     * @return 树的直径（边的数量）
     */
    public static int diameterOfBinaryTree(TreeNode root) {
        return process(root).maxDistance;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        
        System.out.println("测试用例1:");
        System.out.println("树结构: 1(2(4,5),3)");
        System.out.println("直径: " + diameterOfBinaryTree(root1));
        System.out.println("期望: 3 (路径: 4-2-1-3 或 5-2-1-3)");
        System.out.println();
        
        // 测试用例2：线性树
        //   1
        //  /
        // 2
        ///
        //3
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.left.left = new TreeNode(3);
        
        System.out.println("测试用例2 (线性树):");
        System.out.println("树结构: 1(2(3,null),null)");
        System.out.println("直径: " + diameterOfBinaryTree(root2));
        System.out.println("期望: 2 (路径: 3-2-1)");
        System.out.println();
        
        // 测试用例3：单节点
        TreeNode root3 = new TreeNode(1);
        System.out.println("测试用例3 (单节点):");
        System.out.println("树结构: 1");
        System.out.println("直径: " + diameterOfBinaryTree(root3));
        System.out.println("期望: 0 (单节点无边)");
        System.out.println();
        
        // 测试用例4：空树
        System.out.println("测试用例4 (空树):");
        System.out.println("直径: " + diameterOfBinaryTree(null));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例5：不平衡树
        //     1
        //    /
        //   2
        //  / \
        // 3   4
        //    / \
        //   5   6
        TreeNode root5 = new TreeNode(1);
        root5.left = new TreeNode(2);
        root5.left.left = new TreeNode(3);
        root5.left.right = new TreeNode(4);
        root5.left.right.left = new TreeNode(5);
        root5.left.right.right = new TreeNode(6);
        
        System.out.println("测试用例5 (不平衡树):");
        System.out.println("树结构: 1(2(3,4(5,6)),null)");
        System.out.println("直径: " + diameterOfBinaryTree(root5));
        System.out.println("期望: 4 (路径: 3-2-4-5 或类似路径)");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 树形DP，一次遍历收集所需信息");
        System.out.println("- 时间复杂度: O(n) - 每个节点访问一次");
        System.out.println("- 空间复杂度: O(h) - 递归栈深度");
        System.out.println("- 关键技巧: 同时维护高度和直径两个信息");
        System.out.println("- 直径可能经过根节点，也可能在某个子树内部");
    }
}
