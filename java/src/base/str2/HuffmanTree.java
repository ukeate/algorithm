package base.str2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * 哈夫曼编码算法实现
 * 问题描述：为给定的字符频率表构建最优的前缀编码
 * 
 * 算法原理：
 * 1. 根据字符频率构建哈夫曼树（最优二叉树）
 * 2. 频率高的字符获得较短的编码
 * 3. 确保任何字符的编码都不是另一个字符编码的前缀
 * 
 * 核心思想：
 * - 使用优先队列（最小堆）贪心地构建树
 * - 每次选择频率最小的两个节点合并
 * - 最终得到的树满足最优性：平均编码长度最短
 * 
 * 算法特点：
 * - 时间复杂度：O(N*log(N)) - N为字符种类数
 * - 空间复杂度：O(N) - 存储树结构和编码表
 * - 压缩率：根据字符频率分布，通常能达到较好的压缩效果
 * 
 * 应用场景：
 * - 文件压缩（ZIP、GZIP等）
 * - 数据传输优化
 * - 图像压缩（JPEG中的一部分）
 * - 网络协议优化
 */
public class HuffmanTree {

    /**
     * 统计字符串中每个字符的出现频次
     * 这是构建哈夫曼树的第一步：获取字符频率表
     * 
     * @param str 输入字符串
     * @return 字符到频次的映射表
     */
    public static HashMap<Character, Integer> countMap(String str) {
        HashMap<Character, Integer> ans = new HashMap<>();
        char[] s = str.toCharArray();
        
        // 遍历字符串，统计每个字符的出现次数
        for (char cha : s) {
            if (!ans.containsKey(cha)) {
                ans.put(cha, 1);          // 首次出现
            } else {
                ans.put(cha, ans.get(cha) + 1);  // 增加计数
            }
        }
        return ans;
    }

    /**
     * 哈夫曼树的节点类
     * 既可以表示叶子节点（字符），也可以表示内部节点
     */
    private static class Node {
        public int count;      // 节点权重（频次）
        public Node left;      // 左子节点
        public Node right;     // 右子节点

        public Node(int c) {
            count = c;
        }
    }

