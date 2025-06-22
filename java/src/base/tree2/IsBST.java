package base.tree2;

import java.util.ArrayList;

/**
 * 判断二叉树是否为二叉搜索树（BST）
 * 
 * 二叉搜索树定义：
 * 1. 对于任意节点，其左子树中所有节点的值都小于该节点的值
 * 2. 对于任意节点，其右子树中所有节点的值都大于该节点的值
 * 3. 左右子树也都是二叉搜索树
 * 
 * 解题思路：
 * 方法1：中序遍历 - BST的中序遍历结果应该是严格递增的序列
 * 方法2：树形DP - 收集每个子树的最大值、最小值和BST性质信息
 * 
 * 时间复杂度：都是O(n)
 * 空间复杂度：方法1为O(n)（存储中序遍历结果），方法2为O(h)（递归栈空间）
 */
public class IsBST {

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

    // ==================== 方法1：中序遍历法 ====================
    
    /**
     * 中序遍历收集所有节点
     * @param head 当前节点
     * @param arr 存储遍历结果的列表
     */
    private static void in(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        in(head.left, arr);    // 遍历左子树
        arr.add(head);         // 访问当前节点
        in(head.right, arr);   // 遍历右子树
    }

    /**
     * 判断是否为BST（方法1：中序遍历法）
     * 原理：BST的中序遍历结果应该是严格递增序列
     * @param head 树的根节点
     * @return 是否为BST
     */
    public static boolean isBST1(Node head) {
        if (head == null) {
            return true;
        }
        
        // 中序遍历收集所有节点
        ArrayList<Node> arr = new ArrayList<>();
        in(head, arr);
        
        // 检查中序遍历结果是否严格递增
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).val <= arr.get(i - 1).val) {
                return false;  // 不是严格递增，不是BST
            }
        }
        return true;
    }

    // ==================== 方法2：树形DP ====================
    
    /**
     * 封装子树信息的类
     */
    private static class Info {
        public boolean isBST;  // 当前子树是否为BST
        public int max;        // 当前子树的最大值
        public int min;        // 当前子树的最小值

        public Info(boolean is, int max, int min) {
            this.isBST = is;
            this.max = max;
            this.min = min;
        }
    }

    /**
     * 树形DP的递归实现
     * @param x 当前节点
     * @return 当前子树的信息（BST性质 + 最大值 + 最小值）
     */
    private static Info process(Node x) {
        if (x == null) {
            return null;  // 空子树返回null
        }
        
        // 递归获取左右子树信息
        Info li = process(x.left);
        Info ri = process(x.right);
        
        // 计算当前子树的最大值和最小值
        int max = x.val;
        int min = x.val;
        if (li != null) {
            max = Math.max(li.max, max);
            min = Math.min(li.min, min);
        }
        if (ri != null) {
            max = Math.max(ri.max, max);
            min = Math.min(ri.min, min);
        }

        // 判断当前子树是否为BST
        boolean isBST = false;
        
        // 左子树是否为BST（空子树视为BST）
        boolean lb = li == null ? true : li.isBST;
        // 右子树是否为BST（空子树视为BST）
        boolean rb = ri == null ? true : ri.isBST;
        // 左子树的最大值是否小于当前节点值
        boolean lm = li == null ? true : (li.max < x.val);
        // 右子树的最小值是否大于当前节点值
        boolean rm = ri == null ? true : (ri.min > x.val);
        
        // 当前子树是BST的条件：
        // 1. 左子树是BST 2. 右子树是BST 3. 左子树最大值 < 当前值 4. 右子树最小值 > 当前值
        if (lb && rb && lm && rm) {
            isBST = true;
        }
        
        return new Info(isBST, max, min);
    }

    /**
     * 判断是否为BST（方法2：树形DP）
     * @param head 树的根节点
     * @return 是否为BST
     */
    public static boolean isBST2(Node head) {
        if (head == null) {
            return true;
        }
        return process(head).isBST;
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
            if (isBST1(head) != isBST2(head)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
