package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 448. 找到所有数组中消失的数字 (Find All Numbers Disappeared in an Array)
 * 
 * 问题描述：
 * 给你一个含 n 个整数的数组 nums，其中 nums[i] 在区间 [1, n] 内。
 * 请你找出所有在 [1, n] 范围内但没有出现在 nums 中的数字，并以数组的形式返回结果。
 * 
 * 进阶：你能在不使用额外空间且时间复杂度为 O(n) 的情况下解决这个问题吗？
 * 你可以假定返回的数组不算在额外空间内。
 * 
 * 示例：
 * - 输入：nums = [4,3,2,7,8,2,3,1]
 * - 输出：[5,6]
 * 
 * 解法思路：
 * 循环不变式 + 原地哈希：
 * 1. 利用数组索引作为哈希表，将每个数字放到其应该在的位置
 * 2. 对于数字 x，应该放在索引 x-1 的位置
 * 3. 通过交换操作，尽可能让 nums[i] = i+1
 * 4. 最后遍历数组，如果 nums[i] != i+1，说明数字 i+1 消失了
 * 
 * 核心技巧：
 * - walk函数实现元素归位：将nums[i]放到正确位置
 * - 处理重复元素：如果目标位置已经正确，停止交换
 * - 原地修改：不使用额外的O(n)空间
 * 
 * 时间复杂度：O(n) - 每个元素最多被移动一次到正确位置
 * 空间复杂度：O(1) - 除了返回数组外，只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/
 */
public class P448_FindAllNumbersDisappearedInAnArray {
    
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
     * 将位置i的元素移动到其应该在的位置
     * 
     * 算法过程：
     * 1. 如果nums[i]已经在正确位置(nums[i] == i+1)，停止
     * 2. 否则，将nums[i]放到位置nums[i]-1上
     * 3. 如果目标位置已经有正确元素，停止（避免无限循环）
     * 4. 否则继续交换，直到nums[i]到达正确位置或遇到重复
     * 
     * @param nums 数组
     * @param i 当前处理的位置
     */
    private static void walk(int[] nums, int i) {
        while (nums[i] != i + 1) { // 当前位置元素不正确
            int nexti = nums[i] - 1; // nums[i]应该放在的位置
            
            // 如果目标位置已经有正确的元素，停止交换
            // 这处理了重复元素的情况，避免无限循环
            if (nums[nexti] == nexti + 1) {
                break;
            }
            
            // 将nums[i]与其目标位置的元素交换
            swap(nums, i, nexti);
        }
    }

    /**
     * 找到所有消失的数字
     * 
     * 算法步骤：
     * 1. 对每个位置执行walk操作，尽可能让元素归位
     * 2. 遍历数组，找出所有nums[i] != i+1的位置
     * 3. 这些位置对应的数字i+1就是消失的数字
     * 
     * @param nums 输入数组，包含[1,n]范围内的数字
     * @return 所有消失的数字列表
     */
    public static List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return ans;
        }
        
        int n = nums.length;
        
        // 第一阶段：让每个元素尽可能归位
        for (int i = 0; i < n; i++) {
            walk(nums, i);
        }
        
        // 第二阶段：找出所有不在正确位置的数字
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                ans.add(i + 1); // 数字i+1消失了
            }
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] nums1 = {4, 3, 2, 7, 8, 2, 3, 1};
        System.out.println("测试用例1:");
        System.out.println("输入: " + java.util.Arrays.toString(nums1));
        System.out.println("输出: " + findDisappearedNumbers(nums1));
        System.out.println("期望: [5, 6]");
        System.out.println();
        
        // 测试用例2：连续数组
        int[] nums2 = {1, 2, 3, 4, 5};
        System.out.println("测试用例2 (无消失数字):");
        System.out.println("输入: " + java.util.Arrays.toString(nums2));
        System.out.println("输出: " + findDisappearedNumbers(nums2));
        System.out.println("期望: []");
        System.out.println();
        
        // 测试用例3：全重复
        int[] nums3 = {1, 1, 1, 1};
        System.out.println("测试用例3 (重复元素):");
        System.out.println("输入: " + java.util.Arrays.toString(nums3));
        System.out.println("输出: " + findDisappearedNumbers(nums3));
        System.out.println("期望: [2, 3, 4]");
        System.out.println();
        
        // 测试用例4：单元素
        int[] nums4 = {2};
        System.out.println("测试用例4 (单元素):");
        System.out.println("输入: " + java.util.Arrays.toString(nums4));
        System.out.println("输出: " + findDisappearedNumbers(nums4));
        System.out.println("期望: [1]");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 时间复杂度: O(n) - 每个元素最多被移动一次");
        System.out.println("- 空间复杂度: O(1) - 原地修改，不使用额外空间");
        System.out.println("- 核心思想: 利用数组索引作为哈希表");
        System.out.println("- 重复处理: 通过循环不变式优雅处理重复元素");
    }
}