    /**
     * 节点比较器：用于优先队列的排序
     * 按照节点的频次进行升序排列（最小堆）
     */
    private static class NodeComp implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return o1.count - o2.count;  // 频次小的优先级高
        }
    }

    /**
     * 递归填充哈夫曼编码表
     * 通过深度优先遍历哈夫曼树，为每个字符分配编码
     * 
     * 编码规则：
     * - 向左走添加'0'
     * - 向右走添加'1'
     * - 到达叶子节点时，记录完整编码
     * 
     * @param head 当前遍历的节点
     * @param pre 当前路径的编码前缀
     * @param nodes 节点到字符的映射（用于识别叶子节点）
     * @param ans 最终的编码表
     */
    private static void fill(Node head, String pre, HashMap<Node, Character> nodes, HashMap<Character, String> ans) {
        if (nodes.containsKey(head)) {
            // 当前节点是叶子节点（包含字符）
            ans.put(nodes.get(head), pre);
        } else {
            // 当前节点是内部节点，继续递归
            fill(head.left, pre + "0", nodes, ans);   // 左子树：添加'0'
            fill(head.right, pre + "1", nodes, ans);  // 右子树：添加'1'
        }
    }

    /**
     * 根据字符频率表构建哈夫曼编码
     * 这是整个算法的核心函数
     * 
     * 算法步骤：
     * 1. 将所有字符作为叶子节点加入优先队列
     * 2. 重复执行：取出频次最小的两个节点，合并成新节点
     * 3. 直到队列中只剩一个节点（根节点）
     * 4. 遍历树结构，为每个字符分配二进制编码
     * 
     * 特殊情况处理：
     * - 只有一个字符时，分配编码"0"（避免空编码）
     * 
     * @param countMap 字符频率表
     * @return 字符到哈夫曼编码的映射
     */
    public static HashMap<Character, String> createHuffman(HashMap<Character, Integer> countMap) {
        HashMap<Character, String> ans = new HashMap<>();
        
        // 特殊情况：只有一个字符
        if (countMap.size() == 1) {
            for (char key : countMap.keySet()) {
                ans.put(key, "0");  // 单字符编码为"0"
            }
            return ans;
        }
        
        // 建立节点到字符的映射（用于识别叶子节点）
        HashMap<Node, Character> nodes = new HashMap<>();
        
        // 创建优先队列（最小堆）
        PriorityQueue<Node> heap = new PriorityQueue<>(new NodeComp());
        
        // 为每个字符创建叶子节点并加入堆
        for (Map.Entry<Character, Integer> entry : countMap.entrySet()) {
            Node cur = new Node(entry.getValue());  // 创建节点
            char cha = entry.getKey();
            nodes.put(cur, cha);                    // 记录节点-字符映射
            heap.add(cur);                          // 加入优先队列
        }
        
        // 构建哈夫曼树：不断合并频次最小的两个节点
        while (heap.size() > 1) {
            Node a = heap.poll();  // 取出频次最小的节点
            Node b = heap.poll();  // 取出频次第二小的节点
            
            // 创建新的内部节点
            Node h = new Node(a.count + b.count);  // 频次为两子节点之和
            h.left = a;                            // 频次小的作为左子节点
            h.right = b;                           // 频次大的作为右子节点
            
            heap.add(h);  // 将新节点重新加入堆
        }
        
        // 获取根节点并生成编码表
        Node head = heap.poll();
        fill(head, "", nodes, ans);  // 从根节点开始填充编码
        
        return ans;
    }

    /**
     * 使用哈夫曼编码对字符串进行编码
     * 将原始字符串转换为二进制编码串
     * 
     * @param str 原始字符串
     * @param huffman 哈夫曼编码表
     * @return 编码后的二进制字符串
     */
    public static String encode(String str, HashMap<Character, String> huffman) {
        char[] s = str.toCharArray();
        StringBuilder builder = new StringBuilder();
        
        // 逐字符编码并拼接
        for (char cha : s) {
            builder.append(huffman.get(cha));  // 查找并添加字符的编码
        }
        
        return builder.toString();
    }

    /**
     * 解码用的Trie树节点
     * 专门用于构建解码树，支持快速解码
     */
    private static class TrieNode {
        public char val;              // 叶子节点存储的字符（内部节点为0）
        public TrieNode[] nexts;      // 子节点数组：[0]表示'0', [1]表示'1'
        
        public TrieNode() {
            val = 0;                  // 初始值为0
            nexts = new TrieNode[2];  // 二进制：只有两个子节点
        }
    }

    /**
     * 根据哈夫曼编码表构建解码用的Trie树
     * 将编码表转换为便于解码的树形结构
     * 
     * 构建过程：
     * 1. 为每个字符的编码路径在树中创建对应的路径
     * 2. 在路径的末尾节点存储对应的字符
     * 3. '0'对应左子节点，'1'对应右子节点
     * 
     * @param huffman 哈夫曼编码表
     * @return 解码树的根节点
     */
    private static TrieNode createTrie(HashMap<Character, String> huffman) {
        TrieNode root = new TrieNode();
        
        // 为每个字符-编码对构建树路径
        for (char key : huffman.keySet()) {
            char[] path = huffman.get(key).toCharArray();  // 获取字符的编码路径
            TrieNode cur = root;
            
            // 沿着编码路径在树中前进/创建节点
            for (int i = 0; i < path.length; i++) {
                int idx = path[i] == '0' ? 0 : 1;  // '0'->0, '1'->1
                
                if (cur.nexts[idx] == null) {
                    cur.nexts[idx] = new TrieNode();  // 创建新节点
                }
                cur = cur.nexts[idx];  // 移动到下一个节点
            }
            
            cur.val = key;  // 在路径末尾存储字符
        }
        
        return root;
    }

    /**
     * 使用哈夫曼编码对二进制串进行解码
     * 将编码后的二进制字符串还原为原始字符串
     * 
     * 解码过程：
     * 1. 构建解码用的Trie树
     * 2. 从根节点开始，根据二进制位选择路径
     * 3. 到达叶子节点时，输出字符并回到根节点
     * 4. 继续处理剩余的二进制位
     * 
     * 关键特性：
     * - 由于哈夫曼编码是前缀编码，解码过程无歧义
     * - 遇到叶子节点立即可以确定一个字符
     * 
     * @param raw 编码后的二进制字符串
     * @param huffman 哈夫曼编码表
     * @return 解码后的原始字符串
     */
    public static String decode(String raw, HashMap<Character, String> huffman) {
        TrieNode root = createTrie(huffman);  // 构建解码树
        TrieNode cur = root;                  // 当前位置从根节点开始
        char[] encode = raw.toCharArray();
        StringBuilder builder = new StringBuilder();
        
        // 逐位处理编码串
        for (int i = 0; i < encode.length; i++) {
            int idx = encode[i] == '0' ? 0 : 1;  // 确定移动方向
            cur = cur.nexts[idx];                // 在树中移动
            
            // 检查是否到达叶子节点
            if (cur.nexts[0] == null && cur.nexts[1] == null) {
                builder.append(cur.val);  // 输出解码的字符
                cur = root;               // 重新从根节点开始
            }
        }
        
        return builder.toString();
    }

    /**
     * 生成指定长度和字符范围的随机字符串
     * 用于测试哈夫曼编码算法
     * 
     * @param len 字符串长度
     * @param range 字符种类数（从'a'开始）
     * @return 随机生成的字符串
     */
    private static String randomStr(int len, int range) {
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ((int) (range) * Math.random() + 'a');
        }
        return String.valueOf(str);
    }

    /**
     * 测试哈夫曼编码的正确性和效果
     * 包含预设样例测试和大规模随机测试
     */
    public static void main(String[] args) {
        // 预设测试数据：模拟实际字符频率分布
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('A', 60);   // 高频字符
        map.put('B', 45);
        map.put('C', 13);   // 低频字符
        map.put('D', 69);   // 最高频字符
        map.put('E', 14);
        map.put('F', 5);    // 最低频字符
        map.put('G', 3);
        
        // 构建哈夫曼编码并输出
        HashMap<Character, String> huffman = createHuffman(map);
        System.out.println("=== 哈夫曼编码表 ===");
        for (Map.Entry<Character, String> entry : huffman.entrySet()) {
            System.out.println("字符 '" + entry.getKey() + "' -> 编码: " + entry.getValue());
        }
        
        System.out.println("\n=== 编码解码测试 ===");
        String str = "CBBBAABBACAABDDEFBA";
        System.out.println("原始字符串: " + str);
        
        // 根据实际字符串构建编码表
        HashMap<Character, Integer> countMap = countMap(str);
        HashMap<Character, String> hf = createHuffman(countMap);
        
        // 编码过程
        String encode = encode(str, hf);
        System.out.println("编码结果: " + encode);
        System.out.println("原始长度: " + str.length() * 8 + " 位");
        System.out.println("编码长度: " + encode.length() + " 位");
        System.out.println("压缩率: " + String.format("%.2f%%", 
                         (1.0 - (double)encode.length() / (str.length() * 8)) * 100));
        
        // 解码过程
        String decode = decode(encode, hf);
        System.out.println("解码结果: " + decode);
        System.out.println("编解码正确性: " + str.equals(decode));
        
        System.out.println("\n=== 大规模随机测试 ===");
        int times = 100000;   // 测试次数
        int maxLen = 500;     // 最大字符串长度
        int range = 26;       // 字符种类数（a-z）
        
        System.out.println("测试参数: 次数=" + times + ", 最大长度=" + maxLen + 
                         ", 字符种类=" + range);
        
        for (int i = 0; i < times; i++) {
            int n = (int) ((maxLen) * Math.random()) + 1;
            String test = randomStr(n, range);
            
            // 编码解码测试
            HashMap<Character, Integer> counts = countMap(test);
            HashMap<Character, String> hf2 = createHuffman(counts);
            String encode2 = encode(test, hf2);
            String decode2 = decode(encode2, hf2);
            
            // 验证正确性
            if (!test.equals(decode2)) {
                System.out.println("发现错误！");
                System.out.println("原始: " + test);
                System.out.println("编码: " + encode2);
                System.out.println("解码: " + decode2);
                return;
            }
            
            // 每10000次输出进度
            if ((i + 1) % 10000 == 0) {
                System.out.println("已完成 " + (i + 1) + " 次测试");
            }
        }
        
        System.out.println("大规模测试完成！所有 " + times + " 次测试都通过");
        System.out.println("哈夫曼编码算法实现正确");
    }
}
