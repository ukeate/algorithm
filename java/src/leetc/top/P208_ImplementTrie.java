package leetc.top;

/**
 * LeetCode 208. 实现 Trie (前缀树) (Implement Trie / Prefix Tree)
 * 
 * 问题描述：
 * Trie（发音类似 "try"）或者说前缀树是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。
 * 这一数据结构有相当多的应用情景，例如自动补完和拼写检查。
 * 
 * 请你实现 Trie 类：
 * - Trie() 初始化前缀树对象
 * - void insert(String word) 向前缀树中插入字符串 word
 * - boolean search(String word) 如果字符串 word 在前缀树中，返回 true；否则，返回 false
 * - boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix，返回 true；否则，返回 false
 * 
 * 示例：
 * Trie trie = new Trie();
 * trie.insert("apple");
 * trie.search("apple");   // 返回 True
 * trie.search("app");     // 返回 False  
 * trie.startsWith("app"); // 返回 True
 * trie.insert("app");
 * trie.search("app");     // 返回 True
 * 
 * 解法思路：
 * 树形数据结构设计：
 * 1. 节点设计：每个节点包含26个子节点指针（对应26个字母）和一个结束标记
 * 2. 插入操作：从根节点开始，按字符路径创建或跟随节点，最后标记结束
 * 3. 查找操作：从根节点开始，按字符路径查找，检查是否到达有效结束节点
 * 4. 前缀查找：类似查找，但不需要检查结束标记
 * 
 * 核心优势：
 * - 空间效率：公共前缀共享存储空间
 * - 时间效率：查找时间与字符串长度成正比，与存储的字符串数量无关
 * - 前缀查询：天然支持前缀匹配，适合自动补全场景
 * 
 * 数据结构特点：
 * - 根节点不包含字符，每条从根到叶子的路径代表一个字符串
 * - 节点的end标记表示是否有字符串在此结束
 * - 26叉树结构，每个节点最多有26个子节点
 * 
 * 时间复杂度：O(m) - m为字符串长度，所有操作都与字符串长度线性相关
 * 空间复杂度：O(ALPHABET_SIZE * N * M) - N为字符串数量，M为平均长度
 * 
 * LeetCode链接：https://leetcode.com/problems/implement-trie-prefix-tree/
 */
public class P208_ImplementTrie {
    
    /**
     * Trie树的节点定义
     */
    public static class Node {
        public boolean end;      // 标记是否有字符串在此节点结束
        public Node[] nexts;     // 子节点数组，对应26个字母
        
        /**
         * 节点构造函数
         * 初始化节点状态
         */
        public Node() {
            end = false;           // 默认不是结束节点
            nexts = new Node[26];  // 创建26个子节点位置（a-z）
        }
    }

    private Node root;  // Trie树的根节点

    /**
     * Trie构造函数
     * 初始化前缀树
     */
    public P208_ImplementTrie() {
        root = new Node();  // 创建根节点
    }

    /**
     * 向前缀树中插入字符串
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历
     * 2. 对字符串的每个字符：
     *    - 计算字符对应的路径索引（'a'=0, 'b'=1, ...）
     *    - 如果对应子节点不存在，创建新节点
     *    - 移动到子节点
     * 3. 在最后一个节点标记字符串结束
     * 
     * @param word 要插入的字符串
     */
    public void insert(String word) {
        if (word == null) {
            return;  // 空字符串不插入
        }
        
        char[] str = word.toCharArray();  // 转换为字符数组便于处理
        Node node = root;                 // 从根节点开始
        int path = 0;                     // 路径索引
        
        // 遍历字符串的每个字符
        for (int i = 0; i < str.length; i++) {
            path = str[i] - 'a';  // 计算字符对应的路径索引
            
            // 如果路径不存在，创建新节点
            if (node.nexts[path] == null) {
                node.nexts[path] = new Node();
            }
            
            node = node.nexts[path];  // 移动到下一个节点
        }
        
        node.end = true;  // 标记字符串在此节点结束
    }

    /**
     * 在前缀树中查找完整字符串
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历
     * 2. 对字符串的每个字符：
     *    - 计算字符对应的路径索引
     *    - 如果对应子节点不存在，返回false
     *    - 移动到子节点
     * 3. 检查最终节点是否标记为字符串结束
     * 
     * @param word 要查找的字符串
     * @return 字符串是否存在于前缀树中
     */
    public boolean search(String word) {
        if (word == null) {
            return false;  // 空字符串返回false
        }
        
        char[] chs = word.toCharArray();  // 转换为字符数组
        Node node = root;                 // 从根节点开始
        int idx = 0;                      // 路径索引
        
        // 遍历字符串的每个字符
        for (int i = 0; i < chs.length; i++) {
            idx = chs[i] - 'a';  // 计算字符对应的路径索引
            
            // 如果路径不存在，说明字符串不在树中
            if (node.nexts[idx] == null) {
                return false;
            }
            
            node = node.nexts[idx];  // 移动到下一个节点
        }
        
        // 检查是否有字符串在此节点结束
        return node.end;
    }

    /**
     * 检查是否存在以给定前缀开头的字符串
     * 
     * 算法步骤：
     * 1. 从根节点开始遍历
     * 2. 对前缀的每个字符：
     *    - 计算字符对应的路径索引
     *    - 如果对应子节点不存在，返回false
     *    - 移动到子节点
     * 3. 如果能成功遍历完前缀，返回true（不需要检查end标记）
     * 
     * @param prefix 要检查的前缀
     * @return 是否存在以该前缀开头的字符串
     */
    public boolean startsWith(String prefix) {
        if (prefix == null) {
            return false;  // 空前缀返回false
        }
        
        char[] chs = prefix.toCharArray();  // 转换为字符数组
        Node node = root;                   // 从根节点开始
        int idx = 0;                        // 路径索引
        
        // 遍历前缀的每个字符
        for (int i = 0; i < chs.length; i++) {
            idx = chs[i] - 'a';  // 计算字符对应的路径索引
            
            // 如果路径不存在，说明没有以此前缀开头的字符串
            if (node.nexts[idx] == null) {
                return false;
            }
            
            node = node.nexts[idx];  // 移动到下一个节点
        }
        
        // 能遍历完前缀就说明存在以该前缀开头的字符串
        return true;
    }
}
