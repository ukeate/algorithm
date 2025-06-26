package basic.c38;

/**
 * 数字1的出现次数问题
 * 
 * 问题描述：
 * 给定一个整数n，计算从1到n的所有整数中数字1出现的次数
 * 例如：n=13时，数字1在1,10,11,12,13中出现，
 * 具体为：1(1次)，10(1次)，11(2次)，12(1次)，13(1次)，总共6次
 * 
 * 算法思路：
 * 方法1：暴力枚举 - 对每个数字统计1的个数，时间复杂度O(n*logn)
 * 方法2：数位DP - 利用数学规律按位分析，时间复杂度O(logn)
 * 
 * 数位DP核心思想：
 * 对于一个k位数，分析第i位为1时的情况数，
 * 需要考虑高位、当前位、低位的组合情况
 * 
 * @author 算法学习
 */
public class OneNum {
    
    /**
     * 计算单个数字中1的出现次数
     * 
     * @param num 输入数字
     * @return 数字num中1的出现次数
     * 
     * 时间复杂度：O(logn)
     */
    private static int count1(int num) {
        int res = 0;
        while (num > 0) {
            if (num % 10 == 1) {
                res++;
            }
            num /= 10;
        }
        return res;
    }

    /**
     * 方法1：暴力解法
     * 逐个统计每个数字中1的出现次数
     * 
     * @param num 上界数字
     * @return [1,num]范围内所有数字中1的总出现次数
     * 
     * 时间复杂度：O(n*logn)
     * 空间复杂度：O(1)
     */
    public static int num1(int num) {
        if (num < 1) {
            return 0;
        }
        
        int count = 0;
        for (int i = 1; i <= num; i++) {
            count += count1(i);
        }
        return count;
    }

    /**
     * 计算数字的位数
     * 
     * @param num 输入数字
     * @return 数字的位数
     */
    private static int len(int num) {
        int len = 0;
        while (num > 0) {
            len++;
            num /= 10;
        }
        return len;
    }

    /**
     * 计算10的指定次幂
     * 
     * @param base 指数
     * @return 10^base
     */
    private static int power10(int base) {
        return (int) Math.pow(10, base);
    }

    /**
     * 方法2：数位DP优化解法
     * 利用数学规律分析每一位上1的出现次数
     * 
     * @param num 上界数字
     * @return [1,num]范围内所有数字中1的总出现次数
     * 
     * 算法思路：
     * 对于数字num，分析最高位和其他位：
     * 1. 最高位为1的情况数
     * 2. 其他位为1的情况数  
     * 3. 递归处理剩余部分
     * 
     * 时间复杂度：O(logn)
     * 空间复杂度：O(logn) - 递归栈深度
     */
    public static int num2(int num) {
        if (num < 1) {
            return 0;
        }
        
        int len = len(num);
        // 单位数直接返回1（只有数字1）
        if (len == 1) {
            return 1;
        }
        
        // 最高位的权重，如1234的最高位权重是1000
        int tmp1 = power10(len - 1);
        // 最高位数字
        int first = num / tmp1;
        
        // 计算最高位为1的情况数
        // 如果最高位就是1，那么范围是[1000, num]，共(num%tmp1 + 1)个
        // 如果最高位大于1，那么范围是[1000, 1999]，共tmp1个
        int firstOneNum = first == 1 ? num % tmp1 + 1 : tmp1;
        
        // 计算其他位为1的情况数
        // 对于k位数，除最高位外每一位都有first * (k-1) * 10^(k-2)种情况
        // 这里first是最高位的取值范围[1, first]
        int otherOneNum = first * (len - 1) * (tmp1 / 10);
        
        // 递归处理剩余的低位部分
        return firstOneNum + otherOneNum + num2(num % tmp1);
    }

    /**
     * 测试方法：对比两种算法的性能
     */
    public static void main(String[] args) {
        int num = 50000000;
        
        // 测试暴力方法（注意：大数据量时会很慢）
        long start = System.currentTimeMillis();
        System.out.println("暴力方法结果: " + num1(num));
        long end = System.currentTimeMillis();
        System.out.println("暴力方法耗时: " + (end - start) + " ms");

        // 测试数位DP方法
        start = System.currentTimeMillis();
        System.out.println("数位DP结果: " + num2(num));
        end = System.currentTimeMillis();
        System.out.println("数位DP耗时: " + (end - start) + " ms");
        
        // 验证小数据的正确性
        System.out.println("\n验证小数据:");
        for (int i = 1; i <= 20; i++) {
            int result1 = num1(i);
            int result2 = num2(i);
            System.out.println("n=" + i + ", 暴力=" + result1 + ", DP=" + result2 + 
                             (result1 == result2 ? " ✓" : " ✗"));
        }
    }
}
