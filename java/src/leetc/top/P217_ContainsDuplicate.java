package leetc.top;

import java.util.HashSet;

/**
 * LeetCode 217. 存在重复元素 (Contains Duplicate)
 * 
 * 问题描述：
 * 给你一个整数数组 nums 。如果任一值在数组中出现至少两次，返回 true ；
 * 如果数组中每个元素互不相同，返回 false 。
 * 
 * 示例：
 * 输入：nums = [1,2,3,1]
 * 输出：true
 * 
 * 解法思路：
 * 提供两种解法：
 * 1. 排序法：先排序，然后检查相邻元素是否相等
 * 2. 哈希集合法：使用HashSet检查元素是否已存在
 * 
 * 时间复杂度对比：
 * - 排序法：O(n log n) - 排序的时间复杂度
 * - 哈希集合法：O(n) - 遍历一次数组
 * 
 * 空间复杂度对比：
 * - 排序法：O(1) - 原地排序，不需要额外空间
 * - 哈希集合法：O(n) - 最坏情况下需要存储所有元素
 * 
 * LeetCode链接：https://leetcode.com/problems/contains-duplicate/
 */
public class P217_ContainsDuplicate {
    
    /**
     * 交换数组中两个位置的元素
     * 
     * @param arr 数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * 堆化操作：维护最大堆性质
     * 
     * @param arr 数组
     * @param idx 当前节点索引
     * @param heapSize 堆的大小
     */
    private static void heapify(int[] arr, int idx, int heapSize) {
        int left = idx * 2 + 1;  // 左子节点索引
        
        while (left < heapSize) {
            // 找出左右子节点中较大的一个
            int largest = left + 1 < heapSize && arr[left + 1] > arr[left] ? left + 1 : left;
            // 比较父节点和较大子节点
            largest = arr[largest] > arr[idx] ? largest : idx;
            
            // 如果父节点已经是最大的，堆性质满足
            if (largest == idx) {
                break;
            }
            
            // 交换父节点和较大子节点
            swap(arr, largest, idx);
            idx = largest;
            left = idx * 2 + 1;
        }
    }

    /**
     * 堆排序算法
     * 
     * 算法步骤：
     * 1. 建堆：从最后一个非叶子节点开始，自下而上进行堆化
     * 2. 排序：重复执行"取堆顶元素，重新堆化"的过程
     * 
     * @param arr 待排序数组
     */
    private static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        
        int heapSize = arr.length;
        
        // 建堆：从最后一个非叶子节点开始，自下而上堆化
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, heapSize);
        }
        
        // 排序：重复取堆顶元素并重新堆化
        swap(arr, 0, --heapSize);  // 将最大元素放到数组末尾
        while (heapSize > 0) {
            heapify(arr, 0, heapSize);  // 重新堆化
            swap(arr, 0, --heapSize);   // 取出当前最大元素
        }
    }

    /**
     * 方法1：排序法检查重复元素
     * 
     * 算法思路：
     * 1. 使用堆排序对数组进行排序
     * 2. 检查排序后的数组中是否有相邻的相等元素
     * 3. 如果有相邻相等元素，说明存在重复
     * 
     * 优点：空间复杂度低，原地排序
     * 缺点：时间复杂度较高，会修改原数组
     * 
     * @param nums 整数数组
     * @return 是否存在重复元素
     */
    public boolean containsDuplicate1(int[] nums) {
        // 边界情况：数组为空或长度小于2，不可能有重复
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        // 对数组进行排序
        heapSort(nums);
        
        // 检查排序后的数组中是否有相邻的相等元素
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                return true;  // 发现重复元素
            }
        }
        
        return false;  // 没有发现重复元素
    }

    /**
     * 方法2：哈希集合法检查重复元素
     * 
     * 算法思路：
     * 1. 使用HashSet存储已经遇到的元素
     * 2. 遍历数组，对每个元素检查是否已在集合中
     * 3. 如果元素已存在，说明有重复；否则将元素加入集合
     * 4. 遍历完成后没有发现重复，返回false
     * 
     * 优点：时间复杂度低，不修改原数组
     * 缺点：需要额外的空间存储哈希集合
     * 
     * @param nums 整数数组
     * @return 是否存在重复元素
     */
    public boolean containsDuplicate2(int[] nums) {
        // 边界情况：数组为空或长度小于2，不可能有重复
        if (nums == null || nums.length < 2) {
            return false;
        }
        
        HashSet<Integer> set = new HashSet<>();
        
        // 遍历数组，检查每个元素是否已存在
        for (int num : nums) {
            if (set.contains(num)) {
                return true;  // 发现重复元素
            }
            set.add(num);  // 将元素加入集合
        }
        
        return false;  // 没有发现重复元素
    }
}
