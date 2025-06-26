package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 163. 缺失的区间 (Missing Ranges)
 * 
 * 问题描述：
 * 给你一个排序的整数数组 nums ，其中元素的范围在 [lower, upper] 当中，
 * 返回不包含在数组中的缺失区间。
 * 
 * 输出格式：
 * - 如果缺失区间只有一个数字，返回该数字的字符串形式
 * - 如果缺失区间有多个连续数字，返回 "start->end" 的格式
 * 
 * 示例：
 * 输入：nums = [0,1,3,50,75], lower = 0, upper = 99
 * 输出：["2","4->49","51->74","76->99"]
 * 
 * 解法思路：
 * 一次遍历 + 区间检测：
 * 1. 维护一个当前期望的下一个数字（初始为lower）
 * 2. 遍历数组，对每个数字检查是否存在gap
 * 3. 如果当前数字大于期望值，说明存在缺失区间
 * 4. 添加缺失区间到结果中，更新期望值
 * 5. 处理数组结束后剩余的缺失区间
 * 
 * 关键处理：
 * - 数字等于upper时提前返回（后续无需检查）
 * - 更新期望值为当前数字+1
 * - 最后检查是否还有尾部缺失区间
 * 
 * 边界情况：
 * - 数组为空：整个[lower, upper]都缺失
 * - 数组完全覆盖范围：无缺失
 * - 数组部分覆盖：存在头部、中间或尾部缺失
 * 
 * 时间复杂度：O(n) - 遍历数组一次
 * 空间复杂度：O(1) - 不计算返回结果的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/missing-ranges/
 */
public class P163_MissingRanges {
    
    /**
     * 格式化缺失区间的字符串表示
     * 
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 区间的字符串表示
     */
    private static String miss(int lower, int upper) {
        String left = String.valueOf(lower);
        String right = "";
        
        if (upper > lower) {
            // 区间包含多个数字，使用 "start->end" 格式
            right = "->" + upper;
        }
        // 区间只有一个数字，直接返回该数字
        
        return left + right;
    }
    
    /**
     * 找出缺失的区间
     * 
     * @param nums 排序的整数数组
     * @param lower 范围下界
     * @param upper 范围上界
     * @return 缺失区间的字符串列表
     */
    public static List<String> findMissingRanges(int[] nums, int lower, int upper) {
        List<String> ans = new ArrayList<>();
        
        // 遍历数组中的每个数字
        for (int num : nums) {
            if (num > lower) {
                // 存在缺失区间：[lower, num-1]
                ans.add(miss(lower, num - 1));
            }
            
            if (num == upper) {
                // 达到上界，后续无需检查
                return ans;
            }
            
            // 更新期望的下一个数字
            lower = num + 1;
        }
        
        // 处理数组结束后的尾部缺失区间
        if (lower <= upper) {
            ans.add(miss(lower, upper));
        }
        
        return ans;
    }
}
