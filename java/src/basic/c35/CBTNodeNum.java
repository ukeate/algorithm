package basic.c35;

/**
 * 完全二叉树节点个数统计
 * 
 * 问题描述：
 * 给定一个完全二叉树的根节点，计算该树的节点总数。
 * 要求时间复杂度小于O(N)。
 * 
 * 完全二叉树性质：
 * 1. 除了最后一层，其他层都是满的
 * 2. 最后一层的节点都靠左排列
 * 
 * 算法思路：
 * 利用完全二叉树的性质，对于任何一个节点：
 * 1. 先求出该节点右子树的最左深度
 * 2. 如果右子树的最左深度等于整棵树的高度，说明左子树是满二叉树
 * 3. 如果不等，说明右子树是满二叉树（高度比整棵树少1）
 * 4. 利用满二叉树节点数公式：2^h - 1（h为高度）
 * 
 * 时间复杂度：O(log²N)
 * - 每层递归需要O(logN)时间求最左深度
 * - 总共有O(logN)层递归
 * 
 * 空间复杂度：O(logN)（递归调用栈）
 */
public class CBTNodeNum {
    
    /**
     * 二叉树节点定义
     */
    public static class Node {
        public int val;      // 节点值
        public Node left;    // 左子节点
        public Node right;   // 右子节点
        
        /**
         * 构造函数
         * @param v 节点值
         */
        public Node(int v) {
            this.val = v;
        }
    }

    /**
     * 求从指定节点开始的最左路径深度
     * 
     * @param node 起始节点
     * @param level 当前层级
     * @return 最左路径的深度
     */
    private static int mostLeftLevel(Node node, int level) {
        while (node != null) {
            level++;           // 深度加1
            node = node.left;  // 继续向左
        }
        return level - 1; // 返回实际深度（减去初始的1）
    }

    /**
     * 递归计算完全二叉树节点数量
     * 
     * 核心算法：
     * 1. 如果到达叶子节点层，返回1
     * 2. 求右子树的最左深度
     * 3. 如果右子树最左深度等于整棵树高度：
     *    - 左子树是满二叉树，节点数为2^(h-level)
     *    - 递归求右子树节点数
     * 4. 否则：
     *    - 右子树是满二叉树，节点数为2^(h-level-1)
     *    - 递归求左子树节点数
     * 
     * @param node 当前节点
     * @param level 当前层级
     * @param h 整棵树的高度
     * @return 以node为根的子树的节点数量
     */
    private static int nodeNum(Node node, int level, int h) {
        // 递归终止条件：到达最后一层
        if (level == h) {
            return 1;
        }
        
        // 求右子树的最左深度
        if (mostLeftLevel(node.right, level + 1) == h) {
            // 情况1：右子树的最左深度等于整棵树高度
            // 说明左子树是满二叉树
            // 左子树节点数：2^(h-level)，加上当前节点，再加上右子树节点数
            return (1 << (h - level)) + nodeNum(node.right, level + 1, h);
        } else {
            // 情况2：右子树的最左深度小于整棵树高度
            // 说明右子树是高度为(h-level-1)的满二叉树
            // 右子树节点数：2^(h-level-1)，加上当前节点，再加上左子树节点数
            return (1 << (h - level - 1)) + nodeNum(node.left, level + 1, h);
        }
    }
    
    /**
     * 计算完全二叉树的节点总数
     * 
     * @param head 树的根节点
     * @return 节点总数
     */
    public static int num(Node head) {
        if (head == null) {
            return 0; // 空树返回0
        }
        
        // 首先求出整棵树的高度
        int height = mostLeftLevel(head, 1);
        
        // 调用递归函数计算节点数
        return nodeNum(head, 1, height);
    }

    /**
     * 测试方法：构建测试用例验证算法
     * 
     * 测试用例构建的完全二叉树：
     *       1
     *      / \
     *     2   3
     *    / \ /
     *   4  5 6
     * 
     * 总节点数：6
     */
    public static void main(String[] args) {
        // 构建完全二叉树
        Node head = new Node(1);
        head.left = new Node(2);
        head.right = new Node(3);
        head.left.left = new Node(4);
        head.left.right = new Node(5);
        head.right.left = new Node(6);
        
        System.out.println("完全二叉树节点总数: " + num(head));
        System.out.println("预期结果: 6");
        
        // 测试空树
        System.out.println("空树节点数: " + num(null));
        
        // 测试单节点树
        Node singleNode = new Node(1);
        System.out.println("单节点树节点数: " + num(singleNode));
        
        // 测试满二叉树情况
        Node fullTree = new Node(1);
        fullTree.left = new Node(2);
        fullTree.right = new Node(3);
        fullTree.left.left = new Node(4);
        fullTree.left.right = new Node(5);
        fullTree.right.left = new Node(6);
        fullTree.right.right = new Node(7);
        System.out.println("满二叉树节点数: " + num(fullTree));
        System.out.println("预期结果: 7");
    }
}
