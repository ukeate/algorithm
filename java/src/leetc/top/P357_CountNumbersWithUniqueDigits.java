package leetc.top;

/**
 * LeetCode 357. 统计各位数字都不同的数字个数 (Count Numbers with Unique Digits)
 * 
 * 问题描述：
 * 给你一个整数 n ，统计并返回各位数字都不同的数字 x 的个数，其中 0 <= x < 10^n 。
 * 
 * 示例：
 * 输入：n = 2
 * 输出：91
 * 解释：答案应为除了 11, 22, 33, 44, 55, 66, 77, 88, 99 外，在 0 ≤ x < 100 范围内的所有数字。
 * 
 * 输入：n = 0
 * 输出：1
 * 解释：只有一个数字：0
 * 
 * 提示：0 <= n <= 8
 * 
 * 解法思路：
 * 排列组合数学 + 动态规划：
 * 
 * 1. 核心思想：
 *    - 这是一个排列组合问题，需要分别计算1位数、2位数、...、n位数中各位数字都不同的数字个数
 *    - 对于k位数，第一位有9种选择（1-9），后续位依次有9、8、7...种选择
 *    - 最终答案是所有位数的累加
 * 
 * 2. 数学分析：
 *    - 1位数：1,2,3,4,5,6,7,8,9 共9个（0需要特殊处理）
 *    - 2位数：第一位9种选择（1-9），第二位9种选择（0-9除了第一位） = 9 × 9 = 81个
 *    - 3位数：第一位9种选择，第二位9种选择，第三位8种选择 = 9 × 9 × 8 = 648个
 *    - k位数：9 × 9 × 8 × 7 × ... × (11-k)个
 * 
 * 3. 特殊情况：
 *    - n = 0: 只有数字0，返回1
 *    - n = 1: 包含0-9，共10个数字
 *    - n > 10: 理论上不可能有超过10位数字都不同的数字
 * 
 * 4. 动态规划状态：
 *    - dp[i] 表示恰好i位数且各位数字都不同的数字个数
 *    - dp[1] = 9（1-9）
 *    - dp[i] = dp[i-1] × (11-i) for i > 1
 * 
 * 核心思想：
 * - 排列计算：利用排列公式计算各位数字都不同的情况
 * - 分位累加：分别计算每种位数的贡献
 * - 边界处理：正确处理0和特殊情况
 * 
 * 关键技巧：
 * - 首位限制：第一位不能是0（除了数字0本身）
 * - 逐位减少：每选择一位数字，后续选择就减少一种
 * - 累积求和：所有位数的结果累加
 * 
 * 时间复杂度：O(n) - 计算n种不同位数
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/count-numbers-with-unique-digits/
 */
public class P357_CountNumbersWithUniqueDigits {
    
    /**
     * 方法一：数学公式计算（推荐）
     * 
     * 直接使用排列组合公式计算结果
     * 
     * @param n 数字的最大位数
     * @return 各位数字都不同的数字个数
     */
    public int countNumbersWithUniqueDigits(int n) {
        // 边界情况
        if (n == 0) return 1;  // 只有数字0
        if (n == 1) return 10; // 数字0-9
        
        // 基础情况：1位数有9个（1-9，不包括0，因为0会单独计算）
        int result = 10; // 包含0-9的所有1位数
        int uniqueDigits = 9; // 当前位数的各位数字都不同的数字个数
        
        // 计算2位数到n位数的贡献
        for (int i = 2; i <= n; i++) {
            // i位数的第一位有9种选择（1-9）
            // 后续每一位的选择数依次递减：9,8,7,...
            uniqueDigits *= (11 - i); // 第i位有(11-i)种选择
            result += uniqueDigits;
        }
        
        return result;
    }
    
