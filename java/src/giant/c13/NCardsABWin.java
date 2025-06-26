package giant.c13;

import java.text.DecimalFormat;

/**
 * N卡牌获胜概率问题（谷歌面试题）
 * 
 * 问题描述：
 * 有一副无限的牌组，每组包含面值为1~N的牌。
 * 游戏规则：
 * 1. 每次从新的一组牌中等概率地抽取一张牌（概率为1/N）
 * 2. 当累加和 < a 时，必须继续抽牌
 * 3. 当累加和 >= a 且 < b 时，获胜
 * 4. 当累加和 >= b 时，失败
 * 求获胜的概率
 * 
 * 特殊情况：
 * 原始问题：N=10, a=17, b=21（经典21点游戏变种）
 * 一般情况：任意N, a, b值
 * 
 * 解决方案：
 * 1. 方法1：简单递归法 - 针对原始问题，时间复杂度O(10^a)
 * 2. 方法2：通用递归法 - 适用于任意参数，时间复杂度O(N^a)
 * 3. 方法3：数学公式优化法 - 利用递推关系，时间复杂度O(a)
 * 4. 方法4：动态规划法 - 自底向上计算，时间复杂度O(a)
 * 
 * 核心数学观察：
 * p(i) = (p(i+1) + p(i+2) + ... + p(i+N)) / N
 * 利用这个递推关系可以显著优化计算过程
 * 
 * 时间复杂度：O(a) - 方法4优化版本
 * 空间复杂度：O(a)
 */
public class NCardsABWin {
    
    /**
     * 递归计算函数（方法1 - 原始问题版本）
     * 
     * 算法思路：
     * 从当前累加和cur开始，递归计算获胜概率
     * 
     * 边界条件：
     * - 如果 cur >= 17 且 cur < 21，获胜，概率为1.0
     * - 如果 cur >= 21，失败，概率为0.0
     * - 否则必须继续抽牌
     * 
     * 递推关系：
     * P(cur) = (P(cur+1) + P(cur+2) + ... + P(cur+10)) / 10
     * 
     * @param cur 当前累加和
     * @return 从当前状态开始的获胜概率
     */
    private static double p1(int cur) {
        if (cur >= 17 && cur < 21) {
            return 1.0;  // 获胜区间，概率为1
        }
        if (cur >= 21) {
            return 0.0;  // 失败区间，概率为0
        }
        
        // 必须继续抽牌，计算所有可能结果的平均概率
        double w = 0.0;
        for (int i = 1; i <= 10; i++) {
            w += p1(cur + i);  // 抽到i的概率为1/10
        }
        return w / 10;  // 返回期望概率
    }

    /**
     * 方法1：简单递归法（原始问题）
     * 
     * 专门针对N=10, a=17, b=21的经典21点问题
     * 
     * 时间复杂度：O(10^17)，指数级复杂度
     * 空间复杂度：O(17)，递归调用栈深度
     * 
     * 适用场景：仅用于验证算法正确性，实际应用中太慢
     * 
     * @return 获胜概率
     */
    public static double rate1() {
        return p1(0);  // 从累加和0开始
    }

    /**
     * 递归计算函数（方法2 - 通用版本）
     * 
     * 算法思路：
     * 将原始问题推广到任意参数N, a, b
     * 
     * 边界条件：
     * - 如果 cur >= a 且 cur < b，获胜，概率为1.0
     * - 如果 cur >= b，失败，概率为0.0
     * - 否则必须继续抽牌
     * 
     * 递推关系：
     * P(cur) = (P(cur+1) + P(cur+2) + ... + P(cur+N)) / N
     * 
     * @param cur 当前累加和
     * @param n 卡牌面值范围[1,N]
     * @param a 获胜区间下界
     * @param b 获胜区间上界（失败区间下界）
     * @return 从当前状态开始的获胜概率
     */
    private static double p2(int cur, int n, int a, int b) {
        if (cur >= a && cur < b) {
            return 1.0;  // 获胜区间
        }
        if (cur >= b) {
            return 0.0;  // 失败区间
        }
        
        // 必须继续抽牌
        double w = 0.0;
        for (int i = 1; i <= n; i++) {
            w += p2(cur + i, n, a, b);
        }
        return w / n;
    }

