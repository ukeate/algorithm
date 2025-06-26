package basic.c43;

import java.util.HashSet;

/**
 * 单词拆分方案数问题（Word Break II - Count Ways）
 * 
 * 问题描述：
 * 给定一个字符串s和一个词典数组arr，词典中的单词可以重复使用
 * 计算有多少种方式可以将字符串s拆分成词典中的单词
 * 
 * 例如：s = "catsanddog", arr = ["cat", "cats", "and", "sand", "dog"]
 * 可能的拆分方式：["cat", "sand", "dog"] 和 ["cats", "and", "dog"]
 * 返回方案数：2
 * 
 * 算法思路：
 * 方法1：递归暴力 + HashSet - O(2^n)
 * 方法2：动态规划 + HashSet - O(n³)
 * 方法3：递归 + Trie树 - O(2^n)，但常数更优
 * 方法4：动态规划 + Trie树 - O(n²)，最优解法
 * 
 * 核心优化：
 * - DP优化：避免重复子问题
 * - Trie树优化：快速匹配前缀，避免不必要的substring操作
 * 
 * @author 算法学习
 */
public class WordBreak {
    
    /**
     * 方法1：递归暴力解法 + HashSet
     * 
     * @param str 当前字符串
     * @param i 当前处理位置
     * @param set 词典集合
     * @return 从位置i开始的拆分方案数
     * 
     * 递归思路：
     * 对于每个位置i，尝试所有可能的结束位置end
     * 如果str[i..end]在词典中，则递归处理str[end+1..]
     */
    private static int process(String str, int i, HashSet<String> set) {
        // 递归边界：已经处理完整个字符串
        if (i == str.length()) {
            return 1;
        }
        
        int ways = 0;
        // 尝试所有可能的结束位置
        for (int end = i; end < str.length(); end++) {
            String pre = str.substring(i, end + 1);
            if (set.contains(pre)) {
                // 如果当前子串在词典中，递归处理剩余部分
                ways += process(str, end + 1, set);
            }
        }
        return ways;
    }

    /**
     * 方法1的入口函数
     * 
     * @param str 目标字符串
     * @param arr 词典数组
     * @return 拆分方案数
     */
    public static int ways1(String str, String[] arr) {
        HashSet<String> set = new HashSet<>();
        for (String w : arr) {
            set.add(w);
        }
        return process(str, 0, set);
    }

    /**
     * 方法2：动态规划优化 + HashSet
     * 
     * @param str 目标字符串
     * @param arr 词典数组
     * @return 拆分方案数
     * 
     * DP状态定义：dp[i] = 从位置i开始到字符串末尾的拆分方案数
     * 状态转移：dp[i] = sum(dp[end+1]) for all valid end
     * 
     * 时间复杂度：O(n³) - n²个子串，每个substring操作O(n)
     * 空间复杂度：O(n + m) - DP数组 + HashSet
     */
    public static int ways2(String str, String[] arr) {
        if (str == null || str.length() == 0 || arr == null || arr.length == 0) {
            return 0;
        }
        
        HashSet<String> set = new HashSet<>();
        for (String s : arr) {
            set.add(s);
        }
        
        int n = str.length();
        int[] dp = new int[n + 1];
        dp[n] = 1; // 边界：空字符串有1种拆分方式
        
        // 从后往前填DP表
        for (int i = n - 1; i >= 0; i--) {
            for (int end = i; end < n; end++) {
                if (set.contains(str.substring(i, end + 1))) {
                    dp[i] += dp[end + 1];
                }
            }
        }
        return dp[0];
    }

    /**
     * Trie树节点定义
     * 用于优化字符串匹配
     */
    private static class Node {
        public boolean end;     // 标记是否为单词结尾
        public Node[] nexts;    // 26个字母的子节点
        
        public Node() {
            end = false;
            nexts = new Node[26];
        }
    }

    /**
     * 方法3：递归 + Trie树优化
     * 
     * @param str 字符数组形式的字符串
     * @param root Trie树根节点
     * @param i 当前处理位置
     * @return 从位置i开始的拆分方案数
     * 
     * Trie树优化：
     * 1. 避免重复的substring操作
     * 2. 可以提前终止不匹配的分支
     * 3. 一次遍历就能找到所有匹配的前缀
     */
    private static int process3(char[] str, Node root, int i) {
        if (i == str.length) {
            return 1;
        }
        
        int ways = 0;
        Node cur = root;
        
        // 从当前位置开始，沿着Trie树寻找匹配的单词
        for (int end = i; end < str.length; end++) {
            int path = str[end] - 'a';
            if (cur.nexts[path] == null) {
                // Trie树中没有这个路径，提前终止
                break;
            }
            cur = cur.nexts[path];
            if (cur.end) {
                // 找到一个完整单词，递归处理剩余部分
                ways += process3(str, root, end + 1);
            }
        }
        return ways;
    }

