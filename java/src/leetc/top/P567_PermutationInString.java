package leetc.top;

import java.util.Arrays;

/**
 * LeetCode 567. 字符串的排列 (Permutation in String)
 * 
 * 问题描述：
 * 给你两个字符串 s1 和 s2，写一个函数来判断 s2 是否包含 s1 的排列。
 * 换句话说，第一个字符串的排列之一是第二个字符串的子串。
 * 
 * 示例：
 * - 输入：s1 = "ab" s2 = "eidbaooo"
 * - 输出：True
 * - 解释：s2 包含 s1 的排列之一 ("ba")
 * 
 * - 输入：s1="ab" s2="eidboaoo"
 * - 输出：False
 * 
 * 解法思路：
 * 滑动窗口 + 字符统计：
 * 1. 排列问题本质上是字符频次匹配问题
 * 2. 在s2中寻找长度为len(s1)的窗口，使得窗口内字符频次与s1完全匹配
 * 3. 使用滑动窗口技术优化，避免重复计算
 * 
 * 三种解法：
 * 1. 暴力：枚举所有可能的子串，排序比较
 * 2. 固定窗口：每次比较固定长度窗口的字符频次
 * 3. 滑动窗口：动态维护窗口内的字符频次
 * 
 * 时间复杂度：O(n) - 滑动窗口解法
 * 空间复杂度：O(1) - 字符集大小固定（256个字符）
 * 
 * LeetCode链接：https://leetcode.com/problems/permutation-in-string/
 */
public class P567_PermutationInString {
    
