package base.tree2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 寻找二叉树中两个节点的最近公共祖先（Lowest Common Ancestor, LCA）
 * 
 * 最近公共祖先定义：对于两个节点n1和n2，最近公共祖先是距离n1和n2最近的公共祖先节点
 * 
 * 解题思路：
 * 方法1：使用父节点映射表 - 构建每个节点到其父节点的映射，然后找交集
 * 方法2：树形DP - 递归过程中同时收集节点查找信息和祖先信息
 * 
 * 时间复杂度：都是O(n)
 * 空间复杂度：方法1为O(n)（哈希表），方法2为O(h)（递归栈）
 */
public class LowestAncestor {
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

    // ==================== 方法1：父节点映射表法 ====================
    
    /**
     * 填充父节点映射表
     * @param head 当前节点
     * @param parent 父节点映射表
     */
    private static void fillParent(Node head, HashMap<Node, Node> parent) {
        if (head.left != null) {
            parent.put(head.left, head);      // 记录左子节点的父节点
            fillParent(head.left, parent);    // 递归处理左子树
        }
        if (head.right != null) {
            parent.put(head.right, head);     // 记录右子节点的父节点
            fillParent(head.right, parent);   // 递归处理右子树
        }
    }

    /**
     * 寻找最近公共祖先（方法1：父节点映射表法）
     * 
     * 算法步骤：
     * 1. 构建所有节点到其父节点的映射表
     * 2. 从n1开始向上遍历，记录所有祖先节点到集合中
     * 3. 从n2开始向上遍历，第一个在集合中出现的节点就是LCA
     * 
     * @param head 树的根节点
     * @param n1 第一个目标节点
     * @param n2 第二个目标节点
     * @return 最近公共祖先节点
     */
    public static Node lowestAncestor1(Node head, Node n1, Node n2) {
        if (head == null) {
            return null;
        }
        
        // 构建父节点映射表
        HashMap<Node, Node> parent = new HashMap<>();
        parent.put(head, null);  // 根节点没有父节点
        fillParent(head, parent);
        
        // 收集n1的所有祖先节点
        HashSet<Node> set1 = new HashSet<>();
        Node cur = n1;
        set1.add(cur);
        while (parent.get(cur) != null) {
            cur = parent.get(cur);
            set1.add(cur);
        }
        
        // 从n2开始向上寻找，第一个在set1中的节点就是LCA
        cur = n2;
        while (!set1.contains(cur)) {
            cur = parent.get(cur);
        }
        return cur;
    }

    // ==================== 方法2：树形DP ====================

    /**
     * 封装递归过程中的信息
     */
    private static class Info {
        public boolean findA;  // 当前子树中是否找到了节点A
        public boolean findB;  // 当前子树中是否找到了节点B
        public Node ans;       // 当前子树中A和B的最近公共祖先（如果存在）

        public Info(boolean a, boolean b, Node an) {
            findA = a;
            findB = b;
            ans = an;
        }
    }

    /**
     * 树形DP的递归实现
     * @param x 当前节点
     * @param a 目标节点A
     * @param b 目标节点B
     * @return 当前子树的信息
     */
    private static Info process(Node x, Node a, Node b) {
        if (x == null) {
            return new Info(false, false, null);  // 空节点：没找到A或B，没有LCA
        }
        
        // 递归获取左右子树信息
        Info li = process(x.left, a, b);
        Info ri = process(x.right, a, b);
        
        // 判断当前子树是否包含节点A和B
        boolean findA = (x == a) || li.findA || ri.findA;
        boolean findB = (x == b) || li.findB || ri.findB;
        
        // 确定当前子树的LCA
        Node ans = null;
        if (li.ans != null) {
            // 左子树已经找到了LCA
            ans = li.ans;
        } else if (ri.ans != null) {
            // 右子树已经找到了LCA
            ans = ri.ans;
        } else {
            // 左右子树都没有LCA，检查当前节点是否为LCA
            if (findA && findB) {
                ans = x;  // 当前节点就是LCA
            }
        }
        
        return new Info(findA, findB, ans);
    }

    /**
     * 寻找最近公共祖先（方法2：树形DP）
     * @param head 树的根节点
     * @param a 第一个目标节点
     * @param b 第二个目标节点
     * @return 最近公共祖先节点
     */
    public static Node lowestAncestor2(Node head, Node a, Node b) {
        return process(head, a, b).ans;
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

    /**
     * 先序遍历收集所有节点
     * @param head 当前节点
     * @param arr 存储节点的列表
     */
    private static void fillPre(Node head, ArrayList<Node> arr) {
        if (head == null) {
            return;
        }
        arr.add(head);
        fillPre(head.left, arr);
        fillPre(head.right, arr);
    }

    /**
     * 从树中随机选择一个节点
     * @param head 树的根节点
     * @return 随机选择的节点
     */
    private static Node randomPick(Node head) {
        if (head == null) {
            return null;
        }
        ArrayList<Node> arr = new ArrayList<>();
        fillPre(head, arr);
        return arr.get((int) (arr.size() * Math.random()));
    }

    public static void main(String[] args) {
        int times = 100000;   // 测试次数
        int maxLevel = 3;     // 最大层级
        int maxVal = 100;     // 最大节点值
        System.out.println("test begin");
        
        // 对拍测试：比较两种方法的结果是否一致
        for (int i = 0; i < times; i++) {
            Node head = randomTree(maxLevel, maxVal);
            Node n1 = randomPick(head);     // 随机选择第一个节点
            Node n2 = randomPick(head);     // 随机选择第二个节点
            Node ans1 = lowestAncestor1(head, n1, n2);
            Node ans2 = lowestAncestor2(head, n1, n2);
            if (ans1 != ans2) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}
