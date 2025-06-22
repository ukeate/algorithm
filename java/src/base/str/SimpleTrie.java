package base.str;

/**
 * 简单前缀树（Trie树）实现
 * 问题描述：实现一个支持插入、删除和前缀查询的字典树数据结构
 * 
 * 数据结构特点：
 * 1. 每个节点代表一个字符，从根到叶子的路径代表一个字符串
 * 2. 具有公共前缀的字符串共享相同的路径前缀
 * 3. 支持高效的前缀匹配和字符串查找
 * 
 * 核心操作：
 * - insert: 向树中插入一个字符串
 * - erase: 从树中删除一个字符串
 * - countWordsEqualTo: 统计等于指定字符串的单词数量
 * - countWordsStartWith: 统计以指定前缀开头的单词数量
 * 
 * 时间复杂度：
 * - 插入/删除/查找：O(M) - M为字符串长度
 * - 空间复杂度：O(ALPHABET_SIZE * N * M) - 最坏情况
 * 
 * 应用场景：
 * - 自动补全、拼写检查
 * - IP路由表、DNS解析
 * - 单词频率统计、前缀匹配
 * 
 * @see <a href="https://leetcode.cn/problems/implement-trie-ii-prefix-tree/">LeetCode 1804</a>
 */
// https://leetcode.cn/problems/implement-trie-ii-prefix-tree/
public class SimpleTrie {
    
    /**
     * Trie树的实现类
     * 包含完整的前缀树功能
     */
    private static class Trie {
        
        /**
         * Trie树节点类
         * 每个节点包含：
         * - pass: 通过该节点的字符串数量（用于前缀计数）
         * - end: 以该节点结尾的字符串数量（用于完整单词计数）
         * - nexts: 指向子节点的数组（26个字母）
         */
        public static class Node {
            public int pass;         // 经过此节点的字符串数量
            public int end;          // 以此节点为结尾的字符串数量
            public Node[] nexts;     // 子节点数组，索引0-25对应字母a-z

            /**
             * 构造函数：初始化节点
             * 创建26个子节点位置（对应a-z）
             */
            public Node() {
                nexts = new Node[26];  // 26个英文字母
            }
        }

        private Node root;  // 根节点

        /**
         * 构造函数：初始化Trie树
         * 创建一个空的根节点
         */
        public Trie() {
            root = new Node();
        }

        /**
         * 统计等于指定单词的字符串数量
         * 查找完全匹配的字符串在树中出现的次数
         * 
         * 算法步骤：
         * 1. 从根节点开始，按字符逐层向下遍历
         * 2. 如果某个字符对应的子节点不存在，返回0
         * 3. 如果能完整遍历到单词末尾，返回该节点的end值
         * 
         * @param word 要查询的单词
         * @return 该单词在树中出现的次数
         */
        private int countWordsEqualTo(String word) {
            if (word == null) {
                return 0;
            }
            
            char[] chs = word.toCharArray();
            Node node = root;  // 从根节点开始
            
            // 逐字符向下遍历
            for (int i = 0; i < chs.length; i++) {
                int ii = chs[i] - 'a';  // 计算字符索引（a=0, b=1, ...）
                if (node.nexts[ii] == null) {
                    return 0;  // 路径不存在，单词不在树中
                }
                node = node.nexts[ii];  // 移动到下一个节点
            }
            
            return node.end;  // 返回以此节点结尾的单词数量
        }

