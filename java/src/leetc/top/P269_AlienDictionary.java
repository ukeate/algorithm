package leetc.top;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 269. 外星人字典 (Alien Dictionary)
 * 
 * 问题描述：
 * 现有一种使用字母的全新语言，这门语言的字母顺序与英语顺序不同。
 * 假设，你收到一个不为空的单词列表，该列表是按照这门新语言的字母顺序进行排序的。
 * 请你根据该单词列表，返回一个可能的字典序。如果不存在合法字典序，则返回空字符串。
 * 若存在多种可能的合法字典序，返回其中任意一种即可。
 * 
 * 示例：
 * 输入: ["wrt","wrf","er","ett","rftt"]
 * 输出: "wertf"
 * 解释: 根据单词的字典序，可以推断出：
 * - w < e (从"wrt"和"er"的比较)
 * - t < f (从"wrt"和"wrf"的比较)  
 * - r < t (从"er"和"ett"的比较)
 * - e < r (从"er"和"rftt"的比较)
 * 
 * 解法思路：
 * 拓扑排序 + 有向图构建：
 * 1. 通过相邻单词的比较，构建字符间的偏序关系
 * 2. 将偏序关系建模为有向图，节点是字符，边表示大小关系
 * 3. 使用拓扑排序得到字符的线性排序（字典序）
 * 4. 如果能完成拓扑排序，说明存在合法字典序；否则存在环，无解
 * 
 * 核心算法步骤：
 * 1. 构建图：通过相邻单词的第一个不同字符，建立字符间的大小关系
 * 2. 计算入度：统计每个字符作为目标节点的入边数量
 * 3. 拓扑排序：从入度为0的字符开始，逐步移除节点并更新入度
 * 4. 验证结果：最终排序包含所有字符则有解，否则存在环无解
 * 
 * 关键细节：
 * - 只比较相邻单词的第一个不同字符
 * - 处理特殊情况：前缀关系（如"abc"和"ab"）
 * - 避免重复添加相同的有向边
 * - 检测是否存在环（拓扑排序无法完成）
 * 
 * 时间复杂度：O(N + E) - N为字符数，E为边数，即拓扑排序的复杂度
 * 空间复杂度：O(N + E) - 存储图和入度信息
 * 
 * LeetCode链接：https://leetcode.com/problems/alien-dictionary/
 */
public class P269_AlienDictionary {
    
    /**
     * 根据外星人语言的单词序列推导字典序
     * 
     * 算法步骤：
     * 1. 收集所有出现的字符，初始化入度为0
     * 2. 通过相邻单词比较，构建字符间的偏序关系图
     * 3. 检查是否存在前缀冲突（长单词是短单词的前缀）
     * 4. 使用拓扑排序得到字符的线性排序
     * 5. 验证排序是否包含所有字符（检测环的存在）
     * 
     * @param words 按外星人字典序排列的单词数组
     * @return 可能的字典序字符串，无解返回空字符串
     */
    public static String alienORder(String[] words) {
        // 边界检查：空输入
        if (words == null || words.length == 0) {
            return "";
        }
        
        int n = words.length;
        // 统计所有字符的入度（初始化为0）
        HashMap<Character, Integer> indegree = new HashMap<>();
        
        // 第一步：收集所有出现的字符
        for (int i = 0; i < n; i++) {
            for (char c : words[i].toCharArray()) {
                indegree.put(c, 0);
            }
        }
        
        // 构建有向图：字符 -> 它后面的字符集合
        HashMap<Character, HashSet<Character>> graph = new HashMap<>();
        
        // 第二步：通过相邻单词比较构建偏序关系
        for (int i = 0; i < n - 1; i++) {
            char[] cur = words[i].toCharArray();   // 当前单词
            char[] nex = words[i + 1].toCharArray(); // 下一个单词
            int len = Math.min(cur.length, nex.length);
            int j = 0;
            
            // 找到第一个不同的字符位置
            for (; j < len; j++) {
                if (cur[j] != nex[j]) {
                    // 建立有向边：cur[j] -> nex[j]（cur[j] < nex[j]）
                    if (!graph.containsKey(cur[j])) {
                        graph.put(cur[j], new HashSet<>());
                    }
                    
                    // 避免重复添加相同的边
                    if (!graph.get(cur[j]).contains(nex[j])) {
                        graph.get(cur[j]).add(nex[j]);
                        // 更新目标字符的入度
                        indegree.put(nex[j], indegree.get(nex[j]) + 1);
                    }
                    break; // 找到第一个不同字符后退出
                }
            }
            
            // 特殊情况检查：如果cur是nex的前缀且cur更长，则无解
            // 例如：["abc", "ab"] 是无效的，因为"abc"不能排在"ab"前面
            if (j < cur.length && j == nex.length) {
                return ""; // 前缀冲突，无解
            }
        }
        
        // 第三步：拓扑排序
        StringBuilder ans = new StringBuilder();
        Queue<Character> que = new LinkedList<>();
        
        // 将所有入度为0的字符加入队列
        for (Character key : indegree.keySet()) {
            if (indegree.get(key) == 0) {
                que.offer(key);
            }
        }
        
        // 执行拓扑排序
        while (!que.isEmpty()) {
            char cur = que.poll();  // 取出入度为0的字符
            ans.append(cur);        // 加入结果序列
            
            // 更新所有邻接字符的入度
            if (graph.containsKey(cur)) {
                for (char next : graph.get(cur)) {
                    indegree.put(next, indegree.get(next) - 1);
                    // 如果某个字符的入度变为0，加入队列
                    if (indegree.get(next) == 0) {
                        que.offer(next);
                    }
                }
            }
        }
        
        // 第四步：验证结果
        // 如果拓扑排序包含了所有字符，说明无环，有解
        // 否则存在环，无解
        return ans.length() == indegree.size() ? ans.toString() : "";
    }
}
