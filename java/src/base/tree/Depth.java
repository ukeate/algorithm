package base.tree;

/**
 * 二叉树深度计算
 * 
 * 问题描述：
 * 计算二叉树的最大深度（也称为高度），即从根节点到叶子节点的最长路径长度。
 * 
 * 核心思想：
 * 使用递归的思想，树的深度等于左右子树深度的最大值加1。
 * 
 * 算法流程：
 * 1. 如果节点为空，返回深度0
 * 2. 递归计算左子树深度
 * 3. 递归计算右子树深度
 * 4. 返回左右子树深度的最大值加1
 * 
 * 递归公式：
 * depth(node) = max(depth(node.left), depth(node.right)) + 1
 * 
 * 应用场景：
 * - 树结构分析：判断树是否平衡
 * - 内存分析：评估递归栈深度
 * - 算法优化：选择合适的遍历策略
 * - 数据结构设计：AVL树、红黑树平衡判断
 * 
 * @author algorithm-base
 * @version 1.0
 */
public class Depth {

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
     * 计算二叉树深度的递归方法
     * 
     * 算法分析：
     * - 时间复杂度：O(N)，需要访问每个节点一次
     * - 空间复杂度：O(H)，H为树的高度，递归栈的深度
     * - 最坏情况：O(N)，退化为链表时
     * - 最好情况：O(logN)，完全二叉树时
     * 
     * 递归过程示例：
     * 对于树：    1
     *           / \
     *          2   3
     *         /
     *        4
     * 
     * 计算过程：
     * 1. depth(1) = max(depth(2), depth(3)) + 1
     * 2. depth(2) = max(depth(4), depth(null)) + 1 = max(1, 0) + 1 = 2
     * 3. depth(3) = max(depth(null), depth(null)) + 1 = max(0, 0) + 1 = 1
     * 4. depth(4) = max(depth(null), depth(null)) + 1 = max(0, 0) + 1 = 1
     * 5. depth(1) = max(2, 1) + 1 = 3
     * 
     * @param root 树的根节点
     * @return 树的最大深度
     */
    public static int depth(Node root) {
        // 基础情况：空节点深度为0
        if (root == null) {
            return 0;
        }
        
        // 递归计算左右子树深度，取最大值加1
        return Math.max(depth(root.left), depth(root.right)) + 1;
    }

    /**
     * 迭代版本的深度计算（使用层序遍历）
     * 
     * 适用场景：
     * - 避免递归栈溢出（深度过大时）
     * - 需要同时获取每层节点信息时
     * 
     * @param root 树的根节点
     * @return 树的最大深度
     */
    public static int depthIterative(Node root) {
        if (root == null) {
            return 0;
        }
        
        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.offer(root);
        int depth = 0;
        
        // 层序遍历，每层结束后深度加1
        while (!queue.isEmpty()) {
            int levelSize = queue.size();  // 当前层的节点数
            depth++;
            
            // 处理当前层的所有节点
            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();
                
                // 将下一层节点加入队列
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        
        return depth;
    }

    /**
     * 构建测试用的二叉树
     * 树结构：      1
     *             / \
     *            2   3
     *           /   / \
     *          4   5   6
     *               \
     *                7
     * 深度为4
     */
    private static Node buildTestTree() {
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.right.left = new Node(5);
        root.right.right = new Node(6);
        root.right.left.right = new Node(7);
        return root;
    }

    /**
     * 测试深度计算算法
     * 
     * 测试用例包括：
     * 1. 空树：深度为0
     * 2. 单节点树：深度为1
     * 3. 完全二叉树：深度为层数
     * 4. 不平衡树：深度为最长路径
     * 5. 链状树：深度等于节点数
     */
    public static void main(String[] args) {
        System.out.println("二叉树深度计算算法测试");
        System.out.println("======================");
        
        // 测试用例1：空树
        System.out.println("测试用例1：空树");
        int depth1 = depth(null);
        int depthIter1 = depthIterative(null);
        System.out.println("递归方法深度：" + depth1);
        System.out.println("迭代方法深度：" + depthIter1);
        System.out.println();
        
        // 测试用例2：单节点树
        System.out.println("测试用例2：单节点树");
        Node singleNode = new Node(1);
        int depth2 = depth(singleNode);
        int depthIter2 = depthIterative(singleNode);
        System.out.println("递归方法深度：" + depth2);
        System.out.println("迭代方法深度：" + depthIter2);
        System.out.println();
        
        // 测试用例3：复杂二叉树
        System.out.println("测试用例3：复杂二叉树");
        Node testTree = buildTestTree();
        int depth3 = depth(testTree);
        int depthIter3 = depthIterative(testTree);
        System.out.println("树结构：");
        System.out.println("      1");
        System.out.println("     / \\");
        System.out.println("    2   3");
        System.out.println("   /   / \\");
        System.out.println("  4   5   6");
        System.out.println("       \\");
        System.out.println("        7");
        System.out.println("递归方法深度：" + depth3);
        System.out.println("迭代方法深度：" + depthIter3);
        System.out.println();
        
        // 测试用例4：链状树（左倾）
        System.out.println("测试用例4：左倾链状树");
        Node chainTree = new Node(1);
        chainTree.left = new Node(2);
        chainTree.left.left = new Node(3);
        chainTree.left.left.left = new Node(4);
        
        int depth4 = depth(chainTree);
        int depthIter4 = depthIterative(chainTree);
        System.out.println("链状树深度：" + depth4);
        System.out.println("迭代方法深度：" + depthIter4);
        System.out.println();
        
        // 算法特点总结
        System.out.println("算法特点总结：");
        System.out.println("================");
        System.out.println("递归方法：");
        System.out.println("• 优点：代码简洁，易于理解");
        System.out.println("• 缺点：深度过大时可能栈溢出");
        System.out.println("• 时间复杂度：O(N)");
        System.out.println("• 空间复杂度：O(H)，H为树高");
        System.out.println();
        System.out.println("迭代方法：");
        System.out.println("• 优点：避免栈溢出，可获取层级信息");
        System.out.println("• 缺点：代码相对复杂");
        System.out.println("• 时间复杂度：O(N)");
        System.out.println("• 空间复杂度：O(W)，W为最大层宽度");
        System.out.println();
        System.out.println("应用建议：");
        System.out.println("• 一般情况：使用递归方法，简洁高效");
        System.out.println("• 深度很大：使用迭代方法，避免栈溢出");
        System.out.println("• 需要层信息：使用迭代方法，便于扩展");
    }
}
