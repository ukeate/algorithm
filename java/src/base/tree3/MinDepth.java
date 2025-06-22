package base.tree3;

/**
 * 二叉树的最小深度问题
 * 给定一个二叉树，找出其最小深度
 * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量
 * https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
 */
public class MinDepth {
    /**
     * 二叉树节点类
     */
    private static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 递归计算最小深度的辅助方法
     * @param x 当前节点
     * @return 以x为根的子树的最小深度
     */
    private static int process(TreeNode x) {
        // 叶子节点的深度为1
        if (x.left == null && x.right == null) {
            return 1;
        }
        int leftH = Integer.MAX_VALUE;
        if (x.left != null) {
            leftH = process(x.left);  // 递归计算左子树最小深度
        }
        int rightH = Integer.MAX_VALUE;
        if (x.right != null) {
            rightH = process(x.right);  // 递归计算右子树最小深度
        }
        // 返回较小的子树深度加1
        return 1 + Math.min(leftH, rightH);
    }

    /**
     * 方法1：递归实现求最小深度
     * @param head 二叉树根节点
     * @return 最小深度
     */
    public static int min1(TreeNode head) {
        if (head == null) {
            return 0;
        }
        return process(head);
    }

    /**
     * 方法2：Morris遍历实现求最小深度
     * 时间复杂度O(n)，空间复杂度O(1)
     * @param head 二叉树根节点
     * @return 最小深度
     */
    public static int min2(TreeNode head) {
        if (head == null) {
            return 0;
        }
        TreeNode cur = head;
        TreeNode r = null;
        int curH = 0;                // 当前高度
        int minH = Integer.MAX_VALUE; // 最小深度
        
        while (cur != null) {
            r = cur.left;
            if (r != null) {
                int rH = 1;  // 记录r到cur左子树最右节点的距离
                // 找到cur左子树的最右节点
                while (r.right != null && r.right != cur) {
                    rH++;
                    r = r.right;
                }
                if (r.right == null) {
                    // 第一次到达cur，建立Morris连接
                    curH++;
                    r.right = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // 第二次到达cur，检查是否为叶子节点
                    if (r.left == null) {
                        minH = Math.min(minH, curH);
                    }
                    curH -= rH;  // 回退高度
                    r.right = null;  // 断开Morris连接
                }
            } else {
                // cur没有左子树，高度增加
                curH++;
            }
            cur = cur.right;
        }
        
        // 检查最右路径的叶子节点
        int finalRH = 1;
        cur = head;
        while (cur.right != null) {
            finalRH++;
            cur = cur.right;
        }
        // 如果最右节点是叶子节点，更新最小深度
        if (cur.left == null && cur.right == null) {
            minH = Math.min(minH, finalRH);
        }
        return minH;
    }
}
