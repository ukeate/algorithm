package leetc.top;

/**
 * LeetCode 31. 下一个排列 (Next Permutation)
 * 
 * 问题描述：
 * 实现获取下一个排列的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 * 必须原地修改，只允许使用额外常数空间。
 * 
 * 示例：
 * - 输入：[1,2,3] → 输出：[1,3,2]
 * - 输入：[3,2,1] → 输出：[1,2,3]
 * - 输入：[1,1,5] → 输出：[1,5,1]
 * 
 * 解法思路：
 * 字典序算法（经典算法）：
 * 1. 从右往左找第一个升序位置（nums[i] < nums[i+1]）
 * 2. 如果找不到，说明当前是最大排列，直接反转整个数组
 * 3. 如果找到了，从右往左找第一个比nums[i]大的数nums[j]
 * 4. 交换nums[i]和nums[j]
 * 5. 反转nums[i+1...]部分，使其变为升序（最小排列）
 * 
 * 算法正确性：
 * - 步骤1确保找到需要"进位"的位置
 * - 步骤3确保找到最小的"进位"候选
 * - 步骤5确保后续部分是最小排列
 * 
 * 时间复杂度：O(n) - 最多遍历数组3次
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/next-permutation/
 */
public class P31_NextPermutation {
    
    /**
     * 交换数组中两个位置的元素
     * 
     * @param nums 数组
     * @param i 第一个位置
     * @param j 第二个位置
     */
    private static void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    /**
     * 反转数组指定范围内的元素
     * 
     * @param nums 数组
     * @param l 左边界（包含）
     * @param r 右边界（包含）
     */
    private static void reverse(int[] nums, int l, int r) {
        while (l < r) {
            swap(nums, l++, r--);
        }
    }

    /**
     * 获取下一个排列
     * 
     * 算法步骤详解：
     * 1. 从右往左找第一个"降序点"：nums[i] < nums[i+1]
     *    - 这个位置是需要"增大"的位置
     *    - 如果整个数组都是倒序，则不存在下一个排列
     * 
     * 2. 从右往左找第一个比nums[i]大的数：
     *    - 这个数是最小的可以"进位"的候选
     *    - 由于右侧是倒序，第一个找到的就是最小的
     * 
     * 3. 交换两个数：
     *    - 实现"进位"操作
     * 
     * 4. 反转i+1到末尾的部分：
     *    - 使得后续部分变为最小排列
     *    - 保证结果是"下一个"排列而不是"更大的"排列
     * 
     * 示例分析：[1,5,4,3,2] → [2,1,3,4,5]
     * 1. 找降序点：i=0（nums[0]=1 < nums[1]=5）
     * 2. 找进位候选：j=4（nums[4]=2是第一个>nums[0]的）
     * 3. 交换：[2,5,4,3,1]
     * 4. 反转[1:]：[2,1,3,4,5]
     * 
     * @param nums 待处理的数组（原地修改）
     */
    public static void nextPermutation(int[] nums) {
        int n = nums.length;
        
        // 步骤1：从右往左找第一个升序位置（降序点）
        int firstLess = -1;  // 需要"增大"的位置
        for (int i = n - 2; i >= 0; i--) {
            if (nums[i] < nums[i + 1]) {
                firstLess = i;
                break;
            }
        }
        
        // 如果没有找到降序点，说明当前是最大排列
        if (firstLess < 0) {
            reverse(nums, 0, n - 1);  // 反转整个数组，得到最小排列
            return;
        }
        
        // 步骤2：从右往左找第一个比nums[firstLess]大的数
        int rightClosestMore = -1;  // 最小的进位候选
        for (int i = n - 1; i > firstLess; i--) {
            if (nums[i] > nums[firstLess]) {
                rightClosestMore = i;
                break;  // 第一个找到的就是最小的（因为右侧是倒序）
            }
        }
        
        // 步骤3：交换两个关键位置的数字
        swap(nums, firstLess, rightClosestMore);
        
        // 步骤4：反转firstLess+1到末尾的部分，使其变为升序
        reverse(nums, firstLess + 1, n - 1);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：[1,2,3] → [1,3,2]
        int[] nums1 = {1, 2, 3};
        System.out.print("输入: [1,2,3] → ");
        nextPermutation(nums1);
        System.out.println("输出: " + java.util.Arrays.toString(nums1));
        System.out.println("期望: [1,3,2]");
        System.out.println();
        
        // 测试用例2：[3,2,1] → [1,2,3]
        int[] nums2 = {3, 2, 1};
        System.out.print("输入: [3,2,1] → ");
        nextPermutation(nums2);
        System.out.println("输出: " + java.util.Arrays.toString(nums2));
        System.out.println("期望: [1,2,3]");
        System.out.println();
        
        // 测试用例3：[1,1,5] → [1,5,1]
        int[] nums3 = {1, 1, 5};
        System.out.print("输入: [1,1,5] → ");
        nextPermutation(nums3);
        System.out.println("输出: " + java.util.Arrays.toString(nums3));
        System.out.println("期望: [1,5,1]");
        System.out.println();
        
        // 测试用例4：[1,5,4,3,2] → [2,1,3,4,5]
        int[] nums4 = {1, 5, 4, 3, 2};
        System.out.print("输入: [1,5,4,3,2] → ");
        nextPermutation(nums4);
        System.out.println("输出: " + java.util.Arrays.toString(nums4));
        System.out.println("期望: [2,1,3,4,5]");
        System.out.println();
        
        System.out.println("算法特点：");
        System.out.println("- 时间复杂度：O(n)，最多遍历数组3次");
        System.out.println("- 空间复杂度：O(1)，只使用常数额外空间");
        System.out.println("- 核心思想：字典序 + 贪心选择");
        System.out.println("- 适用场景：排列生成、字典序问题");
    }
}
