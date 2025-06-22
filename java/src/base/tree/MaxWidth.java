package base.tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树最大宽度计算
 * 
 * 问题描述：
 * 计算二叉树的最大宽度，即任意一层节点数的最大值。
 * 
 * 核心思想：
 * 使用层序遍历逐层统计节点数量，记录并更新最大值。
 * 
 * 两种实现方案：
 * 1. HashMap方案：使用Map记录每个节点的层级信息
 * 2. 双指针方案：使用指针标记每层的开始和结束位置
 * 
 * 算法流程：
 * 1. 从根节点开始进行层序遍历
 * 2. 统计每一层的节点数量
 * 3. 维护全局最大宽度记录
 * 4. 遍历完成后返回最大值
 * 
 * 关键技巧：
 * - 层级标识：准确识别节点所属层级
 * - 计数维护：实时统计当前层节点数
 * - 边界处理：正确处理层级切换时的计数
 * - 最值更新：及时更新全局最大宽度
 * 
 * 应用场景：
 * - 树结构可视化布局
 * - 内存空间需求评估
 * - 数据结构性能分析
 * - 算法复杂度评估
 * 
 * @author algorithm-base
 * @version 1.0
 */
public class MaxWidth {
    
    /**
     * 二叉树节点定义
     */
    public static class Node {
        public int val;        // 节点值
        public Node left;      // 左子节点
        public Node right;     // 右子节点

        public Node(int v) {
            val = v;
        }

        @Override
        public String toString() {
            return "Node{" + val + "}";
        }
    }

    /**
     * 方法1：使用HashMap记录层级信息的最大宽度计算
     * 
     * 算法思路：
     * 1. 使用HashMap存储每个节点对应的层级
     * 2. 在层序遍历过程中统计每层的节点数
     * 3. 当遇到新层级时，更新最大宽度并重置计数
     * 
     * 实现细节：
     * - levelMap：存储节点到层级的映射关系
     * - level：当前处理的层级
     * - width：当前层级的节点数量
     * - max：记录的最大宽度值
     * 
     * 示例过程：
     * 对于树：    1
     *           / \
     *          2   3
     *         / \
     *        4   5
     * 
     * 层级分布：
     * - 第1层：节点1 (宽度=1)
     * - 第2层：节点2,3 (宽度=2)
     * - 第3层：节点4,5 (宽度=2)
     * 最大宽度：2
     * 
     * @param head 二叉树根节点
     * @return 二叉树的最大宽度
     * 
     * 时间复杂度：O(N)，每个节点访问一次
     * 空间复杂度：O(N)，HashMap存储所有节点的层级信息
     */
    public static int maxWidth1(Node head) {
        // 边界情况：空树宽度为0
        if (head == null) {
            return 0;
        }
        
        // 初始化数据结构
        Queue<Node> queue = new LinkedList<>();
        HashMap<Node, Integer> levelMap = new HashMap<>(); // 节点->层级映射
        
        // 根节点入队，设置为第1层
        queue.add(head);
        levelMap.put(head, 1);
        
        int level = 1;      // 当前处理的层级
        int width = 0;      // 当前层级的节点计数
        int max = 0;        // 全局最大宽度
        
        // 开始层序遍历
        while (!queue.isEmpty()) {
            Node node = queue.poll();           // 取出当前节点
            int nodeLevel = levelMap.get(node); // 获取节点层级
            
            // 将子节点加入队列并设置层级
            if (node.left != null) {
                levelMap.put(node.left, nodeLevel + 1);
                queue.add(node.left);
            }
            if (node.right != null) {
                levelMap.put(node.right, nodeLevel + 1);
                queue.add(node.right);
            }
            
            // 判断是否仍在当前层级
            if (nodeLevel == level) {
                width++;  // 当前层节点数加1
            } else {
                // 进入新层级，更新最大宽度并重置计数
                max = Math.max(max, width);
                level++;     // 更新层级
                width = 1;   // 新层级的第一个节点
            }
        }
        
        // 处理最后一层的宽度（循环结束时还没有比较最后一层）
        max = Math.max(max, width);
        return max;
    }

