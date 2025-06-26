package leetc.top;

import java.util.*;

/**
 * LeetCode 126. 单词接龙 II (Word Ladder II)
 * 
 * 问题描述：
 * 给定两个单词（beginWord 和 endWord）和一个字典，找出所有从 beginWord 到 endWord 的最短转换序列。
 * 转换需遵循如下规则：
 * 1. 每次转换只能改变一个字母
 * 2. 转换过程中的中间单词必须是字典中的单词
 * 
 * 解法思路：
 * 双向BFS + 路径重构：
 * 1. 预处理：构建每个单词的邻接表（相差一个字母的单词）
 * 2. 双向距离计算：
 *    - 从起点开始BFS，记录到各节点的距离
 *    - 从终点开始BFS，记录到各节点的距离
 * 3. 路径重构：
 *    - 使用DFS递归构造所有最短路径
 *    - 剪枝条件：fromDistance[next] = fromDistance[cur] + 1 且 toDistance[next] = toDistance[cur] - 1
 * 
 * 核心算法：
 * - getNext(): 生成与当前单词相差一个字母的所有有效单词
 * - nexts(): 为所有单词构建邻接表
 * - distances(): BFS计算距离表
 * - paths(): DFS重构所有最短路径
 * 
 * 优化策略：
 * - 双向BFS减少搜索空间
 * - 距离表剪枝避免无效路径
 * - 预处理邻接表避免重复计算
 * 
 * 时间复杂度：O(N*26*M + N*M) - N为单词数，M为单词长度
 * 空间复杂度：O(N*M) - 存储邻接表和距离信息
 * 
 * LeetCode链接：https://leetcode.com/problems/word-ladder-ii/
 */
public class P126_WordLadderII {
    private static List<String> getNext(String word, HashSet<String> dict) {
        ArrayList<String> res = new ArrayList<>();
        char[] chs = word.toCharArray();
        for (char cur = 'a'; cur <= 'z'; cur++) {
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] != cur) {
                    char tmp = chs[i];
                    chs[i] = cur;
                    if (dict.contains(String.valueOf(chs))) {
                        res.add(String.valueOf(chs));
                    }
                    chs[i] = tmp;
                }
            }
        }
        return res;
    }

    private static HashMap<String, List<String>> nexts(List<String> words) {
        HashSet<String> dict = new HashSet<>(words);
        HashMap<String, List<String>> nexts = new HashMap<>();
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), getNext(words.get(i), dict));
        }
        return nexts;
    }

    private static HashMap<String, Integer> distances(String start, HashMap<String, List<String>> nexts) {
        HashMap<String, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        Queue<String> que = new LinkedList<>();
        que.add(start);
        HashSet<String> set = new HashSet<>();
        set.add(start);
        while (!que.isEmpty()) {
            String cur = que.poll();
            for (String next : nexts.get(cur)) {
                if (!set.contains(next)) {
                    distances.put(next, distances.get(cur) + 1);
                    que.add(next);
                    set.add(next);
                }
            }
        }
        return distances;
    }

    private static void paths(String cur, String to, HashMap<String, List<String>> nexts,
                              HashMap<String, Integer> fromDistances, HashMap<String, Integer> toDistances,
                              LinkedList<String> path, List<List<String>> res) {
        path.add(cur);
        if (to.equals(cur)) {
            res.add(new LinkedList<>(path));
        } else {
            for (String next : nexts.get(cur)) {
                if (fromDistances.get(next) == fromDistances.get(cur) + 1
                    && toDistances.get(next) == toDistances.get(cur) - 1) {
                    paths(next, to, nexts, fromDistances, toDistances, path, res);
                }
            }
        }
        path.pollLast();
    }

    public static List<List<String>> findLadders(String start, String end, List<String> list) {
        list.add(start);
        HashMap<String, List<String>> nexts = nexts(list);
        HashMap<String, Integer> fromDistances = distances(start, nexts);
        List<List<String>> res = new ArrayList<>();
        if (!fromDistances.containsKey(end)) {
            return res;
        }
        HashMap<String, Integer> toDistances = distances(end, nexts);
        LinkedList<String> pathList = new LinkedList<>();
        paths(start, end, nexts, fromDistances, toDistances, pathList, res);
        return res;
    }
}
