package base.str2;

/**
 * 最长公共子串问题
 * 
 * 问题描述：
 * 给定两个字符串，找到它们的最长公共子串（连续字符序列）。
 * 
 * 核心思想：
 * 1. 动态规划方法：构建二维DP表记录以每个位置结尾的公共子串长度
 * 2. 滚动优化方法：通过对角线遍历避免使用二维数组，空间O(1)
 * 
 * 算法对比：
 * - 方法1：经典DP，时间O(M*N)，空间O(M*N)
 * - 方法2：滚动数组优化，时间O(M*N)，空间O(1)
 * 
 * 应用场景：
 * - 字符串相似度计算
 * - 生物信息学中的序列比对
 * - 文本差异检测
 * - 数据去重和相似性分析
 * 
 * @author algorithm-base
 * @version 1.0
 */
public class LCSubstring {
    
    /**
     * 构建动态规划表格
     * 
     * DP状态定义：
     * dp[i][j] = 以str1[i]和str2[j]结尾的最长公共子串长度
     * 
     * 状态转移方程：
     * if str1[i] == str2[j]:
     *     dp[i][j] = dp[i-1][j-1] + 1
     * else:
     *     dp[i][j] = 0
     * 
     * 边界条件：
     * - 第一行：如果str1[0] == str2[j]，则dp[0][j] = 1
     * - 第一列：如果str1[i] == str2[0]，则dp[i][0] = 1
     * 
     * @param str1 第一个字符数组
     * @param str2 第二个字符数组
     * @return 二维DP表格
     * 
     * 时间复杂度：O(M*N)，M和N分别为两字符串长度
     * 空间复杂度：O(M*N)，存储DP表格
     */
    private static int[][] getDp(char[] str1, char[] str2) {
        int[][] dp = new int[str1.length][str2.length];
        
        // 初始化第一行：str1[0]与str2各字符的比较
        for (int i = 0; i < str1.length; i++) {
            if (str1[i] == str2[0]) {
                dp[i][0] = 1;
            }
        }
        
        // 初始化第一列：str2[0]与str1各字符的比较
        for (int j = 1; j < str2.length; j++) {
            if (str1[0] == str2[j]) {
                dp[0][j] = 1;
            }
        }
        
        // 填充DP表格：从(1,1)开始逐行填充
        for (int i = 1; i < str1.length; i++) {
            for (int j = 1; j < str2.length; j++) {
                if (str1[i] == str2[j]) {
                    // 字符匹配：长度为左上角值+1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                }
                // 字符不匹配：dp[i][j] = 0（已初始化）
            }
        }
        return dp;
    }

    /**
     * 方法1：基于动态规划的最长公共子串求解
     * 
     * 算法流程：
     * 1. 构建DP表格记录所有位置的公共子串长度
     * 2. 遍历DP表格找到最大值及其位置
     * 3. 根据最大值位置回溯得到公共子串
     * 
     * 示例分析：
     * str1 = "ABC1234567DEFG", str2 = "HIJKL123456MNOP"
     * 
     * DP表格（部分）：
     *       H I J K L 1 2 3 4 5 6 M N O P
     *   A   0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     *   B   0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     *   C   0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
     *   1   0 0 0 0 0 1 0 0 0 0 0 0 0 0 0
     *   2   0 0 0 0 0 0 2 0 0 0 0 0 0 0 0
     *   3   0 0 0 0 0 0 0 3 0 0 0 0 0 0 0
     *   4   0 0 0 0 0 0 0 0 4 0 0 0 0 0 0
     *   5   0 0 0 0 0 0 0 0 0 5 0 0 0 0 0
     *   6   0 0 0 0 0 0 0 0 0 0 6 0 0 0 0  ← 最大值6
     * 
     * 最长公共子串："123456"
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共子串
     * 
     * 时间复杂度：O(M*N + M*N) = O(M*N)，构建DP表格 + 查找最大值
     * 空间复杂度：O(M*N)，存储完整DP表格
     */
    public static String lcsPath1(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return "";
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        int[][] dp = getDp(str1, str2);
        
        // 查找DP表格中的最大值及其位置
        int end = 0;      // 最长公共子串在str1中的结束位置
        int max = 0;      // 最长公共子串的长度
        
        for (int i = 0; i < str1.length; i++) {
            for (int j = 0; j < str2.length; j++) {
                if (dp[i][j] > max) {
                    end = i;    // 记录结束位置
                    max = dp[i][j];  // 更新最大长度
                }
            }
        }
        
        // 根据结束位置和长度提取子串
        // 子串范围：[end-max+1, end+1)
        return s1.substring(end - max + 1, end + 1);
    }

    /**
     * 方法2：滚动数组优化的最长公共子串求解
     * 
     * 核心思想：
     * 观察DP表格的填充过程，发现计算dp[i][j]只需要dp[i-1][j-1]的值。
     * 可以通过对角线遍历的方式，用O(1)空间完成计算。
     * 
     * 遍历策略：
     * 1. 从右上角(0, str2.length-1)开始
     * 2. 沿对角线向右下方向遍历
     * 3. 当到达右边界时，行数加1，列数重置为0
     * 4. 继续对角线遍历直到遍历完所有位置
     * 
     * 对角线遍历示例（4x4矩阵）：
     * ┌─────┬─────┬─────┬─────┐
     * │(0,3)│  ↘  │     │     │
     * ├─────┼─────┼─────┼─────┤
     * │     │(1,2)│(1,3)│  ↘  │
     * ├─────┼─────┼─────┼─────┤  
     * │     │     │(2,1)│(2,2)│
     * ├─────┼─────┼─────┼─────┤
     * │(3,0)│  ↘  │     │(3,3)│
     * └─────┴─────┴─────┴─────┘
     * 
     * @param s1 第一个字符串
     * @param s2 第二个字符串
     * @return 最长公共子串
     * 
     * 时间复杂度：O(M*N)，每个位置访问一次
     * 空间复杂度：O(1)，只使用常量额外空间
     */
    public static String lcsPath2(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0) {
            return "";
        }
        
        char[] str1 = s1.toCharArray();
        char[] str2 = s2.toCharArray();
        
        // 初始化遍历起点：右上角
        int row = 0;                    // 当前对角线起始行
        int col = str2.length - 1;      // 当前对角线起始列
        int max = 0;                    // 最长公共子串长度
        int end = 0;                    // 最长公共子串在str1中的结束位置
        
        // 遍历所有对角线
        while (row < str1.length) {
            int i = row;      // 当前行指针
            int j = col;      // 当前列指针
            int len = 0;      // 当前对角线上的连续匹配长度
            
            // 沿对角线向右下遍历
            while (i < str1.length && j < str2.length) {
                if (str1[i] != str2[j]) {
                    len = 0;  // 字符不匹配，连续长度重置
                } else {
                    len++;    // 字符匹配，连续长度增加
                }
                
                // 更新最大值
                if (len > max) {
                    end = i;   // 记录当前结束位置
                    max = len; // 更新最大长度
                }
                
                i++;  // 向下移动
                j++;  // 向右移动
            }
            
            // 移动到下一条对角线的起点
            if (col > 0) {
                col--;  // 列向左移动（上半部分对角线）
            } else {
                row++;  // 行向下移动（下半部分对角线）
            }
        }
        
        // 根据结束位置和长度提取子串
        return s1.substring(end - max + 1, end + 1);
    }

