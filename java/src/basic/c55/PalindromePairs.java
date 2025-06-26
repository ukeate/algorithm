package basic.c55;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 回文对问题
 * 
 * 问题描述：
 * 给定一组字符串，找到每一对能够拼接成回文字符串的字符串对。
 * 返回所有满足条件的字符串对的下标列表。
 * 
 * 示例：
 * 输入: words = ["abcd","dcba","lls","s","sssll"]
 * 输出: [[0,1],[1,0],[3,2],[2,4]]
 * 解释: 
 * - words[0] + words[1] = "abcd" + "dcba" = "abcddcba" 是回文
 * - words[1] + words[0] = "dcba" + "abcd" = "dcbaabcd" 是回文
 * - words[3] + words[2] = "s" + "lls" = "slls" 是回文
 * - words[2] + words[4] = "lls" + "sssll" = "llssssll" 是回文
 * 
 * 算法思路：
 * 1. 使用Manacher算法预处理每个字符串的回文信息
 * 2. 对于每个字符串，分别检查前缀和后缀的回文性
 * 3. 如果前缀是回文，查找能匹配剩余后缀的逆序字符串
 * 4. 如果后缀是回文，查找能匹配剩余前缀的逆序字符串
 * 5. 特殊处理空字符串的情况
 * 
 * 时间复杂度：O(N * K²) 其中N为字符串数量，K为平均字符串长度
 * 空间复杂度：O(N * K)
 * 
 * LeetCode: https://leetcode.com/problems/palindrome-pairs/
 * 
 * @author 算法学习
 */
public class PalindromePairs {
    
    /**
     * 反转字符串
     * 
     * @param str 输入字符串
     * @return 反转后的字符串
     */
    private static String reverse(String str) {
        char[] chs = str.toCharArray();
        int l = 0;
        int r = chs.length - 1;
        
        while (l < r) {
            char tmp = chs[l];
            chs[l++] = chs[r];
            chs[r--] = tmp;
        }
        
        return String.valueOf(chs);
    }

    /**
     * 添加一对索引到结果列表
     * 
     * @param res 结果列表
     * @param left 左索引
     * @param right 右索引
     */
    private static void add(List<List<Integer>> res, int left, int right) {
        List<Integer> pair = new ArrayList<>();
        pair.add(left);
        pair.add(right);
        res.add(pair);
    }

    /**
     * 为Manacher算法准备字符数组（插入分隔符）
     * 
     * @param word 输入字符串
     * @return 插入分隔符后的字符数组
     */
    private static char[] manachers(String word) {
        char[] chs = word.toCharArray();
        char[] ans = new char[chs.length * 2 + 1];
        int idx = 0;
        
        // 插入分隔符'#'
        for (int i = 0; i < ans.length; i++) {
            ans[i] = (i & 1) == 0 ? '#' : chs[idx++];
        }
        
        return ans;
    }

    /**
     * Manacher算法：计算每个位置的回文半径
     * 
     * @param word 输入字符串
     * @return 回文半径数组
     */
    private static int[] manacher(String word) {
        char[] s = manachers(word);
        int[] rs = new int[s.length];  // 回文半径数组
        int center = -1;               // 回文中心
        int pr = -1;                   // 回文右边界
        
        for (int i = 0; i < s.length; i++) {
            // 如果在回文右边界内，可以利用对称性
            rs[i] = pr > i ? Math.min(rs[(center << 1) - i], pr - i) : 1;
            
            // 尝试扩展回文
            while (i + rs[i] < s.length && i - rs[i] > -1) {
                if (s[i + rs[i]] != s[i - rs[i]]) {
                    break;
                }
                rs[i]++;
            }
            
            // 更新回文中心和右边界
            if (i + rs[i] > pr) {
                pr = i + rs[i];
                center = i;
            }
        }
        
        return rs;
    }

