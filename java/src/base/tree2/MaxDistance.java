package base.tree2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 二叉树中任意两个节点间的最大距离
 * 
 * 问题描述：给定一个二叉树，找到树中任意两个节点间的最大距离
 * 距离定义：两个节点之间路径上的边数
 * 
 * 解题思路：
 * 方法1：暴力方法 - 对每对节点计算距离，找最大值
 * 方法2：树形DP - 对每个节点，最大距离可能是：
 *   - 左子树内的最大距离
 *   - 右子树内的最大距离  
 *   - 经过当前节点的最大距离（左子树高度 + 右子树高度 + 1）
 * 
 * 时间复杂度：
 * 方法1：O(n^2) 
 * 方法2：O(n)
 */
public class MaxDistance {
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

    // ==================== 方法1：暴力方法 ====================
    
    /**
     * 计算两个节点之间的距离
     * @param parent 父节点映射表
     * @param n1 第一个节点
     * @param n2 第二个节点
     * @return 两节点间的距离
     */
    private static int distance(HashMap<Node, Node> parent, Node n1, Node n2) {
        // 收集n1到根的所有节点
        HashSet<Node> set1 = new HashSet<>();
        Node cur = n1;
        set1.add(cur);
        while (parent.get(cur) != null) {
            cur = parent.get(cur);
            set1.add(cur);
        }
        
        // 从n2向上找，找到第一个公共祖先
        cur = n2;
        while (!set1.contains(cur)) {
            cur = parent.get(cur);
        }
        Node lca = cur;  // 最近公共祖先
        
        // 计算n1到lca的距离
        cur = n1;
        int distance1 = 1;
        while (cur != lca) {
            cur = parent.get(cur);
            distance1++;
        }
        
        // 计算n2到lca的距离
        cur = n2;
        int distance2 = 1;
        while (cur != lca) {
            cur = parent.get(cur);
            distance2++;
        }
        
        // 总距离 = distance1 + distance2 - 1（减1是因为lca被算了两次）
        return distance1 + distance2 - 1;
    }

    /**
     * 填充父节点映射表
     * @param head 当前节点
     * @param map 父节点映射表
     */
    private static void fillParent(Node head, HashMap<Node, Node> map) {
        if (head.left != null) {
            map.put(head.left, head);       // 记录左子节点的父节点
            fillParent(head.left, map);     // 递归处理左子树
        }
        if (head.right != null) {
            map.put(head.right, head);      // 记录右子节点的父节点
            fillParent(head.right, map);    // 递归处理右子树
        }
    }

    /**
     * 构建父节点映射表
     * @param head 树的根节点
     * @return 父节点映射表
     */
    private static HashMap<Node, Node> parentMap(Node head) {
        HashMap<Node, Node> map = new HashMap<>();
        map.put(head, null);  // 根节点没有父节点
        fillParent(head, map);
        return map;
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
        arr.add(head);                // 访问当前节点
        fillPre(head.left, arr);      // 递归处理左子树
        fillPre(head.right, arr);     // 递归处理右子树
    }

    /**
     * 获取树的所有节点列表
     * @param head 树的根节点
     * @return 所有节点的列表
     */
    private static ArrayList<Node> preList(Node head) {
        ArrayList<Node> arr = new ArrayList<>();
        fillPre(head, arr);
        return arr;
    }

    /**
     * 求二叉树中的最大距离（方法1：暴力方法）
     * 算法：枚举所有节点对，计算每对节点的距离，取最大值
     * @param head 树的根节点
     * @return 最大距离
     */
    public static int maxDistance1(Node head) {
        if (head == null) {
            return 0;
        }
        
        // 获取所有节点
        ArrayList<Node> arr = preList(head);
        // 构建父节点映射表
        HashMap<Node, Node> parentMap = parentMap(head);
        
        int max = 0;
        // 枚举所有节点对，计算距离
        for (int i = 0; i < arr.size(); i++) {
            for (int j = i; j < arr.size(); j++) {
                max = Math.max(max, distance(parentMap, arr.get(i), arr.get(j)));
            }
        }
        return max;
    }

    // ==================== 方法2：树形DP ====================

    /**
     * 封装子树信息的类
     */
    private static class Info {
        public int maxDistance;  // 当前子树中的最大距离
        public int height;       // 当前子树的高度

        public Info(int m, int h) {
            maxDistance = m;
            height = h;
        }
    }

    /**
     * 树形DP的递归实现
     * @param x 当前节点
     * @return 当前子树的信息（最大距离 + 高度）
     */
    private static Info process(Node x) {
        if (x == null) {
            return new Info(0, 0);  // 空节点：距离0，高度0
        }
        
        // 递归获取左右子树信息
        Info li = process(x.left);
        Info ri = process(x.right);
        
        // 三种情况的最大距离：
        int p1 = li.maxDistance;                    // 情况1：左子树内的最大距离
        int p2 = ri.maxDistance;                    // 情况2：右子树内的最大距离
        int p0 = li.height + ri.height + 1;        // 情况3：经过当前节点的最大距离
        
        // 当前子树高度
        int height = Math.max(li.height, ri.height) + 1;
        
        return new Info(Math.max(Math.max(p1, p2), p0), height);
    }

    /**
     * 求二叉树中的最大距离（方法2：树形DP）
     * @param head 树的根节点
     * @return 最大距离
     */
    public static int maxDistance2(Node head) {
        return process(head).maxDistance;
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
            int ans1 = maxDistance1(head);   // 暴力方法
            int ans2 = maxDistance2(head);   // 树形DP
            if ( ans1 != ans2 ) {
                System.out.println("Wrong");
                System.out.println(ans1 + "|" + ans2);
            }
        }
        System.out.println("test end");
    }
}
