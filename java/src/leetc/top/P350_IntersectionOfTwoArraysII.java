package leetc.top;

import java.util.*;

/**
 * LeetCode 350. 两个数组的交集 II (Intersection of Two Arrays II)
 * 
 * 问题描述：
 * 给你两个整数数组 nums1 和 nums2 ，请你以数组形式返回两数组的交集。
 * 返回结果中每个元素出现的次数，应与元素在两个数组中都出现的次数一致（如果出现次数不一致，则考虑取较小值）。
 * 可以不考虑输出结果的顺序。
 * 
 * 示例：
 * 输入：nums1 = [1,2,2,1], nums2 = [2,2]
 * 输出：[2,2]
 * 
 * 输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 * 输出：[4,9] 或者 [9,4]
 * 
 * 进阶：
 * - 如果给定的数组已经排好序呢？你将如何优化你的算法？
 * - 如果 nums1 的大小比 nums2 小，哪种方法更优？
 * - 如果 nums2 的元素存储在磁盘上，内存是有限的，并且你不能一次加载所有的元素到内存中，你将怎么处理？
 * 
 * 解法思路：
 * 哈希计数法：
 * 
 * 1. 核心思想：
 *    - 使用哈希表记录一个数组中每个元素的出现次数
 *    - 遍历另一个数组，如果元素在哈希表中存在且计数大于0，则加入结果并减少计数
 * 
 * 2. 解法对比：
 *    方法一：哈希计数法（推荐）
 *    - 统计较小数组的元素频次，遍历较大数组
 *    - 时间复杂度：O(m + n)，空间复杂度：O(min(m, n))
 *    
 *    方法二：排序 + 双指针法
 *    - 对两个数组排序，然后使用双指针找交集
 *    - 时间复杂度：O(m log m + n log n)，空间复杂度：O(1)
 *    
 *    方法三：排序 + 二分查找法
 *    - 对一个数组排序，对另一个数组的每个元素进行二分查找
 *    - 时间复杂度：O((m + n) log n)，适用于一个数组较小的情况
 * 
 * 3. 算法步骤（哈希计数法）：
 *    - 选择较小的数组建立哈希表，统计每个元素的出现次数
 *    - 遍历较大的数组，对每个元素：
 *      * 如果在哈希表中存在且计数大于0，加入结果
 *      * 将哈希表中对应元素的计数减1
 *    - 返回结果数组
 * 
 * 核心思想：
 * - 频次统计：使用哈希表统计元素出现次数
 * - 取最小值：交集元素的个数取两个数组中较小的出现次数
 * - 空间优化：选择较小的数组建立哈希表
 * 
 * 关键技巧：
 * - 大小判断：选择较小数组建哈希表，节省空间
 * - 动态减计数：避免重复统计已使用的元素
 * - 适配性：根据数组特点选择最优算法
 * 
 * 时间复杂度：O(m + n) - m和n分别为两个数组的长度
 * 空间复杂度：O(min(m, n)) - 哈希表存储较小数组的元素
 * 
 * LeetCode链接：https://leetcode.com/problems/intersection-of-two-arrays-ii/
 */
public class P350_IntersectionOfTwoArraysII {
    
    /**
     * 方法一：哈希计数法（推荐）
     * 
     * 使用哈希表统计一个数组的元素频次，然后遍历另一个数组找交集
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组，包含重复元素
     */
    public int[] intersect(int[] nums1, int[] nums2) {
        // 优化：使用较小的数组建立哈希表，节省空间
        if (nums1.length > nums2.length) {
            return intersect(nums2, nums1);
        }
        
        // 统计nums1中每个元素的出现次数
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums1) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        
        // 遍历nums2，寻找交集元素
        List<Integer> result = new ArrayList<>();
        for (int num : nums2) {
            // 如果元素在countMap中存在且计数大于0
            if (countMap.getOrDefault(num, 0) > 0) {
                result.add(num);                        // 加入结果
                countMap.put(num, countMap.get(num) - 1); // 减少计数
            }
        }
        
