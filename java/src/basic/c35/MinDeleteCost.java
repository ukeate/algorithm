package basic.c35;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 字符串删除最小代价问题
 * 
 * 问题描述：
 * 给定两个字符串s1和s2，需要从s2中删除一些字符，使得剩余的字符能够作为s1的子串出现。
 * 求最少需要删除多少个字符。
 * 
 * 例如：s1="abcdf", s2="ab12cd3"
 * 可以删除s2中的"123"，剩下"abcd"，它是s1的子串，删除字符数为3
 * 
 * 解决方案：
 * 1. 方法1：暴力枚举s2的所有子序列 - O(2^N)，适合s2较小的情况
 * 2. 方法2：枚举s1的所有子串，计算编辑距离 - O(M²*N)
 * 3. 方法3：优化的动态规划 - O(M*N)，最优解
 * 4. 方法4：贪心匹配算法 - O(M*N²)
 * 
 * 时间复杂度：从O(2^N)到O(M*N)不等，其中M=s1.length(), N=s2.length()
 * 空间复杂度：O(M*N)
 */
public class MinDeleteCost {
    
    /**
     * 字符串长度比较器：按长度降序排列
     * 用于优先检查较长的子序列
     */
    private static class LenComp implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o2.length() - o1.length(); // 长度降序
        }
    }

    /**
     * 递归生成s2的所有子序列
     * 
     * @param s2 原字符串的字符数组
     * @param idx 当前处理的字符位置
     * @param path 当前构建的子序列
     * @param subs 存储所有子序列的列表
     */
    private static void process(char[] s2, int idx, String path, List<String> subs) {
        if (idx == s2.length) {
            subs.add(path); // 到达末尾，添加当前子序列
            return;
        }
        // 不选择当前字符
        process(s2, idx + 1, path, subs);
        // 选择当前字符
        process(s2, idx + 1, path + s2[idx], subs);
    }

    /**
     * 方法1：暴力枚举s2的所有子序列，找到能作为s1子串的最长子序列
     * 
     * 算法思路：
     * 1. 生成s2的所有2^N个子序列
     * 2. 按长度降序排列，优先检查较长的子序列
     * 3. 找到第一个能作为s1子串的子序列，返回删除字符数
     * 
     * 时间复杂度：O(2^N * M)，适合N较小的情况
     * 空间复杂度：O(2^N)
     * 
     * @param s1 目标字符串
     * @param s2 源字符串
     * @return 最少删除字符数
     */
    public static int min1(String s1, String s2) {
        List<String> subs = new ArrayList<>();
        process(s2.toCharArray(), 0, "", subs);      // 生成所有子序列
        subs.sort(new LenComp());                    // 按长度降序排列
        
        // 找到第一个能作为s1子串的子序列
        for (String sub : subs) {
            if (s1.indexOf(sub) != -1) {             // 检查是否为s1的子串
                return s2.length() - sub.length();   // 返回删除字符数
            }
        }
        return s2.length(); // 如果没有匹配的子序列，删除所有字符
    }

    /**
     * 计算将s2变成s1子串的编辑距离（只允许删除操作）
     * 
     * 动态规划状态定义：
     * dp[i][j]表示s2[0...i-1]变成s1[0...j-1]的最小删除次数
     * 
     * 状态转移：
     * - 如果s2[i-1] == s1[j-1]：dp[i][j] = dp[i-1][j-1]（字符匹配，无需删除）
     * - 否则：dp[i][j] = dp[i-1][j] + 1（删除s2[i-1]）
     * 
     * @param s2 源字符串数组
     * @param s1 目标字符串数组
     * @return 最小删除次数，如果无法变成子串则返回MAX_VALUE
     */
    private static int distance(char[] s2, char[] s1) {
        int row = s2.length;
        int col = s1.length;
        int[][] dp = new int[row][col];
        
        // 初始化第一行和第一列
        dp[0][0] = s2[0] == s1[0] ? 0 : Integer.MAX_VALUE;
        
        // 第一行：s2的第一个字符能否匹配s1的前缀
        for (int j = 1; j < col; j++) {
            dp[0][j] = Integer.MAX_VALUE; // s2只有一个字符，无法匹配s1的长串
        }
        
        // 第一列：s2的前缀变成s1的第一个字符的代价
        for (int i = 1; i < row; i++) {
            dp[i][0] = (dp[i - 1][0] != Integer.MAX_VALUE || s2[i] == s1[0]) ? i : Integer.MAX_VALUE;
        }
        
        // 填充DP表
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < col; j++) {
                dp[i][j] = Integer.MAX_VALUE;
                
                // 删除s2[i]的情况
                if (dp[i - 1][j] != Integer.MAX_VALUE) {
                    dp[i][j] = dp[i - 1][j] + 1;
                }
                
                // s2[i]匹配s1[j]的情况
                if (s2[i] == s1[j] && dp[i - 1][j - 1] != Integer.MAX_VALUE) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
                }
            }
        }
        
        return dp[row - 1][col - 1];
    }

    /**
     * 方法2：枚举s1的所有子串，计算每个子串与s2的编辑距离
     * 
     * 算法思路：
     * 1. 枚举s1的所有子串
     * 2. 对每个子串，计算s2变成该子串的最小删除次数
     * 3. 返回所有可能删除次数的最小值
     * 
     * 时间复杂度：O(M² * N²)，M为s1长度，N为s2长度
     * 空间复杂度：O(N)
     * 
     * @param s1 目标字符串
     * @param s2 源字符串
     * @return 最少删除字符数
     */
    public static int min2(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) {
            return s2.length();
        }
        
        int ans = Integer.MAX_VALUE;
        char[] str2 = s2.toCharArray();
        
        // 枚举s1的所有子串
        for (int i = 0; i < s1.length(); i++) {
            for (int j = i + 1; j <= s1.length(); j++) {
                ans = Math.min(ans, distance(str2, s1.substring(i, j).toCharArray()));
            }
        }
        
        return ans == Integer.MAX_VALUE ? s2.length() : ans;
    }

    /**
     * 方法3：优化的动态规划解法
     * 
     * 算法思路：
     * 将问题转化为：s2的子序列与s1的子串的最长公共子序列问题
     * 通过巧妙的DP状态设计，一次遍历解决所有子串情况
     * 
     * 时间复杂度：O(M * N)
     * 空间复杂度：O(M * N)
     * 
     * @param s1 目标字符串
     * @param s2 源字符串
     * @return 最少删除字符数
     */
    public static int min3(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) {
            return s2.length();
        }
        
        char[] str2 = s2.toCharArray();
        char[] str1 = s1.toCharArray();
        int m = str2.length;
        int n = str1.length;
        int[][] dp = new int[m][n];
        int ans = m;
        
        // 对每个起始位置进行DP
        for (int l = 0; l < n; l++) {
            // 初始化第一行
            dp[0][l] = str2[0] == str1[l] ? 0 : m;
            
            // 初始化第一列
            for (int row = 1; row < m; row++) {
                dp[row][l] = (str2[row] == str1[l] || dp[row - 1][l] != m) ? row : m;
            }
            ans = Math.min(ans, dp[m - 1][l]);
            
            // 扩展子串长度
            for (int r = l + 1; r < n && r - l < m; r++) {
                int first = r - l;
                
                // 处理第一行
                dp[first][r] = (str2[first] == str1[r] && dp[first - 1][r - 1] == 0) ? 0 : m;
                
                // 处理其他行
                for (int row = first + 1; row < m; row++) {
                    dp[row][r] = m;
                    
                    // 删除操作
                    if (dp[row - 1][r] != m) {
                        dp[row][r] = dp[row - 1][r] + 1;
                    }
                    
                    // 匹配操作
                    if (dp[row - 1][r - 1] != m && str2[row] == str1[r]) {
                        dp[row][r] = Math.min(dp[row][r], dp[row - 1][r - 1]);
                    }
                }
                ans = Math.min(ans, dp[m - 1][r]);
            }
        }
        return ans;
    }

    /**
     * 方法4：贪心匹配算法
     * 
     * 算法思路：
     * 1. 为s1的每个字符建立位置索引
     * 2. 对s2的每个字符作为起点，贪心地在s1中找最长匹配序列
     * 3. 返回s2长度减去最长匹配长度
     * 
     * 时间复杂度：O(M * N²)
     * 空间复杂度：O(M)
     * 
     * @param s1 目标字符串
     * @param s2 源字符串
     * @return 最少删除字符数
     */
    public static int min4(String s1, String s2) {
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 建立字符到位置列表的映射
        HashMap<Character, ArrayList<Integer>> map1 = new HashMap<>();
        for (int i = 0; i < str1.length; i++) {
            ArrayList<Integer> list = map1.getOrDefault(str1[i], new ArrayList<>());
            list.add(i);
            map1.put(str1[i], list);
        }
        
        int ans = 0; // 记录找到的最长匹配长度
        
        // 以s2的每个字符为起点尝试匹配
        for (int i = 0; i < str2.length; i++) {
            if (map1.containsKey(str2[i])) {
                ArrayList<Integer> c1List = map1.get(str2[i]);
                
                // 尝试s1中该字符的每个位置
                for (int j = 0; j < c1List.size(); j++) {
                    int cur1 = c1List.get(j) + 1; // s1的当前位置
                    int cur2 = i + 1;             // s2的当前位置
                    int count = 1;                // 当前匹配长度
                    
                    // 贪心匹配后续字符
                    for (int k = cur2; k < str2.length && cur1 < str1.length; k++) {
                        if (str2[k] == str1[cur1]) {
                            cur1++;
                            count++;
                        }
                    }
                    ans = Math.max(ans, count); // 更新最长匹配长度
                }
            }
        }
        
        return s2.length() - ans; // 删除字符数 = 总长度 - 最长匹配长度
    }

    /**
     * 生成随机字符串用于测试
     * 
     * @param l 最大长度
     * @param v 字符种类数（使用前v个小写字母）
     * @return 随机字符串
     */
    private static String randomStr(int l, int v) {
        int len = (int) (Math.random() * l);
        char[] str = new char[len];
        for (int i = 0; i < len; i++) {
            str[i] = (char) ('a' + (int) (v * Math.random()));
        }
        return String.valueOf(str);
    }

    /**
     * 测试方法：验证所有算法的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== 字符串删除最小代价测试 ===");
        
        // 基本测试用例
        String s1 = "abcdf";
        String s2 = "ab12cd3";
        
        System.out.println("基本测试：");
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);
        System.out.println("方法1结果: " + min1(s1, s2));
        System.out.println("方法2结果: " + min2(s1, s2));
        System.out.println("方法3结果: " + min3(s1, s2));
        System.out.println("方法4结果: " + min4(s1, s2));
        
        // 随机测试验证正确性
        int times = 10000;
        int s1len = 20;
        int s2len = 10;
        int v = 5;
        
        System.out.println("\n开始随机测试验证...");
        for (int i = 0; i < times; i++) {
            String rs1 = randomStr(s1len, v);
            String rs2 = randomStr(s2len, v);
            
            int ans1 = min1(rs1, rs2);
            int ans2 = min2(rs1, rs2);
            int ans3 = min3(rs1, rs2);
            int ans4 = min4(rs1, rs2);
            
            if (ans1 != ans2 || ans3 != ans4 || ans1 != ans3) {
                System.out.println("算法结果不一致！");
                System.out.println("测试用例: s1=" + rs1 + ", s2=" + rs2);
                System.out.println("结果: " + ans1 + "|" + ans2 + "|" + ans3 + "|" + ans4);
                break;
            }
        }
        System.out.println("随机测试完成！");
    }
}
