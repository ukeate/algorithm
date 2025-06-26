package basic.c43;

import java.util.HashMap;

/**
 * 树上最长路径和问题
 * 
 * 问题描述：
 * 给定一个二叉树和一个目标值k，找到树中累加和等于k的最长路径长度
 * 路径必须是从某个节点开始向下的连续路径（不能往上回头）
 * 
 * 算法思路：
 * 使用前缀和 + DFS遍历的方法
 * 1. 对每个节点，维护从根到当前节点的路径前缀和
 * 2. 使用HashMap记录每个前缀和第一次出现的层级
 * 3. 利用前缀和性质：sum[i] - sum[j] = k，则从j+1到i的子路径和为k
 * 4. 在DFS回溯时清理HashMap，保证正确性
 * 
 * 核心技巧：
 * - 前缀和思想在树结构上的应用
 * - DFS + 回溯保证每条路径独立计算
 * - HashMap优化查找时间复杂度
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - h为树的高度，递归栈 + HashMap空间
 * 
 * @author 算法学习
 */
public class LongestTreeSum {
    
    /**
     * 二叉树节点定义
     */
    public static class Node {
        int val;           // 节点值
        public Node left;  // 左子节点
        public Node right; // 右子节点
        
        public Node(int v) {
            val = v;
        }
    }

    /** 全局变量：记录找到的最长路径长度 */
    public static int ans = 0;
    
    /**
     * DFS遍历树，寻找累加和为k的最长路径
     * 
     * @param x 当前遍历的节点
     * @param level 当前节点的层级（深度）
     * @param preSum 从根节点到当前节点父亲的路径前缀和
     * @param k 目标累加和
     * @param sumMap 前缀和到层级的映射表
     * 
     * 算法核心：
     * 1. 计算到当前节点的前缀和 allSum
     * 2. 查找是否存在前缀和为 (allSum - k) 的祖先节点
     * 3. 如果存在，说明从那个祖先的下一层到当前节点的路径和为k
     * 4. 递归处理左右子树
     * 5. 回溯时清理当前节点的前缀和记录
     */
    private static void process(Node x, int level, int preSum, int k, HashMap<Integer, Integer> sumMap) {
        if (x != null) {
            // 计算从根到当前节点的前缀和
            int allSum = preSum + x.val;
            
            // 检查是否存在以当前节点为终点、和为k的路径
            // 如果存在前缀和为(allSum - k)的祖先，说明找到了目标路径
            if (sumMap.containsKey(allSum - k)) {
                // 计算路径长度：当前层级 - 祖先层级
                ans = Math.max(ans, level - sumMap.get(allSum - k));
            }
            
            // 将当前前缀和记录到map中（如果还没记录过）
            // 只记录第一次出现的层级，保证路径最长
            if (!sumMap.containsKey(allSum)) {
                sumMap.put(allSum, level);
            }
            
            // 递归处理左右子树
            process(x.left, level + 1, allSum, k, sumMap);
            process(x.right, level + 1, allSum, k, sumMap);
            
            // 回溯：如果当前前缀和是在当前层级首次记录的，则删除
            // 这样保证每条从根到叶子的路径都能独立计算
            if (sumMap.get(allSum) == level) {
                sumMap.remove(allSum);
            }
        }
    }

    /**
     * 寻找树中累加和为k的最长路径长度
     * 
     * @param head 树的根节点
     * @param k 目标累加和
     * @return 最长路径的长度（节点个数）
     * 
     * 初始化说明：
     * - ans设为0，记录全局最优解
     * - sumMap初始放入(0, -1)，表示空路径的前缀和为0，层级为-1
     * - 这样当某个节点值本身等于k时，能正确计算路径长度为level-(-1)=level+1
     */
    public static int longest(Node head, int k) {
        ans = 0; // 重置全局答案
        HashMap<Integer, Integer> sumMap = new HashMap<>();
        
        // 初始化：前缀和0对应层级-1（表示根节点的父亲）
        // 这样如果根节点本身就等于k，路径长度计算为0-(-1)=1
        sumMap.put(0, -1);
        
        process(head, 0, 0, k, sumMap);
        return ans;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        int k = 0; // 寻找累加和为0的路径
        
        // 构建测试二叉树
        /*
         *           3
         *         /   \
         *       -2     3
         *      / \    / \
         *     1   4  5  -7
         *    / \ / \   / \
         *   3 5 2 5  -5 1 5
         */
        Node head = new Node(3);
        head.left = new Node(-2);
        head.left.left = new Node(1);
        head.left.right = new Node(4);
        head.left.left.left = new Node(3);
        head.left.left.right = new Node(5);
        head.left.right.left = new Node(2);
        head.left.right.right = new Node(5);

        head.right = new Node(3);
        head.right.left = new Node(5);
        head.right.right = new Node(-7);
        head.right.left.left = new Node(-5);
        head.right.left.right = new Node(-3);
        head.right.right.left = new Node(1);
        head.right.right.right = new Node(5);

        System.out.println("累加和为 " + k + " 的最长路径长度: " + longest(head, k));
        
        // 测试其他目标值
        System.out.println("累加和为 5 的最长路径长度: " + longest(head, 5));
        System.out.println("累加和为 3 的最长路径长度: " + longest(head, 3));
    }
}
