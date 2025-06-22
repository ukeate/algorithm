package base.direct;

/**
 * 判断一个数是否可以表示为连续正整数的和
 * 
 * 问题：给定一个正整数num，判断是否可以表示为至少2个连续正整数的和
 * 
 * 解题思路：
 * 1. 暴力解法：枚举所有可能的起始位置和长度
 * 2. 规律解法：一个数能表示为连续整数和 ⟺ 该数不是2的幂
 * 
 * 数学原理：
 * - 如果num = a + (a+1) + ... + (a+k-1) = k*a + k*(k-1)/2
 * - 整理得：num = k*(2a+k-1)/2
 * - 当k为奇数时，num必须是k的倍数
 * - 当k为偶数时，num必须是k/2的倍数且为奇数
 * - 综合分析：2的幂无法表示为连续整数和，其他数都可以
 */
public class IsContinuousSum {
    /**
     * 暴力解法：枚举所有可能的连续序列
     * 时间复杂度：O(n²)
     * 
     * @param num 待判断的数
     * @return 如果能表示为连续整数和返回true，否则返回false
     */
    public static boolean is1(int num) {
        // 枚举起始位置i
        for (int i = 1; i <= num; i++) {
            int sum = i;
            // 从位置i开始累加连续整数
            for (int j = i + 1; j <= num; j++) {
                if (sum + j > num) {
                    break; // 和超过目标，跳出
                }
                if (sum + j == num) {
                    return true; // 找到连续序列
                }
                sum += j;
            }
        }
        return false;
    }

    /**
     * 规律解法：基于数学规律的O(1)算法
     * 
     * 核心规律：一个正整数能表示为连续整数和 ⟺ 它不是2的幂
     * 
     * 证明思路：
     * 1. 2的幂的二进制表示只有一个1，无法分解为连续整数和
     * 2. 非2的幂都有奇因子，可以通过奇因子构造连续序列
     * 
     * 位运算技巧：
     * - num & (num - 1) == 0 表示num是2的幂
     * - num & (num - 1) != 0 表示num不是2的幂
     * 
     * @param num 待判断的数
     * @return 如果能表示为连续整数和返回true，否则返回false
     */
    public static boolean is2(int num) {
        // 方法1：直接判断是否为2的幂
        // return num == (num & -num);  // 这个也能判断2的幂，但逻辑相反
        
        // 方法2：更直观的判断
        return (num & (num - 1)) != 0;
    }

    /**
     * 测试方法：验证规律的正确性
     */
    public static void main(String[] args) {
        // 先打印前200个数的结果，观察规律
        for (int num = 1; num < 200; num++) {
            System.out.println(num + ":" + is1(num));
        }
        
        System.out.println("test begin");
        // 验证两种方法的一致性
        for (int num = 1; num < 5000; num++) {
            if (is1(num) != is2(num)) {
                System.out.println("Wrong");
            }
        }
        System.out.println("test end");
    }
}
