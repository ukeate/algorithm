package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 763. 划分字母区间 (Partition Labels)
 * 
 * 问题描述：
 * 字符串 S 由小写字母组成。我们要把这个字符串划分为尽可能多的片段，
 * 同一字母最多出现在一个片段中。返回一个表示每个字符串片段的长度的列表。
 * 
 * 示例：
 * - 输入：S = "ababcbacadefegdehijhklij"
 * - 输出：[9,7,8]
 * - 解释：
 *   划分结果为 "ababcbaca", "defegde", "hijhklij"
 *   每个字母最多出现在一个片段中
 *   像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少
 * 
 * 解法思路：
 * 贪心算法 + 双指针：
 * 1. 预处理：记录每个字符在字符串中最后出现的位置
 * 2. 遍历字符串，维护当前片段的边界：
 *    - left：当前片段的起始位置
 *    - right：当前片段必须到达的最远位置
 * 3. 对于每个字符，更新right为该字符最后出现位置的最大值
 * 4. 当遍历到right位置时，可以确定一个片段
 * 
 * 核心思想：
 * - 一个片段的右边界由该片段内所有字符的最远位置决定
 * - 只有到达了最远位置，才能保证片段内的字符不会在后续出现
 * 
 * 时间复杂度：O(n) - 遍历字符串两次
 * 空间复杂度：O(1) - 使用固定大小的数组记录字符位置
 * 
 * LeetCode链接：https://leetcode.com/problems/partition-labels/
 */
public class P763_PartitionLabels {
    
    /**
     * 划分字母区间
     * 
     * 算法步骤：
     * 1. 第一次遍历：记录每个字符最后出现的位置
     * 2. 第二次遍历：
     *    - 维护当前片段的左右边界
     *    - 更新右边界为当前字符最远位置的最大值
     *    - 当到达右边界时，确定一个片段
     * 
     * @param s 输入字符串
     * @return 每个字符串片段的长度列表
     */
    public static List<Integer> partitionLabels(String s) {
        char[] str = s.toCharArray();
        
        // 记录每个字符最后出现的位置
        int[] far = new int[26];
        for (int i = 0; i < str.length; i++) {
            far[str[i] - 'a'] = i;
        }
        
        List<Integer> ans = new ArrayList<>();
        int left = 0;                      // 当前片段的起始位置
        int right = far[str[0] - 'a'];     // 当前片段必须到达的最远位置
        
        // 从第二个字符开始遍历
        for (int i = 1; i < str.length; i++) {
            if (i > right) {
                // 超过了当前片段的右边界，说明前一个片段结束
                ans.add(right - left + 1);  // 记录片段长度
                left = i;                    // 开始新片段
            }
            // 更新当前片段的右边界
            right = Math.max(right, far[str[i] - 'a']);
        }
        
        // 添加最后一个片段的长度
        ans.add(right - left + 1);
        return ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        String s1 = "ababcbacadefegdehijhklij";
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("输出: " + partitionLabels(s1));
        System.out.println("期望: [9, 7, 8]");
        System.out.println("片段: \"ababcbaca\", \"defegde\", \"hijhklij\"");
        System.out.println();
        
        // 测试用例2：每个字符都不同
        String s2 = "abcdef";
        System.out.println("输入: \"" + s2 + "\"");
        System.out.println("输出: " + partitionLabels(s2));
        System.out.println("期望: [1, 1, 1, 1, 1, 1]");
        System.out.println("片段: \"a\", \"b\", \"c\", \"d\", \"e\", \"f\"");
        System.out.println();
        
        // 测试用例3：所有字符都相同
        String s3 = "aaaa";
        System.out.println("输入: \"" + s3 + "\"");
        System.out.println("输出: " + partitionLabels(s3));
        System.out.println("期望: [4]");
        System.out.println("片段: \"aaaa\"");
        System.out.println();
        
        // 测试用例4：两个字符交替
        String s4 = "abab";
        System.out.println("输入: \"" + s4 + "\"");
        System.out.println("输出: " + partitionLabels(s4));
        System.out.println("期望: [4]");
        System.out.println("片段: \"abab\"");
        System.out.println();
        
        // 算法说明
        System.out.println("算法核心思想：");
        System.out.println("- 贪心策略：尽可能早地结束片段");
        System.out.println("- 片段边界：由片段内所有字符的最远位置决定");
        System.out.println("- 时间复杂度：O(n)，空间复杂度：O(1)");
    }
}
