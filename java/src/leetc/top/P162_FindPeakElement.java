package leetc.top;

/**
 * LeetCode 162. 寻找峰值 (Find Peak Element)
 * 
 * 问题描述：
 * 峰值元素是指其值严格大于左右相邻值的元素。
 * 给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回任何一个峰值所在位置即可。
 * 你可以假设 nums[-1] = nums[n] = -∞。
 * 你必须实现时间复杂度为 O(log n) 的算法。
 * 
 * 解法思路：
 * 二分查找：
 * 1. 题目保证边界外的元素为负无穷，这保证了峰值的存在
 * 2. 对于任意位置mid，比较nums[mid]与其邻居的大小关系
 * 3. 根据比较结果选择搜索方向：
 *    - 如果nums[mid-1] > nums[mid]，左侧必有峰值
 *    - 如果nums[mid] < nums[mid+1]，右侧必有峰值
 *    - 如果nums[mid]既大于左邻也大于右邻，mid就是峰值
 * 
 * 边界处理：
 * - 首先检查边界位置是否为峰值
 * - 如果nums[0] > nums[1]，则0是峰值
 * - 如果nums[n-1] > nums[n-2]，则n-1是峰值
 * 
 * 二分查找的正确性：
 * - 由于边界为负无穷，必然存在峰值
 * - 通过比较中间元素与邻居的关系，可以确定峰值的方向
 * - 每次能排除一半的搜索空间
 * 
 * 算法特点：
 * - 不需要找到全局最大值，只需找到局部峰值
 * - 利用数组的局部性质进行二分
 * - 保证O(log n)的时间复杂度
 * 
 * 时间复杂度：O(log n) - 二分查找
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/find-peak-element/
 */
public class P162_FindPeakElement {
    
    /**
     * 寻找峰值元素的索引
     * 
     * @param nums 整数数组
     * @return 峰值元素的索引
     */
    public static int findPeakElement(int[] nums) {
        int n = nums.length;
        
        // 边界情况：数组长度小于2
        if (n < 2) {
            return 0;
        }
        
        // 检查左边界是否为峰值
        if (nums[0] > nums[1]) {
            return 0;
        }
        
        // 检查右边界是否为峰值
        if (nums[n - 1] > nums[n - 2]) {
            return n - 1;
        }
        
        // 二分查找峰值（在中间区域）
        int l = 1, r = n - 2, m = 0;
        
        while (l < r) {
            m = (l + r) / 2;
            
            if (nums[m - 1] < nums[m] && nums[m] > nums[m + 1]) {
                // 找到峰值：mid既大于左邻也大于右邻
                return m;
            } else if (nums[m - 1] > nums[m]) {
                // 左邻居更大，峰值在左侧
                r = m - 1;
            } else {
                // 右邻居更大，峰值在右侧
                l = m + 1;
            }
        }
        
        return l;  // 搜索区间收缩到一个点时，该点就是峰值
    }
}
