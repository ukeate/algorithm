package basic.c52;

/**
 * 二叉树最少摄像头覆盖问题
 * 
 * 问题描述：
 * 给定一个二叉树，我们在树的节点上安装摄像头。
 * 节点上的每个摄像头都可以监视其父对象、自身及其直接子对象。
 * 计算监控树的所有节点所需的最少摄像头数量。
 * 
 * 算法思路：
 * 使用动态规划 + 树形DP：
 * 对于每个节点，定义三种状态：
 * 1. uncovered: 节点未被覆盖，以该节点为根的子树需要的最少摄像头数
 * 2. coveredNoCamera: 节点被覆盖但自身没有摄像头，子树需要的最少摄像头数
 * 3. coveredHasCamera: 节点被覆盖并有相机，子树需要的最少摄像头数
 * 
 * 状态转移：
 * - uncovered: 左右子树都是被覆盖且无摄像头的状态
 * - coveredNoCamera: 左右子树至少有一个有摄像头
 * - coveredHasCamera: 当前节点放摄像头，左右子树取最优状态
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(h) 其中h是树的高度
 * 
 * LeetCode: https://leetcode.com/problems/binary-tree-cameras/
 * 
 * @author 算法学习
 */
public class MinCameraCover {
    
    /**
     * 二叉树节点定义
     */
    public static class Node {
        public int val;
        public Node left;
        public Node right;
        
        public Node(int val) {
            this.val = val;
        }
    }

    /**
     * 状态信息类
     * 记录不同状态下的最少摄像头数量
     */
    private static class Info {
        public long uncovered;        // 节点未被覆盖时的最少摄像头数
        public long coveredNoCamera;  // 节点被覆盖但无摄像头时的最少摄像头数
        public long coveredHasCamera; // 节点被覆盖且有摄像头时的最少摄像头数
        
        /**
         * 构造状态信息
         * 
         * @param un 未覆盖状态的摄像头数
         * @param no 覆盖无摄像头状态的摄像头数
         * @param has 覆盖有摄像头状态的摄像头数
         */
        public Info(long un, long no, long has) {
            uncovered = un;
            coveredNoCamera = no;
            coveredHasCamera = has;
        }
    }

    /**
     * 树形DP递归处理
     * 
     * @param x 当前节点
     * @return 当前子树的状态信息
     */
    private static Info process1(Node x) {
        // 空节点的处理
        if (x == null) {
            // 空节点：未覆盖和有摄像头都是不可能的状态，设为无穷大
            // 被覆盖无摄像头是合理的，不需要额外摄像头
            return new Info(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        }
        
        // 递归获取左右子树的状态信息
        Info left = process1(x.left);
        Info right = process1(x.right);
        
        // 计算当前节点的三种状态
        
        // 1. 当前节点未被覆盖：左右子树都必须是被覆盖且无摄像头的状态
        // （因为如果子树有摄像头，当前节点就被覆盖了）
        long uncovered = left.coveredNoCamera + right.coveredNoCamera;
        
        // 2. 当前节点被覆盖但无摄像头：左右子树至少有一个有摄像头
        long coveredNoCamera = Math.min(
            left.coveredHasCamera + right.coveredHasCamera,  // 左右都有摄像头
            Math.min(
                left.coveredHasCamera + right.coveredNoCamera,   // 左有右无
                left.coveredNoCamera + right.coveredHasCamera    // 左无右有
            )
        );
        
        // 3. 当前节点有摄像头：可以覆盖自己和子节点，左右子树取最优状态
        long coveredHasCamera = 1 + 
            Math.min(left.uncovered, Math.min(left.coveredNoCamera, left.coveredHasCamera)) +
            Math.min(right.uncovered, Math.min(right.coveredNoCamera, right.coveredHasCamera));
        
        return new Info(uncovered, coveredNoCamera, coveredHasCamera);
    }

    /**
     * 计算覆盖整棵树所需的最少摄像头数
     * 
     * @param root 树的根节点
     * @return 最少摄像头数
     */
    public static int min(Node root) {
        Info info = process1(root);
        
        // 根节点的最优解：
        // - 如果根节点未被覆盖，需要额外放置一个摄像头
        // - 否则取被覆盖状态的最优解
        return (int) Math.min(
            info.uncovered + 1,  // 根节点未覆盖，需要额外摄像头
            Math.min(info.coveredNoCamera, info.coveredHasCamera)
        );
    }
    
    /**
     * 测试方法
     * 验证二叉树最少摄像头覆盖算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 二叉树最少摄像头覆盖测试 ===");
        
        // 测试用例1: 简单二叉树
        //     0
        //    / \
        //   0   0
        //    \
        //     0
        Node root1 = new Node(0);
        root1.left = new Node(0);
        root1.right = new Node(0);
        root1.left.right = new Node(0);
        
        int result1 = min(root1);
        System.out.println("测试用例1 - 最少摄像头数: " + result1);
        System.out.println("预期结果: 1 (在根节点放置摄像头)");
        
        // 测试用例2: 单节点
        Node root2 = new Node(0);
        int result2 = min(root2);
        System.out.println("\n测试用例2 - 单节点最少摄像头数: " + result2);
        System.out.println("预期结果: 1 (必须在根节点放置摄像头)");
        
        // 测试用例3: 三节点链
        //   0
        //  /
        // 0
        //  \
        //   0
        Node root3 = new Node(0);
        root3.left = new Node(0);
        root3.left.right = new Node(0);
        
        int result3 = min(root3);
        System.out.println("\n测试用例3 - 三节点链最少摄像头数: " + result3);
        System.out.println("预期结果: 2");
        
        // 测试用例4: 空树
        int result4 = min(null);
        System.out.println("\n测试用例4 - 空树最少摄像头数: " + result4);
        System.out.println("预期结果: 0");
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(n) - 每个节点访问一次");
        System.out.println("空间复杂度: O(h) - 递归栈深度等于树高");
        System.out.println("核心思想: 树形DP + 状态转移");
        System.out.println("关键点: 贪心策略 - 尽量让叶子节点的父节点放摄像头");
    }
}
