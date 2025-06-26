package leetc.top;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * LeetCode 78. 子集 (Subsets)
 * 
 * 问题描述：
 * 给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。你可以按任意顺序返回解集。
 * 
 * 示例：
 * - 输入：nums = [1,2,3]
 * - 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * 
 * 解法思路：
 * 回溯算法 (Backtracking)：
 * 1. 对于每个元素，都有两种选择：包含在当前子集中或不包含
 * 2. 递归地对每个位置进行选择，当处理完所有元素时，得到一个完整的子集
 * 3. 使用路径 path 记录当前选择的元素，答案列表 ans 收集所有子集
 * 
 * 核心思想：
 * - 二选一递归：对于位置 idx，先不选当前元素，再选择当前元素
 * - 状态回溯：选择元素后要撤销选择，保持状态一致性
 * 
 * 时间复杂度：O(2^n) - 每个元素都有选择/不选择两种状态
 * 空间复杂度：O(n) - 递归栈的深度为n，path的最大长度为n
 * 
 * LeetCode链接：https://leetcode.com/problems/subsets/
 */
public class P78_Subsets {
    
    /**
     * 复制路径到新的ArrayList
     * 
     * @param path 当前选择路径
     * @return 路径的副本
     */
    private static ArrayList<Integer> copy(LinkedList<Integer> path) {
        ArrayList<Integer> ans = new ArrayList<>();
        for (Integer num : path) {
            ans.add(num);
        }
        return ans;
    }

    /**
     * 递归生成所有子集的核心方法
     * 
     * 算法流程：
     * 1. 递归终止：当idx到达数组末尾时，将当前路径添加到结果中
     * 2. 不选择当前元素：直接递归处理下一个位置
     * 3. 选择当前元素：添加到路径中，递归处理下一个位置，然后回溯
     * 
     * @param nums 输入数组
     * @param idx 当前处理的索引位置
     * @param path 当前选择的元素路径
     * @param ans 所有子集的结果列表
     */
    private static void process(int nums[], int idx, LinkedList<Integer> path, List<List<Integer>> ans) {
        // 递归终止条件：处理完所有元素
        if (idx == nums.length) {
            ans.add(copy(path));  // 将当前路径的副本添加到结果中
            return;
        }
        
        // 不选择当前元素nums[idx]，直接递归下一个位置
        process(nums, idx + 1, path, ans);
        
        // 选择当前元素nums[idx]
        path.addLast(nums[idx]);           // 1. 做出选择
        process(nums, idx + 1, path, ans); // 2. 递归处理
        path.removeLast();                 // 3. 撤销选择（回溯）
    }

    /**
     * 生成数组的所有子集
     * 
     * @param nums 输入的整数数组，元素互不相同
     * @return 所有可能的子集列表
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        process(nums, 0, path, ans);
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P78_Subsets solution = new P78_Subsets();
        
        // 测试用例1：[1,2,3]
        int[] nums1 = {1, 2, 3};
        System.out.println("输入: [1,2,3]");
        System.out.println("输出: " + solution.subsets(nums1));
        System.out.println("期望: [[], [1], [2], [1, 2], [3], [1, 3], [2, 3], [1, 2, 3]]");
        System.out.println();
        
        // 测试用例2：[0]
        int[] nums2 = {0};
        System.out.println("输入: [0]");
        System.out.println("输出: " + solution.subsets(nums2));
        System.out.println("期望: [[], [0]]");
        System.out.println();
        
        // 测试用例3：空数组
        int[] nums3 = {};
        System.out.println("输入: []");
        System.out.println("输出: " + solution.subsets(nums3));
        System.out.println("期望: [[]]");
        System.out.println();
        
        // 性能测试
        int[] nums4 = {1, 2, 3, 4, 5};
        long start = System.currentTimeMillis();
        List<List<Integer>> result = solution.subsets(nums4);
        long end = System.currentTimeMillis();
        System.out.println("输入: [1,2,3,4,5]");
        System.out.println("子集数量: " + result.size());
        System.out.println("期望数量: " + (1 << nums4.length) + " (2^" + nums4.length + ")");
        System.out.println("执行时间: " + (end - start) + "ms");
    }
}
