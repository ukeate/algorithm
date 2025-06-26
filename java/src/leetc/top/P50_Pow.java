package leetc.top;

/**
 * LeetCode 50. Pow(x, n)
 * 
 * 问题描述：
 * 实现 pow(x, n)，即计算 x 的 n 次幂函数（即 x^n）。
 * 
 * 示例：
 * - 输入：x = 2.00000, n = 10，输出：1024.00000
 * - 输入：x = 2.10000, n = 3，输出：9.26100
 * - 输入：x = 2.00000, n = -2，输出：0.25000
 * 
 * 解法思路：
 * 快速幂算法（二进制幂）：
 * 1. 将指数n转换为二进制表示
 * 2. 对于n的每一位，如果是1，就将当前的base累乘到结果中
 * 3. 每次循环base自身平方，对应二进制位的左移
 * 4. 时间复杂度从O(n)优化到O(log n)
 * 
 * 核心思想：
 * x^n = x^(b_k * 2^k + b_(k-1) * 2^(k-1) + ... + b_1 * 2^1 + b_0 * 2^0)
 *     = (x^(2^k))^b_k * (x^(2^(k-1)))^b_(k-1) * ... * (x^(2^1))^b_1 * (x^(2^0))^b_0
 * 
 * 特殊情况处理：
 * - n = 0：结果为1
 * - n = Integer.MIN_VALUE：需要特殊处理避免溢出
 * - n < 0：计算x^|n|，然后取倒数
 * 
 * 时间复杂度：O(log n) - 指数的二进制位数
 * 空间复杂度：O(1) - 只使用常数额外空间
 */
public class P50_Pow {
    
    /**
     * 快速幂算法实现（方法1）
     * 
     * 算法步骤：
     * 1. 处理特殊情况：n=0, n=Integer.MIN_VALUE
     * 2. 获取指数的绝对值
     * 3. 使用快速幂算法计算x^|n|
     * 4. 根据n的正负性返回结果或倒数
     * 
     * @param x 底数
     * @param n 指数
     * @return x的n次幂
     */
    public static double myPow1(double x, int n) {
        // 特殊情况：任何数的0次幂都是1
        if (n == 0) {
            return 1D;
        }
        
        // 特殊情况：Integer.MIN_VALUE取绝对值会溢出
        if (n == Integer.MIN_VALUE) {
            // 只有x=1或x=-1时，结果才不会是0
            return (x == 1D || x == -1D) ? 1D : 0;
        }
        
        int pow = Math.abs(n);  // 指数的绝对值
        double t = x;           // 当前的底数（会不断平方）
        double ans = 1D;        // 累积结果
        
        // 快速幂算法：遍历指数的每一个二进制位
        while (pow != 0) {
            // 如果当前二进制位是1，将当前底数累乘到结果中
            if ((pow & 1) != 0) {
                ans *= t;
            }
            pow >>= 1;  // 指数右移一位
            t = t * t;  // 底数平方，对应二进制位的左移
        }
        
        // 根据原指数的正负性返回结果
        return n < 0 ? (1D / ans) : ans;
    }

    /**
     * 快速幂算法实现（方法2 - 优化Integer.MIN_VALUE处理）
     * 
     * 改进点：
     * 更优雅地处理Integer.MIN_VALUE的情况，避免直接返回0
     * 
     * @param x 底数
     * @param n 指数
     * @return x的n次幂
     */
    public static double myPow2(double x, int n) {
        // 特殊情况：任何数的0次幂都是1
        if (n == 0) {
            return 1D;
        }
        
        // 处理Integer.MIN_VALUE：先计算|n|-1，最后再乘一次x
        int pow = Math.abs(n == Integer.MIN_VALUE ? n + 1 : n);
        double t = x;           // 当前的底数（会不断平方）
        double ans = 1D;        // 累积结果
        
        // 快速幂算法
        while (pow != 0) {
            if ((pow & 1) != 0) {
                ans *= t;
            }
            pow >>= 1;
            t = t * t;
        }
        
        // 如果原来是Integer.MIN_VALUE，需要额外乘一次x
        if (n == Integer.MIN_VALUE) {
            ans *= x;
        }
        
        // 根据原指数的正负性返回结果
        return n < 0 ? (1D / ans) : ans;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例
        System.out.println("=== 测试 myPow1 ===");
        System.out.println("2^10 = " + myPow1(2.0, 10));           // 1024.0
        System.out.println("2.1^3 = " + myPow1(2.1, 3));          // 9.261
        System.out.println("2^(-2) = " + myPow1(2.0, -2));        // 0.25
        System.out.println("1^MIN_VALUE = " + myPow1(1.0, Integer.MIN_VALUE)); // 1.0
        
        System.out.println("\n=== 测试 myPow2 ===");
        System.out.println("2^10 = " + myPow2(2.0, 10));           // 1024.0
        System.out.println("2.1^3 = " + myPow2(2.1, 3));          // 9.261
        System.out.println("2^(-2) = " + myPow2(2.0, -2));        // 0.25
        System.out.println("1^MIN_VALUE = " + myPow2(1.0, Integer.MIN_VALUE)); // 1.0
        
        System.out.println("\n=== 与系统pow函数对比 ===");
        System.out.println("系统pow(1, MIN_VALUE) = " + Math.pow(1.0, Integer.MIN_VALUE));
        System.out.println("myPow1(1, MIN_VALUE) = " + myPow1(1.0, Integer.MIN_VALUE));
        System.out.println("myPow2(1, MIN_VALUE) = " + myPow2(1.0, Integer.MIN_VALUE));
        
        // 测试边界情况
        System.out.println("\n=== 边界情况测试 ===");
        System.out.println("0^0 = " + myPow1(0.0, 0));             // 1.0
        System.out.println("(-1)^3 = " + myPow1(-1.0, 3));         // -1.0
        System.out.println("(-1)^4 = " + myPow1(-1.0, 4));         // 1.0
    }
}
