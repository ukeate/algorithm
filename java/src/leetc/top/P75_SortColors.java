package leetc.top;

/**
 * LeetCode 75. 颜色分类 (Sort Colors)
 * 
 * 问题描述：
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组，原地对它们进行排序，
 * 使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 * 
 * 我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
 * 必须在不使用库函数的情况下解决这个问题。
 * 
 * 解法思路：
 * 荷兰国旗问题 - 三路快排的分区思想：
 * 1. 使用三个指针：less指向0区域的右边界，more指向2区域的左边界，idx是当前遍历位置
 * 2. 初始状态：less=-1（0区域为空），more=length（2区域为空），idx=0
 * 3. 遍历过程：
 *    - 如果nums[idx]==1：1已在正确位置，idx++
 *    - 如果nums[idx]==0：与less+1位置交换，扩展0区域，idx++
 *    - 如果nums[idx]==2：与more-1位置交换，扩展2区域，idx不变（因为交换来的元素未检查）
 * 
 * 核心思想：
 * 维护三个区域的边界，通过交换操作将元素放入正确区域
 * [0...less] 区域存放0，[less+1...more-1] 区域存放1，[more...n-1] 区域存放2
 * 
 * 时间复杂度：O(n) - 每个元素最多被访问一次
 * 空间复杂度：O(1) - 原地排序，只使用常数额外空间
 */
public class P75_SortColors {
    
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
     * 颜色分类的主算法 - 荷兰国旗问题
     * 
     * 算法原理：
     * 使用三个指针维护三个区域：
     * - [0, less]: 存放所有的0
     * - [less+1, more-1]: 存放所有的1  
     * - [more, n-1]: 存放所有的2
     * - [idx, more-1]: 待处理区域
     * 
     * @param nums 包含0、1、2的数组
     */
    public static void sortColors(int[] nums) {
        int less = -1;        // 0区域的右边界（指向最后一个0的位置）
        int more = nums.length; // 2区域的左边界（指向第一个2的位置）
        int idx = 0;          // 当前处理的位置
        
        // 当idx到达2区域的边界时结束
        while (idx < more) {
            if (nums[idx] == 1) {
                // 1已经在正确区域，直接移动到下一个位置
                idx++;
            } else if (nums[idx] == 0) {
                // 将0放入0区域：与less+1位置交换，然后扩展0区域
                swap(nums, idx++, ++less);
            } else { // nums[idx] == 2
                // 将2放入2区域：与more-1位置交换，然后扩展2区域
                // 注意：idx不移动，因为交换来的元素还需要检查
                swap(nums, idx, --more);
            }
        }
    }
}
