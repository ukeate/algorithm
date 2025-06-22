package base.math;

/**
 * 蓄水池抽样算法（Reservoir Sampling）
 * 
 * 问题背景：
 * 从N个元素的数据流中随机抽取K个元素，其中N未知或很大
 * 要求每个元素被选中的概率相等
 * 
 * 算法原理：
 * 1. 前K个元素直接放入蓄水池
 * 2. 对于第i个元素（i > K）：
 *    - 以K/i的概率决定是否放入蓄水池
 *    - 如果放入，则随机替换蓄水池中的一个元素
 * 
 * 数学证明：
 * 每个元素最终被保留的概率都是K/N，保证了随机性
 * 
 * 应用场景：
 * - 大数据流处理中的随机采样
 * - 内存受限的随机抽样
 * - 在线算法中的等概率抽样
 */
// 蓄水池, 在线等概率抽样
// bagN/allN概率进袋, 袋内等概率替代
public class Reservoir {
    /**
     * 蓄水池抽样器实现
     */
    public static class RandomBox {
        private int[] bag;      // 蓄水池数组
        private int n;          // 蓄水池容量
        private int count;      // 已处理的元素数量

        /**
         * 构造函数
         * @param capacity 蓄水池容量
         */
        public RandomBox(int capacity) {
            bag = new int[capacity];
            n = capacity;
            count = 0;
        }

        /**
         * 生成1到max之间的随机数（包含max）
         * @param max 随机数上界
         * @return 1到max之间的随机整数
         */
        private int rand(int max) {
            return (int) (max * Math.random()) + 1;
        }

        /**
         * 向蓄水池中添加一个元素
         * 
         * 算法步骤：
         * 1. 如果蓄水池未满，直接添加
         * 2. 如果蓄水池已满：
         *    - 以n/count的概率决定是否替换
         *    - 如果替换，随机选择一个位置进行替换
         * 
         * @param num 要添加的元素
         */
        public void add(int num) {
            count++;
            if (count <= n) {
                // 蓄水池未满，直接添加
                bag[count - 1] = num;
            } else {
                // 蓄水池已满，按概率决定是否替换
                if (rand(count) <= n) {
                    // 决定替换，随机选择替换位置
                    bag[rand(n) - 1] = num;
                }
            }
        }

        /**
         * 获取当前蓄水池中的所有元素
         * @return 蓄水池中的元素数组
         */
        public int[] choices() {
            int[] ans = new int[n];
            for (int i = 0; i < n; i++) {
                ans[i] = bag[i];
            }
            return ans;
        }
    }

    /**
     * 生成1到i之间的随机数（包含i）
     * @param i 随机数上界
     * @return 1到i之间的随机整数
     */
    private static int random(int i) {
        return (int) (Math.random() * i) + 1;
    }

    /**
     * 测试方法 - 验证蓄水池抽样的随机性
     * 
     * 测试思路：
     * 1. 多次执行蓄水池抽样
     * 2. 统计每个元素被选中的次数
     * 3. 理论上每个元素被选中的次数应该大致相等
     */
    public static void main(String[] args) {
        int times = 100000;     // 测试次数
        int allN = 155;         // 总元素数量
        int bagN = 10;          // 蓄水池容量
        int[] counts = new int[allN + 1];   // 统计每个元素被选中的次数
        
        // 方法1：直接实现蓄水池抽样逻辑
        for (int i = 0; i < times; i++) {
            int[] bag = new int[bagN];
            int idx = 0;
            for (int num = 1; num <= allN; num++) {
                if (num <= 10) {
                    // 前10个元素直接放入蓄水池
                    bag[idx++] = num;
                } else {
                    // 后续元素按概率决定是否替换
                    if (random(num) <= bagN) {
                        idx = (int) (Math.random() * bagN);
                        bag[idx] = num;
                    }
                }
            }
            // 统计被选中的元素
            for (int num : bag) {
                counts[num]++;
            }
        }
        
        // 输出统计结果
        for (int i = 0; i <= allN; i++) {
            System.out.println(counts[i]);
        }

        System.out.println("====");
        
        // 方法2：使用封装的RandomBox类
        counts = new int[allN + 1];
        for (int i = 0; i < times; i++) {
            RandomBox box = new RandomBox(bagN);
            for (int num = 1; num <= allN; num++) {
                box.add(num);
            }
            int[] ans = box.choices();
            for (int j = 0; j < ans.length; j++) {
                counts[ans[j]]++;
            }
        }
        
        // 输出统计结果
        for (int i = 0; i < counts.length; i++) {
            System.out.println(counts[i]);
        }
    }
}
