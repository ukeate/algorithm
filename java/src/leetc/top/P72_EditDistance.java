package leetc.top;

/**
 * LeetCode 72. 编辑距离 (Edit Distance)
 * 
 * 问题描述：
 * 给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数。
 * 
 * 你可以对一个单词进行如下三种操作：
 * 1. 插入一个字符
 * 2. 删除一个字符
 * 3. 替换一个字符
 * 
 * 示例：
 * - 输入：word1 = "horse", word2 = "ros"，输出：3
 * - 解释：horse -> rorse (将 'h' 替换为 'r')
 *         rorse -> rose (删除 'r')  
 *         rose -> ros (删除 'e')
 * 
 * 解法思路：
 * 动态规划（经典问题）：
 * 1. dp[i][j]表示将str1[0...i-1]转换为str2[0...j-1]的最少操作数
 * 2. 状态转移方程：
 *    - 如果str1[i-1] == str2[j-1]：dp[i][j] = dp[i-1][j-1]
 *    - 否则：dp[i][j] = min(
 *        dp[i-1][j] + dc,    // 删除str1[i-1]
 *        dp[i][j-1] + ic,    // 插入str2[j-1]到str1  
 *        dp[i-1][j-1] + rc   // 替换str1[i-1]为str2[j-1]
 *      )
 * 3. 边界条件：
 *    - dp[0][j] = j * ic（插入j个字符）
 *    - dp[i][0] = i * dc（删除i个字符）
 * 
 * 空间优化：
 * - 使用一维数组 + 滚动变量，将空间复杂度从O(m×n)优化到O(min(m,n))
 * 
 * 时间复杂度：O(m×n) - m和n分别是两个字符串的长度
 * 空间复杂度：O(min(m,n)) - 使用空间优化的一维DP
 */
public class P72_EditDistance {
    
    /**
     * 计算编辑距离（支持自定义操作代价）
     * 
     * 算法优化：
     * 1. 长短字符串优化：让短字符串作为列，减少空间开销
     * 2. 一维DP滚动：只使用O(shorter.length)的空间
     * 3. 操作代价可配置：插入、删除、替换的代价可以不同
     * 
     * @param str1 源字符串
     * @param str2 目标字符串
     * @param ic 插入操作的代价 (insert cost)
     * @param dc 删除操作的代价 (delete cost)
     * @param rc 替换操作的代价 (replace cost)
     * @return 最少操作数
     */
    private static int minDistance(String str1, String str2, int ic, int dc, int rc) {
        // 边界条件检查
        if (str1 == null || str2 == null) {
            return 0;
        }
        
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        
        // 优化：让短字符串作为列，减少空间复杂度
        char[] longs = s1.length >= s2.length ? s1 : s2;
        char[] shorts = s1.length < s2.length ? s1 : s2;
        
        // 如果s1较短，交换插入和删除的代价
        if (s1.length < s2.length) {
            int tmp = ic;
            ic = dc;
            dc = tmp;
        }
        
        // dp[j]表示将longs[0...i-1]转换为shorts[0...j-1]的最少操作数
        int[] dp = new int[shorts.length + 1];
        
        // 初始化：将空字符串转换为shorts[0...j-1]需要插入j个字符
        for (int i = 1; i <= shorts.length; i++) {
            dp[i] = ic * i;
        }
        
        // 填充DP表：处理longs的每一个字符
        for (int i = 1; i <= longs.length; i++) {
            int pre = dp[0];  // 保存dp[i-1][j-1]的值
            dp[0] = dc * i;   // 将longs[0...i-1]转换为空字符串需要删除i个字符
            
            for (int j = 1; j <= shorts.length; j++) {
                int tmp = dp[j];  // 保存当前dp[j]的值（即dp[i-1][j]）
                
                if (longs[i - 1] == shorts[j - 1]) {
                    // 字符相同，不需要操作
                    dp[j] = pre;
                } else {
                    // 字符不同，考虑替换操作
                    dp[j] = pre + rc;
                }
                
                // 考虑插入操作：在longs中插入shorts[j-1]
                dp[j] = Math.min(dp[j], dp[j - 1] + ic);
                
                // 考虑删除操作：删除longs[i-1]
                dp[j] = Math.min(dp[j], tmp + dc);
                
                pre = tmp;  // 更新pre为下一轮的dp[i-1][j-1]
            }
        }
        
        return dp[shorts.length];
    }

    /**
     * 标准编辑距离计算（插入、删除、替换代价均为1）
     * 
     * @param word1 源单词
     * @param word2 目标单词
     * @return 将word1转换为word2的最少操作数
     */
    public static int minDistance(String word1, String word2) {
        return minDistance(word1, word2, 1, 1, 1);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：horse -> ros
        String word1 = "horse", word2 = "ros";
        System.out.println("输入: word1=\"" + word1 + "\", word2=\"" + word2 + "\"");
        System.out.println("编辑距离: " + minDistance(word1, word2));
        System.out.println("期望结果: 3");
        System.out.println();
        
        // 测试用例2：intention -> execution
        word1 = "intention"; word2 = "execution";
        System.out.println("输入: word1=\"" + word1 + "\", word2=\"" + word2 + "\"");
        System.out.println("编辑距离: " + minDistance(word1, word2));
        System.out.println("期望结果: 5");
        System.out.println();
        
        // 测试用例3：相同字符串
        word1 = "abc"; word2 = "abc";
        System.out.println("输入: word1=\"" + word1 + "\", word2=\"" + word2 + "\"");
        System.out.println("编辑距离: " + minDistance(word1, word2));
        System.out.println("期望结果: 0");
        System.out.println();
        
        // 测试用例4：空字符串
        word1 = ""; word2 = "abc";
        System.out.println("输入: word1=\"" + word1 + "\", word2=\"" + word2 + "\"");
        System.out.println("编辑距离: " + minDistance(word1, word2));
        System.out.println("期望结果: 3");
        System.out.println();
        
        // 测试自定义代价
        System.out.println("=== 自定义操作代价测试 ===");
        word1 = "cat"; word2 = "dog";
        System.out.println("cat -> dog (ic=1, dc=1, rc=2): " + 
                          minDistance(word1, word2, 1, 1, 2));
        System.out.println("cat -> dog (ic=2, dc=2, rc=1): " + 
                          minDistance(word1, word2, 2, 2, 1));
    }
}
