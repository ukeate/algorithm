package basic.c35;

/**
 * 编辑距离问题（Edit Distance）
 * 
 * 问题描述：
 * 给定两个字符串s1和s2，以及三种编辑操作的代价：
 * - 插入一个字符的代价：ic（insert cost）
 * - 删除一个字符的代价：dc（delete cost）
 * - 替换一个字符的代价：rc（replace cost）
 * 
 * 求将字符串s1编辑成s2所需的最小代价。
 * 
 * 经典变种：
 * - 当ic=dc=rc=1时，就是经典的编辑距离（Levenshtein Distance）
 * - 支持不同的操作代价，使问题更加灵活
 * 
 * 解决方案：
 * 1. 方法1：标准二维动态规划 - 空间复杂度O(M*N)
 * 2. 方法2：空间优化版动态规划 - 空间复杂度O(min(M,N))
 * 
 * 算法核心思想：
 * dp[i][j]表示将s1[0...i-1]编辑成s2[0...j-1]的最小代价
 * 
 * 时间复杂度：O(M*N)，其中M和N分别是两个字符串的长度
 * 空间复杂度：方法1为O(M*N)，方法2为O(min(M,N))
 */
public class EditCost {
    
    /**
     * 方法1：标准二维动态规划解法
     * 
     * 算法思路：
     * 定义dp[i][j]为将s1[0...i-1]编辑成s2[0...j-1]的最小代价
     * 
     * 状态转移方程：
     * 1. 边界条件：
     *    - dp[i][0] = dc * i（删除s1的前i个字符）
     *    - dp[0][j] = ic * j（插入s2的前j个字符）
     * 
     * 2. 一般情况：dp[i][j]可能来自三种操作：
     *    - 替换操作：dp[i-1][j-1] + (s1[i-1]==s2[j-1] ? 0 : rc)
     *    - 插入操作：dp[i][j-1] + ic（在s1中插入s2[j-1]）
     *    - 删除操作：dp[i-1][j] + dc（删除s1[i-1]）
     * 
     * @param s1 源字符串
     * @param s2 目标字符串
     * @param ic 插入代价
     * @param dc 删除代价
     * @param rc 替换代价
     * @return 最小编辑代价
     */
    public static int min1(String s1, String s2, int ic, int dc, int rc) {
        // 边界条件处理
        if (s1 == null || s2 == null) {
            return 0;
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int n = str1.length + 1;  // s1长度+1
        int m = str2.length + 1;  // s2长度+1
        
        // 创建DP表
        int[][] dp = new int[n][m];
        
        // 初始化第一行：s1为空，需要插入s2的所有字符
        for (int j = 1; j < m; j++) {
            dp[0][j] = ic * j;
        }
        
        // 初始化第一列：s2为空，需要删除s1的所有字符
        for (int i = 1; i < n; i++) {
            dp[i][0] = dc * i;
        }
        
        // 填充DP表
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < m; j++) {
                // 情况1：替换操作（或字符相同，无需操作）
                if (str1[i - 1] == str2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];  // 字符相同，无额外代价
                } else {
                    dp[i][j] = dp[i - 1][j - 1] + rc;  // 替换操作
                }
                
                // 情况2：插入操作（在s1中插入s2[j-1]）
                dp[i][j] = Math.min(dp[i][j], dp[i][j - 1] + ic);
                
                // 情况3：删除操作（删除s1[i-1]）
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + dc);
            }
        }
        
        return dp[n - 1][m - 1];
    }

    /**
     * 方法2：空间优化的动态规划解法
     * 
     * 算法思路：
     * 观察到dp[i][j]只依赖于dp[i-1][j-1]、dp[i-1][j]和dp[i][j-1]
     * 可以用一维数组滚动更新，大大节省空间
     * 
     * 优化策略：
     * 1. 让较长的字符串作为"行"方向，较短的作为"列"方向
     * 2. 只需要一个长度为min(M,N)+1的一维数组
     * 3. 用变量pre记录左上角的值（对应dp[i-1][j-1]）
     * 
     * @param str1 源字符串
     * @param str2 目标字符串
     * @param ic 插入代价
     * @param dc 删除代价
     * @param rc 替换代价
     * @return 最小编辑代价
     */
    public static int min2(String str1, String str2, int ic, int dc, int rc) {
        // 边界条件处理
        if (str1 == null || str2 == null) {
            return 0;
        }
        
        char[] s1 = str1.toCharArray();
        char[] s2 = str2.toCharArray();
        
        // 确保s1是较长的字符串，s2是较短的字符串
        char[] longs = s1.length >= s2.length ? s1 : s2;
        char[] shorts = s1.length < s2.length ? s1 : s2;
        
        // 如果交换了字符串，需要交换对应的操作代价
        // 因为现在是将longs编辑成shorts
        if (s1.length < s2.length) {
            int tmp = ic;
            ic = dc;  // 原来的插入变成删除
            dc = tmp; // 原来的删除变成插入
        }
        
        // 创建一维DP数组，长度为较短字符串长度+1
        int[] dp = new int[shorts.length + 1];
        
        // 初始化第一行：longs为空，需要插入shorts的所有字符
        for (int i = 1; i <= shorts.length; i++) {
            dp[i] = ic * i;
        }
        
        // 逐行更新DP数组
        for (int i = 1; i <= longs.length; i++) {
            int pre = dp[0];  // 保存左上角的值（dp[i-1][j-1]）
            dp[0] = dc * i;   // 更新第一列：shorts为空，需要删除longs的前i个字符
            
            for (int j = 1; j <= shorts.length; j++) {
                int tmp = dp[j];  // 保存当前值，稍后会成为下一轮的pre
                
                // 计算dp[i][j]的值
                if (longs[i - 1] == shorts[j - 1]) {
                    dp[j] = pre;  // 字符相同，使用左上角的值
                } else {
                    dp[j] = pre + rc;  // 替换操作
                }
                
                // 比较插入和删除操作
                dp[j] = Math.min(dp[j], dp[j - 1] + ic);  // 插入操作
                dp[j] = Math.min(dp[j], tmp + dc);        // 删除操作
                
                pre = tmp;  // 更新pre为当前行的前一个位置的值
            }
        }
        
        return dp[shorts.length];
    }

    /**
     * 测试方法：验证两种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 编辑距离算法测试 ===");
        
        // 测试用例1：基本情况
        String s1 = "ab12cd3";
        String s2 = "abcdf";
        int ic = 5, dc = 3, rc = 2;
        
        System.out.println("测试用例1:");
        System.out.println("源字符串: \"" + s1 + "\"");
        System.out.println("目标字符串: \"" + s2 + "\"");
        System.out.println("操作代价 - 插入:" + ic + ", 删除:" + dc + ", 替换:" + rc);
        
        int result1_1 = min1(s1, s2, ic, dc, rc);
        int result1_2 = min2(s1, s2, ic, dc, rc);
        
        System.out.println("方法1结果: " + result1_1);
        System.out.println("方法2结果: " + result1_2);
        System.out.println("结果一致: " + (result1_1 == result1_2));
        System.out.println();

        // 测试用例2：反向操作
        s1 = "abcdf";
        s2 = "ab12cd3";
        ic = 3; dc = 2; rc = 4;
        
        System.out.println("测试用例2（反向操作）:");
        System.out.println("源字符串: \"" + s1 + "\"");
        System.out.println("目标字符串: \"" + s2 + "\"");
        System.out.println("操作代价 - 插入:" + ic + ", 删除:" + dc + ", 替换:" + rc);
        
        int result2_1 = min1(s1, s2, ic, dc, rc);
        int result2_2 = min2(s1, s2, ic, dc, rc);
        
        System.out.println("方法1结果: " + result2_1);
        System.out.println("方法2结果: " + result2_2);
        System.out.println("结果一致: " + (result2_1 == result2_2));
        System.out.println();

        // 测试用例3：空字符串
        s1 = "";
        s2 = "ab12cd3";
        ic = 1; dc = 7; rc = 5;
        
        System.out.println("测试用例3（空源字符串）:");
        System.out.println("源字符串: \"" + s1 + "\"");
        System.out.println("目标字符串: \"" + s2 + "\"");
        System.out.println("操作代价 - 插入:" + ic + ", 删除:" + dc + ", 替换:" + rc);
        
        int result3_1 = min1(s1, s2, ic, dc, rc);
        int result3_2 = min2(s1, s2, ic, dc, rc);
        
        System.out.println("方法1结果: " + result3_1);
        System.out.println("方法2结果: " + result3_2);
        System.out.println("结果一致: " + (result3_1 == result3_2));
        System.out.println();

        // 测试用例4：空目标字符串
        s1 = "abcdf";
        s2 = "";
        ic = 2; dc = 9; rc = 8;
        
        System.out.println("测试用例4（空目标字符串）:");
        System.out.println("源字符串: \"" + s1 + "\"");
        System.out.println("目标字符串: \"" + s2 + "\"");
        System.out.println("操作代价 - 插入:" + ic + ", 删除:" + dc + ", 替换:" + rc);
        
        int result4_1 = min1(s1, s2, ic, dc, rc);
        int result4_2 = min2(s1, s2, ic, dc, rc);
        
        System.out.println("方法1结果: " + result4_1);
        System.out.println("方法2结果: " + result4_2);
        System.out.println("结果一致: " + (result4_1 == result4_2));
        System.out.println();
        
        // 测试用例5：经典编辑距离（所有操作代价为1）
        s1 = "kitten";
        s2 = "sitting";
        ic = dc = rc = 1;
        
        System.out.println("测试用例5（经典编辑距离）:");
        System.out.println("源字符串: \"" + s1 + "\"");
        System.out.println("目标字符串: \"" + s2 + "\"");
        System.out.println("所有操作代价均为1");
        
        int result5_1 = min1(s1, s2, ic, dc, rc);
        int result5_2 = min2(s1, s2, ic, dc, rc);
        
        System.out.println("方法1结果: " + result5_1);
        System.out.println("方法2结果: " + result5_2);
        System.out.println("结果一致: " + (result5_1 == result5_2));
        System.out.println("经典编辑距离: " + result5_1);
    }
}
