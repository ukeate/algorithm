package basic.c46;

/**
 * 整数拆分问题（分拆数）
 * 
 * 问题描述：
 * 给定一个正整数n，求有多少种方法可以将n表示为正整数的和
 * 不同的加数顺序视为同一种方法（即考虑的是分拆而不是分解）
 * 
 * 例如：
 * n=4的拆分方法有：
 * (4)
 * (3,1)
 * (2,2)
 * (2,1,1)
 * (1,1,1,1)
 * 共5种方法
 * 
 * 经典递推关系：
 * 设p(n,k)表示用不超过k的正整数拆分n的方法数
 * - 如果k > n，则p(n,k) = p(n,n)
 * - p(n,k) = p(n,k-1) + p(n-k,k)
 *   - p(n,k-1)：不使用k的拆分方法数
 *   - p(n-k,k)：至少使用一个k的拆分方法数
 * 
 * 解法思路：
 * 方法1：递归暴力求解
 * 方法2：动态规划（二维）
 * 方法3：动态规划优化（利用递推关系）
 * 
 * 时间复杂度：O(n²)
 * 空间复杂度：O(n²)
 * 
 * @author 算法学习
 */
public class SplitNum {
    
    /**
     * 递归求解拆分方法数
     * 
     * @param pre 当前可以使用的最小数字（保证有序性）
     * @param rest 剩余需要拆分的数字
     * @return 拆分方法数
     * 
     * 算法思路：
     * 递归尝试每个可能的拆分数字，从pre开始到rest结束
     * 保证拆分结果是非递减序列，避免重复计算
     */
    private static int process(int pre, int rest) {
        if (rest == 0) {
            return 1;  // 成功拆分完成，找到一种方法
        }
        if (pre > rest) {
            return 0;  // 当前可用的最小数字都大于剩余数字，无法拆分
        }
        
        int ways = 0;
        // 尝试使用从pre到rest的每个数字
        for (int i = pre; i <= rest; i++) {
            ways += process(i, rest - i);
        }
        
        return ways;
    }

    /**
     * 方法1：递归暴力求解
     * 
     * @param n 要拆分的正整数
     * @return 拆分方法数
     */
    public static int ways1(int n) {
        if (n < 1) {
            return 0;
        }
        // 从1开始，拆分整个数字n
        return process(1, n);
    }

    /**
     * 方法2：二维动态规划
     * 
     * @param n 要拆分的正整数
     * @return 拆分方法数
     * 
     * 算法思路：
     * dp[pre][rest]表示用不小于pre的数字拆分rest的方法数
     * 状态转移：dp[pre][rest] = sum(dp[i][rest-i]) for i in [pre, rest]
     * 
     * 填表顺序：从大到小填充pre，从小到大填充rest
     */
    public static int ways2(int n) {
        if (n < 1) {
            return 0;
        }
        
        // dp[pre][rest]: 用不小于pre的数字拆分rest的方法数
        int[][] dp = new int[n + 1][n + 1];
        
        // 边界条件：rest=0时，空拆分算作1种方法
        for (int pre = 1; pre < dp.length; pre++) {
            dp[pre][0] = 1;
        }
        
        // 填表：从大到小枚举pre，从小到大枚举rest
        for (int pre = n; pre > 0; pre--) {
            for (int rest = pre; rest <= n; rest++) {  // rest必须>=pre才有意义
                // 状态转移：尝试所有可能的第一个拆分数字
                for (int i = pre; i <= rest; i++) {
                    dp[pre][rest] += dp[i][rest - i];
                }
            }
        }
        
        return dp[1][n];
    }

