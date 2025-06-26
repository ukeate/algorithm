package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * LeetCode 103. 二叉树的锯齿形层序遍历 (Binary Tree Zigzag Level Order Traversal)
 * 
 * 问题描述：
 * 给定一个二叉树，返回其节点值的锯齿形层序遍历。
 * 即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行。
 * 
 * 示例：
 * 二叉树：[3,9,20,null,null,15,7]
 * 返回：[[3],[20,9],[15,7]]
 * 
 * 解法思路：
 * 双端队列 + 方向控制：
 * 1. 使用双端队列(LinkedList)支持从两端插入和删除
 * 2. 使用布尔标志isHead控制当前层的遍历方向
 * 3. 从左到右遍历时：
 *    - 从队列头部取节点，子节点从尾部加入（先左后右）
 * 4. 从右到左遍历时：
 *    - 从队列尾部取节点，子节点从头部加入（先右后左）
 * 5. 每完成一层，切换遍历方向
 * 
 * 核心技巧：
 * - 双端队列同时支持FIFO和LIFO操作
 * - 通过控制取节点和加节点的方向实现锯齿形遍历
 * - 巧妙利用队列的先进先出特性维护下一层的正确顺序
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(w) - w为树的最大宽度
 * 
 * LeetCode链接：https://leetcode.com/problems/binary-tree-zigzag-level-order-traversal/
 */
public class P103_BinaryTreeZigzagLevelOrderTraversal {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        public TreeNode(int v) {
            val = v;
        }
    }

    /**
     * 二叉树锯齿形层序遍历主方法
     * 
     * @param root 二叉树根节点
     * @return 锯齿形层序遍历结果
     */
    public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        
        if (root == null) {
            return ans;
        }
        
        // 使用双端队列支持从两端操作
        LinkedList<TreeNode> deque = new LinkedList<>();
        deque.add(root);
        
        int size = 0;
        boolean isHead = true;  // 控制遍历方向：true表示从头部开始，false表示从尾部开始
        
        while (!deque.isEmpty()) {
            size = deque.size();
            List<Integer> curLevel = new ArrayList<>();
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; i++) {
                TreeNode cur;
                
                if (isHead) {
                    // 从左到右：从队列头部取节点
                    cur = deque.pollFirst();
                    curLevel.add(cur.val);
                    
                    // 子节点从尾部加入，保持左->右的顺序
                    if (cur.left != null) {
                        deque.addLast(cur.left);
                    }
                    if (cur.right != null) {
                        deque.addLast(cur.right);
                    }
                } else {
                    // 从右到左：从队列尾部取节点
                    cur = deque.pollLast();
                    curLevel.add(cur.val);
                    
                    // 子节点从头部加入，注意顺序是先右后左
                    // 这样下一层从头部取时就是左->右的顺序
                    if (cur.right != null) {
                        deque.addFirst(cur.right);
                    }
                    if (cur.left != null) {
                        deque.addFirst(cur.left);
                    }
                }
            }
            
            ans.add(curLevel);
            isHead = !isHead;  // 每完成一层，切换遍历方向
        }
        
        return ans;
    }
}
