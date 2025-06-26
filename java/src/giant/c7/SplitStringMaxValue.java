package giant.c7;

import java.util.HashMap;

/**
 * 字符串分割最高得分问题
 * 
 * 问题描述：
 * 给定一个字符串str和一个整数k，以及一些贴纸片段parts和对应的得分record。
 * 要求在k步内将字符串完全拼接出来，求能获得的最高总得分。
 * 
 * 例如：
 * str = "abcdefg", k = 3
 * parts = ["abc", "def", "g", "ab", "cd", "efg", "defg"]
 * record = [1, 1, 1, 3, 3, 3, 2]
 * 
 * 可能的分割方案：
 * - "abc" + "def" + "g" = 1 + 1 + 1 = 3分（3步）
 * - "ab" + "cd" + "efg" = 3 + 3 + 3 = 9分（3步）
 * 最优解是9分
 * 
 * 解决方案：
 * 1. 方法1：递归暴力法 - 时间复杂度O(2^N)
 * 2. 方法2：动态规划法 - 时间复杂度O(N^2*K)
 * 3. 方法3：前缀树优化法 - 时间复杂度O(N*K*L)，L是最长贴纸长度
 * 
 * 核心思想：
 * 使用动态规划，dp[i][j]表示从位置i开始，用j步拼接完剩余字符串的最大得分
 * 状态转移：尝试所有可能的贴纸片段，选择最优方案
 * 
 * 时间复杂度：O(N*K*L) - 方法3优化版本
 * 空间复杂度：O(N*K + 贴纸总长度)
 */
public class SplitStringMaxValue {
    
    /**
     * 递归处理函数（方法1使用）
     * 
     * 算法思路：
     * 从当前位置开始，尝试所有可能长度的子串
     * 如果子串在贴纸中存在，递归处理剩余部分
     * 
     * @param str 目标字符串
     * @param idx 当前处理的位置
     * @param rest 剩余可用步数
     * @param records 贴纸得分映射表
     * @return 从当前位置开始能获得的最大得分，-1表示无法完成
     */
    private static int process(String str, int idx, int rest, HashMap<String, Integer> records) {
        if (rest < 0) {
            return -1;  // 步数不够，无法完成
        }
        if (idx == str.length()) {
            return rest == 0 ? 0 : -1;  // 字符串处理完毕，检查步数是否恰好用完
        }
        
        int ans = -1;
        // 尝试从当前位置开始的所有可能子串
        for (int end = idx; end < str.length(); end++) {
            String first = str.substring(idx, end + 1);  // 当前尝试的子串
            
            // 如果这个子串存在于贴纸中，递归处理剩余部分
            int next = records.containsKey(first) ? 
                      process(str, end + 1, rest - 1, records) : -1;
            
            if (next != -1) {
                // 如果剩余部分可以完成，更新最大得分
                ans = Math.max(ans, records.get(first) + next);
            }
        }
        return ans;
    }

