package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 437. 路径总和 III (Path Sum III)
 * 
 * 问题描述：
 * 给定一个二叉树的根节点 root，和一个整数 targetSum，求该二叉树里节点值之和等于 targetSum 的路径的数目。
 * 路径不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
 * 
 * 示例：
 * 输入：root = [10,5,-3,3,2,null,11,3,-2,null,1], targetSum = 8
 * 输出：3
 * 解释：和等于 8 的路径有 3 条
 * 
 * 解法思路：
 * 前缀和 + 哈希表：
 * 1. 使用前缀和的思想：如果从根到某节点的路径和为 preSum，
 *    从根到另一节点的路径和为 currentSum，且 currentSum - preSum = targetSum，
 *    则从第一个节点到第二个节点的路径和为 targetSum
 * 2. 用HashMap记录从根到当前路径上每个前缀和出现的次数
 * 3. 对于当前节点，查找 (currentSum - targetSum) 在map中的出现次数
 * 4. 递归处理左右子树，注意回溯时要恢复map状态
 * 
 * 核心思想：
 * - 路径和问题转化为前缀和差值问题
 * - 使用HashMap快速查找所需的前缀和
 * - 回溯保证不同路径之间不互相影响
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(n) - 递归栈和HashMap空间
 * 
 * LeetCode链接：https://leetcode.com/problems/path-sum-iii/
 */
public class P437_PathSumIII {
    /**
     * 二叉树节点定义
     */
    public class TreeNode {
        public int val;         // 节点值
        public TreeNode left;   // 左子节点
        public TreeNode right;  // 右子节点
    }

    /**
     * 递归计算以当前节点为路径一部分的目标和路径数量
     * 
     * @param x 当前节点
     * @param sum 目标和
     * @param pre 从根节点到当前节点父节点的路径和
     * @param preSumMap 前缀和频次映射表
     * @return 包含当前节点的目标和路径数量
     */
    private static int process(TreeNode x, int sum, long pre, HashMap<Long, Integer> preSumMap) {
        if (x == null) {
            return 0;
        }
        
        // 计算从根节点到当前节点的路径和
        long all = pre + x.val;
        int ans = 0;
        
        // 查找是否存在前缀和为 (all - sum) 的路径
        // 如果存在，说明从那个位置到当前节点的路径和为sum
        if (preSumMap.containsKey(all - sum)) {
            ans = preSumMap.get(all - sum);
        }
        
        // 将当前路径和加入映射表
        if (!preSumMap.containsKey(all)) {
            preSumMap.put(all, 1);
        } else {
            preSumMap.put(all, preSumMap.get(all) + 1);
        }
        
        // 递归处理左右子树
        ans += process(x.left, sum, all, preSumMap);
        ans += process(x.right, sum, all, preSumMap);
        
        // 回溯：移除当前路径和，恢复map状态
        if (preSumMap.get(all) == 1) {
            preSumMap.remove(all);
        } else {
            preSumMap.put(all, preSumMap.get(all) - 1);
        }
        
        return ans;
    }

    /**
     * 计算二叉树中路径和等于目标值的路径数量
     * 
     * @param root 二叉树根节点
     * @param sum 目标路径和
     * @return 满足条件的路径数量
     */
    public static int pathSum(TreeNode root, int sum) {
        HashMap<Long, Integer> preSumMap = new HashMap<>();
        // 初始化：前缀和为0的路径有1条（空路径）
        preSumMap.put(0L, 1);
        return process(root, sum, 0, preSumMap);
    }
}
