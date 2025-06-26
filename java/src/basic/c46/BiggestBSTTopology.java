package basic.c46;

import java.util.HashMap;
import java.util.Map;

/**
 * 最大BST拓扑结构问题
 * 
 * 问题描述：
 * 给定一棵二叉树，求其中符合二叉搜索树拓扑结构的最大子结构的节点数量
 * 拓扑结构是指：可以只保留部分节点，但保留的节点之间的父子关系必须和原树一致
 * BST要求：左子树所有节点值 < 根节点值 < 右子树所有节点值
 * 
 * 例如：
 *      6
 *     / \
 *    1   12
 *   / \  / \
 *  0  3 10 13
 *      /  \
 *     4   14
 * 
 * 可以保留节点{6, 1, 0, 3, 12, 10, 14}形成BST拓扑结构
 * 
 * 解法思路：
 * 方法1（暴力）：
 * - 枚举每个节点作为BST的根
 * - 对每个根，递归计算符合BST性质的子树大小
 * 
 * 方法2（优化）：
 * - 使用后序遍历 + 记录每个节点的BST信息
 * - 利用子树信息避免重复计算
 * 
 * 时间复杂度：
 * - 方法1：O(n² * h) 其中h是树高
 * - 方法2：O(n²)
 * 
 * 空间复杂度：O(n)
 * 
 * @author 算法学习
 */
public class BiggestBSTTopology {
    
    /**
     * 二叉树节点定义
     */
    private static class Node {
        public int val;        // 节点值
        public Node left;      // 左子节点
        public Node right;     // 右子节点

        public Node(int v) {
            this.val = v;
        }
    }

    /**
     * 判断节点n是否在以h为根的BST中的正确位置
     * 
     * @param h BST的根节点
     * @param n 要检查的节点
     * @param val 节点n的值
     * @return 如果节点在正确位置返回true，否则返回false
     * 
     * 算法思路：
     * 按照BST的性质，从根节点开始查找，如果val < root.val就往左找，否则往右找
     * 如果能找到节点n，说明它在BST中的正确位置
     */
    private static boolean isBSTNode(Node h, Node n, int val) {
        if (h == null) {
            return false;
        }
        if (h == n) {
            return true;  // 找到了目标节点
        }
        // 根据BST性质决定往左还是往右找
        return isBSTNode(val < h.val ? h.left : h.right, n, val);
    }

    /**
     * 计算以h为根的BST中，包含节点n的子树的大小
     * 
     * @param h BST的根节点
     * @param n 子树的根节点
     * @return 符合BST拓扑结构的子树大小
     * 
     * 算法思路：
     * 如果节点n在BST中的正确位置，则递归计算其左右子树的大小
     * 否则返回0（不符合BST拓扑结构）
     */
    private static int size(Node h, Node n) {
        if (h != null && n != null && isBSTNode(h, n, n.val)) {
            return size(h, n.left) + size(h, n.right) + 1;
        }
        return 0;
    }

    /**
     * 方法1：暴力求解最大BST拓扑结构
     * 
     * @param head 二叉树的根节点
     * @return 最大BST拓扑结构的节点数量
     * 
     * 算法思路：
     * 枚举每个节点作为BST的根，计算以该节点为根的最大BST拓扑结构
     * 递归遍历所有节点，取最大值
     */
    public static int max1(Node head) {
        if (head == null) {
            return 0;
        }
        
        // 计算以当前节点为根的BST拓扑结构大小
        int max = size(head, head);
        
        // 递归计算左右子树的最大BST拓扑结构
        max = Math.max(max1(head.left), max);
        max = Math.max(max1(head.right), max);
        
        return max;
    }

    /**
     * BST信息记录类
     * 记录每个节点左右子树中符合BST条件的节点数量
     */
    private static class Record {
        public int l;  // 左子树中符合BST条件的节点数量
        public int r;  // 右子树中符合BST条件的节点数量
        
        public Record(int left, int right) {
            this.l = left;
            this.r = right;
        }
    }

    /**
     * 修改节点的BST信息，删除不符合条件的子树
     * 
     * @param node 要修改的节点
     * @param upVal 上层节点的值（用于判断BST条件）
     * @param map 存储节点BST信息的映射
     * @param isLeft 当前节点是否为上层节点的左子树
     * @return 需要减去的节点数量
     * 
     * 算法思路：
     * 如果当前节点不满足BST条件，则删除整个子树
     * 否则递归处理不符合条件的子节点，更新BST信息
     */
    private static int modifyMap(Node node, int upVal, Map<Node, Record> map, boolean isLeft) {
        if (node == null || (!map.containsKey(node))) {
            return 0;
        }
        
        Record cur = map.get(node);
        
        // 检查BST条件：左子树节点值 < 父节点值，右子树节点值 > 父节点值
        if ((isLeft && node.val > upVal) || ((!isLeft) && node.val < upVal)) {
            // 不符合BST条件，删除整个子树
            map.remove(node);
            return cur.l + cur.r + 1;
        } else {
            // 符合条件，递归处理可能不符合条件的子树
            int minus = modifyMap(isLeft ? node.right : node.left, upVal, map, isLeft);
            
            // 更新BST信息
            if (isLeft) {
                cur.r = cur.r - minus;  // 左子树的右边需要减去不符合条件的节点
            } else {
                cur.l = cur.l - minus;  // 右子树的左边需要减去不符合条件的节点
            }
            
            map.put(node, cur);
            return minus;
        }
    }

