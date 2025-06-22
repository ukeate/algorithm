package base.xor;

/**
 * 统计一个整数二进制表示中1的个数
 * 使用Brian Kernighan算法，通过位运算快速统计
 * 时间复杂度：O(k)，其中k是二进制表示中1的个数
 */
public class Count1 {
    /**
     * 统计整数n的二进制表示中1的个数
     * 核心思想：n & (n-1)可以消除n的最右边的1
     * 例如：n=12(1100), n-1=11(1011), n&(n-1)=8(1000)，消除了最右边的1
     * 
     * 这里使用的是提取最右边1的技巧：
     * rightOne = n & ((~n) + 1) = n & (-n)
     * 这会得到n的最右边的1所在的位，然后用异或消除这个1
     * 
     * @param n 要统计的整数
     * @return 二进制表示中1的个数
     */
    public static int count(int n) {
        int count = 0;
        while (n != 0) {
            // 提取最右边的1：n & ((~n) + 1) 等价于 n & (-n)
            // ~n表示n的按位取反，(~n) + 1表示n的补码，即-n
            // n & (-n)会保留n的最右边的1，其他位都变为0
            int rightOne = n & ((~n) + 1);
            count++;           // 找到一个1，计数器加1
            n ^= rightOne;     // 用异或消除这个1：n = n ^ rightOne
        }
        return count;
    }
}
