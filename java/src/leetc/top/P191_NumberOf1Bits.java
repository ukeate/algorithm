package leetc.top;

/**
 * LeetCode 191. 位1的个数 (Number of 1 Bits)
 * 
 * 问题描述：
 * 编写一个函数，输入是一个无符号整数（以二进制串的形式），
 * 返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
 * 
 * 提示：
 * - 输入必须是长度为 32 的二进制串。
 * - 进阶：如果多次调用这个函数，你将如何优化你的算法？
 * 
 * 示例：
 * 输入：n = 00000000000000000000000000001011
 * 输出：3
 * 解释：输入的二进制串有三个 '1'。
 * 
 * 解法思路：
 * 本类提供了两种不同的解法：
 * 
 * 方法1：Brian Kernighan算法
 * - 核心思想：n & (n-1) 能消除n的最右边的1
 * - 每次操作都去掉一个1，直到n变为0
 * - 操作次数等于1的个数
 * 
 * 方法2：并行计算法（分治思想）
 * - 将32位分成更小的段，并行计算每段的1的个数
 * - 类似于归并排序的合并过程
 * - 通过位运算实现高效的并行计算
 * 
 * 两种方法的比较：
 * - 方法1：时间复杂度O(k)，k为1的个数，适合稀疏的情况
 * - 方法2：时间复杂度O(1)，固定的计算步数，适合密集的情况
 * 
 * LeetCode链接：https://leetcode.com/problems/number-of-1-bits/
 */
public class P191_NumberOf1Bits {
    
    /**
     * 方法1：Brian Kernighan算法统计1的个数
     * 
     * 算法原理：
     * - n & (n-1)：消除n的最右边的1
     * - 例如：n=12(1100), n-1=11(1011), n&(n-1)=8(1000)
     * - 每次操作去掉一个1，循环次数等于1的个数
     * 
     * 关键技巧：
     * - rightOne = n & (-n)：提取最右边的1
     * - n ^= rightOne：消除最右边的1
     * - 等价于 n = n & (n-1)
     * 
     * @param n 32位无符号整数
     * @return 二进制表示中1的个数
     */
    public static int hammingWeight1(int n) {
        int bits = 0;      // 1的个数计数器
        int rightOne = 0;  // 最右边的1
        
        while (n != 0) {
            bits++;                    // 计数器递增
            rightOne = n & (-n);       // 提取最右边的1
            n ^= rightOne;             // 消除最右边的1
        }
        
        return bits;
    }

    /**
     * 方法2：并行计算法统计1的个数
     * 
     * 算法原理：
     * 1. 将相邻的1位相加，得到16个2位的和
     * 2. 将相邻的2位相加，得到8个4位的和
     * 3. 将相邻的4位相加，得到4个8位的和
     * 4. 将相邻的8位相加，得到2个16位的和
     * 5. 将相邻的16位相加，得到1个32位的和
     * 
     * 分步解析：
     * 第1步：0x55555555 = 01010101..., 将奇偶位分别提取并相加
     * 第2步：0x33333333 = 00110011..., 将相邻2位相加
     * 第3步：0x0f0f0f0f = 00001111..., 将相邻4位相加
     * 第4步：0x00ff00ff = 00000000111111110000000011111111, 将相邻8位相加
     * 第5步：0x0000ffff = 前16位清零，将相邻16位相加
     * 
     * 掩码作用：
     * - 提取特定位置的位段
     * - 确保加法运算不会产生进位干扰
     * - 实现并行的位段加法
     * 
     * @param n 32位无符号整数
     * @return 二进制表示中1的个数
     */
    public static int hammingWeight2(int n) {
        // 第1步：相邻1位相加 -> 得到16个2位计数
        n = (n & 0x55555555) + ((n >>> 1) & 0x55555555);
        
        // 第2步：相邻2位相加 -> 得到8个4位计数
        n = (n & 0x33333333) + ((n >>> 2) & 0x33333333);
        
        // 第3步：相邻4位相加 -> 得到4个8位计数
        n = (n & 0x0f0f0f0f) + ((n >>> 4) & 0x0f0f0f0f);
        
        // 第4步：相邻8位相加 -> 得到2个16位计数
        n = (n & 0x00ff00ff) + ((n >>> 8) & 0x00ff00ff);
        
        // 第5步：相邻16位相加 -> 得到1个32位计数
        n = (n & 0x0000ffff) + ((n >>> 16) & 0x0000ffff);
        
        return n;
    }
}
