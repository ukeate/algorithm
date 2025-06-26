package leetc.top;

/**
 * LeetCode 687. 最长同值路径 (Longest Univalue Path)
 * 
 * 问题描述：
 * 给定一个二叉树，找到最长的路径，这个路径中的每个节点具有相同值。
 * 这条路径可以经过也可以不经过根节点。
 * 
 * 注意：两个节点之间的路径长度由它们之间的边数表示。
 * 
 * 示例：
 * 输入:
 *               5
 *              / \
 *             4   5
 *            / \   \
 *           1   1   5
 * 输出: 2
 * 解释: 最长的同值路径是 [5,5,5]，长度为2。
 * 
 * 输入:
 *               1
 *              / \
 *             4   5
 *            / \   \
 *           4   4   5
 * 输出: 2
 * 解释: 最长的同值路径是 [4,4,4] 或 [5,5,5]，长度为2。
 * 
 * 解法思路：
 * 树形DP + 递归信息收集：
 * 1. 对于每个节点，需要知道以该节点为端点的最长同值路径长度
 * 2. 以及以该节点为根的子树中的最长同值路径长度
 * 3. 同值路径可能经过当前节点（连接左右子树），也可能完全在某个子树中
 * 4. 递归收集信息，同时更新全局最大值
 * 
 * 核心思想：
 * - 每个节点返回两个信息：向上延伸的最长同值路径长度 + 子树内最长同值路径长度
 * - 路径延伸条件：父子节点值相同
 * - 路径合并：左右子树同值路径可以通过当前节点合并
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈深度，h为树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/longest-univalue-path/
 */
public class P687_LongestUnivaluePath {
    
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
     * 封装递归返回的信息
     */
    private static class Info {
        public int len; // 从当前节点向下延伸的最长同值路径长度（节点数）
        public int max; // 当前子树中的最长同值路径长度（节点数）

        public Info(int l, int m) {
            len = l;
            max = m;
        }
    }

    /**
     * 递归处理每个节点，返回相关信息
     * 
     * @param x 当前处理的节点
     * @return 包含路径长度信息的Info对象
     */
    private static Info process(TreeNode x) {
        if (x == null) {
            return new Info(0, 0); // 空节点没有路径
        }
        
        TreeNode l = x.left, r = x.right;
        Info li = process(l); // 左子树信息
        Info ri = process(r); // 右子树信息
        
        // 计算从当前节点向下延伸的最长同值路径长度
        int len = 1; // 至少包含当前节点自己
        
        // 检查能否向左子树延伸
        if (l != null && l.val == x.val) {
            len = li.len + 1;
        }
        
        // 检查能否向右子树延伸（取更长的一边）
        if (r != null && r.val == x.val) {
            len = Math.max(len, ri.len + 1);
        }
        
        // 计算当前子树中的最长同值路径长度
        // 考虑三种情况：左子树内部、右子树内部、经过当前节点
        int max = Math.max(Math.max(li.max, ri.max), len);
        
        // 特殊情况：如果左右子树都与当前节点同值，可以形成经过当前节点的路径
        if (l != null && r != null && l.val == x.val && r.val == x.val) {
            max = Math.max(max, li.len + ri.len + 1);
        }
        
        return new Info(len, max);
    }

    /**
     * 计算最长同值路径的长度
     * 
     * @param root 二叉树根节点
     * @return 最长同值路径的长度（边数）
     */
    public static int longestUnivaluePath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 获取结果并转换为边数（节点数-1）
        return process(root).max - 1;
    }
}
