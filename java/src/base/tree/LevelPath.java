package base.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树自底向上层序遍历
 * 
 * 问题描述：
 * 给定一个二叉树，返回其节点值自底向上的层序遍历结果。
 * 即从最深层开始，每一层从左到右遍历，然后向上一层。
 * 
 * 核心思想：
 * 1. 使用标准的层序遍历（BFS）从上到下收集每层节点
 * 2. 将收集到的每层结果插入到结果列表的头部，实现自底向上的效果
 * 
 * 算法流程：
 * 1. 使用队列进行层序遍历
 * 2. 记录每层的节点数，逐层处理
 * 3. 每层处理完后，将该层结果插入到总结果的开头
 * 4. 继续处理下一层，直到队列为空
 * 
 * 关键技巧：
 * - 队列管理：使用队列的size()确定当前层的节点数
 * - 结果插入：add(0, list)实现头部插入，构造自底向上顺序
 * - 层级分离：每层单独处理，避免层间数据混淆
 * 
 * 应用场景：
 * - 打印树的倒序层级结构
 * - 从叶子节点开始的数据处理
 * - 树的可视化展示（从底部开始）
 * - 特定的数据分析需求
 * 
 * @author algorithm-base
 * @version 1.0
 */
public class LevelPath {
    
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
     * 自底向上层序遍历的主方法
     * 
     * 算法详解：
     * 1. 从根节点开始，使用队列进行标准的层序遍历
     * 2. 对每一层：
     *    - 记录当前层的节点数量（队列大小）
     *    - 依次处理该层的所有节点
     *    - 将子节点加入队列（为下一层做准备）
     *    - 将当前层的结果插入到总结果的开头
     * 3. 重复直到所有层都处理完成
     * 
     * 示例过程：
     * 对于树：    3
     *           / \
     *          9   20
     *             /  \
     *            15   7
     * 
     * 正常层序：[[3], [9, 20], [15, 7]]
     * 自底向上：[[15, 7], [9, 20], [3]]
     * 
     * @param root 二叉树根节点
     * @return 自底向上的层序遍历结果
     * 
     * 时间复杂度：O(N)，每个节点访问一次
     * 空间复杂度：O(W)，W为树的最大宽度（队列最大长度）
     */
    public static List<List<Integer>> levelPath(Node root) {
        List<List<Integer>> ans = new LinkedList<>();
        
        // 边界情况：空树直接返回空结果
        if (root == null) {
            return ans;
        }
        
        // 初始化队列，将根节点入队
        Queue<Node> que = new LinkedList<>();
        que.add(root);
        
        // 开始层序遍历
        while (!que.isEmpty()) {
            int size = que.size();          // 当前层的节点数量
            List<Integer> currentLevel = new LinkedList<>();  // 当前层的结果
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; i++) {
                Node node = que.poll();     // 取出当前节点
                currentLevel.add(node.val); // 加入当前层结果
                
                // 将子节点加入队列（为下一层做准备）
                if (node.left != null) {
                    que.add(node.left);
                }
                if (node.right != null) {
                    que.add(node.right);
                }
            }
            
            // 关键步骤：将当前层结果插入到总结果的开头
            // 这样就实现了自底向上的顺序
            ans.add(0, currentLevel);
        }
        
