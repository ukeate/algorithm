package base.rand;

/**
 * 不等概率随机函数转等概率随机函数
 * 
 * 经典问题：
 * 给定一个不等概率的随机函数notFair()，它返回0的概率为p，返回1的概率为1-p（p ≠ 0.5）
 * 要求实现一个等概率的随机函数rand01()，使得返回0和1的概率都为0.5
 * 
 * 算法核心思想：
 * 使用"拒绝采样"(Rejection Sampling)技术
 * 1. 连续调用两次notFair()函数
 * 2. 如果两次结果相同（00或11），则丢弃结果重新生成
 * 3. 如果两次结果不同（01或10），则取第一次的结果
 * 
 * 数学原理：
 * - P(0, 1) = p × (1-p)     // 第一次0，第二次1的概率
 * - P(1, 0) = (1-p) × p     // 第一次1，第二次0的概率
 * - 显然 P(0, 1) = P(1, 0)，所以通过这种方式可以得到等概率的0和1
 * 
 * 算法特点：
 * - 时间复杂度：期望O(1)，最坏情况下可能需要多次重试
 * - 空间复杂度：O(1)
 * - 适用于任何不等概率的二值随机函数
 * 
 * 实际应用：
 * - 硬件随机数生成器的偏差校正
 * - 蒙特卡罗算法中的采样技术
 * - 概率算法的基础工具
 */
public class Rand01 {
    /**
     * 模拟的不等概率随机函数
     * 
     * 这里模拟一个返回0的概率为0.8，返回1的概率为0.2的偏向函数
     * 在实际应用中，这个函数可能是硬件提供的、或者其他算法产生的有偏随机源
     * 
     * @return 0（概率0.8）或1（概率0.2）
     */
    private static int notFair() {
        return Math.random() > 0.8 ? 0 : 1;
    }

    /**
     * 等概率随机函数 - 从不等概率函数生成等概率的0和1
     * 
     * 算法步骤：
     * 1. 循环调用notFair()函数两次
     * 2. 如果两次结果相同，继续循环（拒绝采样）
     * 3. 如果两次结果不同，返回第一次的结果
     * 
     * 为什么这样做？
     * - 假设notFair()返回0的概率为p，返回1的概率为1-p
     * - P(先0后1) = p × (1-p)
     * - P(先1后0) = (1-p) × p
     * - 这两个概率相等！
     * - 在两次结果不同的情况下，"先0后1"和"先1后0"的概率相等
     * - 因此我们可以用第一次的结果作为等概率的随机数
     * 
     * 时间复杂度分析：
     * - 两次结果相同的概率 = p² + (1-p)² 
     * - 两次结果不同的概率 = 2p(1-p)
     * - 期望重试次数 = 1 / [2p(1-p)]
     * - 当p接近0.5时，期望重试次数接近1（最优）
     * - 当p接近0或1时，期望重试次数会增加
     * 
     * @return 0或1，各自概率为0.5
     */
    public static int rand01() {
        int ans = 0;
        do {
            ans = notFair();                    // 第一次调用
        } while (ans == notFair());             // 如果两次结果相同，继续循环
        return ans;                             // 返回第一次的结果
    }

    /**
     * 测试方法 - 验证生成的随机数是否接近等概率分布
     */
    public static void main(String[] args) {
        System.out.println("=== 不等概率转等概率随机数测试 ===");
        
        // 测试原始的不等概率函数
        System.out.println("1. 原始不等概率函数测试（10000次）:");
        int[] notFairCount = new int[2];
        for (int i = 0; i < 10000; i++) {
            notFairCount[notFair()]++;
        }
        System.out.println("不等概率函数 - 0的次数: " + notFairCount[0] + 
                         ", 1的次数: " + notFairCount[1]);
        System.out.println("比例约为 4:1 (0.8:0.2)");
        
        // 测试转换后的等概率函数
        System.out.println("\n2. 转换后的等概率函数测试（10000次）:");
        int[] fairCount = new int[2];
        for (int i = 0; i < 10000; i++) {
            fairCount[rand01()]++;
        }
        System.out.println("等概率函数 - 0的次数: " + fairCount[0] + 
                         ", 1的次数: " + fairCount[1]);
        System.out.println("比例接近 1:1 (0.5:0.5)");
        
        // 计算实际概率
        double prob0 = (double) fairCount[0] / 10000;
        double prob1 = (double) fairCount[1] / 10000;
        System.out.println("\n3. 概率分析:");
        System.out.println("P(0) ≈ " + String.format("%.3f", prob0));
        System.out.println("P(1) ≈ " + String.format("%.3f", prob1));
        System.out.println("理论值: P(0) = P(1) = 0.5");
        
        // 计算偏差
        double deviation = Math.abs(prob0 - 0.5);
        System.out.println("与理论值的偏差: " + String.format("%.3f", deviation));
        if (deviation < 0.02) {
            System.out.println("✓ 测试通过：偏差在可接受范围内");
        } else {
            System.out.println("⚠ 注意：偏差较大，可能需要更多测试样本");
        }
    }
}