    /**
     * 方法2：使用双指针标记层级边界的最大宽度计算
     * 
     * 算法思路：
     * 1. 使用curEnd标记当前层的最后一个节点
     * 2. 使用nextEnd标记下一层的最后一个节点
     * 3. 当处理到curEnd时，表示当前层结束，统计宽度并切换到下一层
     * 
     * 优势：
     * - 空间效率更高，不需要额外的HashMap存储
     * - 逻辑更直观，直接通过指针控制层级切换
     * - 内存访问模式更好，缓存友好
     * 
     * 实现细节：
     * - curEnd：当前层的最后一个节点引用
     * - nextEnd：下一层的最后一个节点引用
     * - width：当前层的节点计数
     * - max：全局最大宽度记录
     * 
     * @param head 二叉树根节点
     * @return 二叉树的最大宽度
     * 
     * 时间复杂度：O(N)，每个节点访问一次
     * 空间复杂度：O(W)，W为树的最大宽度（队列空间）
     */
    public static int maxWidth2(Node head) {
        // 边界情况：空树宽度为0
        if (head == null) {
            return 0;
        }
        
        // 初始化数据结构
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        
        Node curEnd = head;     // 当前层的最后一个节点
        Node nextEnd = null;    // 下一层的最后一个节点
        int max = 0;           // 全局最大宽度
        int width = 0;         // 当前层的节点计数
        
        // 开始层序遍历
        while (!queue.isEmpty()) {
            Node node = queue.poll();  // 取出当前节点
            
            // 将子节点加入队列
            if (node.left != null) {
                queue.add(node.left);
                nextEnd = node.left;    // 更新下一层的最后一个节点
            }
            if (node.right != null) {
                queue.add(node.right);
                nextEnd = node.right;   // 更新下一层的最后一个节点
            }
            
            width++;  // 当前层节点数加1
            
            // 检查是否到达当前层的最后一个节点
            if (node == curEnd) {
                // 当前层结束，更新最大宽度并准备处理下一层
                max = Math.max(max, width);
                width = 0;           // 重置宽度计数
                curEnd = nextEnd;    // 切换层级边界
            }
        }
        
        return max;
    }

