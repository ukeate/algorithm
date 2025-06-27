package leetc.top;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * LeetCode 212. 单词搜索 II (Word Search II)
 * 
 * 问题描述：
 * 给定一个 m x n 二维字符网格 board 和一个单词列表 words，找出所有同时在二维网格和字典中出现的单词。
 * 单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。
 * 同一个单元格内的字母在一个单词中不允许被重复使用。
 * 
 * 示例：
 * 输入：board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], 
 *      words = ["oath","pea","eat","rain"]
 * 输出：["eat","oath"]
 * 
 * 解法思路：
 * Trie（前缀树）+ DFS（深度优先搜索）+ 剪枝优化：
 * 
 * 1. 构建Trie树：
 *    - 将所有待查找的单词插入Trie树
 *    - 每个节点记录通过次数(pass)和结束次数(end)
 * 
 * 2. DFS搜索：
 *    - 从每个位置开始进行DFS
 *    - 沿着Trie树路径搜索，确保路径有效
 *    - 找到完整单词时加入结果并减少end计数
 * 
 * 3. 关键优化：
 *    - 动态剪枝：当某个单词被找到后，从Trie中移除，避免重复
 *    - pass计数优化：当子树中没有剩余单词时，停止搜索
 *    - 回溯时恢复现场：暂时标记访问状态，搜索完毕后恢复
 * 
 * 核心思想：
 * - Trie树优化：避免对每个单词单独进行DFS
 * - 路径共享：多个单词可以共享相同的前缀路径
 * - 动态剪枝：实时更新搜索空间，提高效率
 * 
 * 时间复杂度：O(M*N*4^L) - M*N为网格大小，L为最长单词长度
 * 空间复杂度：O(K*L) - K为单词数量，L为平均单词长度
 * 
 * LeetCode链接：https://leetcode.com/problems/word-search-ii/
 */
public class P212_WordSearchII {
    
    /**
     * Trie树节点定义
     */
    public static class TrieNode {
        public TrieNode[] nexts;  // 26个子节点，对应a-z
        public int pass;          // 通过当前节点的单词数量
        public int end;           // 以当前节点结束的单词数量
        
        public TrieNode() {
            nexts = new TrieNode[26];
            pass = 0;
            end = 0;
        }
    }

    /**
     * 向Trie树中插入单词
     * 
     * @param node Trie树根节点
     * @param word 待插入的单词
     */
    private static void fillWord(TrieNode node, String word) {
        node.pass++;  // 根节点通过数+1
        char[] chs = word.toCharArray();
        
        for (int i = 0; i < chs.length; i++) {
            int path = chs[i] - 'a';  // 计算字符对应的路径索引
            
            // 如果路径不存在，创建新节点
            if (node.nexts[path] == null) {
                node.nexts[path] = new TrieNode();
            }
            
            node = node.nexts[path];  // 移动到下一个节点
            node.pass++;              // 通过计数+1
        }
        node.end++;  // 单词结束标记+1
    }

    /**
     * 将路径转换为字符串
     * 
     * @param path 字符路径
     * @return 路径对应的字符串
     */
    private static String path(LinkedList<Character> path) {
        char[] str = new char[path.size()];
        int idx = 0;
        for (Character cha : path) {
            str[idx++] = cha;
        }
        return String.valueOf(str);
    }

    /**
     * DFS搜索过程，核心递归函数
     * 
     * @param board 字符网格
     * @param row 当前行坐标
     * @param col 当前列坐标
     * @param path 当前搜索路径
     * @param cur 当前Trie节点
     * @param ans 结果列表
     * @return 返回本次搜索中找到并移除的单词数量
     */
    private static int process(char[][] board, int row, int col, LinkedList<Character> path, 
                              TrieNode cur, List<String> ans) {
        char cha = board[row][col];
        
        // 如果当前位置已被访问（标记为0），直接返回
        if (cha == 0) {
            return 0;
        }
        
        int idx = cha - 'a';  // 计算字符对应的路径索引
        
        // 如果Trie中没有对应路径或该路径下没有剩余单词，直接返回
        if (cur.nexts[idx] == null || cur.nexts[idx].pass == 0) {
            return 0;
        }
        
        cur = cur.nexts[idx];    // 移动到下一个Trie节点
        path.addLast(cha);       // 将字符加入路径
        int fix = 0;             // 记录本次搜索找到的单词数
        
        // 检查是否找到完整单词
        if (cur.end > 0) {
            ans.add(path(path));  // 将单词加入结果
            cur.end--;            // 减少结束计数，避免重复添加
            fix++;                // 找到单词数+1
        }
        
        // 标记当前位置为已访问
        board[row][col] = 0;
        
        // 向四个方向进行DFS搜索
        if (row > 0) {
            fix += process(board, row - 1, col, path, cur, ans);  // 上
        }
        if (row < board.length - 1) {
            fix += process(board, row + 1, col, path, cur, ans);  // 下
        }
        if (col > 0) {
            fix += process(board, row, col - 1, path, cur, ans);  // 左
        }
        if (col < board[0].length - 1) {
            fix += process(board, row, col + 1, path, cur, ans);  // 右
        }
        
        // 回溯：恢复现场
        board[row][col] = cha;  // 恢复字符
        path.pollLast();        // 移除路径中的最后一个字符
        
        // 动态剪枝：更新pass计数，减少已找到的单词数
        cur.pass -= fix;
        
        return fix;  // 返回本次搜索找到的单词数
    }

    /**
     * 主方法：在字符网格中查找所有存在的单词
     * 
     * @param board 字符网格
     * @param words 待查找的单词列表
     * @return 在网格中找到的单词列表
     */
    public List<String> findWords(char[][] board, String[] words) {
        // 构建Trie树
        TrieNode head = new TrieNode();
        HashSet<String> set = new HashSet<>();
        
        // 去重并插入单词到Trie树
        for (String word : words) {
            if (!set.contains(word)) {
                fillWord(head, word);
                set.add(word);
            }
        }
        
        List<String> ans = new ArrayList<>();
        LinkedList<Character> path = new LinkedList<>();
        
        // 从每个位置开始DFS搜索
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                process(board, row, col, path, head, ans);
            }
        }
        
        return ans;
    }
}
