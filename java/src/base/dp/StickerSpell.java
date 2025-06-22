package base.dp;

import java.util.HashMap;

/**
 * 贴纸拼单词问题
 * 
 * 问题描述：
 * 我们有 n 种不同的贴纸，每种贴纸上都有一个小写英文单词。
 * 你希望从这些贴纸中裁剪字母来拼写给定的目标字符串 target。
 * 如果你愿意的话，你可以多次使用每一种贴纸。
 * 返回你需要拼写出 target 的贴纸的最少数量。如果任务不可能，则返回 -1。
 * 
 * 解法分析：
 * 1. 暴力递归：尝试每个贴纸，计算使用后剩余的目标字符
 * 2. 优化1：将贴纸转换为字符频次数组，避免重复字符串操作
 * 3. 优化2：使用贪心策略，优先选择包含目标字符串第一个字符的贴纸
 * 4. 记忆化搜索：使用HashMap缓存中间结果，避免重复计算
 * 
 * 时间复杂度：指数级，但通过记忆化和剪枝可以大大优化
 * 空间复杂度：O(target长度 * 递归深度)
 */
// https://leetcode.com/problems/stickers-to-spell-word
public class StickerSpell {
    
    /**
     * 计算两个字符串的差集
     * 返回s1中去掉s2中字符后的剩余字符组成的字符串
     * 
     * @param s1 被减数字符串
     * @param s2 减数字符串  
     * @return s1减去s2后的剩余字符串
     */
    private static String minus(String s1, String s2) {
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 统计s1中每个字符的出现次数
        int[] count = new int[26];
        for (char cha : str1) {
            count[cha - 'a']++;
        }
        
        // 减去s2中的字符
        for (char cha : str2) {
            count[cha - 'a']--;
        }
        
        // 构建剩余字符组成的字符串
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                for (int j = 0; j < count[i]; j++) {
                    builder.append((char) (i + 'a'));
                }
            }
        }
        return builder.toString();
    }

    /**
     * 暴力递归解法1
     * 
     * @param stickers 贴纸数组
     * @param target 目标字符串
     * @return 需要的最少贴纸数量
     */
    private static int process1(String[] stickers, String target) {
        // base case：目标字符串为空，不需要贴纸
        if (target.length() == 0) {
            return 0;
        }
        
        int min = Integer.MAX_VALUE;
        
        // 尝试每一个贴纸
        for (String first : stickers) {
            String rest = minus(target, first);
            
            // 如果这个贴纸有用（能减少目标字符串的长度）
            if (rest.length() != target.length()) {
                min = Math.min(min, process1(stickers, rest));
            }
        }
        
        return min + (min == Integer.MAX_VALUE ? 0 : 1);
    }

    /**
     * 暴力递归解法1入口
     * 
     * @param stickers 贴纸数组
     * @param target 目标字符串
     * @return 需要的最少贴纸数量，无法完成返回-1
     */
    public static int minStickers1(String[] stickers, String target) {
        int ans = process1(stickers, target);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    /**
     * 优化后的递归解法2
     * 使用字符频次数组代替字符串操作，并添加贪心策略
     * 
     * @param stickers 贴纸的字符频次数组
     * @param t 目标字符串
     * @return 需要的最少贴纸数量
     */
    private static int process2(int[][] stickers, String t) {
        if (t.length() == 0) {
            return 0;
        }
        
        char[] target = t.toCharArray();
        int[] tcounts = new int[26];
        
        // 统计目标字符串中每个字符的频次
        for (char cha : target) {
            tcounts[cha - 'a']++;
        }
        
        int n = stickers.length;
        int min = Integer.MAX_VALUE;
        
        for (int i = 0; i < n; i++) {
            int[] sticker = stickers[i];
            
            // 贪心策略：只考虑包含目标字符串第一个字符的贴纸
            if (sticker[target[0] - 'a'] > 0) {
                StringBuilder builder = new StringBuilder();
                
                // 计算使用当前贴纸后的剩余字符
                for (int j = 0; j < 26; j++) {
                    if (tcounts[j] > 0) {
                        int nums = tcounts[j] - sticker[j];
                        for (int k = 0; k < nums; k++) {
                            builder.append((char) (j + 'a'));
                        }
                    }
                }
                
                String rest = builder.toString();
                min = Math.min(min, process2(stickers, rest));
            }
        }
        
        return min + (min == Integer.MAX_VALUE ? 0 : 1);
    }

    /**
     * 优化解法2入口
     * 将字符串贴纸转换为字符频次数组
     * 
     * @param stickers 贴纸数组
     * @param target 目标字符串
     * @return 需要的最少贴纸数量，无法完成返回-1
     */
    public static int minStickers2(String[] stickers, String target) {
        int n = stickers.length;
        // 将每个贴纸转换为字符频次数组
        int[][] counts = new int[n][26];
        for (int i = 0; i < n; i++) {
            char[] str = stickers[i].toCharArray();
            for (char cha : str) {
                counts[i][cha - 'a']++;
            }
        }
        
        int ans = process2(counts, target);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    /**
     * 记忆化搜索解法
     * 
     * @param stickers 贴纸的字符频次数组
     * @param t 目标字符串
     * @param dp 记忆化缓存
     * @return 需要的最少贴纸数量
     */
    private static int process3(int[][] stickers, String t, HashMap<String, Integer> dp) {
        // 如果已经计算过，直接返回缓存结果
        if (dp.containsKey(t)) {
            return dp.get(t);
        }
        
        char[] target = t.toCharArray();
        int[] tcounts = new int[26];
        for (char cha : target) {
            tcounts[cha - 'a']++;
        }
        
        int n = stickers.length;
        int min = Integer.MAX_VALUE;
        
        for (int i = 0; i < n; i++) {
            int[] sticker = stickers[i];
            
            // 贪心策略：只考虑包含目标字符串第一个字符的贴纸
            if (sticker[target[0] - 'a'] > 0) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < 26; j++) {
                    if (tcounts[j] > 0) {
                        int nums = tcounts[j] - sticker[j];
                        for (int k = 0; k < nums; k++) {
                            builder.append((char) (j + 'a'));
                        }
                    }
                }
                
                String rest = builder.toString();
                min = Math.min(min, process3(stickers, rest, dp));
            }
        }
        
        int ans = min + (min == Integer.MAX_VALUE ? 0 : 1);
        // 缓存结果
        dp.put(t, ans);
        return ans;
    }

    /**
     * 记忆化搜索解法入口
     * 
     * @param stickers 贴纸数组
     * @param target 目标字符串
     * @return 需要的最少贴纸数量，无法完成返回-1
     */
    public static int minStickers3(String[] stickers, String target) {
        int n = stickers.length;
        int[][] counts = new int[n][26];
        for (int i = 0; i < n; i++) {
            char[] str = stickers[i].toCharArray();
            for (char cha : str) {
                counts[i][cha - 'a']++;
            }
        }
        
        HashMap<String, Integer> dp = new HashMap<>();
        dp.put("", 0);  // base case：空字符串需要0个贴纸
        
        int ans = process3(counts, target, dp);
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }
}
