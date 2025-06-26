package leetc.top;

import java.util.*;

/**
 * LeetCode 347. 前 K 个高频元素 (Top K Frequent Elements)
 * 
 * 问题描述：
 * 给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。
 * 你可以按 任意顺序 返回答案。
 * 
 * 示例：
 * 输入: nums = [1,1,1,2,2,3], k = 2
 * 输出: [1,2]
 * 
 * 输入: nums = [1], k = 1
 * 输出: [1]
 * 
 * 进阶：你所设计算法的时间复杂度 必须 优于 O(n log n) ，其中 n 是数组的大小。
 * 
 * 解法思路：
 * 哈希表 + 堆排序：
 * 
 * 1. 核心思想：
 *    - 先用哈希表统计每个元素的出现频率
 *    - 再使用堆（优先队列）来找出频率最高的k个元素
 * 
 * 2. 解法对比：
 *    方法一：最小堆（推荐）
 *    - 维护大小为k的最小堆
 *    - 堆顶是当前k个高频元素中频率最小的
 *    - 时间复杂度：O(n log k)
 *    
 *    方法二：最大堆
 *    - 将所有元素按频率放入最大堆
 *    - 然后取出前k个元素
 *    - 时间复杂度：O(n log n)
 *    
 *    方法三：桶排序
 *    - 根据频率建立桶，桶的索引为频率值
 *    - 从高频率到低频率遍历桶
 *    - 时间复杂度：O(n)
 * 
 * 3. 算法步骤（最小堆法）：
 *    - 统计频率：遍历数组，用哈希表记录每个元素的出现次数
 *    - 建立最小堆：遍历哈希表，维护大小为k的最小堆
 *    - 提取结果：从堆中提取所有元素作为结果
 * 
 * 核心思想：
 * - 频率统计：哈希表快速统计元素频率
 * - 堆选择：用堆维护TopK问题的经典解法
 * - 空间换时间：以O(n)空间换取更好的时间复杂度
 * 
 * 关键技巧：
 * - 最小堆维护：保持堆大小为k，淘汰频率较小的元素
 * - 自定义比较器：按频率而非元素值进行比较
 * - 桶排序优化：当k接近n时，桶排序更优
 * 
 * 时间复杂度：
 * - 最小堆法：O(n log k) - n为数组长度，k为所求元素个数
 * - 桶排序法：O(n) - 线性时间复杂度
 * 
 * 空间复杂度：O(n) - 哈希表和堆的空间开销
 * 
 * LeetCode链接：https://leetcode.com/problems/top-k-frequent-elements/
 */
public class P347_TopKFrequentElements {
    
    /**
     * 方法一：最小堆法（推荐）
     * 
     * 使用最小堆维护频率最高的k个元素
     * 堆的大小始终保持为k，堆顶元素是当前k个元素中频率最小的
     * 
     * @param nums 输入数组
     * @param k 需要返回的高频元素个数
     * @return 前k个高频元素数组
     */
    public int[] topKFrequent(int[] nums, int k) {
        // 第一步：统计每个元素的出现频率
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 第二步：建立最小堆，按频率排序
        // 堆顶始终是当前k个元素中频率最小的
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> frequencyMap.get(a) - frequencyMap.get(b)
        );
        
        // 第三步：遍历频率表，维护大小为k的最小堆
        for (int num : frequencyMap.keySet()) {
            minHeap.offer(num);
            
            // 如果堆大小超过k，移除频率最小的元素
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        
        // 第四步：从堆中提取结果
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }
        
