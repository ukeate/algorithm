package giant.c38;

/**
 * 填补差距最小步数问题
 * 
 * 问题描述：
 * 给定两个数a和b，进行如下操作：
 * - 第1轮，把1选择给a或者b
 * - 第2轮，把2选择给a或者b
 * - ...
 * - 第i轮，把i选择给a或者b
 * 
 * 问题目标：
 * 想让a和b的值一样大，请问至少需要多少轮？
 * 
 * 解题思路：
 * 这是一个经典的数学问题，有三种解法：
 * 1. 暴力递归：枚举所有可能的选择
 * 2. 数学分析：通过等差数列和奇偶性分析
 * 3. 二分搜索优化：快速定位最小步数
 * 
 * 数学核心：
 * 设差值为s = |a - b|，经过n轮后，数字1到n的总和为sum = n*(n+1)/2
 * 要使a和b相等，需要满足：sum - s为偶数且sum >= s
 * 这是因为我们需要将总和重新分配，使得两个数相等
 * 
 * 算法优势：
 * - 暴力方法：时间复杂度O(2^n)，适合小规模验证
 * - 数学方法：时间复杂度O(n)，适合中等规模
 * - 二分优化：时间复杂度O(log n)，适合大规模问题
 * 
 * 来源：字节跳动面试题
 * 
 * @author Zhu Runqi
 */
public class FillGapMinStep {
    
    /**
     * 暴力递归方法（用于验证正确性）
     * 枚举所有可能的选择路径，找到使a和b相等的最小轮数
     * 
     * @param a 当前第一个数的值
     * @param b 当前第二个数的值
     * @param i 当前轮数
     * @param n 最大轮数限制
     * @return 使a和b相等的最小轮数，找不到则返回MAX_VALUE
     */
    private static int process(int a, int b, int i, int n) {
        if (i > n) {
            return Integer.MAX_VALUE;  // 超出限制，无解
        }
        
        // 检查当前是否可以通过这一轮使a和b相等
        if (a + i == b || a == b + i) {
            return i;  // 找到解
        }
        
        // 递归尝试两种选择：将i给a或给b
        return Math.min(process(a + i, b, i + 1, n), process(a, b + i, i + 1, n));
    }

    /**
     * 暴力递归求解（限制搜索深度）
     * 
     * @param a 初始值a
     * @param b 初始值b  
     * @return 最小轮数
     */
    public static int minStep0(int a, int b) {
        if (a == b) {
            return 0;
        }
        
        int limit = 15;  // 限制搜索深度，避免超时
        return process(a, b, 1, limit);
    }

    /**
     * 数学分析方法（线性搜索）
     * 
     * 核心思想：
     * 1. 计算初始差值s = |a - b|
     * 2. 从1开始累加，直到sum >= s且(sum - s)为偶数
     * 3. 这样可以通过重新分配使两个数相等
     * 
     * 数学原理：
     * - 假设经过n轮，总和为sum = 1+2+...+n = n*(n+1)/2
     * - 要使a和b相等，需要重新分配这些数字
     * - 设分配给a的数字和为x，分配给b的数字和为y
     * - 则有：x + y = sum，且 a + x = b + y
     * - 解得：x - y = b - a，即 x = (sum + b - a)/2
     * - 要使x为整数，需要(sum + b - a)为偶数，即(sum - s)为偶数
     * 
     * @param a 初始值a
     * @param b 初始值b
     * @return 最小轮数
     */
    public static int minStep1(int a, int b) {
        if (a == b) {
            return 0;
        }
        
        int s = Math.abs(a - b);  // 计算初始差值
        int num = 1, sum = 0;
        
        // 寻找最小的num，使得sum >= s且(sum - s)为偶数
        for (; !(sum >= s && (sum - s) % 2 == 0); num++) {
            sum += num;
        }
        
        return num - 1;
    }

