package leetc.top;

/**
 * LeetCode 287. 寻找重复数 (Find the Duplicate Number)
 * 
 * 问题描述：
 * 给定一个包含 n + 1 个整数的数组 nums ，其数字都在 [1, n] 范围内（包括 1 和 n），
 * 可知至少存在一个重复的整数。
 * 
 * 假设 nums 只有一个重复的整数 ，返回这个重复的数 。
 * 你设计的解决方案必须不修改数组 nums 且只用常量级 O(1) 的额外空间。
 * 
 * 示例：
 * 输入：nums = [1,3,4,2,2]
 * 输出：2
 * 
 * 输入：nums = [3,1,3,4,2]
 * 输出：3
 * 
 * 解法思路：
 * Floyd判圈算法（快慢指针检测环）：
 * 
 * 1. 核心观察：
 *    - 将数组看作链表，nums[i]指向nums[nums[i]]
 *    - 由于存在重复数字，必然形成环
 *    - 重复的数字就是环的入口
 * 
 * 2. 算法步骤：
 *    - 第一阶段：用快慢指针检测环的存在
 *    - 第二阶段：找到环的入口点（重复数字）
 * 
 * 3. 数学原理：
 *    - 设环外距离为a，环内快慢指针相遇点距入口为b，环长为c
 *    - 相遇时：slow走了a+b，fast走了a+b+nc（n为fast在环内的圈数）
 *    - 由于fast速度是slow的2倍：2(a+b) = a+b+nc
 *    - 化简得：a = nc-b = (n-1)c + (c-b)
 *    - 从起点和相遇点同时出发，速度相同，会在入口处相遇
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * LeetCode链接：https://leetcode.com/problems/find-the-duplicate-number/
 */
public class P287_FindTheDuplicateNumber {
    
    /**
     * 使用Floyd判圈算法寻找重复数
     * 
     * @param nums 包含重复数的数组
     * @return 重复的数字
     */
    public static int findDuplicate(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length < 2) {
            return -1;
        }
        
        // 第一阶段：快慢指针检测环
        // slow指针每次移动一步，fast指针每次移动两步
        int slow = nums[0];           // 慢指针从起点开始
        int fast = nums[nums[0]];     // 快指针从起点的下一步开始
        
        // 寻找快慢指针的第一次相遇点
        while (slow != fast) {
            slow = nums[slow];        // 慢指针移动一步
            fast = nums[nums[fast]];  // 快指针移动两步
        }
        
        // 第二阶段：寻找环的入口点
        // 从起点和相遇点同时出发，速度相同，相遇处即为环入口
        fast = 0;  // 重置快指针到起点
        while (slow != fast) {
            fast = nums[fast];  // 从起点开始，每次移动一步
            slow = nums[slow];  // 从相遇点开始，每次移动一步
        }
        
        // 相遇点就是重复的数字（环的入口）
        return slow;
    }
}
