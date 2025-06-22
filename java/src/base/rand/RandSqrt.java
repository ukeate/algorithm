package base.rand;

/**
 * 平方根分布随机数生成器
 * 
 * 问题描述：
 * 如何生成一个随机数，使其分布遵循f(x) = 2x，其中x ∈ [0,1]
 * 即概率密度函数为f(x) = 2x的随机变量
 * 
 * 数学背景：
 * 1. 标准均匀分布U(0,1)的概率密度函数为f(x) = 1
 * 2. 我们需要的分布的概率密度函数为f(x) = 2x
 * 3. 这意味着越接近1的值出现的概率越大，越接近0的值出现的概率越小
 * 
 * 累积分布函数(CDF)分析：
 * - 对f(x) = 2x进行积分：F(x) = ∫[0,x] 2t dt = x²
 * - 所以CDF为F(x) = x²
 * - 逆函数为F⁻¹(y) = √y
 * 
 * 逆变换采样法(Inverse Transform Sampling)：
 * 1. 生成均匀分布的随机数U ~ U(0,1)
 * 2. 计算X = F⁻¹(U) = √U
 * 3. 则X遵循所需的概率分布
 * 
 * 但本算法使用了更巧妙的方法：
 * 使用两个独立的均匀随机数，取其最大值
 * 
 * 数学原理：
 * 设U₁, U₂ ~ U(0,1)，X = max(U₁, U₂)
 * 则P(X ≤ x) = P(U₁ ≤ x, U₂ ≤ x) = P(U₁ ≤ x) × P(U₂ ≤ x) = x²
 * 所以X的CDF为F(x) = x²，概率密度函数为f(x) = 2x
 * 
 * 算法优势：
 * 1. 实现简单，只需要两次均匀随机数调用
 * 2. 计算效率高，避免了开方运算
 * 3. 数学原理清晰，易于理解和验证
 * 
 * 应用场景：
 * - 模拟某些自然现象中偏向较大值的随机过程
 * - 蒙特卡罗方法中的重要性采样
 * - 机器学习中的某些概率分布建模
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
public class RandSqrt {
    /**
     * 生成遵循f(x) = 2x分布的随机数
     * 
     * 算法实现：
     * 使用两个独立的均匀随机数，返回它们的最大值
     * 
     * 数学验证：
     * 设返回值为X，我们需要验证X的概率密度函数确实是f(x) = 2x
     * 
     * 证明过程：
     * 1. 设U₁, U₂为两个独立的U(0,1)随机变量
     * 2. X = max(U₁, U₂)
     * 3. CDF: F_X(x) = P(X ≤ x) = P(max(U₁, U₂) ≤ x)
     * 4. = P(U₁ ≤ x AND U₂ ≤ x)
     * 5. = P(U₁ ≤ x) × P(U₂ ≤ x)  (独立性)
     * 6. = x × x = x²
     * 7. PDF: f_X(x) = d/dx[F_X(x)] = d/dx[x²] = 2x
     * 
     * 期望值计算：
     * E[X] = ∫[0,1] x × 2x dx = ∫[0,1] 2x² dx = [2x³/3]₀¹ = 2/3
     * 
     * 方差计算：
     * E[X²] = ∫[0,1] x² × 2x dx = ∫[0,1] 2x³ dx = [x⁴/2]₀¹ = 1/2
     * Var[X] = E[X²] - (E[X])² = 1/2 - (2/3)² = 1/2 - 4/9 = 1/18
     * 
     * @return [0,1]范围内的随机数，遵循f(x) = 2x分布
     */
    public static double randomSqrt() {
        return Math.max(Math.random(), Math.random());
    }

    /**
     * 测试方法 - 验证生成的随机数是否遵循预期分布
     */
    public static void main(String[] args) {
        System.out.println("=== 平方根分布随机数测试 ===");
        
        int sampleSize = 100000;
        double[] samples = new double[sampleSize];
        
        // 生成样本
        for (int i = 0; i < sampleSize; i++) {
            samples[i] = randomSqrt();
        }
        
        // 计算样本统计量
        double sum = 0;
        double sumSquare = 0;
        for (double sample : samples) {
            sum += sample;
            sumSquare += sample * sample;
        }
        
        double sampleMean = sum / sampleSize;
        double sampleVar = sumSquare / sampleSize - sampleMean * sampleMean;
        
        System.out.println("样本统计量（基于" + sampleSize + "个样本）:");
        System.out.println("样本均值: " + String.format("%.4f", sampleMean));
        System.out.println("理论均值: " + String.format("%.4f", 2.0/3.0));
        System.out.println("均值偏差: " + String.format("%.4f", Math.abs(sampleMean - 2.0/3.0)));
        
        System.out.println("\n样本方差: " + String.format("%.6f", sampleVar));
        System.out.println("理论方差: " + String.format("%.6f", 1.0/18.0));
        System.out.println("方差偏差: " + String.format("%.6f", Math.abs(sampleVar - 1.0/18.0)));
        
        // 分布验证 - 将[0,1]区间分为10个子区间
        System.out.println("\n分布验证（区间频率统计）:");
        int[] bins = new int[10];
        for (double sample : samples) {
            int binIndex = Math.min((int)(sample * 10), 9);
            bins[binIndex]++;
        }
        
        System.out.println("区间\t\t观察频率\t理论频率\t偏差");
        for (int i = 0; i < 10; i++) {
            double left = i / 10.0;
            double right = (i + 1) / 10.0;
            double observedFreq = (double) bins[i] / sampleSize;
            
            // 理论频率 = ∫[left,right] 2x dx = [x²]_left^right = right² - left²
            double theoreticalFreq = right * right - left * left;
            double deviation = Math.abs(observedFreq - theoreticalFreq);
            
            System.out.printf("[%.1f,%.1f)\t%.4f\t\t%.4f\t\t%.4f\n", 
                left, right, observedFreq, theoreticalFreq, deviation);
        }
        
        // 整体评估
        double totalDeviation = 0;
        for (int i = 0; i < 10; i++) {
            double left = i / 10.0;
            double right = (i + 1) / 10.0;
            double observedFreq = (double) bins[i] / sampleSize;
            double theoreticalFreq = right * right - left * left;
            totalDeviation += Math.abs(observedFreq - theoreticalFreq);
        }
        double avgDeviation = totalDeviation / 10;
        
        System.out.println("\n平均频率偏差: " + String.format("%.4f", avgDeviation));
        if (avgDeviation < 0.005) {
            System.out.println("✓ 测试通过：分布与理论分布高度吻合");
        } else if (avgDeviation < 0.01) {
            System.out.println("✓ 测试通过：分布与理论分布基本吻合");
        } else {
            System.out.println("⚠ 注意：可能需要更多样本来验证分布");
        }
        
        // 展示几个生成的随机数示例
        System.out.println("\n随机数示例:");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%.4f ", randomSqrt());
        }
        System.out.println("\n注意：这些数更倾向于接近1的值");
    }
}