        return ans;
    }

    /**
     * 标准层序遍历（从上到下）用于对比
     * 
     * @param root 二叉树根节点
     * @return 标准的层序遍历结果
     */
    public static List<List<Integer>> levelOrderNormal(Node root) {
        List<List<Integer>> ans = new LinkedList<>();
        
        if (root == null) {
            return ans;
        }
        
        Queue<Node> que = new LinkedList<>();
        que.add(root);
        
        while (!que.isEmpty()) {
            int size = que.size();
            List<Integer> currentLevel = new LinkedList<>();
            
            for (int i = 0; i < size; i++) {
                Node node = que.poll();
                currentLevel.add(node.val);
                
                if (node.left != null) {
                    que.add(node.left);
                }
                if (node.right != null) {
                    que.add(node.right);
                }
            }
            
            // 标准版本：直接添加到末尾
            ans.add(currentLevel);
        }
        
        return ans;
    }

    /**
     * 递归版本的自底向上层序遍历（另一种实现方式）
     * 
     * 思路：
     * 1. 先递归计算每层的深度
     * 2. 根据深度逆序构建结果
     * 
     * @param root 二叉树根节点
     * @return 自底向上的层序遍历结果
     */
    public static List<List<Integer>> levelPathRecursive(Node root) {
        List<List<Integer>> result = new LinkedList<>();
        fillLevels(root, 0, result);
        return result;
    }

    /**
     * 递归填充各层节点的辅助方法
     */
    private static void fillLevels(Node node, int level, List<List<Integer>> result) {
        if (node == null) return;
        
        // 如果这是第一次访问这一层，需要在结果开头插入新的层列表
        if (level >= result.size()) {
            result.add(0, new LinkedList<>());
        }
        
        // 将当前节点加入对应层级（由于是从开头插入，所以索引需要调整）
        result.get(result.size() - 1 - level).add(node.val);
        
        // 递归处理左右子树
        fillLevels(node.left, level + 1, result);
        fillLevels(node.right, level + 1, result);
    }

    /**
     * 构建测试用的二叉树
     * 树结构：      3
     *             / \
     *            9   20
     *               /  \
     *              15   7
     * 
     * 层级分布：
     * - 第1层：[3]
     * - 第2层：[9, 20]  
     * - 第3层：[15, 7]
     * 
     * 自底向上结果：[[15, 7], [9, 20], [3]]
     */
    private static Node buildTestTree() {
        Node root = new Node(3);
        root.left = new Node(9);
        root.right = new Node(20);
        root.right.left = new Node(15);
        root.right.right = new Node(7);
        return root;
    }

    /**
     * 构建复杂测试树
     * 树结构：        1
     *               / \
     *              2   3
     *             /     \
     *            4       5
     *           / \     / \
     *          6   7   8   9
     */
    private static Node buildComplexTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.right.right = new Node(5);
        root.left.left.left = new Node(6);
        root.left.left.right = new Node(7);
        root.right.right.left = new Node(8);
        root.right.right.right = new Node(9);
        return root;
    }

    /**
     * 打印层序遍历结果的格式化方法
     */
    private static void printLevelOrder(List<List<Integer>> levels, String title) {
        System.out.println(title + ":");
        for (int i = 0; i < levels.size(); i++) {
            System.out.println("第" + (i + 1) + "层: " + levels.get(i));
        }
        System.out.println();
    }

    /**
     * 测试自底向上层序遍历算法
     * 
     * 测试内容：
     * 1. 标准测试用例
     * 2. 复杂树结构测试
     * 3. 边界情况测试（空树、单节点）
     * 4. 不同实现方式的结果对比
     */
    public static void main(String[] args) {
        System.out.println("二叉树自底向上层序遍历算法测试");
        System.out.println("================================");
        
        // 测试用例1：标准树
        System.out.println("测试用例1：标准二叉树");
        Node testTree = buildTestTree();
        
        System.out.println("树结构：");
        System.out.println("    3");
        System.out.println("   / \\");
        System.out.println("  9   20");
        System.out.println("     /  \\");
        System.out.println("   15    7");
        System.out.println();
        
        // 对比标准层序遍历和自底向上层序遍历
        List<List<Integer>> normalOrder = levelOrderNormal(testTree);
        List<List<Integer>> bottomUp = levelPath(testTree);
        List<List<Integer>> recursiveBottomUp = levelPathRecursive(testTree);
        
        printLevelOrder(normalOrder, "标准层序遍历（从上到下）");
        printLevelOrder(bottomUp, "自底向上层序遍历（迭代版本）");
        printLevelOrder(recursiveBottomUp, "自底向上层序遍历（递归版本）");
        
        // 验证两种自底向上实现结果是否一致
        boolean sameResult = bottomUp.equals(recursiveBottomUp);
        System.out.println("两种实现结果一致性：" + (sameResult ? "一致 ✓" : "不一致 ✗"));
        System.out.println();
        
        // 测试用例2：复杂树
        System.out.println("测试用例2：复杂二叉树");
        Node complexTree = buildComplexTree();
        
        System.out.println("树结构：");
        System.out.println("      1");
        System.out.println("     / \\");
        System.out.println("    2   3");
        System.out.println("   /     \\");
        System.out.println("  4       5");
        System.out.println(" / \\     / \\");
        System.out.println("6   7   8   9");
        System.out.println();
        
        List<List<Integer>> complexBottomUp = levelPath(complexTree);
        printLevelOrder(complexBottomUp, "复杂树的自底向上层序遍历");
        
        // 测试用例3：边界情况
        System.out.println("测试用例3：边界情况");
        
        // 空树测试
        List<List<Integer>> emptyResult = levelPath(null);
        System.out.println("空树结果：" + emptyResult + " (应为空列表)");
        
        // 单节点树测试
        Node singleNode = new Node(42);
        List<List<Integer>> singleResult = levelPath(singleNode);
        System.out.println("单节点树结果：" + singleResult + " (应为[[42]])");
        System.out.println();
        
        // 算法特点分析
        System.out.println("算法特点分析：");
        System.out.println("================");
        System.out.println("迭代版本（推荐）：");
        System.out.println("• 时间复杂度：O(N) - 每个节点访问一次");
        System.out.println("• 空间复杂度：O(W) - W为树的最大宽度");
        System.out.println("• 优点：简单直观，内存效率高");
        System.out.println("• 缺点：需要额外的头部插入操作");
        System.out.println();
        System.out.println("递归版本：");
        System.out.println("• 时间复杂度：O(N) - 每个节点访问一次");
        System.out.println("• 空间复杂度：O(H) - H为树的高度（递归栈）");
        System.out.println("• 优点：代码简洁，递归思路清晰");
        System.out.println("• 缺点：深度过大时可能栈溢出");
        System.out.println();
        System.out.println("应用场景：");
        System.out.println("• 树的可视化展示（从底部开始）");
        System.out.println("• 从叶子节点开始的数据处理");
        System.out.println("• 特定格式的输出需求");
        System.out.println("• 逆向层级分析");
        System.out.println();
        System.out.println("vs 标准层序遍历：");
        System.out.println("• 主要区别：结果顺序相反");
        System.out.println("• 实现复杂度：相当，只是插入位置不同");
        System.out.println("• 性能差异：头部插入略有开销，但总体O(N)");
    }
}
