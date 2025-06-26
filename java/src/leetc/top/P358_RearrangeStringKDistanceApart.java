package leetc.top;

import java.util.*;

/**
 * LeetCode 358. K距离间隔重排字符串 (Rearrange String k Distance Apart)
 * 
 * 问题描述：
 * 给你一个字符串 s 和一个整数 k ，请你重新排列字符串，使得重排后的字符串中相同字符的位置间隔距离至少为 k 。
 * 所有输入的字符串都是小写字母。如果无法重新排列，返回一个空字符串 ""。
 * 
 * 示例：
 * 输入：s = "aabbcc", k = 3
 * 输出："abcabc"
 * 解释：相同的字母在新的字符串中间隔距离至少为 3 。
 * 
 * 输入：s = "aaabc", k = 3
 * 输出：""
 * 解释：无法重新排列得到距离至少为 3 的字符串。
 * 
 * 输入：s = "aaadbbcc", k = 2
 * 输出："abacabcd"
 * 
 * 提示：
 * - 1 <= s.length <= 3 * 10^5
 * - s 仅由小写英文字母组成
 * - 0 <= k <= s.length
 * 
 * 解法思路：
 * 贪心算法 + 优先队列：
 * 
 * 1. 核心思想：
 *    - 优先选择频次最高的字符，这样可以最大化利用空间
 *    - 使用冷却机制，保证相同字符间隔至少k个位置
 *    - 当无法找到合适字符时，说明无法满足条件
 * 
 * 2. 算法步骤：
 *    - 统计字符频次
 *    - 使用最大堆维护字符（按频次排序）
 *    - 使用队列维护冷却中的字符
 *    - 贪心选择当前可用频次最高的字符
 * 
 * 3. 关键观察：
 *    - 如果某个字符频次过高，可能导致无法满足k距离要求
 *    - 需要在合适的时机重新激活冷却的字符
 *    - 当所有字符都在冷却且无法选择时，无解
 * 
 * 核心思想：
 * - 贪心策略：优先选择频次最高的可用字符
 * - 冷却机制：保证相同字符的间隔约束
 * - 可行性检查：及时判断是否无解
 * 
 * 关键技巧：
 * - 优先队列：维护字符按频次的优先级
 * - 队列冷却：模拟字符的冷却过程
 * - 贪心选择：每次选择最优字符
 * 
 * 时间复杂度：O(n log(charset)) - n为字符串长度，charset为字符集大小
 * 空间复杂度：O(charset) - 存储字符频次和队列
 * 
 * LeetCode链接：https://leetcode.com/problems/rearrange-string-k-distance-apart/
 */
public class P358_RearrangeStringKDistanceApart {
    
    /**
     * 方法一：贪心算法 + 优先队列（推荐）
     * 
     * 使用最大堆和冷却队列实现
     * 
     * @param s 输入字符串
     * @param k 最小间隔距离
     * @return 重排后的字符串，无法满足条件返回空串
     */
    public String rearrangeString(String s, int k) {
        if (k <= 1) return s; // 无需间隔或间隔为1，直接返回
        
        // 统计字符频次
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }
        
        // 最大堆：按频次从大到小排序
        PriorityQueue<Character> maxHeap = new PriorityQueue<>((a, b) -> count.get(b) - count.get(a));
        maxHeap.addAll(count.keySet());
        
        StringBuilder result = new StringBuilder();
        Queue<Character> cooldown = new LinkedList<>(); // 冷却队列
        
        while (!maxHeap.isEmpty()) {
            // 尝试选择当前频次最高的字符
            Character current = maxHeap.poll();
            result.append(current);
            
            // 减少该字符的频次
            count.put(current, count.get(current) - 1);
            cooldown.offer(current);
            
            // 如果冷却队列达到k个元素，重新激活第一个字符
            if (cooldown.size() >= k) {
                Character reactivated = cooldown.poll();
                if (count.get(reactivated) > 0) {
                    maxHeap.offer(reactivated);
                }
            }
        }
        