    /**
     * 找到能与指定字符串组成回文对的所有字符串
     * 
     * @param word 当前字符串
     * @param idx 当前字符串的索引
     * @param words 所有字符串的索引映射
     * @return 能组成回文对的索引对列表
     */
    private static List<List<Integer>> findPairs(String word, int idx, HashMap<String, Integer> words) {
        List<List<Integer>> res = new ArrayList<>();
        String reverse = reverse(word);
        
        // 处理空字符串的特殊情况
        Integer rest = words.get("");
        if (rest != null && rest != idx && word.equals(reverse)) {
            // 当前字符串本身是回文，可以与空字符串组成回文对
            add(res, rest, idx);  // 空字符串在前
            add(res, idx, rest);  // 空字符串在后
        }
        
        // 使用Manacher算法分析回文结构
        int[] rs = manacher(word);
        int mid = rs.length >> 1;  // 中间位置
        
        // 检查前缀回文：如果前缀是回文，找能匹配后缀的逆序字符串
        for (int i = 1; i < mid; i++) {
            if (i - rs[i] == -1) {
                // 前缀是回文，找原字符串后缀的逆序
                rest = words.get(reverse.substring(0, mid - i));
                if (rest != null && rest != idx) {
                    add(res, rest, idx);  // 找到的字符串在前，当前字符串在后
                }
            }
        }
        
        // 检查后缀回文：如果后缀是回文，找能匹配前缀的逆序字符串
        for (int i = mid + 1; i < rs.length; i++) {
            if (i + rs[i] == rs.length) {
                // 后缀是回文，找原字符串前缀的逆序
                rest = words.get(reverse.substring((mid << 1) - i));
                if (rest != null && rest != idx) {
                    add(res, idx, rest);  // 当前字符串在前，找到的字符串在后
                }
            }
        }
        
        return res;
    }

    /**
     * 主方法：找到所有回文对
     * 
     * @param words 字符串数组
     * @return 回文对的索引列表
     */
    public static List<List<Integer>> pairs(String[] words) {
        // 建立字符串到索引的映射
        HashMap<String, Integer> set = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            set.put(words[i], i);
        }
        
        List<List<Integer>> res = new ArrayList<>();
        
        // 对每个字符串查找其回文对
        for (int i = 0; i < words.length; i++) {
            res.addAll(findPairs(words[i], i, set));
        }
        
        return res;
    }

    /**
     * 测试方法
     * 验证回文对算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 回文对问题测试 ===");
        
        // 测试用例1
        String[] words1 = {"abcd", "dcba", "lls", "s", "sssll"};
        List<List<Integer>> result1 = pairs(words1);
        
        System.out.println("字符串数组1: [\"abcd\", \"dcba\", \"lls\", \"s\", \"sssll\"]");
        System.out.println("回文对:");
        for (List<Integer> pair : result1) {
            int i = pair.get(0), j = pair.get(1);
            System.out.printf("[%d,%d]: \"%s\" + \"%s\" = \"%s\"%n", 
                    i, j, words1[i], words1[j], words1[i] + words1[j]);
        }
        
        // 测试用例2
        String[] words2 = {"lls", "s", "sssll"};
        List<List<Integer>> result2 = pairs(words2);
        
        System.out.println("\n字符串数组2: [\"lls\", \"s\", \"sssll\"]");
        System.out.println("回文对:");
        for (List<Integer> pair : result2) {
            int i = pair.get(0), j = pair.get(1);
            System.out.printf("[%d,%d]: \"%s\" + \"%s\" = \"%s\"%n", 
                    i, j, words2[i], words2[j], words2[i] + words2[j]);
        }
        
        // 测试用例3
        String[] words3 = {"a", ""};
        List<List<Integer>> result3 = pairs(words3);
        
        System.out.println("\n字符串数组3: [\"a\", \"\"]");
        System.out.println("回文对:");
        for (List<Integer> pair : result3) {
            int i = pair.get(0), j = pair.get(1);
            System.out.printf("[%d,%d]: \"%s\" + \"%s\" = \"%s\"%n", 
                    i, j, words3[i], words3[j], words3[i] + words3[j]);
        }
        
        System.out.println("\n=== 算法分析 ===");
        System.out.println("时间复杂度: O(N * K²) - N个字符串，平均长度K");
        System.out.println("空间复杂度: O(N * K) - 存储字符串映射和结果");
        System.out.println("核心算法: Manacher算法 + 哈希表查找");
        System.out.println("关键技巧: 利用回文的对称性分别处理前缀和后缀回文");
    }
}
