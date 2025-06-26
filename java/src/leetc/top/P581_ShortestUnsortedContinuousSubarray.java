package leetc.top;

/**
 * LeetCode 581. 最短无序连续子数组 (Shortest Unsorted Continuous Subarray)
 * 
 * 问题描述：
 * 给你一个整数数组 nums，你需要找出一个连续子数组，如果对这个子数组进行升序排序，
 * 那么整个数组都会变为升序排序。
 * 请你找出符合题意的最短子数组，并输出它的长度。
 * 
 * 示例：
 * - 输入：nums = [2,6,4,8,10,9,15]
 * - 输出：5
 * - 解释：你只需要对 [6, 4, 8, 10, 9] 进行升序排序，那么整个表都会变为升序排序。
 * 
 * - 输入：nums = [1,2,3,4]
 * - 输出：0
 * - 解释：数组已经是升序的，不需要排序任何子数组。
 * 
 * 解法思路：
 * 单调性检测：
 * 1. 从左到右遍历，找到第一个破坏递增性的位置（右边界）
 * 2. 从右到左遍历，找到第一个破坏递减性的位置（左边界）
 * 3. 这两个边界之间就是需要排序的最短子数组
 * 
 * 核心思想：
 * - 正向扫描：维护最大值，遇到比最大值小的元素时更新右边界
 * - 反向扫描：维护最小值，遇到比最小值大的元素时更新左边界
 * - 边界确定：左右边界确定后，计算子数组长度
 * 
 * 关键洞察：
 * - 需要排序的子数组右边界：最后一个不满足单调递增的位置
 * - 需要排序的子数组左边界：最后一个不满足单调递减的位置
 * 
 * 时间复杂度：O(n) - 两次线性扫描
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/shortest-unsorted-continuous-subarray/
 */
public class P581_ShortestUnsortedContinuousSubarray {
    
    /**
     * 找到最短无序连续子数组的长度
     * 
     * 算法步骤：
     * 1. 从左到右扫描，维护最大值，找到需要排序的右边界
     * 2. 从右到左扫描，维护最小值，找到需要排序的左边界
     * 3. 计算左右边界之间的距离，即为所需排序的子数组长度
     * 
     * 核心逻辑：
     * - 右边界：如果当前元素小于历史最大值，说明它位置不对，更新右边界
     * - 左边界：如果当前元素大于历史最小值，说明它位置不对，更新左边界
     * 
     * @param nums 输入数组
     * @return 需要排序的最短子数组长度
     */
    public static int findUnsortedSubarray(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        int n = nums.length;
        int right = -1; // 需要排序的右边界，初始化为-1表示还未找到
        int max = Integer.MIN_VALUE; // 从左到右扫描过程中的最大值
        
        // 第一次扫描：从左到右，找到需要排序的右边界
        for (int i = 0; i < n; i++) {
            if (max > nums[i]) {
                // 当前元素小于历史最大值，说明它的位置不对
                // 需要被包含在排序范围内，更新右边界
                right = i;
            }
            // 更新历史最大值
            max = Math.max(max, nums[i]);
        }
        
        int min = Integer.MAX_VALUE; // 从右到左扫描过程中的最小值
        int left = n; // 需要排序的左边界，初始化为n表示还未找到
        
        // 第二次扫描：从右到左，找到需要排序的左边界
        for (int i = n - 1; i >= 0; i--) {
            if (min < nums[i]) {
                // 当前元素大于历史最小值，说明它的位置不对
                // 需要被包含在排序范围内，更新左边界
                left = i;
            }
            // 更新历史最小值
            min = Math.min(min, nums[i]);
        }
        
        // 计算需要排序的子数组长度
        // 如果right == -1，说明数组已经有序，返回0
        // 否则返回右边界-左边界+1
        return Math.max(0, right - left + 1);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] nums1 = {2, 6, 4, 8, 10, 9, 15};
        System.out.println("测试用例1:");
        System.out.println("输入: " + java.util.Arrays.toString(nums1));
        System.out.println("输出: " + findUnsortedSubarray(nums1));
        System.out.println("期望: 5");
        System.out.println("需要排序的子数组: [6, 4, 8, 10, 9] (索引1-5)");
        System.out.println();
        
        // 测试用例2：已排序数组
        int[] nums2 = {1, 2, 3, 4};
        System.out.println("测试用例2 (已排序):");
        System.out.println("输入: " + java.util.Arrays.toString(nums2));
        System.out.println("输出: " + findUnsortedSubarray(nums2));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例3：完全逆序
        int[] nums3 = {5, 4, 3, 2, 1};
        System.out.println("测试用例3 (完全逆序):");
        System.out.println("输入: " + java.util.Arrays.toString(nums3));
        System.out.println("输出: " + findUnsortedSubarray(nums3));
        System.out.println("期望: 5");
        System.out.println("需要排序整个数组");
        System.out.println();
        
        // 测试用例4：只有两个元素需要交换
        int[] nums4 = {1, 3, 2, 4, 5};
        System.out.println("测试用例4 (局部无序):");
        System.out.println("输入: " + java.util.Arrays.toString(nums4));
        System.out.println("输出: " + findUnsortedSubarray(nums4));
        System.out.println("期望: 2");
        System.out.println("需要排序的子数组: [3, 2] (索引1-2)");
        System.out.println();
        
        // 测试用例5：单个元素
        int[] nums5 = {1};
        System.out.println("测试用例5 (单元素):");
        System.out.println("输入: " + java.util.Arrays.toString(nums5));
        System.out.println("输出: " + findUnsortedSubarray(nums5));
        System.out.println("期望: 0");
        System.out.println();
        
        // 测试用例6：重复元素
        int[] nums6 = {1, 2, 3, 3, 3};
        System.out.println("测试用例6 (重复元素):");
        System.out.println("输入: " + java.util.Arrays.toString(nums6));
        System.out.println("输出: " + findUnsortedSubarray(nums6));
        System.out.println("期望: 0");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 双向扫描检测单调性破坏点");
        System.out.println("- 时间复杂度: O(n) - 两次线性扫描");
        System.out.println("- 空间复杂度: O(1) - 只使用常数额外空间");
        System.out.println("- 关键技巧: 正向找右边界，反向找左边界");
        System.out.println("- 边界含义: 包含所有需要重新排序的元素");
    }
}
