package leetc.top;

/**
 * LeetCode 324. 摆动排序 II (Wiggle Sort II)
 * 
 * 问题描述：
 * 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的形式。
 * 你可以假设所有输入数组都可以得到满足题目要求的结果。
 * 
 * 示例：
 * 输入：nums = [1, 5, 1, 1, 6, 4]
 * 输出：[1, 6, 1, 5, 1, 4]
 * 解释：[1, 4, 1, 5, 1, 6] 同样是符合题目要求的结果，可以被判题程序接受。
 * 
 * 解法思路：
 * 三向切分 + 巧妙映射：
 * 
 * 1. 核心思想：
 *    - 找到数组的中位数，将数组分为三部分：小于、等于、大于中位数
 *    - 使用特殊的索引映射，将元素放置到正确的摆动位置
 * 
 * 2. 算法步骤：
 *    - 使用改进的快速选择算法找到中位数
 *    - 使用三向切分将数组分为三个区域
 *    - 通过索引映射确保摆动排序的要求
 * 
 * 3. 索引映射技巧：
 *    - 对于长度为n的数组，映射公式为：(1 + 2*i) % (n|1)
 *    - 这个映射确保较小的数放在偶数位置，较大的数放在奇数位置
 *    - 相等的数被均匀分布，避免相邻
 * 
 * 4. 三向切分：
 *    - 小于中位数的数放在左边（映射到偶数位置的后半部分）
 *    - 等于中位数的数放在中间
 *    - 大于中位数的数放在右边（映射到奇数位置）
 * 
 * 核心思想：
 * - 中位数选择：确定分割点，保证两边数量基本相等
 * - 三向切分：处理重复元素，确保相等元素不相邻
 * - 索引映射：巧妙的位置变换，直接得到摆动排序结果
 * 
 * 关键技巧：
 * - newIndex映射：将逻辑位置映射到实际数组位置
 * - 原地操作：O(1)额外空间复杂度
 * - 一次遍历：O(n)时间复杂度完成排序
 * 
 * 时间复杂度：O(n) - 快速选择 + 三向切分
 * 空间复杂度：O(1) - 原地算法
 * 
 * LeetCode链接：https://leetcode.com/problems/wiggle-sort-ii/
 */
public class P324_WiggleSortII {
    
    /**
     * 将数组重排为摆动排序
     * 
     * 算法流程：
     * 1. 找到数组的中位数
     * 2. 使用三向切分，同时应用索引映射
     * 3. 将小于中位数的数放在左区域（偶数位置后半部分）
     * 4. 将大于中位数的数放在右区域（奇数位置）
     * 5. 将等于中位数的数放在中间区域
     * 
     * @param nums 待排序的数组
     */
    public void wiggleSort(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        
        int n = nums.length;
        
        // 第一步：找到中位数
        int median = findKthLargest(nums, (n + 1) / 2);
        
        // 第二步：使用三向切分和索引映射进行摆动排序
        int left = 0;      // 左边界：小于中位数的区域
        int right = n - 1; // 右边界：大于中位数的区域
        int i = 0;         // 当前处理位置
        
        // 三向切分 + 索引映射
        while (i <= right) {
            if (nums[newIndex(i, n)] > median) {
                // 当前元素大于中位数，应该放在右区域（奇数位置）
                swap(nums, newIndex(i, n), newIndex(left, n));
                left++;
                i++;
            } else if (nums[newIndex(i, n)] < median) {
                // 当前元素小于中位数，应该放在左区域（偶数位置后半部分）
                swap(nums, newIndex(i, n), newIndex(right, n));
                right--;
                // 注意：这里不递增i，因为交换过来的元素还未处理
            } else {
                // 当前元素等于中位数，保持在中间位置
                i++;
            }
        }
    }
    
    /**
     * 索引映射函数
     * 
     * 将逻辑索引映射到实际数组位置，实现摆动排序的效果：
     * - 较大的数优先放在奇数位置：1, 3, 5, ...
     * - 较小的数优先放在偶数位置：0, 2, 4, ...
     * - 映射公式：(1 + 2*i) % (n|1)
     * 
     * 这个映射的巧妙之处：
     * - 对于前半部分索引，映射到奇数位置（从左到右）
     * - 对于后半部分索引，映射到偶数位置（从右到左）
     * 
     * @param i 逻辑索引
     * @param n 数组长度
     * @return 映射后的实际索引
     */
    private int newIndex(int i, int n) {
        return (1 + 2 * i) % (n | 1);
    }
    
    /**
     * 交换数组中两个位置的元素
     * 
     * @param nums 数组
     * @param i 位置i
     * @param j 位置j
     */
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    /**
     * 找到数组中第k大的元素（快速选择算法）
     * 
     * 使用类似快速排序的分区思想：
     * 1. 随机选择pivot
     * 2. 将数组分为两部分：大于pivot和小于等于pivot
     * 3. 根据pivot位置决定在哪一部分继续查找
     * 
     * @param nums 数组
     * @param k 第k大（1-indexed）
     * @return 第k大的元素值
     */
    private int findKthLargest(int[] nums, int k) {
        int left = 0;
        int right = nums.length - 1;
        
        while (left < right) {
            int pivotIndex = partition(nums, left, right);
            
            if (pivotIndex == k - 1) {
                break; // 找到第k大的元素
            } else if (pivotIndex < k - 1) {
                left = pivotIndex + 1; // 在右半部分查找
            } else {
                right = pivotIndex - 1; // 在左半部分查找
            }
        }
        
        return nums[k - 1];
    }
    
    /**
     * 分区操作（降序排列，大的元素在前面）
     * 
     * @param nums 数组
     * @param left 左边界
     * @param right 右边界
     * @return pivot的最终位置
     */
    private int partition(int[] nums, int left, int right) {
        int pivot = nums[right]; // 选择最右边的元素作为pivot
        int i = left;
        
        for (int j = left; j < right; j++) {
            if (nums[j] >= pivot) { // 降序：大于等于pivot的放在左边
                swap(nums, i, j);
                i++;
            }
        }
        
        swap(nums, i, right); // 将pivot放到正确位置
        return i;
    }
}
