package leetc.top;

import java.util.*;

/**
 * LeetCode 127. 单词接龙 (Word Ladder)
 * 
 * 问题描述：
 * 给定两个单词（beginWord 和 endWord）和一个字典，找出从 beginWord 到 endWord 的最短转换序列的长度。
 * 转换需遵循如下规则：
 * 1. 每次转换只能改变一个字母
 * 2. 转换过程中的中间单词必须是字典中的单词
 * 
 * 解法思路：
 * 方法一：标准BFS
 * 1. 预处理构建邻接表
 * 2. BFS遍历，记录距离
 * 3. 遇到目标单词时返回距离
 * 
 * 方法二：双向BFS优化
 * 1. 从起点和终点同时开始BFS
 * 2. 每次选择较小的集合进行扩展
 * 3. 当两个搜索相遇时返回结果
 * 
 * 双向BFS优势：
 * - 搜索空间从O(b^d)减少到O(b^(d/2))
 * - 特别适用于搜索深度较大的情况
 * - 每次扩展较小集合提高效率
 * 
 * 算法核心：
 * - next(): 生成相邻单词
 * - nexts(): 构建邻接表
 * - BFS遍历寻找最短路径
 * 
 * 时间复杂度：O(N*26*M) - N为单词数，M为单词长度
 * 空间复杂度：O(N*M) - 邻接表存储空间
 * 
 * LeetCode链接：https://leetcode.com/problems/word-ladder/
 */
public class P127_WordLadder {
    
    /**
     * 获取与指定单词相差一个字母的所有有效单词
     * 
     * @param word 当前单词
     * @param dict 字典集合
     * @return 相邻单词列表
     */
    private static ArrayList<String> next(String word, HashSet<String> dict) {
        ArrayList<String> res = new ArrayList<>();
        char[] chs = word.toCharArray();
        
        // 尝试替换每个位置的每个字母
        for (char cur = 'a'; cur <= 'z'; cur++) {
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] != cur) {  // 只有当字母不同时才替换
                    char tmp = chs[i];
                    chs[i] = cur;
                    
                    // 检查新单词是否在字典中
                    if (dict.contains(String.valueOf(chs))) {
                        res.add(String.valueOf(chs));
                    }
                    
                    chs[i] = tmp;  // 恢复原字母
                }
            }
        }
        return res;
    }

    /**
     * 为所有单词构建邻接表
     * 
     * @param words 单词列表
     * @return 邻接表映射
     */
    private static HashMap<String, ArrayList<String>> nexts(List<String> words) {
        HashSet<String> dict = new HashSet<>(words);
        HashMap<String, ArrayList<String>> nexts = new HashMap<>();
        
        // 为每个单词计算其邻接单词
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), next(words.get(i), dict));
        }
        return nexts;
    }

    /**
     * 方法一：标准BFS求最短路径长度
     * 
     * @param start 开始单词
     * @param to 目标单词
     * @param list 字典单词列表
     * @return 最短转换序列长度，无法转换返回0
     */
    public static int ladderLength(String start, String to, List<String> list) {
        list.add(start);  // 将起始单词加入字典
        
        // 构建邻接表
        HashMap<String, ArrayList<String>> nexts = nexts(list);
        
        // BFS搜索
        HashMap<String, Integer> distanceMap = new HashMap<>();
        distanceMap.put(start, 1);
        HashSet<String> set = new HashSet<>();
        set.add(start);
        Queue<String> que = new LinkedList<>();
        que.add(start);
        
        while (!que.isEmpty()) {
            String cur = que.poll();
            Integer distance = distanceMap.get(cur);
            
            for (String next : nexts.get(cur)) {
                if (next.equals(to)) {
                    // 找到目标单词，返回距离
                    return distance + 1;
                }
                
                if (!set.contains(next)) {
                    set.add(next);
                    que.add(next);
                    distanceMap.put(next, distance + 1);
                }
            }
        }
        
        return 0;  // 无法转换
    }

    /**
     * 方法二：双向BFS优化版本
     * 
     * @param start 开始单词
     * @param end 结束单词
     * @param words 字典单词列表
     * @return 最短转换序列长度，无法转换返回0
     */
    public static int ladderLength2(String start, String end, List<String> words) {
        HashSet<String> dict = new HashSet<>(words);
        
        // 目标单词不在字典中，无法转换
        if (!dict.contains(end)) {
            return 0;
        }
        
        // 双向BFS的两个搜索集合
        HashSet<String> startSet = new HashSet<>();
        HashSet<String> endSet = new HashSet<>();
        HashSet<String> visit = new HashSet<>();  // 已访问节点
        
        startSet.add(start);
        endSet.add(end);
        
        for (int len = 2; !startSet.isEmpty(); len++) {
            HashSet<String> nextSet = new HashSet<>();
            
            // 从当前集合的每个单词开始扩展
            for (String w : startSet) {
                for (int j = 0; j < w.length(); j++) {
                    char[] ch = w.toCharArray();
                    
                    // 尝试替换每个位置的字母
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c != w.charAt(j)) {
                            ch[j] = c;
                            String next = String.valueOf(ch);
                            
                            // 检查是否与另一端相遇
                            if (endSet.contains(next)) {
                                return len;
                            }
                            
                            // 添加到下一层搜索集合
                            if (dict.contains(next) && !visit.contains(next)) {
                                nextSet.add(next);
                                visit.add(next);
                            }
                        }
                    }
                }
            }
            
            // 优化：总是从较小的集合开始搜索
            startSet = (nextSet.size() < endSet.size()) ? nextSet : endSet;
            endSet = (startSet == nextSet) ? endSet : nextSet;
        }
        
        return 0;  // 无法转换
    }
}