    /**
     * 方法3的入口函数：递归 + Trie树
     * 
     * @param str 目标字符串
     * @param arr 词典数组
     * @return 拆分方案数
     */
    public static int ways3(String str, String[] arr) {
        if (str == null ||str.length() == 0 || arr == null || arr.length == 0) {
            return 0;
        }
        
        // 构建Trie树
        Node root = new Node();
        for (String s : arr) {
            char[] ss = s.toCharArray();
            Node node = root;
            for (int i = 0; i < ss.length; i++) {
                int idx = ss[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            node.end = true; // 标记单词结尾
        }
        
        return process3(str.toCharArray(), root, 0);
    }

    /**
     * 方法4：动态规划 + Trie树优化（最优解法）
     * 
     * @param s 目标字符串
     * @param arr 词典数组
     * @return 拆分方案数
     * 
     * 结合DP和Trie的优势：
     * - DP避免重复子问题
     * - Trie树优化字符串匹配
     * 
     * 时间复杂度：O(n²) - 对每个位置，最多遍历n个字符
     * 空间复杂度：O(n + m*k) - DP数组 + Trie树
     */
    public static int ways4(String s, String[] arr) {
        if (s == null || s.length() == 0 || arr == null || arr.length == 0) {
            return 0;
        }
        
        // 构建Trie树
        Node root = new Node();
        for (String str : arr) {
            char[] ss = str.toCharArray();
            Node node = root;
            for (int i = 0; i < ss.length; i++) {
                int idx = ss[i] - 'a';
                if (node.nexts[idx] == null) {
                    node.nexts[idx] = new Node();
                }
                node = node.nexts[idx];
            }
            node.end = true;
        }
        
        char[] str = s.toCharArray();
        int n = str.length;
        int[] dp = new int[n + 1];
        dp[n] = 1; // 边界条件
        
        // 从后往前填DP表，结合Trie树匹配
        for (int i = n - 1; i >= 0; i--) {
            Node cur = root;
            for (int end = i; end < n; end++) {
                int path = str[end] - 'a';
                if (cur.nexts[path] == null) {
                    break; // Trie树提前终止
                }
                cur = cur.nexts[path];
                if (cur.end) {
                    dp[i] += dp[end + 1];
                }
            }
        }
        return dp[0];
    }

    /**
     * 测试样本类
     */
    private static class Sample {
        public String str;
        public String[] arr;
        
        public Sample(String s, String[] a) {
            str = s;
            arr = a;
        }
    }

    /**
     * 生成随机词典
     * 
     * @param candidates 候选字符集
     * @param num 词典大小
     * @param len 单词最大长度
     * @return 随机词典
     */
    private static String[] randomSeeds(char[] candidates, int num, int len) {
        String[] arr = new String[(int) (num * Math.random()) + 1];
        for (int i = 0; i < arr.length; i++) {
            char[] str = new char[(int) (len * Math.random()) + 1];
            for (int j = 0; j < str.length; j++) {
                str[j] = candidates[(int) (candidates.length * Math.random())];
            }
            arr[i] = String.valueOf(str);
        }
        return arr;
    }

    /**
     * 生成随机测试样本
     * 目标字符串由词典中的单词拼接而成，保证有解
     * 
     * @param candidates 候选字符集
     * @param num 词典大小
     * @param len 单词最大长度
     * @param joint 目标字符串的拼接次数
     * @return 测试样本
     */
    private static Sample randomSample(char[] candidates, int num, int len, int joint) {
        String[] seeds = randomSeeds(candidates, num, len);
        
        // 去重
        HashSet<String> set = new HashSet<>();
        for (String str : seeds) {
            set.add(str);
        }
        String[] arr = new String[set.size()];
        int idx = 0;
        for (String str : set) {
            arr[idx++] = str;
        }
        
        // 生成目标字符串（随机拼接词典中的单词）
        StringBuilder all = new StringBuilder();
        for (int i = 0; i < joint; i++) {
            all.append(arr[(int) (Math.random() * arr.length)]);
        }
        
        return new Sample(all.toString(), arr);
    }

    /**
     * 测试方法：验证四种算法的正确性
     */
    public static void main(String[] args) {
        int times = 30000;
        char[] candidates = {'a', 'b'}; // 候选字符集
        int num = 20;    // 词典大小
        int len = 4;     // 单词最大长度
        int joint = 5;   // 拼接次数
        
        System.out.println("测试开始...");
        for (int i = 0; i < times; i++) {
            Sample sample = randomSample(candidates, num, len, joint);
            int ans1 = ways1(sample.str, sample.arr);
            int ans2 = ways2(sample.str, sample.arr);
            int ans3 = ways3(sample.str, sample.arr);
            int ans4 = ways4(sample.str, sample.arr);
            
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans4) {
                System.out.println("算法错误!");
                System.out.println("结果: " + ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                System.out.println("字符串: " + sample.str);
                System.out.println("词典: " + java.util.Arrays.toString(sample.arr));
                break;
            }
        }
        System.out.println("测试结束");
        
        // 手动测试示例
        System.out.println("\n手动测试:");
        String testStr = "catsanddog";
        String[] testArr = {"cat", "cats", "and", "sand", "dog"};
        System.out.println("字符串: " + testStr);
        System.out.println("词典: " + java.util.Arrays.toString(testArr));
        System.out.println("方案数: " + ways4(testStr, testArr));
    }
}
