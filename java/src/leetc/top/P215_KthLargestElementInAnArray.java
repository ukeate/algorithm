package leetc.top;

/**
 * LeetCode 215. 数组中的第K个最大元素 (Kth Largest Element in an Array)
 * 
 * 问题描述：
 * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 * 
 * 示例：
 * 输入: [3,2,1,5,6,4] 和 k = 2
 * 输出: 5
 * 解释: 排序后的数组为 [1,2,3,4,5,6]，第2大的元素是5
 * 
 * 解法思路：
 * 快速选择算法（Quick Select）：
 * 1. 基于快速排序的分区思想
 * 2. 每次分区后，确定pivot的最终位置
 * 3. 根据目标位置与pivot位置的关系，决定在哪一侧继续搜索
 * 4. 平均时间复杂度O(n)，最坏O(n²)，但实际表现优秀
 * 
 * 核心优化：
 * - 随机选择pivot：避免最坏情况的出现
 * - 三路快排分区：将相等元素聚集在中间，处理重复值
 * - 原地算法：不需要额外空间
 * 
 * 分区过程：
 * - less指向小于pivot的区域右边界
 * - more指向大于pivot的区域左边界  
 * - 中间区域为等于pivot的元素
 * 
 * 与排序的区别：
 * - 排序需要处理整个数组
 * - 快速选择只需要找到第k个位置的元素
 * - 利用分治思想，每次排除一半不必要的搜索空间
 * 
 * 时间复杂度：平均O(n)，最坏O(n²)
 * 空间复杂度：O(log n) - 递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/kth-largest-element-in-an-array/
 */
public class P215_KthLargestElementInAnArray {
    
    /**
     * 交换数组中两个位置的元素
     * 
     * @param arr 数组
     * @param i1 第一个位置
     * @param i2 第二个位置
     */
    private static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = tmp;
    }

    /**
     * 三路快排的分区函数
     * 
     * 算法思路：
     * 1. 将数组分为三个区域：< pivot, = pivot, > pivot
     * 2. less指向小于区域的右边界，more指向大于区域的左边界
     * 3. cur指针遍历未处理区域
     * 
     * 分区结果：
     * [l, less] < pivot
     * [less+1, more-1] = pivot  
     * [more, r] > pivot
     * 
     * @param arr 待分区的数组
     * @param l 左边界
     * @param r 右边界  
     * @param pivot 基准值
     * @return 返回等于pivot区域的左右边界 [less+1, more-1]
     */
    private static int[] partition(int[] arr, int l, int r, int pivot) {
        int less = l - 1;    // 小于区域的右边界
        int more = r + 1;    // 大于区域的左边界
        int cur = l;         // 当前处理指针
        
        while (cur < more) {
            if (arr[cur] < pivot) {
                // 当前元素小于pivot，放入小于区域
                swap(arr, ++less, cur++);
            } else if (arr[cur] > pivot) {
                // 当前元素大于pivot，放入大于区域
                swap(arr, cur, --more);
                // 注意：cur不增加，因为交换来的元素还未处理
            } else {
                // 当前元素等于pivot，继续处理下一个
                cur++;
            }
        }
        
        // 返回等于pivot区域的边界
        return new int[] {less + 1, more - 1};
    }

    /**
     * 快速选择算法的递归实现
     * 
     * 算法思路：
     * 1. 随机选择pivot避免最坏情况
     * 2. 三路分区得到pivot的确切位置范围
     * 3. 根据目标位置idx决定在哪个区域继续搜索
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @param idx 目标位置（从小到大排序的第idx+1小的元素）
     * @return 第idx+1小的元素值
     */
    private static int process(int[] arr, int l, int r, int idx) {
        if (l == r) {
            return arr[l];  // 区间只有一个元素，直接返回
        }
        
        // 随机选择pivot，避免最坏情况
        int pivot = arr[l + (int) (Math.random() * (r - l + 1))];
        
        // 三路分区
        int[] range = partition(arr, l, r, pivot);
        
        if (idx >= range[0] && idx <= range[1]) {
            // 目标位置在等于pivot的区域内，直接返回pivot
            return arr[idx];
        } else if (idx < range[0]) {
            // 目标位置在小于pivot的区域，递归搜索左侧
            return process(arr, l, range[0] - 1, idx);
        } else {
            // 目标位置在大于pivot的区域，递归搜索右侧
            return process(arr, range[1] + 1, r, idx);
        }
    }

    /**
     * 寻找数组中第k小的元素
     * 
     * @param arr 数组
     * @param k 第k小（1-indexed）
     * @return 第k小的元素值
     */
    private static int minKth(int[] arr, int k) {
        return process(arr, 0, arr.length - 1, k - 1);
    }

    /**
     * 寻找数组中第k大的元素
     * 
     * 转换思路：
     * - 第k大的元素 = 第(n-k+1)小的元素
     * - 例如：在长度为6的数组中，第2大 = 第5小
     * 
     * @param nums 数组
     * @param k 第k大（1-indexed）
     * @return 第k大的元素值
     */
    public static int findKthLargest(int[] nums, int k) {
        return minKth(nums, nums.length + 1 - k);
    }
}
