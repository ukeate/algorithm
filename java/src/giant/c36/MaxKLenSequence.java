package giant.c36;

import java.util.TreeSet;

/**
 * 长度为K的字典序最大子序列问题
 * 
 * 问题描述：
 * 给定一个字符串str和一个正数k，返回长度为k的所有子序列中字典序最大的子序列
 * 
 * 解题思路：
 * 使用贪心算法 + 单调栈的思想：
 * 1. 遍历字符串，维护一个单调递减的栈
 * 2. 当遇到更大的字符时，如果栈顶字符较小且后续字符足够补充到k个，则弹出栈顶
 * 3. 当剩余字符数量恰好等于所需字符数量时，直接添加所有剩余字符
 * 4. 最终取栈中前k个字符作为结果
 * 
 * 算法核心：
 * - 贪心策略：尽可能选择字典序大的字符，且保证能够选够k个字符
 * - 时间复杂度：O(n)，其中n是字符串长度
 * - 空间复杂度：O(n)，用于维护栈
 * 
 * 来源：腾讯面试题
 * 
 * @author Zhu Runqi
 */
public class MaxKLenSequence {
    
    /**
     * 贪心算法求长度为k的字典序最大子序列
     * 
     * @param s 输入字符串
     * @param k 子序列长度
     * @return 字典序最大的长度为k的子序列
     */
    public static String maxString(String s, int k) {
        if (k <= 0 || s.length() < k) {
            return "";
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        char[] stack = new char[n];  // 用数组模拟栈，存储候选字符
        int size = 0;  // 栈的实际大小
        
        for (int i = 0; i < n; i++) {
            // 贪心策略：如果当前字符比栈顶字符大，且后续字符足够补充到k个，则弹出栈顶
            while (size > 0 && stack[size - 1] < str[i] && size + n - i > k) {
                size--;
            }
            
            // 优化：如果栈中字符数 + 剩余字符数 = k，直接构造结果
            if (size + n - i == k) {
                return String.valueOf(stack, 0, size) + s.substring(i);
            }
            
            // 将当前字符加入栈
            stack[size++] = str[i];
        }
        
        // 返回栈中前k个字符
        return String.valueOf(stack, 0, k);
    }

    /**
     * 暴力递归方法（用于验证正确性）
     * 生成所有长度为k的子序列，然后找出字典序最大的
     */
    private static void process(int si, int pi, char[] str, char[] path, TreeSet<String> ans) {
        if (si == str.length) {
            if (pi == path.length) {
                ans.add(String.valueOf(path));
            }
        } else {
            // 不选择当前字符
            process(si + 1, pi, str, path, ans);
            // 选择当前字符（如果还有位置）
            if (pi < path.length) {
                path[pi] = str[si];
                process(si + 1, pi + 1, str, path, ans);
            }
        }
    }

    /**
     * 暴力方法：用于对比和验证
     */
    private static String sure(String str, int k) {
        if (k <= 0 || str.length() < k) {
            return "";
        }
        
        TreeSet<String> ans = new TreeSet<>();
        process(0, 0, str.toCharArray(), new char[k], ans);
        return ans.last();  // 返回字典序最大的
    }

    /**
     * 生成随机字符串用于测试
     */
    private static String randomStr(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (Math.random() * range) + 'a');
        }
        return String.valueOf(str);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 手动测试用例
        System.out.println("=== 手动测试用例 ===");
        
        // 测试用例1
        String test1 = "bdca";
        int k1 = 2;
        String result1 = maxString(test1, k1);
        System.out.println("字符串: " + test1 + ", k=" + k1);
        System.out.println("结果: " + result1 + " (期望: dc)");
        System.out.println("解释: 所有长度为2的子序列有bd,bc,ba,dc,da,ca，其中dc字典序最大");
        System.out.println();
        
        // 测试用例2
        String test2 = "abcxyz";
        int k2 = 3;
        String result2 = maxString(test2, k2);
        System.out.println("字符串: " + test2 + ", k=" + k2);
        System.out.println("结果: " + result2 + " (期望: xyz)");
        System.out.println("解释: xyz是所有长度为3的子序列中字典序最大的");
        System.out.println();
        
        // 测试用例3
        String test3 = "zyxabc";
        int k3 = 3;
        String result3 = maxString(test3, k3);
        System.out.println("字符串: " + test3 + ", k=" + k3);
        System.out.println("结果: " + result3 + " (期望: zyx)");
        System.out.println("解释: 前三个字符zyx本身就是字典序最大的");
        System.out.println();
        
        // 测试用例4：边界情况
        String test4 = "abc";
        int k4 = 3;
        String result4 = maxString(test4, k4);
        System.out.println("字符串: " + test4 + ", k=" + k4);
        System.out.println("结果: " + result4 + " (期望: abc)");
        System.out.println("解释: 当k等于字符串长度时，结果就是原字符串");
        System.out.println();
        
        // 随机测试验证正确性
        System.out.println("=== 随机测试验证 ===");
        int n = 12;
        int r = 5;
        int times = 10000;
        System.out.println("开始随机测试...");
        boolean passed = true;
        
        for (int i = 0; i < times; i++) {
            int len = (int) (Math.random() * (n + 1));
            String str = randomStr(len, r);
            int k = (int) (Math.random() * (str.length() + 1));
            String ans1 = maxString(str, k);
            String ans2 = sure(str, k);
            if (!ans1.equals(ans2)) {
                System.out.println("测试失败！");
                System.out.println("字符串: " + str + ", k=" + k);
                System.out.println("贪心算法结果: " + ans1);
                System.out.println("暴力算法结果: " + ans2);
                passed = false;
                break;
            }
        }
        
        if (passed) {
            System.out.println("所有 " + times + " 个随机测试用例通过！");
        }
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("贪心算法:");
        System.out.println("  时间复杂度: O(n)，其中n是字符串长度");
        System.out.println("  空间复杂度: O(n)，用于维护栈");
        System.out.println("暴力算法:");
        System.out.println("  时间复杂度: O(2^n * k)，需要枚举所有子序列");
        System.out.println("  空间复杂度: O(C(n,k) * k)，需要存储所有长度为k的子序列");
        
        System.out.println("\n=== 算法关键点 ===");
        System.out.println("1. 贪心策略：优先选择字典序大的字符");
        System.out.println("2. 约束条件：必须保证能选够k个字符");
        System.out.println("3. 优化技巧：当剩余字符数恰好等于所需字符数时直接构造结果");
        System.out.println("4. 数据结构：使用数组模拟栈，比真正的栈效率更高");
    }
}
