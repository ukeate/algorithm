package leetc.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 15. 三数之和 (3Sum)
 * 
 * 问题描述：
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 
 * 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。
 * 请你返回所有和为 0 且不重复的三元组。
 * 
 * 解法思路：
 * 排序 + 双指针：
 * 1. 首先对数组排序，便于去重和使用双指针
 * 2. 固定第一个数，将问题转化为两数之和
 * 3. 对于剩余数组，使用双指针寻找目标和
 * 4. 去重处理：跳过相同的元素避免重复解
 * 
 * 算法步骤：
 * 1. 排序数组
 * 2. 外层循环固定第一个数 nums[i]
 * 3. 内层使用双指针寻找 nums[l] + nums[r] = -nums[i]
 * 4. 根据和的大小移动指针
 * 5. 找到解后进行去重处理
 * 
 * 去重策略：
 * - 第一个数去重：i == 0 || nums[i-1] != nums[i]
 * - 两数之和去重：l == begin || nums[l-1] != nums[l]
 * 
 * 优化要点：
 * - 排序后使用双指针，避免三层循环
 * - 提前去重，减少不必要的计算
 * - 复用twoSum逻辑，代码结构清晰
 * 
 * 时间复杂度：O(n²) - 外层循环O(n)，内层双指针O(n)
 * 空间复杂度：O(1) - 不计算返回结果的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/3sum/
 */
public class P15_3Sum {
    
    /**
     * 在排序数组中寻找两数之和等于目标值的所有不重复组合
     * 
     * @param nums 已排序的数组
     * @param begin 搜索起始位置
     * @param target 目标和
     * @return 所有满足条件的两数组合
     */
    private static List<List<Integer>> twoSum(int[] nums, int begin, int target) {
        int l = begin, r = nums.length - 1;
        List<List<Integer>> ans = new ArrayList<>();
        
        while (l < r) {
            if (nums[l] + nums[r] > target) {
                // 和太大，右指针左移
                r--;
            } else if (nums[l] + nums[r] < target) {
                // 和太小，左指针右移
                l++;
            } else {
                // 找到目标和
                if (l == begin || nums[l - 1] != nums[l]) {
                    // 去重：只在左指针指向新值时添加结果
                    List<Integer> cur = new ArrayList<>();
                    cur.add(nums[l]);
                    cur.add(nums[r]);
                    ans.add(cur);
                }
                l++;  // 继续寻找其他可能的组合
            }
        }
        
        return ans;
    }

    /**
     * 寻找所有和为0的三数组合
     * 
     * @param nums 整数数组
     * @return 所有不重复的三数组合
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);  // 排序，便于去重和双指针操作
        List<List<Integer>> ans = new ArrayList<>();
        
        // 固定第一个数，枚举所有可能
        for (int i = 0; i < nums.length - 2; i++) {
            if (i == 0 || nums[i - 1] != nums[i]) {
                // 去重：跳过重复的第一个数
                
                // 在剩余数组中寻找两数之和等于 -nums[i]
                List<List<Integer>> nexts = twoSum(nums, i + 1, -nums[i]);
                
                // 将第一个数添加到每个解的开头
                for (List<Integer> cur : nexts) {
                    cur.add(0, nums[i]);  // 在列表开头插入第一个数
                    ans.add(cur);
                }
            }
        }
        
        return ans;
    }
}
