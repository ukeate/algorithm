package leetc.top;

/**
 * LeetCode 29. 两数相除 (Divide Two Integers)
 * 
 * 问题描述：
 * 给定两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
 * 返回被除数 dividend 除以除数 divisor 得到的商。
 * 整数除法的结果应当截去（truncate）其小数部分，例如：truncate(8.345) = 8 以及 truncate(-2.7335) = -2
 * 
 * 假设我们的环境只能存储32位有符号整数，其数值范围是 [−2³¹, 2³¹ − 1]。
 * 本题中，如果除法结果溢出，则返回 2³¹ − 1。
 * 
 * 解法思路：
 * 位运算模拟除法：
 * 1. 不能使用乘除法，所以用位运算实现基本算术运算
 * 2. 加法：使用异或和进位模拟
 * 3. 减法：加法 + 取反
 * 4. 乘法：移位累加
 * 5. 除法：二进制长除法，从高位到低位试商
 * 
 * 核心算法：
 * - 将所有运算转化为位运算
 * - 除法通过移位和减法实现：从最高位开始，判断被除数能否减去除数的2^i倍
 * - 特殊处理溢出情况和负数边界
 * 
 * 时间复杂度：O(log n) - n为被除数的位数
 * 空间复杂度：O(1) - 只使用常数额外空间
 * 
 * LeetCode链接：https://leetcode.com/problems/divide-two-integers/
 */
public class P29_DivideTwoIntegers {
    /**
     * 实现加法运算（不使用+操作符）
     * 使用位运算：异或实现无进位加法，与运算+左移实现进位
     * 
     * @param a 加数1
     * @param b 加数2
     * @return a + b的结果
     */
    private static int add(int a, int b) {
        int sum = a;
        while (b != 0) {
            sum = a ^ b;        // 异或得到无进位加法结果
            b = (a & b) << 1;   // 与运算+左移得到进位
            a = sum;            // 更新a为当前和
        }
        return sum;
    }

    /**
     * 实现取负运算（不使用-操作符）
     * 使用补码原理：~n + 1 = -n
     * 
     * @param n 原数
     * @return -n
     */
    private static int negNum(int n) {
        return add(~n, 1);  // 按位取反加1得到相反数
    }

    /**
     * 实现减法运算（不使用-操作符）
     * 减法转化为加法：a - b = a + (-b)
     * 
     * @param a 被减数
     * @param b 减数
     * @return a - b的结果
     */
    private static int minus(int a, int b) {
        return add(a, negNum(b));
    }

    /**
     * 实现乘法运算（不使用*操作符）
     * 使用移位和加法：基于二进制位的乘法累加
     * 
     * @param a 乘数1
     * @param b 乘数2
     * @return a * b的结果
     */
    private static int multi(int a, int b) {
        int ans = 0;
        while (b != 0) {
            if ((b & 1) != 0) {  // 如果b的最低位是1
                ans = add(ans, a);  // 累加当前的a
            }
            a <<= 1;    // a左移1位（相当于乘以2）
            b >>>= 1;   // b无符号右移1位
        }
        return ans;
    }

    /**
     * 判断数字是否为负数
     * 
     * @param n 待判断的数字
     * @return 是否为负数
     */
    private static boolean isNeg(int n) {
        return n < 0;
    }

    /**
     * 实现除法运算的核心算法（不使用/操作符）
     * 使用二进制长除法：从高位到低位试商
     * 
     * @param a 被除数（已转为正数）
     * @param b 除数（已转为正数）
     * @return a / b的结果
     */
    private static int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a;  // 转为正数
        int y = isNeg(b) ? negNum(b) : b;  // 转为正数
        int res = 0;
        
        // 从第30位开始试商（最高位是符号位）
        for (int i = 30; i >= 0; i = minus(i, 1)) {
            // 如果x >= y * 2^i，说明商的第i位是1
            if ((x >> i) >= y) {
                res |= (1 << i);          // 设置商的第i位为1
                x = minus(x, y << i);     // 从被除数中减去y * 2^i
            }
        }
        
        // 根据原始符号确定结果符号
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }

    /**
     * 两数相除的主方法
     * 处理特殊情况和溢出
     * 
     * @param dividend 被除数
     * @param divisor 除数
     * @return 除法结果，溢出时返回Integer.MAX_VALUE
     */
    public static int divide(int dividend, int divisor) {
        // 特殊情况1：除数是Integer.MIN_VALUE
        if (divisor == Integer.MIN_VALUE) {
            return dividend == Integer.MIN_VALUE ? 1 : 0;
        }
        
        // 特殊情况2：被除数是Integer.MIN_VALUE
        if (dividend == Integer.MIN_VALUE) {
            if (divisor == negNum(1)) {  // 除数是-1
                return Integer.MAX_VALUE;  // 溢出，返回最大值
            }
            // 避免溢出的技巧：先加1再除，然后补偿
            int res = div(add(dividend, 1), divisor);
            return add(res, div(minus(dividend, multi(res, divisor)), divisor));
        }
        
        // 正常情况：调用核心除法算法
        return div(dividend, divisor);
    }

    /**
     * 辅助方法：将数字转换为二进制字符串表示
     * 
     * @param num 要转换的数字
     * @return 32位二进制字符串
     */
    private static String printNumBinary(int num) {
        StringBuilder builder = new StringBuilder();
        for (int i = 31; i >= 0; i--) {
            builder.append(((num >> i) & 1) == 0 ? '0' : '1');
        }
        return builder.toString();
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println(divide(Integer.MIN_VALUE, -3));
    }
}