    /**
     * 方法2：通用递归法
     * 
     * 算法特点：
     * - 适用于任意参数组合
     * - 直观易理解的递归实现
     * - 包含必要的边界检查
     * 
     * 边界情况处理：
     * - 如果b-a >= n，必然获胜（抽任何牌都能进入获胜区间）
     * - 如果参数不合法，返回0
     * 
     * 时间复杂度：O(N^a)，指数级
     * 空间复杂度：O(a)，递归调用栈
     * 
     * @param n 卡牌面值范围[1,N]
     * @param a 获胜区间下界
     * @param b 获胜区间上界
     * @return 获胜概率
     */
    public static double rate2(int n, int a, int b) {
        // 参数合法性检查
        if (n < 1 || a >= b || a < 0 || b < 0) {
            return 0.0;
        }
        
        // 特殊情况：获胜区间长度 >= 卡牌范围，必然获胜
        if (b - a >= n) {
            return 1.0;
        }
        
        return p2(0, n, a, b);
    }

    /**
     * 递归计算函数（方法3 - 数学公式优化版本）
     * 
     * 算法思路：
     * 利用递推关系的数学性质进行优化：
     * p(i) = (p(i+1) + p(i+2) + ... + p(i+n)) / n
     * p(i+1) = (p(i+2) + p(i+3) + ... + p(i+1+n)) / n
     * 
     * 推导出优化公式：
     * p(i) = (p(i+1) + p(i+1)*n - p(i+1+n)) / n
     * p(i) = p(i+1) * (n+1)/n - p(i+1+n)/n
     * 
     * 特殊处理：
     * - 当i = a-1时，公式失效，需要特殊计算
     * - p(a-1) = (b-a)/n （因为抽到[1, b-a]都获胜）
     * 
     * @param cur 当前累加和
     * @param n 卡牌面值范围[1,N]
     * @param a 获胜区间下界
     * @param b 获胜区间上界
     * @return 从当前状态开始的获胜概率
     */
    private static double p3(int cur, int n, int a, int b) {
        if (cur >= a && cur < b) {
            return 1.0;  // 获胜区间
        }
        if (cur >= b) {
            return 0.0;  // 失败区间
        }
        
        // 特殊情况：在a-1位置，直接计算概率
        if (cur == a - 1) {
            return 1.0 * (b - a) / n;
        }
        
        // 使用优化的递推公式
        double w = p3(cur + 1, n, a, b) + p3(cur + 1, n, a, b) * n;
        
        // 如果cur+1+n < b，需要减去超出范围的部分
        if (cur + 1 + n < b) {
            w -= p3(cur + 1 + n, n, a, b);
        }
        
        return w / n;
    }

    /**
     * 方法3：数学公式优化法
     * 
     * 算法特点：
     * - 利用递推关系的数学性质
     * - 显著减少递归调用次数
     * - 仍然是递归实现，但效率更高
     * 
     * 数学原理：
     * 通过递推关系的变形，避免重复计算
     * 将O(N)的递归调用优化为O(1)的公式计算
     * 
     * 时间复杂度：O(a)
     * 空间复杂度：O(a)，递归调用栈
     * 
     * @param n 卡牌面值范围[1,N]
     * @param a 获胜区间下界
     * @param b 获胜区间上界
     * @return 获胜概率
     */
    public static double rate3(int n, int a, int b) {
        if (n < 1 || a >= b || a < 0 || b < 0) {
            return 0.0;
        }
        if (b - a >= n) {
            return 1.0;
        }
        return p3(0, n, a, b);
    }

