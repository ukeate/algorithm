package base.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 二叉树路径和问题
 * 
 * 问题描述：
 * 给定一个二叉树和一个目标和，找到所有从根节点到叶子节点的路径，
 * 使得路径上所有节点值的和等于目标和。
 * 
 * 核心思想：
 * 使用深度优先搜索（DFS）+ 回溯算法，遍历所有从根到叶的路径，
 * 记录满足条件的完整路径。
 * 
 * 算法流程：
 * 1. 从根节点开始深度优先遍历
 * 2. 维护当前路径和剩余目标值
 * 3. 到达叶子节点时检查是否满足条件
 * 4. 使用回溯恢复路径状态，探索其他分支
 * 
 * 关键技巧：
 * - 路径记录：使用List动态维护当前路径
 * - 剩余目标：用减法避免累加误差
 * - 回溯处理：及时清理路径状态
 * - 深拷贝：保存路径快照避免引用污染
 * 
 * 应用场景：
 * - 文件系统路径查找
 * - 决策树路径分析
 * - 游戏寻路算法
 * - 数据挖掘路径发现
 * 
 * @author algorithm-base
 * @version 1.0
 */
public class HasPathSum {

    /**
     * 二叉树节点定义
     */
    private static class Node {
        public int val;        // 节点值
        public Node left;      // 左子节点
        public Node right;     // 右子节点

        public Node(int val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return "Node{" + val + "}";
        }
    }

    /**
     * 深拷贝路径列表，避免引用污染
     * 
     * 为什么需要深拷贝：
     * 由于Java中List是引用类型，如果直接将path添加到结果集中，
     * 后续对path的修改会影响已保存的结果。因此需要创建副本。
     * 
     * @param path 原始路径
     * @return 路径的深拷贝
     */
    private static List<Integer> copy(List<Integer> path) {
        List<Integer> ans = new ArrayList<>();
        for (Integer num : path) {
            ans.add(num);
        }
        return ans;
    }

    /**
     * 递归处理路径和问题的核心方法
     * 
     * 算法详解：
     * 1. 将当前节点加入路径
     * 2. 检查是否为叶子节点且满足目标和
     * 3. 如果是叶子节点且满足条件，保存当前路径
     * 4. 如果不是叶子节点，递归处理左右子树
     * 5. 回溯：移除当前节点，恢复路径状态
     * 
     * 递归参数说明：
     * @param x 当前处理的节点
     * @param path 当前路径（从根到当前节点的路径）
     * @param rest 剩余需要的目标和（目标和减去已走过路径的和）
     * @param ans 结果集，存储所有满足条件的路径
     * 
     * 示例过程：
     * 对于树：    5
     *           / \
     *          4   8
     *         /   / \
     *        11  13  4
     *       / \      \
     *      7   2      1
     * 目标和：22
     * 
     * 路径[5,4,11,2]：5+4+11+2=22 ✓
     * 路径[5,8,4,1]：5+8+4+1=18 ✗
     * 
     * @return 更新后的结果集
     */
    private static List<List<Integer>> pProcess(Node x, List<Integer> path, int rest, List<List<Integer>> ans) {
        // 检查是否为叶子节点（无左右子树）
        if (x.left == null && x.right == null) {
            // 叶子节点：检查当前节点值是否等于剩余目标和
            if (x.val == rest) {
                path.add(x.val);           // 将叶子节点加入路径
                ans.add(copy(path));       // 深拷贝保存满足条件的路径
                path.remove(path.size() - 1); // 回溯：移除叶子节点
            }
            return ans;
        }
        
        // 非叶子节点：将当前节点加入路径，继续递归
        path.add(x.val);
        
        // 递归处理左子树（如果存在）
        if (x.left != null) {
            pProcess(x.left, path, rest - x.val, ans);
        }
        
        // 递归处理右子树（如果存在）
        if (x.right != null) {
            pProcess(x.right, path, rest - x.val, ans);
        }
        
        // 回溯：移除当前节点，恢复到进入此节点前的状态
        path.remove(path.size() - 1);
        
        return ans;
    }

    /**
     * 查找路径和的主方法
     * 
     * 方法职责：
     * 1. 处理边界情况（空树）
     * 2. 初始化数据结构（路径列表、结果集）
     * 3. 调用递归处理方法
     * 4. 返回最终结果
     * 
     * @param root 二叉树根节点
     * @param sum 目标路径和
     * @return 所有满足条件的根到叶路径列表
     * 
     * 时间复杂度：O(N * L)，N为节点数，L为平均路径长度
     * 空间复杂度：O(N * L)，存储所有路径 + 递归栈空间
     */
    public static List<List<Integer>> hasPathSum(Node root, int sum) {
        List<List<Integer>> ans = new ArrayList<>();
        
        // 边界条件：空树直接返回空结果
        if (root == null) {
            return ans;
        }
        
        // 初始化路径列表
        List<Integer> path = new ArrayList<>();
        
        // 开始递归搜索
        return pProcess(root, path, sum, ans);
    }

