package base.rand;

/**
 * 随机数生成器工具类 - 在指定范围内生成均匀分布的随机数
 * 
 * 核心问题：
 * 给定一个能够生成[min, max]范围内随机数的生成器，如何：
 * 1. 生成均匀分布的0-1随机数
 * 2. 生成任意范围[from, to]内的随机数
 * 
 * 算法原理：
 * 1. 利用拒绝采样(Rejection Sampling)确保概率均匀
 * 2. 使用二进制位组合生成目标范围的随机数
 * 3. 通过数学变换保证输出的均匀性
 * 
 * 应用场景：
 * - 当系统提供的随机数生成器范围不符合需求时
 * - 需要从一个已知分布生成另一个分布的随机数时
 * - 算法竞赛中的随机数生成问题
 */
public class Box {
    /**
     * 随机数生成器类 - 封装特定范围的随机数生成逻辑
     */
    private static class RandBox {
        private final int min;    // 随机数生成的最小值
        private final int max;    // 随机数生成的最大值

        /**
         * 构造函数 - 初始化随机数生成范围
         * 
         * @param min 最小值（包含）
         * @param max 最大值（包含）
         */
        public RandBox(int min, int max) {
            this.min = min;
            this.max = max;
        }

        /**
         * 生成[min, max]范围内的随机整数
         * 
         * 使用Java内置的Math.random()方法：
         * - Math.random()返回[0.0, 1.0)范围内的double值
         * - 通过数学变换映射到目标整数范围
         * 
         * @return [min, max]范围内的随机整数
         */
        public int rand() {
            return min + (int) (Math.random() * (max - min + 1));
        }

        /**
         * 生成均匀分布的0-1随机数
         * 
         * 算法思路：
         * 1. 计算当前范围的大小：size = max - min + 1
         * 2. 判断size是否为奇数
         * 3. 如果是奇数，需要排除中间值以保证0和1的概率相等
         * 4. 使用拒绝采样，重新生成直到不是中间值
         * 5. 根据值与中点的关系返回0或1
         * 
         * 核心思想：
         * 为了保证0和1的概率各为50%，当范围大小为奇数时，
         * 需要排除中间的那个数，只使用剩下的偶数个数来分配给0和1
         * 
         * @return 0或1的随机值
         */
        public int rand01() {
            int size = this.max - this.min + 1;
            boolean odd = (size & 1) != 0;     // 判断size是否为奇数
            int mid = size >> 1;               // 中点位置
            int ans = 0;
            
            do {
                ans = this.rand() - min;       // 获取相对于min的偏移量
            } while (odd && ans == mid);       // 如果是奇数范围，排除中间值
            
            return ans < mid ? 0 : 1;          // 小于中点返回0，否则返回1
        }

        /**
         * 生成指定范围[from, to]内的随机数
         * 
         * 算法思路：
         * 1. 计算目标范围大小：range = to - from
         * 2. 确定需要多少个二进制位来表示这个范围
         * 3. 使用rand01()生成足够的随机位
         * 4. 将这些位组合成一个数字
         * 5. 如果生成的数字超过范围，重新生成
         * 6. 最终结果加上起始值from
         * 
         * 核心思想：
         * 通过二进制位的组合来生成随机数，确保在目标范围内均匀分布
         * 使用拒绝采样处理范围不是2的幂次的情况
         * 
         * @param from 目标范围的起始值（包含）
         * @param to 目标范围的结束值（包含）
         * @return [from, to]范围内的随机整数
         */
        public int rand(int from, int to) {
            if (from == to) {
                return from;                   // 范围只有一个数，直接返回
            }
            
            int range = to - from;             // 目标范围大小
            int num = 1;                       // 需要的二进制位数
            
            // 计算需要多少位来表示range
            // 找到最小的num使得(2^num - 1) >= range
            while ((1 << num) - 1 < range) {
                num++;
            }
            
            int ans = 0;
            do {
                ans = 0;
                // 生成num位的随机二进制数
                for (int i = 0; i < num; i++) {
                    ans |= (this.rand01() << i);   // 在第i位设置随机位
                }
            } while (ans > range);             // 超出范围则重新生成
            
            return ans + from;                 // 加上偏移量得到最终结果
        }
    }

    /**
     * 测试方法 - 演示随机数生成器的使用
     */
    public static void main(String[] args) {
        // 创建一个[3, 9]范围的随机数生成器
        RandBox box = new RandBox(3, 9);
        
        System.out.println("=== 随机数生成器测试 ===");
        
        // 测试基本随机数生成
        System.out.println("1. 基本随机数生成 [3, 9]:");
        for (int i = 0; i < 10; i++) {
            System.out.print(box.rand() + " ");
        }
        System.out.println();
        
        // 测试0-1随机数生成
        System.out.println("\n2. 0-1随机数生成:");
        int[] count = new int[2];
        for (int i = 0; i < 1000; i++) {
            count[box.rand01()]++;
        }
        System.out.println("0的次数: " + count[0] + ", 1的次数: " + count[1]);
        System.out.println("比例接近1:1，验证了均匀性");
        
        // 测试指定范围随机数生成
        System.out.println("\n3. 指定范围随机数生成 [10, 20]:");
        for (int i = 0; i < 10; i++) {
            System.out.print(box.rand(10, 20) + " ");
        }
        System.out.println();
        
        // 测试边界情况
        System.out.println("\n4. 边界情况测试:");
        System.out.println("相同范围 [5, 5]: " + box.rand(5, 5));
        System.out.println("小范围 [1, 2]: " + box.rand(1, 2));
    }
}
