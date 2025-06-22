package base.tree;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据前序和中序遍历构建二叉树
 * 
 * 问题描述：
 * 给定二叉树的前序遍历和中序遍历序列，重构原始的二叉树。
 * 
 * 核心思想：
 * 1. 前序遍历的第一个元素是根节点
 * 2. 在中序遍历中找到根节点位置，左边是左子树，右边是右子树
 * 3. 递归处理左右子树
 * 
 * 算法流程：
 * 1. 前序遍历确定根节点
 * 2. 中序遍历确定左右子树范围
 * 3. 递归构建左右子树
 * 4. 连接父子节点关系
 * 
 * 优化策略：
 * 使用HashMap预存储中序遍历中每个值的索引位置，避免每次线性查找，
 * 将时间复杂度从O(N²)优化到O(N)。
 * 
 * 应用场景：
 * - 二叉树序列化/反序列化
 * - 树结构恢复
 * - 编译器语法树构建
 * - 表达式树重建
 * 
 * 注意事项：
 * - 输入数组不包含重复元素
 * - 前序和中序数组长度必须相等
 * - 数组元素必须一一对应
 * 
 * @author algorithm-base
 * @version 1.0
 */
public class BuildPreIn {

    /**
     * 二叉树节点定义
     */
    private static class Node {
        public int val;        // 节点值
        public Node left;      // 左子节点
        public Node right;     // 右子节点