    /**
     * 方法二：动态规划解法
     * 
     * 使用动态规划的思想，逐步构建答案
     * 
     * @param n 数字的最大位数
     * @return 各位数字都不同的数字个数
     */
    public int countNumbersWithUniqueDigitsDP(int n) {
        if (n == 0) return 1;
        
        // dp[i] 表示恰好i位且各位数字都不同的数字个数
        int[] dp = new int[n + 1];
        
        // 基础情况
        dp[0] = 1;  // 数字0
        if (n >= 1) dp[1] = 9; // 1-9
        
        // 状态转移
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] * (11 - i);
        }
        
        // 累加所有位数的结果
        int result = 1; // 包含数字0
        for (int i = 1; i <= n; i++) {
            result += dp[i];
        }
        
        return result;
    }
    
    /**
     * 方法三：递归解法
     * 
     * 使用递归计算排列数
     * 
     * @param n 数字的最大位数
     * @return 各位数字都不同的数字个数
     */
    public int countNumbersWithUniqueDigitsRecursive(int n) {
        if (n == 0) return 1;
        
        return 1 + countUniqueDigits(n, 0, 0);
    }
    
    /**
     * 递归辅助函数
     * 
     * @param maxLen 最大长度
     * @param currentLen 当前长度
     * @param used 已使用数字的位掩码
     * @return 从当前状态开始的各位数字都不同的数字个数
     */
    private int countUniqueDigits(int maxLen, int currentLen, int used) {
        if (currentLen == maxLen) {
            return 0;
        }
        
        int count = 0;
        for (int digit = (currentLen == 0) ? 1 : 0; digit <= 9; digit++) {
            if ((used & (1 << digit)) == 0) {
                // 如果当前数字未被使用
                count += 1 + countUniqueDigits(maxLen, currentLen + 1, used | (1 << digit));
            }
        }
        
        return count;
    }
    
    /**
     * 方法四：预计算优化
     * 
     * 由于n的范围有限（0<=n<=8），可以预计算所有结果
     * 
     * @param n 数字的最大位数
     * @return 各位数字都不同的数字个数
     */
    public int countNumbersWithUniqueDigitsPrecomputed(int n) {
        // 预计算的结果数组 n=0到n=8
        int[] precomputed = {1, 10, 91, 739, 5275, 32491, 168571, 712891, 2345851};
        
        return n < precomputed.length ? precomputed[n] : precomputed[precomputed.length - 1];
    }
    
    /**
     * 方法五：详细分析版本
     * 
     * 带有详细计算过程的版本，便于理解
     * 
     * @param n 数字的最大位数
     * @return 各位数字都不同的数字个数
     */
    public int countNumbersWithUniqueDigitsDetailed(int n) {
        if (n == 0) return 1; // 只有0
        
        int totalCount = 10; // 1位数：0,1,2,3,4,5,6,7,8,9
        
        // 计算2位数到n位数
        for (int digits = 2; digits <= n; digits++) {
            int count = 9; // 第一位数字的选择（1-9）
            
            // 计算后续位数的选择
            for (int pos = 1; pos < digits; pos++) {
                count *= (10 - pos); // 第pos+1位有(10-pos)种选择
            }
            
            totalCount += count;
        }
        
        return totalCount;
    }
    
    /**
     * 辅助方法：计算阶乘
     * 
     * @param n 数字
     * @return n的阶乘
     */
    private int factorial(int n) {
        if (n <= 1) return 1;
        return n * factorial(n - 1);
    }
    
    /**
     * 辅助方法：计算排列数P(n,r) = n!/(n-r)!
     * 
     * @param n 总数
     * @param r 选择数
     * @return 排列数
     */
    private int permutation(int n, int r) {
        if (r > n || r < 0) return 0;
        
        int result = 1;
        for (int i = 0; i < r; i++) {
            result *= (n - i);
        }
        return result;
    }
    
    /**
     * 方法六：使用排列公式的版本
     * 
     * 明确使用排列公式P(n,r)的实现
     * 
     * @param n 数字的最大位数
     * @return 各位数字都不同的数字个数
     */
    public int countNumbersWithUniqueDigitsPermutation(int n) {
        if (n == 0) return 1;
        if (n == 1) return 10;
        
        int result = 10; // 1位数的贡献
        
        for (int digits = 2; digits <= n; digits++) {
            // digits位数的贡献
            // 第一位：9种选择（1-9）
            // 后续位：P(9, digits-1) = 9!/(9-(digits-1))! = 9×8×...×(11-digits)
            int contribution = 9 * permutation(9, digits - 1);
            result += contribution;
        }
        
        return result;
    }
    
    /**
     * 测试和演示方法
     */
    public void demonstrateCalculation(int n) {
        System.out.println("计算 n=" + n + " 的各位数字都不同的数字个数：");
        
        if (n == 0) {
            System.out.println("n=0: 只有数字0，结果=1");
            return;
        }
        
        System.out.println("1位数: 0,1,2,3,4,5,6,7,8,9 共10个");
        int total = 10;
        
        for (int digits = 2; digits <= n; digits++) {
            int count = 9; // 第一位
            StringBuilder calculation = new StringBuilder("" + digits + "位数: 9");
            
            for (int pos = 1; pos < digits; pos++) {
                int choices = 10 - pos;
                count *= choices;
                calculation.append(" × ").append(choices);
            }
            
            calculation.append(" = ").append(count);
            System.out.println(calculation.toString());
            total += count;
        }
        
        System.out.println("总计: " + total);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        P357_CountNumbersWithUniqueDigits solution = new P357_CountNumbersWithUniqueDigits();
        
        // 测试所有方法的一致性
        for (int n = 0; n <= 8; n++) {
            int result1 = solution.countNumbersWithUniqueDigits(n);
            int result2 = solution.countNumbersWithUniqueDigitsDP(n);
            int result3 = solution.countNumbersWithUniqueDigitsPrecomputed(n);
            
            System.out.printf("n=%d: 方法1=%d, 方法2=%d, 方法3=%d%n", n, result1, result2, result3);
            
            // 验证结果一致性
            assert result1 == result2 && result2 == result3 : "结果不一致！";
        }
        
        // 详细演示几个例子
        System.out.println("\n=== 详细计算过程 ===");
        solution.demonstrateCalculation(0);
        System.out.println();
        solution.demonstrateCalculation(2);
        System.out.println();
        solution.demonstrateCalculation(3);
    }
} 