    /**
     * 方法1：递归暴力法
     * 
     * 算法思路：
     * 使用递归尝试所有可能的分割方案
     * 对每个位置，尝试所有可能的贴纸片段
     * 
     * 时间复杂度：O(2^N)，指数级复杂度
     * 空间复杂度：O(N)，递归调用栈
     * 
     * 适用场景：字符串长度很小时使用
     * 
     * @param str 目标字符串
     * @param k 最大步数
     * @param parts 贴纸片段数组
     * @param record 对应的得分数组
     * @return 能获得的最高总得分，-1表示无法完成
     */
    public static int max1(String str, int k, String[] parts, int[] record) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        
        // 构建贴纸得分映射表
        HashMap<String, Integer> records = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            records.put(parts[i], record[i]);
        }
        
        return process(str, 0, k, records);
    }

    /**
     * 方法2：动态规划法
     * 
     * 算法思路：
     * dp[idx][rest]表示从位置idx开始，用rest步完成剩余字符串的最大得分
     * 状态转移：对每个位置尝试所有可能的贴纸片段
     * 
     * 边界条件：
     * - dp[n][0] = 0（字符串处理完且步数用完）
     * - dp[n][rest] = -1（字符串处理完但还有剩余步数）
     * 
     * 状态转移方程：
     * dp[idx][rest] = max(record[part] + dp[idx+len(part)][rest-1])
     * 对所有可能的贴纸片段part
     * 
     * 时间复杂度：O(N^2*K)
     * 空间复杂度：O(N*K)
     * 
     * @param str 目标字符串
     * @param k 最大步数
     * @param parts 贴纸片段数组
     * @param record 对应的得分数组
     * @return 能获得的最高总得分，-1表示无法完成
     */
    public static int max2(String str, int k, String[] parts, int[] record) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        
        // 构建贴纸得分映射表
        HashMap<String, Integer> records = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            records.put(parts[i], record[i]);
        }
        
        int n = str.length();
        // dp[idx][rest]：从位置idx开始，用rest步完成的最大得分
        int[][] dp = new int[n + 1][k + 1];
        
        // 初始化边界条件
        for (int rest = 1; rest <= k; rest++) {
            dp[n][rest] = -1;  // 字符串处理完但步数有剩余，无法达成
        }
        // dp[n][0] = 0 已经是默认值，表示字符串处理完且步数用完
        
        // 从后往前填表
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= k; rest++) {
                int ans = -1;
                
                // 尝试从当前位置开始的所有可能子串
                for (int end = idx; end < n; end++) {
                    String first = str.substring(idx, end + 1);
                    
                    // 检查是否有足够步数且贴纸存在
                    int next = rest > 0 && records.containsKey(first) ? 
                              dp[end + 1][rest - 1] : -1;
                    
                    if (next != -1) {
                        ans = Math.max(ans, records.get(first) + next);
                    }
                }
                dp[idx][rest] = ans;
            }
        }
        
        return dp[0][k];
    }

    /**
     * 前缀树节点类
     * 用于优化字符串匹配过程
     */
    private static class Node {
        public Node[] nexts;  // 子节点数组，对应26个小写字母
        public int val;       // 该节点对应贴纸的得分，-1表示不是贴纸结尾

        public Node() {
            nexts = new Node[26];
            val = -1;
        }
    }

    /**
     * 构建前缀树
     * 
     * 将所有贴纸片段构建成前缀树，加速匹配过程
     * 
     * @param parts 贴纸片段数组
     * @param record 对应的得分数组
     * @return 前缀树的根节点
     */
    private static Node rootNode(String[] parts, int[] record) {
        Node root = new Node();
        
        for (int i = 0; i < parts.length; i++) {
            char[] str = parts[i].toCharArray();
            Node cur = root;
            
            // 沿着前缀树插入当前贴纸
            for (int j = 0; j < str.length; j++) {
                int path = str[j] - 'a';  // 计算字符对应的路径
                if (cur.nexts[path] == null) {
                    cur.nexts[path] = new Node();
                }
                cur = cur.nexts[path];
            }
            cur.val = record[i];  // 在贴纸结尾节点记录得分
        }
        
        return root;
    }

    /**
     * 方法3：前缀树优化法
     * 
     * 算法思路：
     * 1. 将所有贴纸构建成前缀树，提高匹配效率
     * 2. 使用动态规划，dp[idx][rest]表示从位置idx开始用rest步的最大得分
     * 3. 对每个位置，沿着前缀树尝试所有可能的匹配
     * 
     * 优化点：
     * - 前缀树避免了重复的字符串比较
     * - 可以在匹配过程中提前终止不可能的分支
     * - 空间局部性更好，缓存友好
     * 
     * 时间复杂度：O(N*K*L)，L是最长贴纸的长度
     * 空间复杂度：O(N*K + 贴纸总长度)
     * 
     * @param s 目标字符串
     * @param k 最大步数
     * @param parts 贴纸片段数组
     * @param record 对应的得分数组
     * @return 能获得的最高总得分，-1表示无法完成
     */
    public static int max3(String s, int k, String[] parts, int[] record) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // 构建前缀树
        Node root = rootNode(parts, record);
        char[] str = s.toCharArray();
        int n = str.length;
        
        // dp[idx][rest]：从位置idx开始，用rest步完成的最大得分
        int[][] dp = new int[n + 1][k + 1];
        
        // 初始化边界条件
        for (int rest = 1; rest <= k; rest++) {
            dp[n][rest] = -1;
        }
        
        // 从后往前填表
        for (int idx = n - 1; idx >= 0; idx--) {
            for (int rest = 0; rest <= k; rest++) {
                int ans = -1;
                Node cur = root;
                
                // 从当前位置开始，沿着前缀树尝试匹配
                for (int end = idx; end < n; end++) {
                    int path = str[end] - 'a';
                    
                    // 如果前缀树中没有对应路径，停止尝试
                    if (cur.nexts[path] == null) {
                        break;
                    }
                    
                    cur = cur.nexts[path];
                    
                    // 如果当前节点是某个贴纸的结尾且有足够步数
                    int next = rest > 0 && cur.val != -1 ? 
                              dp[end + 1][rest - 1] : -1;
                    
                    if (next != -1) {
                        ans = Math.max(ans, cur.val + next);
                    }
                }
                dp[idx][rest] = ans;
            }
        }
        
        return dp[0][k];
    }

    /**
     * 测试方法：验证三种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串分割最高得分问题测试 ===");
        
        // 测试用例
        String str = "abcdefg";
        int k = 3;
        String[] parts = {"abc", "def", "g", "ab", "cd", "efg", "defg"};
        int[] record = {1, 1, 1, 3, 3, 3, 2};
        
        System.out.println("测试参数:");
        System.out.println("目标字符串: " + str);
        System.out.println("最大步数: " + k);
        System.out.println("贴纸片段: " + java.util.Arrays.toString(parts));
        System.out.println("对应得分: " + java.util.Arrays.toString(record));
        System.out.println();
        
        System.out.println("算法结果:");
        
        long start = System.currentTimeMillis();
        int result1 = max1(str, k, parts, record);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = max2(str, k, parts, record);
        long time2 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result3 = max3(str, k, parts, record);
        long time3 = System.currentTimeMillis() - start;
        
        System.out.println("方法1（递归暴力）: " + result1 + ", 耗时: " + time1 + "ms");
        System.out.println("方法2（动态规划）: " + result2 + ", 耗时: " + time2 + "ms");
        System.out.println("方法3（前缀树优化）: " + result3 + ", 耗时: " + time3 + "ms");
        
        // 验证结果一致性
        if (result1 == result2 && result2 == result3) {
            System.out.println("✓ 三种方法结果一致，算法正确！");
        } else {
            System.out.println("✗ 算法结果不一致，存在错误！");
        }
        
        System.out.println();
        System.out.println("最优方案分析:");
        System.out.println("\"ab\" + \"cd\" + \"efg\" = 3 + 3 + 3 = 9分（3步）");
        System.out.println("这是3步内能达到的最高得分");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
