package base.bit;

/**
 * 位运算实现基本算术运算
 * 使用位运算实现加法、减法、乘法、除法
 * 主要用于理解计算机底层运算原理
 */
public class BitCal {

    /**
     * 位运算实现加法
     * 核心思想：
     * 1. a ^ b 计算无进位相加结果
     * 2. (a & b) << 1 计算进位
     * 3. 重复上述过程直到进位为0
     * 
     * @param a 加数1
     * @param b 加数2
     * @return a + b的结果
     */
    public static int add(int a, int b) {
        int sum = a;
        while (b != 0) {
            sum = a ^ b;        // 无进位相加
            b = (a & b) << 1;   // 计算进位
            a = sum;            // 更新a为当前结果
        }
        return sum;
    }

    /**
     * 求一个数的相反数（负数）
     * 使用补码：~n + 1
     * @param n 原数
     * @return -n
     */
    private static int negNum(int n) {
        return add(~n, 1);
    }

    /**
     * 位运算实现减法
     * a - b = a + (-b)
     * @param a 被减数
     * @param b 减数
     * @return a - b的结果
     */
    public static int minus(int a, int b) {
        return add(a, negNum(b));
    }

    /**
     * 位运算实现乘法
     * 核心思想：模拟二进制乘法过程
     * 对于b的每一位，如果为1，则将对应位权的a累加到结果中
     * 
     * @param a 乘数1
     * @param b 乘数2
     * @return a * b的结果
     */
    public static int multi(int a, int b) {
        int res = 0;
        while (b != 0) {
            if ((b & 1) != 0) {     // 检查b的最低位是否为1
                res = add(res, a);   // 如果为1，累加当前a值
            }
            a <<= 1;                // a左移一位（相当于*2）
            b >>>= 1;               // b无符号右移一位
        }
        return res;
    }

    /**
     * 判断一个数是否为负数
     * @param n 待判断的数
     * @return 是否为负数
     */
    private static boolean isNeg(int n) {
        return n < 0;
    }

    /**
     * 位运算实现除法（不考虑溢出情况）
     * 核心思想：模拟二进制长除法
     * 从高位到低位，判断被除数是否能减去对应位权的除数
     * 
     * @param a 被除数
     * @param b 除数
     * @return a / b的结果
     */
    public static int div(int a, int b) {
        int x = isNeg(a) ? negNum(a) : a;   // 被除数取绝对值
        int y = isNeg(b) ? negNum(b) : b;   // 除数取绝对值
        int res = 0;
        // 从最高位开始尝试
        for (int i = 30; i >= 0; i = minus(i, 1)) {
            if ((x >> i) >= y) {            // 如果当前位能够整除
                res |= (1 << i);            // 在结果对应位置1
                x = minus(x, y << i);       // 减去对应的值
            }
        }
        // 根据原始符号确定结果符号
        return isNeg(a) ^ isNeg(b) ? negNum(res) : res;
    }

    /**
     * 考虑溢出的除法实现
     * 处理Integer.MIN_VALUE的特殊情况
     * 
     * @param a 被除数
     * @param b 除数
     * @return a / b的结果
     */
    public static int divide(int a, int b) {
        if (a == Integer.MIN_VALUE && b == Integer.MIN_VALUE) {
            return 1;   // 相等的最小值相除
        } else if (b == Integer.MIN_VALUE) {
            return 0;   // 除数为最小值时，结果只能是0
        } else if (a == Integer.MIN_VALUE) {
            // 被除数为最小值的特殊处理
            // 因为Integer.MIN_VALUE取绝对值会溢出
            int c = div(add(a, 1), b);              // 先用(a+1)/b
            return add(c, div(minus(a, multi(c, b)), b));   // 再加上余数部分的除法
        } else {
            return div(a, b);   // 普通情况
        }
    }

}
