package leetc.top;

/**
 * LeetCode 33. 搜索旋转排序数组
 * https://leetcode.cn/problems/search-in-rotated-sorted-array/
 * 
 * 问题描述：
 * 整数数组 nums 按升序排列，数组中的值 互不相同 。
 * 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了旋转，
 * 使数组变为 [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标从 0 开始计数）。
 * 例如， [0,1,2,4,5,6,7] 在下标 3 处经旋转后可能变为 [4,5,6,7,0,1,2] 。
 * 给你旋转后的数组 nums 和一个整数 target ，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回 -1 。
 * 
 * 解题思路：
 * 使用修改版的二分搜索算法。旋转排序数组的特点是：
 * 1. 数组被分为两个有序的部分
 * 2. 通过mid点可以判断哪一部分是有序的
 * 3. 根据target与有序部分的关系来缩小搜索范围
 * 
 * 算法流程：
 * 1. 使用二分搜索的框架
 * 2. 每次都判断当前的mid将数组分成的两部分中哪一部分是有序的
 * 3. 如果target在有序部分的范围内，就在有序部分搜索
 * 4. 否则在另一部分搜索
 * 
 * 特殊情况处理：
 * - 当arr[l] == arr[mid] == arr[r]时，无法判断哪一部分有序，需要特殊处理
 * - 通过移动左指针来消除重复元素的影响
 * 
 * 时间复杂度：O(log n)，但在最坏情况下（大量重复元素）可能退化为O(n)
 * 空间复杂度：O(1)
 */
public class P33_SearchInRotatedSortedArray {
    
    /**
     * 在旋转排序数组中搜索目标值
     * 
     * @param arr 旋转后的排序数组
     * @param num 目标值
     * @return 目标值的下标，如果不存在则返回-1
     */
    public static int search(int[] arr, int num) {
        int l = 0, r = arr.length - 1, mid = 0;
        
        while (l <= r) {
            mid = (l + r) / 2;
            
            // 如果找到目标值，直接返回
            if (arr[mid] == num) {
                return mid;
            }
            
            // 特殊情况：左、中、右三个位置的值都相等，无法判断哪一部分有序
            if (arr[l] == arr[mid] && arr[mid] == arr[r]) {
                // 尝试移动左指针，跳过重复元素
                while (l != mid && arr[l] == arr[mid]) {
                    l++;
                }
                // 如果左指针移动到了mid位置，说明左半部分全是重复元素
                if (l == mid) {
                    l = mid + 1;
                    continue;
                }
            }
            
            // 判断左半部分和右半部分哪一个是有序的
            if (arr[l] != arr[mid]) {
                // 左半部分有序的情况
                if (arr[mid] > arr[l]) {
                    // 如果目标值在左半部分的有序范围内
                    if (num >= arr[l] && num < arr[mid]) {
                        r = mid - 1;
                    } else {
                        l = mid + 1;
                    }
                } 
                // 右半部分有序的情况
                else {
                    // 如果目标值在右半部分的有序范围内
                    if (num > arr[mid] && num <= arr[r]) {
                        l = mid + 1;
                    } else {
                        r = mid - 1;
                    }
                }
            } else {
                // arr[l] == arr[mid] && arr[mid] != arr[r]
                // 右半部分有序的情况
                if (arr[mid] < arr[r]) {
                    // 如果目标值在右半部分的有序范围内
                    if (num > arr[mid] && num <= arr[r]) {
                        l = mid + 1;
                    } else {
                        r = mid - 1;
                    }
                } 
                // 左半部分有序的情况
                else {
                    // 如果目标值在左半部分的有序范围内
                    if (num >= arr[l] && num < arr[mid]) {
                        r = mid - 1;
                    } else {
                        l = mid + 1;
                    }
                }
            }
        }
        
        // 未找到目标值
        return -1;
    }
}