    /**
     * 简化版本：仅判断是否存在路径和（不需要具体路径）
     * 
     * 适用场景：
     * 当只需要判断存在性而不需要具体路径时，可以使用更简洁的实现。
     * 
     * @param root 树根节点
     * @param sum 目标和
     * @return 是否存在满足条件的路径
     */
    public static boolean hasPathSumSimple(Node root, int sum) {
        if (root == null) {
            return false;
        }
        
        // 叶子节点：直接判断值是否等于目标和
        if (root.left == null && root.right == null) {
            return root.val == sum;
        }
        
        // 非叶子节点：递归检查左右子树
        return hasPathSumSimple(root.left, sum - root.val) || 
               hasPathSumSimple(root.right, sum - root.val);
    }

    /**
     * 构建测试用的二叉树
     * 树结构：      5
     *             / \
     *            4   8
     *           /   / \
     *          11  13  4
     *         / \    / \
     *        7   2  5   1
     * 
     * 可能的路径和：
     * - 5+4+11+7 = 27
     * - 5+4+11+2 = 22
     * - 5+8+13 = 26
     * - 5+8+4+5 = 22
     * - 5+8+4+1 = 18
     */
    private static Node buildTestTree() {
        Node root = new Node(5);
        root.left = new Node(4);
        root.right = new Node(8);
        
        root.left.left = new Node(11);
        root.left.left.left = new Node(7);
        root.left.left.right = new Node(2);
        
        root.right.left = new Node(13);
        root.right.right = new Node(4);
        root.right.right.left = new Node(5);
        root.right.right.right = new Node(1);
        
        return root;
    }

    /**
     * 打印所有路径（用于调试和验证）
     */
    private static void printPaths(List<List<Integer>> paths, int targetSum) {
        System.out.println("目标和为 " + targetSum + " 的所有路径：");
        if (paths.isEmpty()) {
            System.out.println("未找到满足条件的路径");
            return;
        }
        
        for (int i = 0; i < paths.size(); i++) {
            List<Integer> path = paths.get(i);
            int sum = path.stream().mapToInt(Integer::intValue).sum();
            System.out.printf("路径%d: %s (和=%d)\n", i + 1, path, sum);
        }
    }

    /**
     * 测试路径和算法
     * 
     * 测试用例设计：
     * 1. 正常情况：存在多条满足条件的路径
     * 2. 边界情况：空树、单节点树
     * 3. 特殊情况：无满足条件的路径
     * 4. 验证所有找到的路径确实满足条件
     */
    public static void main(String[] args) {
        System.out.println("二叉树路径和问题测试");
        System.out.println("====================");
        
        // 构建测试树
        Node testTree = buildTestTree();
        
        System.out.println("测试树结构：");
        System.out.println("      5");
        System.out.println("     / \\");
        System.out.println("    4   8");
        System.out.println("   /   / \\");
        System.out.println("  11  13  4");
        System.out.println(" / \\    / \\");
        System.out.println("7   2  5   1");
        System.out.println();
        
        // 测试用例1：目标和为22
        System.out.println("测试用例1：目标和为 22");
        List<List<Integer>> paths22 = hasPathSum(testTree, 22);
        printPaths(paths22, 22);
        boolean exists22 = hasPathSumSimple(testTree, 22);
        System.out.println("快速判断结果：" + (exists22 ? "存在" : "不存在"));
        System.out.println();
        
        // 测试用例2：目标和为26
        System.out.println("测试用例2：目标和为 26");
        List<List<Integer>> paths26 = hasPathSum(testTree, 26);
        printPaths(paths26, 26);
        boolean exists26 = hasPathSumSimple(testTree, 26);
        System.out.println("快速判断结果：" + (exists26 ? "存在" : "不存在"));
        System.out.println();
        
        // 测试用例3：目标和为100（不存在）
        System.out.println("测试用例3：目标和为 100");
        List<List<Integer>> paths100 = hasPathSum(testTree, 100);
        printPaths(paths100, 100);
        boolean exists100 = hasPathSumSimple(testTree, 100);
        System.out.println("快速判断结果：" + (exists100 ? "存在" : "不存在"));
        System.out.println();
        
        // 测试用例4：空树
        System.out.println("测试用例4：空树");
        List<List<Integer>> pathsNull = hasPathSum(null, 10);
        System.out.println("空树结果：" + (pathsNull.isEmpty() ? "无路径（正确）" : "异常"));
        System.out.println();
        
        // 测试用例5：单节点树
        System.out.println("测试用例5：单节点树");
        Node singleNode = new Node(5);
        List<List<Integer>> pathsSingle = hasPathSum(singleNode, 5);
        printPaths(pathsSingle, 5);
        System.out.println();
        
        // 算法特点总结
        System.out.println("算法特点总结：");
        System.out.println("================");
        System.out.println("完整路径版本：");
        System.out.println("• 优点：获得所有满足条件的具体路径");
        System.out.println("• 缺点：空间复杂度较高");
        System.out.println("• 时间复杂度：O(N * L)");
        System.out.println("• 空间复杂度：O(N * L)");
        System.out.println();
        System.out.println("简化判断版本：");
        System.out.println("• 优点：空间效率高，执行速度快");
        System.out.println("• 缺点：只能判断存在性");
        System.out.println("• 时间复杂度：O(N)");
        System.out.println("• 空间复杂度：O(H)，H为树高");
        System.out.println();
        System.out.println("应用建议：");
        System.out.println("• 需要具体路径：使用完整版本");
        System.out.println("• 仅需存在性判断：使用简化版本");
        System.out.println("• 大数据量：优先考虑简化版本");
    }
}
