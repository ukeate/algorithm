package base.rand;

/**
 * 随机数范围转换：从Rand5到Rand7
 * 
 * 经典算法问题：
 * 给定一个能够等概率生成[1,5]范围随机数的函数gen5()，
 * 要求实现一个能够等概率生成[1,7]范围随机数的函数genTo7()
 * 
 * 问题分析：
 * 1. 不能直接使用gen5() % 7 + 1，因为这会导致概率不均匀
 * 2. 需要利用gen5()构造一个更大的等概率随机数池
 * 3. 然后从这个池中等概率地选择[1,7]范围的数
 * 
 * 解决方案：
 * 1. 先将gen5()转换为gen1()（等概率生成0或1）
 * 2. 用gen1()构造二进制随机数
 * 3. 将二进制随机数映射到[1,7]范围
 * 
 * 算法步骤：
 * 1. gen5() → gen1()：利用拒绝采样技术
 * 2. gen1() → 3位二进制数：能表示0-7，但我们只需要1-7
 * 3. 拒绝采样：如果生成的是0，重新生成
 * 
 * 时间复杂度：期望O(1)，最坏情况可能需要多次重试
 * 空间复杂度：O(1)
 * 
 * 扩展应用：
 * 这种技术可以推广到任意范围的随机数转换问题
 */
public class Rand5to7 {
    /**
     * 模拟等概率生成[1,5]范围随机数的函数
     * 
     * 在实际应用中，这个函数通常是题目给定的，
     * 我们需要基于这个函数来构造目标范围的随机数生成器
     * 
     * @return [1,5]范围内的随机整数，每个数概率为1/5
     */
    private static int gen5() {
        return (int) (Math.random() * 5) + 1;
    }

    /**
     * 从gen5()生成等概率的0-1随机数
     * 
     * 算法思路：
     * 1. gen5()生成1,2,3,4,5，每个概率为1/5
     * 2. 我们需要将其转换为0,1，每个概率为1/2
     * 3. 使用拒绝采样：
     *    - 如果gen5()返回3，丢弃重新生成（保持对称性）
     *    - 如果gen5()返回1或2，映射为0
     *    - 如果gen5()返回4或5，映射为1
     * 
     * 为什么排除3？
     * - 1,2,3,4,5中，如果包含3，无法等分为两组
     * - 排除3后，{1,2} vs {4,5}，两组大小相等
     * - 这样就能保证0和1的概率都是1/2
     * 
     * 时间复杂度：期望O(5/4) = O(1.25)
     * - 生成非3的概率 = 4/5
     * - 期望尝试次数 = 1/(4/5) = 5/4
     * 
     * @return 0或1，各自概率为1/2
     */
    private static int gen1() {
        int ans = 0;
        do {
            ans = gen5();                       // 生成1-5的随机数
        } while (ans == 3);                     // 如果是3，重新生成
        return ans < 3 ? 0 : 1;                 // 1,2映射为0；4,5映射为1
    }

    /**
     * 使用gen5()等概率生成[1,7]范围的随机数
     * 
     * 算法思路：
     * 1. 使用gen1()生成3位二进制数，可以表示0-7
     * 2. 如果生成的是7，将其映射为0
     * 3. 如果生成的是0，重新生成（因为我们需要1-7）
     * 4. 最终结果+1得到[1,7]范围
     * 
     * 具体实现：
     * - 使用3个gen1()调用生成3位二进制数
     * - 二进制位从低到高：gen1() << 0, gen1() << 1, gen1() << 2
     * - 生成的数值范围是0-7
     * - 拒绝采样：如果是7，重新生成
     * - 结果+1映射到[1,7]
     * 
     * 为什么这样做？
     * - 3位二进制数有8种可能：000,001,010,011,100,101,110,111
     * - 对应十进制：0,1,2,3,4,5,6,7
     * - 我们只需要1-7，所以排除7，重新生成
     * - 这样0-6都有相等的概率，加1后得到1-7
     * 
     * 时间复杂度：期望O(8/7) ≈ O(1.14)
     * - 生成非7的概率 = 7/8
     * - 期望尝试次数 = 1/(7/8) = 8/7
     * 
     * @return [1,7]范围内的随机整数，每个数概率为1/7
     */
    public static int genTo7() {
        int ans = 0;
        do {
            // 生成3位二进制随机数
            // 第0位：gen1() << 0 = gen1()
            // 第1位：gen1() << 1 = gen1() * 2
            // 第2位：gen1() << 2 = gen1() * 4
            ans = (gen1() << 2) + (gen1() << 1) + gen1();
        } while (ans == 7);                     // 如果生成的是7，重新生成
        return ans + 1;                         // 加1映射到[1,7]范围
    }

    /**
     * 测试方法 - 验证生成的随机数是否均匀分布
     */
    public static void main(String[] args) {
        System.out.println("=== Rand5转Rand7算法测试 ===");
        
        // 测试原始的gen5函数
        System.out.println("1. 原始gen5函数测试（10000次）:");
        int[] count5 = new int[6]; // 索引0不用，1-5对应实际值
        for (int i = 0; i < 10000; i++) {
            count5[gen5()]++;
        }
        System.out.println("gen5分布:");
        for (int i = 1; i <= 5; i++) {
            System.out.println("  " + i + ": " + count5[i] + " 次");
        }
        
        // 测试转换后的genTo7函数
        System.out.println("\n2. 转换后的genTo7函数测试（10000次）:");
        int[] count7 = new int[8]; // 索引0不用，1-7对应实际值
        for (int i = 0; i < 10000; i++) {
            count7[genTo7()]++;
        }
        System.out.println("genTo7分布:");
        for (int i = 1; i <= 7; i++) {
            System.out.println("  " + i + ": " + count7[i] + " 次");
        }
        
        // 计算均匀性
        System.out.println("\n3. 均匀性分析:");
        double expected = 10000.0 / 7; // 期望每个数出现的次数
        System.out.println("期望每个数出现次数: " + String.format("%.1f", expected));
        
        double maxDeviation = 0;
        for (int i = 1; i <= 7; i++) {
            double deviation = Math.abs(count7[i] - expected);
            maxDeviation = Math.max(maxDeviation, deviation);
        }
        
        System.out.println("最大偏差: " + String.format("%.1f", maxDeviation));
        double deviationRate = maxDeviation / expected;
        System.out.println("偏差率: " + String.format("%.2f%%", deviationRate * 100));
        
        if (deviationRate < 0.05) {
            System.out.println("✓ 测试通过：分布均匀度良好");
        } else {
            System.out.println("⚠ 注意：可能需要更多测试样本来验证均匀性");
        }
    }
}
