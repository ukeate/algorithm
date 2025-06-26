package leetc.top;

import java.util.Arrays;
import java.util.Comparator;

/**
 * LeetCode 179. 最大数 (Largest Number)
 * 
 * 问题描述：
 * 给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）
 * 使之组成一个最大的整数。
 * 注意：输出结果可能非常大，所以你需要返回一个字符串而不是整数。
 * 
 * 示例：
 * 输入：nums = [10,2]        输出："210"
 * 输入：nums = [3,30,34,5,9] 输出："9534330"
 * 输入：nums = [0,0]         输出："0"
 * 
 * 解法思路：
 * 自定义排序 + 贪心策略：
 * 1. 问题核心：如何决定两个数字的排列顺序？
 * 2. 对于数字a和b，比较"ab"和"ba"的大小
 * 3. 如果"ab" > "ba"，则a应该排在b前面
 * 4. 如果"ab" < "ba"，则b应该排在a前面
 * 
 * 排序规则的正确性：
 * - 自定义比较器保证了传递性
 * - 如果a > b且b > c（按自定义规则），则a > c
 * - 这保证了排序结果的最优性
 * 
 * 算法步骤：
 * 1. 将所有数字转换为字符串
 * 2. 使用自定义比较器排序：(a, b) -> (b+a).compareTo(a+b)
 * 3. 将排序后的字符串拼接
 * 4. 处理特殊情况：结果为全0时返回"0"
 * 
 * 贪心策略的证明：
 * - 局部最优：每次选择能使当前位置最大的数字
 * - 全局最优：自定义排序保证了全局最优解
 * - 传递性保证排序的一致性
 * 
 * 边界情况：
 * - 所有数字都是0：返回"0"而不是"000..."
 * - 空数组：返回空字符串
 * - 单个数字：直接返回该数字的字符串
 * 
 * 关键洞察：
 * - 字符串比较的字典序正好符合数字大小比较
 * - "ab" vs "ba"的比较决定了a和b的相对顺序
 * - 贪心选择的局部最优性导致全局最优
 * 
 * 时间复杂度：O(n log n) - 排序的时间复杂度
 * 空间复杂度：O(n) - 存储字符串数组的空间
 * 
 * LeetCode链接：https://leetcode.com/problems/largest-number/
 */
public class P179_LargestNumber {
    
    /**
     * 自定义比较器：按照拼接后的大小进行比较
     * 如果o2+o1 > o1+o2，则o2应该排在o1前面
     */
    public static class Comp implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            // 比较 o2+o1 和 o1+o2 的字典序
            // 返回值 > 0 表示 o2+o1 > o1+o2，即o2应该排在前面
            return (o2 + o1).compareTo(o1 + o2);
        }
    }

    /**
     * 重新排列数组形成最大整数
     * 
     * @param nums 非负整数数组
     * @return 最大整数的字符串表示
     */
    public String largestNumber(int[] nums) {
        // 将数字转换为字符串数组
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        
        // 使用自定义比较器排序
        Arrays.sort(strs, new Comp());
        
        // 拼接排序后的字符串
        StringBuilder builder = new StringBuilder();
        for (String str : strs) {
            builder.append(str);
        }
        
        String ans = builder.toString();
        
        // 处理特殊情况：去除前导零
        char[] str = ans.toCharArray();
        int idx = -1;
        
        // 找到第一个非0字符的位置
        for (int i = 0; i < str.length; i++) {
            if (str[i] != '0') {
                idx = i;
                break;
            }
        }
        
        // 如果全是0，返回"0"；否则返回从第一个非0字符开始的子串
        return idx == -1 ? "0" : ans.substring(idx);
    }
}
