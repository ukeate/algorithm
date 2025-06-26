package leetc.top;

/**
 * LeetCode 4. 寻找两个正序数组的中位数 (Median of Two Sorted Arrays)
 * 
 * 问题描述：
 * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。
 * 请你找出并返回这两个正序数组的中位数。
 * 
 * 算法的时间复杂度应该为 O(log (m+n))。
 * 
 * 示例：
 * - 输入：nums1 = [1,3], nums2 = [2]，输出：2.00000
 * - 解释：合并数组 = [1,2,3]，中位数 2
 * 
 * 解法思路：
 * 第K小数问题 + 二分查找：
 * 1. 将问题转化为寻找第K小的数（中位数就是第(n+m)/2或(n+m)/2+1小的数）
 * 2. 使用二分查找在两个数组中找到第K小的数
 * 3. 根据数组长度关系分情况讨论，确保时间复杂度为O(log(min(m,n)))
 * 
 * 核心算法：
 * - 当K <= min(m,n)时：在两个数组的前K个元素中寻找下中位数
 * - 当K > max(m,n)时：需要排除一些明显不可能的元素
 * - 其他情况：在指定范围内寻找下中位数
 * 
 * 时间复杂度：O(log(min(m,n))) - 通过二分查找优化
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P4_MedianOfTwoSortedArrays {
    
    /**
     * 在两个有序数组的指定范围内寻找下中位数
     * 
     * 算法思路：
     * 使用二分查找，每次比较两个数组的中点元素，根据大小关系缩小搜索范围
     * 
     * @param a1 第一个有序数组
     * @param s1 第一个数组的搜索起始位置
     * @param e1 第一个数组的搜索结束位置
     * @param a2 第二个有序数组
     * @param s2 第二个数组的搜索起始位置
     * @param e2 第二个数组的搜索结束位置
     * @return 两个数组合并后的下中位数
     */
    private static int downMedian(int[] a1, int s1, int e1, int[] a2, int s2, int e2) {
        while (s1 < e1) {
            int m1 = (s1 + e1) / 2;  // 第一个数组的中点
            int m2 = (s2 + e2) / 2;  // 第二个数组的中点
            
            // 计算偏移量：偶数个元素时offset=1，奇数个元素时offset=0
            int offset = ((e1 - s1 + 1) & 1) ^ 1;
            
            if (a1[m1] > a2[m2]) {
                // a1[m1]较大，下中位数在左半部分
                e1 = m1;                // 缩小a1的搜索范围
                s2 = m2 + offset;       // 排除a2左半部分
            } else if (a1[m1] < a2[m2]) {
                // a1[m1]较小，下中位数在右半部分
                s1 = m1 + offset;       // 排除a1左半部分
                e2 = m2;                // 缩小a2的搜索范围
            } else {
                // 两个中点相等，直接返回
                return a1[m1];
            }
        }
        // 搜索范围收缩到单个元素，返回较小值
        return Math.min(a1[s1], a2[s2]);
    }

    /**
     * 在两个有序数组中寻找第K小的数
     * 
     * 算法思路：
     * 根据K值和数组长度的关系，分三种情况处理：
     * 1. K <= 短数组长度：在两个数组的前K个元素中寻找
     * 2. K > 长数组长度：排除一些不可能的元素后再寻找
     * 3. 其他情况：在指定范围内寻找
     * 
     * @param arr1 第一个有序数组
     * @param arr2 第二个有序数组
     * @param kth 要寻找第几小的数（从1开始）
     * @return 第K小的数
     */
    private static int findKthNum(int[] arr1, int[] arr2, int kth) {
        // 确定长数组和短数组
        int[] longs = arr1.length >= arr2.length ? arr1 : arr2;
        int[] shorts = arr1.length < arr2.length ? arr1 : arr2;
        int l = longs.length, s = shorts.length;
        
        // 情况1：K <= 短数组长度，在两个数组的前K个元素中寻找下中位数
        if (kth <= s) {
            return downMedian(shorts, 0, kth - 1, longs, 0, kth - 1);
        }
        
        // 情况2：K > 长数组长度，需要从两个数组中都取一些元素
        if (kth > l) {
            // 检查是否可以直接确定结果
            if (shorts[kth - l - 1] >= longs[l - 1]) {
                return shorts[kth - l - 1];
            }
            if (longs[kth - s - 1] >= shorts[s - 1]){
                return longs[kth - s - 1];
            }
            // 在剩余范围内寻找下中位数
            return downMedian(shorts, kth - l, s - 1, longs, kth - s, l - 1);
        }
        
        // 情况3：短数组长度 < K <= 长数组长度
        if (longs[kth - s - 1] >= shorts[s - 1]) {
            return longs[kth - s - 1];
        }
        return downMedian(shorts, 0, s - 1, longs, kth - s, kth - 1);
    }

    /**
     * 寻找两个正序数组的中位数
     * 
     * 算法步骤：
     * 1. 计算总长度，判断奇偶性
     * 2. 处理边界情况（有空数组）
     * 3. 对于偶数长度：返回第n/2和第n/2+1小数的平均值
     * 4. 对于奇数长度：返回第(n+1)/2小的数
     * 
     * @param nums1 第一个正序数组
     * @param nums2 第二个正序数组
     * @return 两个数组的中位数
     */
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int size = nums1.length + nums2.length;
        boolean even = (size & 1) == 0;  // 判断总长度是否为偶数
        
        if (nums1.length != 0 & nums2.length != 0) {
            // 两个数组都非空
            if (even) {
                // 偶数个元素：返回中间两个数的平均值
                return (double) (findKthNum(nums1, nums2, size / 2) + 
                                findKthNum(nums1, nums2, size / 2 + 1)) / 2;
            } else {
                // 奇数个元素：返回中间的数
                return findKthNum(nums1, nums2, size / 2 + 1);
            }
        } else if (nums1.length != 0) {
            // 只有nums1非空
            if (even) {
                return (double) (nums1[(size - 1) / 2] + nums1[size / 2]) / 2;
            } else {
                return nums1[size / 2];
            }
        } else if (nums2.length != 0) {
            // 只有nums2非空
            if (even) {
                return (double) (nums2[(size - 1) / 2] + nums2[size / 2]) / 2;
            } else {
                return nums2[size / 2];
            }
        } else {
            // 两个数组都为空
            return 0;
        }
    }
}
