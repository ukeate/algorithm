package leetc.top;

import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 297. 二叉树的序列化与反序列化 (Serialize and Deserialize Binary Tree)
 * 
 * 问题描述：
 * 序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，
 * 同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
 * 
 * 请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列/反序列化算法执行逻辑，
 * 你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。
 * 
 * 示例：
 * 输入：root = [1,2,3,null,null,4,5]
 * 输出：[1,2,3,null,null,4,5]
 * 
 * 解法思路：
 * 使用层序遍历（BFS）进行序列化和反序列化：
 * 
 * 序列化过程：
 * 1. 使用队列进行层序遍历
 * 2. 对每个节点，将其值加入结果
 * 3. 将子节点（包括null）加入队列
 * 4. 移除尾部的null值，优化存储
 * 
 * 反序列化过程：
 * 1. 解析字符串，提取节点值
 * 2. 使用队列重建树结构
 * 3. 按层序遍历的顺序恢复父子关系
 * 
 * 核心思想：
 * - 层序遍历保证了节点的访问顺序
 * - 使用null占位符处理缺失的子节点
 * - 队列维护当前层和下一层的节点关系
 * 
 * 时间复杂度：O(n) - 每个节点被访问一次
 * 空间复杂度：O(n) - 队列存储和结果字符串
 * 
 * LeetCode链接：https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
 */
public class P297_SerializeAndDeserializeBinaryTree {
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int val;          // 节点值
        public TreeNode left;    // 左子节点
        public TreeNode right;   // 右子节点
        
        public TreeNode(int value) {
            val = value;
        }
    }

    /**
     * 序列化二叉树为字符串
     * 
     * 使用层序遍历（BFS）的方式序列化：
     * 1. 如果根节点为空，直接返回"[null]"
     * 2. 使用队列进行层序遍历
     * 3. 对每个非空节点，将其左右子节点加入队列
     * 4. 空节点用"null"表示
     * 5. 移除尾部连续的null，优化存储空间
     * 
     * @param root 二叉树根节点
     * @return 序列化后的字符串，格式如"[1,2,3,null,null,4,5]"
     */
    public String serialize(TreeNode root) {
        LinkedList<String> ans = new LinkedList<>();
        
        if (root == null) {
            ans.add(null);
        } else {
            ans.add(String.valueOf(root.val));
            Queue<TreeNode> que = new LinkedList<>();
            que.add(root);
            
            // 层序遍历，构建序列化结果
            while (!que.isEmpty()) {
                root = que.poll();
                
                // 处理左子节点
                if (root.left != null) {
                    ans.add(String.valueOf(root.left.val));
                    que.add(root.left);
                } else {
                    ans.add(null); // 空节点用null表示
                }
                
                // 处理右子节点
                if (root.right != null) {
                    ans.add(String.valueOf(root.right.val));
                    que.add(root.right);
                } else {
                    ans.add(null); // 空节点用null表示
                }
            }
        }
        
        // 移除尾部的null值，优化存储
        while (!ans.isEmpty() && ans.peekLast() == null) {
            ans.pollLast();
        }
        
        // 构建最终的字符串格式
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        String str = ans.pollFirst();
        builder.append(str == null ? "null" : str);
        while (!ans.isEmpty()) {
            str = ans.pollFirst();
            builder.append("," + (str == null ? "null" : str));
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * 创建树节点的辅助方法
     * 
     * @param val 节点值字符串，"null"表示空节点
     * @return 创建的树节点，null表示空节点
     */
    private TreeNode node(String val) {
        if (val.equals("null")) {
            return null;
        }
        System.out.println(val);
        return new TreeNode(Integer.valueOf(val));
    }

    /**
     * 反序列化字符串为二叉树
     * 
     * 解析序列化字符串并重建二叉树：
     * 1. 解析字符串，提取所有节点值
     * 2. 使用队列按层序遍历的顺序重建树
     * 3. 对每个非空节点，从数组中取出其左右子节点的值
     * 4. 维护队列，确保父子关系正确建立
     * 
     * @param data 序列化的字符串
     * @return 重建的二叉树根节点
     */
    public TreeNode deserialize(String data) {
        // 解析字符串，提取节点值数组
        String[] strs = data.substring(1, data.length() - 1).split(",");
        int idx = 0;
        
        // 创建根节点
        TreeNode root = node(strs[idx++]);
        Queue<TreeNode> que = new LinkedList<>();
        if (root != null) {
            que.add(root);
        }
        
        // 层序遍历重建树结构
        TreeNode node = null;
        while (!que.isEmpty()) {
            node = que.poll();
            
            // 设置左子节点
            node.left = node(idx == strs.length ? "null" : strs[idx++]);
            // 设置右子节点
            node.right = node(idx == strs.length ? "null" : strs[idx++]);
            
            // 将非空子节点加入队列，继续处理下一层
            if (node.left != null) {
                que.add(node.left);
            }
            if (node.right != null) {
                que.add(node.right);
            }
        }
        return root;
    }
}
