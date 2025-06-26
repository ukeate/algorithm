package leetc.top;

/**
 * LeetCode 337. 打家劫舍 III (House Robber III)
 * 
 * 问题描述：
 * 小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为"根"。
 * 除了"根"之外，每栋房子有且只有一个"父"房子与之相连。
 * 一番侦察之后，聪明的小偷意识到"这个地方的所有房屋的排列类似于一棵二叉树"。
 * 如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
 * 
 * 计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
 * 
 * 示例：
 * 输入：root = [3,2,3,null,3,null,1]
 *         3
 *        / \
 *       2   3
 *        \   \ 
 *         3   1
 * 输出：7
 * 解释：小偷一晚能够盗取的最高金额 = 3 + 3 + 1 = 7
 * 
 * 输入：root = [3,4,5,1,3,null,1]
 *         3
 *        / \
 *       4   5
 *      / \   \ 
 *     1   3   1
 * 输出：9
 * 解释：小偷一晚能够盗取的最高金额 = 4 + 5 = 9
 * 
 * 解法思路：
 * 树形动态规划 + 状态设计：
 * 
 * 1. 状态定义：
 *    对于每个节点，维护两种状态：
 *    - rob：抢劫当前节点能获得的最大金额
 *    - notRob：不抢劫当前节点能获得的最大金额
 * 
 * 2. 状态转移：
 *    - 如果抢劫当前节点：
 *      rob = node.val + left.notRob + right.notRob
 *    - 如果不抢劫当前节点：
 *      notRob = max(left.rob, left.notRob) + max(right.rob, right.notRob)
 * 
 * 3. 递归策略：
 *    - 自底向上计算每个节点的最优解
 *    - 子问题的最优解构成父问题的最优解
 *    - 最终答案是根节点的max(rob, notRob)
 * 
 * 4. 约束条件：
 *    - 相邻节点不能同时被抢劫
 *    - 父节点和子节点之间存在约束关系
 * 
 * 核心思想：
 * - 树形DP：在树结构上进行动态规划
 * - 状态压缩：每个节点只需要两种状态
 * - 自底向上：从叶子节点开始向根节点传递最优解
 * 
 * 关键技巧：
 * - 二元状态：抢/不抢的二元选择
 * - 信息传递：子节点状态影响父节点决策
 * - 一次遍历：每个节点只访问一次
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈深度，h为树的高度
 * 
 * LeetCode链接：https://leetcode.com/problems/house-robber-iii/
 */
public class P337_HouseRobberIII {
    
    /**
     * 二叉树节点定义
     */
    public static class TreeNode {
        int val;                    // 节点值（房屋金额）
        TreeNode left;              // 左子节点
        TreeNode right;             // 右子节点
        
        TreeNode() {}
        
        TreeNode(int val) {
            this.val = val;
        }
        
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * 状态类：表示节点的两种状态
     */
    public static class State {
        public int rob;             // 抢劫当前节点的最大收益
        public int notRob;          // 不抢劫当前节点的最大收益
        
        public State(int rob, int notRob) {
            this.rob = rob;
            this.notRob = notRob;
        }
    }
    
    /**
     * 计算能够抢劫的最大金额
     * 
     * @param root 二叉树根节点
     * @return 最大抢劫金额
     */
    public int rob(TreeNode root) {
        State result = robHelper(root);
        // 返回抢劫和不抢劫根节点的最大值
        return Math.max(result.rob, result.notRob);
    }
    
    /**
     * 递归计算每个节点的最优状态
     * 
     * 算法逻辑：
     * 1. 递归计算左右子树的状态
     * 2. 计算当前节点抢劫的最大收益
     * 3. 计算当前节点不抢劫的最大收益
     * 4. 返回当前节点的状态
     * 
     * @param node 当前节点
     * @return 当前节点的状态（抢劫/不抢劫的最大收益）
     */
    private State robHelper(TreeNode node) {
        // 基础情况：空节点
        if (node == null) {
            return new State(0, 0);
        }
        
        // 递归计算左右子树的状态
        State left = robHelper(node.left);
        State right = robHelper(node.right);
        
        // 计算当前节点的两种状态
        
        // 1. 抢劫当前节点：获得当前节点的金额 + 左右子节点不抢劫的收益
        // （因为不能抢劫相邻节点，所以子节点必须不抢劫）
        int rob = node.val + left.notRob + right.notRob;
        
        // 2. 不抢劫当前节点：左右子节点可以选择抢劫或不抢劫的最大收益
        int notRob = Math.max(left.rob, left.notRob) + Math.max(right.rob, right.notRob);
        
        return new State(rob, notRob);
    }
}
