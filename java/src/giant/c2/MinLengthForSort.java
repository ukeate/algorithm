package giant.c2;

/**
 * 最短无序连续子数组问题
 * LeetCode 581: https://leetcode.com/problems/shortest-unsorted-continuous-subarray/
 * 
 * 问题描述：
 * 给定一个整数数组，需要寻找一个最短的连续子数组，如果对这个子数组进行升序排序，
 * 那么整个数组都会变为升序排序。返回这个子数组的长度。
 * 
 * 例如：
 * 输入: [2, 6, 4, 8, 10, 9, 15]
 * 输出: 5
 * 解释: 需要对 [6, 4, 8, 10, 9] 进行排序，它们是从索引1到5的子数组
 * 
 * 算法思路：
 * 1. 从左到右遍历，找到最后一个"错位"的位置（应该小但却大的位置）
 * 2. 从右到左遍历，找到最前一个"错位"的位置（应该大但却小的位置）
 * 3. 这两个位置之间的子数组就是需要排序的最短子数组
 * 
 * 核心洞察：
 * - 如果数组已经完全有序，不存在错位，返回0
 * - 错位的判断标准：当前位置的值比之前遇到的最大值小（左到右）
 *   或当前位置的值比之后遇到的最小值大（右到左）
 * 
 * 时间复杂度：O(n)，两次遍历
 * 空间复杂度：O(1)，只使用常数额外空间
 * 
 * @author algorithm learning
 */
public class MinLengthForSort {
    
    /**
     * 寻找最短无序连续子数组的长度
     * 
     * 算法步骤：
     * 1. 第一次遍历（从左到右）：
     *    - 维护到目前为止的最大值
     *    - 如果当前元素小于最大值，说明当前位置"错位"了
     *    - 记录最后一个错位的位置作为右边界
     * 
     * 2. 第二次遍历（从右到左）：
     *    - 维护到目前为止的最小值
     *    - 如果当前元素大于最小值，说明当前位置"错位"了  
     *    - 记录最后一个错位的位置作为左边界
     * 
     * 3. 计算子数组长度：right - left + 1
     * 
     * @param nums 输入数组
     * @return 最短无序连续子数组的长度
     */
    public static int min(int[] nums) {
        // 边界条件：数组为空或长度小于2
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        int n = nums.length;
        int right = -1;                    // 需要排序的右边界（最后一个错位位置）
        int max = Integer.MIN_VALUE;       // 从左到右遍历过程中的最大值
        
        // 第一次遍历：从左到右找到最后一个错位的位置
        for (int i = 0; i < n; i++) {
            if (max > nums[i]) {
                // 当前元素小于前面的最大值，说明当前位置错位了
                right = i;
            }
            // 更新最大值
            max = Math.max(max, nums[i]);
        }
        
        int min = Integer.MAX_VALUE;       // 从右到左遍历过程中的最小值
        int left = n;                      // 需要排序的左边界（最前一个错位位置）
        
        // 第二次遍历：从右到左找到最前一个错位的位置
        for (int i = n - 1; i >= 0; i--) {
            if (min < nums[i]) {
                // 当前元素大于后面的最小值，说明当前位置错位了
                left = i;
            }
            // 更新最小值
            min = Math.min(min, nums[i]);
        }
        
        // 计算需要排序的子数组长度
        // 如果right = -1，说明数组已经有序，返回0
        return Math.max(0, right - left + 1);
    }
    
    /**
     * 测试方法：验证算法正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 最短无序连续子数组测试 ===");
        
        // 测试用例1：标准情况
        int[] nums1 = {2, 6, 4, 8, 10, 9, 15};
        System.out.println("测试用例1: " + java.util.Arrays.toString(nums1));
        System.out.println("最短无序子数组长度: " + min(nums1));
        System.out.println("期望结果: 5");
        System.out.println();
        
        // 测试用例2：已经有序的数组
        int[] nums2 = {1, 2, 3, 4, 5};
        System.out.println("测试用例2: " + java.util.Arrays.toString(nums2));
        System.out.println("最短无序子数组长度: " + min(nums2));
        System.out.println("期望结果: 0");
        System.out.println();
        
        // 测试用例3：完全逆序的数组
        int[] nums3 = {5, 4, 3, 2, 1};
        System.out.println("测试用例3: " + java.util.Arrays.toString(nums3));
        System.out.println("最短无序子数组长度: " + min(nums3));
        System.out.println("期望结果: 5");
        System.out.println();
        
        // 测试用例4：单个元素
        int[] nums4 = {1};
        System.out.println("测试用例4: " + java.util.Arrays.toString(nums4));
        System.out.println("最短无序子数组长度: " + min(nums4));
        System.out.println("期望结果: 0");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
