package leetc.top;

/**
 * LeetCode 42. 接雨水 (Trapping Rain Water)
 * 
 * 问题描述：
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能够接多少雨水。
 * 
 * 解法思路：
 * 双指针法 - 最优解法：
 * 1. 使用左右两个指针分别从数组两端向中间移动
 * 2. 维护leftMax和rightMax分别记录左侧和右侧遇到的最大高度
 * 3. 对于当前位置，能接的雨水量 = min(leftMax, rightMax) - 当前高度
 * 4. 关键观察：如果leftMax < rightMax，那么左指针位置的接水量只取决于leftMax
 * 
 * 核心思想：
 * - 某个位置能接多少雨水，取决于其左右两侧的最大高度的较小值
 * - 双指针法巧妙地避免了预处理左右最大值数组，一次遍历即可完成
 * 
 * 算法正确性：
 * 当leftMax <= rightMax时，左指针位置的接水量确定为max(0, leftMax - arr[l])
 * 因为即使右侧还有更高的柱子，接水量也会被leftMax限制
 * 
 * 时间复杂度：O(n) - 每个元素最多被访问一次
 * 空间复杂度：O(1) - 只使用常数个额外变量
 */
public class P42_TrappingRainWater {
    
    /**
     * 接雨水核心算法 - 双指针法
     * 
     * @param arr 表示柱子高度的数组
     * @return 能够接到的雨水总量
     */
    public static int trap(int[] arr) {
        // 边界条件：少于2个柱子无法接雨水
        if (arr == null || arr.length < 2) {
            return 0;
        }
        
        int n = arr.length;
        int l = 1;               // 左指针，从第二个元素开始
        int leftMax = arr[0];    // 左侧遇到的最大高度
        int r = n - 2;           // 右指针，从倒数第二个元素开始  
        int rightMax = arr[n - 1]; // 右侧遇到的最大高度
        int water = 0;           // 接到的雨水总量
        
        // 双指针向中间移动
        while (l <= r) {
            if (leftMax <= rightMax) {
                // 左侧最大值较小，左指针位置的接水量由leftMax决定
                water += Math.max(0, leftMax - arr[l]);
                leftMax = Math.max(leftMax, arr[l++]);
            } else {
                // 右侧最大值较小，右指针位置的接水量由rightMax决定
                water += Math.max(0, rightMax - arr[r]);
                rightMax = Math.max(rightMax, arr[r--]);
            }
        }
        
        return water;
    }
}
