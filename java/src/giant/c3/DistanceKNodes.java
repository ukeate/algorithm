package giant.c3;

import java.util.*;

/**
 * 二叉树中距离目标节点K距离的所有节点
 * 
 * 问题描述：
 * 给定一个二叉树、一个目标节点target和一个整数值K，
 * 找出所有与目标节点距离为K的节点。
 * 
 * 算法思路：
 * 这个问题的核心挑战是处理"向上"的路径，因为二叉树节点通常只有向下的子节点引用。
 * 
 * 解决方案：
 * 1. 首先遍历整棵树，建立每个节点到其父节点的映射
 * 2. 从目标节点开始，进行广度优先搜索(BFS)
 * 3. 在BFS中，每个节点可以向三个方向扩展：左子节点、右子节点、父节点
 * 4. 使用visited集合避免重复访问节点
 * 5. 当达到距离K时，收集所有节点
 * 
 * 时间复杂度：O(N)，其中N是二叉树的节点数
 * 空间复杂度：O(N)，需要存储父节点映射和BFS队列
 */
public class DistanceKNodes {
    
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
            val = v;
        }
    }

    /**
     * 递归建立父子关系映射
     * 通过深度优先遍历整棵树，为每个节点建立到其父节点的映射
     * 
     * @param cur 当前节点
     * @param parents 父节点映射表，key是子节点，value是父节点
     */
    private static void parents(Node cur, HashMap<Node, Node> parents) {
        if (cur == null) {
            return; // 空节点直接返回
        }
        
        // 为左子节点建立父子关系并递归处理
        if (cur.left != null) {
            parents.put(cur.left, cur);   // 左子节点的父节点是cur
            parents(cur.left, parents);   // 递归处理左子树
        }
        
        // 为右子节点建立父子关系并递归处理
        if (cur.right != null) {
            parents.put(cur.right, cur);  // 右子节点的父节点是cur
            parents(cur.right, parents);  // 递归处理右子树
        }
    }

    /**
     * 查找距离目标节点K距离的所有节点
     * 
     * 算法流程：
     * 1. 建立父子关系映射，使得可以从任意节点向上访问
     * 2. 从目标节点开始进行层次遍历(BFS)
     * 3. 每层遍历时，节点可以向三个方向扩展：左子、右子、父节点
     * 4. 使用visited集合避免重复访问，防止无限循环
     * 5. 当遍历到第K层时，该层的所有节点就是答案
     * 
     * @param root 树的根节点
     * @param target 目标节点
     * @param k 距离值
     * @return 距离目标节点k距离的所有节点列表
     */
    public static List<Node> find(Node root, Node target, int k) {
        // 步骤1：建立父子关系映射
        HashMap<Node, Node> parents = new HashMap<>();
        parents.put(root, null); // 根节点的父节点为null
        parents(root, parents);  // 递归建立所有父子关系
        
        // 步骤2：准备BFS所需的数据结构
        Queue<Node> que = new LinkedList<>();    // BFS队列
        HashSet<Node> visited = new HashSet<>(); // 访问标记集合
        
        // 初始化：将目标节点加入队列和访问集合
        que.offer(target);
        visited.add(target);
        
        int curLevel = 0;           // 当前层级（距离）
        List<Node> ans = new ArrayList<>(); // 结果列表
        
        // 步骤3：BFS层次遍历
        while (!que.isEmpty()) {
            int size = que.size(); // 当前层的节点数量
            
            // 处理当前层的所有节点
            while (size-- > 0) {
                Node cur = que.poll(); // 取出队首节点
                
                // 如果当前层级等于目标距离k，收集该节点
                if (curLevel == k) {
                    ans.add(cur);
                }
                
                // 向左子节点扩展（如果存在且未访问）
                if (cur.left != null && !visited.contains(cur.left)) {
                    visited.add(cur.left);
                    que.offer(cur.left);
                }
                
                // 向右子节点扩展（如果存在且未访问）
                if (cur.right != null && !visited.contains(cur.right)) {
                    visited.add(cur.right);
                    que.offer(cur.right);
                }
                
                // 向父节点扩展（如果存在且未访问）
                if (parents.get(cur) != null && !visited.contains(parents.get(cur))) {
                    visited.add(parents.get(cur));
                    que.offer(parents.get(cur));
                }
            }
            
            // 处理完当前层，层级加1
            curLevel++;
            
            // 如果已经超过目标距离，提前结束
            if (curLevel > k) {
                break;
            }
        }
        
        return ans;
    }
    
    /**
     * 辅助方法：打印节点列表的值
     * @param nodes 节点列表
     * @return 节点值的字符串表示
     */
    private static String printNodes(List<Node> nodes) {
        if (nodes.isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).val);
            if (i < nodes.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * 测试方法：构建测试用例验证算法
     */
    public static void main(String[] args) {
        // 构建测试二叉树：
        //       3
        //      / \
        //     5   1
        //    / \ / \
        //   6  2 0  8
        //     / \
        //    7  4
        
        Node root = new Node(3);
        root.left = new Node(5);
        root.right = new Node(1);
        root.left.left = new Node(6);
        root.left.right = new Node(2);
        root.right.left = new Node(0);
        root.right.right = new Node(8);
        root.left.right.left = new Node(7);
        root.left.right.right = new Node(4);
        
        // 测试用例1：以节点5为目标，查找距离2的所有节点
        Node target1 = root.left; // 节点5
        List<Node> result1 = find(root, target1, 2);
        System.out.println("测试1 - 目标节点：" + target1.val + "，距离：2");
        System.out.println("结果：" + printNodes(result1));
        System.out.println("预期：[7, 4, 1]");
        
        // 测试用例2：以节点5为目标，查找距离1的所有节点  
        List<Node> result2 = find(root, target1, 1);
        System.out.println("\n测试2 - 目标节点：" + target1.val + "，距离：1");
        System.out.println("结果：" + printNodes(result2));
        System.out.println("预期：[6, 2, 3]");
        
        // 测试用例3：以根节点为目标，查找距离2的所有节点
        Node target3 = root; // 节点3
        List<Node> result3 = find(root, target3, 2);
        System.out.println("\n测试3 - 目标节点：" + target3.val + "，距离：2");
        System.out.println("结果：" + printNodes(result3));
        System.out.println("预期：[6, 2, 0, 8]");
        
        // 测试用例4：距离为0的情况（应该返回目标节点本身）
        List<Node> result4 = find(root, target1, 0);
        System.out.println("\n测试4 - 目标节点：" + target1.val + "，距离：0");
        System.out.println("结果：" + printNodes(result4));
        System.out.println("预期：[5]");
    }
}
