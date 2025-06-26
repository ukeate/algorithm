package leetc.top;

/**
 * LeetCode 236. 二叉树的最近公共祖先 (Lowest Common Ancestor of a Binary Tree)
 * 
 * 问题描述：
 * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
 * 
 * 百度百科中最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
 * 满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。"
 * 
 * 示例：
 * 输入：root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
 * 输出：3
 * 解释：节点 5 和节点 1 的最近公共祖先是节点 3 。
 * 
 * 输入：root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
 * 输出：5
 * 解释：节点 5 和节点 4 的最近公共祖先是节点 5 。因为根据定义最近公共祖先节点可以为节点本身。
 * 
 * 解法思路：
 * 递归 + 信息收集：
 * 1. 对每个节点收集三个信息：
 *    - 是否找到了节点o1
 *    - 是否找到了节点o2  
 *    - 最近公共祖先节点（如果已确定）
 * 2. 递归处理左右子树，收集子树信息
 * 3. 根据子树信息和当前节点，更新当前节点的信息
 * 4. 最近公共祖先的确定规则：
 *    - 如果左子树或右子树已经找到LCA，直接返回
 *    - 如果当前节点同时包含o1和o2，则当前节点就是LCA
 * 
 * 核心思想：
 * - 自底向上收集信息，一次遍历解决问题
 * - 利用递归的回溯特性，在回溯过程中确定LCA
 * - 一旦确定LCA，向上传递，不再改变
 * 
 * 时间复杂度：O(n) - 需要遍历树中的每个节点一次
 * 空间复杂度：O(h) - h为树的高度，递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-tree/
 */
public class P236_LowestCommonAncestorOfBinaryTree {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int val;      // 节点值
        public TreeNode left;  // 左子节点
        public TreeNode right; // 右子节点
    }

    /**
     * 递归信息类：封装每个节点的子树信息
     */
    private static class Info {
        public TreeNode ans;   // 最近公共祖先节点（如果已确定）
        public boolean findO1; // 是否在子树中找到了节点o1
        public boolean findO2; // 是否在子树中找到了节点o2
        
        /**
         * 构造函数
         * 
         * @param a 最近公共祖先节点
         * @param f1 是否找到o1
         * @param f2 是否找到o2
         */
        public Info(TreeNode a, boolean f1, boolean f2) {
            ans = a;
            findO1 = f1;
            findO2 = f2;
        }
    }

    /**
     * 递归处理函数：收集以x为根的子树信息
     * 
     * 算法逻辑：
     * 1. 递归收集左右子树信息
     * 2. 判断当前节点x是否为o1或o2
     * 3. 更新findO1和findO2标记
     * 4. 确定最近公共祖先：
     *    - 优先返回子树中已确定的LCA
     *    - 如果子树中未确定，检查当前节点是否同时包含o1和o2
     * 
     * @param x 当前节点
     * @param o1 目标节点1
     * @param o2 目标节点2
     * @return 包含子树信息的Info对象
     */
    private static Info process(TreeNode x, TreeNode o1, TreeNode o2) {
        // 递归边界：空节点
        if (x == null) {
            return new Info(null, false, false);
        }
        
        // 递归收集左右子树信息
        Info li = process(x.left, o1, o2);   // 左子树信息
        Info ri = process(x.right, o1, o2);  // 右子树信息
        
        // 更新当前节点的查找状态
        // 当前节点包含o1：当前节点是o1 或 左右子树中有o1
        boolean findO1 = x == o1 || li.findO1 || ri.findO1;
        // 当前节点包含o2：当前节点是o2 或 左右子树中有o2
        boolean findO2 = x == o2 || li.findO2 || ri.findO2;
        
        // 确定最近公共祖先
        TreeNode ans = null;
        
        // 优先级1：如果左子树已经确定了LCA，直接使用
        if (li.ans != null) {
            ans = li.ans;
        }
        
        // 优先级2：如果右子树已经确定了LCA，直接使用  
        if (ri.ans != null) {
            ans = ri.ans;
        }
        
        // 优先级3：如果子树中都没有确定LCA，检查当前节点
        if (ans == null) {
            // 如果当前节点同时包含o1和o2，则当前节点就是LCA
            if (findO1 && findO2) {
                ans = x;
            }
        }
        
        return new Info(ans, findO1, findO2);
    }

    /**
     * 查找二叉树中两个节点的最近公共祖先
     * 
     * @param head 二叉树根节点
     * @param o1 目标节点1
     * @param o2 目标节点2
     * @return 最近公共祖先节点
     */
    public static TreeNode lowestCommonAncestor(TreeNode head, TreeNode o1, TreeNode o2) {
        return process(head, o1, o2).ans;
    }
}
