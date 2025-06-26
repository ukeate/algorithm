package leetc.top;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * LeetCode 49. 字母异位词分组 (Group Anagrams)
 * 
 * 问题描述：
 * 给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
 * 
 * 字母异位词：字母相同但排列不同的字符串。
 * 
 * 示例：
 * - 输入：strs = ["eat","tea","tan","ate","nat","bat"]
 * - 输出：[["bat"],["nat","tan"],["ate","eat","tea"]]
 * 
 * 解法思路：
 * 哈希表 + 字符串排序：
 * 1. 字母异位词的关键特征：排序后的字符串相同
 * 2. 对每个字符串进行字符排序，得到标准化的key
 * 3. 使用HashMap以排序后的字符串为key，异位词列表为value
 * 4. 遍历所有字符串，根据排序后的key进行分组
 * 5. 最后收集所有分组的结果
 * 
 * 核心思想：
 * - 排序作为分组依据：相同的字母异位词排序后必然相同
 * - 哈希表快速分组：O(1)时间复杂度的查找和插入
 * - 一次遍历完成分组：效率高
 * 
 * 时间复杂度：O(n × m × log m) - n是字符串数量，m是字符串平均长度
 * 空间复杂度：O(n × m) - 存储所有字符串和结果
 */
public class P49_GroupAnagrams {
    
    /**
     * 将字母异位词进行分组
     * 
     * 算法步骤：
     * 1. 初始化HashMap，key为排序后的字符串，value为异位词列表
     * 2. 遍历输入的每个字符串：
     *    a) 将字符串转为字符数组并排序
     *    b) 排序后的字符数组转回字符串作为key
     *    c) 如果key不存在，创建新的异位词列表
     *    d) 将原字符串添加到对应的异位词列表中
     * 3. 收集HashMap中所有的异位词列表并返回
     * 
     * @param strs 输入的字符串数组
     * @return 分组后的字母异位词列表
     */
    public static List<List<String>> groupAnagrams(String[] strs) {
        // key: 排序后的字符串，value: 异位词列表
        HashMap<String, List<String>> map = new HashMap<>();
        
        // 遍历所有字符串进行分组
        for (String str : strs) {
            // 将字符串转为字符数组并排序
            char[] chs = str.toCharArray();
            Arrays.sort(chs);
            
            // 排序后的字符数组作为分组的key
            String key = String.valueOf(chs);
            
            // 如果key不存在，创建新的异位词列表
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            
            // 将原字符串添加到对应的异位词列表中
            map.get(key).add(str);
        }
        
        // 收集所有异位词分组的结果
        List<List<String>> res = new ArrayList<>();
        for (List<String> list : map.values()) {
            res.add(list);
        }
        
        return res;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        String[] strs1 = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println("输入: " + Arrays.toString(strs1));
        List<List<String>> result1 = groupAnagrams(strs1);
        System.out.println("输出: " + result1);
        System.out.println();
        
        // 测试用例2
        String[] strs2 = {""};
        System.out.println("输入: " + Arrays.toString(strs2));
        List<List<String>> result2 = groupAnagrams(strs2);
        System.out.println("输出: " + result2);
        System.out.println();
        
        // 测试用例3
        String[] strs3 = {"a"};
        System.out.println("输入: " + Arrays.toString(strs3));
        List<List<String>> result3 = groupAnagrams(strs3);
        System.out.println("输出: " + result3);
        System.out.println();
        
        // 测试用例4：验证异位词分组
        String[] strs4 = {"abc", "bca", "cab", "xyz", "zyx"};
        System.out.println("输入: " + Arrays.toString(strs4));
        List<List<String>> result4 = groupAnagrams(strs4);
        System.out.println("输出: " + result4);
        System.out.println("分析: abc,bca,cab为一组；xyz,zyx为一组");
    }
}
