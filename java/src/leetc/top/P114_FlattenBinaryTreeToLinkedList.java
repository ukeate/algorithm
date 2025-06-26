package leetc.top;

/**
 * LeetCode 114. 二叉树展开为链表 (Flatten Binary Tree to Linked List)
 * 
 * 问题描述：
 * 给定一个二叉树，原地将它展开为一个单链表。
 * 展开后的单链表应该同样使用 TreeNode，其中 right 子指针指向链表中下一个节点，
 * 而左子指针始终为 null。
 * 展开后的单链表应该与二叉树先序遍历顺序相同。
 * 
 * 解法思路：
 * Morris 遍历 + 链表重构：
 * 1. 第一阶段：使用Morris遍历建立线索二叉树
 *    - 对于每个有左子树的节点，找到其左子树的最右节点
 *    - 将最右节点的right指针指向当前节点（建立线索）
 *    - 同时用pre指针记录遍历顺序，构建先序遍历链
 * 
 * 2. 第二阶段：重构为链表
 *    - 遍历第一阶段建立的链表结构
 *    - 将每个节点的left指向下一个节点，right置空
 *    - 最后统一调整：将left内容移到right，left置null
 * 
 * Morris遍历的核心思想：
 * - 利用叶子节点的空指针来保存遍历信息
 * - 避免使用额外的栈空间进行递归
 * - 在遍历过程中建立和拆除线索
 * 
 * 算法步骤：
 * 1. 如果当前节点无左子树，处理当前节点，移动到右子树
 * 2. 如果有左子树，找到左子树的最右节点（前驱节点）
 * 3. 如果前驱节点右指针为空，建立线索，移动到左子树
 * 4. 如果前驱节点右指针指向当前节点，拆除线索，移动到右子树
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问两次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/flatten-binary-tree-to-linked-list/
 */
public class P114_FlattenBinaryTreeToLinkedList {
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(int v) {
            val = v;
        }
    }

    // 中->左->右
    public static void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        TreeNode pre = null;
        TreeNode cur = root;
        TreeNode r = null;
        while (cur != null) {
            r = cur.left;
            if(r != null) {
                while (r.right != null && r.right != cur) {
                    r = r.right;
                }
                if (r.right == null) {
                    r.right = cur;
                    if (pre != null) {
                        pre.left = cur;
                    }
                    // case1: 中->左
                    pre = cur;
                    cur = cur.left;
                    continue;
                } else {
                    // case3: 中->右
                    r.right = null;
                }
            } else {
                // case2: 左->中
                if (pre != null) {
                    pre.left = cur;
                }
                pre = cur;
            }
            cur = cur.right;
        }
        cur = root;
        TreeNode next = null;
        while (cur != null) {
            cur.right = cur.left;;
            cur.left = null;
            cur = cur.right;
        }
    }
}