    /**
     * 暴力解法1：枚举所有子串，排序比较
     * 
     * 算法步骤：
     * 1. 将目标字符串s1排序作为标准
     * 2. 枚举s2的所有子串
     * 3. 对每个子串排序，与标准比较
     * 
     * 时间复杂度：O(n³logn) - n²个子串，每个排序需要O(nlogn)
     * 
     * @param a 目标字符串s1
     * @param s 源字符串s2
     * @return 是否包含s1的排列
     */
    public static boolean checkInclusion1(String a, String s) {
        if (s == null || a == null || s.length() < a.length()) {
            return false;
        }
        
        // 将目标字符串排序作为比较标准
        char[] aim = a.toCharArray();
        Arrays.sort(aim);
        String aimSort = String.valueOf(aim);
        
        // 枚举所有可能的子串
        for (int l = 0; l < s.length(); l++) {
            for (int r = l; r < s.length(); r++) {
                // 提取子串并排序
                char[] cur = s.substring(l, r + 1).toCharArray();
                Arrays.sort(cur);
                String curSort = String.valueOf(cur);
                
                // 比较排序后的字符串
                if (curSort.equals(aimSort)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查从位置l开始，长度为aim.length的子串是否与aim字符频次相等
     * 
     * @param str 源字符串字符数组
     * @param l 起始位置
     * @param aim 目标字符数组
     * @return 字符频次是否相等
     */
    private static boolean isCountEqual(char[] str, int l, char[] aim) {
        int[] count = new int[256]; // 字符频次统计数组
        
        // 统计目标字符串的字符频次
        for (int i = 0; i < aim.length; i++) {
            count[aim[i]]++;
        }
        
        // 检查源字符串对应位置的字符频次
        for (int i = 0; i < aim.length; i++) {
            if (count[str[l + i]]-- == 0) {
                // 某个字符的频次不匹配
                return false;
            }
        }
        
        return true;
    }

    /**
     * 优化解法2：固定窗口大小，逐个检查
     * 
     * 算法步骤：
     * 1. 只考虑长度等于s1长度的窗口
     * 2. 对每个窗口位置，统计字符频次并比较
     * 
     * 时间复杂度：O(n*m) - n个窗口位置，每个窗口检查需要O(m)
     * 
     * @param a 目标字符串s1
     * @param s 源字符串s2
     * @return 是否包含s1的排列
     */
    public static boolean checkInclusion2(String a, String s) {
        if (s == null || a == null || s.length() < a.length()) {
            return false;
        }
        
        char[] str = s.toCharArray();
        char[] aim = a.toCharArray();
        
        // 枚举所有可能的窗口起始位置
        for (int l = 0; l <= str.length - aim.length; l++) {
            if (isCountEqual(str, l, aim)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 最优解法3：滑动窗口 + 动态维护字符统计
     * 
     * 算法思想：
     * 1. 维护一个固定大小的滑动窗口
     * 2. 用一个计数器all表示还需要匹配的字符数量
     * 3. 窗口右移时，新加入字符减少需求，移出字符增加需求
     * 4. 当all==0时，说明当前窗口内字符完全匹配
     * 
     * 核心技巧：
     * - count[c]>0：字符c是s1中需要的字符
     * - count[c]≤0：字符c不是s1中需要的字符，或已经足够
     * - 只有当count[c]从>0变为≤0时，all才减1
     * - 只有当count[c]从≤0变为>0时，all才加1
     * 
     * @param a 目标字符串s1
     * @param s 源字符串s2
     * @return 是否包含s1的排列
     */
    public static boolean checkInclusion3(String a, String s) {
        if (s == null || a == null || s.length() < a.length()) {
            return false;
        }
        
        char[] str = a.toCharArray();
        int m = str.length;
        
        // 初始化字符需求统计
        int[] count = new int[256];
        for (int i = 0; i < m; i++) {
            count[str[i]]++; // 正数表示需要该字符的数量
        }
        
        int all = m; // 还需要匹配的字符总数
        char[] chs = s.toCharArray();
        int r = 0;
        
        // 初始化窗口：先添加前m个字符
        for (; r < m; r++) {
            if (count[chs[r]]-- > 0) {
                // 这个字符是需要的（count从正数减1）
                all--;
            }
        }
        
        // 滑动窗口：窗口大小固定为m
        for (; r < chs.length; r++) {
            // 检查当前窗口是否匹配
            if (all == 0) {
                return true;
            }
            
            // 窗口右移：添加新字符chs[r]
            if (count[chs[r]]-- > 0) {
                all--; // 添加了一个需要的字符
            }
            
            // 窗口右移：移除旧字符chs[r-m]
            if (count[chs[r - m]]++ >= 0) {
                all++; // 移除了一个需要的字符
            }
        }
        
        // 检查最后一个窗口
        return all == 0;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：包含排列
        String s1_1 = "ab", s2_1 = "eidbaooo";
        System.out.println("测试用例1:");
        System.out.println("s1 = \"" + s1_1 + "\", s2 = \"" + s2_1 + "\"");
        System.out.println("暴力解法: " + checkInclusion1(s1_1, s2_1));
        System.out.println("固定窗口: " + checkInclusion2(s1_1, s2_1));
        System.out.println("滑动窗口: " + checkInclusion3(s1_1, s2_1));
        System.out.println("期望: true (包含\"ba\")");
        System.out.println();
        
        // 测试用例2：不包含排列
        String s1_2 = "ab", s2_2 = "eidboaoo";
        System.out.println("测试用例2:");
        System.out.println("s1 = \"" + s1_2 + "\", s2 = \"" + s2_2 + "\"");
        System.out.println("滑动窗口: " + checkInclusion3(s1_2, s2_2));
        System.out.println("期望: false");
        System.out.println();
        
        // 测试用例3：完全匹配
        String s1_3 = "abc", s2_3 = "bac";
        System.out.println("测试用例3 (完全匹配):");
        System.out.println("s1 = \"" + s1_3 + "\", s2 = \"" + s2_3 + "\"");
        System.out.println("滑动窗口: " + checkInclusion3(s1_3, s2_3));
        System.out.println("期望: true");
        System.out.println();
        
        // 测试用例4：重复字符
        String s1_4 = "aab", s2_4 = "c3ab4a5a";
        System.out.println("测试用例4 (重复字符):");
        System.out.println("s1 = \"" + s1_4 + "\", s2 = \"" + s2_4 + "\"");
        System.out.println("滑动窗口: " + checkInclusion3(s1_4, s2_4));
        System.out.println("期望: false (没有\"aab\"的排列)");
        System.out.println();
        
        // 算法对比
        System.out.println("算法对比:");
        System.out.println("1. 暴力解法: O(n³logn) 时间，简单直观");
        System.out.println("2. 固定窗口: O(nm) 时间，优化了窗口大小");
        System.out.println("3. 滑动窗口: O(n) 时间，最优解法");
        System.out.println("4. 核心思想: 排列等价于字符频次相同");
        System.out.println("5. 关键技巧: 动态维护字符需求统计");
    }
}