    /**
     * 二分搜索优化方法
     * 
     * 核心思想：
     * 1. 使用二分搜索快速定位满足sum >= s的最小n
     * 2. 然后调整奇偶性，最多需要增加3步
     * 
     * 优化原理：
     * - 不需要线性搜索，直接二分找到sum >= 2s的最小n
     * - 利用奇偶性规律：如果sum和s奇偶性不同，最多再增加3步
     * 
     * @param s2 2倍的差值（用于二分搜索的目标值）
     * @return 满足条件的最小n
     */
    private static int best(int s2) {
        int l = 0, r = 1;
        
        // 先通过倍增找到搜索范围
        for (; r * (r + 1) < s2;) {
            l = r;
            r *= 2;
        }
        
        int ans = 0;
        // 二分搜索找到最小的n，使得n*(n+1) >= s2
        while (l <= r) {
            int m = (l + r) / 2;
            if (m * (m + 1) >= s2) {
                ans = m;
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return ans;
    }

    /**
     * 二分搜索优化的数学方法
     * 
     * @param a 初始值a
     * @param b 初始值b
     * @return 最小轮数
     */
    public static int minStep2(int a, int b) {
        if (a == b) {
            return 0;
        }
        
        int s = Math.abs(a - b);
        int begin = best(s << 1);  // 找到满足sum >= 2s的最小n
        
        // 调整奇偶性，最多需要3步
        for (; (begin * (begin + 1) / 2 - s) % 2 != 0;) {
            begin++;
        }
        
        return begin;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 填补差距最小步数问题 ===\n");
        
        // 手动测试用例
        System.out.println("=== 手动测试用例 ===");
        
        // 测试用例1：简单情况
        System.out.println("测试用例1：a=1, b=4");
        int a1 = 1, b1 = 4;
        System.out.println("初始差值: " + Math.abs(a1 - b1));
        System.out.println("暴力方法: " + minStep0(a1, b1));
        System.out.println("数学方法: " + minStep1(a1, b1));
        System.out.println("二分方法: " + minStep2(a1, b1));
        System.out.println("分析: 差值为3，需要找到sum>=3且(sum-3)为偶数的最小sum");
        System.out.println("  第1轮sum=1: 1-3=-2<0，不满足");
        System.out.println("  第2轮sum=3: 3-3=0，是偶数，满足条件");
        System.out.println("  策略: 第1轮给b(1+1=2,4), 第2轮给a(1+2=3,4+0=4)");
        System.out.println();
        
        // 测试用例2：需要调整奇偶性
        System.out.println("测试用例2：a=2, b=5");
        int a2 = 2, b2 = 5;
        System.out.println("初始差值: " + Math.abs(a2 - b2));
        System.out.println("数学方法: " + minStep1(a2, b2));
        System.out.println("二分方法: " + minStep2(a2, b2));
        System.out.println("分析: 差值为3");
        System.out.println("  第1轮sum=1: 1-3=-2，不满足");
        System.out.println("  第2轮sum=3: 3-3=0，是偶数，满足条件");
        System.out.println();
        
        // 测试用例3：大数值情况
        System.out.println("测试用例3：大数值测试");
        int a3 = 19019, b3 = 8439284;
        System.out.println("a=" + a3 + ", b=" + b3);
        System.out.println("初始差值: " + Math.abs(a3 - b3));
        
        long start = System.currentTimeMillis();
        int result1 = minStep1(a3, b3);
        long time1 = System.currentTimeMillis() - start;
        
        start = System.currentTimeMillis();
        int result2 = minStep2(a3, b3);
        long time2 = System.currentTimeMillis() - start;
        
        System.out.println("数学方法结果: " + result1 + " (用时: " + time1 + "ms)");
        System.out.println("二分方法结果: " + result2 + " (用时: " + time2 + "ms)");
        System.out.println();
        
        // 大规模正确性验证
        System.out.println("=== 正确性验证 ===");
        System.out.println("开始验证...");
        boolean passed = true;
        
        for (int a = 1; a < 100 && passed; a++) {
            for (int b = 1; b < 100 && passed; b++) {
                int ans1 = minStep0(a, b);
                int ans2 = minStep1(a, b);
                int ans3 = minStep2(a, b);
                if (ans1 != ans2 || ans1 != ans3) {
                    System.out.println("测试失败: a=" + a + ", b=" + b);
                    System.out.println("暴力方法: " + ans1);
                    System.out.println("数学方法: " + ans2);  
                    System.out.println("二分方法: " + ans3);
                    passed = false;
                }
            }
        }
        
        if (passed) {
            System.out.println("所有测试用例通过！");
        }
        
        System.out.println("\n=== 算法原理解析 ===");
        System.out.println("1. 问题本质：");
        System.out.println("   - 将数字1到n分配给两个数，使它们相等");
        System.out.println("   - 等价于在数轴上通过添加正整数使两点重合");
        System.out.println();
        System.out.println("2. 数学公式：");
        System.out.println("   - 差值s = |a - b|");
        System.out.println("   - 总和sum = n*(n+1)/2");
        System.out.println("   - 条件：sum >= s 且 (sum - s) % 2 == 0");
        System.out.println();
        System.out.println("3. 奇偶性分析：");
        System.out.println("   - sum和s必须有相同的奇偶性");
        System.out.println("   - 如果不同，需要增加步数直到满足条件");
        System.out.println("   - 由于sum增长模式，最多增加3步");
        System.out.println();
        System.out.println("4. 复杂度对比：");
        System.out.println("   - 暴力方法：O(2^n)，只适合小规模验证");
        System.out.println("   - 数学方法：O(√s)，适合中等规模");
        System.out.println("   - 二分方法：O(log s)，适合大规模问题");
        System.out.println();
        System.out.println("5. 实际应用：");
        System.out.println("   - 负载均衡：将任务分配到两个服务器使负载相等");
        System.out.println("   - 游戏平衡：通过道具分配平衡双方实力");
        System.out.println("   - 资源调度：在有序资源分配中的优化问题");
    }
}
