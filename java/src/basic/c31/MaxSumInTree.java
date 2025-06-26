package basic.c31;

/**
 * 二叉树中的最大路径和问题
 * 
 * 问题描述：给定一个二叉树，返回从根节点到叶子节点的最大路径和
 * 
 * 解题方案：提供了三种不同的解决方法
 * 1. 方法1：使用全局变量的先序遍历
 * 2. 方法2：递归后序遍历，返回最大路径和
 * 3. 方法3：使用树形DP，包含任意路径的最大和（更通用）
 * 
 * 时间复杂度：O(N)，其中N是树中节点的个数
 * 空间复杂度：O(H)，其中H是树的高度（递归调用栈）
 */
public class MaxSumInTree {
    
    /**
     * 二叉树节点定义
     */
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node(int v) {
            val = v;
        }
    }
    
    // 方法1的全局变量，记录最大路径和
    public static int maxSum = Integer.MIN_VALUE;
    
    /**
     * 方法1的递归过程：使用全局变量记录最大值
     * @param x 当前节点
     * @param pre 从根到当前节点父节点的路径和
     */
    private static void process1(Node x, int pre) {
        // 到达叶子节点，更新最大值
        if (x.left == null && x.right == null) {
            maxSum = Math.max(maxSum, pre + x.val);
        }
        
        // 递归处理左子树
        if (x.left != null) {
            process1(x.left, pre + x.val);
        }
        
        // 递归处理右子树
        if (x.right != null) {
            process1(x.right, pre + x.val);
        }
    }

    /**
     * 方法1：使用全局变量的解法
     * @param head 根节点
     * @return 从根到叶子的最大路径和
     */
    public static int max1(Node head) {
        maxSum = Integer.MIN_VALUE;
        process1(head, 0);
        return maxSum;
    }

    /**
     * 方法2的递归过程：直接返回最大路径和
     * @param x 当前节点
     * @return 从当前节点到叶子节点的最大路径和
     */
    private static int process2(Node x) {
        // 叶子节点直接返回自身值
        if (x.left == null && x.right == null) {
            return x.val;
        }
        
        int next = Integer.MIN_VALUE;
        
        // 获取左子树的最大路径和
        if (x.left != null) {
            next = process2(x.left);
        }
        
        // 获取右子树的最大路径和，取较大值
        if (x.right != null) {
            next = Math.max(next, process2(x.right));
        }
        
        // 返回当前节点值加上子树的最大路径和
        return x.val + next;
    }

    /**
     * 方法2：递归求解，不使用全局变量
     * @param head 根节点
     * @return 从根到叶子的最大路径和
     */
    public static int max2(Node head) {
        if (head == null) {
            return 0;
        }
        return process2(head);
    }

    /**
     * 方法3的信息结构体：树形DP的返回信息
     */
    public static class Info {
        public int allMax;      // 以当前节点为根的子树中任意路径的最大和
        public int fromHeadMax; // 从当前节点出发到叶子节点的最大路径和
        
        public Info(int all, int from) {
            allMax = all;
            fromHeadMax = from;
        }
    }

    /**
     * 方法3的递归过程：树形DP，求解更通用的最大路径和问题
     * @param x 当前节点
     * @return 包含当前子树信息的Info对象
     */
    private static Info f3(Node x) {
        if (x == null) {
            return null;
        }
        
        // 获取左右子树的信息
        Info li = f3(x.left);
        Info ri = f3(x.right);
        
        // p1: 左子树中的最大路径和
        int p1 = Integer.MIN_VALUE;
        if (li != null) {
            p1 = li.allMax;
        }
        
        // p2: 右子树中的最大路径和
        int p2 = Integer.MIN_VALUE;
        if (ri != null) {
            p2 = ri.allMax;
        }
        
        // p3: 只包含当前节点的路径和
        int p3 = x.val;
        
        // p4: 从当前节点出发的最大路径和
        int p4 = Integer.MIN_VALUE;
        if (li != null) {
            p4 = x.val + li.fromHeadMax;
        }
        if (ri != null) {
            p4 = Math.max(p4, x.val + ri.fromHeadMax);
        }
        
        // 整个子树的最大路径和
        int allMax = Math.max(Math.max(Math.max(p1, p2), p3), p4);
        
        // 从当前节点出发的最大路径和
        int fromHeadMax = Math.max(p3, p4);
        
        return new Info(allMax, fromHeadMax);
    }

    /**
     * 方法3：使用树形DP的通用解法
     * @param head 根节点
     * @return 树中任意路径的最大和
     */
    public static int max3(Node head) {
        if (head == null) {
            return 0;
        }
        return f3(head).allMax;
    }
}
