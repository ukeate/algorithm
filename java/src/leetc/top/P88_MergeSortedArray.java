package leetc.top;

/**
 * LeetCode 88. 合并两个有序数组 (Merge Sorted Array)
 * 
 * 问题描述：
 * 给你两个按非递减顺序排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n，
 * 分别表示 nums1 和 nums2 中元素的数量。
 * 
 * 请你将 nums2 合并到 nums1 中，使合并后的数组同样按非递减顺序排列。
 * 
 * 注意：
 * - nums1 的初始长度为 m + n，其中前 m 个元素表示应合并的元素，
 *   后 n 个元素为 0，应忽略。
 * - nums2 的长度为 n。
 * 
 * 解法思路：
 * 从后往前归并 - 避免元素覆盖：
 * 1. 使用三个指针：idx指向合并位置，m-1和n-1分别指向两个数组的末尾
 * 2. 比较两个数组当前元素，将较大的放入nums1的末尾
 * 3. 从后往前归并可以避免覆盖nums1中未处理的元素
 * 4. 处理剩余元素：如果nums2还有剩余，需要复制到nums1前面
 * 
 * 为什么从后往前：
 * - nums1的后半部分是空的（全0），从后往前不会覆盖有效数据
 * - 如果从前往后，需要额外空间或者移动元素，效率低
 * 
 * 时间复杂度：O(m + n) - 每个元素最多被访问一次
 * 空间复杂度：O(1) - 原地操作，不需要额外空间
 */
public class P88_MergeSortedArray {
    
    /**
     * 合并两个有序数组
     * 
     * @param nums1 第一个有序数组，长度为m+n，后n个位置用于存放合并结果
     * @param m nums1中有效元素的个数
     * @param nums2 第二个有序数组，长度为n
     * @param n nums2中元素的个数
     */
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int idx = nums1.length;  // 合并位置，从nums1的末尾开始
        
        // 从后往前归并，将较大的元素放在后面
        while (m > 0 && n > 0) {
            if (nums1[m - 1] >= nums2[n - 1]) {
                nums1[--idx] = nums1[--m];  // nums1的当前元素较大
            } else {
                nums1[--idx] = nums2[--n];  // nums2的当前元素较大
            }
        }
        
        // 处理nums1的剩余元素（已经在正确位置，无需移动）
        while (m > 0) {
            nums1[--idx] = nums1[--m];
        }
        
        // 处理nums2的剩余元素（需要复制到nums1前面）
        while (n > 0) {
            nums1[--idx] = nums2[--n];
        }
    }
}