    /**
     * 测试方法：验证两种算法的正确性和性能
     * 
     * 测试用例分析：
     * str1 = "ABC1234567DEFG" (长度14)
     * str2 = "HIJKL123456MNOP" (长度15)
     * 
     * 公共子串分析：
     * - "A", "B", "C"：长度1的公共子串
     * - "1", "2", "3", "4", "5", "6"：单个字符匹配
     * - "123456"：最长公共子串，长度6
     * 
     * 预期输出：
     * 方法1结果: 123456
     * 方法2结果: 123456
     * 
     * 性能对比：
     * - 时间复杂度：两种方法都是O(M*N)
     * - 空间复杂度：方法1为O(M*N)，方法2为O(1)
     * - 实际应用：小字符串用方法1（代码简单），大字符串用方法2（节省内存）
     */
    public static void main(String[] args) {
        String str1 = "ABC1234567DEFG";
        String str2 = "HIJKL123456MNOP";
        
        System.out.println("最长公共子串问题测试");
        System.out.println("字符串1: \"" + str1 + "\" (长度: " + str1.length() + ")");
        System.out.println("字符串2: \"" + str2 + "\" (长度: " + str2.length() + ")");
        System.out.println();
        
        // 测试两种方法
        String result1 = lcsPath1(str1, str2);
        String result2 = lcsPath2(str1, str2);
        
        System.out.println("方法1 (DP表格): \"" + result1 + "\" (长度: " + result1.length() + ")");
        System.out.println("方法2 (滚动优化): \"" + result2 + "\" (长度: " + result2.length() + ")");
        System.out.println();
        
        // 验证结果一致性
        if (result1.equals(result2)) {
            System.out.println("✓ 两种方法结果一致");
        } else {
            System.out.println("✗ 两种方法结果不一致");
        }
        System.out.println();
        
        // 算法复杂度分析
        System.out.println("算法复杂度分析：");
        System.out.println("方法1 - DP表格法：");
        System.out.println("  时间复杂度: O(M*N) - 填充DP表格");
        System.out.println("  空间复杂度: O(M*N) - 存储完整DP表格");
        System.out.println("  优点: 代码逻辑清晰，便于理解和调试");
        System.out.println("  缺点: 空间开销大，不适合处理长字符串");
        System.out.println();
        System.out.println("方法2 - 滚动优化法：");
        System.out.println("  时间复杂度: O(M*N) - 对角线遍历");
        System.out.println("  空间复杂度: O(1) - 只使用常量空间");
        System.out.println("  优点: 内存效率高，适合处理大数据");
        System.out.println("  缺点: 代码逻辑相对复杂");
        System.out.println();
        
        // 应用场景说明
        System.out.println("应用场景：");
        System.out.println("• 文档相似度比较");
        System.out.println("• 基因序列比对（生物信息学）");
        System.out.println("• 代码抄袭检测");
        System.out.println("• 数据去重和模糊匹配");
    }
}
