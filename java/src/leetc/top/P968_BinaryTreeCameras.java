package leetc.top;

/**
 * LeetCode 968. 监控二叉树 (Binary Tree Cameras)
 * 
 * 问题描述：
 * 给定一个二叉树，我们在树的节点上安装摄像头。
 * 节点上的每个摄影头都可以监视其父对象、自身及其直接子对象。
 * 计算监控树的所有节点所需的最少摄像头数量。
 * 
 * 示例：
 * 输入：[0,0,null,0,0]
 * 输出：1
 * 解释：如图所示，一台摄像头足以监控所有节点。
 * 
 * 输入：[0,0,null,0,null,0,null,null,0]
 * 输出：2
 * 解释：需要至少两个摄像头来监视树的所有节点。
 * 
 * 解法思路：
 * 树形动态规划 + 贪心策略：
 * 1. 对于每个节点，有三种状态：未被覆盖、被覆盖但无摄像头、被覆盖且有摄像头
 * 2. 使用后序遍历，从叶子节点向上计算最优方案
 * 3. 贪心策略：叶子节点不放摄像头，其父节点放摄像头更优
 * 
 * 状态定义：
 * - uncovered: 节点未被任何摄像头覆盖
 * - covered_no_camera: 节点被覆盖但自身没有摄像头
 * - covered_has_camera: 节点被覆盖且自身有摄像头
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈深度，h为树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/binary-tree-cameras/
 */
