package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 17. 电话号码的字母组合 (Letter Combinations of a Phone Number)
 * 
 * 问题描述：
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。
 * 答案可以按任意顺序返回。
 * 
 * 给出数字到字母的映射如下（与电话按键相同）：
 * 2: ABC, 3: DEF, 4: GHI, 5: JKL, 6: MNO, 7: PQRS, 8: TUV, 9: WXYZ
 * 注意 1 不对应任何字母。
 * 
 * 示例：
 * 输入：digits = "23"
 * 输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
 * 
 * 解法思路：
 * 回溯算法（DFS深度优先搜索）：
 * 1. 对于每个数字，都有多个字母选择
 * 2. 需要枚举所有可能的组合
 * 3. 使用递归的方式，对每个位置尝试所有可能的字母
 * 4. 递归到最后一位时，将组合加入结果集
 * 
 * 算法步骤：
 * 1. 建立数字到字母的映射表
 * 2. 使用递归函数process(位置, 路径, 结果集)
 * 3. 递归边界：处理完所有数字时，添加当前路径到结果
 * 4. 递归过程：枚举当前数字对应的所有字母，递归处理下一位
 * 
 * 回溯特点：
 * - 选择：当前数字对应的字母选择
 * - 约束：每个位置只能选择一个字母
 * - 目标：生成完整的字母组合
 * 
 * 优化要点：
 * - 预先建立映射表，避免重复计算
 * - 使用字符数组作为路径，避免字符串拼接开销
 * - 空字符串直接返回空列表
 * 
 * 时间复杂度：O(3^m × 4^n) - m个对应3字母的数字，n个对应4字母的数字
 * 空间复杂度：O(3^m × 4^n) - 递归栈深度为数字长度，结果集大小
 * 
 * LeetCode链接：https://leetcode.com/problems/letter-combinations-of-a-phone-number/
 */
public class P17_LetterCombinationsOfAPhoneNumber {
    
    // 数字到字母的映射表（数字2-9对应索引0-7）
    public static char[][] phone = {
            {'a', 'b', 'c'},        // 2 -> 索引0
            {'d', 'e', 'f'},        // 3 -> 索引1
            {'g', 'h', 'i'},        // 4 -> 索引2
            {'j', 'k', 'l'},        // 5 -> 索引3
            {'m', 'n', 'o'},        // 6 -> 索引4
            {'p', 'q', 'r', 's'},   // 7 -> 索引5
            {'t', 'u', 'v'},        // 8 -> 索引6
            {'w', 'x', 'y', 'z'}    // 9 -> 索引7
    };

    /**
     * 递归生成字母组合
     * 
     * @param str 数字字符数组
     * @param idx 当前处理的位置
     * @param path 当前路径（字母组合）
     * @param ans 结果集
     */
    private static void process(char[] str, int idx, char[] path, List<String> ans) {
        if (idx == str.length) {
            // 递归边界：处理完所有数字，添加当前组合到结果
            ans.add(String.valueOf(path));
            return;
        }
        
        // 获取当前数字对应的字母选择
        char[] chooses = phone[str[idx] - '2'];  // '2'对应索引0
        
        // 尝试当前数字的每个字母选择
        for (char cur : chooses) {
            path[idx] = cur;  // 做选择
            process(str, idx + 1, path, ans);  // 递归处理下一位
            // 无需显式回溯，因为下次循环会覆盖path[idx]
        }
    }

    /**
     * 返回数字字符串的所有字母组合
     * 
     * @param digits 数字字符串（2-9）
     * @return 所有可能的字母组合
     */
    public static List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        
        // 边界条件：空字符串或null
        if (digits == null || digits.length() == 0) {
            return ans;
        }
        
        char[] str = digits.toCharArray();
        char[] path = new char[str.length];  // 路径数组，存储当前组合
        
        process(str, 0, path, ans);
        
        return ans;
    }
}
