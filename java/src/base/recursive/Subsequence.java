package base.recursive;

import java.util.ArrayList;
import java.util.List;

/**
 * 字符串子序列生成 - 递归生成所有可能的子序列
 * 
 * 问题描述：
 * 给定一个字符串，生成该字符串的所有子序列（subsequence）
 * 子序列是指在不改变字符相对顺序的前提下，删除某些字符得到的序列
 * 
 * 例如：字符串"abc"的所有子序列为：
 * "", "a", "b", "c", "ab", "ac", "bc", "abc"
 * 
 * 算法思想：
 * 对于每个字符，我们有两种选择：
 * 1. 不选择这个字符
 * 2. 选择这个字符
 * 
 * 这是一个典型的二叉递归问题，每个字符都有"选"和"不选"两种状态
 * 
 * 数学分析：
 * - 长度为n的字符串有2^n个子序列（包括空序列）
 * - 时间复杂度：O(2^n)，需要生成2^n个子序列
 * - 空间复杂度：O(n×2^n)，存储所有子序列的空间
 * 
 * 应用场景：
 * - 组合数学问题
 * - 动态规划问题的暴力解法
 * - 回溯算法的基础
 * - 集合的幂集生成
 */
public class Subsequence {
    /**
     * 递归生成子序列的核心函数
     * 
     * 算法思路：
     * 1. 如果已经处理完所有字符，将当前路径加入结果集
     * 2. 否则，对当前字符做两种选择：
     *    - 不选择当前字符，直接递归下一个位置
     *    - 选择当前字符，将其加入路径后递归下一个位置
     * 
     * 递归树结构（以"abc"为例）：
     *                   ""
     *                 /    \
     *               ""      "a"
     *             /  \     /    \
     *           ""   "b"  "a"   "ab"
     *          / \   / \  / \   / \
     *        ""  "c" "b" "bc" "a" "ac" "ab" "abc"
     * 
     * 每个叶子节点就是一个子序列
     * 
     * @param str 字符数组
     * @param idx 当前处理的字符索引
     * @param ans 结果集合
     * @param path 当前构建的子序列
     */
    private static void process1(char[] str, int idx, List<String> ans, String path) {
        if (idx == str.length) {
            // 递归边界：已经处理完所有字符，将当前路径加入结果
            ans.add(path);
            return;
        }
        
        // 选择1：不选择当前字符str[idx]
        // 路径保持不变，直接递归处理下一个字符
        process1(str, idx + 1, ans, path);
        
        // 选择2：选择当前字符str[idx]
        // 将当前字符加入路径，然后递归处理下一个字符
        process1(str, idx + 1, ans, path + str[idx]);
    }
    
    /**
     * 生成字符串所有子序列的主函数
     * 
     * @param s 输入字符串
     * @return 包含所有子序列的列表
     */
    public static List<String> subs(String s) {
        char[] str = s.toCharArray();       // 转换为字符数组便于处理
        String path = "";                   // 初始路径为空
        List<String> ans = new ArrayList<>();  // 结果集合
        process1(str, 0, ans, path);        // 开始递归生成
        return ans;
    }

    /**
     * 测试方法 - 演示子序列生成算法
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串子序列生成演示 ===");
        
        // 测试用例1：简单字符串
        String test1 = "abc";
        System.out.println("1. 字符串 \"" + test1 + "\" 的所有子序列：");
        List<String> result1 = subs(test1);
        System.out.println("子序列数量：" + result1.size() + "（期望：" + Math.pow(2, test1.length()) + "）");
        System.out.println("所有子序列：");
        for (int i = 0; i < result1.size(); i++) {
            String subseq = result1.get(i);
            if (subseq.isEmpty()) {
                System.out.println("  " + (i + 1) + ". \"\" (空序列)");
            } else {
                System.out.println("  " + (i + 1) + ". \"" + subseq + "\"");
            }
        }
        
        // 测试用例2：有重复字符的字符串
        String test2 = "aab";
        System.out.println("\n2. 字符串 \"" + test2 + "\" 的所有子序列：");
        List<String> result2 = subs(test2);
        System.out.println("子序列数量：" + result2.size());
        System.out.println("所有子序列：");
        for (int i = 0; i < result2.size(); i++) {
            String subseq = result2.get(i);
            if (subseq.isEmpty()) {
                System.out.println("  " + (i + 1) + ". \"\" (空序列)");
            } else {
                System.out.println("  " + (i + 1) + ". \"" + subseq + "\"");
            }
        }
        
        // 测试用例3：单字符
        String test3 = "x";
        System.out.println("\n3. 字符串 \"" + test3 + "\" 的所有子序列：");
        List<String> result3 = subs(test3);
        System.out.println("子序列：" + result3);
        
        // 测试用例4：空字符串
        String test4 = "";
        System.out.println("\n4. 空字符串的子序列：");
        List<String> result4 = subs(test4);
        System.out.println("子序列：" + result4);
        
        // 复杂度分析
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("字符串长度\t子序列数量\t时间复杂度");
        for (int n = 0; n <= 6; n++) {
            long count = (long) Math.pow(2, n);
            String complexity = n <= 4 ? "很快" : (n <= 6 ? "尚可" : "较慢");
            System.out.printf("%d\t\t%d\t\t%s\n", n, count, complexity);
        }
        
        System.out.println("\n核心思想：");
        System.out.println("- 每个字符都有'选'和'不选'两种状态");
        System.out.println("- 用递归枚举所有可能的选择组合");
        System.out.println("- 这是组合数学中生成幂集的经典方法");
        
        System.out.println("\n注意事项：");
        System.out.println("- 子序列保持字符的相对顺序");
        System.out.println("- 时间复杂度为O(2^n)，不适合长字符串");
        System.out.println("- 可以用位运算优化，但本质复杂度不变");
    }
}
