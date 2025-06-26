package basic.c48;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自由之环问题（圆盘旋转最小步数）
 * 
 * 问题链接：https://leetcode.com/problems/freedom-trail/
 * 
 * 问题描述：
 * 电子游戏中有一个圆盘，圆盘上有一些字符环形排列
 * 目标是通过旋转圆盘来拼出一个目标字符串
 * 初始时圆盘的12点方向对准位置0
 * 
 * 操作规则：
 * 1. 可以顺时针或逆时针旋转圆盘
 * 2. 旋转圆盘移动一个位置需要1步
 * 3. 选择对准的字符需要1步（按按钮）
 * 
 * 目标：找到拼出目标字符串的最小步数
 * 
 * 例如：
 * ring = "godding", key = "gd"
 * - 初始位置在g(0)，选择g需要1步
 * - 从g(0)旋转到d(2)需要2步，选择d需要1步
 * - 总共需要4步
 * 
 * 算法思路：
 * 使用动态规划 + 记忆化搜索
 * 状态定义：dp[ringPos][keyIdx] 表示当前圆盘对准位置ringPos，
 * 要完成key[keyIdx...]的最小步数
 * 
 * 状态转移：
 * 对于key[keyIdx]这个字符，在ring中找到所有出现位置
 * 计算从当前位置到每个目标位置的旋转步数，选择最小的
 * 
 * 优化技巧：
 * 1. 使用HashMap预处理每个字符在ring中的所有位置
 * 2. 圆盘旋转距离：min(顺时针距离, 逆时针距离)
 * 3. 记忆化搜索避免重复计算
 * 
 * 时间复杂度：O(m*n*k) 其中m是ring长度，n是key长度，k是平均每个字符出现次数
 * 空间复杂度：O(m*n)
 * 
 * @author 算法学习
 * @see <a href="https://leetcode.com/problems/freedom-trail/">LeetCode 514</a>
 */
public class FreedomTrail {
    
    /**
     * 计算圆盘上两个位置之间的最小旋转距离
     * 
     * @param i1 起始位置
     * @param i2 目标位置
     * @param size 圆盘大小
     * @return 最小旋转步数
     * 
     * 算法思路：
     * 圆盘上两点间有两种走法：顺时针和逆时针
     * 顺时针距离：|i2 - i1|
     * 逆时针距离：size - |i2 - i1|
     * 取两者的最小值
     */
    private static int dial(int i1, int i2, int size) {
        return Math.min(Math.abs(i1 - i2), Math.min(i1, i2) + size - Math.max(i1, i2));
    }

    /**
     * 递归求解最小步数（带记忆化）
     * 
     * @param preStrIdx 当前圆盘对准的位置
     * @param keyIdx 当前要匹配的key字符索引
     * @param key 目标字符串数组
     * @param map 字符到位置列表的映射
     * @param size 圆盘大小
     * @param dp 记忆化数组
     * @return 完成key[keyIdx...]的最小步数
     * 
     * 算法思路：
     * 1. 如果已经计算过，直接返回缓存结果
     * 2. 如果key已经全部匹配完，返回0
     * 3. 否则枚举key[keyIdx]在ring中的所有出现位置
     * 4. 计算旋转到每个位置的代价，选择最小的
     */
    private static int minSteps(int preStrIdx, int keyIdx, char[] key, 
                               HashMap<Character, ArrayList<Integer>> map, 
                               int size, int[][] dp) {
        // 查看是否已经计算过
        if (dp[preStrIdx][keyIdx] != -1) {
            return dp[preStrIdx][keyIdx];
        }
        
        // 递归终止条件：key已经全部匹配完
        if (keyIdx == key.length) {
            dp[preStrIdx][keyIdx] = 0;
            return 0;
        }
        
        int ans = Integer.MAX_VALUE;
        
        // 枚举当前字符在ring中的所有出现位置
        for (int curStrIdx : map.get(key[keyIdx])) {
            // 计算总代价：旋转代价 + 按按钮代价(1) + 后续匹配代价
            int step = dial(preStrIdx, curStrIdx, size) + 1
                    + minSteps(curStrIdx, keyIdx + 1, key, map, size, dp);
            ans = Math.min(ans, step);
        }
        
        // 记忆化存储结果
        dp[preStrIdx][keyIdx] = ans;
        return ans;
    }

