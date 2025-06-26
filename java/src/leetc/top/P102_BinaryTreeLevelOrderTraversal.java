package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * LeetCode 102. 二叉树的层序遍历 (Binary Tree Level Order Traversal)
 * 
 * 问题描述：
 * 给你一个二叉树，请你返回其按层序遍历得到的节点值。
 * 即逐层地，从左到右访问所有节点。
 * 
 * 示例：
 * 二叉树：[3,9,20,null,null,15,7]
 * 返回：[[3],[9,20],[15,7]]
 * 
 * 解法思路：
 * BFS (广度优先搜索) + 层次标记：
 * 1. 使用队列进行BFS遍历
 * 2. 维护两个指针：curEnd（当前层最后一个节点）和nextEnd（下一层最后一个节点）
 * 3. 当弹出curEnd节点时，说明当前层遍历完成，开始下一层
 * 4. 在遍历过程中，不断更新nextEnd为最新加入队列的节点
 * 5. 每完成一层，创建新的层结果列表
 * 
 * 核心技巧：
 * - 使用curEnd和nextEnd指针精确控制层边界
 * - 避免了传统方法中需要记录队列大小的复杂性
 * - 最后移除多创建的空列表
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(w) - w为树的最大宽度（队列最大长度）
 * 
 * LeetCode链接：https://leetcode.com/problems/binary-tree-level-order-traversal/
 */
public class P102_BinaryTreeLevelOrderTraversal {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    }

    /**
     * 二叉树层序遍历主方法
     * 
     * @param root 二叉树根节点
     * @return 层序遍历结果，每一层的节点值组成一个列表
     */
    public static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        
        // 空树情况
        if (root == null) {
            return ans;
        }
        
        // 初始化第一层
        ans.add(new ArrayList<>());
        
        Queue<TreeNode> que = new LinkedList<>();
        que.add(root);
        
        // curEnd: 当前层的最后一个节点
        // nextEnd: 下一层的最后一个节点（最新加入队列的节点）
        TreeNode curEnd = root, nextEnd = null;
        
        while (!que.isEmpty()) {
            TreeNode cur = que.poll();
            
            // 将当前节点加入当前层的结果
            ans.get(ans.size() - 1).add(cur.val);
            
            // 处理左子节点
            if (cur.left != null) {
                que.add(cur.left);
                nextEnd = cur.left;  // 更新下一层的结束节点
            }
            
            // 处理右子节点
            if (cur.right != null) {
                que.add(cur.right);
                nextEnd = cur.right;  // 更新下一层的结束节点
            }
            
            // 如果当前节点是本层的最后一个节点
            if (cur == curEnd) {
                curEnd = nextEnd;           // 更新为下一层的结束节点
                ans.add(new ArrayList<>());  // 为下一层创建新的结果列表
            }
        }
        
        // 移除最后创建的空列表（因为最后一层处理完后会多创建一个）
        ans.remove(ans.size() - 1);
        
        return ans;
    }
}
