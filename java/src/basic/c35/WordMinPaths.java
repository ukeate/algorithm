package basic.c35;

import java.util.*;

/**
 * 字符串变换最小步数问题（类似Word Ladder）
 * 
 * 问题描述：
 * 给定一个起始字符串start、一个目标字符串end和一个字符串列表list，
 * 找到从start变换到end的所有最短路径。
 * 
 * 变换规则：
 * - 每次只能改变一个字符
 * - 变换后的字符串必须在给定的list中存在
 * - 所有字符串长度相同
 * 
 * 例如：start="abc", end="cab", list=["abc","cab","acc","cbc","ccc","cac","cbb","aab","abb"]
 * 可能的最短路径：abc -> acc -> cac -> cab
 * 
 * 算法思路：
 * 1. 使用BFS找到最短路径长度
 * 2. 记录每个节点到起点的距离
 * 3. 使用DFS回溯所有最短路径
 * 
 * 核心数据结构：
 * - nexts: 图的邻接表表示
 * - distances: 每个节点到起点的最短距离
 * 
 * 时间复杂度：O(N*M + P)，N为字符串数量，M为字符串长度，P为路径数量
 * 空间复杂度：O(N*M)
 */
public class WordMinPaths {
    
    /**
     * 获取与给定单词相邻的所有单词（只改变一个字符且在字典中存在）
     * 
     * 算法思路：
     * 1. 遍历单词的每个位置
     * 2. 尝试将该位置替换为a-z的每个字符
     * 3. 检查替换后的字符串是否在字典中
     * 
     * @param word 当前单词
     * @param dict 字典集合
     * @return 所有相邻的单词列表
     */
    private static ArrayList<String> getNext(String word, Set<String> dict) {
        ArrayList<String> res = new ArrayList<>();
        char[] chs = word.toCharArray();
        
        // 尝试每个可能的字符替换
        for (char cur = 'a'; cur <= 'z'; cur++) {
            for (int i = 0; i < chs.length; i++) {
                if (chs[i] != cur) {  // 只有当字符不同时才尝试替换
                    char tmp = chs[i];          // 保存原字符
                    chs[i] = cur;               // 替换为新字符
                    
                    String newWord = String.valueOf(chs);
                    if (dict.contains(newWord)) {
                        res.add(newWord);       // 如果在字典中，加入结果
                    }
                    
                    chs[i] = tmp;               // 恢复原字符
                }
            }
        }
        return res;
    }

    /**
     * 构建字符串变换图的邻接表
     * 
     * 算法思路：
     * 1. 将所有字符串加入字典集合（便于快速查找）
     * 2. 为每个字符串找到所有可能的下一步字符串
     * 3. 构建邻接表表示的有向图
     * 
     * @param words 字符串列表
     * @return 邻接表，key为字符串，value为相邻字符串列表
     */
    private static HashMap<String, ArrayList<String>> getNexts(List<String> words) {
        Set<String> dict = new HashSet<>(words);  // 转为集合，提高查找效率
        HashMap<String, ArrayList<String>> nexts = new HashMap<>();
        
        // 为每个字符串构建邻接表
        for (int i = 0; i < words.size(); i++) {
            nexts.put(words.get(i), getNext(words.get(i), dict));
        }
        return nexts;
    }

    /**
     * 使用BFS计算从起始字符串到所有其他字符串的最短距离
     * 
     * 算法思路：
     * 1. 从起始字符串开始BFS遍历
     * 2. 记录每个字符串到起始字符串的最短距离
     * 3. 使用visited集合避免重复访问
     * 
     * @param start 起始字符串
     * @param nexts 图的邻接表
     * @return 距离映射表，key为字符串，value为到起始点的距离
     */
    private static HashMap<String, Integer> getDistances(String start, HashMap<String, ArrayList<String>> nexts) {
        HashMap<String, Integer> distances = new HashMap<>();
        distances.put(start, 0);  // 起始点距离为0
        
        Queue<String> que = new LinkedList<>();
        que.add(start);
        
        HashSet<String> set = new HashSet<>();
        set.add(start);  // 标记起始点已访问
        
        // BFS遍历
        while (!que.isEmpty()) {
            String cur = que.poll();
            
            // 遍历当前字符串的所有邻接字符串
            for (String next : nexts.get(cur)) {
                if (!set.contains(next)) {
                    // 未访问过的字符串，更新距离并加入队列
                    distances.put(next, distances.get(cur) + 1);
                    que.add(next);
                    set.add(next);
                }
            }
        }
        return distances;
    }

