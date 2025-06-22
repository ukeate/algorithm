package base.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树层序遍历（广度优先搜索 BFS）
 * 
 * 层序遍历：按照从上到下、从左到右的顺序遍历二叉树的所有节点
 * 每一层的节点从左到右依次访问，然后再访问下一层的节点
 * 
 * 算法思路：
 * 使用队列（FIFO）来实现层序遍历：
 * 1. 将根节点入队
 * 2. 当队列不为空时，重复以下步骤：
 *    - 从队列头部取出一个节点并处理
 *    - 将该节点的左子节点（如果存在）入队
 *    - 将该节点的右子节点（如果存在）入队
 * 
 * 时间复杂度：O(n)，其中n是树中节点的数量
 * 空间复杂度：O(w)，其中w是树的最大宽度（某一层的最大节点数）
 */
public class TraversalLevel {
    /**
     * 二叉树节点定义
     */
    public static class Node {
        public int val;      // 节点值
        public Node left;    // 左子节点
        public Node right;   // 右子节点

        public Node(int v) {
            val = v;
        }
    }

    /**
     * 层序遍历实现
     * @param cur 树的根节点
     */
    public static void level(Node cur) {
        if (cur == null) {
            return;
        }
        // 使用队列来存储待处理的节点
        Queue<Node> queue = new LinkedList<>();
        queue.add(cur);  // 将根节点入队
        
        while (!queue.isEmpty()) {
            // 从队列头部取出节点并处理
            cur = queue.poll();
            System.out.print(cur.val + ",");
            
            // 将左子节点入队（如果存在）
            if (cur.left != null) {
                queue.add(cur.left);
            }
            // 将右子节点入队（如果存在）
            if (cur.right != null) {
                queue.add(cur.right);
            }
        }
    }

    public static void main(String[] args) {
        // 构建测试二叉树
        //       1
        //     /   \
        //    2     3
        //   / \   / \
        //  4   5 6   7
        // 
        // 层序遍历结果应该是：1,2,3,4,5,6,7
        
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        head.right.right = new Node(7);

        level(head);
        System.out.println("========");
    }
}
