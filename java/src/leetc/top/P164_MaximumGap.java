package leetc.top;

/**
 * LeetCode 164. 最大间距 (Maximum Gap)
 * 
 * 问题描述：
 * 给定一个无序的数组 nums，返回数组在排序之后，相邻元素之间最大的差值。
 * 如果数组元素个数小于 2，则返回 0。
 * 请尝试在线性时间复杂度和空间复杂度的条件下解决此问题。
 * 
 * 解法思路：
 * 桶排序 + 抽屉原理：
 * 1. 如果用传统排序算法，时间复杂度至少O(nlogn)
 * 2. 利用抽屉原理：n个数字分布在n+1个桶中，必有空桶
 * 3. 最大间距不会出现在同一个桶内，只可能出现在不同桶之间
 * 4. 桶的大小设计：range/n，确保桶内最大间距小于等于答案
 * 
 * 算法步骤：
 * 1. 找到数组的最小值和最大值
 * 2. 计算桶的大小：(max-min)/n
 * 3. 将每个数字分配到对应的桶中
 * 4. 记录每个桶的最小值和最大值
 * 5. 遍历相邻的非空桶，计算最大间距
 * 
 * 桶的设计原理：
 * - 总范围：max - min
 * - 桶的数量：n + 1个
 * - 桶的大小：range / n
 * - 保证：桶内的最大间距 ≤ 桶的大小 < 最终答案
 * 
 * 抽屉原理应用：
 * - n个数字放入n+1个桶，至少有一个桶为空
 * - 最大间距必然跨越空桶，因此不在桶内
 * - 只需检查相邻非空桶之间的间距
 * 
 * 时间复杂度：O(n) - 线性时间
 * 空间复杂度：O(n) - 桶的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/maximum-gap/
 */
public class P164_MaximumGap {
    
    /**
     * 计算数字应该分配到哪个桶
     * 
     * @param num 要分配的数字
     * @param len 数组长度
     * @param min 数组最小值
     * @param max 数组最大值
     * @return 桶的索引
     */
    private static int bucket(int num, int len, int min, int max) {
        double range = (double) (max - min) / (double) len;  // 桶的大小
        double distance = (num - min);  // 距离最小值的距离
        return (int) (distance / range);  // 计算桶索引
    }

    /**
     * 找到排序数组中相邻元素的最大间距
     * 
     * @param nums 无序数组
     * @return 最大间距
     */
    public static int maximumGap(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        int len = nums.length;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        
        // 找到数组的最小值和最大值
        for (int i = 0; i < len; i++) {
            min = Math.min(min, nums[i]);
            max = Math.max(max, nums[i]);
        }
        
        // 特殊情况：所有数字相同
        if (min == max) {
            return 0;
        }
        
        // 创建 n+1 个桶
        boolean[] hasNum = new boolean[len + 1];  // 记录桶是否有数字
        int[] maxs = new int[len + 1];           // 记录桶的最大值
        int[] mins = new int[len + 1];           // 记录桶的最小值
        
        int bid = 0;  // 桶索引
        
        // 将每个数字分配到对应的桶中
        for (int i = 0; i < len; i++) {
            bid = bucket(nums[i], len, min, max);
            
            // 更新桶的最小值和最大值
            mins[bid] = hasNum[bid] ? Math.min(mins[bid], nums[i]) : nums[i];
            maxs[bid] = hasNum[bid] ? Math.max(maxs[bid], nums[i]) : nums[i];
            hasNum[bid] = true;
        }
        
        int res = 0;
        int lastMax = maxs[0];  // 上一个非空桶的最大值
        
        // 遍历所有桶，计算相邻非空桶之间的最大间距
        for (int i = 1; i <= len; i++) {
            if (hasNum[i]) {
                // 当前桶的最小值 - 上一个桶的最大值 = 间距
                res = Math.max(res, mins[i] - lastMax);
                lastMax = maxs[i];  // 更新上一个桶的最大值
            }
            // 跳过空桶，这正是最大间距可能出现的地方
        }
        
        return res;
    }
}
