package leetc.top;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * LeetCode 272. 最接近的二叉搜索树值 II (Closest Binary Search Tree Value II)
 * 
 * 问题描述：
 * 给定一个非空二叉搜索树和一个目标值，找到 BST 中最接近目标值的 k 个值。
 * 
 * 注意：
 * - 给定的目标值为浮点数
 * - 题目保证只有一个最优答案
 * 
 * 示例：
 * 输入：root = [4,2,5,1,3], target = 3.714286, k = 2
 * 输出：[4,3] 或 [3,4]
 * 
 * 解法思路：
 * 双栈中序遍历：
 * 1. 使用两个栈分别维护比目标值小的和大于等于目标值的节点
 * 2. 通过中序遍历的性质，栈顶元素就是最接近目标值的候选节点
 * 3. 每次比较两个栈顶元素与目标值的距离，选择更近的加入结果
 * 4. 动态维护两个栈，确保总能获取下一个最接近的值
 * 
 * 核心算法：
 * - 初始化：找到目标值在BST中的位置，分别填充两个栈
 * - 迭代：比较两个栈顶元素与目标值的距离，选择更近的
 * - 维护：每次取出元素后，更新对应的栈以获取下一个候选值
 * 
 * 时间复杂度：O(log n + k) - 初始化O(log n)，每次获取下一个值O(1)
 * 空间复杂度：O(log n) - 两个栈的深度为树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/closest-binary-search-tree-value-ii/
 */
public class P272_ClosestBinarySearchTreeValueII {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int v) {
            val = v;
        }
    }

    private static void big(TreeNode root, double target, Stack<TreeNode> backUpRight) {
        while (root != null) {
            if (root.val == target) {
                backUpRight.push(root);
                break;
            } else if (root.val > target) {
                backUpRight.push(root);
                root = root.left;
            } else {
                root = root.right;
            }
        }
    }

    private static void small(TreeNode root, double target, Stack<TreeNode> backUpLeft) {
        while (root != null) {
            if (root.val == target) {
                backUpLeft.push(root);
                break;
            } else if (root.val < target) {
                backUpLeft.push(root);
                root = root.right;
            } else {
                root = root.left;
            }
        }
    }

    private static int smaller(Stack<TreeNode> small) {
        TreeNode cur = small.pop();
        int ret = cur.val;
        cur = cur.left;
        while (cur != null) {
            small.push(cur);
            cur = cur.right;
        }
        return ret;
    }

    private static int bigger(Stack<TreeNode> big) {
        TreeNode cur = big.pop();
        int ret = cur.val;
        cur = cur.right;
        while (cur != null) {
            big.push(cur);
            cur = cur.left;
        }
        return ret;
    }

    public static List<Integer> closestKValues(TreeNode root, double target, int k) {
        List<Integer> ret = new LinkedList<>();
        // [大、等]
        Stack<TreeNode> big = new Stack<>();
        // [小、等]
        Stack<TreeNode> small = new Stack<>();
        big(root, target, big);
        small(root, target, small);
        if (!big.isEmpty() && !small.isEmpty() && big.peek().val == small.peek().val) {
            smaller(small);
        }
        while (k-- > 0) {
            if (big.isEmpty()) {
                ret.add(smaller(small));
            } else if (small.isEmpty()) {
                ret.add(bigger(big));
            } else {
                double bigger = Math.abs((double) big.peek().val - target);
                double smaller = Math.abs((double) small.peek().val - target);
                if (bigger < smaller) {
                    ret.add(bigger(big));
                } else {
                    ret.add(smaller(small));
                }
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);
        root.right = new TreeNode(6);
        root.right.left = new TreeNode(5);
        root.right.right = new TreeNode(7);
        for (int i : closestKValues(root, 1.5, 7)) {
            System.out.println(i);
        }
    }
}