    /**
     * 使用DFS回溯所有从起始点到目标点的最短路径
     * 
     * 算法思路：
     * 1. 从当前位置开始，尝试所有可能的下一步
     * 2. 只有当下一步的距离等于当前距离+1时，才是最短路径上的节点
     * 3. 递归构建所有可能的最短路径
     * 4. 回溯时需要恢复路径状态
     * 
     * @param cur 当前字符串
     * @param to 目标字符串
     * @param nexts 图的邻接表
     * @param distances 距离映射表
     * @param path 当前路径（用于回溯）
     * @param res 存储所有最短路径的结果列表
     */
    private static void minPath(String cur, String to, 
                              HashMap<String, ArrayList<String>> nexts, 
                              HashMap<String, Integer> distances, 
                              LinkedList<String> path, 
                              List<List<String>> res) {
        path.add(cur);  // 将当前字符串加入路径
        
        if (to.equals(cur)) {
            // 到达目标字符串，找到一条最短路径
            res.add(new LinkedList<>(path));  // 复制当前路径加入结果
        } else {
            // 尝试所有可能的下一步
            for (String next : nexts.get(cur)) {
                // 只有距离正好增加1的字符串才在最短路径上
                if (distances.get(next) == distances.get(cur) + 1) {
                    minPath(next, to, nexts, distances, path, res);
                }
            }
        }
        
        path.pollLast();  // 回溯：移除当前字符串，恢复路径状态
    }

    /**
     * 主方法：找到从start到end的所有最短变换路径
     * 
     * 算法流程：
     * 1. 将起始字符串加入字符串列表（确保在图中）
     * 2. 构建字符串变换图
     * 3. 使用BFS计算最短距离
     * 4. 使用DFS回溯所有最短路径
     * 
     * @param start 起始字符串
     * @param end 目标字符串
     * @param list 可用的字符串列表
     * @return 所有最短路径的列表
     */
    public static List<List<String>> min(String start, String end, List<String> list) {
        list.add(start);  // 确保起始字符串在列表中
        
        // 步骤1：构建图的邻接表
        HashMap<String, ArrayList<String>> nexts = getNexts(list);
        
        // 步骤2：计算最短距离
        HashMap<String, Integer> distances = getDistances(start, nexts);
        
        // 步骤3：回溯所有最短路径
        LinkedList<String> pathList = new LinkedList<>();
        List<List<String>> res = new ArrayList<>();
        minPath(start, end, nexts, distances, pathList, res);
        
        return res;
    }

    /**
     * 测试方法：验证算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串变换最小步数测试 ===");
        
        String start = "abc";
        String end = "cab";
        String[] listArr = {"abc", "cab", "acc", "cbc", "ccc", "cac", "cbb", "aab", "abb"};
        
        List<String> list = new ArrayList<>();
        for (int i = 0; i < listArr.length; i++) {
            list.add(listArr[i]);
        }
        
        System.out.println("起始字符串: " + start);
        System.out.println("目标字符串: " + end);
        System.out.print("可用字符串: ");
        for (String s : listArr) {
            System.out.print(s + " ");
        }
        System.out.println();
        
        List<List<String>> res = min(start, end, list);
        
        System.out.println("\n所有最短变换路径：");
        for (int i = 0; i < res.size(); i++) {
            System.out.print("路径" + (i + 1) + ": ");
            List<String> path = res.get(i);
            for (int j = 0; j < path.size(); j++) {
                System.out.print(path.get(j));
                if (j < path.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
        }
        
        if (res.isEmpty()) {
            System.out.println("无法从起始字符串变换到目标字符串");
        } else {
            System.out.println("最短步数: " + (res.get(0).size() - 1));
        }
    }
}
