package base.tree2;

import java.util.ArrayList;

/**
 * 寻找二叉树中最大的子二叉搜索树
 * 
 * 问题描述：给定一个二叉树，找到其中最大的子二叉搜索树，并返回其节点数量
 * 
 * 二叉搜索树性质：
 * - 左子树所有节点值 < 根节点值 < 右子树所有节点值
 * - 左右子树也都是二叉搜索树
 * 
 * 解题思路：
 * 使用树形DP，为每个节点收集以下信息：
 * 1. 以当前节点为根的子树中，最大BST的节点数
 * 2. 整个子树的节点数
 * 3. 整个子树的最大值和最小值
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(h)（递归栈空间）
 */
// https://leetcode.com/problems/largest-bst-subtree
public class MaxSubBST {
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int val;         // 节点值
        public TreeNode left;   // 左子节点
        public TreeNode right;  // 右子节点

        public TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 封装子树信息的类
     */
    private static class Info {
        public int maxBst;  // 当前子树中最大BST的节点数
        public int size;    // 当前子树的总节点数
        public int max;     // 当前子树的最大值
        public int min;     // 当前子树的最小值

        public Info(int m, int s, int max, int min) {
            maxBst = m;
            size = s;
            this.max = max;
            this.min = min;
        }
    }

    /**
     * 树形DP的递归实现
     * @param x 当前节点
     * @return 当前子树的信息
     */
    private static Info process(TreeNode x) {
        if (x == null) {
            return null;  // 空节点返回null
        }
        
        // 递归获取左右子树信息
        Info li = process(x.left);
        Info ri = process(x.right);
        
        // 计算当前子树的最大值、最小值和总节点数
        int max = x.val;
        int min = x.val;
        int size = 1;
        
        if (li != null) {
            max = Math.max(li.max, max);
            min = Math.min(li.min, min);
            size += li.size;
        }
        if (ri != null) {
            max = Math.max(ri.max, max);
            min = Math.min(ri.min, min);
            size += ri.size;
        }
        
        // 计算当前子树中最大BST的节点数
        // 情况1：左子树中的最大BST
        int p1 = -1;
        if (li != null) {
            p1 = li.maxBst;
        }
        
        // 情况2：右子树中的最大BST
        int p2 = -1;
        if (ri != null) {
            p2 = ri.maxBst;
        }
        
        // 情况3：以当前节点为根的整棵子树是BST
        int p0 = -1;
        
        // 检查左右子树是否都是BST
        boolean lBST = li == null ? true : (li.maxBst == li.size);
        boolean rBST = ri == null ? true : (ri.maxBst == ri.size);
        
        if (lBST && rBST) {
            // 检查BST的大小关系：左子树最大值 < 当前值 < 右子树最小值
            boolean lLess = li == null ? true : (li.max < x.val);
            boolean rMore = ri == null ? true : (x.val < ri.min);
            
            if (lLess && rMore) {
                // 整棵子树是BST
                int lSize = li == null ? 0 : li.size;
                int rSize = ri == null ? 0 : ri.size;
                p0 = lSize + rSize + 1;
            }
        }
        
        return new Info(Math.max(p1, Math.max(p2, p0)), size, max, min);
    }

    /**
     * 寻找最大子二叉搜索树的节点数
     * @param head 树的根节点
     * @return 最大子BST的节点数
     */
    public static int maxSubBST(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return process(head).maxBst;
    }

    // ==================== 暴力方法（用于验证）====================

    /**
     * 中序遍历收集节点
     * @param head 当前节点
     * @param arr 存储节点的列表
     */
    private static void in(TreeNode head, ArrayList<TreeNode> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);
        arr.add(head);
        in(head.right, arr);
    }

    /**
     * 检查以head为根的子树是否为BST，返回节点数
     * @param head 子树根节点
     * @return BST节点数，如果不是BST返回0
     */
    private static int bstSize(TreeNode head) {
        if (head == null) {
            return 0;
        }
        
        // 中序遍历
        ArrayList<TreeNode> arr = new ArrayList<>();
        in(head, arr);
        
        // 检查是否严格递增
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).val <= arr.get(i - 1).val) {
                return 0;  // 不是BST
            }
        }
        return arr.size();  // 是BST，返回节点数
    }

    /**
     * 暴力方法：检查每个子树
     * @param head 当前节点
     * @return 最大子BST的节点数
     */
    private static int maxSubBstSure(TreeNode head) {
        if (head == null) {
            return 0;
        }
        
        // 检查以当前节点为根的子树是否为BST
        int h = bstSize(head);
        if (h != 0) {
            return h;  // 如果是BST，直接返回节点数
        }
        
        // 如果不是BST，递归检查左右子树
        return Math.max(maxSubBstSure(head.left), maxSubBstSure(head.right));
    }

    // ==================== 测试工具方法 ====================

    /**
     * 随机生成树节点（递归）
     * @param level 当前层级
     * @param maxLevel 最大层级
     * @param maxVal 最大节点值
     * @return 随机生成的节点
     */
    private static TreeNode randomLevel(int level, int maxLevel, int maxVal) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        TreeNode head = new TreeNode((int) ((maxVal + 1) * Math.random()));
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        return head;
    }

    /**
     * 随机生成二叉树
     * @param maxLevel 最大层级
     * @param maxVal 最大节点值
     * @return 随机生成的树根节点
     */
    private static TreeNode randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

    public static void main(String[] args) {
        int times = 100000;   // 测试次数
        int maxLevel = 10;    // 最大层级
        int maxVal = 100;     // 最大节点值
        System.out.println("test begin");
        
        // 对拍测试：比较优化算法和暴力算法的结果
        for (int i = 0; i < times; i++) {
            TreeNode head = randomTree(maxLevel, maxVal);
            int ans1 = maxSubBST(head);         // 优化算法
            int ans2 = maxSubBstSure(head);     // 暴力算法
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}