        // 转换为数组
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 方法二：排序 + 双指针法
     * 
     * 对两个数组排序后，使用双指针同时遍历找交集
     * 适用于数组已排序或空间受限的情况
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersectTwoPointers(int[] nums1, int[] nums2) {
        // 对两个数组进行排序
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        
        // 双指针遍历两个已排序数组
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] == nums2[j]) {
                // 找到交集元素
                result.add(nums1[i]);
                i++;
                j++;
            } else if (nums1[i] < nums2[j]) {
                i++; // nums1[i]较小，移动i指针
            } else {
                j++; // nums2[j]较小，移动j指针
            }
        }
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 方法三：基于计数的原地操作（适用于数值范围较小的情况）
     * 
     * 当数值范围较小时，可以使用数组代替哈希表进行计数
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersectCounting(int[] nums1, int[] nums2) {
        // 假设数值范围在0-1000之间（可根据实际情况调整）
        int[] count = new int[1001];
        
        // 统计nums1中每个元素的出现次数
        for (int num : nums1) {
            count[num]++;
        }
        
        // 遍历nums2，寻找交集
        List<Integer> result = new ArrayList<>();
        for (int num : nums2) {
            if (count[num] > 0) {
                result.add(num);
                count[num]--; // 减少可用计数
            }
        }
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 方法四：外部排序法（适用于大数据场景）
     * 
     * 当数据量很大，无法全部加载到内存时的处理方案
     * 这里仅提供思路示例，实际应用中需要配合文件I/O操作
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersectExternalSort(int[] nums1, int[] nums2) {
        // 1. 对两个数组分别进行外部排序
        // 2. 使用双指针法在排序后的数据上找交集
        // 3. 分批读取数据，避免内存溢出
        
        // 这里简化为内存排序作为示例
        return intersectTwoPointers(nums1, nums2);
    }
    
    /**
     * 进阶问题的解决方案
     */
    
    /**
     * 问题1：如果数组已经排序，如何优化？
     * 答案：直接使用双指针法，无需额外排序，时间复杂度降为O(m + n)
     */
    public int[] intersectSorted(int[] nums1, int[] nums2) {
        // 假设输入数组已经排序
        return intersectTwoPointers(nums1, nums2);
    }
    
    /**
     * 问题2：如果nums1比nums2小很多，哪种方法更优？
     * 答案：使用哈希计数法，用较小的nums1建立哈希表
     */
    public int[] intersectSmallArray(int[] nums1, int[] nums2) {
        // 确保nums1是较小的数组
        if (nums1.length > nums2.length) {
            return intersectSmallArray(nums2, nums1);
        }
        return intersect(nums1, nums2);
    }
    
    /**
     * 问题3：nums2存储在磁盘上，内存有限，如何处理？
     * 
     * 解决方案：
     * 1. 将nums1加载到内存并建立哈希表
     * 2. 分批读取nums2的数据块
     * 3. 对每个数据块应用哈希计数法
     * 4. 将结果写入磁盘或累积到最终结果中
     */
    public int[] intersectLimitedMemory(int[] nums1, int[] nums2) {
        // 建立nums1的哈希表（假设nums1能完全加载到内存）
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums1) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        
        List<Integer> result = new ArrayList<>();
        
        // 模拟分批处理nums2（实际应用中需要配合文件I/O）
        int batchSize = 1000; // 每批处理的元素数量
        for (int start = 0; start < nums2.length; start += batchSize) {
            int end = Math.min(start + batchSize, nums2.length);
            
            // 处理当前批次
            for (int i = start; i < end; i++) {
                int num = nums2[i];
                if (countMap.getOrDefault(num, 0) > 0) {
                    result.add(num);
                    countMap.put(num, countMap.get(num) - 1);
                }
            }
        }
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P350_IntersectionOfTwoArraysII solution = new P350_IntersectionOfTwoArraysII();
        
        // 测试用例1
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};
        System.out.println("测试1 - 哈希计数法: " + Arrays.toString(solution.intersect(nums1, nums2)));
        System.out.println("测试1 - 双指针法: " + Arrays.toString(solution.intersectTwoPointers(nums1, nums2)));
        
        // 测试用例2
        int[] nums3 = {4, 9, 5};
        int[] nums4 = {9, 4, 9, 8, 4};
        System.out.println("测试2 - 哈希计数法: " + Arrays.toString(solution.intersect(nums3, nums4)));
        System.out.println("测试2 - 计数法: " + Arrays.toString(solution.intersectCounting(nums3, nums4)));
        
        // 测试用例3：包含重复元素
        int[] nums5 = {1, 2, 2, 1};
        int[] nums6 = {2, 2, 3, 3};
        System.out.println("测试3 - 重复元素: " + Arrays.toString(solution.intersect(nums5, nums6)));
        
        // 测试用例4：一个数组为空
        int[] nums7 = {};
        int[] nums8 = {1, 2, 3};
        System.out.println("测试4 - 空数组: " + Arrays.toString(solution.intersect(nums7, nums8)));
    }
}