public class P968_BinaryTreeCameras {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        public int value;        // 节点值
        public TreeNode left;    // 左子节点
        public TreeNode right;   // 右子节点
    }

    /**
     * 封装节点的三种状态及其对应的最少摄像头数量
     */
    private static class Info {
        public long un;  // 节点未被覆盖时的最少摄像头数量
        public long no;  // 节点被覆盖但无摄像头时的最少摄像头数量
        public long has; // 节点被覆盖且有摄像头时的最少摄像头数量

        public Info(long uncovered, long coveredNoCamera, long coveredHasCamera) {
            un = uncovered;
            no = coveredNoCamera;
            has = coveredHasCamera;
        }
    }

    /**
     * 递归计算以节点x为根的子树在三种状态下的最少摄像头数量
     * 
     * 状态转移逻辑：
     * 1. uncovered: 当前节点未被覆盖，左右子树都不能有摄像头，只能被覆盖无摄像头
     * 2. covered_no_camera: 当前节点被覆盖但无摄像头，至少一个子树必须有摄像头
     * 3. covered_has_camera: 当前节点有摄像头，左右子树可以是任意状态
     * 
     * @param x 当前处理的节点
     * @return 包含三种状态信息的Info对象
     */
    private static Info process1(TreeNode x) {
        if (x == null) {
            // 空节点的边界条件：
            // - 未被覆盖：不可能状态，设为无穷大
            // - 被覆盖无摄像头：不需要任何摄像头，为0
            // - 被覆盖有摄像头：不可能状态，设为无穷大
            return new Info(Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
        }
        
        Info li = process1(x.left);  // 左子树信息
        Info ri = process1(x.right); // 右子树信息
        
        // 计算当前节点在三种状态下的最少摄像头数量
        
        // 1. 当前节点未被覆盖：左右子树都必须是"被覆盖无摄像头"状态
        long uncovered = li.no + ri.no;
        
        // 2. 当前节点被覆盖但无摄像头：至少一个子树必须有摄像头来覆盖当前节点
        long coveredNoCamera = Math.min(li.has + ri.has,          // 左右都有摄像头
                Math.min(li.has + ri.no, li.no + ri.has));        // 一边有摄像头，一边无摄像头
        
        // 3. 当前节点有摄像头：可以覆盖自己和子树，子树可以是任意状态
        long coveredHasCamera = Math.min(li.un, Math.min(li.no, li.has))
                + Math.min(ri.un, Math.min(ri.no, ri.has))
                + 1; // 当前节点的摄像头
        
        return new Info(uncovered, coveredNoCamera, coveredHasCamera);
    }

    /**
     * 方法1：直接DP解法
     * 
     * @param root 二叉树根节点
     * @return 最少摄像头数量
     */
    public int minCameraCover1(TreeNode root) {
        Info data = process1(root);
        // 根节点的最优状态：被覆盖无摄像头 或 被覆盖有摄像头
        // 如果根节点未被覆盖，需要额外加1个摄像头
        return (int) Math.min(data.un + 1, Math.min(data.no, data.has));
    }

    /**
     * 节点状态枚举：更清晰的状态表示
     */
    private enum Status {
        un,    // uncovered - 未被覆盖
        no,    // coveredNoCamera - 被覆盖但无摄像头
        has    // coveredHasCamera - 被覆盖且有摄像头
    }
    
    /**
     * 简化的信息类：状态 + 摄像头数量
     */
    private static class Info2 {
        public Status status; // 节点状态
        public int cameras;   // 摄像头数量
        
        public Info2(Status status, int cameras) {
            this.status = status;
            this.cameras = cameras;
        }
    }

    /**
     * 递归计算以节点x为根的子树的最优状态和摄像头数量
     * 
     * 贪心策略：
     * 1. 如果任一子树未被覆盖，当前节点必须放摄像头
     * 2. 如果任一子树有摄像头，当前节点被覆盖，无需摄像头
     * 3. 如果两个子树都被覆盖且无摄像头，当前节点未被覆盖
     * 
     * @param x 当前处理的节点
     * @return 包含状态和摄像头数量的Info2对象
     */
    private static Info2 process2(TreeNode x) {
        if (x == null) {
            // 空节点被视为"被覆盖无摄像头"状态
            return new Info2(Status.no, 0);
        }
        
        Info2 li = process2(x.left);  // 左子树信息
        Info2 ri = process2(x.right); // 右子树信息
        
        int cameras = li.cameras + ri.cameras; // 子树中的摄像头总数
        
        // 应用贪心策略确定当前节点状态
        if (li.status == Status.un || ri.status == Status.un) {
            // 有子树未被覆盖，当前节点必须放摄像头
            return new Info2(Status.has, cameras + 1);
        }
        
        if (li.status == Status.has || ri.status == Status.has) {
            // 有子树有摄像头，当前节点被其覆盖，无需摄像头
            return new Info2(Status.no, cameras);
        }
        
        // 两个子树都是"被覆盖无摄像头"状态，当前节点未被覆盖
        return new Info2(Status.un, cameras);
    }
    
    /**
     * 方法2：贪心策略解法（推荐）
     * 
     * @param root 二叉树根节点
     * @return 最少摄像头数量
     */
    public static int minCameraCover2(TreeNode root) {
        Info2 data = process2(root);
        // 如果根节点未被覆盖，需要额外放置一个摄像头
        return data.cameras + (data.status == Status.un ? 1 : 0);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：简单二叉树
        //     0
        //    / \
        //   0   0
        TreeNode root1 = new TreeNode();
        root1.left = new TreeNode();
        root1.right = new TreeNode();
        
        System.out.println("测试用例1:");
        System.out.println("树结构: 根节点有两个叶子节点");
        System.out.println("最少摄像头数量: " + minCameraCover2(root1));
        System.out.println("期望: 1 (根节点放摄像头)");
        System.out.println();
        
        // 测试用例2：链式结构
        //   0
        //  /
        // 0
        //  \
        //   0
        TreeNode root2 = new TreeNode();
        root2.left = new TreeNode();
        root2.left.right = new TreeNode();
        
        System.out.println("测试用例2:");
        System.out.println("树结构: 链式结构");
        System.out.println("最少摄像头数量: " + minCameraCover2(root2));
        System.out.println("期望: 1 (中间节点放摄像头)");
        System.out.println();
        
        // 测试用例3：单节点
        TreeNode root3 = new TreeNode();
        System.out.println("测试用例3:");
        System.out.println("树结构: 单节点");
        System.out.println("最少摄像头数量: " + minCameraCover2(root3));
        System.out.println("期望: 1 (根节点必须放摄像头)");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 树形DP + 贪心策略");
        System.out.println("- 时间复杂度: O(n) - 每个节点访问一次");
        System.out.println("- 空间复杂度: O(h) - 递归栈深度");
        System.out.println("- 贪心策略: 叶子节点不放摄像头，其父节点放更优");
        System.out.println("- 状态设计: 三种状态覆盖所有可能情况");
        System.out.println("- 最优子结构: 子树的最优解构成整体最优解");
    }
}