        /**
         * 统计以指定前缀开头的字符串数量
         * 查找所有具有指定前缀的字符串总数
         * 
         * 算法步骤：
         * 1. 从根节点开始，按前缀字符逐层向下遍历
         * 2. 如果某个字符对应的子节点不存在，返回0
         * 3. 如果能完整遍历到前缀末尾，返回该节点的pass值
         * 
         * 关键理解：
         * pass值表示经过该节点的所有字符串数量
         * 这正好等于以该路径为前缀的字符串总数
         * 
         * @param pre 要查询的前缀
         * @return 以该前缀开头的字符串数量
         */
        private int countWordsStartWith(String pre) {
            if (pre == null) {
                return 0;
            }
            
            char[] chs = pre.toCharArray();
            Node node = root;  // 从根节点开始
            
            // 逐字符向下遍历
            for (int i = 0; i < chs.length; i++) {
                int ii = chs[i] - 'a';  // 计算字符索引
                if (node.nexts[ii] == null) {
                    return 0;  // 前缀路径不存在
                }
                node = node.nexts[ii];  // 移动到下一个节点
            }
            
            return node.pass;  // 返回经过此节点的字符串数量
        }

        /**
         * 向Trie树中插入一个单词
         * 如果单词已存在，增加其出现次数
         * 
         * 算法步骤：
         * 1. 从根节点开始，增加pass计数
         * 2. 对于单词的每个字符：
         *    a) 如果对应子节点不存在，创建新节点
         *    b) 移动到子节点，增加pass计数
         * 3. 在最后一个节点增加end计数
         * 
         * 关键操作：
         * - pass++：记录经过该节点的字符串数量
         * - end++：记录以该节点结尾的字符串数量
         * 
         * 示例：插入"cat"
         * root(pass++) -> c(pass++) -> a(pass++) -> t(pass++, end++)
         * 
         * @param word 要插入的单词
         */
        public void insert(String word) {
            if (word == null) {
                return;
            }
            
            char[] str = word.toCharArray();
            Node node = root;
            node.pass++;  // 根节点pass增加
            int path = 0;
            
            // 遍历单词的每个字符
            for (int i = 0; i < str.length; i++) {
                path = str[i] - 'a';  // 计算字符路径索引
                
                // 如果路径不存在，创建新节点
                if (node.nexts[path] == null) {
                    node.nexts[path] = new Node();
                }
                
                node = node.nexts[path];  // 移动到下一个节点
                node.pass++;              // 增加经过计数
            }
            
            node.end++;  // 在单词结尾节点增加结束计数
        }

        /**
         * 从Trie树中删除一个单词
         * 如果单词出现多次，仅减少一次出现次数
         * 如果某条路径不再被其他单词使用，直接删除该路径
         * 
         * 算法步骤：
         * 1. 先检查单词是否存在（countWordsEqualTo != 0）
         * 2. 如果存在，从根节点开始减少pass计数
         * 3. 对于单词的每个字符：
         *    a) 如果减少后子节点的pass为0，直接删除子树
         *    b) 否则移动到子节点，继续减少pass计数
         * 4. 在最后一个节点减少end计数
         * 
         * 优化特性：
         * - 及时删除不再使用的节点，节省空间
         * - pass计数为0表示该路径不再被任何单词使用
         * 
         * 示例：删除"cat"（假设只出现一次）
         * 路径：root -> c -> a -> t
         * 如果没有其他单词使用c->a->t路径，整条路径会被删除
         * 
         * @param word 要删除的单词
         */
        public void erase(String word) {
            // 只有单词确实存在时才进行删除操作
            if (countWordsEqualTo(word) != 0) {
                char[] chs = word.toCharArray();
                Node node = root;
                node.pass--;  // 根节点pass减少
                int path = 0;
                
                // 遍历单词的每个字符
                for (int i = 0; i < chs.length; i++) {
                    path = chs[i] - 'a';  // 计算字符路径索引
                    
                    // 如果减少后pass为0，说明没有其他字符串使用这条路径
                    if (--node.nexts[path].pass == 0) {
                        node.nexts[path] = null;  // 直接删除整个子树
                        return;  // 提前结束，因为后续路径已被删除
                    }
                    
                    node = node.nexts[path];  // 移动到下一个节点
                }
                
                node.end--;  // 减少结尾计数
            }
        }
    }
}