        // 检查是否成功构建了完整的字符串
        return result.length() == s.length() ? result.toString() : "";
    }
    
    /**
     * 方法二：分块处理
     * 
     * 按照k为单位进行分块处理
     * 
     * @param s 输入字符串
     * @param k 最小间隔距离
     * @return 重排后的字符串
     */
    public String rearrangeStringByBlocks(String s, int k) {
        if (k <= 1) return s;
        
        int n = s.length();
        
        // 统计字符频次
        Map<Character, Integer> count = new HashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }
        
        // 按频次排序的字符列表
        List<Character> chars = new ArrayList<>(count.keySet());
        chars.sort((a, b) -> count.get(b) - count.get(a));
        
        // 计算需要的块数
        int maxCount = count.get(chars.get(0));
        int blocks = maxCount;
        
        // 检查可行性：最高频字符的频次是否合理
        // 如果最高频字符需要maxCount个位置，那么至少需要(maxCount-1)*k + 1个位置
        if ((maxCount - 1) * k >= n) {
            return "";
        }
        
        char[] result = new char[n];
        int index = 0;
        
        // 为每个字符分配位置
        for (char c : chars) {
            int freq = count.get(c);
            
            for (int i = 0; i < freq; i++) {
                result[index] = c;
                index += k;
                
                // 如果超出范围，重新开始
                if (index >= n) {
                    index = index % k + 1;
                }
            }
        }
        
        return new String(result);
    }
    
    /**
     * 方法三：递归回溯解法
     * 
     * 使用回溯法尝试所有可能的排列
     * 
     * @param s 输入字符串
     * @param k 最小间隔距离
     * @return 重排后的字符串
     */
    public String rearrangeStringBacktrack(String s, int k) {
        if (k <= 1) return s;
        
        // 统计字符频次
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        char[] result = new char[s.length()];
        if (backtrack(result, count, 0, k)) {
            return new String(result);
        }
        return "";
    }
    
    /**
     * 回溯辅助函数
     * 
     * @param result 结果数组
     * @param count 字符计数数组
     * @param pos 当前位置
     * @param k 间隔距离
     * @return 是否成功构建
     */
    private boolean backtrack(char[] result, int[] count, int pos, int k) {
        if (pos == result.length) return true;
        
        // 尝试每个字符
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0 && canPlace(result, pos, (char)('a' + i), k)) {
                // 放置字符
                result[pos] = (char)('a' + i);
                count[i]--;
                
                // 继续递归
                if (backtrack(result, count, pos + 1, k)) {
                    return true;
                }
                
                // 回溯
                count[i]++;
            }
        }
        
        return false;
    }
    
    /**
     * 检查是否可以在指定位置放置字符
     * 
     * @param result 结果数组
     * @param pos 位置
     * @param c 字符
     * @param k 间隔距离
     * @return 是否可以放置
     */
    private boolean canPlace(char[] result, int pos, char c, int k) {
        // 检查前k个位置是否有相同字符
        for (int i = Math.max(0, pos - k + 1); i < pos; i++) {
            if (result[i] == c) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 方法四：数学验证 + 贪心构造
     * 
     * 先验证可行性，再构造解
     * 
     * @param s 输入字符串
     * @param k 最小间隔距离
     * @return 重排后的字符串
     */
    public String rearrangeStringMath(String s, int k) {
        if (k <= 1) return s;
        
        int n = s.length();
        
        // 统计字符频次
        int[] count = new int[26];
        int maxCount = 0;
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
            maxCount = Math.max(maxCount, count[c - 'a']);
        }
        
        // 数学验证：检查最高频字符是否可行
        // 如果最高频字符出现maxCount次，需要至少(maxCount-1)*k + 1个位置
        if ((maxCount - 1) * k + 1 > n) {
            return "";
        }
        
        // 计算有多少个字符达到最高频次
        int maxCountChars = 0;
        for (int i = 0; i < 26; i++) {
            if (count[i] == maxCount) {
                maxCountChars++;
            }
        }
        
        // 更精确的验证
        if ((maxCount - 1) * k + maxCountChars > n) {
            return "";
        }
        
        // 贪心构造
        char[] result = new char[n];
        int index = 0;
        
        // 按频次从高到低放置字符
        for (int freq = maxCount; freq > 0; freq--) {
            for (int i = 0; i < 26; i++) {
                if (count[i] == freq) {
                    // 放置当前字符的所有实例
                    for (int j = 0; j < freq; j++) {
                        result[index] = (char)('a' + i);
                        index += k;
                        
                        // 如果到达末尾，从下一个可用位置开始
                        if (index >= n) {
                            index = index % k + 1;
                        }
                    }
                    count[i] = 0; // 标记为已处理
                }
            }
        }
        
        return new String(result);
    }
    
    /**
     * 方法五：优化的贪心算法
     * 
     * 对方法一的优化版本
     * 
     * @param s 输入字符串
     * @param k 最小间隔距离
     * @return 重排后的字符串
     */
    public String rearrangeStringOptimized(String s, int k) {
        if (k <= 1) return s;
        
        // 统计字符频次
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // 使用数组代替优先队列，提高效率
        StringBuilder result = new StringBuilder();
        int[] cooldown = new int[26]; // cooldown[i]表示字符i可以再次使用的位置
        
        for (int pos = 0; pos < s.length(); pos++) {
            int selected = -1;
            int maxCount = 0;
            
            // 找到当前可用且频次最高的字符
            for (int i = 0; i < 26; i++) {
                if (count[i] > 0 && cooldown[i] <= pos && count[i] > maxCount) {
                    maxCount = count[i];
                    selected = i;
                }
            }
            
            // 如果没有找到可用字符，说明无解
            if (selected == -1) {
                return "";
            }
            
            // 使用选定的字符
            result.append((char)('a' + selected));
            count[selected]--;
            cooldown[selected] = pos + k; // 设置冷却时间
        }
        
        return result.toString();
    }
    
    /**
     * 辅助方法：验证结果是否正确
     * 
     * @param s 重排后的字符串
     * @param k 最小间隔距离
     * @return 是否满足k距离要求
     */
    public boolean isValid(String s, int k) {
        Map<Character, Integer> lastPos = new HashMap<>();
        
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (lastPos.containsKey(c)) {
                if (i - lastPos.get(c) < k) {
                    return false;
                }
            }
            lastPos.put(c, i);
        }
        return true;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P358_RearrangeStringKDistanceApart solution = new P358_RearrangeStringKDistanceApart();
        
        // 测试用例
        String[] testStrings = {"aabbcc", "aaabc", "aaadbbcc", "ababab"};
        int[] testK = {3, 3, 2, 2};
        
        for (int i = 0; i < testStrings.length; i++) {
            String s = testStrings[i];
            int k = testK[i];
            
            System.out.println("输入: s = \"" + s + "\", k = " + k);
            
            String result1 = solution.rearrangeString(s, k);
            String result2 = solution.rearrangeStringByBlocks(s, k);
            String result3 = solution.rearrangeStringOptimized(s, k);
            
            System.out.println("方法1结果: \"" + result1 + "\" 有效: " + solution.isValid(result1, k));
            System.out.println("方法2结果: \"" + result2 + "\" 有效: " + solution.isValid(result2, k));
            System.out.println("方法3结果: \"" + result3 + "\" 有效: " + solution.isValid(result3, k));
            System.out.println();
        }
    }
}