    /**
     * 后序遍历求解最大BST拓扑结构
     * 
     * @param h 当前节点
     * @param map 存储节点BST信息的映射
     * @return 以当前节点为根的子树中最大BST拓扑结构大小
     * 
     * 算法思路：
     * 1. 后序遍历：先处理左右子树
     * 2. 根据当前节点值，修正左右子树的BST信息
     * 3. 计算以当前节点为根的BST大小
     * 4. 返回当前子树中的最大BST拓扑结构大小
     */
    private static int posOrder(Node h, Map<Node, Record> map) {
        if (h == null) {
            return 0;
        }
        
        // 后序遍历：先处理左右子树
        int ls = posOrder(h.left, map);
        int rs = posOrder(h.right, map);
        
        // 根据当前节点值，修正左右子树的BST信息
        modifyMap(h.left, h.val, map, true);   // 左子树所有节点值必须 < h.val
        modifyMap(h.right, h.val, map, false); // 右子树所有节点值必须 > h.val
        
        // 获取修正后的左右子树BST信息
        Record lr = map.get(h.left);
        Record rr = map.get(h.right);
        
        // 计算左右子树符合BST条件的节点数量
        int lbst = lr == null ? 0 : lr.l + lr.r + 1;
        int rbst = rr == null ? 0 : rr.l + rr.r + 1;
        
        // 记录当前节点的BST信息
        map.put(h, new Record(lbst, rbst));
        
        // 返回最大BST拓扑结构大小：
        // 1. 以当前节点为根的BST（lbst + rbst + 1）
        // 2. 左子树中的最大BST（ls）
        // 3. 右子树中的最大BST（rs）
        return Math.max(lbst + rbst + 1, Math.max(ls, rs));
    }

    /**
     * 方法2：优化的BST拓扑结构求解
     * 
     * @param head 二叉树的根节点
     * @return 最大BST拓扑结构的节点数量
     */
    private static int max2(Node head) {
        Map<Node, Record> map = new HashMap<>();
        return posOrder(head, map);
    }

    /**
     * 生成指定数量的空格字符串（用于打印树形结构）
     */
    private static String getSpace(int num) {
        String space = " ";
        StringBuffer buf = new StringBuffer("");
        for (int i = 0; i < num; i++) {
            buf.append(space);
        }
        return buf.toString();
    }

    /**
     * 打印二叉树的树形结构（中序遍历方式）
     */
    private static void printIn(Node head, int height, String to, int len) {
        if (head == null) {
            return;
        }
        printIn(head.right, height + 1, "v", len);
        String val = to + head.val + to;
        int lenM = val.length();
        int lenL = (len - lenM) / 2;
        int lenR = len - lenM - lenL;
        val = getSpace(lenL) + val + getSpace(lenR);
        System.out.println(getSpace(height * len) + val);
        printIn(head.left, height + 1, "^", len);
    }

    /**
     * 打印二叉树
     */
    private static void print(Node head) {
        printIn(head, 0, "H", 17);
        System.out.println();
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 最大BST拓扑结构测试 ===");
        
        // 构建测试二叉树
        Node head = new Node(6);
        head.left = new Node(1);
        head.left.left = new Node(0);
        head.left.right = new Node(3);
        head.right = new Node(12);
        head.right.left = new Node(10);
        head.right.left.left = new Node(4);
        head.right.left.left.left = new Node(2);
        head.right.left.left.right = new Node(5);
        head.right.left.right = new Node(14);
        head.right.left.right.left = new Node(11);
        head.right.left.right.right = new Node(15);
        head.right.right = new Node(13);
        head.right.right.left = new Node(20);
        head.right.right.right = new Node(16);
        
        System.out.println("测试二叉树结构:");
        print(head);

        System.out.println("方法1（暴力解法）结果: " + max1(head));
        System.out.println("方法2（优化解法）结果: " + max2(head));
        
        // 简单测试用例
        System.out.println("\n=== 简单测试用例 ===");
        
        // 完全BST
        Node bst = new Node(5);
        bst.left = new Node(3);
        bst.right = new Node(7);
        bst.left.left = new Node(2);
        bst.left.right = new Node(4);
        bst.right.left = new Node(6);
        bst.right.right = new Node(8);
        
        System.out.println("完全BST测试:");
        print(bst);
        System.out.println("方法1结果: " + max1(bst));
        System.out.println("方法2结果: " + max2(bst));
        
        // 性能对比
        System.out.println("\n=== 性能对比 ===");
        long start = System.currentTimeMillis();
        int result1 = max1(head);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = max2(head);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.printf("方法1: 结果=%d, 耗时=%dms\n", result1, time1);
        System.out.printf("方法2: 结果=%d, 耗时=%dms\n", result2, time2);
        
        if (time2 > 0) {
            System.out.printf("方法2比方法1快 %.2f 倍\n", (double)time1 / time2);
        }
    }
}
