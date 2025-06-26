package leetc.top;

/**
 * LeetCode 34. 在排序数组中查找元素的第一个和最后一个位置 
 * (Find First and Last Position of Element in Sorted Array)
 * 
 * 问题描述：
 * 给你一个按照非递减顺序排列的整数数组 nums，和一个目标值 target。
 * 请你找出给定目标值在数组中的开始位置和结束位置。
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 * 
 * 你必须设计并实现时间复杂度为 O(log n) 的算法解决此问题。
 * 
 * 示例：
 * 输入：nums = [5,7,7,8,8,10], target = 8
 * 输出：[3,4]
 * 
 * 输入：nums = [5,7,7,8,8,10], target = 6
 * 输出：[-1,-1]
 * 
 * 解法思路：
 * 二分查找变形：
 * 1. 问题转化为两个子问题：
 *    - 找到target第一次出现的位置（最左边界）
 *    - 找到target最后一次出现的位置（最右边界）
 * 2. 使用两次二分查找分别解决这两个问题
 * 3. 第一次二分：找左边界（当找到target时，继续在左半部分查找）
 * 4. 第二次二分：找右边界（当找到target时，继续在右半部分查找）
 * 
 * 核心思想：
 * - 利用数组已排序的性质，使用二分查找达到O(log n)时间复杂度
 * - 通过调整查找策略来寻找边界位置
 * - 左边界：找到target后继续向左查找，寻找更小的索引
 * - 右边界：找到target后继续向右查找，寻找更大的索引
 * 
 * 时间复杂度：O(log n) - 两次二分查找
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/
 */
public class P34_FindFirstAndLastPositionOfElementInSortedArray {
    
    /**
     * 查找目标值第一次出现的位置（左边界）
     * 
     * 算法思路：
     * 1. 使用标准二分查找框架
     * 2. 关键差异：当arr[mid] == num时，记录当前位置，但继续在左半部分查找
     * 3. 这样可以找到最左边的匹配位置
     * 
     * @param arr 有序数组
     * @param num 目标值
     * @return 第一次出现的位置，不存在返回-1
     */
    private static int first(int[] arr, int num) {
        int l = 0;                // 左边界
        int r = arr.length - 1;   // 右边界
        int mid = 0;              // 中点
        int ans = -1;             // 记录答案
        
        while (l <= r) {
            mid = l + ((r - l) >> 1);  // 防止溢出的中点计算
            
            if (arr[mid] < num) {
                // 中点值小于目标值，目标在右半部分
                l = mid + 1;
            } else if (arr[mid] > num) {
                // 中点值大于目标值，目标在左半部分
                r = mid - 1;
            } else {
                // 找到目标值，记录位置，但继续在左半部分查找更小的索引
                ans = mid;
                r = mid - 1;  // 关键：继续在左半部分查找
            }
        }
        
        return ans;
    }

    /**
     * 查找目标值最后一次出现的位置（右边界）
     * 
     * 算法思路：
     * 1. 使用标准二分查找框架
     * 2. 关键差异：当arr[mid] == num时，记录当前位置，但继续在右半部分查找
     * 3. 这样可以找到最右边的匹配位置
     * 
     * @param arr 有序数组
     * @param num 目标值
     * @return 最后一次出现的位置，不存在返回-1
     */
    private static int last(int[] arr, int num) {
        int l = 0;                // 左边界
        int r = arr.length - 1;   // 右边界
        int mid = 0;              // 中点
        int ans = -1;             // 记录答案
        
        while (l <= r) {
            mid = l + ((r - l) >> 1);  // 防止溢出的中点计算
            
            if (arr[mid] < num) {
                // 中点值小于目标值，目标在右半部分
                l = mid + 1;
            } else if (arr[mid] > num) {
                // 中点值大于目标值，目标在左半部分
                r = mid - 1;
            } else {
                // 找到目标值，记录位置，但继续在右半部分查找更大的索引
                ans = mid;
                l = mid + 1;  // 关键：继续在右半部分查找
            }
        }
        
        return ans;
    }

    /**
     * 查找目标值在排序数组中的开始和结束位置
     * 
     * 算法步骤：
     * 1. 检查输入有效性
     * 2. 调用first()方法找到第一个位置
     * 3. 调用last()方法找到最后一个位置
     * 4. 返回结果数组
     * 
     * 优化说明：
     * - 可以进一步优化：如果first()返回-1，则last()也必定返回-1
     * - 但为了代码清晰，这里保持两次独立调用
     * 
     * @param nums 非递减排序的整数数组
     * @param target 目标值
     * @return 长度为2的数组，[开始位置, 结束位置]，不存在返回[-1, -1]
     */
    public static int[] searchRange(int[] nums, int target) {
        int[] ans = {-1, -1};  // 默认结果：未找到
        
        // 边界检查：空数组或null
        if (nums == null || nums.length == 0) {
            return ans;
        }
        
        // 查找第一个和最后一个位置
        ans[0] = first(nums, target);   // 左边界
        ans[1] = last(nums, target);    // 右边界
        
        return ans;
    }
}