    /**
     * 求解自由之环问题的最小步数
     * 
     * @param r 圆盘字符串
     * @param k 目标字符串
     * @return 最小步数
     * 
     * 算法步骤：
     * 1. 预处理：为每个字符建立位置索引
     * 2. 初始化记忆化数组
     * 3. 从位置0开始递归求解
     */
    public static int min(String r, String k) {
        char[] ring = r.toCharArray();
        int size = ring.length;
        
        // 预处理：建立字符到位置列表的映射
        HashMap<Character, ArrayList<Integer>> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            if (!map.containsKey(ring[i])) {
                map.put(ring[i], new ArrayList<>());
            }
            map.get(ring[i]).add(i);
        }
        
        // 初始化记忆化数组，-1表示未计算
        int[][] dp = new int[size][k.length() + 1];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= k.length(); j++) {
                dp[i][j] = -1;
            }
        }
        
        // 从位置0开始，匹配整个key字符串
        return minSteps(0, 0, k.toCharArray(), map, size, dp);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 自由之环问题测试 ===");
        
        // 测试用例1
        String ring1 = "godding";
        String key1 = "gd";
        System.out.println("测试用例1:");
        System.out.println("Ring: \"" + ring1 + "\"");
        System.out.println("Key: \"" + key1 + "\"");
        System.out.println("最小步数: " + min(ring1, key1));
        
        // 测试用例2
        String ring2 = "godding";
        String key2 = "godding";
        System.out.println("\n测试用例2:");
        System.out.println("Ring: \"" + ring2 + "\"");
        System.out.println("Key: \"" + key2 + "\"");
        System.out.println("最小步数: " + min(ring2, key2));
        
        // 测试用例3：单个字符
        String ring3 = "abcde";
        String key3 = "a";
        System.out.println("\n测试用例3:");
        System.out.println("Ring: \"" + ring3 + "\"");
        System.out.println("Key: \"" + key3 + "\"");
        System.out.println("最小步数: " + min(ring3, key3));
        
        // 测试用例4：需要绕圆盘
        String ring4 = "abcde";
        String key4 = "ea";
        System.out.println("\n测试用例4:");
        System.out.println("Ring: \"" + ring4 + "\"");
        System.out.println("Key: \"" + key4 + "\"");
        System.out.println("最小步数: " + min(ring4, key4));
        
        // 详细分析测试用例4
        System.out.println("\n=== 测试用例4详细分析 ===");
        System.out.println("Ring: \"abcde\" (位置: a=0, b=1, c=2, d=3, e=4)");
        System.out.println("Key: \"ea\"");
        System.out.println("步骤分析:");
        System.out.println("1. 初始位置在0(a)");
        System.out.println("2. 要找'e'(位置4): 顺时针4步 or 逆时针1步 -> 选择逆时针1步");
        System.out.println("3. 按按钮选择'e': 1步");
        System.out.println("4. 要找'a'(位置0): 顺时针1步 or 逆时针4步 -> 选择顺时针1步");
        System.out.println("5. 按按钮选择'a': 1步");
        System.out.println("总步数: 1 + 1 + 1 + 1 = 4步");
        
        // 算法特点总结
        System.out.println("\n=== 算法特点总结 ===");
        System.out.println("问题特点：");
        System.out.println("1. 圆盘旋转问题，需要考虑最短路径");
        System.out.println("2. 多选择动态规划，每个字符可能在多个位置");
        System.out.println("3. 状态空间：当前位置 × 剩余字符串");
        
        System.out.println("\n优化技巧：");
        System.out.println("1. 预处理字符位置映射，减少查找时间");
        System.out.println("2. 记忆化搜索避免重复计算");
        System.out.println("3. 圆盘距离计算：min(顺时针, 逆时针)");
        
        System.out.println("\n复杂度分析：");
        System.out.println("时间复杂度: O(m*n*k) - m是ring长度，n是key长度，k是平均重复字符数");
        System.out.println("空间复杂度: O(m*n) - 记忆化数组大小");
        System.out.println("适用场景: 圆盘/环形结构的路径优化问题");
    }
}
