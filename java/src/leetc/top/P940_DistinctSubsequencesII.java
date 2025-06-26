package leetc.top;

/**
 * LeetCode 940. 不同的子序列 II (Distinct Subsequences II)
 * 
 * 问题描述：
 * 给定一个字符串 s，计算 s 的不同非空子序列的个数。
 * 由于结果可能很大，所以返回答案需要模 10^9 + 7。
 * 
 * 字符串的子序列是指，通过删除一些（也可以不删除）字符且不干扰剩余字符相对位置所组成的新字符串。
 * 例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是。
 * 
 * 示例：
 * - 输入：s = "abc"
 * - 输出：7
 * - 解释：7个不同的子序列分别是 "a", "b", "c", "ab", "ac", "bc", "abc"
 * 
 * 解法思路：
 * 动态规划 + 去重：
 * 1. 维护每个字符的子序列数量count[c]
 * 2. 维护总的子序列数量all（包括空集）
 * 3. 对于新字符c，新增的子序列数量 = all - count[c]
 * 4. 更新count[c]和all，实现增量计算和自动去重
 * 
 * 核心思想：
 * - 每个新字符可以与之前所有子序列组合形成新子序列
 * - 通过减去该字符之前贡献的数量来避免重复计算
 * 
 * 时间复杂度：O(n) - 遍历字符串一次
 * 空间复杂度：O(1) - 只使用固定大小的字符计数数组
 * 
 * LeetCode链接：https://leetcode.com/problems/distinct-subsequences-ii/
 */
public class P940_DistinctSubsequencesII {
    
    /**
     * 计算不同子序列的个数
     * 
     * 算法步骤：
     * 1. 初始化：all = 1（表示空集），count数组全为0
     * 2. 对于每个字符c：
     *    - 计算新增子序列数：add = all - count[c]
     *    - 更新该字符的贡献：count[c] += add
     *    - 更新总数：all += add
     * 3. 返回all - 1（排除空集）
     * 
     * 为什么这样可以去重：
     * - count[c]记录了字符c历史上贡献的所有子序列
     * - all - count[c]就是不包含当前字符c的所有子序列
     * - 将这些子序列与当前字符c组合，产生新的不重复子序列
     * 
     * @param s 输入字符串
     * @return 不同非空子序列的个数
     */
    public static int distinctSubseqII(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        long m = 1000000007;  // 模数
        char[] str = s.toCharArray();
        long[] count = new long[26];  // count[i]表示字符(i+'a')贡献的子序列数量
        long all = 1;  // 所有子序列数量（包括空集）
        
        for (char x : str) {
            // 计算新增的子序列数量：当前所有子序列 - 该字符之前贡献的数量
            long add = (all - count[x - 'a'] + m) % m;
            
            // 更新该字符的贡献量
            count[x - 'a'] = (count[x - 'a'] + add) % m;
            
            // 更新总的子序列数量
            all = (all + add) % m;
        }
        
        // 减去空集，返回非空子序列数量
        all = (all - 1 + m) % m;
        return (int) all;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：abc
        String s1 = "abc";
        System.out.println("输入: \"" + s1 + "\"");
        System.out.println("输出: " + distinctSubseqII(s1));
        System.out.println("期望: 7");
        System.out.println("解释: \"a\", \"b\", \"c\", \"ab\", \"ac\", \"bc\", \"abc\"");
        System.out.println();
        
        // 测试用例2：aba (有重复字符)
        String s2 = "aba";
        System.out.println("输入: \"" + s2 + "\"");
        System.out.println("输出: " + distinctSubseqII(s2));
        System.out.println("期望: 6");
        System.out.println("解释: \"a\", \"b\", \"ab\", \"ba\", \"aa\", \"aba\"");
        System.out.println();
        
        // 测试用例3：aaa (全相同字符)
        String s3 = "aaa";
        System.out.println("输入: \"" + s3 + "\"");
        System.out.println("输出: " + distinctSubseqII(s3));
        System.out.println("期望: 3");
        System.out.println("解释: \"a\", \"aa\", \"aaa\"");
        System.out.println();
        
        // 测试用例4：单字符
        String s4 = "a";
        System.out.println("输入: \"" + s4 + "\"");
        System.out.println("输出: " + distinctSubseqII(s4));
        System.out.println("期望: 1");
        System.out.println();
        
        // 算法说明
        System.out.println("算法核心思想：");
        System.out.println("- 每个新字符可以与之前所有子序列组合");
        System.out.println("- 通过减去该字符历史贡献避免重复");
        System.out.println("- 时间复杂度：O(n)，空间复杂度：O(1)");
    }
}
