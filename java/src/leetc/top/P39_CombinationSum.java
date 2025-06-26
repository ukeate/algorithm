package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 39. 组合总和 (Combination Sum)
 * 
 * 问题描述：
 * 给你一个无重复元素的整数数组 candidates 和一个目标整数 target。
 * 找出 candidates 中可以使数字和为目标数 target 的所有不同组合。
 * 
 * candidates 中的同一个数字可以无限制重复被选取。如果至少一个数字的被选数量不同，则两个组合是不同的。
 * 
 * 对于给定的输入，保证和为 target 的不同组合数少于 150 个。
 * 
 * 示例：
 * 输入：candidates = [2,3,6,7], target = 7
 * 输出：[[2,2,3],[7]]
 * 解释：
 * 2 和 3 可以形成一组候选，2 + 2 + 3 = 7 。注意 2 可以使用多次。
 * 7 也是一个候选， 7 = 7 。
 * 仅有这两种组合。
 * 
 * 输入：candidates = [2,3,5], target = 8
 * 输出：[[2,2,2,2],[2,3,3],[3,5]]
 * 
 * 解法思路：
 * 动态规划 + 递归：
 * 1. 对于每个候选数字，都有多种选择：选0个、1个、2个...直到超过target
 * 2. 递归定义：process(idx, target) = 从candidates[0...idx]中选择数字，组成target的所有方案
 * 3. 状态转移：对于candidates[idx]，尝试选择0个、1个、2个...k个（k*candidates[idx] <= target）
 * 4. 递归边界：target=0时返回空组合；idx=-1且target>0时无解
 * 
 * 核心思想：
 * - 每个数字可以无限次使用，所以需要枚举使用次数
 * - 从右往左考虑每个候选数字，避免重复组合
 * - 使用递归自底向上构建所有可能的组合
 * 
 * 算法特点：
 * - 无重复：通过固定选择顺序避免重复组合
 * - 完整性：枚举所有可能的选择次数，保证不遗漏
 * - 无限制：同一数字可以选择多次
 * 
 * 时间复杂度：O(target^(target/min_candidate)) - 取决于目标值和最小候选数
 * 空间复杂度：O(target/min_candidate) - 递归栈深度
 * 
 * LeetCode链接：https://leetcode.com/problems/combination-sum/
 */
public class P39_CombinationSum {
    
    /**
     * 递归函数：从candidates[0...idx]中选择数字，使和为target的所有组合
     * 
     * 算法逻辑：
     * 1. 递归边界：
     *    - target = 0：找到一个有效组合，返回包含空列表的结果
     *    - idx = -1 且 target > 0：无更多候选数字，返回空结果
     * 2. 递归过程：
     *    - 枚举当前数字candidates[idx]的使用次数：0, 1, 2, ..., k
     *    - 其中k满足：k * candidates[idx] <= target
     *    - 对每种使用次数，递归求解剩余问题
     *    - 将当前数字添加到递归结果中，构成完整组合
     * 
     * 为什么从右往左处理：
     * - 避免重复组合：固定选择顺序，保证组合的唯一性
     * - 例如：[2,3]组成5，只考虑[2,3]而不考虑[3,2]
     * 
     * @param arr 候选数字数组
     * @param idx 当前考虑的数字索引
     * @param target 目标和
     * @return 所有可能的组合列表
     */
    private static List<List<Integer>> process1(int[] arr, int idx, int target) {
        List<List<Integer>> ans = new ArrayList<>();
        
        // 递归边界1：目标和为0，找到一个有效组合
        if (target == 0) {
            ans.add(new ArrayList<>());  // 添加空组合
            return ans;
        }
        
        // 递归边界2：没有更多候选数字且目标和大于0，无解
        if (idx == -1) {
            return ans;  // 返回空结果列表
        }
        
        // 枚举当前数字arr[idx]的使用次数
        for (int take = 0; take * arr[idx] <= target; take++) {
            // 递归求解：使用take个arr[idx]后，剩余目标的所有组合
            List<List<Integer>> preAns = process1(arr, idx - 1, target - (take * arr[idx]));
            
            // 将当前数字添加到每个递归结果中
            for (List<Integer> path : preAns) {
                // 添加take个arr[idx]到当前组合
                for (int i = 0; i < take; i++) {
                    path.add(arr[idx]);
                }
                ans.add(path);  // 将完整组合添加到结果中
            }
        }
        
        return ans;
    }

    /**
     * 主函数：找出所有和为target的不同组合
     * 
     * @param candidates 候选数字数组（无重复元素）
     * @param target 目标和
     * @return 所有可能的组合列表
     */
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        // 从最后一个候选数字开始，向前递归处理
        return process1(candidates, candidates.length - 1, target);
    }
}