    /**
     * 生成随机二叉树用于测试
     * 
     * @param level 当前层级（用于控制递归深度）
     * @param maxLevel 最大层级限制
     * @param maxVal 节点值的最大值
     * @return 生成的随机树节点
     */
    private static Node randomLevel(int level, int maxLevel, int maxVal) {
        // 超过最大层级或随机决定停止生成
        if(level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        
        // 创建当前节点
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        
        // 递归生成左右子树
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        
        return head;
    }

    /**
     * 生成随机二叉树的入口方法
     * 
     * @param maxLevel 树的最大深度
     * @param maxVal 节点值的最大值
     * @return 生成的随机二叉树根节点
     */
    private static Node randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

    /**
     * 构建测试用的标准二叉树
     * 树结构：        1
     *               / \
     *              2   3
     *             / \ / \
     *            4  5 6  7
     *           /
     *          8
     * 
     * 层级宽度分布：
     * - 第1层：1个节点 (宽度=1)
     * - 第2层：2个节点 (宽度=2)  
     * - 第3层：4个节点 (宽度=4) ← 最大
     * - 第4层：1个节点 (宽度=1)
     * 最大宽度：4
     */
    private static Node buildTestTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);
        root.left.left.left = new Node(8);
        return root;
    }

    /**
     * 打印二叉树结构（用于调试和可视化）
     * 使用中序遍历的方式展示树结构
     */
    private static void printTree(Node root, String prefix, boolean isLeft) {
        if (root == null) return;
        
        // 打印右子树
        printTree(root.right, prefix + (isLeft ? "│   " : "    "), false);
        
        // 打印当前节点
        System.out.println(prefix + (isLeft ? "└── " : "┌── ") + root.val);
        
        // 打印左子树
        printTree(root.left, prefix + (isLeft ? "    " : "│   "), true);
    }

    /**
     * 层序遍历打印（验证算法正确性）
     */
    private static void printLevelOrder(Node root) {
        if (root == null) return;
        
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int level = 1;
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            System.out.print("第" + level + "层 (" + size + "个节点): ");
            
            for (int i = 0; i < size; i++) {
                Node node = queue.poll();
                System.out.print(node.val + " ");
                
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            System.out.println();
            level++;
        }
    }

    /**
     * 测试二叉树最大宽度算法
     * 
     * 测试内容：
     * 1. 算法正确性验证：对比两种方法的结果
     * 2. 边界情况测试：空树、单节点、链状树
     * 3. 复杂情况测试：随机生成大量测试用例
     * 4. 性能对比：分析两种方法的时间和空间效率
     */
    public static void main(String[] args) {
        System.out.println("二叉树最大宽度计算算法测试");
        System.out.println("============================");
        
        // 测试用例1：标准测试树
        System.out.println("测试用例1：标准测试树");
        Node testTree = buildTestTree();
        
        System.out.println("树结构可视化：");
        printTree(testTree, "", false);
        System.out.println();
        
        System.out.println("层序遍历验证：");
        printLevelOrder(testTree);
        System.out.println();
        
        int width1 = maxWidth1(testTree);
        int width2 = maxWidth2(testTree);
        
        System.out.println("HashMap方法结果：" + width1);
        System.out.println("双指针方法结果：" + width2);
        System.out.println("结果一致性：" + (width1 == width2 ? "通过 ✓" : "失败 ✗"));
        System.out.println();
        
        // 测试用例2：边界情况
        System.out.println("测试用例2：边界情况");
        
        // 空树测试
        int emptyResult1 = maxWidth1(null);
        int emptyResult2 = maxWidth2(null);
        System.out.println("空树测试 - HashMap方法：" + emptyResult1 + ", 双指针方法：" + emptyResult2);
        
        // 单节点树测试
        Node singleNode = new Node(1);
        int singleResult1 = maxWidth1(singleNode);
        int singleResult2 = maxWidth2(singleNode);
        System.out.println("单节点树测试 - HashMap方法：" + singleResult1 + ", 双指针方法：" + singleResult2);
        
        // 链状树测试（左倾）
        Node chainTree = new Node(1);
        chainTree.left = new Node(2);
        chainTree.left.left = new Node(3);
        chainTree.left.left.left = new Node(4);
        
        int chainResult1 = maxWidth1(chainTree);
        int chainResult2 = maxWidth2(chainTree);
        System.out.println("左倾链状树测试 - HashMap方法：" + chainResult1 + ", 双指针方法：" + chainResult2);
        System.out.println();
        
        // 测试用例3：大规模随机测试
        System.out.println("测试用例3：大规模随机测试");
        int times = 100000;    // 测试次数
        int maxLevel = 10;     // 最大树深度
        int maxValue = 100;    // 最大节点值
        
        System.out.println("随机测试参数：");
        System.out.println("测试次数：" + times);
        System.out.println("最大树深度：" + maxLevel);
        System.out.println("最大节点值：" + maxValue);
        System.out.println();
        
        System.out.println("正在进行随机测试...");
        boolean allPassed = true;
        int errorCount = 0;
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxValue);
            int result1 = maxWidth1(head);
            int result2 = maxWidth2(head);
            
            if (result1 != result2) {
                allPassed = false;
                errorCount++;
                if (errorCount <= 5) {  // 只打印前5个错误
                    System.out.println("第" + (i+1) + "次测试失败：HashMap方法=" + result1 + ", 双指针方法=" + result2);
                }
            }
            
            // 进度显示
            if ((i + 1) % 10000 == 0) {
                System.out.println("已完成 " + (i + 1) + " / " + times + " 次测试");
            }
        }
        
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        
        System.out.println();
        System.out.println("随机测试结果：");
        System.out.println("总测试次数：" + times);
        System.out.println("错误次数：" + errorCount);
        System.out.println("正确率：" + String.format("%.4f%%", (times - errorCount) * 100.0 / times));
        System.out.println("总耗时：" + totalTime + " ms");
        System.out.println("平均耗时：" + String.format("%.3f", totalTime * 1.0 / times) + " ms/次");
        System.out.println("测试结果：" + (allPassed ? "全部通过 ✓" : "存在错误 ✗"));
        System.out.println();
        
        // 算法特点总结
        System.out.println("算法特点总结：");
        System.out.println("================");
        System.out.println("HashMap方法（方法1）：");
        System.out.println("• 优点：逻辑清晰，易于理解和实现");
        System.out.println("• 缺点：空间复杂度高，需要额外存储");
        System.out.println("• 时间复杂度：O(N)");
        System.out.println("• 空间复杂度：O(N) - HashMap存储");
        System.out.println("• 适用场景：对空间要求不严格的情况");
        System.out.println();
        System.out.println("双指针方法（方法2）：");
        System.out.println("• 优点：空间效率高，内存访问友好");
        System.out.println("• 缺点：逻辑稍复杂，需要仔细处理边界");
        System.out.println("• 时间复杂度：O(N)");
        System.out.println("• 空间复杂度：O(W) - W为最大宽度");
        System.out.println("• 适用场景：内存受限或性能要求高的情况");
        System.out.println();
        System.out.println("推荐选择：");
        System.out.println("• 学习阶段：使用HashMap方法，便于理解");
        System.out.println("• 生产环境：使用双指针方法，效率更高");
        System.out.println("• 面试情况：展示两种方法，体现思维深度");
    }
}
