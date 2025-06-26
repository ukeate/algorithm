package leetc.top;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 140. 单词拆分 II (Word Break II)
 * 
 * 问题描述：
 * 给定一个字符串 s 和一个字符串字典 wordDict，在字符串 s 中增加空格来构建一个句子，
 * 使得句子中所有的单词都在词典中。以任意顺序返回所有这些可能的句子。
 * 注意：词典中的同一个单词可能在分词中被多次使用。
 * 
 * 解法思路：
 * 前缀树 + 动态规划 + 回溯搜索：
 * 1. 构建前缀树：将字典单词构建成前缀树，支持高效前缀匹配
 * 2. 可行性检查：使用DP预先判断每个位置是否可以拆分
 * 3. 回溯搜索：在可拆分的前提下，枚举所有可能的拆分方案
 * 
 * 三阶段算法：
 * 第一阶段：构建前缀树
 * - 将所有单词插入前缀树
 * - 在每个单词末尾节点保存原始单词字符串
 * 
 * 第二阶段：DP预处理
 * - 判断从每个位置开始是否可以完成拆分
 * - 避免在不可拆分的分支上浪费时间
 * 
 * 第三阶段：回溯搜索
 * - 基于DP结果进行剪枝
 * - 构造所有可能的拆分句子
 * 
 * 关键优化：
 * - 前缀树避免重复字符串比较
 * - DP预处理避免无效搜索分支
 * - 回溯过程中实时剪枝
 * 
 * 时间复杂度：O(n² + 2^n) - DP预处理O(n²)，回溯最坏O(2^n)
 * 空间复杂度：O(m*k + n) - 前缀树空间 + 递归栈空间
 * 
 * LeetCode链接：https://leetcode.com/problems/word-break-ii/
 */
public class P140_WordBreakII {
    
    /**
     * 增强的前缀树节点，包含路径信息
     */
    private static class Node {
        public String path;     // 从根到当前节点构成的完整单词
        public boolean end;     // 是否为单词结尾
        public Node[] nexts;    // 26个子节点
        
        public Node() {
            path = null;
            end = false;
            nexts = new Node[26];
        }
    }

    /**
     * 构建前缀树
     * 
     * @param wordDict 字典单词列表
     * @return 前缀树根节点
     */
    private static Node trie(List<String> wordDict) {
        Node root = new Node();
        
        for (String str : wordDict) {
            char[] chs = str.toCharArray();
            Node node = root;
            int idx = 0;
            
            // 逐字符插入前缀树
            for (int i = 0; i < chs.length; i++) {
                idx = chs[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            
            // 在单词末尾保存完整单词和结束标记
            node.path = str;
            node.end = true;
        }
        
        return root;
    }

    /**
     * 动态规划预处理：检查每个位置的可拆分性
     * 
     * @param s 原字符串
     * @param root 前缀树根节点
     * @return 每个位置的可拆分性数组
     */
    private static boolean[] dp(String s, Node root) {
        char[] str = s.toCharArray();
        int n = str.length;
        boolean[] dp = new boolean[n + 1];
        dp[n] = true;  // 空字符串可以拆分
        
        // 从右往左填充DP数组
        for (int i = n - 1; i >= 0; i--) {
            Node cur = root;
            
            // 尝试从位置i开始的所有可能单词
            for (int end = i; end < n; end++) {
                int path = str[end] - 'a';
                
                // 如果前缀树中没有对应路径，停止搜索
                if (cur.nexts[path] == null) {
                    break;
                }
                
                cur = cur.nexts[path];
                
                // 如果找到完整单词且后续可拆分
                if (cur.end && dp[end + 1]) {
                    dp[i] = true;
                    break;
                }
            }
        }
        
        return dp;
    }

    /**
     * 回溯搜索所有可能的拆分方案
     * 
     * @param str 字符数组
     * @param idx 当前处理位置
     * @param root 前缀树根节点
     * @param dp 可拆分性数组
     * @param path 当前拆分路径
     * @param ans 结果集合
     */
    private static void process(char[] str, int idx, Node root, boolean[] dp, 
                              ArrayList<String> path, List<String> ans) {
        // 到达字符串末尾，构造完整句子
        if (idx == str.length) {
            StringBuilder builder = new StringBuilder();
            
            // 用空格连接所有单词
            for (int i = 0; i < path.size() - 1; i++) {
                builder.append(path.get(i) + " ");
            }
            builder.append(path.get(path.size() - 1));
            
            ans.add(builder.toString());
            return;
        }
        
        Node cur = root;
        
        // 尝试从当前位置开始的所有可能单词
        for (int end = idx; end < str.length; end++) {
            int next = str[end] - 'a';
            
            // 前缀树中没有对应路径，停止搜索
            if (cur.nexts[next] == null) {
                break;
            }
            
            cur = cur.nexts[next];
            
            // 找到完整单词且后续部分可拆分
            if (cur.end && dp[end + 1]) {
                path.add(cur.path);                           // 添加当前单词
                process(str, end + 1, root, dp, path, ans);   // 递归处理剩余部分
                path.remove(path.size() - 1);                 // 回溯
            }
        }
    }

    /**
     * 单词拆分II主方法
     * 
     * @param s 输入字符串
     * @param wordDict 字典单词列表
     * @return 所有可能的拆分句子
     */
    public static List<String> wordBreak(String s, List<String> wordDict) {
        char[] str = s.toCharArray();
        
        // 构建前缀树
        Node root = trie(wordDict);
        
        // DP预处理，检查可拆分性
        boolean[] dp = dp(s, root);
        
        // 回溯搜索所有拆分方案
        ArrayList<String> path = new ArrayList<>();
        List<String> ans = new ArrayList<>();
        process(str, 0, root, dp, path, ans);
        
        return ans;
    }
}