        return result;
    }
    
    /**
     * 方法二：最大堆法
     * 
     * 将所有元素按频率放入最大堆，然后取出前k个
     * 
     * @param nums 输入数组
     * @param k 需要返回的高频元素个数
     * @return 前k个高频元素数组
     */
    public int[] topKFrequentMaxHeap(int[] nums, int k) {
        // 统计频率
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 最大堆：按频率从大到小排序
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
            (a, b) -> frequencyMap.get(b) - frequencyMap.get(a)
        );
        
        // 将所有元素加入最大堆
        maxHeap.addAll(frequencyMap.keySet());
        
        // 取出前k个元素
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        
        return result;
    }
    
    /**
     * 方法三：桶排序法（最优时间复杂度）
     * 
     * 根据频率建立桶，桶的索引为频率值
     * 从高频率到低频率遍历桶，直到找到k个元素
     * 
     * @param nums 输入数组
     * @param k 需要返回的高频元素个数
     * @return 前k个高频元素数组
     */
    public int[] topKFrequentBucketSort(int[] nums, int k) {
        // 统计频率
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 建立桶：索引为频率，值为该频率下的元素列表
        List<Integer>[] buckets = new List[nums.length + 1];
        for (int num : frequencyMap.keySet()) {
            int frequency = frequencyMap.get(num);
            if (buckets[frequency] == null) {
                buckets[frequency] = new ArrayList<>();
            }
            buckets[frequency].add(num);
        }
        
        // 从高频率到低频率遍历桶，收集前k个元素
        List<Integer> result = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && result.size() < k; i--) {
            if (buckets[i] != null) {
                result.addAll(buckets[i]);
            }
        }
        
        // 转换为数组并截取前k个元素
        return result.stream().limit(k).mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 方法四：快速选择算法（进阶）
     * 
     * 基于快速排序的分区思想，不需要完全排序
     * 平均时间复杂度：O(n)，最坏情况：O(n²)
     * 
     * @param nums 输入数组
     * @param k 需要返回的高频元素个数
     * @return 前k个高频元素数组
     */
    public int[] topKFrequentQuickSelect(int[] nums, int k) {
        // 统计频率
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }
        
        // 获取所有不同的元素
        int[] unique = frequencyMap.keySet().stream().mapToInt(Integer::intValue).toArray();
        
        // 使用快速选择找到第k大的频率分界点
        int n = unique.length;
        quickSelect(unique, 0, n - 1, n - k, frequencyMap);
        
        // 返回前k个高频元素
        return Arrays.copyOfRange(unique, n - k, n);
    }
    
    /**
     * 快速选择辅助方法
     * 
     * @param nums 元素数组
     * @param left 左边界
     * @param right 右边界
     * @param k 第k小的元素索引
     * @param frequencyMap 频率映射表
     */
    private void quickSelect(int[] nums, int left, int right, int k, Map<Integer, Integer> frequencyMap) {
        if (left == right) return;
        
        // 随机选择pivot以避免最坏情况
        Random random = new Random();
        int pivotIndex = left + random.nextInt(right - left + 1);
        
        // 分区操作
        pivotIndex = partition(nums, left, right, pivotIndex, frequencyMap);
        
        // 根据pivot位置决定下一步操作
        if (k == pivotIndex) {
            return; // 找到目标位置
        } else if (k < pivotIndex) {
            quickSelect(nums, left, pivotIndex - 1, k, frequencyMap); // 在左半部分查找
        } else {
            quickSelect(nums, pivotIndex + 1, right, k, frequencyMap); // 在右半部分查找
        }
    }
    
    /**
     * 分区操作：按频率将数组分为两部分
     * 
     * @param nums 元素数组
     * @param left 左边界
     * @param right 右边界
     * @param pivotIndex pivot元素索引
     * @param frequencyMap 频率映射表
     * @return 分区后pivot的最终位置
     */
    private int partition(int[] nums, int left, int right, int pivotIndex, Map<Integer, Integer> frequencyMap) {
        int pivotFrequency = frequencyMap.get(nums[pivotIndex]);
        
        // 将pivot移到末尾
        swap(nums, pivotIndex, right);
        
        // 分区操作
        int storeIndex = left;
        for (int i = left; i < right; i++) {
            if (frequencyMap.get(nums[i]) < pivotFrequency) {
                swap(nums, storeIndex, i);
                storeIndex++;
            }
        }
        
        // 将pivot放到正确位置
        swap(nums, storeIndex, right);
        return storeIndex;
    }
    
    /**
     * 交换数组中两个元素
     */
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P347_TopKFrequentElements solution = new P347_TopKFrequentElements();
        
        // 测试用例1
        int[] nums1 = {1, 1, 1, 2, 2, 3};
        int k1 = 2;
        System.out.println("测试1 - 最小堆法: " + Arrays.toString(solution.topKFrequent(nums1, k1)));
        System.out.println("测试1 - 桶排序法: " + Arrays.toString(solution.topKFrequentBucketSort(nums1, k1)));
        
        // 测试用例2
        int[] nums2 = {1};
        int k2 = 1;
        System.out.println("测试2 - 最小堆法: " + Arrays.toString(solution.topKFrequent(nums2, k2)));
        System.out.println("测试2 - 桶排序法: " + Arrays.toString(solution.topKFrequentBucketSort(nums2, k2)));
    }
}
