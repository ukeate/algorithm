package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
 * 
 * 问题描述：
 * 给定一个整数数组 nums，按要求返回一个新数组 counts。
 * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
 * 
 * 示例：
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：
 * - 5 的右侧有 2 个更小的元素 (2 和 1)
 * - 2 的右侧有 1 个更小的元素 (1)
 * - 6 的右侧有 1 个更小的元素 (1)
 * - 1 的右侧有 0 个更小的元素
 * 
 * 解法思路：
 * 归并排序 + 逆序对统计：
 * 
 * 1. 核心思想：
 *    - 从右往左归并排序，在合并过程中统计逆序对
 *    - 当左半部分元素 > 右半部分元素时，构成逆序对
 *    - 逆序对的数量就是右侧小于当前元素的个数
 * 
 * 2. 算法步骤：
 *    - 构建索引数组，记录原始位置
 *    - 对索引数组进行归并排序（按元素值排序）
 *    - 在合并过程中，统计每个元素的逆序对个数
 *    - 将统计结果映射回原始位置
 * 
 * 3. 关键技巧：
 *    - 从右往左处理，确保统计的是"右侧"元素
 *    - 使用索引数组避免元素值的直接移动
 *    - 在合并时累加逆序对计数
 * 
 * 核心思想：
 * - 分治策略：将数组分成两部分，分别计算内部逆序对和跨部分逆序对
 * - 逆序对统计：利用归并排序的稳定性和有序性快速统计
 * - 索引映射：保持原始位置信息，便于结果回填
 * 
 * 时间复杂度：O(n log n) - 归并排序的时间复杂度
 * 空间复杂度：O(n) - 辅助数组和递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/count-of-smaller-numbers-after-self/
 */
public class P315_CountOfSmallerNumbersAfterSelf {
    
    /**
     * 计算右侧小于当前元素的个数
     * 
     * @param nums 输入数组
     * @return 每个位置右侧小于该元素的数量
     */
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        
        int n = nums.length;
        int[] counts = new int[n];          // 存储每个位置的计数结果
        int[] indexes = new int[n];         // 索引数组，记录原始位置
        
        // 初始化索引数组
        for (int i = 0; i < n; i++) {
            indexes[i] = i;
        }
        
        // 归并排序，同时统计逆序对
        mergeSort(nums, indexes, counts, 0, n - 1);
        
        // 将结果转换为List返回
        for (int count : counts) {
            res.add(count);
        }
        
        return res;
    }
    
    /**
     * 归并排序主函数
     * 
     * 在排序过程中统计逆序对：
     * - 分治：递归处理左右两部分
     * - 合并：在合并有序数组时统计跨部分的逆序对
     * 
     * @param nums 原始数组
     * @param indexes 索引数组
     * @param counts 计数数组
     * @param left 左边界
     * @param right 右边界
     */
    private void mergeSort(int[] nums, int[] indexes, int[] counts, int left, int right) {
        if (left >= right) {
            return; // 递归终止条件
        }
        
        int mid = left + (right - left) / 2;
        
        // 分治：递归排序左右两部分
        mergeSort(nums, indexes, counts, left, mid);      // 处理左半部分
        mergeSort(nums, indexes, counts, mid + 1, right); // 处理右半部分
        
        // 合并：统计跨部分的逆序对
        merge(nums, indexes, counts, left, mid, right);
    }
    
    /**
     * 合并两个有序数组，同时统计逆序对
     * 
     * 关键逻辑：
     * - 当左半部分元素 > 右半部分元素时，形成逆序对
     * - 逆序对数量 = 右半部分当前位置到末尾的元素个数
     * 
     * @param nums 原始数组
     * @param indexes 索引数组
     * @param counts 计数数组
     * @param left 左边界
     * @param mid 中间位置
     * @param right 右边界
     */
    private void merge(int[] nums, int[] indexes, int[] counts, int left, int mid, int right) {
        int[] temp = new int[right - left + 1]; // 临时数组
        int i = left;      // 左半部分指针
        int j = mid + 1;   // 右半部分指针
        int k = 0;         // 临时数组指针
        
        // 合并两个有序部分
        while (i <= mid && j <= right) {
            if (nums[indexes[i]] <= nums[indexes[j]]) {
                // 左半部分元素较小或相等，不构成逆序对
                temp[k++] = indexes[i++];
            } else {
                // 左半部分元素较大，构成逆序对
                // 当前右半部分元素小于左半部分所有剩余元素
                // 所以左半部分每个剩余元素的计数都要增加
                for (int p = i; p <= mid; p++) {
                    counts[indexes[p]]++; // 统计逆序对
                }
                temp[k++] = indexes[j++];
            }
        }
        
        // 处理剩余元素
        while (i <= mid) {
            temp[k++] = indexes[i++];
        }
        while (j <= right) {
            temp[k++] = indexes[j++];
        }
        
        // 将排序结果复制回原数组
        for (i = left; i <= right; i++) {
            indexes[i] = temp[i - left];
        }
    }
}
