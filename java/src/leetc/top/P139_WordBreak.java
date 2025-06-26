package leetc.top;

import java.util.List;

/**
 * LeetCode 139. 单词拆分 (Word Break)
 * 
 * 问题描述：
 * 给你一个字符串 s 和一个字符串列表 wordDict 作为字典。
 * 请你判断是否可以利用字典中出现的单词拼接出 s。
 * 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
 * 
 * 解法思路：
 * 前缀树(Trie) + 动态规划：
 * 1. 构建前缀树：将字典中所有单词构建成前缀树，便于快速查找
 * 2. 动态规划：
 *    - dp[i] = 从位置i开始到字符串末尾是否可以被拆分
 *    - dp[n] = true（空字符串可以被拆分）
 *    - 状态转移：dp[i] = true if 存在单词w使得s[i...i+len-1] = w 且 dp[i+len] = true
 * 
 * 前缀树结构：
 * - 每个节点包含26个子节点（对应a-z）
 * - end标记：表示到当前节点是否构成一个完整单词
 * - 支持高效的前缀匹配
 * 
 * DP状态转移：
 * - 从右往左填充dp数组
 * - 对每个位置i，尝试从i开始的所有可能单词
 * - 利用前缀树进行快速单词匹配
 * 
 * 算法优势：
 * - 前缀树避免重复字符串比较
 * - DP避免重复子问题计算
 * - 时间复杂度优于朴素回溯
 * 
 * 时间复杂度：O(n² + m*k) - n为字符串长度，m为单词数，k为平均单词长度
 * 空间复杂度：O(m*k + n) - 前缀树空间 + DP数组空间
 * 
 * LeetCode链接：https://leetcode.com/problems/word-break/
 */
public class P139_WordBreak {

    /**
     * 前缀树节点定义
     */
    private static class Node {
        public Node[] nexts;    // 26个子节点，对应a-z
        public boolean end;     // 是否为单词结尾
        
        public Node() {
            end = false;
            nexts = new Node[26];
        }
    }

    /**
     * 判断字符串是否可以拆分成字典中的单词
     * 
     * @param s 待拆分的字符串
     * @param wordDict 字典单词列表
     * @return 是否可以拆分
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        // 构建前缀树
        Node root = new Node();
        for (String str : wordDict) {
            char[] word = str.toCharArray();
            Node node = root;
            int idx = 0;
            
            // 将单词插入前缀树
            for (int i = 0; i < word.length; i++) {
                idx = word[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            node.end = true;  // 标记单词结尾
        }
        
        // 动态规划
        char[] str = s.toCharArray();
        int n = str.length;
        boolean[] dp = new boolean[n + 1];
        dp[n] = true;  // 空字符串可以被拆分
        
        // 从右往左填充dp数组
        for (int i = n - 1; i >= 0; i--) {
            Node cur = root;
            
            // 尝试从位置i开始的所有可能单词
            for (int end = i; end < n; end++) {
                int path = str[end] - 'a';
                
                // 如果当前字符路径不存在，停止搜索
                if (cur.nexts[path] == null) {
                    break;
                }
                
                cur = cur.nexts[path];
                
                // 如果找到完整单词且后续部分可以拆分
                if (cur.end && dp[end + 1]) {
                    dp[i] = true;
                    break;  // 找到一种拆分方案即可
                }
            }
        }
        
        return dp[0];  // 整个字符串是否可以拆分
    }
}
