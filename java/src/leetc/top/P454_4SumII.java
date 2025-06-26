package leetc.top;

import java.util.HashMap;

/**
 * LeetCode 454. 四数相加 II (4Sum II)
 * 
 * 问题描述：
 * 给你四个整数数组 nums1、nums2、nums3 和 nums4，数组长度都是 n，请你计算有多少个元组 (i, j, k, l) 
 * 能满足：
 * 0 <= i, j, k, l < n
 * nums1[i] + nums2[j] + nums3[k] + nums4[l] == 0
 * 
 * 示例：
 * - 输入：nums1 = [1,2], nums2 = [-2,-1], nums3 = [-1,2], nums4 = [0,2]
 * - 输出：2
 * - 解释：两个元组如下：
 *   1. (0, 0, 0, 1) -> nums1[0] + nums2[0] + nums3[0] + nums4[1] = 1 + (-2) + (-1) + 2 = 0
 *   2. (1, 1, 0, 0) -> nums1[1] + nums2[1] + nums3[0] + nums4[0] = 2 + (-1) + (-1) + 0 = 0
 * 
 * 解法思路：
 * 分组哈希：
 * 1. 将四个数组分为两组：(nums1, nums2) 和 (nums3, nums4)
 * 2. 计算前两个数组所有可能的和，用哈希表记录每个和值的出现次数
 * 3. 计算后两个数组所有可能的和，查找是否存在相反数
 * 4. 如果存在，说明四个数的和为0，累加计数
 * 
 * 核心思想：
 * - 减少时间复杂度：从O(n^4)降低到O(n^2)
 * - 空间换时间：使用哈希表预存前两个数组的和
 * - 分治思维：将四元组问题转化为两个二元组问题
 * 
 * 时间复杂度：O(n^2) - 两次二重循环
 * 空间复杂度：O(n^2) - 哈希表最多存储n^2个不同的和
 * 
 * LeetCode链接：https://leetcode.com/problems/4sum-ii/
 */
public class P454_4SumII {
    
    /**
     * 计算四数相加为0的元组数量
     * 
     * 算法步骤：
     * 1. 遍历前两个数组，计算所有可能的两数之和
     * 2. 将每个和值及其出现次数存入哈希表
     * 3. 遍历后两个数组，计算所有可能的两数之和
     * 4. 查找该和值的相反数是否在哈希表中
     * 5. 如果存在，累加其出现次数到结果中
     * 
     * 优化思路：
     * - 分组策略：将4个数组分成2组，每组2个
     * - 哈希查找：O(1)时间复杂度查找匹配
     * - 计数累加：处理重复和值的情况
     * 
     * @param a1 第一个数组
     * @param a2 第二个数组
     * @param a3 第三个数组
     * @param a4 第四个数组
     * @return 四数相加为0的元组数量
     */
    public static int fourSumCount(int[] a1, int[] a2, int[] a3, int[] a4) {
        // 哈希表：前两个数组的和 -> 出现次数
        HashMap<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        
        // 第一阶段：计算前两个数组所有可能的两数之和
        for (int i = 0; i < a1.length; i++) {
            for (int j = 0; j < a2.length; j++) {
                sum = a1[i] + a2[j];
                
                // 记录该和值的出现次数
                if (!map.containsKey(sum)) {
                    map.put(sum, 1);
                } else {
                    map.put(sum, map.get(sum) + 1);
                }
            }
        }
        
        int ans = 0;
        
        // 第二阶段：计算后两个数组的和，并查找匹配
        for (int i = 0; i < a3.length; i++) {
            for (int j = 0; j < a4.length; j++) {
                sum = a3[i] + a4[j];
                
                // 查找是否存在相反数，使得四数之和为0
                if (map.containsKey(-sum)) {
                    ans += map.get(-sum); // 累加所有可能的组合数
                }
            }
        }
        
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] nums1_1 = {1, 2};
        int[] nums2_1 = {-2, -1};
        int[] nums3_1 = {-1, 2};
        int[] nums4_1 = {0, 2};
        System.out.println("测试用例1:");
        System.out.println("nums1 = " + java.util.Arrays.toString(nums1_1));
        System.out.println("nums2 = " + java.util.Arrays.toString(nums2_1));
        System.out.println("nums3 = " + java.util.Arrays.toString(nums3_1));
        System.out.println("nums4 = " + java.util.Arrays.toString(nums4_1));
        System.out.println("输出: " + fourSumCount(nums1_1, nums2_1, nums3_1, nums4_1));
        System.out.println("期望: 2");
        System.out.println("满足条件的元组: (0,0,0,1), (1,1,0,0)");
        System.out.println();
        
        // 测试用例2：无匹配
        int[] nums1_2 = {0};
        int[] nums2_2 = {0};
        int[] nums3_2 = {0};
        int[] nums4_2 = {0};
        System.out.println("测试用例2 (全零):");
        System.out.println("nums1 = " + java.util.Arrays.toString(nums1_2));
        System.out.println("nums2 = " + java.util.Arrays.toString(nums2_2));
        System.out.println("nums3 = " + java.util.Arrays.toString(nums3_2));
        System.out.println("nums4 = " + java.util.Arrays.toString(nums4_2));
        System.out.println("输出: " + fourSumCount(nums1_2, nums2_2, nums3_2, nums4_2));
        System.out.println("期望: 1");
        System.out.println();
        
        // 测试用例3：较大数组
        int[] nums1_3 = {-1, -1};
        int[] nums2_3 = {-1, 1};
        int[] nums3_3 = {-1, 1};
        int[] nums4_3 = {1, -1};
        System.out.println("测试用例3 (多重组合):");
        System.out.println("nums1 = " + java.util.Arrays.toString(nums1_3));
        System.out.println("nums2 = " + java.util.Arrays.toString(nums2_3));
        System.out.println("nums3 = " + java.util.Arrays.toString(nums3_3));
        System.out.println("nums4 = " + java.util.Arrays.toString(nums4_3));
        System.out.println("输出: " + fourSumCount(nums1_3, nums2_3, nums3_3, nums4_3));
        System.out.println("期望: 6");
        System.out.println();
        
        // 测试用例4：无解
        int[] nums1_4 = {1, 1};
        int[] nums2_4 = {1, 1};
        int[] nums3_4 = {1, 1};
        int[] nums4_4 = {1, 1};
        System.out.println("测试用例4 (无解):");
        System.out.println("nums1 = " + java.util.Arrays.toString(nums1_4));
        System.out.println("nums2 = " + java.util.Arrays.toString(nums2_4));
        System.out.println("nums3 = " + java.util.Arrays.toString(nums3_4));
        System.out.println("nums4 = " + java.util.Arrays.toString(nums4_4));
        System.out.println("输出: " + fourSumCount(nums1_4, nums2_4, nums3_4, nums4_4));
        System.out.println("期望: 0 (所有元素都是正数，和不可能为0)");
        System.out.println();
        
        // 算法特点说明
        System.out.println("算法特点:");
        System.out.println("- 核心思想: 分组哈希，空间换时间");
        System.out.println("- 时间复杂度: O(n²) vs 暴力O(n⁴)");
        System.out.println("- 空间复杂度: O(n²) - 哈希表存储前两组的和");
        System.out.println("- 关键技巧: 将四元组问题分解为两个二元组问题");
        System.out.println("- 计数处理: 正确累加重复和值的组合数");
    }
}
