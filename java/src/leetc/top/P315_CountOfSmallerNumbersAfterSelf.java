package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
 * 
 * 问题描述：
 * 给你一个整数数组 nums，按要求返回一个新数组 counts，
 * 其中 counts[i] 的值是右侧小于 nums[i] 的元素的数量。
 * 
 * 示例：
 * 输入：nums = [5,2,6,1]
 * 输出：[2,1,1,0]
 * 解释：5的右侧有2个更小的数字(2和1)，2的右侧有1个更小的数字(1)，等等
 * 
 * 解法思路：
 * 使用归并排序的思想，在合并过程中统计逆序对数量：
 * 1. 创建Node类保存原始值和索引位置
 * 2. 对数组按值进行归并排序
 * 3. 在merge过程中，如果左半部分的元素大于右半部分的元素，
 *    说明右半部分从当前位置到mid+1的所有元素都小于左半部分当前元素
 * 4. 累加计数到答案数组中
 * 
 * 核心技巧：
 * - 逆向归并：从右向左合并，便于计算右侧小于当前元素的个数
 * - 索引保存：Node类保存原始索引，确保结果正确映射回原数组
 * 
 * 时间复杂度：O(n log n) - 归并排序的标准复杂度
 * 空间复杂度：O(n) - 需要额外的辅助数组和Node数组
 */
public class P315_CountOfSmallerNumbersAfterSelf {
    /**
     * 节点类，用于保存数组元素的值和原始索引
     */
    public static class Node {
        public int val; // 元素值
        public int idx; // 原始索引位置
        
        public Node(int v, int i) {
            val = v;
            idx = i;
        }
    }

    /**
     * 归并过程，统计右侧小于当前元素的个数
     * 
     * @param arr 节点数组
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @param ans 结果列表
     */
    private static void merge(Node[] arr, int l, int m, int r, List<Integer> ans) {
        Node[] help = new Node[r - l + 1];
        int i = help.length - 1;
        int p1 = m, p2 = r; // 从右向左遍历两个有序部分
        
        while (p1 >= l && p2 >= m + 1) {
            if (arr[p1].val > arr[p2].val) {
                // 左半部分当前元素大于右半部分当前元素
                // 右半部分从m+1到p2的所有元素都小于左半部分当前元素
                ans.set(arr[p1].idx, ans.get(arr[p1].idx) + p2 - m);
            }
            help[i--] = arr[p1].val > arr[p2].val ? arr[p1--] : arr[p2--];
        }
        
        // 处理剩余元素
        while (p1 >= l) {
            help[i--] = arr[p1--];
        }
        while (p2 >= m + 1) {
            help[i--] = arr[p2--];
        }
        
        // 将排序结果写回原数组
        for (i = 0; i < help.length; i++) {
            arr[l + i] = help[i];
        }
    }

    /**
     * 归并排序递归过程
     * 
     * @param arr 节点数组
     * @param l 左边界
     * @param r 右边界
     * @param ans 结果列表
     */
    private static void process(Node[] arr, int l, int r, List<Integer> ans) {
        if (l == r) {
            return; // 单个元素，无需处理
        }
        
        int mid = l + ((r - l) >> 1);
        process(arr, l, mid, ans);         // 递归处理左半部分
        process(arr, mid + 1, r, ans);     // 递归处理右半部分
        merge(arr, l, mid, r, ans);        // 合并两部分并统计
    }

    /**
     * 计算右侧小于当前元素的个数主方法
     * 
     * @param nums 输入的整数数组
     * @return 每个位置右侧小于当前元素的个数列表
     */
    public static List<Integer> countSmaller(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        if (nums == null) {
            return ans;
        }
        
        // 初始化结果数组，所有位置初始为0
        for (int i = 0; i < nums.length; i++) {
            ans.add(0);
        }
        
        if (nums.length < 2) {
            return ans; // 少于2个元素无需比较
        }
        
        // 创建Node数组，保存值和原始索引
        Node[] arr = new Node[nums.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Node(nums[i], i);
        }
        
        // 开始归并排序统计过程
        process(arr, 0, arr.length - 1, ans);
        return ans;
    }
}