    /**
     * 方法3：优化的动态规划
     * 
     * @param n 要拆分的正整数
     * @return 拆分方法数
     * 
     * 算法优化：
     * 利用递推关系：dp[pre][rest] = dp[pre+1][rest] + dp[pre][rest-pre]
     * 避免内层循环，将时间复杂度优化
     * 
     * 递推关系解释：
     * - dp[pre+1][rest]：不使用pre的拆分方法数
     * - dp[pre][rest-pre]：至少使用一个pre的拆分方法数
     */
    public static int ways3(int n) {
        if (n < 1) {
            return 0;
        }
        
        int[][] dp = new int[n + 1][n + 1];
        
        // 边界条件1：rest=0时为1
        for (int pre = 1; pre < dp.length; pre++) {
            dp[pre][0] = 1;
        }
        
        // 边界条件2：pre=rest时为1（只能用自己）
        for (int pre = 1; pre < dp.length; pre++) {
            dp[pre][pre] = 1;
        }
        
        // 状态转移：从右下往左上填表
        for (int pre = n - 1; pre > 0; pre--) {
            for (int rest = pre + 1; rest <= n; rest++) {
                // 关键递推关系
                dp[pre][rest] = dp[pre + 1][rest] + dp[pre][rest - pre];
            }
        }
        
        return dp[1][n];
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 整数拆分问题测试 ===");
        
        // 基础测试
        int n = 4;
        System.out.printf("测试数字: %d\n", n);
        
        long start = System.currentTimeMillis();
        int result1 = ways1(n);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = ways2(n);
        long time2 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result3 = ways3(n);
        long time3 = System.currentTimeMillis() - start;
        
        System.out.printf("方法1（递归）: 结果=%d, 耗时=%dms\n", result1, time1);
        System.out.printf("方法2（二维DP）: 结果=%d, 耗时=%dms\n", result2, time2);
        System.out.printf("方法3（优化DP）: 结果=%d, 耗时=%dms\n", result3, time3);
        
        // 验证结果一致性
        if (result1 == result2 && result2 == result3) {
            System.out.println("✓ 所有方法结果一致");
        } else {
            System.out.println("✗ 结果不一致，需要检查算法");
        }
        
        // 手工验证n=4的拆分
        System.out.println("\n=== 手工验证 n=4 的拆分 ===");
        System.out.println("所有拆分方法：");
        System.out.println("1. (4)");
        System.out.println("2. (3,1)");
        System.out.println("3. (2,2)");
        System.out.println("4. (2,1,1)");
        System.out.println("5. (1,1,1,1)");
        System.out.println("总共5种方法");
        System.out.printf("算法计算结果: %d种\n", result2);
        
        // 小规模测试用例
        System.out.println("\n=== 小规模测试用例 ===");
        int[] testCases = {1, 2, 3, 5, 6, 7, 8};
        
        for (int test : testCases) {
            int res = ways3(test);  // 使用最优化的方法
            System.out.printf("n=%d -> %d种拆分方法\n", test, res);
        }
        
        // 展示具体拆分（仅对小数字）
        System.out.println("\n=== 具体拆分展示 ===");
        System.out.println("n=1: (1)");
        System.out.println("n=2: (2), (1,1)");
        System.out.println("n=3: (3), (2,1), (1,1,1)");
        System.out.println("n=5: (5), (4,1), (3,2), (3,1,1), (2,2,1), (2,1,1,1), (1,1,1,1,1)");
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int largeN = 25;
        
        System.out.printf("大数字测试: n=%d\n", largeN);
        
        // 只测试优化方法，避免递归方法超时
        start = System.currentTimeMillis();
        int largeResult2 = ways2(largeN);
        long largeTime2 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int largeResult3 = ways3(largeN);
        long largeTime3 = System.currentTimeMillis() - start;
        
        System.out.printf("方法2（二维DP）: 结果=%d, 耗时=%dms\n", largeResult2, largeTime2);
        System.out.printf("方法3（优化DP）: 结果=%d, 耗时=%dms\n", largeResult3, largeTime3);
        
        if (largeResult2 == largeResult3) {
            System.out.println("✓ 大数字测试通过");
        } else {
            System.out.println("✗ 大数字测试失败");
        }
        
        if (largeTime3 > 0 && largeTime2 > 0) {
            System.out.printf("方法3比方法2快 %.2f 倍\n", (double)largeTime2 / largeTime3);
        }
        
        // 边界测试
        System.out.println("\n=== 边界测试 ===");
        System.out.printf("n=0: %d种方法\n", ways3(0));
        System.out.printf("n=1: %d种方法\n", ways3(1));
        
        // 数学知识：分拆数的增长
        System.out.println("\n=== 分拆数增长趋势 ===");
        System.out.println("分拆数增长很快，这是组合数学中的重要序列");
        for (int i = 1; i <= 15; i++) {
            System.out.printf("P(%d) = %d\n", i, ways3(i));
        }
    }
}
