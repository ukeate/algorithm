package leetc.top;

import java.util.*;

/**
 * LeetCode 349. 两个数组的交集 (Intersection of Two Arrays)
 * 
 * 问题描述：
 * 给定两个数组 nums1 和 nums2 ，返回它们的交集。
 * 输出结果中的每个元素一定是 唯一 的。我们可以 不考虑输出结果的顺序。
 * 
 * 示例：
 * 输入：nums1 = [1,2,2,1], nums2 = [2,2]
 * 输出：[2]
 * 
 * 输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 * 输出：[9,4] 或者 [4,9]
 * 
 * 提示：
 * - 1 <= nums1.length, nums2.length <= 1000
 * - 0 <= nums1[i], nums2[i] <= 1000
 * 
 * 解法思路：
 * 哈希集合法：
 * 
 * 1. 核心思想：
 *    - 使用哈希集合（Set）的特性来快速查找和去重
 *    - 先将一个数组的所有元素放入集合中
 *    - 遍历另一个数组，检查元素是否在集合中存在
 * 
 * 2. 解法对比：
 *    方法一：双哈希集合法（推荐）
 *    - 将两个数组都转换为集合，然后求交集
 *    - 时间复杂度：O(m + n)，空间复杂度：O(m + n)
 *    
 *    方法二：单哈希集合法
 *    - 将较小的数组转换为集合，遍历另一个数组
 *    - 时间复杂度：O(m + n)，空间复杂度：O(min(m, n))
 *    
 *    方法三：排序 + 双指针法
 *    - 先对两个数组排序，然后使用双指针找交集
 *    - 时间复杂度：O(m log m + n log n)，空间复杂度：O(1)
 * 
 * 3. 算法步骤（双哈希集合法）：
 *    - 将第一个数组转换为集合（自动去重）
 *    - 遍历第二个数组，如果元素在集合中存在，加入结果集合
 *    - 将结果集合转换为数组返回
 * 
 * 核心思想：
 * - 哈希查找：利用哈希表O(1)的查找特性
 * - 自动去重：集合的特性保证结果唯一性
 * - 集合运算：将问题转化为数学集合的交集运算
 * 
 * 关键技巧：
 * - 空间换时间：使用额外空间换取更快的查找速度
 * - 内置API：利用Set的retainAll等内置方法
 * - 优化选择：根据数组大小选择最优算法
 * 
 * 时间复杂度：O(m + n) - m和n分别为两个数组的长度
 * 空间复杂度：O(min(m, n)) - 取决于存储较小数组的集合
 * 
 * LeetCode链接：https://leetcode.com/problems/intersection-of-two-arrays/
 */
public class P349_IntersectionOfTwoArrays {
    
    /**
     * 方法一：双哈希集合法（推荐）
     * 
     * 使用两个HashSet分别存储两个数组的元素，然后求交集
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        // 将第一个数组转换为集合（自动去重）
        Set<Integer> set1 = new HashSet<>();
        for (int num : nums1) {
            set1.add(num);
        }
        
        // 存储交集结果的集合
        Set<Integer> intersectionSet = new HashSet<>();
        
        // 遍历第二个数组，查找交集
        for (int num : nums2) {
            if (set1.contains(num)) {
                intersectionSet.add(num); // 添加到交集中（自动去重）
            }
        }
        
        // 将结果集合转换为数组
        return intersectionSet.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 方法二：单哈希集合法（空间优化）
     * 
     * 优先将较小的数组转换为集合，减少空间使用
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersectionOptimized(int[] nums1, int[] nums2) {
        // 确保nums1是较短的数组，减少空间使用
        if (nums1.length > nums2.length) {
            return intersectionOptimized(nums2, nums1);
        }
        
        // 将较短的数组转换为集合
        Set<Integer> set1 = new HashSet<>();
        for (int num : nums1) {
            set1.add(num);
        }
        
        // 存储交集结果
        Set<Integer> intersectionSet = new HashSet<>();
        
        // 遍历较长的数组
        for (int num : nums2) {
            if (set1.contains(num)) {
                intersectionSet.add(num);
            }
        }
        
        // 转换为数组
        int[] result = new int[intersectionSet.size()];
        int i = 0;
        for (int num : intersectionSet) {
            result[i++] = num;
        }
        
        return result;
    }
    
    /**
     * 方法三：使用内置API（最简洁）
     * 
     * 利用Set的retainAll方法直接求交集
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersectionBuiltIn(int[] nums1, int[] nums2) {
        // 将两个数组转换为集合
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        
        for (int num : nums1) {
            set1.add(num);
        }
        
        for (int num : nums2) {
            set2.add(num);
        }
        
        // 求交集（retainAll会修改set1，只保留两个集合的交集）
        set1.retainAll(set2);
        
        // 转换为数组
        return set1.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 方法四：排序 + 双指针法
     * 
     * 当数组已经排序或者空间限制严格时使用
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersectionTwoPointers(int[] nums1, int[] nums2) {
        // 对两个数组进行排序
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        
        // 使用双指针遍历两个已排序的数组
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] == nums2[j]) {
                // 找到交集元素，避免重复添加
                if (result.isEmpty() || result.get(result.size() - 1) != nums1[i]) {
                    result.add(nums1[i]);
                }
                i++;
                j++;
            } else if (nums1[i] < nums2[j]) {
                i++; // nums1[i]较小，移动i指针
            } else {
                j++; // nums2[j]较小，移动j指针
            }
        }
        
        // 转换为数组
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 方法五：位图法（当数值范围较小时使用）
     * 
     * 适用于数值范围较小（如0-1000）的情况
     * 
     * @param nums1 第一个数组
     * @param nums2 第二个数组
     * @return 交集数组
     */
    public int[] intersectionBitmap(int[] nums1, int[] nums2) {
        // 假设数值范围是0-1000（根据题目提示）
        boolean[] bitmap1 = new boolean[1001];
        boolean[] bitmap2 = new boolean[1001];
        
        // 标记第一个数组中出现的数字
        for (int num : nums1) {
            bitmap1[num] = true;
        }
        
        // 标记第二个数组中出现的数字
        for (int num : nums2) {
            bitmap2[num] = true;
        }
        
        // 找出交集
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i <= 1000; i++) {
            if (bitmap1[i] && bitmap2[i]) {
                result.add(i);
            }
        }
        
        return result.stream().mapToInt(Integer::intValue).toArray();
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P349_IntersectionOfTwoArrays solution = new P349_IntersectionOfTwoArrays();
        
        // 测试用例1
        int[] nums1 = {1, 2, 2, 1};
        int[] nums2 = {2, 2};
        System.out.println("测试1 - 哈希集合法: " + Arrays.toString(solution.intersection(nums1, nums2)));
        System.out.println("测试1 - 双指针法: " + Arrays.toString(solution.intersectionTwoPointers(nums1, nums2)));
        
        // 测试用例2
        int[] nums3 = {4, 9, 5};
        int[] nums4 = {9, 4, 9, 8, 4};
        System.out.println("测试2 - 哈希集合法: " + Arrays.toString(solution.intersection(nums3, nums4)));
        System.out.println("测试2 - 位图法: " + Arrays.toString(solution.intersectionBitmap(nums3, nums4)));
        
        // 测试用例3：空交集
        int[] nums5 = {1, 2, 3};
        int[] nums6 = {4, 5, 6};
        System.out.println("测试3 - 空交集: " + Arrays.toString(solution.intersection(nums5, nums6)));
    }
} 