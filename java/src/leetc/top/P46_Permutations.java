package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 46. 全排列 (Permutations)
 * 
 * 问题描述：
 * 给定一个不含重复数字的数组nums，返回其所有可能的全排列。你可以按任意顺序返回答案。
 * 
 * 示例：
 * - 输入：nums = [1,2,3]
 * - 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 解法思路：
 * 回溯算法（交换法）：
 * 1. 对于每个位置idx，尝试将数组中idx及之后的每个元素放在该位置
 * 2. 通过交换操作将选中的元素移动到idx位置
 * 3. 递归处理下一个位置idx+1
 * 4. 回溯：恢复交换操作，尝试下一种可能
 * 5. 当idx == nums.length时，找到一个完整排列
 * 
 * 核心思想：
 * - 分治思想：确定第一个位置的元素后，剩下的问题是子数组的全排列
 * - 原地操作：通过交换避免创建额外数组，空间效率高
 * - 回溯模板：选择 → 递归 → 撤销选择
 * 
 * 时间复杂度：O(n! × n) - n!个排列，每个排列需要O(n)时间复制
 * 空间复杂度：O(n) - 递归深度为n（不计算结果存储空间）
 */
public class P46_Permutations {
    
    /**
     * 交换数组中两个位置的元素
     * 
     * @param nums 数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    /**
     * 回溯生成全排列的核心递归函数
     * 
     * 算法步骤：
     * 1. 边界条件：idx == nums.length时，当前数组就是一个完整排列
     * 2. 对于位置idx，尝试将nums[idx...n-1]中的每个元素放在idx位置
     * 3. 交换nums[idx]和nums[j]，确定idx位置的元素
     * 4. 递归处理下一个位置：process(nums, idx+1, ans)
     * 5. 回溯：交换回来，尝试下一个元素
     * 
     * @param nums 数组（在递归过程中会被修改）
     * @param idx 当前要确定的位置
     * @param ans 存储所有排列的结果列表
     */
    private static void process(int[] nums, int idx, List<List<Integer>> ans) {
        // 边界条件：所有位置都已确定，找到一个完整排列
        if (idx == nums.length) {
            ArrayList<Integer> cur = new ArrayList<>();
            for (int num : nums) {
                cur.add(num);  // 复制当前排列到结果中
            }
            ans.add(cur);
            return;
        }
        
        // 对于当前位置idx，尝试放入nums[idx...n-1]中的每个元素
        for (int j = idx; j < nums.length; j++) {
            // 选择：将nums[j]放在idx位置
            swap(nums, idx, j);
            
            // 递归：处理下一个位置
            process(nums, idx + 1, ans);
            
            // 回溯：撤销选择，恢复原状态
            swap(nums, idx, j);
        }
    }

    /**
     * 生成数组的所有排列
     * 
     * @param nums 不含重复数字的数组
     * @return 所有可能的排列
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        process(nums, 0, ans);
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P46_Permutations solution = new P46_Permutations();
        
        // 测试用例1：[1,2,3]
        int[] nums1 = {1, 2, 3};
        List<List<Integer>> result1 = solution.permute(nums1);
        System.out.println("输入: [1,2,3]");
        System.out.println("输出: " + result1);
        System.out.println("排列数: " + result1.size());
        System.out.println();
        
        // 测试用例2：[0,1]
        int[] nums2 = {0, 1};
        List<List<Integer>> result2 = solution.permute(nums2);
        System.out.println("输入: [0,1]");
        System.out.println("输出: " + result2);
        System.out.println("排列数: " + result2.size());
        System.out.println();
        
        // 测试用例3：[1]
        int[] nums3 = {1};
        List<List<Integer>> result3 = solution.permute(nums3);
        System.out.println("输入: [1]");
        System.out.println("输出: " + result3);
        System.out.println("排列数: " + result3.size());
    }
}