    /**
     * 方法4：动态规划法
     * 
     * 算法思路：
     * 使用自底向上的动态规划，避免递归的开销
     * dp[i] 表示从累加和i开始的获胜概率
     * 
     * 状态转移：
     * dp[i] = (dp[i+1] + dp[i+1]*n - dp[i+1+n]) / n
     * 
     * 边界条件：
     * - dp[i] = 1.0, 当 a <= i < b
     * - dp[i] = 0.0, 当 i >= b
     * - dp[a-1] = (b-a)/n （特殊情况）
     * 
     * 优化点：
     * - 消除递归调用栈开销
     * - 更好的缓存局部性
     * - 便于进一步优化（如滚动数组）
     * 
     * 时间复杂度：O(a)
     * 空间复杂度：O(b)
     * 
     * @param n 卡牌面值范围[1,N]
     * @param a 获胜区间下界
     * @param b 获胜区间上界
     * @return 获胜概率
     */
    public static double rate4(int n, int a, int b) {
        if (n < 1 || a >= b || a < 0 || b < 0) {
            return 0.0;
        }
        if (b - a >= n) {
            return 1.0;
        }
        
        // dp[i] 表示从累加和i开始的获胜概率
        double[] dp = new double[b];
        
        // 初始化获胜区间的概率为1.0
        for (int i = a; i < b; i++) {
            dp[i] = 1.0;
        }
        
        // 特殊情况：a-1位置的概率
        if (a - 1 >= 0) {
            dp[a - 1] = 1.0 * (b - a) / n;
        }
        
        // 自底向上填表
        for (int cur = a - 2; cur >= 0; cur--) {
            double w = dp[cur + 1] + dp[cur + 1] * n;
            
            // 如果超出范围，需要减去
            if (cur + 1 + n < b) {
                w -= dp[cur + 1 + n];
            }
            
            dp[cur] = w / n;
        }
        
        return dp[0];
    }

    /**
     * 测试方法：验证四种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== N卡牌获胜概率问题测试 ===");
        
        // 经典21点问题测试
        int n = 10, a = 17, b = 21;
        System.out.println("1. 经典21点问题 (N=10, a=17, b=21):");
        
        long start = System.currentTimeMillis();
        double result1 = rate1();
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        double result2 = rate2(n, a, b);
        long time2 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        double result3 = rate3(n, a, b);
        long time3 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        double result4 = rate4(n, a, b);
        long time4 = System.currentTimeMillis() - start;
        
        System.out.println("方法1（简单递归）: " + result1 + ", 耗时: " + time1 + "ms");
        System.out.println("方法2（通用递归）: " + result2 + ", 耗时: " + time2 + "ms");
        System.out.println("方法3（公式优化）: " + result3 + ", 耗时: " + time3 + "ms");
        System.out.println("方法4（动态规划）: " + result4 + ", 耗时: " + time4 + "ms");
        System.out.println();
        
        // 大规模随机测试验证算法正确性
        System.out.println("2. 随机测试验证算法正确性:");
        int times = 100000;
        int maxN = 15, maxM = 20;
        DecimalFormat df = new DecimalFormat("#.####");
        
        System.out.println("测试次数: " + times);
        System.out.println("参数范围: N≤" + maxN + ", a,b≤" + maxM);
        System.out.println("开始测试...");
        
        boolean allCorrect = true;
        int testCount = 0;
        
        for (int i = 0; i < times; i++) {
            n = (int) (Math.random() * maxN) + 1;  // 确保n >= 1
            a = (int) (Math.random() * maxM);
            b = (int) (Math.random() * maxM);
            
            // 跳过无效参数
            if (a >= b) continue;
            
            testCount++;
            
            double ans2 = Double.valueOf(df.format(rate2(n, a, b)));
            double ans3 = Double.valueOf(df.format(rate3(n, a, b)));
            double ans4 = Double.valueOf(df.format(rate4(n, a, b)));
            
            if (ans2 != ans3 || ans2 != ans4) {
                System.out.println("发现错误！");
                System.out.println("参数: n=" + n + ", a=" + a + ", b=" + b);
                System.out.println("方法2: " + ans2);
                System.out.println("方法3: " + ans3);
                System.out.println("方法4: " + ans4);
                allCorrect = false;
                break;
            }
            
            // 显示进度
            if (testCount % 20000 == 0) {
                System.out.println("已测试 " + testCount + " 个有效用例...");
            }
        }
        
        if (allCorrect) {
            System.out.println("✓ 所有测试通过，算法正确！");
        } else {
            System.out.println("✗ 发现算法错误！");
        }
        
        System.out.println("有效测试用例数: " + testCount);
        
        // 边界情况测试
        System.out.println("\n3. 边界情况测试:");
        System.out.println("b-a >= n的情况 (必胜): " + rate4(5, 10, 16) + " (期望: 1.0)");
        System.out.println("a >= b的情况 (无效): " + rate4(5, 15, 10) + " (期望: 0.0)");
        System.out.println("n=1的情况: " + rate4(1, 5, 7) + " (应该是具体值)");
        
        System.out.println("\n=== 测试完成 ===");
    }
}
