package basic.c41;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * 矩阵中单词路径搜索问题（Word Search II）
 * 
 * 问题描述：
 * 给定一个二维字符矩阵和一个单词列表，
 * 找出所有可以在矩阵中通过相邻路径构成的单词
 * 
 * 路径规则：
 * - 可以上下左右移动到相邻单元格
 * - 每个单元格在一次搜索中只能使用一次
 * - 路径必须连续，不能跳跃
 * 
 * 算法思路：
 * 1. 构建Trie前缀树存储所有单词，支持高效匹配
 * 2. 对矩阵每个位置进行DFS搜索
 * 3. 动态剪枝：已找到的单词从Trie中删除，避免重复
 * 4. 回溯恢复：搜索完毕后恢复矩阵状态
 * 
 * 时间复杂度：O(M*N*4^L) - M*N个起点，每个位置最多4个方向，最大深度L
 * 空间复杂度：O(K*L) - K个单词，平均长度L的Trie树
 * 
 * @author 算法学习
 */
public class WordPath {
    
    /**
     * Trie前缀树节点
     * 用于高效存储和匹配单词
     */
    public static class Node {
        public Node[] nexts; // 26个字母的子节点
        public int pass;     // 通过此节点的单词数量
        public int end;      // 以此节点结尾的单词数量
        
        public Node() {
            nexts = new Node[26];
            pass = 0;
            end = 0;
        }
    }

    /**
     * 将单词添加到Trie树中
     * 
     * @param head Trie树根节点
     * @param word 要添加的单词
     * 
     * 算法过程：
     * 1. 遍历单词的每个字符
     * 2. 在Trie中创建相应路径
     * 3. 更新pass和end计数
     */
    private static void fillWord(Node head, String word) {
        head.pass++;
        char[] s = word.toCharArray();
        int idx = 0;
        Node node = head;
        
        // 遍历单词的每个字符
        for (int i = 0; i < s.length; i++) {
            idx = s[i] - 'a';
            // 如果路径不存在，创建新节点
            if (node.nexts[idx] == null) {
                node.nexts[idx] = new Node();
            }
            node = node.nexts[idx];
            node.pass++; // 通过此节点的单词数+1
        }
        node.end++; // 以此节点结尾的单词数+1
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
        for (Character ch : path){
            str[idx++] = ch;
        }
        return String.valueOf(str);
    }

    /**
     * DFS搜索矩阵中的单词路径
     * 
     * @param board 字符矩阵
     * @param row 当前行坐标
     * @param col 当前列坐标
     * @param path 当前路径
     * @param cur 当前Trie节点
     * @param ans 结果列表
     * @return 本次搜索删除的单词数量
     * 
     * 核心优化：
     * 1. 动态剪枝：pass为0时停止搜索
     * 2. 单词删除：找到单词后从Trie中删除避免重复
     * 3. 回溯恢复：保证矩阵状态正确
     */
    private static int process(char[][] board, int row, int col, LinkedList<Character> path, 
                              Node cur, List<String> ans) {
        char ch = board[row][col];
        
        // 如果位置已被访问（标记为0），返回
        if (ch == 0) {
            return 0;
        }
        
        int idx = ch - 'a';
        // 如果Trie中没有这个字符的路径，或者已经没有单词通过此路径
        if (cur.nexts[idx] == null || cur.nexts[idx].pass == 0) {
            return 0;
        }
        
        // 移动到Trie的下一个节点
        cur = cur.nexts[idx];
        // 将当前字符加入路径
        path.addLast(ch);
        
        int fix = 0; // 记录本次搜索删除的单词数
        
        // 如果找到完整单词
        if (cur.end > 0) {
            ans.add(path(path));    // 添加到结果
            cur.end--;              // 删除这个单词（避免重复）
            fix++;
        }
        
        // 标记当前位置为已访问
        board[row][col] = 0;
        
        // 向四个方向继续搜索
        if (row > 0) {
            fix += process(board, row - 1, col, path, cur, ans);
        }
        if (row < board.length - 1) {
            fix += process(board, row + 1, col, path, cur, ans);
        }
        if (col > 0) {
            fix += process(board, row, col - 1, path, cur, ans);
        }
        if (col < board[0].length - 1) {
            fix += process(board, row, col + 1, path, cur, ans);
        }
        
        // 回溯处理：
        cur.pass -= fix;           // 更新pass计数（删除已找到的单词）
        board[row][col] = ch;      // 恢复矩阵状态
        path.pollLast();           // 移除路径中的当前字符
        
        return fix;
    }

    /**
     * 在矩阵中查找所有可能的单词
     * 
     * @param board 字符矩阵
     * @param words 单词列表
     * @return 在矩阵中找到的单词列表
     * 
     * 算法步骤：
     * 1. 构建Trie树，去除重复单词
     * 2. 遍历矩阵每个位置作为起点
     * 3. 从每个起点开始DFS搜索
     * 4. 返回所有找到的单词
     */
    public static List<String> find(char[][] board, String[] words) {
        // 构建Trie树
        Node head = new Node();
        HashSet<String> set = new HashSet<>(); // 用于去重
        
        // 将所有单词加入Trie树（去重处理）
        for (String word : words) {
            if (!set.contains(word)) {
                fillWord(head, word);
                set.add(word);
            }
        }
        
        List<String> ans = new ArrayList<>();
        LinkedList<Character> path = new LinkedList<>();
        
        // 遍历矩阵每个位置作为搜索起点
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                process(board, row, col, path, head, ans);
            }
        }
        
        return ans;
    }
}