        public Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "Node{" + val + "}";
        }
    }

    /**
     * 递归构建二叉树的核心方法
     * 
     * 算法详解：
     * 1. 从前序遍历取根节点（l1位置）
     * 2. 在中序遍历中定位根节点位置（find）
     * 3. 计算左子树大小：find - l2
     * 4. 递归构建左子树：前序[l1+1, l1+leftSize], 中序[l2, find-1]
     * 5. 递归构建右子树：前序[l1+leftSize+1, r1], 中序[find+1, r2]
     * 
     * 区间计算示例：
     * 前序：[3,9,20,15,7]，中序：[9,3,15,20,7]
     * - 根节点：3（前序第一个）
     * - 中序中3的位置：1
     * - 左子树：前序[9]，中序[9]
     * - 右子树：前序[20,15,7]，中序[15,20,7]
     * 
     * @param pre 前序遍历数组
     * @param l1 前序数组左边界
     * @param r1 前序数组右边界
     * @param in 中序遍历数组
     * @param l2 中序数组左边界
     * @param r2 中序数组右边界
     * @param inMap 中序遍历值到索引的映射
     * @return 构建的子树根节点
     */
    private static Node buildF(int[] pre, int l1, int r1, int[] in, int l2, int r2, Map<Integer, Integer> inMap) {
        // 边界条件：区间为空
        if (l1 > r1) {
            return null;
        }
        
        // 创建根节点（前序遍历的第一个元素）
        Node head = new Node(pre[l1]);
        
        // 只有一个节点的情况
        if (l1 == r1) {
            return head;
        }
        
        // 在中序遍历中找到根节点的位置
        int find = inMap.get(pre[l1]);
        
        // 计算左子树的大小
        int leftSize = find - l2;
        
        // 递归构建左子树
        // 前序区间：[l1+1, l1+leftSize]
        // 中序区间：[l2, find-1]
        head.left = buildF(pre, l1 + 1, l1 + leftSize, in, l2, find - 1, inMap);
        
        // 递归构建右子树
        // 前序区间：[l1+leftSize+1, r1]
        // 中序区间：[find+1, r2]
        head.right = buildF(pre, l1 + leftSize + 1, r1, in, find + 1, r2, inMap);
        
        return head;
    }

    /**
     * 构建二叉树的主方法
     * 
     * 预处理优化：
     * 构建HashMap存储中序遍历中每个值的索引，
     * 避免在递归过程中重复线性查找，显著提升性能。
     * 
     * 示例演示：
     * 前序：[3,9,20,15,7]
     * 中序：[9,3,15,20,7]
     * 
     * 构建过程：
     * 1. 根节点3，在中序位置1
     * 2. 左子树：前序[9]，中序[9] -> 节点9
     * 3. 右子树：前序[20,15,7]，中序[15,20,7]
     *    - 根节点20，在中序位置3
     *    - 左子树：前序[15]，中序[15] -> 节点15
     *    - 右子树：前序[7]，中序[7] -> 节点7
     * 
     * 最终树结构：
     *     3
     *    / \
     *   9   20
     *      /  \
     *     15   7
     * 
     * @param pre 前序遍历数组
     * @param in 中序遍历数组
     * @return 构建的二叉树根节点
     * 
     * 时间复杂度：O(N)，每个节点访问一次
     * 空间复杂度：O(N)，HashMap存储 + 递归栈深度
     */
    public static Node build(int[] pre, int[] in) {
        // 输入验证
        if (pre == null || in == null || pre.length != in.length || pre.length == 0) {
            return null;
        }
        
        // 构建中序遍历的值到索引的映射表
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            inMap.put(in[i], i);
        }
        
        // 开始递归构建
        return buildF(pre, 0, pre.length - 1, in, 0, in.length - 1, inMap);
    }

    /**
     * 辅助方法：前序遍历验证构建结果
     * @param root 树根节点
     * @return 前序遍历序列
     */
    public static void preorderPrint(Node root) {
        if (root == null) return;
        System.out.print(root.val + " ");
        preorderPrint(root.left);
        preorderPrint(root.right);
    }

    /**
     * 辅助方法：中序遍历验证构建结果
     * @param root 树根节点
     * @return 中序遍历序列
     */
    public static void inorderPrint(Node root) {
        if (root == null) return;
        inorderPrint(root.left);
        System.out.print(root.val + " ");
        inorderPrint(root.right);
    }

    /**
     * 测试方法：验证二叉树构建算法
     * 
     * 测试用例设计：
     * 1. 标准测试：包含左右子树的完整二叉树
     * 2. 边界测试：空数组、单节点、不平衡树
     * 3. 验证方法：重新遍历构建的树，对比原始序列
     */
    public static void main(String[] args) {
        System.out.println("二叉树构建算法测试");
        System.out.println("==================");
        
        // 测试用例1：标准二叉树
        System.out.println("测试用例1：标准二叉树");
        int[] pre1 = {3, 9, 20, 15, 7};
        int[] in1 = {9, 3, 15, 20, 7};
        
        System.out.println("前序遍历输入：" + java.util.Arrays.toString(pre1));
        System.out.println("中序遍历输入：" + java.util.Arrays.toString(in1));
        
        Node root1 = build(pre1, in1);
        
        System.out.print("构建后前序遍历：");
        preorderPrint(root1);
        System.out.println();
        
        System.out.print("构建后中序遍历：");
        inorderPrint(root1);
        System.out.println();
        System.out.println();
        
        // 测试用例2：单节点树
        System.out.println("测试用例2：单节点树");
        int[] pre2 = {1};
        int[] in2 = {1};
        
        Node root2 = build(pre2, in2);
        System.out.println("单节点树根值：" + (root2 != null ? root2.val : "null"));
        System.out.println();
        
        // 测试用例3：左斜树
        System.out.println("测试用例3：左斜树");
        int[] pre3 = {1, 2, 3};
        int[] in3 = {3, 2, 1};
        
        Node root3 = build(pre3, in3);
        System.out.print("左斜树前序：");
        preorderPrint(root3);
        System.out.println();
        
        // 测试用例4：右斜树
        System.out.println("测试用例4：右斜树");
        int[] pre4 = {1, 2, 3};
        int[] in4 = {1, 2, 3};
        
        Node root4 = build(pre4, in4);
        System.out.print("右斜树前序：");
        preorderPrint(root4);
        System.out.println();
        
        System.out.println("测试完成！");
        System.out.println();
        System.out.println("算法特点：");
        System.out.println("• 时间复杂度：O(N) - 每个节点处理一次");
        System.out.println("• 空间复杂度：O(N) - HashMap + 递归栈");
        System.out.println("• 适用条件：无重复元素的前序中序序列");
        System.out.println("• 核心思想：前序定根，中序分割");
    }
}
