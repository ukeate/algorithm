package giant.c19;

/**
 * 数字1出现次数统计问题
 * 
 * 问题描述：
 * 给定一个正整数n，计算从1到n的所有整数中，数字1总共出现了多少次。
 * 
 * 例如：
 * n = 13时，1到13的数字为：1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13
 * 数字1出现在：1(1次), 10(1次), 11(2次), 12(1次), 13(1次)
 * 总共出现6次
 * 
 * n = 100时，数字1出现21次：
 * 个位：1, 11, 21, 31, 41, 51, 61, 71, 81, 91 (10次)
 * 十位：10, 11, 12, 13, 14, 15, 16, 17, 18, 19 (10次)
 * 百位：100 (1次)
 * 总共21次
 * 
 * LeetCode链接：https://leetcode.cn/problems/1nzheng-shu-zhong-1chu-xian-de-ci-shu-lcof/
 * 
 * 解决方案：
 * 1. 方法1：暴力法 - 逐个检查每个数字，时间复杂度O(n*logn)
 * 2. 方法2：数学归纳法 - 按位分析，时间复杂度O(logn)
 * 
 * 核心思想：
 * 数学归纳法通过分析每一位上1出现的规律，递归计算总数
 * 
 * 算法复杂度：
 * 时间复杂度：O(logn)
 * 空间复杂度：O(logn)
 */
public class OneNumber {
    
    /**
     * 统计一个数字中1的出现次数（暴力法辅助函数）
     * 
     * @param num 待统计的数字
     * @return 数字num中1出现的次数
     */
    private static int get1Nums(int num) {
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
     * 方法1：暴力枚举法
     * 
     * 算法思路：
     * 从1到n逐个遍历每个数字，统计每个数字中1的出现次数，然后累加
     * 
     * 算法特点：
     * - 思路简单直观，易于理解和实现
     * - 时间复杂度较高，不适用于大数据
     * - 适用于验证结果正确性
     * 
     * 时间复杂度：O(n*logn)，n个数字，每个数字需要O(logn)时间统计1的个数
     * 空间复杂度：O(1)
     * 
     * @param num 上限数字
     * @return 从1到num中数字1出现的总次数
     */
    public static int sure(int num) {
        if (num < 1) {
            return 0;
        }
        
        int count = 0;
        for (int i = 1; i <= num; i++) {
            count += get1Nums(i);
        }
        return count;
    }

    /**
     * 计算数字的位数（方法2使用）
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
     * 计算10的幂次（方法2使用）
     * 
     * @param base 指数
     * @return 10^base
     */
    private static int power10(int base) {
        return (int) Math.pow(10, base);
    }

    /**
     * 方法2：数学归纳法（高效算法）
     * 
     * 算法思路：
     * 以数字21345为例，分析最高位上1出现的次数：
     * 1. 如果最高位 = 1：1出现在1xxxx格式中，范围是10000~21345，共(21345-10000+1)次
     * 2. 如果最高位 > 1：1出现在1xxxx格式中，范围是10000~19999，共10000次
     * 3. 其他位的1：最高位每取一个值(0,1,2)，其他位的1出现次数相同，共3*(len-1)*1000次
     * 4. 递归处理剩余部分：countDigitOne(1345)
     * 
     * 详细分析：
     * 对于数字num，位数为len，最高位数字为first：
     * - tmp1 = 10^(len-1)：最高位权重
     * - first = num / tmp1：最高位数字
     * - firstOneNum：最高位为1时的出现次数
     *   * 如果first == 1：num % tmp1 + 1
     *   * 如果first > 1：tmp1
     * - otherOneNum：其他位的1出现次数 = first * (len-1) * (tmp1/10)
     *   * first：最高位可取值个数
     *   * (len-1)：其他位的位数
     *   * (tmp1/10)：每个其他位上1出现的基础次数
     * 
     * 递推公式：
     * countDigitOne(num) = firstOneNum + otherOneNum + countDigitOne(num % tmp1)
     * 
     * 时间复杂度：O(logn)，递归深度等于数字位数
     * 空间复杂度：O(logn)，递归调用栈
     * 
     * @param num 上限数字
     * @return 从1到num中数字1出现的总次数
     */
    public static int countDigitOne(int num) {
        if (num < 1) {
            return 0;
        }
        
        int len = len(num);  // 获取数字位数
        if (len == 1) {
            return 1;  // 单位数情况，只有1本身包含1
        }
        
        int tmp1 = power10(len - 1);   // 最高位的权重，如21345中的10000
        int first = num / tmp1;        // 最高位数字，如21345中的2
        
        // 计算最高位为1时的出现次数
        int firstOneNum = first == 1 ? num % tmp1 + 1 : tmp1;
        
        // 计算其他位上1的出现次数
        // first表示最高位可以取0,1,2...first-1
        // (len-1)表示除最高位外的位数
        // (tmp1/10)表示在固定最高位的情况下，每个其他位上1出现的次数
        int otherOneNum = first * (len - 1) * (tmp1 / 10);
        
        // 递归处理剩余部分
        return firstOneNum + otherOneNum + countDigitOne(num % tmp1);
    }

    /**
     * 测试方法：验证两种算法的正确性和性能
     */
    public static void main(String[] args) {
        System.out.println("=== 数字1出现次数统计问题测试 ===");
        
        // 小规模测试用例，两种方法都可以快速验证
        System.out.println("1. 小规模测试用例对比:");
        int[] testCases = {1, 10, 13, 21, 100, 234, 1000};
        
        for (int test : testCases) {
            int result1 = sure(test);
            int result2 = countDigitOne(test);
            System.out.printf("n = %4d: 暴力法 = %3d, 数学法 = %3d, %s%n", 
                test, result1, result2, result1 == result2 ? "✓" : "✗");
        }
        System.out.println();
        
        // 性能测试：大数值情况
        System.out.println("2. 大数值性能测试:");
        int[] largeCases = {10000, 100000, 1000000};
        
        for (int test : largeCases) {
            long start = System.currentTimeMillis();
            int result = countDigitOne(test);
            long time = System.currentTimeMillis() - start;
            System.out.printf("n = %7d: 结果 = %7d, 耗时 = %2d ms%n", test, result, time);
        }
        System.out.println();
        
        // 边界情况测试
        System.out.println("3. 边界情况测试:");
        System.out.println("n = 0: " + countDigitOne(0) + " (期望: 0)");
        System.out.println("n = 1: " + countDigitOne(1) + " (期望: 1)");
        System.out.println("n = 9: " + countDigitOne(9) + " (期望: 1)");
        System.out.println("n = 11: " + countDigitOne(11) + " (期望: 4)");
        System.out.println("n = 111: " + countDigitOne(111) + " (期望: 33)");
        
        System.out.println();
        
        // 手动验证小案例
        System.out.println("4. 手动验证 n = 13:");
        System.out.println("数字序列: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13");
        System.out.println("包含1的: 1(1个1), 10(1个1), 11(2个1), 12(1个1), 13(1个1)");
        System.out.println("总计: 1+1+2+1+1 = 6");
        System.out.println("算法结果: " + countDigitOne(13));
        
        System.out.println("\n=== 测试完成 ===");
    }
}
