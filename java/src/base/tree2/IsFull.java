package base.tree2;

/**
 * 判断二叉树是否为满二叉树（Full Binary Tree）
 * 
 * 满二叉树定义：如果一棵二叉树的层数为h，则节点总数为2^h - 1
 * 也就是说，除了叶子节点外，每个节点都有两个子节点，且所有叶子节点都在同一层
 * 
 * 解题思路：
 * 方法1：计算节点数和高度 - 满二叉树的节点数 = 2^h - 1
 * 方法2：树形DP - 递归判断左右子树都是满二叉树且高度相等
 * 
 * 时间复杂度：都是O(n)
 * 空间复杂度：O(h)（递归栈空间）
 */
public class IsFull {
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

    // ==================== 方法1：节点数与高度关系法 ====================
    
    /**
     * 封装子树基本信息的类（方法1）
     */
    private static class Info1 {
        public int height;   // 子树高度
        public int size;     // 子树节点总数

        public Info1(int h, int n) {
            height = h;
            this.size = n;
        }
    }

    /**
     * 收集子树高度和节点数信息（方法1的递归实现）
     * @param head 当前节点
     * @return 当前子树的信息（高度 + 节点数）
     */
    private static Info1 process1(Node head) {
        if (head == null) {
            return new Info1(0, 0);  // 空树高度为0，节点数为0
        }
        
        // 递归获取左右子树信息
        Info1 li = process1(head.left);
        Info1 ri = process1(head.right);
        
        // 计算当前子树的高度和节点数
        int height = Math.max(li.height, ri.height) + 1;
        int n = li.size + ri.size + 1;
        
        return new Info1(height, n);
    }

    /**
     * 判断是否为满二叉树（方法1：节点数与高度关系法）
     * 原理：满二叉树的节点数 = 2^h - 1
     * @param head 树的根节点
     * @return 是否为满二叉树
     */
    public static boolean isFull1(Node head) {
        if (head == null) {
            return true;  // 空树视为满二叉树
        }
        Info1 info = process1(head);
        // 检查是否满足：节点数 = 2^高度 - 1
        return (1 << info.height) - 1 == info.size;
    }

    // ==================== 方法2：树形DP ====================
    
    /**
     * 封装子树信息的类（方法2）
     */
    private static class Info2 {
        public boolean isFull;  // 当前子树是否为满二叉树
        public int height;      // 当前子树的高度

        public Info2(boolean f, int h) {
            isFull = f;
            height = h;
        }
    }

    /**
     * 树形DP的递归实现（方法2）
     * @param h 当前节点
     * @return 当前子树的信息（满二叉树性质 + 高度）
     */
    private static Info2 process2(Node h) {
        if (h == null) {
            return new Info2(true, 0);  // 空树是满二叉树，高度为0
        }
        
        // 递归获取左右子树信息
        Info2 li = process2(h.left);
        Info2 ri = process2(h.right);
        
        // 当前子树是满二叉树的条件：
        // 1. 左子树是满二叉树 AND 右子树是满二叉树 AND 左右子树高度相等
        boolean isFull = li.isFull && ri.isFull && li.height == ri.height;
        
        // 计算当前子树高度
        int height = Math.max(li.height, ri.height) + 1;
        
        return new Info2(isFull, height);
    }

    /**
     * 判断是否为满二叉树（方法2：树形DP）
     * @param head 树的根节点
     * @return 是否为满二叉树
     */
    public static boolean isFull2(Node head) {
        if (head == null) {
            return true;
        }
        return process2(head).isFull;
    }

    // ==================== 测试工具方法 ====================
    
    /**
     * 随机生成树节点（递归）
     * @param level 当前层级
     * @param maxLevel 最大层级
     * @param maxVal 最大节点值
     * @return 随机生成的节点
     */
    private static Node randomLevel(int level, int maxLevel, int maxVal) {
        if (level > maxLevel || Math.random() < 0.5) {
            return null;
        }
        Node head = new Node((int) ((maxVal + 1) * Math.random()));
        head.left = randomLevel(level + 1, maxLevel, maxVal);
        head.right = randomLevel(level + 1, maxLevel, maxVal);
        return head;
    }

    /**
     * 随机生成二叉树
     * @param maxLevel 最大层级
     * @param maxVal 最大节点值
     * @return 随机生成的树根节点
     */
    private static Node randomTree(int maxLevel, int maxVal) {
        return randomLevel(1, maxLevel, maxVal);
    }

    public static void main(String[] args) {
        int times = 100000;   // 测试次数
        int maxLevel = 10;    // 最大层级
        int maxVal = 100;     // 最大节点值
        System.out.println("test begin");
        
        // 对拍测试：比较两种方法的结果是否一致
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            boolean ans1 = isFull1(head);
            boolean ans2 = isFull2(head);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}
