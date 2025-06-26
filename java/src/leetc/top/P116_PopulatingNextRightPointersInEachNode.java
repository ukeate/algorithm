package leetc.top;

import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 116. 填充每个节点的下一个右侧节点指针 (Populating Next Right Pointers in Each Node)
 * 
 * 问题描述：
 * 给定一个完美二叉树，其所有叶子节点都在同一层，每个父节点都有两个子节点。
 * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。
 * 如果找不到下一个右侧节点，则将 next 指针设置为 NULL。
 * 初始状态下，所有 next 指针都被设置为 NULL。
 * 
 * 解法思路：
 * 层序遍历 + 同层链接：
 * 1. 使用队列进行层序遍历
 * 2. 记录每层的节点数量
 * 3. 在处理每层节点时，将前一个节点的next指向当前节点
 * 4. 同时将当前节点的子节点加入队列（下一层）
 * 
 * 算法步骤：
 * 1. 将根节点加入队列
 * 2. 对于每一层：
 *    - 记录当前层节点数量
 *    - 依次取出当前层的所有节点
 *    - 为每个节点设置next指针（指向同层下一个节点）
 *    - 将子节点加入队列
 * 3. 重复直到队列为空
 * 
 * 完美二叉树性质：
 * - 所有叶子节点在同一层
 * - 除叶子节点外，每个节点都有两个子节点
 * - 这保证了层序遍历的规律性
 * 
 * 其他解法：
 * - 利用已建立的next指针进行O(1)空间的层次遍历
 * - 递归方法：利用父节点的next指针连接子节点
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(w) - w为树的最大宽度（队列空间）
 * 
 * LeetCode链接：https://leetcode.com/problems/populating-next-right-pointers-in-each-node/
 */
public class P116_PopulatingNextRightPointersInEachNode {
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        public Node next;
    }

    public Node connect(Node root) {
        if (root == null) {
            return root;
        }
        Queue<Node> que = new LinkedList<>();
        que.offer(root);
        while (!que.isEmpty()) {
            Node pre = null;
            int size = que.size();
            for (int i = 0; i < size; i++) {
                Node cur = que.poll();
                if (cur.left != null) {
                    que.offer(cur.left);
                }
                if (cur.right != null) {
                    que.offer(cur.right);
                }
                if (pre != null) {
                    pre.next = cur;
                }
                pre = cur;
            }
        }
        return root;
    }
}
