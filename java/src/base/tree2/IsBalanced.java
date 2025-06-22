package base.tree2;

/**
 * 判断二叉树是否为平衡二叉树
 * 
 * 平衡二叉树定义：对于任意一个节点，其左右子树的高度差不超过1
 * 
 * 解题思路：
 * 方法1：暴力解法 - 对每个节点都计算左右子树高度并判断是否平衡
 * 方法2：树形DP - 一次遍历同时收集高度信息和平衡性信息
 * 
 * 时间复杂度：
 * 方法1：O(n^2) 在最坏情况下（退化为链表）
 * 方法2：O(n) 每个节点只访问一次
 */
public class IsBalanced {
    /**
     * 二叉树节点定义
     */
    private static class Node {
        public int val;      // 节点值
        public Node left;    // 左子节点
        public Node right;   // 右子节点

        public Node(int val) {
            this.val = val;
        }
    }

    // ==================== 方法1：暴力解法 ====================
    
    /**
     * 计算树的高度并检查平衡性（方法1的递归实现）
     * @param head 当前节点
     * @param ans 用于记录是否平衡的数组（长度为1的数组用于模拟引用传递）
     * @return 当前子树的高度
     */
    private static int process1(Node head, boolean[] ans) {
        // 如果已经发现不平衡或者到达空节点，直接返回
        if (!ans[0] || head == null) {
            return -1;
        }
        
        // 递归计算左右子树高度
        int leftHeight = process1(head.left, ans);
        int rightHeight = process1(head.right, ans);
        
        // 检查当前节点是否平衡
        if (Math.abs(leftHeight - rightHeight) > 1) {
            ans[0] = false;  // 标记为不平衡
        }
        
        // 返回当前子树的高度
        return Math.max(leftHeight, rightHeight) + 1;
    }

    /**
     * 判断是否为平衡二叉树（方法1：暴力解法）
     * @param head 树的根节点
     * @return 是否为平衡二叉树
     */
    public static boolean isBalanced1(Node head) {
        boolean[] ans = new boolean[1];
        ans[0] = true;  // 初始假设是平衡的
        process1(head, ans);
        return ans[0];
    }

    // ==================== 方法2：树形DP ====================
    
    /**
     * 封装子树信息的类
     */
    private static class Info {
        public boolean isB;   // 当前子树是否平衡
        public int height;    // 当前子树的高度

        public Info(boolean i, int h) {
            this.isB = i;
            this.height = h;
        }
    }

    /**
     * 树形DP的递归实现
     * @param root 当前节点
     * @return 当前子树的信息（是否平衡 + 高度）
     */
    private static Info process2(Node root) {
        // 空节点：平衡且高度为0
        if (root == null) {
            return new Info(true, 0);
        }
        
        // 递归获取左右子树信息
        Info li = process2(root.left);
        Info ri = process2(root.right);
        
        // 计算当前子树高度
        int ht = Math.max(li.height, ri.height) + 1;
        
        // 判断当前子树是否平衡：
        // 1. 左子树平衡 AND 右子树平衡 AND 左右子树高度差 <= 1
        boolean isB = li.isB && ri.isB && Math.abs(li.height - ri.height) < 2;
        
        return new Info(isB, ht);
    }

    /**
     * 判断是否为平衡二叉树（方法2：树形DP）
     * @param root 树的根节点
     * @return 是否为平衡二叉树
     */
    public static boolean isBalanced2(Node root) {
        return process2(root).isB;
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
        if(level > maxLevel || Math.random() < 0.5) {
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
            if (isBalanced1(head) != isBalanced2(head)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
